package com.example.sdpc.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.sdpc.myapplication.adapter.RecyclerAnimator;
import com.example.sdpc.myapplication.adapter.RecyclerViewAdapter;
import com.example.sdpc.myapplication.manager.DesktopLayoutManager;
import com.example.sdpc.myapplication.manager.ItemSpaceDecoration;
import com.example.sdpc.myapplication.widget.BadgeImageView;
import com.example.sdpc.myapplication.widget.interfaces.Badge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    public static final String KEY_LEFT = "left";
    public static final String KEY_RIGHT = "right";
    public static final String KEY_UP = "up";
    public static final String KEY_DOWN = "down";
    public static final String KEY_HOME = "home";
    private static String TAG = MainActivity.class.getSimpleName();

    private String[] use = {"11111", "22222", "33333", "44444", "55555", "AAAAA", "DDDDD"
            ,"EEEEE", "FFFFF", "GGGGG", "44444", "55555", "AAAAA", "DDDDD","11111", "22222"
    };
    private String[] toAdd = {"66666", "77777", "88888", "99999", "00000", "BBBBB", "CCCCC"
            ,"11111", "22222", "33333", "44444", "55555", "AAAAA", "DDDDD","11111", "22222"
    };
    private ArrayList<String> toAddList = new ArrayList<>();
    private ArrayList<String> inUseList = new ArrayList<>();
    RecyclerView rlvInUse;
    RecyclerView rlvToAdd;
    Button mMain;
    private RecyclerViewAdapter mInUseAdapter;
    private RecyclerViewAdapter mtoAddAdapter;
    private RecyclerAnimator amToAdd;
    private RecyclerAnimator amInUse;
    private DesktopLayoutManager mInUseLayoutManager;
    private DesktopLayoutManager mToAddLayoutManager;

private Handler h = new Handler();
    private boolean mEditMode = false;
    public Map<String, Badge> badge_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initList(toAddList, toAdd);
        initList(inUseList, use);
        initBadgeList();

        rlvInUse = (RecyclerView) findViewById(R.id.rlv_in_use);
        rlvToAdd = (RecyclerView) findViewById(R.id.rlv_to_add);
        mMain = (Button) findViewById(R.id.btn_main);
//        mMain.setOnClickListener(new View.OnClickListener() {
//            int i = 0;
//
//            @Override
//            public void onClick(View v) {
//                rlvInUse.scrollToPosition(i);
//                i = i == rlvInUse.getAdapter().getItemCount() - 1 ? 0 : i + 1;
//            }
//        });

        amToAdd = new RecyclerAnimator();
        amInUse = new RecyclerAnimator();
        mInUseLayoutManager = new DesktopLayoutManager(this);
        mToAddLayoutManager = new DesktopLayoutManager(this);

        mInUseAdapter = new RecyclerViewAdapter(this, inUseList);
        mtoAddAdapter = new RecyclerViewAdapter(this, toAddList);
        mInUseAdapter.setKeyListener(new InUseKeyListener());
        mInUseAdapter.setFocusChangeListener(new InUseOnFocusChangeListener());
        mtoAddAdapter.setKeyListener(new ToAddKeyListener());
        mtoAddAdapter.setFocusChangeListener(new ToAddUseOnFocusChangeListener());

        rlvInUse.setLayoutManager(mInUseLayoutManager);
//        rlvInUse.addItemDecoration(new ItemSpaceDecoration(this, ItemSpaceDecoration.HORIZONTAL_LIST));
        rlvInUse.setAdapter(mInUseAdapter);
        rlvInUse.setItemAnimator(amInUse);

        rlvToAdd.setLayoutManager(mToAddLayoutManager);
//        rlvToAdd.addItemDecoration(new ItemSpaceDecoration(this, ItemSpaceDecoration.HORIZONTAL_LIST));
        rlvToAdd.setAdapter(mtoAddAdapter);
        rlvToAdd.setItemAnimator(amToAdd);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //TODO 初始化各个Badge 上下左右箭头，home icon
    private void initBadgeList() {
        badge_list = new HashMap<>();
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        frameParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        BadgeImageView arrowUp = new BadgeImageView(this);
        arrowUp.setImageResource(R.drawable.up);
        arrowUp.setViewOutOfParent(true);
        arrowUp.setPadding(0, -35, 0, 0);
        arrowUp.setCustomizedLayoutParams(frameParams);

        frameParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameParams.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
        BadgeImageView arrowLeft = new BadgeImageView(this);
        arrowLeft.setImageResource(R.drawable.left);
        arrowLeft.setViewOutOfParent(true);
        arrowLeft.setPadding(-35, 0, 0, 0);
        arrowLeft.setCustomizedLayoutParams(frameParams);

        frameParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameParams.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        BadgeImageView arrowRight = new BadgeImageView(this);
        arrowRight.setImageResource(R.drawable.right);
        arrowRight.setViewOutOfParent(true);
        arrowRight.setPadding(0, 0, -35, 0);
        arrowRight.setCustomizedLayoutParams(frameParams);

        frameParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        BadgeImageView arrowDown = new BadgeImageView(this);
        arrowDown.setImageResource(R.drawable.down);
        arrowDown.setViewOutOfParent(true);
        arrowDown.setPadding(0, 0, 0, -35);
        arrowDown.setCustomizedLayoutParams(frameParams);

        frameParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameParams.gravity = Gravity.TOP | Gravity.RIGHT;
        BadgeImageView homeIcon = new BadgeImageView(this);
        homeIcon.setImageResource(R.drawable.home);
        homeIcon.setCustomizedLayoutParams(frameParams);

        badge_list.put(KEY_LEFT, arrowLeft);
        badge_list.put(KEY_RIGHT, arrowRight);
        badge_list.put(KEY_UP, arrowUp);
        badge_list.put(KEY_DOWN, arrowDown);
        badge_list.put(KEY_HOME, homeIcon);
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
            if (amInUse.isRunning() || amToAdd.isRunning()) {
                return true;
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (!mEditMode) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                            toggleEditMode(v);
                            break;
                    }
                    return false;
                }
                //TODO need to add judge method
                judgeAction(v, keyCode);
                int from = rlvInUse.getChildPosition(v);
                int to;
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        to = from - 1;
                        updateAdapterMove(from, to, v);
                        return true;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        to = from + 1;
                        updateAdapterMove(from, to, v);
                        return true;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        updateAdapterDelete(rlvInUse.getChildPosition(v), rlvToAdd.getChildPosition(v.focusSearch(View.FOCUS_DOWN)), rlvToAdd.indexOfChild(v.focusSearch(View.FOCUS_DOWN)));
                        return true;
                    case KeyEvent.KEYCODE_DPAD_UP:
                        //DO nothing focus will move up to the button above
                        break;
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                        //TODO toggle edit mode
                        toggleEditMode(v);
                        break;
                    default:
                        break;
                }
            }
            return false;
        }

        /**
         * judge if the action is legal or not
         *
         * @param v
         * @param keyCode
         */
        private void judgeAction(View v, int keyCode) {
            //TODO 根据数据内容，判断是否响应某些按键，以及是否能够移动或这移除Item
        }

        private void updateAdapterMove(int from, int to, final View v) {
            if (to < 0 || to >= mInUseAdapter.getItemCount()) {
                return;
            }
            mInUseAdapter.moveItem(from, to);
            amInUse.addAnimationsFinishedListener(new RecyclerView.ItemAnimator.ItemAnimatorFinishedListener() {
                @Override
                public void onAnimationsFinished() {
                    //动画结束后，检查view的位置，更新箭头的状态
                    ((BaseOnFocusChangeListener) v.getOnFocusChangeListener()).updateBadges(v, mEditMode);
                }
            });
        }

        /**
         * transfer data between to Adapters, and  update UI at the same time
         *
         * @param position          position in InUseAdapter witch to be deleted
         * @param destPosition      position in ToAddAdapter witch to be added
         * @param viewGroupPosition position in ViewGroup at witch the view should get focus;
         */
        private void updateAdapterDelete(final int position, int destPosition, final int viewGroupPosition) {
            mtoAddAdapter.addItem(destPosition, mInUseAdapter.deleteItem(position));
            Log.d(TAG, "position :" + position + "--" + "destPosition :" + destPosition);
            amToAdd.addAnimationsFinishedListener(new RecyclerView.ItemAnimator.ItemAnimatorFinishedListener() {
                @Override
                public void onAnimationsFinished() {
                    rlvToAdd.getLayoutManager().getChildAt(viewGroupPosition).requestFocus();
                }
            });

            //when the viewGroupPosition <=0 adding Item won't trigger any animation,so we need to scroll to the destPosition
            if (viewGroupPosition <= 0) {
                rlvToAdd.getLayoutManager().scrollToPosition(destPosition);
            }
        }
    }


    /**
     * 开关编辑模式
     *
     * @param view the view who trigger this toggle
     */
    private void toggleEditMode(View view) {
        mEditMode = !mEditMode;
        if (mEditMode) {
            //turn on
            Log.d(TAG, "edit mode is on");

        } else {
            //turn off;
            Log.d(TAG, "edit mode is off");
        }
        ((BaseOnFocusChangeListener) view.getOnFocusChangeListener()).updateBadges(view, mEditMode);
    }

    private class ToAddKeyListener implements RecyclerViewAdapter.OnKeyListener {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (amInUse.isRunning() || amToAdd.isRunning() ) {
                return true;
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (!mEditMode) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                            toggleEditMode(v);
                            break;
                    }
                    return false;
                }
                //TODO judge if the action is legal or not

                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        break;
                    case KeyEvent.KEYCODE_DPAD_UP:
                        updateAdapterDelete(rlvToAdd.getChildPosition(v)
                                , rlvInUse.getChildPosition(v.focusSearch(View.FOCUS_UP))
                                , rlvInUse.indexOfChild(v.focusSearch(View.FOCUS_UP)));
                        return true;
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                        //TODO toggle edit mode
                        toggleEditMode(v);
                        break;
                    default:
                        break;
                }
            }
            return false;
        }

        /**
         * transfer data between to Adapters, and  update UI at the same time
         *
         * @param position          position in ToAddAdapter witch to be deleted
         * @param destPosition      position in InUseAdapter witch to be added
         * @param viewGroupPosition position in ViewGroup at witch the view should get focus;
         */
        private void updateAdapterDelete(final int position, int destPosition, final int viewGroupPosition) {
            mInUseAdapter.addItem(destPosition, mtoAddAdapter.deleteItem(position));
            Log.d(TAG, "position :" + position + "--" + "destPosition :" + destPosition);
            amInUse.addAnimationsFinishedListener(new RecyclerView.ItemAnimator.ItemAnimatorFinishedListener() {
                @Override
                public void onAnimationsFinished() {
                    rlvInUse.getLayoutManager().getChildAt(viewGroupPosition).requestFocus();
                }
            });
            //when the viewGroupPosition <=0 adding Item won't trigger any animation,so we need to scroll to the destPosition
            if (viewGroupPosition <= 0) {
                rlvInUse.getLayoutManager().scrollToPosition(destPosition);
            }
        }
    }

    private class InUseOnFocusChangeListener implements BaseOnFocusChangeListener {

        @Override
        public void onFocusChange(final View v, final boolean hasFocus) {
            if (hasFocus) {
                v.findViewById(R.id.tv_title).setBackgroundColor(getResources().getColor(R.color.colorAccent));
            } else {
                v.findViewById(R.id.tv_title).setBackgroundDrawable(null);
            }
            updateBadges(v, hasFocus);
        }

        public void updateBadges(View v, boolean hasFocus) {
            if (mEditMode) {
                if (hasFocus) {
                    //TODO 根据左右item内容的状态，判断如何绘制UI
                    badge_list.get(KEY_DOWN).setTargetViewGroup((ViewGroup) v);//v is ViewGroup
                    if (rlvInUse.getChildPosition(v) < mInUseAdapter.getItemCount() - 1) {
                        badge_list.get(KEY_RIGHT).setTargetViewGroup((ViewGroup) v);
                    } else {
                        badge_list.get(KEY_RIGHT).remove();
                    }
                    if (rlvInUse.getChildPosition(v) > 0) {
                        badge_list.get(KEY_LEFT).setTargetViewGroup((ViewGroup) v);
                    } else {
                        badge_list.get(KEY_LEFT).remove();
                    }
                    return;
                }
                badge_list.get(KEY_DOWN).remove();
                badge_list.get(KEY_RIGHT).remove();
                badge_list.get(KEY_LEFT).remove();
                return;
            }
            badge_list.get(KEY_DOWN).remove();
            badge_list.get(KEY_RIGHT).remove();
            badge_list.get(KEY_LEFT).remove();
        }
    }

    private class ToAddUseOnFocusChangeListener implements BaseOnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                v.findViewById(R.id.tv_title).setBackgroundColor(getResources().getColor(R.color.colorAccent));
            } else {
                v.findViewById(R.id.tv_title).setBackgroundDrawable(null);
            }
            updateBadges(v, hasFocus);
        }

        public void updateBadges(View v, boolean hasFocus) {
            if (mEditMode) {
                if (hasFocus) {
                    badge_list.get(KEY_UP).setTargetViewGroup((ViewGroup) v);//v is ViewGroup
                    return;
                }
                badge_list.get(KEY_UP).remove();
                return;
            }
            badge_list.get(KEY_UP).remove();
        }
    }

    public interface BaseOnFocusChangeListener extends View.OnFocusChangeListener {
        void updateBadges(View v, boolean hasFocus);
    }

}


