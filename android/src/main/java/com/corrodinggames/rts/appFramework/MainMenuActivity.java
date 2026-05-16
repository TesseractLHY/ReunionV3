package com.corrodinggames.rts.appFramework;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.tesseract.union.hook.LayoutHook;
import com.corrodinggames.rts.R;
import com.corrodinggames.rts.appFramework.android.AndroidSAF;
import com.corrodinggames.rts.game.units.custom.logicBooleans.VariableScope;
import com.corrodinggames.rts.gameFramework.SettingsEngine;
import com.corrodinggames.rts.gameFramework.class_1061;
import com.corrodinggames.rts.gameFramework.h.class_988;
import com.corrodinggames.rts.gameFramework.i.class_991;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class MainMenuActivity extends class_1 {
    public class_5 gameView;

    @Override // android.app.Activity
    public void finish() {
        super.finish();
        class_84.method_133(this, true);
    }

    @Override // com.corrodinggames.rts.appFramework.class_1, android.app.Activity
    public void onResume() {
        super.onResume();
        setup();
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076 != null) {
            class_5 class_5VarMethod_108 = class_84.method_108(this, this.gameView);
            this.gameView = class_5VarMethod_108;
            class_1061VarMethod_3076.method_2997(this, class_5VarMethod_108, true);
        }
        class_84.method_115(this, true);
        class_988.method_2639();
        setButtonText();
    }

    @Override // android.app.Activity
    public void onStart() {
        super.onStart();
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076 != null) {
            class_1061VarMethod_3076.method_3001(this.gameView);
        }
    }

    @Override // android.app.Activity
    public void onStop() {
        super.onStop();
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076 != null) {
            class_1061VarMethod_3076.method_2996(this, this.gameView);
        }
    }

    @Override // android.app.Activity
    public void onPause() {
        super.onPause();
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076 != null) {
            class_1061VarMethod_3076.method_3030(this.gameView);
        }
    }

    public void setup() {
        boolean z;
        class_1061 class_1061VarMethod_3037 = class_1061.method_3037(this);
        Button button = (Button) findViewById(R.id.startgameButton);
        if (class_1061VarMethod_3037 != null && class_1061VarMethod_3037.field_6334 && !class_1061VarMethod_3037.field_6335) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            button.setVisibility(0);
        } else {
            button.setVisibility(8);
        }
        Button button2 = (Button) findViewById(R.id.buyButton);
        if (!class_1061VarMethod_3037.field_6321) {
            button2.setVisibility(8);
        }
        findViewById(R.id.TitleImage);
        if (class_1061VarMethod_3037 != null && class_1061VarMethod_3037.field_6352 != null) {
            class_1061VarMethod_3037.field_6352.method_2699();
        }
        ((TextView) findViewById(R.id.titleInfo)).setText(class_1061VarMethod_3037.method_3069());
    }

    public void setButtonText() {
        ((Button) findViewById(R.id.buyButton)).setText(class_988.method_2636("menus.front.buyNow", new Object[0]));
        ((Button) findViewById(R.id.startgameButton)).setText(class_988.method_2636("menus.front.continue", new Object[0]));
        ((Button) findViewById(R.id.menuCustomButton)).setText(class_988.method_2636("menus.front.singlePlayer", new Object[0]));
        ((Button) findViewById(R.id.multiplayerButton)).setText(class_988.method_2636("menus.front.multiplayer", new Object[0]));
        ((Button) findViewById(R.id.settingsButton)).setText(class_988.method_2636("menus.front.settings", new Object[0]));
        ((Button) findViewById(R.id.helpButton)).setText(class_988.method_2636("menus.front.help", new Object[0]));
        ((Button) findViewById(R.id.modsButton)).setText(class_988.method_2636("menus.front.mods", new Object[0]));
        ((Button) findViewById(R.id.exitgameButton)).setText(class_988.method_2636("menus.front.exit", new Object[0]));
        LayoutHook.setButtonText(this);
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            Method method = IntroScreen.class.getMethod("overridePendingTransition", Integer.TYPE, Integer.TYPE);
            if (method != null) {
                try {
                    method.invoke(this, Integer.valueOf(R.anim.mainfadein), Integer.valueOf(R.anim.splashfadeout));
                    Log.e(AndroidSAF.TAG, "overridePendingTransition done");
                } catch (Exception e) {
                    Log.e(AndroidSAF.TAG, "overridePendingTransition invoke:", e);
                }
            }
        } catch (Exception e2) {
            Log.e(AndroidSAF.TAG, "overridePendingTransition:", e2);
        }
        if (class_84.method_127(this, true)) {
            setContentView(R.layout.menu);
            this.gameView = class_84.method_125(this);
            setup();
            class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
            setButtonText();
            Button button = (Button) findViewById(R.id.buyButton);
            button.getBackground().setColorFilter(new LightingColorFilter(-1, -13434880));
            button.setOnClickListener(new class_76(this));
            ((Button) findViewById(R.id.startgameButton)).setOnClickListener(new class_82(this));
            ((Button) findViewById(R.id.menuCustomButton)).setOnClickListener(new class_83(this));
            ((Button) findViewById(R.id.multiplayerButton)).setOnClickListener(new class_88(this));
            ((Button) findViewById(R.id.helpButton)).setOnClickListener(new class_93(this));
            Button button2 = (Button) findViewById(R.id.modsButton);
            if (class_1061VarMethod_3076.field_6321) {
                button2.setVisibility(8);
            } else {
                button2.setOnClickListener(new class_94(this));
            }
            ((Button) findViewById(R.id.settingsButton)).setOnClickListener(new class_95(this));
            ((Button) findViewById(R.id.exitgameButton)).setOnClickListener(new class_96(this));
            warnAboutBugs();
            if (class_991.field_5780 != null) {
                class_1061VarMethod_3076.method_3032("Error", class_991.field_5780);
                class_991.field_5780 = null;
            }
            if (!showUpdateMessagePopup(this, false)) {
                SettingsActivity.askAboutLastDebugOption();
            }
        }
    }

    public static boolean showUpdateMessagePopup(Context context, boolean z) {
        String str = class_243.field_589;
        if (str == null) {
            return false;
        }
        int i = class_243.field_588;
        boolean z2 = class_243.field_587;
        SettingsEngine settingsEngine = SettingsEngine.getInstance(context);
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076 == null) {
            class_1061.method_3031("showUpdateMessagePopup: game==null");
            return false;
        }
        boolean z3 = i == -1 || i != settingsEngine.lastSeenMessageId;
        if (class_1061VarMethod_3076.field_6321 && settingsEngine.lastSeenMessageId == -1) {
            z3 = false;
        }
        if (z) {
            z3 = true;
        }
        settingsEngine.lastSeenMessageId = i;
        if (!z3) {
            return false;
        }
        String str2 = settingsEngine.lastSeenMessageIds;
        if (str2 == null) {
            str2 = VariableScope.nullOrMissingString;
        }
        if (str2.contains(",".concat(String.valueOf(i)))) {
            return false;
        }
        if (str2.length() > 100) {
            str2 = VariableScope.nullOrMissingString;
        }
        settingsEngine.lastSeenMessageIds = str2 + "," + i;
        settingsEngine.save();
        if (!z2) {
            Toast.makeText(context, str, 1).show();
        } else {
            new AlertDialog.Builder(context).setIcon(android.R.drawable.ic_dialog_alert).setTitle(VariableScope.nullOrMissingString).setMessage(str).setPositiveButton("Ok", new class_97()).show();
        }
        return true;
    }

    public void resumeMultiplayer() {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        if (class_1061VarMethod_3076.field_6352 != null && !class_1061VarMethod_3076.field_6352.field_5898) {
            startActivityForResult(new Intent(getApplicationContext(), (Class<?>) MultiplayerBattleroomActivity.class), 0);
        } else {
            startActivityForResult(new Intent(getApplicationContext(), (Class<?>) InGameActivity.class), 0);
        }
    }

    public void warnAboutBugs() {
        boolean z;
        LayoutHook.warnAboutBugs(this);
        if (!SettingsEngine.getInstance(this).shownAudioWarning) {
            if (!Build.MODEL.toUpperCase().contains("GT-I9100")) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Bugs in Samsung Galaxy S2's audio may cause crashes and freezes.").setMessage("Would you like to disable sound?").setPositiveButton("Disable sound", new class_79(this)).setNeutralButton("Remind me", new class_78(this)).setNegativeButton("Risk it", new class_77(this)).show();
            }
        }
    }

    public void checkIfHelpShouldBeShown(class_98 class_98Var) {
        if (!SettingsEngine.getInstance(getBaseContext()).hasPlayedGameOrSeenHelp) {
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("This appears to be your first time playing").setMessage("Would you like to view the quick help slides?").setPositiveButton("Yes", new class_81(this)).setNegativeButton("No", new class_80(this, class_98Var)).show();
        } else {
            class_98Var.method_141();
        }
    }
}
