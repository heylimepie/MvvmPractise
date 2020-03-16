package com.limepie.mvvmandroid.webengine;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebView;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;

@Route(path = "/webengine/webactivity")
public class WebActivity extends ComponentActivity {

    AgentWeb mAgentWeb;
    @Autowired(name = "title")
    String title;
    @Autowired(name = "url")
    String url;

    long startTimeRecord, resumeTimeRecord, pageStartRecord, pageFinishRecord;
    AgentWebView agentWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        startTimeRecord = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ARouter.getInstance().inject(this);
        FrameLayout container = findViewById(R.id.container);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        View view = findViewById(R.id.iv_close);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        agentWebView = WebViewManager.getInstance(getApplication()).getAgentWebView();
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent((FrameLayout) container, new FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebView(agentWebView)
                .setWebViewClient(mWebViewClient)
                .createAgentWeb()
                .ready()
                .go(url);
    }

    WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            pageStartRecord = System.currentTimeMillis();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            pageFinishRecord = System.currentTimeMillis();
            Log.i("yjw","show cost time " + (resumeTimeRecord - startTimeRecord));
            Log.i("yjw","resume time " + resumeTimeRecord);
            Log.i("yjw", "cost time " + pageFinishRecord +
                    " - " + pageStartRecord + " = " + (pageFinishRecord - pageStartRecord));
            Log.i("yjw","webview show cost time " + (pageFinishRecord - resumeTimeRecord));
            super.onPageFinished(view, url);
        }
    };

    WebChromeClient webChromeClient = new WebChromeClient() {

    };

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        resumeTimeRecord = System.currentTimeMillis();
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        WebViewManager.getInstance(getApplication()).recycleWebView(agentWebView);
        super.onDestroy();
    }
}
