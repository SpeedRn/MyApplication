package com.example.sdpc.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.sdpc.myapplication.R;
import com.example.sdpc.myapplication.widget.FlyTextLayout;
import com.example.sdpc.myapplication.widget.interfaces.Badge;

import java.util.ArrayList;

/**
 * Created by sdpc on 16-8-25.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.NextViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private Badge home;
    private ArrayList<String> mDataList;

    public GridAdapter(Context context) {
        this(context, new ArrayList<String>());
    }

    public GridAdapter(Context context, ArrayList<String> data) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mDataList = data;

    }
    @Override
    public NextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.next_item,null);
        return new NextViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NextViewHolder holder, int position) {
        holder.vote.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.parent.flyText("+1");
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return 90;
    }

    class NextViewHolder extends RecyclerView.ViewHolder{
        private FlyTextLayout parent;
        private Button vote;
        public NextViewHolder(View itemView) {
            super(itemView);
            parent = (FlyTextLayout) itemView.findViewById(R.id.parent);
            vote = (Button) itemView.findViewById(R.id.vote);
        }
    }
}
