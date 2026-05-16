package com.corrodinggames.rts.appFramework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.corrodinggames.rts.R;
import com.corrodinggames.rts.appFramework.android.AndroidSAF;
import com.corrodinggames.rts.gameFramework.class_1061;
import java.util.Timer;

/* JADX INFO: loaded from: classes.dex */
public class IntroScreen extends Activity {
    public static Timer timer;
    public boolean alreadyLoaded;
    public boolean loadingComplete;
    public boolean timerComplete;

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        class_1061.method_2981();
        class_84.method_128(this, false, true);
        setContentView(R.layout.intro_screen);
        Log.e(AndroidSAF.TAG, "introScreen()");
        findViewById(R.id.IntroSurfaceView);
        this.loadingComplete = false;
        this.timerComplete = false;
        this.alreadyLoaded = class_1061.method_3076() != null;
        class_243.method_161(this);
        class_1061.method_2999(this, new class_37(this));
        startTimer();
    }

    public void doLoadingComplete() {
        synchronized (this) {
            this.loadingComplete = true;
            showMenuIfReady();
        }
    }

    public void doTimerComplete() {
        synchronized (this) {
            this.timerComplete = true;
            showMenuIfReady();
        }
    }

    public void showMenuIfReady() {
        synchronized (this) {
            if (this.loadingComplete && this.timerComplete) {
                startActivity(new Intent(getBaseContext(), (Class<?>) MainMenuActivity.class));
                timer = null;
                finish();
            }
        }
    }

    public void startTimer() {
        synchronized (this) {
            if (timer == null) {
                timer = new Timer();
                class_38 class_38Var = new class_38(this);
                if (!this.alreadyLoaded) {
                    timer.schedule(class_38Var, 1700L);
                } else {
                    timer.schedule(class_38Var, 300L);
                }
            }
        }
    }

    @Override // android.app.Activity
    public void finish() {
        synchronized (this) {
            Timer timer2 = timer;
            if (timer2 != null) {
                timer2.cancel();
                timer = null;
            }
            super.finish();
        }
    }

    @Override // android.app.Activity
    public void onPause() {
        synchronized (this) {
            Timer timer2 = timer;
            if (timer2 != null) {
                timer2.cancel();
                timer = null;
            }
            super.onPause();
        }
    }

    @Override // android.app.Activity
    public void onResume() {
        synchronized (this) {
            startTimer();
            super.onResume();
            class_84.method_115(this, false);
        }
    }
}
