package com.cx.shopify.api;

import android.text.TextUtils;
import android.util.Log;
public class Shopify {
    private static String aId;
    public static void init(String accessId){
        aId=accessId;
    }

    public static String getAccessId() {
        if(TextUtils.isEmpty(aId)){
            Log.d("shopify","使用shopify请先初始化init");
            return "";
        }
        return aId;
    }
}
