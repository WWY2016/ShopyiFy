package com.cx.shopify.test.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cx.shopify.test.R;
import com.cx.shopify.util.ShopifyUtil;
import com.cx.shopify.view.ShopifyListView;


public class MainActivity extends AppCompatActivity {
    private EditText etFloat,etList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etFloat = findViewById(R.id.et_float);
        etList = findViewById(R.id.et_list);
        etFloat.setText("https://quick-chatb.quickcep.com/nativeVideo.html?platform=other&accessId=14272c38-2db5-416e-ad7b-253574a96fbd&debug=true&location=homepage");
        etList.setText("https://quick-chatb.quickcep.com/nativeVideo.html?platform=other&accessId=14272c38-2db5-416e-ad7b-253574a96fbd&debug=true&location=homepage&quickcepAppVideoID=1872545893175103489");
        findViewById(R.id.bt_test).setOnClickListener(view -> startActivity(new Intent(MainActivity.this,MainActivityTest.class)));
        findViewById(R.id.bt_net).setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, MainActivityNet.class);
            intent.putExtra("float_url",etFloat.getText().toString());
            intent.putExtra("list_url",etList.getText().toString());
            startActivity(intent);
        });

    }
}
