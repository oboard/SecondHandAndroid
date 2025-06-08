package com.lyhzytlxt.secondhand.app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lyhzytlxt.secondhand.R;

public class OrderInfoActivity extends AppCompatActivity {
    private TextView mTvOrderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        // 启用 ActionBar 的返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("订单详情");
        }

        mTvOrderInfo = findViewById(R.id.tv_order_info);
        
        // 获取传递过来的订单信息
        String orderInfo = getIntent().getStringExtra("order_info");
        if (orderInfo != null) {
            mTvOrderInfo.setText(orderInfo);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 