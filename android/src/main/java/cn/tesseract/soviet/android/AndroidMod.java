package cn.tesseract.soviet.android;

import cn.tesseract.soviet.SovietMod;

/**
 * Android platform mod definition.
 * Android now relies on resource overrides (scheme b) instead of runtime hooks.
 */
public class AndroidMod extends SovietMod {

    @Override
    protected Class<?>[] getHookClasses() {
        return new Class<?>[0];
    }
}
