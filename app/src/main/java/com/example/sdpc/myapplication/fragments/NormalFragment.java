package com.example.sdpc.myapplication.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.sdpc.myapplication.R;
import com.example.sdpc.myapplication.TabPageActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by sdpc on 16-8-31.
 */
public  class NormalFragment extends BaseFragment {
    private BaseFragment nextFragment;
    private String pageTitle = "NormalFragment";
    View child;
    View child2;
    SimpleDraweeView bg;

    public NormalFragment(){}

    public NormalFragment(String title) {
        this(title, null);
    }

    public NormalFragment(String title, BaseFragment next) {
        this.pageTitle = title;
        nextFragment = next;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.base_fragment, null);
        child = v.findViewById(R.id.focus_button);
        child2 = v.findViewById(R.id.focus_button2);
        bg = (SimpleDraweeView) v.findViewById(R.id.iv_bg);
//        bg = new SimpleDraweeView(getActivity());
//
//        bg.setLayoutParams(new LinearLayout.LayoutParams(600, 600));
        bg.setImageURI(Uri.parse("res://" + getActivity().getPackageName() + File.separator + R.raw.temp_sport));
//        ((ViewGroup) v).addView(bg);
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextFragment.release();
//                ((TabPageActivity)getActivity()).getPager().beginFakeDrag();
//                ((TabPageActivity)getActivity()).getPager().fakeDragBy(-2);
//                ((TabPageActivity)getActivity()).getPager().endFakeDrag();

//                try {
//                    Class cl = View.class;
//                    Method[] mm ;
//                    mm = cl.getDeclaredMethods();
//                    for (Method method : mm) {
//                        if (method.getName().contains("postSendViewScrolledAccessibilityEventCallback")) {
//                            System.out.println(method.getName());
//                            method.setAccessible(true);
//                            method.invoke(((TabPageActivity) getActivity()).getPager());
//                        }
//                    }
//                } catch (InvocationTargetException | IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//                ((TabPageActivity) getActivity()).getPager().postInvalidateOnAnimation();
            }
        });
        child2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextFragment.start();
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
