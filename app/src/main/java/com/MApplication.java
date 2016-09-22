package com;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.stv.launcher.dev.leakcanary.LeakCanary;
import com.stv.launcher.dev.watcher.RefWatcher;

/**
 * Created by sdpc on 16-7-22.
 */
public class MApplication extends MultiDexApplication {
    public RefWatcher watcher;
    public static MApplication INSTANCE ;
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        watcher = LeakCanary.install(this);
        Fresco.initialize(this);
    }
}
