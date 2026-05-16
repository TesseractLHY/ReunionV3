package com.corrodinggames.rts.appFramework;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.corrodinggames.rts.R;
import com.corrodinggames.rts.gameFramework.class_1061;
import com.corrodinggames.rts.gameFramework.h.class_988;

/* JADX INFO: loaded from: classes.dex */
public class LevelGroupSelectActivity extends class_1 {
    public static final String customLevelsDir = "/SD/rusted_warfare_maps";
    public static final String customLevelsDir2 = "/SD/rustedWarfare/maps";
    public static final String skirmishLevelsDir = "maps/skirmish";
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
        class_1061 class_1061VarMethod_3037 = class_1061.method_3037(this);
        if (class_1061VarMethod_3037 != null) {
            class_5 class_5VarMethod_108 = class_84.method_108(this, this.gameView);
            this.gameView = class_5VarMethod_108;
            class_1061VarMethod_3037.method_2997(this, class_5VarMethod_108, true);
        }
        class_84.method_115(this, true);
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
        class_1061.method_3037(this);
        findViewById(R.id.levelButtonBack).setOnClickListener(new class_39(this));
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.levelHolder);
        linearLayout.removeAllViews();
        onCreateMode(linearLayout, class_988.method_2636("menus.singlePlayer.campaign", new Object[0]), "maps/normal", true);
        onCreateMode(linearLayout, class_988.method_2636("menus.singlePlayer.skirmish", new Object[0]), skirmishLevelsDir, true);
        onCreateMode(linearLayout, class_988.method_2636("menus.singlePlayer.challenge", new Object[0]), "maps/challenge", false);
        onCreateMode(linearLayout, class_988.method_2636("menus.singlePlayer.survival", new Object[0]), "maps/survival", false);
        onCreateMode(linearLayout, class_988.method_2636("menus.singlePlayer.sandbox", new Object[0]), (View.OnClickListener) new class_40(this), false);
        onCreateMode(linearLayout, class_988.method_2636("menus.singlePlayer.custom", new Object[0]), customLevelsDir, false);
        onCreateMode(linearLayout, class_988.method_2636("menus.singlePlayer.loadSave", new Object[0]), (View.OnClickListener) new class_41(this), true);
        ((TextView) findViewById(R.id.LevelTextTop)).setText(class_988.method_2636("menus.singlePlayer.title", new Object[0]));
    }

    public void onCreateMode(LinearLayout linearLayout, String str, String str2, boolean z) {
        onCreateMode(linearLayout, str, new class_42(this, str2), z);
    }

    public void onCreateMode(LinearLayout linearLayout, String str, View.OnClickListener onClickListener, boolean z) {
        boolean z2 = class_1061.method_3037(this).field_6321 && !z;
        Button button = new Button(getBaseContext());
        button.setBackgroundResource(R.drawable.btn_dropdown);
        if (z2) {
            str = "[LOCKED] ".concat(String.valueOf(str));
            button.setOnClickListener(new class_43(this));
            button.getBackground().setColorFilter(new LightingColorFilter(Color.argb(255, 128, 128, 128), 0));
        } else {
            button.setOnClickListener(onClickListener);
        }
        button.setText(str);
        button.setTypeface(Typeface.DEFAULT_BOLD);
        button.setTextColor(-1);
        button.setShadowLayer(1.0f, 2.0f, 2.0f, Color.argb(127, 0, 0, 0));
        button.setPadding(15, 16, 15, 16);
        button.setTextSize(14.0f);
        linearLayout.addView(button);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(button.getLayoutParams());
        layoutParams.setMargins(0, 2, 0, 2);
        layoutParams.gravity = 1;
        button.setLayoutParams(layoutParams);
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTitle("Mode");
        if (class_84.method_127(this, true)) {
            setContentView(R.layout.level_select);
            ((Spinner) findViewById(R.id.aiDifficulty)).setVisibility(8);
            this.gameView = class_84.method_125(this);
            setup();
        }
    }

    public void startSandbox() {
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        LevelSelectActivity.loadSinglePlayerMapRaw("skirmish/[z;p10]Crossing Large (10p).tmx", true, 3, 1, true, true);
        class_1061VarMethod_3076.field_6352.method_2769("starting singleplayer (sandbox)");
        class_1061VarMethod_3076.field_6352.field_5985 = "You";
        class_1061VarMethod_3076.field_6352.field_5975 = true;
        class_1061VarMethod_3076.field_6352.method_2826();
        class_1061.method_3043("started startSinglePlayerServer (sandbox)");
        startActivityForResult(new Intent(getApplicationContext(), (Class<?>) MultiplayerBattleroomActivity.class), 0);
    }
}
