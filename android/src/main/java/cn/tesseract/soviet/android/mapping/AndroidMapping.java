package cn.tesseract.soviet.android.mapping;

/**
 * Known class/method/field names for the Android dex2jar build.
 * Some game classes retain readable names while others are obfuscated.
 *
 * Resource IDs are referenced by name (not by R$id constant) because the
 * R class lives in the patched base AAR and must not be regenerated.
 */
public final class AndroidMapping {

    // ---- Activities (readable names) ----
    public static final String MAIN_MENU = "com.corrodinggames.rts.appFramework.MainMenuActivity";
    public static final String INTRO_SCREEN = "com.corrodinggames.rts.appFramework.IntroScreen";

    // ---- Obfuscated classes (dex2jar two-letter names) ----
    public static final String CHANGELOG_INIT = "com/corrodinggames/rts/appFramework/ix";
    public static final String CANVAS_HELPER = "com/corrodinggames/rts/gameFramework/m/ec";
    public static final String CANVAS_STATE = "com/corrodinggames/rts/gameFramework/m/fe";

    // ---- Resource class ----
    public static final String R_ID = "com.corrodinggames.rts.R$id";

    // ---- Resource ID names (looked up via getIdentifier at runtime) ----
    public static final String ID_START_GAME = "startgameButton";
    public static final String ID_MENU_CUSTOM = "menuCustomButton";
    public static final String ID_MULTIPLAYER = "multiplayerButton";
    public static final String ID_SETTINGS = "settingsButton";
    public static final String ID_MODS = "modsButton";
    public static final String ID_EXITGAME = "exitgameButton";
    public static final String ID_HELP = "helpButton";

    private AndroidMapping() {}
}
