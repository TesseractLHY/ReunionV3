package cn.tesseract.soviet.desktop.hook;

import cn.tesseract.crosshook.Callback;
import cn.tesseract.crosshook.Hook;
import cn.tesseract.soviet.SovietLogger;

/**
 * Desktop main menu hooks.
 *
 * Unlike Android (which uses android.widget.Button / findViewById via
 * MainMenuActivity), the desktop build uses libRocket (HTML/CSS GUI toolkit).
 *
 * Key classes:
 *   Root = com.corrodinggames.librocket.scripts.Root   -- main menu controller
 *   GUI  = com.corrodinggames.librocket.a               -- GUI engine (singleton, a())
 *
 * Root.showMainMenu():
 *   1. gameFramework.l.B().bS.u = false  (reset game state)
 *   2. librocket.a.a().b()               (hide/clear current UI)
 *
 * The main menu document (mainmenu.rml) is loaded asynchronously by the GUI
 * engine after showMainMenu() returns. To modify menu elements, hook the
 * document load completion or Root.onEnter().
 *
 * Typical libRocket pattern for UI manipulation:
 *   ElementDocument doc = libRocket.getActiveDocument();
 *   Element el = doc.getElementById("elementId");
 *   el.setInnerRML("new content");
 *   el.setAttribute("width", "900px");
 *   el.addEventListener("click", ...);
 */
public class MenuHook {

    /**
     * Hook after the main menu is shown.
     * At this point the GUI engine has been instructed to load the menu,
     * but the document may not yet be fully loaded.
     */
    @Hook(
        targetClass = "com.corrodinggames.librocket.scripts.Root",
        targetMethod = "showMainMenu",
        injector = Hook.TAIL
    )
    public static void onMainMenuShown(Callback<?> c) {
        SovietLogger.i("MenuHook", "Main menu shown (desktop)");
        // TODO: After identifying libRocket element IDs:
        //   com.corrodinggames.librocket.b libRocket = ...;
        //   ElementDocument doc = libRocket.getActiveDocument();
        //   Element startBtn = doc.getElementById("startgame");
        //   Element exitBtn = doc.getElementById("exitgame");
        //   ...modify elements...
    }
}
