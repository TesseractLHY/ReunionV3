package com.corrodinggames.rts.appFramework;

import android.app.Application;
import com.corrodinggames.rts.gameFramework.class_1061;
import com.corrodinggames.rts.gameFramework.m.class_1236;

/* JADX INFO: loaded from: classes.dex */
public class RWApplication extends Application {
    @Override // android.app.Application, android.content.ComponentCallbacks2
    public void onTrimMemory(int i) {
        super.onTrimMemory(i);
        if (i == 80 || i == 15) {
            class_1236.method_3336();
            class_1061.method_3043("Queuing gpu cache clear");
        }
        class_1061.method_3043("onTrimMemory: ".concat(String.valueOf(i)));
    }
}
