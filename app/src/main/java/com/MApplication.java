package com;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.example.sdpc.myapplication.FrescoConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.stv.launcher.dev.leakcanary.LeakCanary;
import com.stv.launcher.dev.watcher.RefWatcher;

import java.util.concurrent.Executor;


/**
 * Created by sdpc on 16-7-22.
 */
public class MApplication extends Application {
    public RefWatcher watcher;
    public final Executor uiThread = new UIThreadExecutor();
    public static MApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        watcher = LeakCanary.install(this);
        Fresco.initialize(this, FrescoConfig.getImagePipelineConfig(this));
        System.out.println(Runtime.getRuntime().availableProcessors());
    }


    /**
     * An {@link java.util.concurrent.Executor} that runs tasks on the UI thread.
     */
    private static class UIThreadExecutor implements Executor {
        @Override
        public void execute(Runnable command) {
            new Handler(Looper.getMainLooper()).post(command);
        }
    }
}
