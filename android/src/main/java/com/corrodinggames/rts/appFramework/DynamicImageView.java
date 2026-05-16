package com.corrodinggames.rts.appFramework;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class DynamicImageView extends ImageView {
    public int maxFixedHeight;
    public boolean widthResize;

    public DynamicImageView(Context context) {
        super(context);
        this.widthResize = false;
        this.maxFixedHeight = -1;
    }

    public DynamicImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.widthResize = false;
        this.maxFixedHeight = -1;
    }

    public void setResizeWidth(boolean z) {
        this.widthResize = z;
    }

    public void setMaxFixedHeight(int i) {
        this.maxFixedHeight = i;
    }

    @Override
    protected void onMeasure(int var1, int var2) {
        Drawable var4 = this.getDrawable();
        if (var4 != null) {
            if (!this.widthResize && this.maxFixedHeight == -1) {
                var1 = MeasureSpec.getSize(var1);
                this.setMeasuredDimension(var1, (int) Math.ceil((double) (var1 * var4.getIntrinsicHeight()) / var4.getIntrinsicWidth()));
            } else {
                var2 = MeasureSpec.getSize(var2);
                if (this.maxFixedHeight != -1) {
                    var2 = this.maxFixedHeight;
                }

                int var3 = (int) Math.ceil(var2 * ((double) var4.getIntrinsicWidth() / var4.getIntrinsicHeight()));
                var1 = MeasureSpec.getSize(var1);
                if (var3 > var1) {
                    var2 = (int) Math.ceil((double) (var1 * var4.getIntrinsicHeight()) / var4.getIntrinsicWidth());
                } else {
                    var1 = var3;
                }

                this.setMeasuredDimension(var1, var2);
            }
        } else {
            super.onMeasure(var1, var2);
        }
    }
}
