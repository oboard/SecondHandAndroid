package com.lyhzytlxt.secondhand.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lyhzytlxt.secondhand.R;
import com.lyhzytlxt.secondhand.home.bean.ResultBeanData;
import com.lyhzytlxt.secondhand.utils.Constants;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private List<ResultBeanData.ResultBean> mGoodsList;
    private Context mContext;

    public FavoriteAdapter(List<ResultBeanData.ResultBean> goodsList) {
        mGoodsList = goodsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResultBeanData.ResultBean goodsInfo = mGoodsList.get(position);
        
        // 加载商品图片
        Glide.with(mContext)
                .load(Constants.HOME_URL + goodsInfo.getImage())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(holder.mIvGoods);
        
        holder.mTvTitle.setText(goodsInfo.getDesc());
        holder.mTvPrice.setText(String.format("¥%.2f", goodsInfo.getPrice()));
        holder.mTvLocation.setText(goodsInfo.getAddress());

        // 点击整个商品项跳转到商品详情
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, GoodsInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("pk", goodsInfo.getProduct_id());
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvGoods;
        TextView mTvTitle;
        TextView mTvPrice;
        TextView mTvLocation;

        ViewHolder(View view) {
            super(view);
            mIvGoods = view.findViewById(R.id.iv_goods);
            mTvTitle = view.findViewById(R.id.tv_title);
            mTvPrice = view.findViewById(R.id.tv_price);
            mTvLocation = view.findViewById(R.id.tv_location);
        }
    }
} 