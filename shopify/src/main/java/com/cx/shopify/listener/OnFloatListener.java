package com.cx.shopify.listener;

public interface OnFloatListener {
    void openPage();

    void setVideoInfo(String json);

    void closeFloat();

    void toPage(String json);
}
