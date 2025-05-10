package com.cx.shopify.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cx.shopify.listener.OnFloatClient;
import com.cx.shopify.listener.OnFloatListener;

public class FloatWebView extends WebView {
    public FloatWebView(Context context) {
        super(context);
        init();
    }

    public FloatWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("JavascriptInterface")
    private void init() {

        setHorizontalScrollBarEnabled(false); // 隐藏水平滚动条
        setVerticalScrollBarEnabled(false); // 隐藏垂直滚动条

        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true); // 支持JS
        settings.setSupportZoom(false); // 支持手势缩放
        settings.setBuiltInZoomControls(false);// 支持手势缩放
        settings.setDisplayZoomControls(false);// 隐藏Zoom缩放按钮
        settings.setUseWideViewPort(true);// 任意比例的缩放
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDefaultFontSize(16);
        settings.setDomStorageEnabled(true);
        setBackgroundColor(Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // https配置
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
             setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            isNestedScrollingEnabled();
        setLongClickable(false);
        addJavascriptInterface(this, "Hpage");
        setWebChromeClient(mWebChromeClient);
        setWebViewClient(mClient);
//        mClient.shouldOverrideUrlLoading(this, "https://www.baidu.com/");
    }

    // 动态设置宽度
    public void setDynamicWidth(int widthPx) {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(widthPx, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            params.width = widthPx;
        }
        setLayoutParams(params);
        requestLayout(); // 请求重新布局
    }

    // 动态设置高度
    public void setDynamicHeight(int heightPx) {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx);
        } else {
            params.height = heightPx;
        }
        setLayoutParams(params);
        requestLayout();
    }

    // 同时设置宽高
    public void setDynamicSize(int widthPx, int heightPx) {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(widthPx, heightPx);
        } else {
            params.width = widthPx;
            params.height = heightPx;
        }
        setLayoutParams(params);
        requestLayout();
    }

    private OnFloatListener mOnFloatListener;

    public void setOnFloatListener(OnFloatListener onFloatListener) {
        this.mOnFloatListener = onFloatListener;
    }

    @JavascriptInterface
    public void openPage() {
        if (mOnFloatListener != null) mOnFloatListener.openPage();
    }

    @JavascriptInterface
    public void setVideoInfo(String json) {
        if (mOnFloatListener != null) mOnFloatListener.setVideoInfo(json);
    }

    @JavascriptInterface
    public void closeFloat() {
        if (mOnFloatListener != null) mOnFloatListener.closeFloat();
    }

    @JavascriptInterface
    public void toPage(String json) {
        if (mOnFloatListener != null) mOnFloatListener.toPage(json);
    }

    private OnFloatClient mOnFloatClient;

    public void setOnFloatClient(OnFloatClient onFloatClient) {
        this.mOnFloatClient = onFloatClient;
    }

    private WebViewClient mClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // TODO Auto-generated method stub
            // handler.cancel();// Android默认的处理方式
            handler.proceed();// 接受所有网站的证书
            // handleMessage(Message msg);// 进行其他处理
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (errorCode == -2 || errorCode == -10 || errorCode == -6) {

            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (mOnFloatClient != null) mOnFloatClient.onPageFinished(view, url);
            super.onPageFinished(view, url);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            clearHistory();
        }
    };

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (mOnFloatClient != null) mOnFloatClient.onProgressChanged(view, newProgress);
            super.onProgressChanged(view, newProgress);
        }


    };
}
