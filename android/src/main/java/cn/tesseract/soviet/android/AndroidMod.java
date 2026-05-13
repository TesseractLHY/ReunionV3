package cn.tesseract.soviet.android;

import cn.tesseract.soviet.SovietMod;

/**
 * Android platform mod definition.
 * Lists all hook classes that should be registered on Android.
 */
public class AndroidMod extends SovietMod {
    @Override
    protected Class<?>[] getHookClasses() {
        return new Class<?>[]{};
    }
}
