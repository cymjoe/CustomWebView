package com.chunjing.cymjoe.custom_webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CustomWebView {
    Activity mActivity;
    ScrollWebView webView;

    public ScrollWebView getWebView() {
        return webView;
    }

    String title;

    public CustomWebView(Activity activity) {
        mActivity = activity;
        webView = new ScrollWebView(mActivity);
        webView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        progressView = new ProgressView(mActivity);
        progressView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
        progressView.setColor(mActivity.getResources().getColor(R.color.colorAccent));
        progressView.setProgress(0);

    }

    public CustomWebView setProgressColor(int color) {
        if (progressView == null) {
            throw new NullPointerException("progressView is null");
        }
        progressView.setColor(color);

        return this;

    }

    public CustomWebView setProgressHeight(int height) {
        if (progressView == null) {
            throw new NullPointerException("progressView is null");
        }
        progressView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));


        return this;

    }

    public CustomWebView setOnScrollListener(ScrollWebView.OnScrollChangeListener listener) {

        webView.setOnScrollChangeListener(listener);
        return this;
    }

    public static CustomWebView with(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("activity can not be null .");
        }
        return new CustomWebView(activity);
    }

    ProgressView progressView;
    ViewGroup v;

    public CustomWebView setWebParent(ViewGroup v) {
        this.v = v;
        this.v.removeAllViews();
        FrameLayout layout = new FrameLayout(mActivity);
        layout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(webView);
        layout.addView(progressView);
        this.v.addView(layout);
        init();
        return this;
    }

    TextView tvTitle;

    public CustomWebView setTitle(TextView view) {
        this.tvTitle = view;


        return this;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void onResume() {
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);

        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void onStop() {
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(false);

        }
    }


    public CustomWebView init() {
        WebSettings webSettings = webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setTextZoom(100);//设置webview缩放比例

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
        webSettings.setDomStorageEnabled(true);//开启DOM
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口

        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webView.setWebChromeClient(new MyWebCromeClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String titles = view.getTitle();
                if (!TextUtils.isEmpty(titles)) {
                    title = titles;
                    if (title != null && !title.equals("")) {

                        if (tvTitle != null) {

                            tvTitle.setText(title);
                        }
                    }
                }

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // 接受所有网站的证书
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                view.loadUrl(url);

                return true;
            }
        });
        return this;
    }

    class MyWebCromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                //加载完毕进度条消失
                progressView.setVisibility(View.GONE);
            } else {
                progressView.setVisibility(View.VISIBLE);
                //更新进度
                progressView.setProgress(newProgress);

            }
            super.onProgressChanged(view, newProgress);
        }
    }

    public CustomWebView setWebClient(WebViewClient client) {

        webView.setWebViewClient(client);

        return this;
    }

    public CustomWebView setWebChromeClient(WebChromeClient client) {

        webView.setWebChromeClient(client);

        return this;
    }

    @SuppressLint("JavascriptInterface")
    public CustomWebView addJs(Object object, String name) {
        webView.addJavascriptInterface(object, name);
        return this;
    }

    public CustomWebView loadUrl(String url) {
        Log.d("PPWQEQW", url);
        webView.loadUrl(url);
        return this;
    }

    public void onDestory() {
        webView.destroy();
        v.removeAllViews();
    }
}
