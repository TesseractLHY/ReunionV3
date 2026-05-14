package cn.tesseract.patcher.patches;

import java.util.*;

import org.objectweb.asm.*;

import cn.tesseract.patcher.Patch;

/**
 * Fixes methods that dex2jar failed to translate.
 *
 * Dex2jar replaces untranslatable methods with:
 *   throw new RuntimeException("d2j fail translate: ...")
 *
 * This patch replaces those stubs (or known-broken methods) with working code.
 */
public class FixDex2JMethodsPatch implements Patch {

    static class FixEntry {
        final MethodWriter writer;
        final boolean always;
        FixEntry(MethodWriter w, boolean a) { writer = w; always = a; }
    }

    private final Map<String, Map<String, FixEntry>> fixes = new HashMap<>();

    public FixDex2JMethodsPatch() {
        // ix.a() — changelog init that dex2jar couldn't translate
        // Original decoded Base64 changelog + started a thread. Stub sets safe defaults.
        fix("com/corrodinggames/rts/appFramework/ix", "a", "(Landroid/content/Context;)V", false,
                mv -> {
                    mv.visitInsn(Opcodes.ICONST_1);
                    mv.visitFieldInsn(Opcodes.PUTSTATIC, "com/corrodinggames/rts/appFramework/ix", "a", "Z");
                    mv.visitInsn(Opcodes.ICONST_0);
                    mv.visitFieldInsn(Opcodes.PUTSTATIC, "com/corrodinggames/rts/appFramework/ix", "b", "I");
                    mv.visitLdcInsn("");
                    mv.visitFieldInsn(Opcodes.PUTSTATIC, "com/corrodinggames/rts/appFramework/ix", "c",
                            "Ljava/lang/String;");
                    mv.visitInsn(Opcodes.RETURN);
                    mv.visitMaxs(1, 1);
                    mv.visitEnd();
                });

        // ec.a(Canvas,fe) — orphaned restore() without matching save()
        // dex2jar mis-translated a try-finally block
        fix("com/corrodinggames/rts/gameFramework/m/ec",
                "a", "(Landroid/graphics/Canvas;Lcom/corrodinggames/rts/gameFramework/m/fe;)V", true,
                mv -> {
                    mv.visitVarInsn(Opcodes.ALOAD, 1);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/graphics/Canvas", "getSaveCount", "()I",
                            false);
                    mv.visitInsn(Opcodes.ICONST_1);
                    Label skip = new Label();
                    mv.visitJumpInsn(Opcodes.IF_ICMPLE, skip);
                    mv.visitVarInsn(Opcodes.ALOAD, 1);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/graphics/Canvas", "restore", "()V", false);
                    mv.visitLabel(skip);
                    mv.visitInsn(Opcodes.RETURN);
                    mv.visitMaxs(2, 3);
                    mv.visitEnd();
                });
    }

    private void fix(String cls, String mtd, String desc, boolean always, MethodWriter w) {
        fixes.computeIfAbsent(cls, k -> new HashMap<>()).put(mtd + desc, new FixEntry(w, always));
    }

    @Override public String name() { return "FixDex2JMethods"; }

    @Override
    public void init(Map<String, Object> c) {
        System.out.println("[FixDex2JMethods] " + fixes.size() + " targets");
    }

    @Override
    public byte[] transform(String className, byte[] classBytes) {
        Map<String, FixEntry> methods = fixes.get(className);
        if (methods == null) return null;

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        boolean[] replaced = { false };
        new ClassReader(classBytes).accept(new ClassVisitor(Opcodes.ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(int acc, String name, String desc, String sig, String[] ex) {
                FixEntry f = methods.get(name + desc);
                if (f != null && (f.always || isStub(classBytes, name, desc))) {
                    replaced[0] = true;
                    System.out.println("  Fix: " + className + "." + name + (f.always ? " [always]" : " [stub]"));
                    MethodVisitor mv = cv.visitMethod(acc, name, desc, sig, ex);
                    f.writer.write(mv);
                    return null;
                }
                return cv.visitMethod(acc, name, desc, sig, ex);
            }
        }, 0);
        return replaced[0] ? cw.toByteArray() : null;
    }

    private static boolean isStub(byte[] bytes, String mtd, String desc) {
        boolean[] found = { false };
        new ClassReader(bytes).accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public MethodVisitor visitMethod(int a, String n, String d, String s, String[] e) {
                if (!n.equals(mtd) || !d.equals(desc)) return null;
                return new MethodVisitor(Opcodes.ASM9) {
                    @Override
                    public void visitLdcInsn(Object c) {
                        if (c instanceof String && ((String) c).contains("d2j fail translate")) found[0] = true;
                    }
                };
            }
        }, ClassReader.SKIP_DEBUG);
        return found[0];
    }

    @FunctionalInterface
    interface MethodWriter { void write(MethodVisitor mv); }
}
