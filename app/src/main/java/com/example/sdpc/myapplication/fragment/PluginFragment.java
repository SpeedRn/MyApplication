package com.example.sdpc.myapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sdpc.myapplication.R;
import com.example.sdpc.myapplication.SdActivator;
import com.stv.launcher.sdk.fragment.BaseFragment;

import java.util.Random;

/**
 *
 * Created by sdpc on 16-6-15.
 */
public class PluginFragment extends BaseFragment {
    @Override
    public View onInflaterContent(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        //不能用自己的LayoutInflater
        // 需要使用插件使用的context获取Layoutinflater,这样，host方才能利用此context inflate布局文件
//        View view = layoutInflater.inflate(R.layout.sdfragment,null);

        return LayoutInflater.from(SdActivator.getContext()).inflate(R.layout.sdfragment,null);
    }

    @Override
    public void onFragmentSeletedPre(boolean b) {

    }

    @Override
    public void onFragmentShowChanged(boolean b) {

    }

    @Override
    public boolean onFocusRequested(int i) {
        return 17 == i || 19 == i;
    }

    @Override
    public boolean onHomeKeyEventHandled() {
        Random r = new Random();
        //有时候返回true ....
        return r.nextInt()/2 == 1;
    }

    @Override
    public void setHoverListener(View.OnHoverListener onHoverListener) {

    }
}
