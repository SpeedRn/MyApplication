package com.example.sdpc.myapplication.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.sdpc.myapplication.R;
import com.example.sdpc.myapplication.widget.interfaces.TabAnimatable;

/**
 * Item to be shown on {@link TabStripImpl}
 * Created by ShaoDong on 16-9-21.
 */
public class TabItem extends FrameLayout implements TabAnimatable {
    private static String TAG = TabItem.class.getSimpleName();
    private AlphaGradientTextView tabTextView;
    private Drawable mTempBackGroundDrawable;
    private ImageView animateView;
    private boolean mIsRunning;

    public TabItem(Context context) {
        this(context, null);
    }

    public TabItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.tabTextView = new AlphaGradientTextView(context);
        setClipToPadding(false);
        setClipChildren(false);
        tabTextView.setGravity(Gravity.CENTER);
        addView(tabTextView);
    }

    public AlphaGradientTextView getTabTextView() {
        return tabTextView;
    }

    @Override
    public void in() {
        Log.d(TAG, "animate in");
        animateView = new ImageView(getContext());
        LayoutParams lp =  new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT | Gravity.TOP;
        lp.setMargins(0,-40,-40,0);
        animateView.setTag("temp");
        animateView.setLayoutParams(lp);
        animateView.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.home));
        addView(animateView);
        clearAndStoreBackground();
        TranslateAnimation tAnimation = new TranslateAnimation(0, 0, -getHeight(), 0);
        tAnimation.setDuration(1000);
        tAnimation.setFillAfter(true);
        animateView.startAnimation(tAnimation);
        tAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsRunning = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void out() {
        for (int i = 0; i < getChildCount(); i++) {
            final View v = getChildAt(i);
            if (TextUtils.equals("temp", (String) v.getTag())) {
                Log.d(TAG, "animate out");
                TranslateAnimation tAnimation = new TranslateAnimation(0, 0, 0, getHeight());
                tAnimation.setDuration(1000);
                tAnimation.setFillAfter(true);
                v.startAnimation(tAnimation);
                tAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mIsRunning = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        removeView(v);
                        restoreBackground();
                        mIsRunning = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }
    }

    @Override
    public void clearTabAnimation() {
        if (isRunning()) {
            animateView.clearAnimation();
        }
        removeView(animateView);
    }

    /**
     * clear this view's background to out stand the foreground view
     */
    private void clearAndStoreBackground() {
        mTempBackGroundDrawable = getBackground();
        setBackgroundDrawable(null);
    }

    private void restoreBackground() {
        setBackgroundDrawable(mTempBackGroundDrawable);
    }

    @Override
    public boolean isRunning() {
        return mIsRunning;
    }

    public void setText(String content){
        tabTextView.setText(content);
        tabTextView.requestLayout();
    }
}
