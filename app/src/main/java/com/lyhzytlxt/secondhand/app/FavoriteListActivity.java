package com.lyhzytlxt.secondhand.app;

import android.content.Intent;
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
import com.lyhzytlxt.secondhand.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoriteListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private FavoriteAdapter mAdapter;
    private List<ResultBeanData.ResultBean> mGoodsList;
    private TextView mTvEmpty;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // 获取用户邮箱
        mEmail = getIntent().getStringExtra("email");

        // 启用 ActionBar 的返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("我的收藏");
        }

        mGoodsList = new ArrayList<>();
        
        mRecyclerView = findViewById(R.id.recycler_view);
        mTvEmpty = findViewById(R.id.tv_empty);
        
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FavoriteAdapter(mGoodsList);
        mRecyclerView.setAdapter(mAdapter);

        loadFavoriteList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFavoriteList() {
        String url = Constants.HOME_URL + "get_favorite_list";
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", mEmail);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int ret = response.getInt("ret");
                                if (ret == 0) {
                                    JSONArray data = response.getJSONArray("data");
                                    mGoodsList.clear();
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject item = data.getJSONObject(i);
                                        ResultBeanData.ResultBean goodsInfo = new ResultBeanData.ResultBean();
                                        goodsInfo.setDesc(item.getString("desc"));
                                        goodsInfo.setPrice(item.getDouble("price"));
                                        goodsInfo.setImage(item.getString("image"));
                                        goodsInfo.setUser(item.getString("user"));
                                        goodsInfo.setAddress(item.getString("address"));
                                        goodsInfo.setProduct_id(item.getInt("product_id"));
                                        mGoodsList.add(goodsInfo);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    mTvEmpty.setVisibility(mGoodsList.isEmpty() ? View.VISIBLE : View.GONE);
                                } else {
                                    String desc = response.getString("desc");
                                    ToastUtil.showMsg(FavoriteListActivity.this, desc);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                ToastUtil.showMsg(FavoriteListActivity.this, "数据解析错误");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ToastUtil.showMsg(FavoriteListActivity.this, "网络错误");
                        }
                    });

            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
} 