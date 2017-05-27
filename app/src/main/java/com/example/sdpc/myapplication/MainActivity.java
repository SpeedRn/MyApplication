package com.example.sdpc.myapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdpc.myapplication.adapter.RecyclerUpAnimator;
import com.example.sdpc.myapplication.adapter.RecyclerViewAdapter;
import com.example.sdpc.myapplication.manager.DesktopLayoutManager;
import com.example.sdpc.myapplication.widget.BadgeImageView;
import com.example.sdpc.myapplication.widget.DesktopRecyclerView;
import com.example.sdpc.myapplication.widget.DraweeViewSwitcher;
import com.example.sdpc.myapplication.widget.GuideView;
import com.example.sdpc.myapplication.widget.interfaces.Badge;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class MainActivity extends FragmentActivity {
    public static final String KEY_LEFT = "left";
    public static final String KEY_RIGHT = "right";
    public static final String KEY_UP = "up";
    public static final String KEY_DOWN = "down";
    public static final String KEY_HOME = "home";
    private static String TAG = MainActivity.class.getSimpleName();
    private boolean dataFromServer = true;

    private String[] use = {"11111", "22222", "33333", "44444"
            , "55555", "AAAAA", "DDDDD"
            , "11111", "22222", "33333", "44444", "55555", "AAAAA", "DDDDD", "11111", "22222"
            , "11111", "22222", "33333", "44444", "55555", "AAAAA", "DDDDD", "11111", "22222"
    };
    private String[] toAdd = {"66666", "77777", "88888", "99999", "00000"
            , "BBBBB", "CCCCC"
            , "11111", "22222", "33333", "44444", "55555", "AAAAA", "DDDDD", "11111", "22222"
    };

    private static Map<Integer, String> bigImageLocalMap = new HashMap<>();

    static {
        bigImageLocalMap.put(0, "res://com.stv.launcher" + File.separator + R.raw.temp_search);
        bigImageLocalMap.put(1, "res://com.stv.launcher" + File.separator + R.raw.temp_sport);
        bigImageLocalMap.put(2, "res://com.stv.launcher" + File.separator + R.raw.temp_sport);
        bigImageLocalMap.put(3, "res://com.stv.launcher" + File.separator + R.raw.temp_sport);
        bigImageLocalMap.put(4, "res://com.stv.launcher" + File.separator + R.raw.temp_sport);
        bigImageLocalMap.put(5, "res://com.stv.launcher" + File.separator + R.raw.temp_sport);
        bigImageLocalMap.put(6, "res://com.stv.launcher" + File.separator + R.raw.temp_sport);
//        bigImageLocalMap.put(7, "res://com.stv.launcher" + File.separator + R.drawable.temp_shopping);
        bigImageLocalMap.put(7, "asdfafdr" + File.separator + R.raw.temp_app);
    }

    private ArrayList<String> toAddList = new ArrayList<>();
    private ArrayList<String> inUseList = new ArrayList<>();
    DesktopRecyclerView rlvInUse;
    DesktopRecyclerView rlvToAdd;
    Button mMain;
    private TextView mTVTitle;
    private TextView mTVDescription;
    private DraweeViewSwitcher mSwitcher;
    private ImageView testIV;
    private View noView;
    private RecyclerViewAdapter mInUseAdapter;
    private RecyclerViewAdapter mtoAddAdapter;
    private RecyclerUpAnimator amToAdd;
    private RecyclerUpAnimator amInUse;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    System.out.println("change text view");
                    mTVDescription.setText(((Bundle) msg.obj).getString("dis"));
                    mTVTitle.setText(((Bundle) msg.obj).getString("title"));
                    break;
                case 2:
                    //TODO 动画播放
                    playAnimation();
                    sendEmptyMessageDelayed(2, 3000);
                    break;
                case 3:
                    stopAnimation();
                default:
                    break;
            }

        }
    };
    private DesktopLayoutManager mInUseLayoutManager;
    private DesktopLayoutManager mToAddLayoutManager;

    private boolean mEditMode = false;
    public Map<String, Badge> badge_list;
    private View vFooter;
    private ViewGroup rootView;
    private RectF guideRect = new RectF();
    private GuideView guideView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initList(toAddList, toAdd);
        initList(inUseList, use);
        initBadgeList();

        rlvInUse = (DesktopRecyclerView) findViewById(R.id.rlv_in_use);
        rlvToAdd = (DesktopRecyclerView) findViewById(R.id.rlv_to_add);
        noView = findViewById(R.id.no_view);
        mTVDescription = (TextView) findViewById(R.id.tv_description);
        mTVTitle = (TextView) findViewById(R.id.tv_content_title);
        mSwitcher = (DraweeViewSwitcher) findViewById(R.id.drawee_switcher);
        testIV = (ImageView) findViewById(R.id.iv_test);
        mMain = (Button) findViewById(R.id.btn_main);
        mMain.setOnClickListener(new View.OnClickListener() {
            int i = 0;

            @Override
            public void onClick(View v) {
                badge_list.get(KEY_DOWN).remove();
                badge_list.get(KEY_RIGHT).remove();
                badge_list.get(KEY_LEFT).remove();
            }
        });

        amToAdd = new RecyclerUpAnimator();
        amInUse = new RecyclerUpAnimator();
        mInUseLayoutManager = new DesktopLayoutManager(this);
        mToAddLayoutManager = new DesktopLayoutManager(this);

        mInUseAdapter = new RecyclerViewAdapter(this, inUseList);
        mtoAddAdapter = new RecyclerViewAdapter(this, toAddList);
        mtoAddAdapter.setOnDataSetChangedListener(new DataChangeListener());
        mInUseAdapter.setKeyListener(new InUseKeyListener());
        mInUseAdapter.setFocusChangeListener(new InUseOnFocusChangeListener());
        mtoAddAdapter.setKeyListener(new ToAddKeyListener());
        mtoAddAdapter.setFocusChangeListener(new ToAddUseOnFocusChangeListener());

        mInUseAdapter.setHomeView(badge_list.get(KEY_HOME));
        mInUseAdapter.setHomePosition(2);

        //TODO based on data from server.....
        vFooter = View.inflate(this, R.layout.recycler_item, null);
        if (dataFromServer) {
            //Footer view :the next desktop
            rlvToAdd.addFooterView(vFooter);
            vFooter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, TestStateListDrawActivity.class));
                }
            });
        }

        rootView = (ViewGroup) findViewById(R.id.root);
        guideView = new GuideView(this);
        rootView.addView(guideView);

        rlvInUse.setLayoutManager(mInUseLayoutManager);
        rlvInUse.setAdapter(mInUseAdapter);
        rlvInUse.setItemAnimator(amInUse);

        rlvToAdd.setLayoutManager(mToAddLayoutManager);
        rlvToAdd.setAdapter(mtoAddAdapter);
        rlvToAdd.setItemAnimator(amToAdd);


    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
//        setBigImage();

        Intent intent = getIntent();
        String s = intent.getStringExtra("testString");
        String s2 = intent.getStringExtra("testString2");
        Toast.makeText(this, s + s2, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    private void setBigImage(View v) {
//        mask.setVisibility(View.INVISIBLE);
        Random random = new Random();
        Uri uri;
        uri = Uri.parse(bigImageLocalMap.get(rlvInUse.indexOfChild(v) % 8));
//        mSwitcher.setImageURI(uri);
        uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://com.stv.launcher" + File.separator + R.raw.temp_sport);
        testIV.setImageURI(uri);

//        mask.setBackgroundDrawable(ivHEHEHE.getHierarchy().getTopLevelDrawable().getCurrent());
//        ivHEHEHE.getHierarchy().setPlaceholderImage(mask.getBackground().getConstantState().newDrawable());

    }

    //TODO 初始化各个Badge 上下左右箭头，home icon
    private void initBadgeList() {
        badge_list = new HashMap<>();
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Animation animation = new TranslateAnimation(0, 10, 0, 0);

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

    /**
     * 播放各个控件的动画
     */
    private void playAnimation() {
        Animation upAnimation = new TranslateAnimation(0, 0, 0, -20);
        upAnimation.setDuration(300);
        upAnimation.setRepeatCount(3);
        upAnimation.setRepeatMode(Animation.REVERSE);

        Animation downAnimation = new TranslateAnimation(0, 0, 0, 20);
        downAnimation.setDuration(300);
        downAnimation.setRepeatCount(3);
        downAnimation.setRepeatMode(Animation.REVERSE);

        Animation leftAnimation = new TranslateAnimation(0, -20, 0, 0);
        leftAnimation.setDuration(300);
        leftAnimation.setRepeatCount(3);
        leftAnimation.setRepeatMode(Animation.REVERSE);

        Animation rightAnimation = new TranslateAnimation(0, 20, 0, 0);
        rightAnimation.setDuration(300);
        rightAnimation.setRepeatCount(3);
        rightAnimation.setRepeatMode(Animation.REVERSE);

        ((View) badge_list.get(KEY_UP)).startAnimation(upAnimation);
        ((View) badge_list.get(KEY_LEFT)).startAnimation(leftAnimation);
        ((View) badge_list.get(KEY_RIGHT)).startAnimation(rightAnimation);
        ((View) badge_list.get(KEY_DOWN)).startAnimation(downAnimation);

    }

    private void stopAnimation() {
        ((View) badge_list.get(KEY_UP)).clearAnimation();
        ((View) badge_list.get(KEY_LEFT)).clearAnimation();
        ((View) badge_list.get(KEY_RIGHT)).clearAnimation();
        ((View) badge_list.get(KEY_DOWN)).clearAnimation();
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
                        if (v.focusSearch(View.FOCUS_DOWN) == vFooter) {
                            //there a footer view at the end of the list
                            updateAdapterDelete(rlvInUse.getChildPosition(v), rlvToAdd.getChildPosition(v.focusSearch(View.FOCUS_DOWN)), rlvToAdd.indexOfChild(v.focusSearch(View.FOCUS_DOWN)));
                            return true;
                        }
                        //no footers at the end
                        if (rlvInUse.indexOfChild(v) > rlvToAdd.indexOfChild(v.focusSearch(View.FOCUS_DOWN))) {
                            // 如果当前viewGroupPosition >目标viewGroupPosition,需要添加在目标之后
                            updateAdapterDelete(rlvInUse.getChildPosition(v)//In Use adapter position
                                    , rlvToAdd.getChildPosition(v.focusSearch(View.FOCUS_DOWN)) + 1//To add adapter position
                                    , rlvToAdd.indexOfChild(v.focusSearch(View.FOCUS_DOWN)) + 1);
                        } else {
                            updateAdapterDelete(rlvInUse.getChildPosition(v), rlvToAdd.getChildPosition(v.focusSearch(View.FOCUS_DOWN)), rlvToAdd.indexOfChild(v.focusSearch(View.FOCUS_DOWN)));
                        }
                        return true;
                    case KeyEvent.KEYCODE_DPAD_UP:
                        //DO nothing desktop_manager_focus will move up to the button above
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
            Log.d(TAG, from + ":" + to);
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
         * @param viewGroupPosition position in ViewGroup at witch the view should get desktop_manager_focus;
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

    @Override
    public Window getWindow() {
        return super.getWindow();
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
            mHandler.sendEmptyMessage(2);

        } else {
            //turn off;
            Log.d(TAG, "edit mode is off");
            mHandler.removeMessages(2);
            mHandler.sendEmptyMessage(3);
        }
        ((BaseOnFocusChangeListener) view.getOnFocusChangeListener()).updateBadges(view, mEditMode);
    }

    private class ToAddKeyListener implements RecyclerViewAdapter.OnKeyListener {

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
                //TODO judge if the action is legal or not

                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        break;
                    case KeyEvent.KEYCODE_DPAD_UP:
                        if (v.focusSearch(View.FOCUS_UP) == mMain) {
                            updateAdapterDelete(rlvToAdd.getChildPosition(v)
                                    , 0
                                    , 0);
                            return true;
                        }
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
         * @param viewGroupPosition position in ViewGroup at witch the view should get desktop_manager_focus;
         */
        private void updateAdapterDelete(final int position, int destPosition, final int viewGroupPosition) {
            if (destPosition < 0) {
                destPosition = 0;
            }
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

//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//                    }
//                }, 2000);
                int[] location = new int[2];
                Rect tempRect = new Rect();
                v.getLocationOnScreen(location);
                try {
                    Method method = v.getClass().getMethod("getBoundsOnScreen", Rect.class);
                    method.invoke(v, tempRect);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                        guideRect.top = location[1] - rootView.getPaddingTop();
//                        guideRect.left = location[0] - rootView.getPaddingLeft();
//                        guideRect.right = location[0] + v.getWidth() - rootView.getPaddingLeft();
//                        guideRect.bottom = location[1] + v.getHeight() - rootView.getPaddingTop();
                guideRect.set(tempRect);
                guideView.setClipRect(guideRect);
                guideView.setGuideBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pic_guide_ok));
                guideView.invalidate();
//                v.findViewById(R.id.tv_title).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                setBigImage(v);
            } else {
//                v.findViewById(R.id.tv_title).setBackgroundDrawable(null);
            }
//            updateBadges(v, hasFocus);

            if (hasFocus) {
                Message msg = Message.obtain();
                msg.what = 1;
                Bundle b = new Bundle();
                b.putString("dis", "asdfasdfasdf");
                b.putString("title", "asdfasdf");
                msg.obj = b;
                //TODO update title and description
                //TODO need array boundary judge]
                mHandler.removeMessages(1);
                mHandler.sendMessage(msg);
            }
        }

        public void updateBadges(View v, boolean hasFocus) {
            stopAnimation();
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
//                v.findViewById(R.id.tv_title).setBackgroundDrawable(null);
            }
            updateBadges(v, hasFocus);
        }

        public void updateBadges(View v, boolean hasFocus) {
            stopAnimation();
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

    private class DataChangeListener implements RecyclerViewAdapter.OnDataSetChangedListener {
        @Override
        public void onItemMoved(int from, int to) {

        }

        @Override
        public void onItemDelete(int position) {
            Toast.makeText(MainActivity.this
                    , "item delete and left :" + rlvToAdd.getAdapter().getItemCount(), Toast.LENGTH_SHORT).show();
            if (rlvToAdd.getAdapter().getItemCount() == 0 && !dataFromServer) {
                //TODO add no more item icon .....
                noView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onItemInserted(int position) {
            Toast.makeText(MainActivity.this
                    , "item inserted and left :" + rlvToAdd.getAdapter().getItemCount(), Toast.LENGTH_SHORT).show();
            if (!dataFromServer) {
                noView.setVisibility(View.INVISIBLE);
            }
        }
    }
}


