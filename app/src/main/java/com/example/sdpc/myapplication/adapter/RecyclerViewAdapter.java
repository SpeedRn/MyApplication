package com.example.sdpc.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sdpc.myapplication.R;
import com.example.sdpc.myapplication.widget.interfaces.Badge;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by ShaoDong on 2016/8/7.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ScreenInfoHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private ArrayList<String> mDataList;
    /**
     * the views needs to be attached on items when it gains focus;
     */
    private List<Badge> mBadgeList;
    private OnKeyListener mKeyListener;
    private OnFocusChangeListener mFocusChangeListener;

    public RecyclerViewAdapter(Context context){
        this(context,new ArrayList<String>());
    }
    public RecyclerViewAdapter(Context context,ArrayList<String> data){
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mDataList = data;
        mFocusChangeListener = new OnFocusChangeListener();
    }
    @Override
    public ScreenInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recycler_item,null);
        return new ScreenInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(ScreenInfoHolder holder, int position) {
        holder.title.setText(mDataList.get(position));
        holder.container.setOnFocusChangeListener(mFocusChangeListener);
        holder.container.setOnKeyListener(mKeyListener);
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void moveItem(int from,int to){
        String s = mDataList.remove(from);
        mDataList.add(to,s);
        notifyItemMoved(from,to);
    }
    public String deleteItem(int position){
        notifyItemRemoved(position);
        return mDataList.remove(position);
    }
    public void addItem(int position , String s){
        mDataList.add(position,s);
        notifyItemInserted(position);
    }

    public List<String> getmDataList() {
        return mDataList;
    }

    public void setmDataList(ArrayList<String> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    public void setKeyListener(OnKeyListener mKeyListener) {
        this.mKeyListener = mKeyListener;
    }

    class ScreenInfoHolder extends RecyclerView.ViewHolder {
        private View container;
        private TextView title;
        private ImageView icon;
        public ScreenInfoHolder(View itemView) {
            super(itemView);
            container = itemView;
            title = (TextView) itemView.findViewById(R.id.tv_title);
            icon = (ImageView) itemView.findViewById(R.id.iv_icon);
        }
    }


    public interface OnItemClickListener{
        public void onItemClicked();
    }

    public interface OnKeyListener extends View.OnKeyListener{}

    private class OnFocusChangeListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                v.findViewById(R.id.tv_title).setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
            }else{
                v.findViewById(R.id.tv_title).setBackgroundDrawable(null);
            }
        }
    }
}
