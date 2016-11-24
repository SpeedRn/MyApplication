package com.example.sdpc.myapplication.widget.interfaces;


import com.example.sdpc.myapplication.widget.TabItemNew;

/**
 * Created by shaodong on 16-9-29.
 */

public interface ITabStrip {
    public <T extends TabItemNew> void addTab(int position , T v);
    public <T extends TabItemNew> T getTabItem(int position);
    public int getTabCount();
    public void scrollToChild(int position);
    public void notifyStrategyChanged();
    public void setBindStrategy(TabPagerBindStrategy strategy);
    public void setTabText(String newText,int position);
}
