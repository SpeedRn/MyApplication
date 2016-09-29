package com.example.sdpc.myapplication.widget.interfaces;

/**
 * Created by shaodong on 16-9-29.
 */

public interface TabPagerBindStrategy {
    public int getCount();
    public void setPagerCurrentItem(int position);
    public void setTabCurrentItem(int position);
    public String getPageTitle(int position);
}
