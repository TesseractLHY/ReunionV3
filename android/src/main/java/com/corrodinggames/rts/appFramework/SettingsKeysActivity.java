package com.corrodinggames.rts.appFramework;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import com.corrodinggames.rts.R;
import com.corrodinggames.rts.gameFramework.SettingsEngine;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class SettingsKeysActivity extends Activity {
    public String[] mKeyLabels;
    public SettingsEngine settings;
    public boolean saveChanges = true;
    public ArrayList buttons = new ArrayList();

    public void saveSettings() {
        this.settings.keyAction = ((class_239) this.buttons.get(0)).field_581;
        this.settings.keyJump = ((class_239) this.buttons.get(1)).field_581;
        this.settings.keyLeft = ((class_239) this.buttons.get(2)).field_581;
        this.settings.keyRight = ((class_239) this.buttons.get(3)).field_581;
        this.settings.keyDown = ((class_239) this.buttons.get(4)).field_581;
        this.settings.save();
        finish();
    }

    @Override // android.app.Activity
    public void onPause() {
        if (this.saveChanges && isFinishing()) {
            saveSettings();
        }
        super.onPause();
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTitle("Keyboard");
        if (class_84.method_127(this, false)) {
            setContentView(R.layout.settings_keyboard);
            this.settings = SettingsEngine.getInstance(getBaseContext());
            this.buttons = new ArrayList();
            class_239 class_239Var = new class_239(this);
            class_239Var.field_579 = (Button) findViewById(R.id.settingsKAction);
            class_239Var.field_580 = "Action";
            class_239Var.field_581 = this.settings.keyAction;
            this.buttons.add(class_239Var);
            class_239 class_239Var2 = new class_239(this);
            class_239Var2.field_579 = (Button) findViewById(R.id.settingsKJump);
            class_239Var2.field_580 = "Jump";
            class_239Var2.field_581 = this.settings.keyJump;
            this.buttons.add(class_239Var2);
            class_239 class_239Var3 = new class_239(this);
            class_239Var3.field_579 = (Button) findViewById(R.id.settingsKLeft);
            class_239Var3.field_580 = "Left";
            class_239Var3.field_581 = this.settings.keyLeft;
            this.buttons.add(class_239Var3);
            class_239 class_239Var4 = new class_239(this);
            class_239Var4.field_579 = (Button) findViewById(R.id.settingsKRight);
            class_239Var4.field_580 = "Right";
            class_239Var4.field_581 = this.settings.keyRight;
            this.buttons.add(class_239Var4);
            class_239 class_239Var5 = new class_239(this);
            class_239Var5.field_579 = (Button) findViewById(R.id.settingsKDown);
            class_239Var5.field_580 = "Down";
            class_239Var5.field_581 = this.settings.keyDown;
            this.buttons.add(class_239Var5);
            for (class_239 class_239Var6 : this.buttons) {
                class_239Var6.method_160();
                class_239Var6.field_579.setOnClickListener(new class_240(this, class_239Var6));
            }
            ((Button) findViewById(R.id.settingsKDone)).setOnClickListener(new class_236(this));
            ((Button) findViewById(R.id.settingsKCancel)).setOnClickListener(new class_237(this));
            ((Button) findViewById(R.id.settingKDefaults)).setOnClickListener(new class_238(this));
        }
    }

    public String getKeyLabel(int i) {
        if (this.mKeyLabels == null) {
            this.mKeyLabels = getResources().getStringArray(R.array.keycode_labels);
        }
        if (i > 0) {
            String[] strArr = this.mKeyLabels;
            if (i < strArr.length) {
                return strArr[i - 1];
            }
        }
        return "Unknown Key";
    }
}
