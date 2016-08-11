package com.example.sdpc.myapplication.manager;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.sdpc.myapplication.MainActivity;
import com.example.sdpc.myapplication.widget.interfaces.Badge;

import java.util.Map;

/**
 * The LayoutManager fo RecyclerView in DeskTopActivity
 * Created by shaodong on 16-8-8.
 */
public class DesktopLayoutManager extends LinearLayoutManager {
    private Context mContext;
    private static String TAG = DesktopLayoutManager.class.getSimpleName();

    public DesktopLayoutManager(Context context) {
        super(context);
        mContext = context;
        setOrientation(LinearLayout.HORIZONTAL);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(242, 157);
    }

    @Override
    public boolean onRequestChildFocus(RecyclerView parent, RecyclerView.State state, View child, View focused) {
        //当子view获得焦点时，触发此方法

        return super.onRequestChildFocus(parent, state, child, focused);
    }

    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        return super.onInterceptFocusSearch(focused, direction);
    }

    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        View view = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
        if(view == null && (focusDirection == View.FOCUS_RIGHT || focusDirection == View.FOCUS_LEFT)){
            view = focused;
        }
        return view;
    }

    @Override
    public void onScrollStateChanged(int state) {
        switch (state){
            case RecyclerView.SCROLL_STATE_SETTLING:
                break;
            default:break;
        }
        super.onScrollStateChanged(state);
    }
}
