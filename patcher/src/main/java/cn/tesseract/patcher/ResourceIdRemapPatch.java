package cn.tesseract.patcher;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

public class ResourceIdRemapPatch implements Patch {
    private static final String SOURCE_PREFIX = "com/corrodinggames/rts/";
    private static final String TARGET_PREFIX = "cn/tesseract/reunion/";

    @Override
    public byte[] transform(String className, byte[] classBytes) {
        if (!className.startsWith(SOURCE_PREFIX + "R$"))
            return null;

        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        final String owner = className;
        final String targetOwner = className.replace(SOURCE_PREFIX, TARGET_PREFIX);

        cr.accept(new ClassVisitor(Opcodes.ASM9, cw) {
            final List<String> fieldNames = new ArrayList<>();
            final List<String> fieldDescs = new ArrayList<>();

            @Override
            public FieldVisitor visitField(int access, String name, String desc, String sig, Object value) {
                if ((access & Opcodes.ACC_STATIC) != 0 && ("I".equals(desc) || "[I".equals(desc))) {
                    fieldNames.add(name);
                    fieldDescs.add(desc);
                    return super.visitField(access, name, desc, sig, null);
                }
                return super.visitField(access, name, desc, sig, value);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String sig, String[] ex) {
                return "<clinit>".equals(name) ? null : super.visitMethod(access, name, desc, sig, ex);
            }

            @Override
            public void visitEnd() {
                if (!fieldNames.isEmpty()) {
                    MethodVisitor mv = cv.visitMethod(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
                    mv.visitCode();
                    for (int i = 0; i < fieldNames.size(); i++) {
                        String name = fieldNames.get(i);
                        String desc = fieldDescs.get(i);
                        mv.visitFieldInsn(Opcodes.GETSTATIC, targetOwner, name, desc);
                        mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, name, desc);
                    }
                    mv.visitInsn(Opcodes.RETURN);
                    mv.visitMaxs(0, 0);
                    mv.visitEnd();
                }
                super.visitEnd();
            }
        }, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }
}
