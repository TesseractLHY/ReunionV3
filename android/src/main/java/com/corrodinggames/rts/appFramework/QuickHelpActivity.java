package com.corrodinggames.rts.appFramework;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import com.corrodinggames.rts.R;
import com.corrodinggames.rts.gameFramework.SettingsEngine;

/* JADX INFO: loaded from: classes.dex */
public class QuickHelpActivity extends Activity {
    public Bitmap[] bitmaps;
    public Gallery gallery;
    public ImageView imageView;
    public long lockTouchTill;
    public LinearLayout outerlayout;
    public int currentImage = -1;
    public Integer[] Imgid = {Integer.valueOf(R.drawable.help1), Integer.valueOf(R.drawable.help2), Integer.valueOf(R.drawable.help3), Integer.valueOf(R.drawable.help4)};

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

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        if (this.currentImage == -1) {
            this.gallery.setSelection(0);
            setImage(0);
        }
        super.onWindowFocusChanged(z);
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        int i = 0;
        if (class_84.method_127(this, false)) {
            setContentView(R.layout.quick_help);
            System.gc();
            this.bitmaps = new Bitmap[this.Imgid.length];
            SettingsEngine settingsEngine = SettingsEngine.getInstance(getBaseContext());
            settingsEngine.hasPlayedGameOrSeenHelp = true;
            settingsEngine.save();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            while (true) {
                Bitmap[] bitmapArr = this.bitmaps;
                if (i < bitmapArr.length) {
                    bitmapArr[i] = BitmapFactory.decodeResource(getResources(), this.Imgid[i].intValue(), options);
                    i++;
                } else {
                    setup();
                    Gallery gallery = (Gallery) findViewById(R.id.quickhelp_gallery);
                    this.gallery = gallery;
                    gallery.setAdapter((SpinnerAdapter) new class_196(this, this));
                    ImageView imageView = (ImageView) findViewById(R.id.quickhelp_image);
                    this.imageView = imageView;
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    this.outerlayout = (LinearLayout) findViewById(R.id.quickhelp_outerlayout);
                    this.lockTouchTill = System.currentTimeMillis() + 500;
                    this.imageView.setOnClickListener(new class_194(this));
                    this.gallery.setOnItemClickListener(new class_195(this));
                    return;
                }
            }
        }
    }

    public void setImage(int i) {
        this.currentImage = i;
        this.imageView.setImageBitmap(this.bitmaps[i]);
        int width = this.outerlayout.getWidth();
        int height = this.outerlayout.getHeight();
        float f = width;
        if (((int) (r6.getHeight() * (f / r6.getWidth()))) < height) {
            height = (int) (f * (r6.getHeight() / r6.getWidth()));
        } else {
            width = (int) ((r6.getWidth() / r6.getHeight()) * height);
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.imageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        this.imageView.setLayoutParams(layoutParams);
    }
}
