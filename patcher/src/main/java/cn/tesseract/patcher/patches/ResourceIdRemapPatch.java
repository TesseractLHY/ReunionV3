package cn.tesseract.patcher.patches;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.util.regex.*;

import org.objectweb.asm.*;

import cn.tesseract.patcher.Patch;

/**
 * Remaps resource IDs in R classes from old (APK) values to new (build) values.
 * Rewrites ConstantValue on fields and ldc constants in &lt;clinit&gt; for int[] arrays.
 */
public class ResourceIdRemapPatch implements Patch {

    private Map<Integer, Integer> idMap = Collections.emptyMap();

    @Override public String name() { return "ResourceIdRemap"; }

    @Override @SuppressWarnings("unchecked")
    public void init(Map<String, Object> context) {
        Map<Integer, Integer> m = (Map<Integer, Integer>) context.get("resourceIdMap");
        if (m != null) idMap = m;
        System.out.println("[ResourceIdRemap] " + idMap.size() + " ID mappings");
    }

    @Override
    public byte[] transform(String className, byte[] classBytes) {
        if (!className.startsWith("com/corrodinggames/rts/R$")
                && !className.equals("com/corrodinggames/rts/R")) return null; // slashes from Patcher

        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cr.accept(new Remapper(Opcodes.ASM9, cw, idMap), 0);
        return cw.toByteArray();
    }

    private static class Remapper extends ClassVisitor {
        private final Map<Integer, Integer> idMap;
        private int fChanges, iChanges;

        Remapper(int api, ClassVisitor cv, Map<Integer, Integer> idMap) {
            super(api, cv);
            this.idMap = idMap;
        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc, String sig, Object value) {
            if (value instanceof Integer && "I".equals(desc)) {
                Integer newId = idMap.get((Integer) value);
                if (newId != null && !newId.equals(value)) { fChanges++; value = newId; }
            }
            return super.visitField(access, name, desc, sig, value);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String sig, String[] ex) {
            MethodVisitor mv = super.visitMethod(access, name, desc, sig, ex);
            return new MethodVisitor(Opcodes.ASM9, mv) {
                @Override
                public void visitLdcInsn(Object cst) {
                    if (cst instanceof Integer) {
                        Integer newId = idMap.get((Integer) cst);
                        if (newId != null && !newId.equals(cst)) { iChanges++; cst = newId; }
                    }
                    super.visitLdcInsn(cst);
                }
            };
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
            if (fChanges + iChanges > 0)
                System.out.println("  R: " + fChanges + " fields, " + iChanges + " ldks");
        }
    }

    /** Build oldID→newID by matching R class field names from JAR against R.txt from build */
    public static Map<Integer, Integer> buildIdMapFromJar(Path rFile, Path jarFile) throws IOException {
        Map<String, Integer> nameToNew = parseRFile(rFile);
        Map<String, Integer> nameToOld = scanJarRClasses(jarFile);

        Map<Integer, Integer> map = new HashMap<>();
        for (var e : nameToOld.entrySet()) {
            Integer newId = nameToNew.get(e.getKey());
            if (newId != null && !newId.equals(e.getValue())) map.put(e.getValue(), newId);
        }
        return map;
    }

    static Map<String, Integer> parseRFile(Path path) throws IOException {
        Map<String, Integer> map = new HashMap<>();
        Pattern p = Pattern.compile("int (\\w+) (\\w+) (0x[0-9a-fA-F]+)");
        for (String line : Files.readAllLines(path)) {
            Matcher m = p.matcher(line);
            if (m.matches()) map.put(m.group(1) + "/" + m.group(2),
                    Integer.parseInt(m.group(3).substring(2), 16));
        }
        return map;
    }

    private static Map<String, Integer> scanJarRClasses(Path jarFile) throws IOException {
        Map<String, Integer> out = new HashMap<>();
        try (JarInputStream jis = new JarInputStream(new BufferedInputStream(Files.newInputStream(jarFile)))) {
            JarEntry e;
            while ((e = jis.getNextJarEntry()) != null) {
                String name = e.getName();
                if (name.startsWith("com/corrodinggames/rts/R$") && name.endsWith(".class")) {
                    String type = name.substring(name.lastIndexOf('$') + 1, name.lastIndexOf('.'));
                    parseRFields(jis.readAllBytes(), type, out);
                }
            }
        }
        return out;
    }

    private static void parseRFields(byte[] bytes, String type, Map<String, Integer> out) {
        new ClassReader(bytes).accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public FieldVisitor visitField(int access, String name, String desc, String sig, Object value) {
                if (value instanceof Integer && "I".equals(desc)) out.put(type + "/" + name, (Integer) value);
                return null;
            }
        }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
    }
}
