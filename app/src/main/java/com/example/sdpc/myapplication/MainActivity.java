package com.example.sdpc.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.example.sdpc.myapplication.adapter.RecyclerAnimator;
import com.example.sdpc.myapplication.adapter.RecyclerViewAdapter;
import com.example.sdpc.myapplication.manager.DesktopLayoutManager;
import com.example.sdpc.myapplication.manager.ItemSpaceDecoration;
import com.example.sdpc.myapplication.widget.BadgeImageView;
import com.example.sdpc.myapplication.widget.interfaces.Badge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private String[] use = {"11111", "22222", "33333", "44444", "55555","AAAAA","DDDDD"};
    private String[] toAdd = {"66666", "77777", "88888", "99999", "00000","BBBBB","CCCCC"};
    private ArrayList<String> toAddList = new ArrayList<>();
    private ArrayList<String> inUseList = new ArrayList<>();


    private RecyclerViewAdapter mInUseAdapter;
    private RecyclerViewAdapter mtoAddAdapter;
    private RecyclerAnimator amToAdd  = new RecyclerAnimator();
    private static String TAG = MainActivity.class.getSimpleName();
    RecyclerView rlvInUse;
    RecyclerView rlvToAdd;
    private boolean mEditMode = false;
    public static Map<String,Badge> BADGELIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initList(toAddList, toAdd);
        initList(inUseList, use);
        initBadgeList();

        rlvInUse = (RecyclerView) findViewById(R.id.rlv_in_use);
        rlvToAdd = (RecyclerView) findViewById(R.id.rlv_to_add);

        mInUseAdapter = new RecyclerViewAdapter(this, inUseList);
        mtoAddAdapter = new RecyclerViewAdapter(this, toAddList);
        mInUseAdapter.setKeyListener(new InUseKeyListener());

        rlvInUse.setLayoutManager(new DesktopLayoutManager(this));
        rlvInUse.addItemDecoration(new ItemSpaceDecoration(this, ItemSpaceDecoration.HORIZONTAL_LIST));
        rlvInUse.setAdapter(mInUseAdapter);
        rlvInUse.setItemAnimator(new RecyclerAnimator());



        rlvToAdd.setLayoutManager(new DesktopLayoutManager(this));
        rlvToAdd.addItemDecoration(new ItemSpaceDecoration(this, ItemSpaceDecoration.HORIZONTAL_LIST));
        rlvToAdd.setAdapter(mtoAddAdapter);
        rlvToAdd.setItemAnimator(amToAdd);

    }

    //TODO 初始化各个Badge 上下左右箭头，home icon
    private void initBadgeList() {
        BADGELIST.put("left", new BadgeImageView(this));
    }

    private void initList(ArrayList<String> list, String[] data) {
        Collections.addAll(list, data);
    }

    /**
     * Items in InUserRecyclerView should register this Listener
     */
    private class InUseKeyListener implements RecyclerViewAdapter.OnKeyListener {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        int from = rlvInUse.getChildAdapterPosition(v);
                        int to = from - 1;
                        updateAdapterMove(from, to);
                        return true;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        break;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        updateAdapterDelete(rlvInUse.getChildAdapterPosition(v),rlvToAdd.getChildAdapterPosition(v.focusSearch(View.FOCUS_DOWN)),rlvToAdd.indexOfChild(v.focusSearch(View.FOCUS_DOWN)));
                        return true;
                    case KeyEvent.KEYCODE_DPAD_UP:
                        break;
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                        //TODO toggle edit mode
                        toggleEditMode();
                        break;
                    default:
                        break;
                }
            }
            return false;
        }

        private void updateAdapterMove(int from, int to) {
            if(to <0){
                return;
            }
            mInUseAdapter.moveItem(from, to);
        }

        private void updateAdapterAdd(int position) {

        }

        /**
         * transfer data between to Adapters, and  update UI at the same time
         * @param position position in InUseAdapter witch to be deleted
         * @param destPosition position in ToAddAdapter witch to be added
         * @param viewGroupPosition position in ViewGroup at witch the view should get focus;
         */
        private void updateAdapterDelete(final int position , int destPosition,final int viewGroupPosition) {
            mtoAddAdapter.addItem(destPosition,mInUseAdapter.deleteItem(position));
            Log.d(TAG,"position :" + position + "--" + "destPosition :" + destPosition );
            amToAdd.addAnimationsFinishedListener(new RecyclerView.ItemAnimator.ItemAnimatorFinishedListener() {
                @Override
                public void onAnimationsFinished() {
                    rlvToAdd.getLayoutManager().getChildAt(viewGroupPosition).requestFocus();
                }
            });

            //when the viewGroupPosition <=0 adding Item won't trigger any animation,so we need to scroll to the destPosition
            if(viewGroupPosition <=0){
                rlvToAdd.getLayoutManager().scrollToPosition(destPosition);
            }
        }
    }

    //TODO 开关编辑模式
    private void toggleEditMode() {
        mEditMode = !mEditMode;
        if(mEditMode){
            //turn on
            return;
        }
        //turn off;


    }

    private class ToAddKeyListener implements RecyclerViewAdapter.OnKeyListener {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (0 == rlvInUse.getChildLayoutPosition(v)) {
                    }
                    return true;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    break;
                default:
                    break;
            }
            return false;
        }

//        @Override
//        public void updateAdapter(RecyclerViewAdapter adapter) {
//
//        }
    }

}

