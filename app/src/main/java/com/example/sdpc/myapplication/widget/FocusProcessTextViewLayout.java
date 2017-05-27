
package com.example.sdpc.myapplication.widget;

import com.example.sdpc.myapplication.R;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class FocusProcessTextViewLayout extends FrameLayout {

    private int mDuration;
    private float mPivotX;
    private float mPivotY;
    private float mShadowZ;
    private float mInitialValue;
    private float mFinalValue;
    private float mCurrentScaleX;
    private float mCurrentScaleY;
    private boolean mResetByAnimation;
    private ScaleXUpdateListener mScaleXUpdateListener;
    private ScaleYUpdateListener mScaleYUpdateListener;

    private ArrayList<Animator> mAnimators = new ArrayList<Animator>(3);

    public FocusProcessTextViewLayout(Context context) {
        this(context, null);
    }

    public FocusProcessTextViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusProcessTextViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FocusProcess);
        /** 防止字体放大后变虚，先设置最大字号，再缩小显示 */
        // mInitialValue为缩小比例
        mInitialValue = a.getFloat(R.styleable.FocusProcess_initNarrow, 1.0f);
        mFinalValue = a.getFloat(R.styleable.FocusProcess_finalNarrow, 1.0f);
        mDuration = a.getInt(R.styleable.FocusProcess_duration, 100);
        mPivotX = a.getFloat(R.styleable.FocusProcess_pivotX, -1);
        mPivotY = a.getFloat(R.styleable.FocusProcess_pivotY, -1);
        mShadowZ = a.getInt(R.styleable.FocusProcess_shadowZ, 0);
        mResetByAnimation = a.getBoolean(R.styleable.FocusProcess_resetByAni, false);
        a.recycle();
        setScaleX(mInitialValue);
        setScaleY(mInitialValue);

        mCurrentScaleX = mCurrentScaleY = mInitialValue;

        mScaleXUpdateListener = new ScaleXUpdateListener();
        mScaleYUpdateListener = new ScaleYUpdateListener();

        getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                setPivotX(mPivotX > 0 ? mPivotX : getWidth() / 2);
                setPivotY(mPivotY > 0 ? mPivotY : getHeight() / 2);
                FocusProcessTextViewLayout.this.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

        for (Animator a : mAnimators) {
            a.cancel();
            // a.end();
        }
        mAnimators.clear();

        ObjectAnimator xScaleAnim = null;
        ObjectAnimator yScaleAnim = null;
        ObjectAnimator upAnim = null;
        if (gainFocus) {
            xScaleAnim = ObjectAnimator.ofFloat(FocusProcessTextViewLayout.this, "scaleX", mCurrentScaleX, mFinalValue);
            yScaleAnim = ObjectAnimator.ofFloat(FocusProcessTextViewLayout.this, "scaleY", mCurrentScaleY, mFinalValue);
            xScaleAnim.setDuration(mDuration);
            yScaleAnim.setDuration(mDuration);
            xScaleAnim.addUpdateListener(mScaleXUpdateListener);
            yScaleAnim.addUpdateListener(mScaleYUpdateListener);

            if (mShadowZ > 0) {
                upAnim = ObjectAnimator.ofFloat(FocusProcessTextViewLayout.this, "translationZ", mShadowZ);
                upAnim.setInterpolator(new DecelerateInterpolator());
                upAnim.setDuration(mDuration);
            }
        } else {
            if (mResetByAnimation) {
                xScaleAnim = ObjectAnimator.ofFloat(FocusProcessTextViewLayout.this, "scaleX", mCurrentScaleX, mInitialValue);
                yScaleAnim = ObjectAnimator.ofFloat(FocusProcessTextViewLayout.this, "scaleY", mCurrentScaleY, mInitialValue);
                xScaleAnim.setDuration(mDuration);
                yScaleAnim.setDuration(mDuration);
                xScaleAnim.addUpdateListener(mScaleXUpdateListener);
                yScaleAnim.addUpdateListener(mScaleYUpdateListener);
            } else {
                FocusProcessTextViewLayout.this.setScaleX(mInitialValue);
                FocusProcessTextViewLayout.this.setScaleY(mInitialValue);
                mCurrentScaleX = mInitialValue;
                mCurrentScaleY = mInitialValue;
            }

            if (mShadowZ > 0) {
                upAnim = ObjectAnimator.ofFloat(this, "translationZ", 0);
                upAnim.setInterpolator(new AccelerateInterpolator());
                // TODO set duration 0 to fix bug
                upAnim.setDuration(0);
            }
        }

        if (xScaleAnim != null) {
            mAnimators.add(xScaleAnim);
        }
        if (yScaleAnim != null) {
            mAnimators.add(yScaleAnim);
        }
        if (upAnim != null) {
            mAnimators.add(upAnim);
        }

        AnimatorSet aniSet = new AnimatorSet();
        aniSet.playTogether(mAnimators);
        aniSet.start();
    }

    class ScaleXUpdateListener implements AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mCurrentScaleX = (Float) animation.getAnimatedValue("scaleX");
        }
    }

    class ScaleYUpdateListener implements AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mCurrentScaleY = (Float) animation.getAnimatedValue("scaleY");
        }
    }
}
