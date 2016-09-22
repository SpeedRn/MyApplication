package com;

import android.app.Application;

import com.stv.launcher.dev.leakcanary.LeakCanary;
import com.stv.launcher.dev.watcher.RefWatcher;

/**
 * Created by sdpc on 16-7-22.
 */
public class MApplication extends Application {
    public RefWatcher watcher;
    @Override
    public void onCreate() {
        super.onCreate();
        watcher = LeakCanary.install(this);
    }
}
