package com.example.sdpc.myapplication.manager;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sdpc on 16-8-25.
 */
public class NextDeskLayoutManager extends GridLayoutManager {
    public NextDeskLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public NextDeskLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                380);
    }


    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.onFocusSearchFailed(focused, focusDirection, recycler, state);
    }

    @Override
    public boolean onRequestChildFocus(RecyclerView parent, RecyclerView.State state, View child, View focused) {
        smoothScrollToPosition(parent,state,parent.getChildPosition((View)focused.getParent()));
        return true;
    }
}
