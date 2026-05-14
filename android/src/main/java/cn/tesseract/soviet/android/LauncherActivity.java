package cn.tesseract.soviet.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.corrodinggames.rts.appFramework.IntroScreen;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, IntroScreen.class));
        finish();
    }
}
