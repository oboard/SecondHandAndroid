package com.example.lianghao.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lianghao.app.GoodsInfoActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 商品相关的工具类
 */
public class GoodsUtils {
    
    /**
     * 检查商品是否已收藏
     * @param context 上下文
     * @param goodsId 商品ID
     * @return 是否已收藏
     */
    public static boolean isGoodsCollected(Context context, int goodsId) {
        SharedPreferences sp = context.getSharedPreferences("collected_goods", Context.MODE_PRIVATE);
        return sp.getBoolean("goods_" + goodsId, false);
    }

    /**
     * 设置商品收藏状态
     * @param context 上下文
     * @param goodsId 商品ID
     * @param isCollected 是否收藏
     */
    public static void setGoodsCollected(Context context, int goodsId, boolean isCollected) {
        SharedPreferences sp = context.getSharedPreferences("collected_goods", Context.MODE_PRIVATE);
        sp.edit().putBoolean("goods_" + goodsId, isCollected).apply();
    }

    /**
     * 检查商品是否已点赞
     * @param context 上下文
     * @param goodsId 商品ID
     * @return 是否已点赞
     */
    public static boolean isGoodsLiked(Context context, int goodsId) {
        SharedPreferences sp = context.getSharedPreferences("liked_goods", Context.MODE_PRIVATE);
        return sp.getBoolean("goods_" + goodsId, false);
    }

    /**
     * 设置商品点赞状态
     * @param context 上下文
     * @param goodsId 商品ID
     * @param isLiked 是否点赞
     */
    public static void setGoodsLiked(Context context, int goodsId, boolean isLiked) {
        SharedPreferences sp = context.getSharedPreferences("liked_goods", Context.MODE_PRIVATE);
        sp.edit().putBoolean("goods_" + goodsId, isLiked).apply();
    }

    /**
     * 请求购买服务
     * @param goodsPk 商品ID
     * @param email 用户邮箱
     * @param callback 回调接口
     * @param context 上下文
     */
    public static void requestGiveService(int goodsPk, String email, GoodsInfoActivity.VolleyCallback callback, Context context) {
        String url = Constants.GIVE_SERVICE_URL;
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("goods_pk", goodsPk);
            jsonBody.put("email", email);

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            callback.onSuccessResponse(response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("onErrorResponse", error.toString());
                            Toast.makeText(context, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
            queue.add(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
} 