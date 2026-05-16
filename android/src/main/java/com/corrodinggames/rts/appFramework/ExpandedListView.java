package com.corrodinggames.rts.appFramework;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

public class ExpandedListView extends ListView {
    public int old_count;
    public ViewGroup.LayoutParams params;

    public ExpandedListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.old_count = 0;
    }

    @Override
    public void onDraw(Canvas canvas) {
        calculateListHeight();
        super.onDraw(canvas);
    }

    public void calculateListHeight() {
        if (getCount() != this.old_count) {
            this.old_count = getCount();
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            this.params = layoutParams;
            layoutParams.height = (this.old_count > 0 ? getChildAt(0).getHeight() : 0) * getCount();
            setLayoutParams(this.params);
        }
    }
}
