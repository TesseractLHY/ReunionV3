package cn.tesseract.soviet;

/**
 * Platform-agnostic logging.
 * On Android, delegates to android.util.Log via reflection.
 * On JVM, uses System.out.
 */
public final class SovietLogger {

    private static final boolean IS_ANDROID;

    static {
        boolean android;
        try {
            Class.forName("android.util.Log");
            android = true;
        } catch (ClassNotFoundException e) {
            android = false;
        }
        IS_ANDROID = android;
    }

    private SovietLogger() {}

    public static void i(String tag, String msg) {
        if (IS_ANDROID) {
            try {
                Class<?> log = Class.forName("android.util.Log");
                log.getMethod("i", String.class, String.class).invoke(null, tag, msg);
                return;
            } catch (Exception ignored) {}
        }
        System.out.println("[Soviet/" + tag + "] " + msg);
    }

    public static void w(String tag, String msg) {
        if (IS_ANDROID) {
            try {
                Class<?> log = Class.forName("android.util.Log");
                log.getMethod("w", String.class, String.class).invoke(null, tag, msg);
                return;
            } catch (Exception ignored) {}
        }
        System.out.println("[Soviet/" + tag + "] WARN: " + msg);
    }

    public static void e(String tag, String msg, Throwable t) {
        if (IS_ANDROID) {
            try {
                Class<?> log = Class.forName("android.util.Log");
                log.getMethod("e", String.class, String.class, Throwable.class)
                        .invoke(null, tag, msg, t);
                return;
            } catch (Exception ignored) {}
        }
        System.out.println("[Soviet/" + tag + "] ERROR: " + msg);
        if (t != null) t.printStackTrace(System.out);
    }
}
