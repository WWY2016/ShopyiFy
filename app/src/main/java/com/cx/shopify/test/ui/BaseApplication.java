package com.cx.shopify.test.ui;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.cx.shopify.api.Shopify;

public class BaseApplication extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Shopify.init("5e2179c3-093b-4e6f-a476-d301ff850570");
    }


}
