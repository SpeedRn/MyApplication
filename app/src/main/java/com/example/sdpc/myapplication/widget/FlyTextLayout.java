package com.example.sdpc.myapplication.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by sdpc on 16-8-25.
 */
public class FlyTextLayout extends RelativeLayout {
    private Paint p = new Paint();
    private int textColor ;
    private int defaultTextColor = 0x8fffffff;
    private static final int[] ATTRS = new int[] {android.R.attr.textSize,
            android.R.attr.textColor ,
    android.R.attr.layout_width};



    public FlyTextLayout(Context context) {
        this(context,null);
    }

    public FlyTextLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlyTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,ATTRS);

        textColor = a.getColor(1,defaultTextColor);

        a.recycle();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * create a textView with animation:flying from center to top right.and fade out at the same time;
     * @param text the text to show
     */
    public void flyText(String text){
        final TextView t = new TextView(getContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        System.out.println(textColor);
        t.setTextColor(textColor);
        t.setLayoutParams(lp);
        t.setTextSize(30);
        t.setText(text);

        ObjectAnimator xScaleAnim = ObjectAnimator.ofFloat(t, "scaleX", 1, 1.5f,1);
        xScaleAnim.setDuration(500);
        ObjectAnimator yScaleAnim = ObjectAnimator.ofFloat(t, "scaleY", 1, 1.5f,1);
        yScaleAnim.setDuration(500);
        AnimatorSet amSet = new AnimatorSet();
        amSet.playTogether(xScaleAnim,yScaleAnim);


        int destX = getWidth()/2;
        int destY = 0-getHeight()/2;
        ObjectAnimator alpha = ObjectAnimator.ofFloat(t,"alpha",1.0f,0f);
        alpha.setDuration(2000);
        ObjectAnimator xScaleMoving = ObjectAnimator.ofFloat(t, "scaleX", 1, 0.5f);
        xScaleMoving.setDuration(1000);
        ObjectAnimator yScaleMoving = ObjectAnimator.ofFloat(t, "scaleY", 1, 0.5f);
        yScaleMoving.setDuration(1000);

        ObjectAnimator xTranslate = ObjectAnimator.ofFloat(t,"translationX",0,destX);
        xTranslate.setDuration(2000);
        ObjectAnimator yTranslate = ObjectAnimator.ofFloat(t,"translationY",0,destY);
        yTranslate.setDuration(2000);
        AnimatorSet amSet2 = new AnimatorSet();
        amSet2.playTogether(alpha,xTranslate,yTranslate,xScaleMoving,yScaleMoving);

        AnimatorSet all = new AnimatorSet();
        all.playSequentially(amSet,amSet2);
        all.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(t);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                removeView(t);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        addView(t);
        all.start();
    }

}
