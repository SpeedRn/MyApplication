package com.example.sdpc.myapplication.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sdpc.myapplication.R;
import com.example.sdpc.myapplication.widget.interfaces.Badge;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by ShaoDong on 2016/8/7.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ScreenInfoHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int homePosition = -1;
    private Badge home;

    private ArrayList<String> mDataList;
    private OnKeyListener mKeyListener;
    private View.OnFocusChangeListener mFocusChangeListener;
    private OnDataSetChangedListener mDataSetChangedListener;

    public RecyclerViewAdapter(Context context) {
        this(context, new ArrayList<String>());
    }

    public RecyclerViewAdapter(Context context, ArrayList<String> data) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mDataList = data;

    }

    @Override
    public ScreenInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recycler_item, null);
        return new ScreenInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(ScreenInfoHolder holder, int position) {
        holder.title.setText(mDataList.get(position));
        holder.container.setOnFocusChangeListener(mFocusChangeListener);
        holder.container.setOnKeyListener(mKeyListener);
        holder.container.setTag(position);
        bindRec(holder.rec,"");
        if(position == homePosition){
            home.setTargetViewGroup((ViewGroup) holder.container);
            holder.home = home;
        }else if(holder.home != null){
            //this view is recycled or the home Position has changed
            holder.home.remove();
            holder.home = null;
        }
    }

    /**
     * bind the hot icon
     * @param target
     * @param url
     */
    private void bindRec(SimpleDraweeView target, String url) {
        url = "http://www.lanrentuku.com/savepic/img/allimg/1206/5-120601154112.png";
        Uri uri = Uri.parse(url);
        target.setVisibility(View.VISIBLE);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(48, 48))
                .build();
        DraweeController controller =  Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(target.getController())
                .build();
        target.setController(controller);
//        target.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void moveItem(int from, int to) {
        String s = mDataList.remove(from);
        mDataList.add(to, s);
        notifyItemMoved(from, to);
        if(mDataSetChangedListener!=null){
            mDataSetChangedListener.onItemMoved(from,to);
        }
    }

    public String deleteItem(int position) {
        notifyItemRemoved(position);
        String result = mDataList.remove(position);
        if(mDataSetChangedListener!=null){
            mDataSetChangedListener.onItemDelete(position);
        }
        return result;

    }

    public void addItem(int position, String s) {
        mDataList.add(position, s);
        notifyItemInserted(position);
        if(mDataSetChangedListener!=null){
            mDataSetChangedListener.onItemInserted(position);
        }
    }


    public void setHomePosition(int homePosition){
        this.homePosition = homePosition;
    }

    public void setHomeView(Badge home){
        this.home = home;

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

    public void setFocusChangeListener(View.OnFocusChangeListener mFocusChangeListener) {
        this.mFocusChangeListener = mFocusChangeListener;
    }

    public void setOnDataSetChangedListener(OnDataSetChangedListener mDataSetChangedListener) {
        this.mDataSetChangedListener = mDataSetChangedListener;
    }

    class ScreenInfoHolder extends RecyclerView.ViewHolder {
        private View container;
        private TextView title;
        private ImageView icon;
        private Badge home;
        private SimpleDraweeView rec;

        public ScreenInfoHolder(View itemView) {
            super(itemView);
            container = itemView;
            title = (TextView) itemView.findViewById(R.id.tv_title);
            icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            rec = (SimpleDraweeView) itemView.findViewById(R.id.iv_rec);
        }
    }

    public interface OnKeyListener extends View.OnKeyListener {
    }

    public interface OnDataSetChangedListener {
        public void onItemMoved(int from,int to);
        public void onItemDelete(int position);
        public void onItemInserted(int position);
    }
}
