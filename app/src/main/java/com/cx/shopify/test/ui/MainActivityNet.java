package com.cx.shopify.test.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cx.shopify.test.R;
import com.cx.shopify.util.ShopifyUtil;
import com.cx.shopify.view.ShopifyListView;


public class MainActivityNet extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        String floatUrl = getIntent().getStringExtra("float_url");
        String listUrl = getIntent().getStringExtra("list_url");
        if (TextUtils.isEmpty(floatUrl)||TextUtils.isEmpty(listUrl)){
            Toast.makeText(this, "url为空", Toast.LENGTH_SHORT).show();
        }
        ShopifyListView mShopifyListView =findViewById(R.id.mShopifyListView);
        mShopifyListView
                .setUrl(listUrl)
                .setOnBackListener(data -> {
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        });
        ShopifyUtil su= ShopifyUtil.getInstance(this)
                .setOnBackListener(data -> {
                    Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
                })
                .setUrl(floatUrl);
        su.show();
        findViewById(R.id.bt).setOnClickListener(view -> {
            su.setUrl(floatUrl);
        });
    }
}
