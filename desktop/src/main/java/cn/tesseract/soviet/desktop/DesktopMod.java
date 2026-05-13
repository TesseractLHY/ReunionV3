package cn.tesseract.soviet.desktop;

import cn.tesseract.soviet.SovietMod;
import cn.tesseract.soviet.desktop.hook.MenuHook;

/**
 * Desktop platform mod definition.
 * Lists all hook classes that should be registered on the desktop JVM build.
 */
public class DesktopMod extends SovietMod {

    @Override
    protected Class<?>[] getHookClasses() {
        return new Class<?>[] {
                MenuHook.class,
        };
    }
}
