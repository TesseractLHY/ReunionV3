package com.corrodinggames.rts.appFramework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import com.corrodinggames.rts.R;
import com.corrodinggames.rts.appFramework.android.AndroidSAF;
import com.corrodinggames.rts.game.units.custom.logicBooleans.VariableScope;
import com.corrodinggames.rts.gameFramework.class_1061;
import com.corrodinggames.rts.gameFramework.class_907;

/* JADX INFO: loaded from: classes.dex */
public class IntroScreenView extends View implements SurfaceHolder.Callback {
    public Paint loadingPaint;
    public int loadingTimerCount;
    public Bitmap logo;
    public SurfaceHolder surfaceHolder;
    public Paint tempPaint;

    public void init(Context context) {
        $invoke$special$init(context);
    }

    public IntroScreenView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.loadingPaint = new Paint();
        this.tempPaint = new Paint();
        Log.e(AndroidSAF.TAG, "IntroScreenView()");
        $invoke$special$init(context);
    }

    private /* synthetic */ void $invoke$special$init(Context context) {
        this.logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        this.loadingPaint.setTextAlign(Paint.Align.CENTER);
        this.loadingPaint.setTextSize(class_907.method_2255(context));
        this.loadingPaint.setAntiAlias(true);
        this.loadingPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, 0));
        this.loadingPaint.setColor(-1);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override
    public void onDraw(Canvas canvas) {
        String str;
        super.onDraw(canvas);
        if (canvas == null) {
            throw new RuntimeException("c is null");
        }
        canvas.drawColor(Color.rgb(36, 36, 36));
        float fMethod_2286 = class_907.method_2286((getWidth() - 30.0f) / this.logo.getWidth(), 0.0f, 2.0f);
        canvas.save();
        canvas.scale(fMethod_2286, fMethod_2286);
        canvas.drawBitmap(this.logo, ((getWidth() / fMethod_2286) / 2.0f) - (this.logo.getWidth() / 2.0f), ((getHeight() / fMethod_2286) / 2.0f) - (this.logo.getHeight() / 2.0f), (Paint) null);
        canvas.restore();
        class_1061 class_1061VarMethod_3076 = class_1061.method_3076();
        String str2 = VariableScope.nullOrMissingString;
        if (class_1061VarMethod_3076 == null) {
            str = VariableScope.nullOrMissingString;
        } else {
            str = class_1061VarMethod_3076.field_6441;
        }
        if (str != null) {
            str2 = str;
        }
        int i = this.loadingTimerCount + 1;
        this.loadingTimerCount = i;
        String str3 = "Loading";
        for (int i2 = 0; i2 <= i % 4; i2++) {
            str3 = str3 + ".";
        }
        canvas.drawText(class_907.method_2331("    ".concat(String.valueOf(str3))), getWidth() / 2, getHeight() - 80, this.loadingPaint);
        canvas.drawText(str2, getWidth() / 2, getHeight() - 40, this.loadingPaint);
        Log.e(AndroidSAF.TAG, "intro:drawBitmap:");
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
        }
        postInvalidate();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.e(AndroidSAF.TAG, "intro:surfaceCreated");
        this.surfaceHolder = surfaceHolder;
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}
