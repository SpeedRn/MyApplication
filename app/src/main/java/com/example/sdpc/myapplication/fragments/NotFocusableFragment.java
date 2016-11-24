package com.example.sdpc.myapplication.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sdpc.myapplication.R;

/**
 * Created by sdpc on 16-8-31.
 */
@SuppressLint("ValidFragment")
public class NotFocusableFragment extends BaseFragment {
    private String pageTitle = "NormalFragment";
    View child;

    public NotFocusableFragment(String title) {
        this.pageTitle = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.base_fragment, null);
        child  = v.findViewById(R.id.focus_button);
        child.setFocusable(false);
        return v;
    }

    @Override
    public void release() {

    }

    @Override
    public void start() {

    }

    @Override
    public String getPageTitle() {
        return pageTitle;
    }
}
