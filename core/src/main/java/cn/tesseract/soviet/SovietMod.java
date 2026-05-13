package cn.tesseract.soviet;

import cn.tesseract.crosshook.HookRegistry;

/**
 * Base class for platform-specific mod entry points.
 * Each platform module provides a concrete subclass that lists its hook classes.
 */
public abstract class SovietMod {

    protected abstract Class<?>[] getHookClasses();

    public void registerHooks() {
        for (Class<?> hookClass : getHookClasses()) {
            SovietLogger.i("SovietMod", "Registering hooks in " + hookClass.getSimpleName());
            HookRegistry.instance.register(hookClass);
        }
    }
}
