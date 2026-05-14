package cn.tesseract.soviet.desktop;

import cn.tesseract.soviet.SovietLogger;

/**
 * Desktop entry point. Starts the mod launcher, then delegates to the real game main.
 * Usage: replace the game's main class with this one:
 *   java -cp "game-lib.jar;mods/lib/*;mods/*;libs/*" cn.tesseract.soviet.desktop.DesktopLauncher [game args...]
 */
public class DesktopLauncher {

    public static void main(String[] args) throws Exception {
        SovietLogger.i("DesktopLauncher", "Starting Soviet Desktop Mod...");

        SovietLogger.i("DesktopLauncher", "Delegating to game main...");
        Class<?> gameMain = Class.forName("com.corrodinggames.rts.java.Main");
        gameMain.getMethod("main", String[].class).invoke(null, (Object) args);
    }
}
