package com.lyhzytlxt.secondhand;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.lyhzytlxt.secondhand.adapter.ServedListAdapter;
import com.lyhzytlxt.secondhand.bean.ServedListBean;
import com.lyhzytlxt.secondhand.utils.Constants;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class ServedListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ServedListAdapter mAdapter;
    private SharedPreferences mSharedPreferences;
    private String curUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_served_list);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        mSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        curUserEmail = mSharedPreferences.getString("email", "null");

        // 请求已售出商品列表
        requestServedList();
    }

    private void requestServedList() {
        String url = Constants.SERVED_LIST_URL;
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", curUserEmail);

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            processData(response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("onErrorResponse", error.toString());
                            Toast.makeText(ServedListActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
            queue.add(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processData(String json) {
        Gson gson = new Gson();
        ServedListBean servedListBean = gson.fromJson(json, ServedListBean.class);
        if (servedListBean != null && servedListBean.getInfo() != null) {
            mAdapter = new ServedListAdapter(this, servedListBean.getInfo());
            mRecyclerView.setAdapter(mAdapter);
        }
    }
} 