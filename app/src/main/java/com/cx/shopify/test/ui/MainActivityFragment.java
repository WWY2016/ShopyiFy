package com.cx.shopify.test.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cx.shopify.test.R;


public class MainActivityFragment extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_f);
        // 获取FragmentManager的实例
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 创建Fragment实例
        MainFragment fragment = new MainFragment();
        // 将Fragment添加到FrameLayout容器中
        transaction.add(R.id.fl_fl, fragment);
        // 提交事务
        transaction.commit();



    }
}
