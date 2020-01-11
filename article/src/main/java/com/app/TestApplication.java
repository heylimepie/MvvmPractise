package com.app;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.limepie.mvvmandroid.article.BuildConfig;
import com.limepie.mvvmandroid.net.http.HttpManager;

public class TestApplication extends Application {
    private boolean isDebug = BuildConfig.DEBUG;
    @Override
    public void onCreate() {
        super.onCreate();
        if(isDebug) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(TestApplication.this);
        HttpManager.getInstance();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();
    }
}
