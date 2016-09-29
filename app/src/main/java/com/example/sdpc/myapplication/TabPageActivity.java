package com.example.sdpc.myapplication;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.example.sdpc.myapplication.adapter.FragmentAdapter;
import com.example.sdpc.myapplication.fragments.BaseFragment;
import com.example.sdpc.myapplication.fragments.NormalFragment;
import com.example.sdpc.myapplication.manager.DefaultStrategy;
import com.example.sdpc.myapplication.widget.TabStripImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sdpc on 16-8-29.
 */
public class TabPageActivity extends FragmentActivity implements BaseFragment.TabAnimationCallBack {
    private static List<String> data;
    private static String[] s = {"阿斯顿发送地方f", "22asdfasdf222", "33333", "44444", "dfasdfasdfasdfasdfasdfadf", "66666"
            , "77777", "88888", "99adfasdf999", "00000", "aasdfasaaaa"
            , "bbbbb", "cca阿三地方叫ccc", "阿斯顿发送的发送", "eeeee", "ffasdfasdfasdffff"
            , "bfadfbbbb", "送的发送觉得烦", "ddasdfasdfddd", "eeeee", "ffasdfasdfasdffff"
    };

    static {
        data = Arrays.asList(s);
    }

    private ViewPager pager;
    private TabStripImpl tabStrip;
    private Button btnManager;
    FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_pager);
        final List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new NormalFragment(s[0]));
        fragments.add(new NormalFragment(s[1]));
        fragments.add(new NormalFragment(s[2]));
        fragments.add(new NormalFragment(s[3]));
        fragments.add(new NormalFragment(s[4]));
        fragments.add(new NormalFragment(s[5]));
        fragments.add(new NormalFragment(s[6]));
        fragments.add(new NormalFragment(s[7]));
        fragments.add(new NormalFragment(s[8]));
        fragments.add(new NormalFragment(s[9]));
        fragments.add(new NormalFragment(s[10]));
        fragments.add(new NormalFragment(s[11]));
        fragments.add(new NormalFragment(s[12]));
        fragments.add(new NormalFragment(s[13]));
        fragments.add(new NormalFragment(s[14]));
        fragments.add(new NormalFragment(s[15]));
        fragments.add(new NormalFragment(s[16]));
        fragments.add(new NormalFragment(s[17]));
        fragments.add(new NormalFragment(s[18]));
        fragments.add(new NormalFragment(s[19]));
        fragments.add(new NormalFragment(s[20]));

        final List<BaseFragment> fragments2 = new ArrayList<>();
        fragments2.add(new NormalFragment(s[0]));
        fragments2.add(new NormalFragment(s[1]));
        fragments2.add(new NormalFragment(s[2]));
        fragments2.add(new NormalFragment(s[3]));
        fragments2.add(new NormalFragment(s[4]));
        fragments2.add(new NormalFragment(s[5]));

        btnManager = (Button) findViewById(R.id.manager_bt);
        btnManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (adapter.getCount() == fragments.size()) {
//                    adapter.setFragments(fragments2);
//                } else {
//                    adapter.setFragments(fragments);
//                }
//                adapter.notifyStrategyChanged();
//                pager.setCurrentItem(pager.getCurrentItem() - 5);
                for (int i = 0; i < tabStrip.getTabCount(); i++) {

                    tabStrip.setTabText("222444",i);
                }
            }
        });

        pager = (ViewPager) findViewById(R.id.pager);
        tabStrip = (TabStripImpl) findViewById(R.id.tab_strip);
        //设置选中页面对应TabItem的颜色
        tabStrip.setSelectedTextColor(getResources().getColor(R.color.colorAccent));


        adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.setFragments(fragments);
        pager.setAdapter(adapter);
        //must be set after the adapter set;
        DefaultStrategy bindStrategy = new DefaultStrategy(tabStrip,pager);

        tabStrip.setBindStrategy(bindStrategy);

        tabStrip.setOnTabItemFocusChangeListener(new TabStripImpl.OnTabItemFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus, int position) {
                if (hasFocus) {
                    tabStrip.startOutTabItemAnimation(position);
                }
            }
        });
        bindStrategy.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (3 == position) {
                    tabStrip.showImportance(position);
                } else {
                    tabStrip.hideImportance();
                    if (!tabStrip.getTabItem(position).isFocused()) {
                        tabStrip.startInTabItemAnimation(position);
                    }
                    for (int i = 0; i < tabStrip.getTabCount(); i++) {
                        if (i != position) {
                            tabStrip.clearTabItemAnimation(i);
                        }
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public TabStripImpl getTab() {
        return tabStrip;
    }

    @Override
    public void startAnimation() {
    }

    @Override
    public void stopAnimation() {

    }

    @Override
    public void clearAnimation() {

    }
}
