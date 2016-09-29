package com.example.sdpc.myapplication.widget.interfaces;

/**
 * Created by shaodong on 16-9-22.
 */

public interface TabAnimatable {
    /**
     * different from {@link #out()} clearTabAnimation when its running
     */
    void clearTabAnimation();

    /**
     * play a fade out animation
     */
    void out();

    /**
     * play a fade in animation
     */
    void in();

    boolean isRunning();
}
