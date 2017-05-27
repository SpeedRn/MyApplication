package com.example.sdpc.myapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by sdpc on 16-8-24.
 */
public class TouchBlockLayout extends RelativeLayout {
    private static final String TAG = "TouchBlockLayout";


    public TouchBlockLayout(Context context) {
        this(context, null);
    }

    public TouchBlockLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchBlockLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "TouchBlockLayout onMeasure  " + widthMeasureSpec + "  " + heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "TouchBlockLayout onLayout" + l + " " + t + " " + r + " " + b);
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void attachViewToParent(View child, int index, ViewGroup.LayoutParams params) {
        super.attachViewToParent(child, index, params);
        Log.d(TAG, "TouchBlockLayout attachViewToParent");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

}
