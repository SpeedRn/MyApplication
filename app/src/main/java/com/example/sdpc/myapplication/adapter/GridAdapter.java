package com.example.sdpc.myapplication.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.sdpc.myapplication.R;
import com.example.sdpc.myapplication.manager.VoteManager;
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

    private static final int VIEW_TYPE_OTHER = 1;

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
        if (VIEW_TYPE_OTHER == viewType) {
            View v = mLayoutInflater.inflate(R.layout.next_other_item, null);
            return new NextViewHolder(v);
        }

        View v = mLayoutInflater.inflate(R.layout.next_item, null);
        return new NextViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NextViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_OTHER:
                holder.edit.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final View content = View.inflate(mContext, R.layout.dialog_content, null);
                                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                final AlertDialog dialog = builder.setTitle("编辑")
                                        .setView(content)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dia, int which) {
                                                //TODO 网络请求
                                                Toast.makeText(mContext, ((EditText) content.findViewById(R.id.et_content)).getText().toString(), Toast.LENGTH_SHORT).show();
                                                dia.dismiss();

                                            }
                                        }).show();
                            }
                        });
                break;
            default:
                if(VoteManager.getINSTANCE().checkIsVotedLocal("key" + position)){
                    holder.vote.setText("已投票");
                    holder.vote.setEnabled(false);
                }else{
                    holder.vote.setText("投票");
                    holder.vote.setEnabled(true);
                }
                holder.vote.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //TODO vote logic http request
                                holder.parent.flyText("+1");
                                holder.vote.setText("已投票");
                                holder.vote.setEnabled(false);
                                VoteManager.getINSTANCE().recordVote("key"+position,true);
                            }
                        });
                break;
        }

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return VIEW_TYPE_OTHER;
        }
        return super.getItemViewType(position);
    }

    class NextViewHolder extends RecyclerView.ViewHolder {
        private FlyTextLayout parent;
        private Button vote;
        private Button edit;

        public NextViewHolder(View itemView) {
            super(itemView);
            parent = (FlyTextLayout) itemView.findViewById(R.id.parent);
            vote = (Button) itemView.findViewById(R.id.vote);
            edit = (Button) itemView.findViewById(R.id.btn_edit);
        }
    }
}
