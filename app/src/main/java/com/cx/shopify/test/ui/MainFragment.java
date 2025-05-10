package com.cx.shopify.test.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cx.shopify.test.R;
import com.cx.shopify.util.ShopifyUtil;
import com.cx.shopify.view.ShopifyListView;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_main_test, null);
        ShopifyListView mShopifyListView =view.findViewById(R.id.mShopifyListView);
        mShopifyListView
                .setUrl("file:///android_asset/vtest.html")
                .setOnBackListener(data -> {
                    startActivity(new Intent(getContext(),MainActivity.class));
                });
        ShopifyUtil.getInstance(getActivity())
                .setOnBackListener(data -> {
                    Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
                })
                .setUrl("file:///android_asset/test.html").show();
        return view;
    }
}
