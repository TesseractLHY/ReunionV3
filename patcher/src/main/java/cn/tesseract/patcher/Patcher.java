package cn.tesseract.patcher;

import net.fabricmc.mappingio.format.enigma.EnigmaDirReader;
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

import cn.tesseract.patcher.patches.Dex2JarFixPatch;
import cn.tesseract.patcher.patches.ResourceIdRemapPatch;

public class Patcher {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: Patcher <input> <output> [--platform android|desktop] [--remap-ids <rFile>] [--enigma-dir <enigmaDir>]");
            System.exit(1);
        }

        Path inputJar = Paths.get(args[0]);
        Path outputJar = Paths.get(args[1]);

        Platform platform = Platform.ANDROID;
        Path rFile = null;
        Path enigmaDir = null;
        for (int i = 2; i < args.length; i++) {
            if ("--platform".equals(args[i]) && i + 1 < args.length) {
                platform = Platform.valueOf(args[++i].trim().toUpperCase(Locale.ROOT));
            } else if ("--remap-ids".equals(args[i]) && i + 1 < args.length) {
                rFile = Paths.get(args[++i]);
            } else if ("--enigma-dir".equals(args[i]) && i + 1 < args.length) {
                enigmaDir = Paths.get(args[++i]);
            }
        }

        System.out.println("=== Patcher ===");
        System.out.println("Input:  " + inputJar);
        System.out.println("Output: " + outputJar);
        System.out.println("Platform: " + platform.name().toLowerCase(Locale.ROOT));

        List<Patch> patches = new ArrayList<>();
        if (platform == Platform.ANDROID) {
            patches.add(new Dex2JarFixPatch());
            if (rFile != null) {
                patches.add(new ResourceIdRemapPatch(ResourceIdRemapPatch.buildIdMapFromJar(rFile, inputJar)));
            }
        } else {
            //TODO: add desktop patches if needed

        }

        int total = 0;
        int patched = 0;
        boolean needsRemap = enigmaDir != null;
        Path stagedOutputJar = needsRemap
                ? Files.createTempFile(outputJar.toAbsolutePath().getParent(), outputJar.getFileName().toString(), ".staged.jar")
                : outputJar;

        try (JarInputStream jis = new JarInputStream(new BufferedInputStream(Files.newInputStream(inputJar)));
             JarOutputStream jos = new JarOutputStream(new BufferedOutputStream(Files.newOutputStream(stagedOutputJar)))) {
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

        if (needsRemap) {
            System.out.println("Remapping " + platform + " jar with enigma mappings: " + enigmaDir);
            remapJar(stagedOutputJar, outputJar, enigmaDir);
            Files.deleteIfExists(stagedOutputJar);
        }

        System.out.println("Done. " + patched + " patched, " + total + " total.");
    }

    private static void remapJar(Path inputJar, Path outputJar, Path enigmaDir) throws IOException {
        MemoryMappingTree mappingTree = new MemoryMappingTree();
        EnigmaDirReader.read(enigmaDir, "obf", "named", mappingTree);

        TinyRemapper remapper = TinyRemapper.newRemapper()
                .withMappings(TinyUtils.createMappingProvider(mappingTree, "obf", "named"))
                .build();

        try (OutputConsumerPath outputConsumer = new OutputConsumerPath.Builder(outputJar).build()) {
            outputConsumer.addNonClassFiles(inputJar);
            remapper.readInputs(inputJar);
            remapper.apply(outputConsumer);
        } finally {
            remapper.finish();
        }
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
