package cn.tesseract.patcher;

@FunctionalInterface
public interface Patch {
    byte[] transform(String className, byte[] classBytes);
}
