/*
 * BadgeView.java
 * BadgeView
 * 
 * Copyright (c) 2012 Stefan Jauker.
 * https://github.com/kodex83/BadgeView
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.sdpc.myapplication.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sdpc.myapplication.widget.interfaces.Badge;


/**
 * this badge is only suitable for FrameLayout
 */
public class BadgeTextView extends TextView implements Badge {

    private boolean mHideOnNull = true;
    LayoutParams mFrameLayoutParams;
    RelativeLayout.LayoutParams mRelativeLayoutParams;

    private static String TAG = BadgeTextView.class.getCanonicalName();
    private boolean mIsOutOfParent = false;
    private Context mContext;

    public BadgeTextView(Context context) {
        this(context, null);
    }

    public BadgeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public BadgeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init() {
        //default layout params
        mFrameLayoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        mRelativeLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mRelativeLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        // set default font
        setTextColor(Color.WHITE);
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        setPadding(dip2Px(5), dip2Px(1), dip2Px(5), dip2Px(1));

        // set default background
        setBackground(9, Color.parseColor("#d3321b"));

        setGravity(Gravity.CENTER);

        // default values
        setHideOnNull(true);
        setBadgeCount(0);

        setFocusable(false);
    }

    /**
     * set the background color and the radius
     *
     * @param dipRadius
     * @param badgeColor
     */
    public void setBackground(int dipRadius, int badgeColor) {
        int radius = dip2Px(dipRadius);
        float[] radiusArray = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};

        RoundRectShape roundRect = new RoundRectShape(radiusArray, null, null);
        ShapeDrawable bgDrawable = new ShapeDrawable(roundRect);
        bgDrawable.getPaint().setColor(badgeColor);
        setBackground(bgDrawable);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        Log.d(TAG, "this is an orphan ");
    }

    /**
     * @return Returns true if view is hidden on badge value 0 or null;
     */
    public boolean isHideOnNull() {
        return mHideOnNull;
    }

    /**
     * @param hideOnNull the hideOnNull to set
     */
    public void setHideOnNull(boolean hideOnNull) {
        mHideOnNull = hideOnNull;
        setText(getText());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setText(CharSequence text, BufferType type) {
        if (isHideOnNull() && (text == null || text.toString().equalsIgnoreCase("0"))) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
        }
        super.setText(text, type);
    }

    public void setBadgeCount(int count) {
        setText(String.valueOf(count));
    }

    public Integer getBadgeCount() {
        if (getText() == null) {
            return null;
        }

        String text = getText().toString();
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void incrementBadgeCount(int increment) {
        Integer count = getBadgeCount();
        if (count == null) {
            setBadgeCount(increment);
        } else {
            setBadgeCount(increment + count);
        }
    }

    public void decrementBadgeCount(int decrement) {
        incrementBadgeCount(-decrement);
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

        //TODO 需要判断，parent必须是wrapcontent属性，否则也认为错误
        if (target.getParent() instanceof FrameLayout || target.getParent() instanceof RelativeLayout) {
            if (getLayoutParams() == null) {
                //set default layout Params
                mFrameLayoutParams.height = ((View) target.getParent()).getMeasuredHeight();
                mFrameLayoutParams.width = ((View) target.getParent()).getMeasuredWidth();
                mRelativeLayoutParams.height = ((View) target.getParent()).getMeasuredHeight();
                mRelativeLayoutParams.width = ((View) target.getParent()).getMeasuredWidth();
                this.setLayoutParams(target.getParent() instanceof FrameLayout ? mFrameLayoutParams : mRelativeLayoutParams);
            }
            ((ViewGroup) target.getParent()).setClipChildren(!mIsOutOfParent);
            ((ViewGroup) target.getParent()).setClipToPadding(!mIsOutOfParent);
            ((ViewGroup) target.getParent()).addView(this);
        } else if (target.getParent() == null) {
            Log.e(TAG, "ParentView is needed");
        } else {
            Log.e(TAG, "ParentView must be FrameLayout or RelativeLayout!");
        }
        //TODO 此方法会丢失焦点，丢失焦点过程影响badgeView的添加逻辑,放弃此方法
//        } else if (target.getParent() instanceof ViewGroup) {
//            // use a new Framelayout container for adding badge
//            ViewGroup parentContainer = (ViewGroup) target.getParent();
//
//            int groupIndex = parentContainer.indexOfChild(target);
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
// }
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
            if (getLayoutParams() == null) {
                //set default layout Params
                mFrameLayoutParams.height = target.getMeasuredHeight();
                mFrameLayoutParams.width = target.getMeasuredWidth();
                mRelativeLayoutParams.height = target.getMeasuredHeight();
                mRelativeLayoutParams.width = target.getMeasuredWidth();
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
     * {@inheritDoc}
     */
    @Override
    public void setViewOutOfParent(boolean isOut) {
        mIsOutOfParent = isOut;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends ViewGroup.MarginLayoutParams> void setCustomizedLayoutParams(T layoutParams) {
        if (layoutParams instanceof LayoutParams) {
            mFrameLayoutParams = (LayoutParams) layoutParams;
            setLayoutParams(mFrameLayoutParams);
            return;
        }
        if (layoutParams instanceof RelativeLayout.LayoutParams) {
            mRelativeLayoutParams = (RelativeLayout.LayoutParams) layoutParams;
            setLayoutParams(mRelativeLayoutParams);
            return;
        }
        Log.e(TAG, "Params must be FrameLayoutParams or RelativeLayoutParams");
    }

    /*
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
