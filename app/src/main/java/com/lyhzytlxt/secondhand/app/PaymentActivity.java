package com.lyhzytlxt.secondhand.app;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lyhzytlxt.secondhand.R;
import com.lyhzytlxt.secondhand.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity {
    private TextView mTvAmount;
    private Button mBtnPay;
    private String mGoodsId;
    private double mAmount;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // 启用 ActionBar 的返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("支付订单");
        }

        // 获取传递的数据
        mGoodsId = getIntent().getStringExtra("goods_id");
        mAmount = getIntent().getDoubleExtra("amount", 0.0);
        mEmail = getIntent().getStringExtra("email");

        initViews();
        setupListeners();
    }

    private void initViews() {
        mTvAmount = findViewById(R.id.tv_amount);
        mBtnPay = findViewById(R.id.btn_pay);

        // 显示支付金额
        mTvAmount.setText(String.format("¥%.2f", mAmount));
    }

    private void setupListeners() {
        mBtnPay.setOnClickListener(v -> {
            // 执行支付操作
            processPayment();
        });
    }

    private void processPayment() {
        String url = Constants.HOME_URL + "process_payment";
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("goods_id", mGoodsId);
            jsonBody.put("email", mEmail);
            jsonBody.put("amount", mAmount);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int ret = response.getInt("ret");
                                String desc = response.getString("desc");
                                if (ret == 0) {
                                    // 支付成功
                                    Toast.makeText(PaymentActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    // 支付失败
                                    Toast.makeText(PaymentActivity.this, desc, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(PaymentActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(PaymentActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                        }
                    });

            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
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