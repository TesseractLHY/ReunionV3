package com.corrodinggames.rts.appFramework;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.corrodinggames.rts.R;
import com.corrodinggames.rts.gameFramework.h.class_988;

public class CreditsActivity extends Activity {
    @Override
    public void onPause() {
        super.onPause();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTitle("Credits");
        setVolumeControlStream(3);
        requestWindowFeature(1);
        setContentView(R.layout.credits);
        getWindow().setBackgroundDrawable(null);
        ((TextView) findViewById(R.id.creditsText)).setText(class_988.method_2636("menus.credits.main") + "\n" + class_988.method_2636("menus.credits.notices"));
        findViewById(R.id.creditsClose).setOnClickListener(view -> finish());
    }
}
