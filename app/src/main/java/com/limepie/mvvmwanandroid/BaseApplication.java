package com.limepie.mvvmwanandroid;

import android.app.Application;

import com.limepie.mvvmwanandroid.http.HttpManager;

public class BaseApplication extends Application {
    public static BaseApplication sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        init();
    }

    private void init() {
        HttpManager.getInstance();
    }
}
