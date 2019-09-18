package com.chunjing.cymjoe.customwebview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chunjing.cymjoe.custom_webview.CustomWebView;
import com.chunjing.cymjoe.custom_webview.ScrollWebView;

public class MainActivity extends AppCompatActivity implements ScrollWebView.OnScrollChangeListener {

    private LinearLayout ll;
    private TextView tv;
    CustomWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll = ((LinearLayout) findViewById(R.id.ll));
        tv = (findViewById(R.id.tv));
        webView = CustomWebView.with(this)
                .setWebParent(ll)
                .addJs(new TIO(), "TIO")
                .setTitle(tv)
                .init()
                .setOnScrollListener(this)
                .loadUrl("http://129.204.195.48:8123/#/Tutorial/Index?tid=69");


    }

    @Override
    protected void onDestroy() {
        webView.onDestory();
        super.onDestroy();

    }

    @Override
    public void onPageEnd(int l, int t, int oldl, int oldt) {

    }

    @Override
    public void onPageTop(int l, int t, int oldl, int oldt) {

    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {

    }

    class TIO {

        @JavascriptInterface
        public void toPlayVideo(String title, String url, String ss) {

            Log.d("PPQWEQ", title + "  " + url + "  " + ss);


        }
//
        @JavascriptInterface
        public void goWebpage(String url, String type, String ss) {
            Log.d("PPQWEQ", type + "  " + url + "  " + ss);


        }


    }
}
