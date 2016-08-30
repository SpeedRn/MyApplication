package com.example.sdpc.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sdpc.myapplication.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sdpc on 16-8-29.
 */
public class TabPageActivity extends AppCompatActivity {
    private static List<String> data;
    private static String[] s = {"11adsf111", "22asdfasdf222", "33333", "44444", "55555", "66666"
            , "77777", "88888", "99adfasdf999", "00000", "aasdfasaaaa"
            , "bbbbb", "cca阿三地方叫ccc", "阿斯顿发送的发送", "eeeee", "ffasdfasdfasdffff"
            , "bfadfbbbb", "ccadfaasdfasdfasdfasdfasdfsdfccc", "ddasdfasdfddd", "eeeee", "ffasdfasdfasdffff"
//            , "bbasdfadfbbb", "ccccadsfasdfc", "阿斯顿发送地方", "eeeee", "ffasdfasdfasdffff"
//            , "bbadfasdfbbb", "cadfadsfasdfcccc", "ddasdfasdfddd", "而我研究员", "ffasdfasdfasdffff"
    };

    static {
        data = Arrays.asList(s);
    }

    private ViewPager pager ;
    private PagerSlidingTabStrip tabStrip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_pager);

        pager = (ViewPager) findViewById(R.id.pager);
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tab_strip);
        tabStrip.setSelectedTextColor(getResources().getColor(R.color.colorAccent));

        pager.setAdapter(new MyAdapter());

        tabStrip.setViewPager(pager);

    }

    class MyAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(TabPageActivity.this);
            iv.setFocusable(true);
            iv.setFocusableInTouchMode(true);
            iv.setLayoutParams(new ViewPager.LayoutParams());
            iv.setBackgroundResource(R.drawable.big);
            container.addView(iv);
            return iv;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object ;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return data.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
