package com.corrodinggames.rts.appFramework;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.corrodinggames.rts.R;

public class FlowLayout extends ViewGroup {
    public int mHorizontalSpacing;
    public int mVerticalSpacing;

    public FlowLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.FlowLayout);
        try {
            this.mHorizontalSpacing = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.FlowLayout_horizontalSpacing, 0);
            this.mVerticalSpacing = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpacing, 0);
        } finally {
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    @Override
    public void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int paddingRight = getPaddingRight();
        boolean z = View.MeasureSpec.getMode(i) != MeasureSpec.UNSPECIFIED;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int childCount = getChildCount();
        int i3 = 0;
        int iMax = 0;
        int i4 = 0;
        int iMax2 = 0;
        boolean z2 = false;
        while (i3 < childCount) {
            View childAt = getChildAt(i3);
            int i5 = childCount;
            if (childAt.getVisibility() != View.GONE) {
                measureChild(childAt, i, i2);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int i6 = this.mHorizontalSpacing;
                if (layoutParams.field_127 < 0) {
                    i4 = i6;
                } else {
                    i4 = layoutParams.field_127;
                }
                if (z && (z2 || childAt.getMeasuredWidth() + paddingLeft > size - paddingRight)) {
                    paddingTop += iMax2 + this.mVerticalSpacing;
                    iMax = Math.max(iMax, paddingLeft - i4);
                    paddingLeft = getPaddingLeft();
                    iMax2 = 0;
                }
                iMax2 = Math.max(iMax2, childAt.getMeasuredHeight());
                layoutParams.field_125 = paddingLeft;
                layoutParams.field_126 = paddingTop;
                paddingLeft += childAt.getMeasuredWidth() + i4;
                z2 = layoutParams.field_128;
            }
            i3++;
            childCount = i5;
        }
        setMeasuredDimension(resolveSize(Math.max(iMax, paddingLeft - i4) + getPaddingRight(), i), resolveSize(paddingTop + iMax2 + getPaddingBottom(), i2));
    }

    @Override
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt.getVisibility() != View.GONE) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                childAt.layout(layoutParams.field_125, layoutParams.field_126, layoutParams.field_125 + childAt.getMeasuredWidth(), layoutParams.field_126 + childAt.getMeasuredHeight());
            }
        }
    }

    @Override 
    public boolean drawChild(Canvas canvas, View view, long j) {
        boolean zDrawChild = super.drawChild(canvas, view, j);
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (layoutParams.field_127 > 0) {
            view.getRight();
            view.getTop();
            view.getHeight();
        }
        if (layoutParams.field_128) {
            view.getRight();
            view.getTop();
            view.getHeight();
        }
        return zDrawChild;
    }

    @Override 
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override 
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams.width, layoutParams.height);
    }

    public class LayoutParams extends ViewGroup.LayoutParams {
        public int field_125;
        public int field_126;
        public int field_127;
        public boolean field_128;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.FlowLayout_LayoutParams);
            try {
                this.field_127 = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.FlowLayout_LayoutParams_layout_horizontalSpacing, -1);
                this.field_128 = typedArrayObtainStyledAttributes.getBoolean(R.styleable.FlowLayout_LayoutParams_layout_breakLine, false);
            } finally {
                typedArrayObtainStyledAttributes.recycle();
            }
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }
    }
}
