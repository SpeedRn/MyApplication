package com.example.sdpc.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MApplication;

/**
 * Created by sdpc on 16-7-22.
 */
public class BaseFragment extends Fragment {

    public BaseFragment(){
        System.out.println("constructor of BaseFragment!");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment,null);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MApplication)getActivity().getApplication()).watcher.watch(this);
    }
}
