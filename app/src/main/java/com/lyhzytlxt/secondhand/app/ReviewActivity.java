package com.lyhzytlxt.secondhand.app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

public class ReviewActivity extends AppCompatActivity {
    private RatingBar mRatingBar;
    private EditText mEtComment;
    private Button mBtnSubmit;
    private String mGoodsId;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        // 启用 ActionBar 的返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("评价商品");
        }

        // 获取传递的数据
        mGoodsId = getIntent().getStringExtra("goods_id");
        mEmail = getIntent().getStringExtra("email");

        initViews();
        setupListeners();
    }

    private void initViews() {
        mRatingBar = findViewById(R.id.rating_bar);
        mEtComment = findViewById(R.id.et_comment);
        mBtnSubmit = findViewById(R.id.btn_submit);
    }

    private void setupListeners() {
        mBtnSubmit.setOnClickListener(v -> {
            // 提交评价
            submitReview();
        });
    }

    private void submitReview() {
        String comment = mEtComment.getText().toString().trim();
        float rating = mRatingBar.getRating();

        if (rating == 0) {
            Toast.makeText(this, "请选择评分", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Constants.HOME_URL + "submit_review";
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("goods_id", mGoodsId);
            jsonBody.put("email", mEmail);
            jsonBody.put("rating", rating);
            jsonBody.put("comment", comment);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int ret = response.getInt("ret");
                                String desc = response.getString("desc");
                                if (ret == 0) {
                                    // 评价成功
                                    Toast.makeText(ReviewActivity.this, "评价成功", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    // 评价失败
                                    Toast.makeText(ReviewActivity.this, desc, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ReviewActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ReviewActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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