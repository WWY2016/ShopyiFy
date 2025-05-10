package com.cx.shopify.listener;

import android.webkit.WebView;

public interface OnFloatClient {
   default void onPageFinished(WebView view, String url){};
    default void onProgressChanged(WebView view, int newProgress){};
}
