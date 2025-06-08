package com.lyhzytlxt.secondhand.app;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lyhzytlxt.secondhand.R;
import com.lyhzytlxt.secondhand.home.bean.ResultBeanData;
import com.lyhzytlxt.secondhand.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private OrderAdapter mAdapter;
    private List<ResultBeanData.ResultBean> mOrderList;
    private TextView mTvEmpty;
    private String mEmail;
    private String mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        // 先获取参数
        mEmail = getIntent().getStringExtra("email");
        mStatus = getIntent().getStringExtra("status");
        if (mStatus == null) {
            mStatus = "all"; // 设置默认值
        }

        // 启用 ActionBar 的返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getOrderTitle());
        }

        mOrderList = new ArrayList<>();
        
        mRecyclerView = findViewById(R.id.recycler_view);
        mTvEmpty = findViewById(R.id.tv_empty);
        
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new OrderAdapter(mOrderList);
        mRecyclerView.setAdapter(mAdapter);

        loadOrderList();
    }

    private String getOrderTitle() {
        switch (mStatus) {
            case "all":
                return "全部订单";
            case "pending_payment":
                return "待付款";
            case "pending_receipt":
                return "待收货";
            case "completed":
                return "已完成";
            default:
                return "订单列表";
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

    private void loadOrderList() {
        String url = Constants.HOME_URL + "get_order_list";
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", mEmail);
            jsonBody.put("status", mStatus);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int ret = response.getInt("ret");
                                if (ret == 0) {
                                    JSONArray data = response.getJSONArray("data");
                                    mOrderList.clear();
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject item = data.getJSONObject(i);
                                        ResultBeanData.ResultBean goodsInfo = new ResultBeanData.ResultBean();
                                        goodsInfo.setDesc(item.getString("desc"));
                                        goodsInfo.setPrice(item.getDouble("price"));
                                        goodsInfo.setImage(item.getString("image"));
                                        goodsInfo.setUser(item.getString("user"));
                                        goodsInfo.setAddress(item.getString("address"));
                                        goodsInfo.setStatus(item.getString("status"));
                                        goodsInfo.setProduct_id(item.getInt("product_id"));
                                        mOrderList.add(goodsInfo);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    mTvEmpty.setVisibility(mOrderList.isEmpty() ? View.VISIBLE : View.GONE);
                                } else {
                                    String desc = response.getString("desc");
                                    Toast.makeText(OrderListActivity.this, desc, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(OrderListActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                        }
                    });

            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
} 