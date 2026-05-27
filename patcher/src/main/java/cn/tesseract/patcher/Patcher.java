package cn.tesseract.patcher;

import net.fabricmc.mappingio.format.enigma.EnigmaDirReader;
import net.fabricmc.mappingio.format.enigma.EnigmaDirWriter;
import net.fabricmc.mappingio.tree.MemoryMappingTree;
import net.fabricmc.tinyremapper.OutputConsumerPath;
import net.fabricmc.tinyremapper.TinyRemapper;
import net.fabricmc.tinyremapper.TinyUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.stream.Stream;

public class Patcher {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: Patcher <input> <output> [--platform android|desktop] [--remap-ids <rFile>] [--mappings-dir <mappingsDir>]");
            System.exit(1);
        }

        Path inputJar = Paths.get(args[0]);
        Path outputJar = Paths.get(args[1]);

        Platform platform = Platform.ANDROID;
        Path rFile = null;
        Path mappingsDir = null;
        for (int i = 2; i < args.length; i++) {
            if ("--platform".equals(args[i]) && i + 1 < args.length) {
                platform = Platform.valueOf(args[++i].trim().toUpperCase(Locale.ROOT));
            } else if ("--remap-ids".equals(args[i]) && i + 1 < args.length) {
                rFile = Paths.get(args[++i]);
            } else if ("--mappings-dir".equals(args[i]) && i + 1 < args.length) {
                mappingsDir = Paths.get(args[++i]);
            }
        }

        List<Patch> patches = new ArrayList<>();
        if (platform == Platform.ANDROID) {
            patches.add(new Dex2JarFixPatch());
            if (rFile != null) {
                patches.add(new ResourceIdRemapPatch(ResourceIdRemapPatch.buildIdMapFromJar(rFile, inputJar)));
            }
        }

        int total = 0;
        int patched = 0;
        assert mappingsDir != null;
        Path stagedOutputJar = Paths.get(outputJar.toString().replace(".jar", "-staged.jar"));

        System.out.println("Remapping " + platform + " jar with mappings root: " + mappingsDir);
        remapJar(inputJar, stagedOutputJar, mappingsDir, platform);

        try (JarInputStream jis = new JarInputStream(new BufferedInputStream(Files.newInputStream(stagedOutputJar)));
             JarOutputStream jos = new JarOutputStream(new BufferedOutputStream(Files.newOutputStream(outputJar)))) {
            JarEntry entry;
            while ((entry = jis.getNextJarEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }

                byte[] data = readAllBytes(jis);
                String entryName = entry.getName();

                if (entryName.endsWith(".class")) {
                    total++;
                    String className = entryName.substring(0, entryName.length() - ".class".length());
                    for (Patch patch : patches) {
                        byte[] result = patch.transform(className, data);
                        if (result != null) {
                            data = result;
                            patched++;
                            break;
                        }
                    }
                }

                JarEntry out = new JarEntry(entryName);
                out.setTime(entry.getTime());
                jos.putNextEntry(out);
                jos.write(data);
                jos.closeEntry();
            }
        }
        Files.deleteIfExists(stagedOutputJar);

        System.out.println("Done. " + patched + " patched, " + total + " total.");
    }

    private static void remapJar(Path inputJar, Path outputJar, Path mappingsDir, Platform platform) throws IOException {
        MemoryMappingTree androidIntermediary = new MemoryMappingTree();
        EnigmaDirReader.read(mappingsDir.resolve("android"), "source", "target", androidIntermediary);
        MemoryMappingTree androidNamed = new MemoryMappingTree();
        EnigmaDirReader.read(mappingsDir.resolve("android_named"), "source", "target", androidNamed);
        MemoryMappingTree desktopIntermediary = new MemoryMappingTree();
        EnigmaDirReader.read(mappingsDir.resolve("desktop"), "source", "target", desktopIntermediary);
        MemoryMappingTree desktopNamed = new MemoryMappingTree();
        EnigmaDirReader.read(mappingsDir.resolve("desktop_named"), "source", "target", desktopNamed);

        MemoryMappingTree firstNamed, secondNamed;
        if (getLatestModified(mappingsDir.resolve("android_named")) >= getLatestModified(mappingsDir.resolve("desktop_named"))) {
            firstNamed = androidNamed;
            secondNamed = desktopNamed;
        } else {
            firstNamed = desktopNamed;
            secondNamed = androidNamed;
        }

        MemoryMappingTree shared = MappingUtils.buildSharedMappings(androidIntermediary, desktopIntermediary, firstNamed, secondNamed);
        MemoryMappingTree androidMerged = MappingUtils.buildMergedMappings(shared, androidNamed);
        MemoryMappingTree desktopMerged = MappingUtils.buildMergedMappings(shared, desktopNamed);

        EnigmaDirWriter writer = new EnigmaDirWriter(mappingsDir.resolve("android_named"), true);
        androidMerged.accept(writer);
        writer.close();
        writer = new EnigmaDirWriter(mappingsDir.resolve("desktop_named"), true);
        desktopMerged.accept(writer);
        writer.close();

        MemoryMappingTree intermediary, named;
        switch (platform) {
            case ANDROID:
                intermediary = androidIntermediary;
                named = androidMerged;
                break;
            case DESKTOP:
                intermediary = desktopIntermediary;
                named = desktopMerged;
                break;
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }

        MemoryMappingTree mappingTree = MappingUtils.buildNamedMappings(intermediary, named);

        TinyRemapper remapper = TinyRemapper.newRemapper()
                .withMappings(TinyUtils.createMappingProvider(mappingTree, "source", "target"))
                .build();

        try (OutputConsumerPath outputConsumer = new OutputConsumerPath.Builder(outputJar).build()) {
            outputConsumer.addNonClassFiles(inputJar);
            remapper.readInputs(inputJar);
            remapper.apply(outputConsumer);
        } finally {
            remapper.finish();
        }
    }

    private static long getLatestModified(Path dir) throws IOException {
        if (!Files.exists(dir)) return 0;
        final long[] latest = {0};
        try (Stream<Path> stream = Files.walk(dir)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".mapping"))
                    .forEach(p -> {
                        try {
                            long t = Files.getLastModifiedTime(p).toMillis();
                            if (t > latest[0]) latest[0] = t;
                        } catch (IOException ignored) {
                        }
                    });
        }
        return latest[0];
    }

    private enum Platform {
        ANDROID,
        DESKTOP
    }

    public static byte[] readAllBytes(InputStream in) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int n;
        while ((n = in.read(buf)) != -1) bos.write(buf, 0, n);
        return bos.toByteArray();
    }
}
