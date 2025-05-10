package com.cx.shopify.api;

public class Config {
    private static  String h5URL="https://quick-chatb.quickcep.com/nativeVideo.html";

    public static  String FloatURL=h5URL+"?platform=others&accessId="+Shopify.getAccessId()+"&location=";
    public static  String LIST_URL=h5URL+"?platform=others&accessId="+Shopify.getAccessId();
}
