package com.example.sdpc.myapplication.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * Created by ShaoDong on 2016/8/7.
 */
public class DeskTopManagerItemLayout extends FocusProcessTextViewLayout {
    public DeskTopManagerItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DeskTopManagerItemLayout(Context context) {
        super(context);
    }

    public DeskTopManagerItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }
}
