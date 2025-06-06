package com.example.lianghao.app;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lianghao.R;

public class OrderInfoActivity extends AppCompatActivity {
    private TextView mTvOrderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        mTvOrderInfo = findViewById(R.id.tv_order_info);
        
        // 获取传递过来的订单信息
        String orderInfo = getIntent().getStringExtra("order_info");
        if (orderInfo != null) {
            mTvOrderInfo.setText(orderInfo);
        }
    }
} 