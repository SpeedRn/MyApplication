package com.example.sdpc.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sdpc.myapplication.R;

/**
 * Created by ShaoDong on 2016/8/7.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ScreenInfoHolder> {
    private LayoutInflater mLayoutInflater;

    public RecyclerViewAdapter(Context context){
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public ScreenInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recycler_item,null);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(242, 157);
        view.setLayoutParams(params);
        return new ScreenInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(ScreenInfoHolder holder, int position) {
        //TODO 重用吧。。
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ScreenInfoHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView icon;
        public ScreenInfoHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            icon = (ImageView) itemView.findViewById(R.id.iv_icon);
        }


    }
}
