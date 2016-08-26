package com.example.sdpc.myapplication.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by sdpc on 16-8-24.
 */
public class TouchBlockLayout extends RelativeLayout {
    public TouchBlockLayout(Context context) {
        this(context,null);
    }

    public TouchBlockLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TouchBlockLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
