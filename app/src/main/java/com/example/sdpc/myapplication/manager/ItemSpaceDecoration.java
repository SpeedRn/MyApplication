package com.example.sdpc.myapplication.manager;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

/**
 * this class can be used as a RecyclerView's divider(with LinearLayoutManager)
 * Created by shaodong on 16-8-8.
 */
public class ItemSpaceDecoration extends RecyclerView.ItemDecoration{


    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private float mDivider;

    private int mOrientation;

    public ItemSpaceDecoration(Context context, int orientation) {
        mDivider = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,context.getResources().getDisplayMetrics());
        setOrientation(orientation);
    }
    public ItemSpaceDecoration(Context context ) {
        mDivider = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,context.getResources().getDisplayMetrics());
    }

    /**
     * set custom divider width (height)
     * @param divider the divider to be shown between items;
     */
    public void setDivider(float divider){
        mDivider = divider;
    }
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        if (mOrientation == VERTICAL_LIST) {
//            outRect.set(0, 0, 0, (int) mDivider);
//        } else {
//            outRect.set(0, 0, (int) mDivider, 0);
//        }

        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = (int) mDivider;
        outRect.bottom = (int) mDivider;
        if (parent.getChildPosition(view) %4==0) {
            outRect.left = 0;
        }
    }
}
