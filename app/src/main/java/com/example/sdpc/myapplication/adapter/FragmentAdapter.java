package com.example.sdpc.myapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.sdpc.myapplication.fragments.BaseFragment;
import com.example.sdpc.myapplication.fragments.VideoFragment;

import java.util.List;

/**
 * Created by sdpc on 16-8-31.
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {
    List<BaseFragment> fragments;

    public void setFragments(List<BaseFragment> fragments) {
        this.fragments = fragments;
    }

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getPageTitle();
    }

}
