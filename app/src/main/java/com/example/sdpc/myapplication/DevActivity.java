package com.example.sdpc.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.sdpc.myapplication.fragments.NormalFragment;
import com.example.sdpc.myapplication.fragments.NotFocusableFragment;

/**
 * Created by sdpc on 17-5-27.
 */

public class DevActivity extends FragmentActivity {
    private FrameLayout container;
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private TextView tvDevMode;
    private TextView tvFactoryMode;
    private TextView tvLeakCanary;
    private TextView tvBlockMonitor;
    private TextView tvPluginDev;
    private View.OnFocusChangeListener tabFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                return;
            }
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            switch (v.getId()) {
                case R.id.tv_dev_mode:
                    transaction.replace(R.id.container,new NotFocusableFragment("jhhe"));
                    break;
                case R.id.tv_factory_mode:
                    transaction.replace(R.id.container,new NormalFragment("jhhe"));
                    break;
                case R.id.tv_leakcanary_mode:
                    transaction.replace(R.id.container,new NotFocusableFragment("jhhe"));
                    break;
                case R.id.tv_block_monitor:
                    transaction.replace(R.id.container,new NormalFragment("jhhe"));
                    break;
                case R.id.tv_plugin_dev_mode:
                    transaction.replace(R.id.container,new NotFocusableFragment("jhhe"));
                    break;
            }
            transaction.commit();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);

        container = (FrameLayout) findViewById(R.id.container);
        tvBlockMonitor = (TextView) findViewById(R.id.tv_block_monitor);
        tvDevMode = (TextView) findViewById(R.id.tv_dev_mode);
        tvFactoryMode = (TextView) findViewById(R.id.tv_factory_mode);
        tvLeakCanary = (TextView) findViewById(R.id.tv_leakcanary_mode);
        tvPluginDev = (TextView) findViewById(R.id.tv_plugin_dev_mode);

        tvBlockMonitor.setOnFocusChangeListener(tabFocusChangeListener);
        tvDevMode.setOnFocusChangeListener(tabFocusChangeListener);
        tvFactoryMode.setOnFocusChangeListener(tabFocusChangeListener);
        tvLeakCanary.setOnFocusChangeListener(tabFocusChangeListener);
        tvPluginDev.setOnFocusChangeListener(tabFocusChangeListener);

    }

}
