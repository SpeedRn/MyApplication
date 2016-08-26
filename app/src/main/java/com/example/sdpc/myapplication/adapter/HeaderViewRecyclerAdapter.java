package com.example.sdpc.myapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.ArrayList;

/**
 * NOTE :Header View will cause index chaos when moving item
 * here I just use the Footer View Function
 * Created by Shaodong on 16-8-22.
 */
public class HeaderViewRecyclerAdapter extends RecyclerView.Adapter{
    /**
     * The item view type returned by {@link Adapter#getItemViewType(int)} when
     * the item is a header or footer.
     */
    public static final int ITEM_VIEW_TYPE_HEADER = -2;
    public static final int ITEM_VIEW_TYPE_FOOTER = -1;
    private RecyclerView.Adapter mAdapter;
    //even though this is a list but we can only use one header or footer item...tragedy
    ArrayList<View> mHeaderViews;
    ArrayList<View> mFooterViews;

    // Used as a placeholder in case the provided info views are indeed null.
    // Currently only used by some CTS tests, which may be removed.
    static final ArrayList<View> EMPTY_INFO_LIST = new ArrayList<View>();

    public HeaderViewRecyclerAdapter(ArrayList<View> headerViews,ArrayList<View> footerViews,RecyclerView.Adapter adapter){
        mAdapter = adapter;

        if (headerViews == null) {
            mHeaderViews = EMPTY_INFO_LIST;
        } else {
            mHeaderViews = headerViews;
        }
        if (footerViews == null) {
            mFooterViews = EMPTY_INFO_LIST;
        } else {
            mFooterViews = footerViews;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_VIEW_TYPE_HEADER){
            return new HeaderViewHolder(mHeaderViews.get(0));
        }
        if(viewType == ITEM_VIEW_TYPE_FOOTER){
            return new HeaderViewHolder(mFooterViews.get(0));
        }
        return mAdapter.onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //TODO
        int numHeaders = getHeadersCount();
        if (mAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                if(mAdapter instanceof RecyclerView.Adapter){
                    mAdapter.onBindViewHolder(holder,adjPosition);
                }
            }
        }
        if(position == getItemCount()-1){
            ((HeaderViewHolder)holder).v.setTag(position);
        }

    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFooterViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        int numHeaders = getHeadersCount();
        if(position < numHeaders){
            return ITEM_VIEW_TYPE_HEADER;
        }
        if (mAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = mAdapter.getItemCount();

            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }
        return ITEM_VIEW_TYPE_FOOTER;
    }

    @Override
    public int getItemCount() {
        if (mAdapter != null) {
            return getFootersCount() + getHeadersCount() + mAdapter.getItemCount();
        } else {
            return getFootersCount() + getHeadersCount();
        }
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (mAdapter != null) {
            mAdapter.registerAdapterDataObserver(observer);
        }
    }

    @Override
    public long getItemId(int position) {
        int numHeaders = getHeadersCount();
        if (mAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(observer);
        }
    }


    public boolean removeHeader(View v) {
        for (int i = 0; i < mHeaderViews.size(); i++) {
            View tempView = mHeaderViews.get(i);
            if (tempView == v) {
                mHeaderViews.remove(i);

                return true;
            }
        }

        return false;
    }

    public boolean removeFooter(View v) {
        for (int i = 0; i < mFooterViews.size(); i++) {
            View tempView = mFooterViews.get(i);
            if (tempView == v) {
                mFooterViews.remove(i);

                return true;
            }
        }
        return false;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public View v;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            v = itemView;
        }
    }

    public RecyclerView.Adapter getAdapter(){
        return mAdapter;
    }
}
