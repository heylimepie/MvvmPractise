package com.limepie.mvvmandroid.webengine;

import android.content.Context;
import android.os.Looper;
import android.view.ViewGroup;

import com.just.agentweb.AgentWebView;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

class WebViewPool {
    // 缓存池需要又容器存储webview
    // 获得Webview的时候

    int poolSize;

    //    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    LinkedBlockingQueue<AgentWebView> queue;

    public WebViewPool(int size, final Context ctx) {
        poolSize = size;
        queue = new LinkedBlockingQueue<AgentWebView>(size);
//        Looper.getMainLooper();
        for (int i = 0; i < size; i++) {
            AgentWebView webView = new AgentWebView(ctx);
            queue.offer(webView);
        }
    }

    public AgentWebView getAgentWebView() {
        return queue.poll();
    }

    public void recycleWebView(AgentWebView webView) {
        if (webView == null) {
            return;
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }

        webView.stopLoading();
        if (webView.getHandler() != null) {
            webView.getHandler().removeCallbacksAndMessages(null);
        }
        webView.removeAllViews();
        ViewGroup mViewGroup = null;
        if ((mViewGroup = ((ViewGroup) webView.getParent())) != null) {
            mViewGroup.removeView(webView);
        }
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.setTag(null);
        webView.clearFormData();
        webView.clearHistory();
        webView.clearCache(true);
        webView.removeAllViewsInLayout();
        webView.loadUrl("about:blank");
        queue.offer(webView);
    }

    public void destroy() {

    }
}
