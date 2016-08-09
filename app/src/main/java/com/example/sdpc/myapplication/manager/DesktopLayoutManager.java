package com.example.sdpc.myapplication.manager;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * The LayoutManager fo RecyclerView in DeskTopActivity
 * Created by shaodong on 16-8-8.
 */
public class DesktopLayoutManager extends LinearLayoutManager {
    private Context mContext;

    public DesktopLayoutManager(Context context){
        super(context);
        mContext = context;
        setOrientation(LinearLayout.HORIZONTAL);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(242,157);
    }
    @Override
    public boolean onRequestChildFocus(RecyclerView parent, RecyclerView.State state, View child, View focused) {
        //当子view获得焦点时，触发此方法
        return super.onRequestChildFocus(parent,state,child,focused);
    }

    @Override
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate) {
        //TODO 控制滚动位置
        return super.requestChildRectangleOnScreen(parent, child, rect, immediate);
    }
}
