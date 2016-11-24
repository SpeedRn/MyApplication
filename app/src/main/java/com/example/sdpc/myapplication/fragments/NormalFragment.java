package com.example.sdpc.myapplication.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.sdpc.myapplication.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

/**
 * Created by sdpc on 16-8-31.
 */
@SuppressLint("ValidFragment")
public class NormalFragment extends BaseFragment {
    private BaseFragment nextFragment;
    private String pageTitle = "NormalFragment";
    View child;
    SimpleDraweeView bg;

    public NormalFragment(String title) {
        this(title,null);
    }

    public NormalFragment(String title,BaseFragment next){
        this.pageTitle = title;
        nextFragment = next;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.base_fragment, null);
        child = v.findViewById(R.id.focus_button);
//        bg = (SimpleDraweeView) v.findViewById(R.id.iv_bg);
        bg = new SimpleDraweeView(getActivity());

        bg.setLayoutParams(new LinearLayout.LayoutParams(600,600));
        bg.setImageURI(Uri.parse("res://" + getActivity().getPackageName() + File.separator + R.drawable.big));
        ((ViewGroup)v).addView(bg);
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextFragment.release();
            }
        });
        return v;
    }

    public void setFocusable(boolean focusable) {
        if (child == null) {
            return;
        }
        child.setFocusable(focusable);
        child.setVisibility(View.INVISIBLE);
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
