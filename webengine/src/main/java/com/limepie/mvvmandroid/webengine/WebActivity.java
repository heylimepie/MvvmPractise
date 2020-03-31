package com.limepie.mvvmandroid.webengine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebView;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

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

        Log.i("yjw","onCreate -->" + Looper.myLooper().getThread().getName());
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
        // 写的有问题  这里应该是会阻塞
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
            super.onPageFinished(view, url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
           final CountDownLatch countDownLatch = new CountDownLatch(1);
            String url = request.getUrl().toString();


            //判断url里是否带有图片标识，带有图片标识的 直接认为是图片
            final ImageLoadFlag imageLoadFlag = new ImageLoadFlag();
            final InputStreamWrapper  inputStreamWrapper = new InputStreamWrapper();
            if(!TextUtils.isEmpty(url)&&isBitmap(url)) {
                //通过图片加载框架加载图片，获取inputStream，直接传递给WebResourceResponse,这里步骤过多，中间转了几次，做法不对
                Glide.with(WebActivity.this)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageLoadFlag.setImageLodeFlag(false);
                        countDownLatch.countDown();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Bitmap result;
                        if(resource instanceof BitmapDrawable) {
                            result = ((BitmapDrawable) resource).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            result.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
                            inputStreamWrapper.setIs(isBm);
                        }
                        imageLoadFlag.setImageLodeFlag(true);
                        countDownLatch.countDown();
                        return false;
                    }
                })
                        .preload();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(!imageLoadFlag.isImageLodeFlag()) {
                    return super.shouldInterceptRequest(view, request);
                } else {
                   WebResourceResponse webResourceResponse = new WebResourceResponse("image/*", "UTF-8", inputStreamWrapper.getIs());
                   return webResourceResponse;
                }

            } else {
                return super.shouldInterceptRequest(view, request);
            }
        }


    };

    private boolean bitmapRequest(Map<String,String> headerMap) {
        String acceptValue= headerMap.get("Accept");
        if(!TextUtils.isEmpty(acceptValue)&&acceptValue.contains("image")) {
            return true;
        }
        return false;
    }

    private boolean analysisBitmapRequest() {
        return false;
    }

    WebChromeClient webChromeClient = new WebChromeClient() {

    };

    private boolean isBitmap(String str) {
        return str.endsWith(".png")||str.endsWith(".jpg")||str.endsWith(".svg");
    }

    @Override
    protected void onPause() {
//        mAgentWeb.getWebLifeCycle().onPause();
        agentWebView.onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        resumeTimeRecord = System.currentTimeMillis();
        agentWebView.onResume();
//        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        WebViewManager.getInstance(getApplication()).recycleWebView(agentWebView);
        super.onDestroy();
    }

    class ImageLoadFlag {
        boolean imageLodeFlag;

        public boolean isImageLodeFlag() {
            return imageLodeFlag;
        }

        public void setImageLodeFlag(boolean imageLodeFlag) {
            this.imageLodeFlag = imageLodeFlag;
        }


    }

    class InputStreamWrapper {
        InputStream is;

        public InputStream getIs() {
            return is;
        }

        public void setIs(InputStream is) {
            this.is = is;
        }
    }
}
