package com.example.sdpc.myapplication.fragments;

import android.support.v4.app.Fragment;

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
}
