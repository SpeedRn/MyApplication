package com.example.sdpc.myapplication.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.sdpc.myapplication.widget.interfaces.Badge;

/**
 * this badge is only suitable for FrameLayout and RelativeLayout
 * Created by sdpc on 16-6-24.
 */
public class BadgeImageView extends ImageView implements Badge {
    FrameLayout.LayoutParams mFrameLayoutParams;
    RelativeLayout.LayoutParams mRelativeLayoutParams;

    private static String TAG = BadgeImageView.class.getCanonicalName();
    private boolean mIsOutOfParent = false;

    public BadgeImageView(Context context) {
        this(context, null);
    }

    public BadgeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        //default layout params
         mFrameLayoutParams =new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
         mRelativeLayoutParams =new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        // set default background : transparent
        setImageResource(android.R.color.transparent);
        //that the image shown as its original size
        setScaleType(ScaleType.CENTER);
        setFocusable(false);
    }

    /**
     * {@inheritDoc}
     */
    public void setTargetView(View target) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        if (target == null) {
            return;
        }
        //TODO 需要判断，parent必须是wrap_content属性，否则也认为错误.
        if (target.getParent() instanceof FrameLayout || target.getParent() instanceof RelativeLayout) {
            if(getLayoutParams() == null){
                //set default layout Params
                mFrameLayoutParams.height = ((View)target.getParent()).getMeasuredHeight();
                mFrameLayoutParams.width = ((View)target.getParent()).getMeasuredWidth();
                mRelativeLayoutParams.height = ((View)target.getParent()).getMeasuredHeight();
                mRelativeLayoutParams.width = ((View)target.getParent()).getMeasuredWidth();
                this.setLayoutParams(target.getParent() instanceof FrameLayout ? mFrameLayoutParams : mRelativeLayoutParams);
            }
            ((ViewGroup) target.getParent()).setClipChildren(!mIsOutOfParent);
            ((ViewGroup) target.getParent()).setClipToPadding(!mIsOutOfParent);
            ((ViewGroup) target.getParent()).addView(this);
        } else if (target.getParent() == null) {
            Log.e(TAG, "ParentView is needed");
        }else{
            Log.e(TAG, "ParentView must be FrameLayout or RelativeLayout!");
        }
//        } else if (target.getParent() instanceof ViewGroup) {
//            // use a new Framelayout container for adding badge
//            ViewGroup parentContainer = (ViewGroup) target.getParent();
//            int groupIndex = parentContainer.indexOfChild(target);
            // 此处会丢失焦点，丢失焦点过程影响badgeView的添加逻辑，因此放弃使用此种逻辑
//            parentContainer.removeView(target);
//
//            FrameLayout badgeContainer = new FrameLayout(getContext());
//            ViewGroup.LayoutParams parentLayoutParams = target.getLayoutParams();
//
//            badgeContainer.setLayoutParams(parentLayoutParams);
//            target.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//            parentContainer.addView(badgeContainer, groupIndex, parentLayoutParams);
//            badgeContainer.addView(target);
//            badgeContainer.addView(this);
//            target.requestFocus();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        Log.d(TAG,"this is an orphan ");
    }

    /**
     * {@inheritDoc}
     */
    public void setViewOutOfParent(boolean isOut){
        mIsOutOfParent = isOut;
    }


    /**
     * {@inheritDoc}
     */
    public <T extends ViewGroup.MarginLayoutParams> void setCustomizedLayoutParams(T layoutParams){
        if(layoutParams instanceof FrameLayout.LayoutParams){
            mFrameLayoutParams = (FrameLayout.LayoutParams) layoutParams;
            setLayoutParams(mFrameLayoutParams);
            return;
        }
        if(layoutParams instanceof RelativeLayout.LayoutParams){
            mRelativeLayoutParams = (RelativeLayout.LayoutParams) layoutParams;
            setLayoutParams(mRelativeLayoutParams);
            return;
        }
        Log.e(TAG,"Params must be FrameLayoutParams or RelativeLayoutParams");
    }

    /**
     * {@inheritDoc}
     */
    public void setTargetViewGroup(ViewGroup target) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        if (target == null) {
            return;
        }
        if (target instanceof FrameLayout || target instanceof RelativeLayout) {
            if(getLayoutParams() == null){
                //set default layout Params
                mFrameLayoutParams.height = target.getMeasuredHeight();
                mFrameLayoutParams.width = target.getMeasuredWidth();
                mRelativeLayoutParams.height = target.getMeasuredHeight();
                mRelativeLayoutParams.width =target.getMeasuredWidth();
                this.setLayoutParams(target instanceof FrameLayout ? mFrameLayoutParams : mRelativeLayoutParams);
            }
            target.setClipChildren(!mIsOutOfParent);
            target.setClipToPadding(!mIsOutOfParent);
            target.addView(this);
        } else {
            Log.e(getClass().getSimpleName(), "target must FragmentLayout or RelativeLayout");
        }
    }

    /**
     * converts dip to px
     */
    private int dip2Px(float dip) {
        return (int) (dip * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }
    @Override
    public ViewParent getCurrentParent() {
        return getParent();
    }
}
