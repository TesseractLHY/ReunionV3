package cn.tesseract.patcher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

import cn.tesseract.patcher.patches.FixDex2JMethodsPatch;
import cn.tesseract.patcher.patches.ResourceIdRemapPatch;

/**
 * Applies a pipeline of patches to class entries in a JAR.
 * Usage: Patcher &lt;input&gt; &lt;output&gt; --remap-ids &lt;rFile&gt;
 * Pipeline: fixDex2JMethods (always), remapResourceIds (if --remap-ids given)
 */
public class Patcher {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: Patcher <input> <output> [--platform android|desktop] [--remap-ids <rFile>]");
            System.exit(1);
        }

        Path inputJar = Paths.get(args[0]);
        Path outputJar = Paths.get(args[1]);

        Platform platform = Platform.ANDROID;
        Path rFile = null;
        for (int i = 2; i < args.length; i++) {
            if ("--platform".equals(args[i]) && i + 1 < args.length)
                platform = Platform.valueOf(args[++i].trim().toUpperCase(Locale.ROOT));
            if ("--remap-ids".equals(args[i]) && i + 1 < args.length) rFile = Paths.get(args[++i]);
        }

        System.out.println("=== Patcher ===");
        System.out.println("Input:  " + inputJar);
        System.out.println("Output: " + outputJar);
        System.out.println("Platform: " + platform.name().toLowerCase(Locale.ROOT));

        List<Patch> patches = new ArrayList<>();
        Map<String, Object> context = new HashMap<>();
        if (platform == Platform.ANDROID) {
            FixDex2JMethodsPatch f = new FixDex2JMethodsPatch();
            f.init(context);

            patches.add(f);
            if (rFile != null) {
                Map<Integer, Integer> idMap = ResourceIdRemapPatch.buildIdMapFromJar(rFile, inputJar);
                if (!idMap.isEmpty()) {
                    context.put("resourceIdMap", idMap);
                    ResourceIdRemapPatch r = new ResourceIdRemapPatch();
                    r.init(context);
                    patches.add(r);
                }
            }
        } else if (platform == Platform.DESKTOP) {
            //TODO: desktop-specific patches

        }

        int total = 0, patched = 0;
        try (JarInputStream jis = new JarInputStream(new BufferedInputStream(Files.newInputStream(inputJar)));
             JarOutputStream jos = new JarOutputStream(new BufferedOutputStream(Files.newOutputStream(outputJar)))) {

            JarEntry entry;
            while ((entry = jis.getNextJarEntry()) != null) {
                byte[] data = readAllBytes(jis);

                if (entry.getName().endsWith(".class")) {
                    total++;
                    String cn = entry.getName().replace(".class", "");
                    for (Patch p : patches) {
                        byte[] result = p.transform(cn, data);
                        if (result != null) { data = result; patched++; break; }
                    }
                }

                JarEntry out = new JarEntry(entry.getName());
                out.setTime(entry.getTime());
                jos.putNextEntry(out);
                jos.write(data);
                jos.closeEntry();
            }
        }
        System.out.println("Done. " + patched + " patched, " + total + " total.");
    }

    private enum Platform {
        ANDROID,
        DESKTOP
    }

    private static byte[] readAllBytes(InputStream in) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int n;
        while ((n = in.read(buf)) != -1) bos.write(buf, 0, n);
        return bos.toByteArray();
    }
}
