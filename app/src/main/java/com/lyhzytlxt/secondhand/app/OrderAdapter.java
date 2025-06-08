package com.lyhzytlxt.secondhand.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lyhzytlxt.secondhand.R;
import com.lyhzytlxt.secondhand.home.bean.ResultBeanData;
import com.lyhzytlxt.secondhand.utils.Constants;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<ResultBeanData.ResultBean> mOrderList;
    private Context mContext;

    public OrderAdapter(List<ResultBeanData.ResultBean> orderList) {
        mOrderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResultBeanData.ResultBean goodsInfo = mOrderList.get(position);
        
        holder.mTvTitle.setText(goodsInfo.getDesc());
        holder.mTvPrice.setText(String.format("¥%.2f", goodsInfo.getPrice()));
        holder.mTvLocation.setText(goodsInfo.getAddress());
        
        // 加载商品图片
        Glide.with(mContext)
                .load(Constants.HOME_URL + goodsInfo.getImage())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(holder.mIvGoods);

        // 设置按钮状态和点击事件
        setupButton(holder.mBtnAction, goodsInfo);

        // 点击整个订单项跳转到订单详情
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
        return mOrderList.size();
    }

    private void setupButton(Button button, ResultBeanData.ResultBean goodsInfo) {
        // 根据订单状态设置按钮
        String status = goodsInfo.getStatus();
        if (status != null) {
            switch (status) {
                case "pending_payment":
                    button.setText("去支付");
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(v -> {
                        Intent intent = new Intent(mContext, PaymentActivity.class);
                        intent.putExtra("goods_id", goodsInfo.getUser());
                        intent.putExtra("amount", goodsInfo.getPrice());
                        mContext.startActivity(intent);
                    });
                    break;
                case "pending_receipt":
                    button.setText("确认收货");
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(v -> {
                        // TODO: 实现确认收货功能
                    });
                    break;
                case "completed":
                    button.setText("评价");
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(v -> {
                        Intent intent = new Intent(mContext, ReviewActivity.class);
                        intent.putExtra("goods_id", goodsInfo.getUser());
                        mContext.startActivity(intent);
                    });
                    break;
                default:
                    button.setVisibility(View.GONE);
                    break;
            }
        } else {
            button.setVisibility(View.GONE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvGoods;
        TextView mTvTitle;
        TextView mTvPrice;
        TextView mTvLocation;
        Button mBtnAction;

        ViewHolder(View view) {
            super(view);
            mIvGoods = view.findViewById(R.id.iv_goods);
            mTvTitle = view.findViewById(R.id.tv_title);
            mTvPrice = view.findViewById(R.id.tv_price);
            mTvLocation = view.findViewById(R.id.tv_location);
            mBtnAction = view.findViewById(R.id.btn_action);
        }
    }
} 