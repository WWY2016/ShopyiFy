/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cx.shopify.fragment;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.fragment.app.DialogFragment;

import com.cx.shopify.R;
import com.cx.shopify.listener.OnBackListener;

public class H5Fragment extends DialogFragment {
    public View mView;
    public WebView mWebView;
    public ProgressBar mProgressBar;
    public String url;
    public OnBackListener onBackListener;
    public static H5Fragment newInstance() {
        return new H5Fragment();
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //隐藏title
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mView = inflater.inflate(R.layout.fragment_h5_view, container, false);
        mWebView=mView.findViewById(R.id.mWebView);
        mProgressBar=mView.findViewById(R.id.pb);
        init();
        return mView;
    }
    @SuppressLint("JavascriptInterface")
    private void init() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true); // 支持JS
        settings.setSupportZoom(false); // 支持手势缩放
        settings.setBuiltInZoomControls(false);// 支持手势缩放
        settings.setDisplayZoomControls(false);// 隐藏Zoom缩放按钮
        settings.setUseWideViewPort(true);// 任意比例的缩放
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDefaultFontSize(16);
        settings.setDomStorageEnabled(true);
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);//开启硬件加速
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // https配置
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mWebView.isNestedScrollingEnabled();
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.addJavascriptInterface(this, "Hpage");
        mWebView.setLongClickable(false);
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setWebViewClient(mClient);
        mClient.shouldOverrideUrlLoading(mWebView, this.url);
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
            super.onPageFinished(view, url);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        }
    };

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (mProgressBar == null) return;
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                if (mProgressBar.getVisibility() != View.VISIBLE) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    };

    public H5Fragment setURL(String url) {
        this.url=url;
        return this;
    }
    @JavascriptInterface
    public void closePage() {
        if(isVisible()){
            this.dismiss();
        }
    }
    @JavascriptInterface
    public void toPage(String json) {
        if(this.onBackListener!=null){
            onBackListener.onBack(json);
        }
    }

    public H5Fragment setOnBackListener(OnBackListener onBackListener) {
        this.onBackListener=onBackListener;
        return this;
    }
}
