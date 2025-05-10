package com.cx.shopify.test.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cx.shopify.test.R;
import com.cx.shopify.util.ShopifyUtil;
import com.cx.shopify.view.ShopifyListView;


public class MainActivityTest extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);


        ShopifyListView mShopifyListView =findViewById(R.id.mShopifyListView);
        mShopifyListView
                .setUrl("file:///android_asset/vtest.html")
                .setOnBackListener(data -> {
                    Intent intent = new Intent();
                    intent.setClass(MainActivityTest.this, MainActivityNet.class);
                    intent.putExtra("float_url","https://quick-chatb.quickcep.com/nativeVideo.html?platform=other&accessId=14272c38-2db5-416e-ad7b-253574a96fbd&debug=true&location=homepage");
                    intent.putExtra("list_url","https://quick-chatb.quickcep.com/nativeVideo.html?platform=other&accessId=14272c38-2db5-416e-ad7b-253574a96fbd&debug=true&location=homepage&quickcepAppVideoID=1872545893175103489");
                    startActivity(intent);
        });
        ShopifyUtil.getInstance(this)
                .setOnBackListener(data -> {
                    Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
                })
                .setUrl("file:///android_asset/test.html").show();

    }
}
