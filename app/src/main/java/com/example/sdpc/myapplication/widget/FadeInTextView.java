package com.example.sdpc.myapplication.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

/**
 * Created by sdpc on 16-8-18.
 */
public class FadeInTextView extends TextView {
    public FadeInTextView(Context context) {
        this(context,null);
    }

    public FadeInTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FadeInTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        AlphaAnimation animation = new AlphaAnimation(0f,1.0f);
        animation.setDuration(1000);
        startAnimation(animation);
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }
}