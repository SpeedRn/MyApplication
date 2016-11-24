package com.example.sdpc.myapplication.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ViewSwitcher;

/**
 * Created by sdpc on 16-11-14.
 */

public class BaseViewSwitcher extends DraweeViewAnimator {
    /**
     * The factory used to create the two children.
     */
    ViewSwitcher.ViewFactory mFactory;

    /**
     * Creates a new empty ViewSwitcher.
     *
     * @param context the application's environment
     */
    public BaseViewSwitcher(Context context) {
        super(context);
    }

    /**
     * Creates a new empty ViewSwitcher for the given context and with the
     * specified set attributes.
     *
     * @param context the application environment
     * @param attrs   a collection of attributes
     */
    public BaseViewSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException if this switcher already contains two children
     */
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() >= 2) {
            throw new IllegalStateException("Can't add more than 2 views to a ViewSwitcher");
        }
        super.addView(child, index, params);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return ViewSwitcher.class.getName();
    }

    /**
     * Returns the next view to be displayed.
     *
     * @return the view that will be displayed after the next views flip.
     */
    public View getNextView() {
        int which = mWhichChild == 0 ? 1 : 0;
        return getChildAt(which);
    }

    private View obtainView() {
        View child = mFactory.makeView();
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
        if (lp == null) {
            lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        }
        addView(child, lp);
        return child;
    }

    /**
     * Sets the factory used to create the two views between which the
     * ViewSwitcher will flip. Instead of using a factory, you can call
     * {@link #addView(View, int, ViewGroup.LayoutParams)}
     * twice.
     *
     * @param factory the view factory used to generate the switcher's content
     */
    public void setFactory(ViewSwitcher.ViewFactory factory) {
        mFactory = factory;
        obtainView();
        obtainView();
    }

    /**
     * Reset the ViewSwitcher to hide all of the existing views and to make it
     * think that the first time animation has not yet played.
     */
    public void reset() {
        mFirstTime = true;
        View v;
        v = getChildAt(0);
        if (v != null) {
            v.setVisibility(View.GONE);
        }
        v = getChildAt(1);
        if (v != null) {
            v.setVisibility(View.GONE);
        }
    }

    /**
     * Creates views in a ViewSwitcher.
     */
    public interface ViewFactory {
        /**
         * Creates a new {@link View} to be added in a
         * {@link ViewSwitcher}.
         *
         * @return a {@link View}
         */
        View makeView();
    }

}
