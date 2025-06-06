package com.example.lianghao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lianghao.R;
import com.example.lianghao.bean.ServedListBean;
import com.example.lianghao.utils.Constants;

import java.util.List;

public class ServedListAdapter extends RecyclerView.Adapter<ServedListAdapter.ViewHolder> {
    private Context mContext;
    private List<ServedListBean.InfoBean> mData;

    public ServedListAdapter(Context context, List<ServedListBean.InfoBean> data) {
        this.mContext = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_served_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServedListBean.InfoBean item = mData.get(position);
        
        // 加载商品图片
        Glide.with(mContext)
                .load(Constants.BASE_URL + item.getDisplay_image())
                .into(holder.mIvGoods);
        
        holder.mTvTitle.setText(item.getTitle());
        holder.mTvPrice.setText(String.format("¥%.2f", item.getPrice()));
        holder.mTvBuyer.setText("买家：" + item.getBuyer_username());
        holder.mTvTime.setText("成交时间：" + item.getCreate_time());
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvGoods;
        TextView mTvTitle;
        TextView mTvPrice;
        TextView mTvBuyer;
        TextView mTvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvGoods = itemView.findViewById(R.id.iv_goods);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvPrice = itemView.findViewById(R.id.tv_price);
            mTvBuyer = itemView.findViewById(R.id.tv_buyer);
            mTvTime = itemView.findViewById(R.id.tv_time);
        }
    }
} 