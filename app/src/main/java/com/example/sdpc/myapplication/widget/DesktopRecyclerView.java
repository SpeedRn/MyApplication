package com.example.sdpc.myapplication.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HeaderViewListAdapter;

import com.example.sdpc.myapplication.adapter.HeaderViewRecyclerAdapter;
import com.example.sdpc.myapplication.manager.DesktopLayoutManager;

import java.util.ArrayList;

/**
 * Created by shaodong on 16-8-16.
 */
public class DesktopRecyclerView extends RecyclerView {
    private static String TAG = DesktopRecyclerView.class.getSimpleName();
    private Adapter mAdapter;
    int mSelection = 0;
    ArrayList<View> mHeaderViews = new ArrayList<View>();
    ArrayList<View> mFooterViews = new ArrayList<View>();

    public DesktopRecyclerView(Context context) {
        this(context, null);
    }

    public DesktopRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DesktopRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setChildrenDrawingOrderEnabled(true);

        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(((DesktopLayoutManager)getLayoutManager()).getCurrentFocusedView() != null){
                    ((DesktopLayoutManager)getLayoutManager()).getCurrentFocusedView().requestFocus();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(((DesktopLayoutManager)getLayoutManager()).getCurrentFocusedView() != null){
                    ((DesktopLayoutManager)getLayoutManager()).getCurrentFocusedView().requestFocus();
                }
            }
        });
    }

    public void setSelection(int selection, int offset) {
        if (selection == mSelection)
            return;

        mSelection = selection;
        int mScrollToPosition = selection + offset;

        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        int firstChild = layoutManager.findFirstCompletelyVisibleItemPosition();
        int lastChild = layoutManager.findLastCompletelyVisibleItemPosition();
        int first = firstChild;
        int last = lastChild;
        if (selection >= first && selection <= last) {
            selectChild();
        } else {
            smoothScrollToPosition(mScrollToPosition);
        }
    }


    public int getSelectPosition() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            int position = (Integer) getChildAt(i).getTag();
            if (position == mSelection)
                return i;
        }
        return 0;
    }

    private void selectChild() {
        ((DesktopLayoutManager)getLayoutManager()).getCurrentFocusedView().requestFocus();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int focusedIndex = indexOfChild(getFocusedChild());
        if (focusedIndex < 0) {
            return i;
        }
        if (i < focusedIndex) {
            return i;
        }
        if (i >= focusedIndex && i < childCount - 1) {
            return i + 1;
        }
        return focusedIndex;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        if (mHeaderViews.isEmpty() && mFooterViews.isEmpty()) {
            super.setAdapter(adapter);
        } else {
            adapter = new HeaderViewRecyclerAdapter(mHeaderViews, mFooterViews, adapter);
            super.setAdapter(adapter);
        }
    }

    public void addHeaderView(View v) {
        mHeaderViews.clear();
        mHeaderViews.add(v);
        // Wrap the adapter if it wasn't already wrapped.
        if (mAdapter != null) {
            if (!(mAdapter instanceof HeaderViewRecyclerAdapter)) {
                mAdapter = new HeaderViewRecyclerAdapter(mHeaderViews, mFooterViews, mAdapter);
            }

            // In the case of re-adding a header view, or adding one later on,
            // we need to notify the observer.
                mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * this method can only be called before setting the adapter
     * @param v
     */
    public void addFooterView(View v) {
        mFooterViews.clear();
        mFooterViews.add(v);
        // Wrap the adapter if it wasn't already wrapped.
        if (mAdapter != null) {
            if (!(mAdapter instanceof HeaderViewRecyclerAdapter)) {
                mAdapter = new HeaderViewRecyclerAdapter(mHeaderViews, mFooterViews, mAdapter);
            }

            // In the case of re-adding a header view, or adding one later on,
            // we need to notify the observer.
               mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Removes a previously-added footer view.
     *
     * @param v The view to remove
     * @return
     * true if the view was removed, false if the view was not a footer view
     */
    public boolean removeFooterView(View v){
        if (mFooterViews.size() > 0) {
            boolean result = false;
            if (mAdapter != null && ((HeaderViewRecyclerAdapter) mAdapter).removeFooter(v)) {
                result = true;
                mFooterViews.remove(v);
            }
            return result;
        }
        return false;
    }

    public int getHeaderViewsCount(){
        return mHeaderViews.size();
    }
    public int getFooterViewsCount(){
        return mFooterViews.size();
    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public RecycledViewPool getRecycledViewPool() {
        return super.getRecycledViewPool();
    }

}
