package com.example.sdpc.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdpc.myapplication.adapter.RecyclerDownAnimator;
import com.example.sdpc.myapplication.adapter.RecyclerUpAnimator;
import com.example.sdpc.myapplication.adapter.RecyclerViewAdapter;
import com.example.sdpc.myapplication.manager.DesktopLayoutManager;
import com.example.sdpc.myapplication.widget.BadgeImageView;
import com.example.sdpc.myapplication.widget.DesktopRecyclerView;
import com.example.sdpc.myapplication.widget.interfaces.Badge;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends FragmentActivity {
    public static final String KEY_LEFT = "left";
    public static final String KEY_RIGHT = "right";
    public static final String KEY_UP = "up";
    public static final String KEY_DOWN = "down";
    public static final String KEY_HOME = "home";
    private static String TAG = MainActivity.class.getSimpleName();
    private boolean dataFromServer = true;

    private String[] use = {"11111", "22222", "33333", "44444"
//            , "55555", "AAAAA", "DDDDD"
            , "11111", "22222", "33333", "44444", "55555", "AAAAA", "DDDDD", "11111", "22222"
//            , "11111", "22222", "33333", "44444", "55555", "AAAAA", "DDDDD", "11111", "22222"
    };
    private String[] toAdd = {"66666", "77777", "88888", "99999", "00000"
//            , "BBBBB", "CCCCC"
//            , "11111", "22222", "33333", "44444", "55555", "AAAAA", "DDDDD", "11111", "22222"
    };
    private ArrayList<String> toAddList = new ArrayList<>();
    private ArrayList<String> inUseList = new ArrayList<>();
    DesktopRecyclerView rlvInUse;
    DesktopRecyclerView rlvToAdd;
    Button mMain;
    private TextView mTVTitle;
    private TextView mTVDescription;
    private SimpleDraweeView ivHEHEHE;
    private View noView;
    private View mask;
    private RecyclerViewAdapter mInUseAdapter;
    private RecyclerViewAdapter mtoAddAdapter;
    private RecyclerDownAnimator amToAdd;
    private RecyclerUpAnimator amInUse;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
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
        mask = findViewById(R.id.mask);
        mTVDescription = (TextView) findViewById(R.id.tv_description);
        mTVTitle = (TextView) findViewById(R.id.tv_content_title);
        ivHEHEHE = (SimpleDraweeView) findViewById(R.id.ivhehehe);
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

        amToAdd = new RecyclerDownAnimator();
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
                    startActivity(new Intent(MainActivity.this, NextDeskTop.class));
                }
            });
        }

        rlvInUse.setLayoutManager(mInUseLayoutManager);
        rlvInUse.setAdapter(mInUseAdapter);
        rlvInUse.setItemAnimator(amInUse);

        rlvToAdd.setLayoutManager(mToAddLayoutManager);
        rlvToAdd.setAdapter(mtoAddAdapter);
        rlvToAdd.setItemAnimator(amToAdd);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBigImage();


    }

    private void setBigImage() {
        mask.setVisibility(View.INVISIBLE);
                Uri uri = Uri.parse("http://img15.3lian.com/2015/f2/50/d/70.jpg");
//        Uri uri = Uri.parse("res://" + getPackageName() + File.separator + R.drawable.big);
        Postprocessor alphaEdgePostprocessor = new BasePostprocessor() {
            @Override
            public String getName() {
                return "AlphaProcess0r";
            }

            @Override
            public void process(Bitmap sourceImg) {

                int width = sourceImg.getWidth();
                int height = sourceImg.getHeight();

//                int[] argb = new int[width * height];
//
//                sourceImg.getPixels(argb, 0, width, 0, 0, width, height);// 获得图片的ARGB值
//                //定义一个完全现实的半径，超出半径的 ，越远，透明度越高
//                //完全透明的点-----四个顶点，及距离为 Math.sqrt(width*width + height*height) /2
//                //TODO 应该为百分比
//                double r = height / 2 * 0.5 ;//完全显示的半径
//                int max = (int) (Math.sqrt(width * width + height * height) / 2);
//                //max ~ R线性渐变
//
//                for (int i = 0; i < argb.length; i++) {
//                    // 计算透明图和透明范围
//                    int alpha = 255;
//                    //该点到中心距离。
//                    double l =  Math.sqrt(Math.pow((i % width - width / 2), 2) + Math.pow((i / width - height / 2), 2)) ;
//                    //透明度
//                    if (l >= r) {
//                        //渐进式变换
//                        alpha = (int) (255 * Math.pow((1.0 - (l - r ) / (max - r)* 1.0),2) );
//                    }
//                    argb[i] = (alpha << 24) | (argb[i] & 0x00FFFFFF);
//
//                }
//                sourceImg.setPixels(argb, 0, width, 0, 0, width, height);

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.desktop_manager_mask);
                try {
                    Canvas cv = new Canvas(sourceImg);
                    cv.drawBitmap(bitmap, 0, 0, null);
                    cv.save(Canvas.ALL_SAVE_FLAG);
                    cv.restore();
                } catch (Exception e) {
                    bitmap = null;
                    e.getStackTrace();
                }finally {
                    bitmap.recycle();
                }

//                mask.setVisibility(View.VISIBLE);
            }

        };

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
//                .setResizeOptions(new ResizeOptions(1280,600))
                .setPostprocessor(alphaEdgePostprocessor)
                .build();
//                mask.setVisibility(View.VISIBLE);
        PipelineDraweeController controller = (PipelineDraweeController)
                Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(ivHEHEHE.getController())
                        // other setters as you need
                        .build();
        ivHEHEHE.setController(controller);
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
                v.findViewById(R.id.tv_title).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                setBigImage();
            } else {
                v.findViewById(R.id.tv_title).setBackgroundDrawable(null);
            }
            updateBadges(v, hasFocus);
            Message msg = Message.obtain();
            msg.what = 1;
            Bundle b = new Bundle();
            b.putString("dis", "asdfasdfasdf");
            b.putString("title", "asdfasdf");
            msg.obj = b;
            if (hasFocus) {
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
                v.findViewById(R.id.tv_title).setBackgroundDrawable(null);
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


