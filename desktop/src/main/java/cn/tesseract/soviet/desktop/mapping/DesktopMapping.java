package cn.tesseract.soviet.desktop.mapping;

/**
 * Known class/method names for the desktop ProGuard-obfuscated build.
 *
 * The desktop build uses single-letter class names (a-z, aa-ab...) for most
 * game packages via ProGuard. A few packages that need reflection access are
 * deobfuscated:
 *
 *   - com.corrodinggames.rts.game.units.custom.logicBooleans.* (modding API)
 *   - com.corrodinggames.rts.java.audio.lwjgl.* (audio backend)
 *   - com.corrodinggames.rts.gameFramework.SettingsEngine
 *   - com.corrodinggames.rts.gameFramework.utility.SlickToAndroidKeycodes
 *   - com.corrodinggames.librocket.scripts.* (UI scripts)
 *
 * ============ Activity Mapping (appFramework) ============
 * b = base Activity class (extends android.app.Activity)
 * g = InGameActivity (confirmed by "IngameActivity: finish" log)
 *
 * ============ Desktop vs Android Key Difference ============
 * Desktop uses libRocket for UI, not Android Views.
 * Main menu is managed by Root (librocket/scripts/Root), not MainMenuActivity.
 * Root.showMainMenu() is the equivalent of MainMenuActivity.onCreate().
 */
public final class DesktopMapping {

    // ---- Entry point (not obfuscated) ----
    public static final String GAME_MAIN = "com/corrodinggames/rts/java/Main";

    // ---- UI layer (deobfuscated) ----
    public static final String ROOT_SCRIPT = "com/corrodinggames/librocket/scripts/Root";
    public static final String GUI_ENGINE = "com/corrodinggames/librocket/a";

    // ---- deobfuscated game packages ----
    public static final String SETTINGS_ENGINE = "com/corrodinggames/rts/gameFramework/SettingsEngine";

    // ---- R class (field names match Android, values may differ) ----
    public static final String R_ID = "com/corrodinggames/rts/R$id";

    // ---- Game engine (obfuscated) ----
    public static final String GAME_ENGINE = "com/corrodinggames/rts/game/i";

    // ---- Obfuscated Activities (appFramework) ----
    public static final String BASE_ACTIVITY = "com/corrodinggames/rts/appFramework/b";
    public static final String IN_GAME_ACTIVITY = "com/corrodinggames/rts/appFramework/g";

    // ---- Root key methods ----
    public static final String ROOT_SHOW_MAIN_MENU = "showMainMenu";

    // ---- External obfuscated packages ----
    public static final String EXT_LIB_A = "a/a/a";
    public static final String EXT_LIB_ORG = "org/a/a";

    private DesktopMapping() {}
}
