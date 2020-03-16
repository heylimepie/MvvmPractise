package com.limepie.mvvmandroid.webengine;

import android.content.Context;

import com.just.agentweb.AgentWebView;

public class WebViewManager {

    private volatile static WebViewManager INSTANCE;
    WebViewPool webViewPool;

    private WebViewManager(Context ctx) {
        webViewPool = new WebViewPool(3,ctx);
    }

    public static WebViewManager getInstance(Context ctx) {
        if (INSTANCE == null) {
            synchronized (WebViewManager.class) {
                INSTANCE = new WebViewManager(ctx);
            }
        }
        return INSTANCE;
    }

    public AgentWebView getAgentWebView() {
        return webViewPool.getAgentWebView();
    }

    public void recycleWebView(AgentWebView webView) {
        webViewPool.recycleWebView(webView);
    }
}
