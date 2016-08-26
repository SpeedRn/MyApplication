package com.example.sdpc.myapplication.widget.interfaces;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * used to attach on a view witch has a FrameLayout or RelativeLayout parent
 * Created by shaodong on 16-6-28.
 */
public interface Badge {
    /**
     * Attach the BadgeView to the target view
     * target view must have a {@link RelativeLayout} or {@link FrameLayout} parent ,so that he
     *
     * @param target the view to attach the BadgeView
     */
    void setTargetView(View target);
    /**
     * remove self from parent
     */
    void remove();
    /**
     * set this badge directly to a ViewGroup (Must be FrameLayout)
     */
    void setTargetViewGroup(ViewGroup target);
    /**
     * decide whether this badge can be shown out of the target
     * @param isOut
     */
    void setViewOutOfParent(boolean isOut);
    /**
     * set LayoutParams to this badge
     * PS: developer should check the type of the layoutParams to ensure it will be valid
     * @param params the params to be set ot this badge
     */
    <T extends ViewGroup.MarginLayoutParams>void setCustomizedLayoutParams(T params);
    /**
     * return the current view that this badge is attached to;
     *
     * @return null if this badge is not attaching any ViewGroup;
     */
    ViewParent getCurrentParent();
}
