package com.example.sdpc.myapplication.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.DraweeView;

/**
 * {@hide}
 * Created by sdpc on 16-8-31.
 */
public abstract class BaseFragment extends Fragment {

    public abstract void release();

    public abstract void start();

    public abstract String getPageTitle();

    /**
     * 插件调用的控制tab 动画显示的方法
     * TODO 感觉需要配置参数，用于识别插件身份，是插件只能控制与自己相关的tab动画显示。后续如果插件开放较多的话，建议为每个插件分配一个加密字符串作为身份象征。防止互相影响
     */
    public interface TabAnimationCallBack {
        public void startAnimation();

        public void stopAnimation();

        public void clearAnimation();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        Log.d("BaseFragment","visible :" + getUserVisibleHint());
//    }

    public void onResume(){
       super.onResume();
        if(!getUserVisibleHint()){
            setAllDraweeViewVisibility((ViewGroup) getView(), View.INVISIBLE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("BaseFragment", "visible :" + getUserVisibleHint());
        //Fragment visibility has changed
        if (isVisibleToUser) {
            setAllDraweeViewVisibility((ViewGroup) getView(), View.VISIBLE);
        } else {
            setAllDraweeViewVisibility((ViewGroup) getView(), View.INVISIBLE);
        }
    }

    private void setAllDraweeViewVisibility(ViewGroup parent, int visibility) {
        if (null == parent) {
            return;
        }
        for (int i = 0; i < parent.getChildCount(); i++) {
            View v = parent.getChildAt(i);
            if (v instanceof DraweeView) {
                //the drawable is removed when it's view becomes invisible;
                v.setVisibility(visibility);
            } else if (v instanceof ViewGroup) {
                setAllDraweeViewVisibility((ViewGroup) v, visibility);
            } else {
                continue;
            }
        }
    }
}
