package com.corrodinggames.rts.appFramework;

import android.app.Activity;
import android.os.Bundle;
import com.corrodinggames.rts.R;

/* JADX INFO: loaded from: classes.dex */
public class NewMissionStarterActivity extends Activity {
    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        setup();
        class_84.method_115(this, false);
    }

    @Override // android.app.Activity
    public void onStop() {
        finish();
        super.onStop();
    }

    public void setup() {
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (class_84.method_127(this, true)) {
            setContentView(R.layout.new_mission_starter);
            setup();
        }
    }
}
