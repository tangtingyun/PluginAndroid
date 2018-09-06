package com.zyf.lyn.pluginandroid;

import android.app.Application;

/**
 * Created by zhangyifei on 18/9/6.
 */

public class PluginApp extends Application {

    public static PluginApp sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }
}
