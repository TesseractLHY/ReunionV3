package cn.tesseract.patcher.patches;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cn.tesseract.patcher.Patch;

public class Dex2JarFixPatch implements Patch {
    @Override
    public byte[] transform(String className, byte[] classBytes) {
        ClassWriter cw;
        switch (className) {
            case "com/corrodinggames/rts/appFramework/class_243":
                cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                new ClassReader(classBytes).accept(new ClassVisitor(Opcodes.ASM9, cw) {
                    @Override
                    public MethodVisitor visitMethod(int acc, String name, String desc, String sig, String[] ex) {
                        if (name.equals("method_161") && desc.equals("(Landroid/content/Context;)V")) {
                            MethodVisitor mv = cv.visitMethod(acc, name, desc, sig, ex);
                            mv.visitInsn(Opcodes.ICONST_1);
                            mv.visitFieldInsn(Opcodes.PUTSTATIC, "com/corrodinggames/rts/appFramework/class_243", "field_587", "Z");
                            mv.visitInsn(Opcodes.ICONST_0);
                            mv.visitFieldInsn(Opcodes.PUTSTATIC, "com/corrodinggames/rts/appFramework/class_243", "field_588", "I");
                            mv.visitLdcInsn("");
                            mv.visitFieldInsn(Opcodes.PUTSTATIC, "com/corrodinggames/rts/appFramework/class_243", "field_589",
                                    "Ljava/lang/String;");
                            mv.visitInsn(Opcodes.RETURN);
                            mv.visitMaxs(1, 1);
                            mv.visitEnd();
                            return null;
                        }
                        return cv.visitMethod(acc, name, desc, sig, ex);
                    }
                }, 0);
                return cw.toByteArray();
            case "com/corrodinggames/rts/gameFramework/m/class_1192":
                cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                new ClassReader(classBytes).accept(new ClassVisitor(Opcodes.ASM9, cw) {
                    @Override
                    public MethodVisitor visitMethod(int acc, String name, String desc, String sig, String[] ex) {
                        if (name.equals("method_3199") && desc.equals("(Landroid/graphics/Canvas;Lcom/corrodinggames/rts/gameFramework/m/class_1221;)V")) {
                            MethodVisitor mv = cv.visitMethod(acc, name, desc, sig, ex);
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
                            return null;
                        }
                        return cv.visitMethod(acc, name, desc, sig, ex);
                    }
                }, 0);
                return cw.toByteArray();
        }

        return null;
    }
}
