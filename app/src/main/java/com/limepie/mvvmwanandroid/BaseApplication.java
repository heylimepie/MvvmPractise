package com.limepie.mvvmwanandroid;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.limepie.mvvmandroid.net.http.HttpManager;
import com.limepie.mvvmandroid.webengine.WebViewManager;


public class BaseApplication extends Application {
    public static BaseApplication sApp;
    private boolean isDebug = BuildConfig.DEBUG;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        init();
    }

    private void init() {
        if(isDebug) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(BaseApplication.this);
        WebViewManager.getInstance(sApp);
        HttpManager.getInstance();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();
    }
}
