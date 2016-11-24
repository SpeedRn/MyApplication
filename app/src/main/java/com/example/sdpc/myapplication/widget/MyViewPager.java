package com.example.sdpc.myapplication.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.sdpc.myapplication.NextDeskTop;

/**
 * Created by sdpc on 16-10-19.
 */

public class MyViewPager extends ViewPager {
    private static String TAG = "MyViewPager";

    public MyViewPager(Context context) {
        this(context, null);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

//    @Override
//    public boolean arrowScroll(int direction) {
//        View currentFocused = findFocus();
//        if (currentFocused == this) {
//            currentFocused = null;
//        } else if (currentFocused != null) {
//            boolean isChild = false;
//            for (ViewParent parent = currentFocused.getParent(); parent instanceof ViewGroup;
//                 parent = parent.getParent()) {
//                if (parent == this) {
//                    isChild = true;
//                    break;
//                }
//            }
//            if (!isChild) {
//                // This would cause the focus search down below to fail in fun ways.
//                final StringBuilder sb = new StringBuilder();
//                sb.append(currentFocused.getClass().getSimpleName());
//                for (ViewParent parent = currentFocused.getParent(); parent instanceof ViewGroup;
//                     parent = parent.getParent()) {
//                    sb.append(" => ").append(parent.getClass().getSimpleName());
//                }
//                Log.e(TAG, "arrowScroll tried to find focus based on non-child " +
//                        "current focused view " + sb.toString());
//                currentFocused = null;
//            }
//        }
//        boolean handled = false;
//        final Rect mTempRect = new Rect();
//        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused,
//                direction);
//        if (nextFocused != null && nextFocused != currentFocused) {
//            if (direction == View.FOCUS_LEFT) {
//                // If has reach the left boundary
//                final int nextLeft = getChildRectInPagerCoordinates(mTempRect, nextFocused).left;
//                final int currLeft = getChildRectInPagerCoordinates(mTempRect, currentFocused).left;
//                if (currentFocused != null && nextLeft >= currLeft && getCurrentItem() <= 1) {
//                    handled = true;
//                }
//            }
//        } else if ((direction == FOCUS_LEFT || direction == FOCUS_BACKWARD) && getCurrentItem() <= 1) {
//            // Trying to move left and nothing there; try to page.
//            handled = true;
//        }
//
//        return !handled && super.arrowScroll(direction);
//    }

    private Rect getChildRectInPagerCoordinates(Rect outRect, View child) {
        if (outRect == null) {
            outRect = new Rect();
        }
        if (child == null) {
            outRect.set(0, 0, 0, 0);
            return outRect;
        }
        outRect.left = child.getLeft();
        outRect.right = child.getRight();
        outRect.top = child.getTop();
        outRect.bottom = child.getBottom();

        ViewParent parent = child.getParent();
        while (parent instanceof ViewGroup && parent != this) {
            final ViewGroup group = (ViewGroup) parent;
            outRect.left += group.getLeft();
            outRect.right += group.getRight();
            outRect.top += group.getTop();
            outRect.bottom += group.getBottom();

            parent = group.getParent();
        }
        return outRect;
    }
}
