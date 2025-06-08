package com.lyhzytlxt.secondhand;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lyhzytlxt.secondhand.app.FavoriteListActivity;
import com.lyhzytlxt.secondhand.app.OrderListActivity;
import com.lyhzytlxt.secondhand.utils.Constants;

import java.util.Objects;

public class ThirdFragment extends Fragment {

    private ThirdViewModel mViewModel;
    private ImageView mIbLogin;
    private SharedPreferences mSharedPreferences;
    private TextView mTvLogin;
    private ImageButton mBtnSetting;
    private boolean isLogined;
    private View mBtnMyReleased;
    private View mBtnMyServed;    // 我要做的


    public static ThirdFragment newInstance() {
        return new ThirdFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_fragment, container, false);
        mIbLogin = view.findViewById(R.id.ib_login);
        mTvLogin = view.findViewById(R.id.tv_login);
        mBtnSetting = view.findViewById(R.id.btn_setting);
        mBtnMyReleased = view.findViewById(R.id.btn_my_released_goods);
        mBtnMyServed = view.findViewById(R.id.btn_my_served_goods);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ThirdViewModel.class);
        // TODO: Use the ViewModel
        mSharedPreferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        initListeners();
    }
    // 初始化那些监听器
    public void initListeners(){
        // 如果已经登录过了
        if (!mSharedPreferences.getString("email", "null").equals("null")){
            isLogined = true;
            mIbLogin.setEnabled(false);
            String username = mSharedPreferences.getString("username", "");
            String head_portrait_url = "/media/portraits/10060471_105425187390_2_J2ykXM6.jpg";
            Glide.with(getContext()).load(Constants.BASE_URL+head_portrait_url).into(mIbLogin);
            mTvLogin.setText(username);
            mBtnSetting.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            });
        }else{
            isLogined = false;
            mIbLogin.setEnabled(true);
            mTvLogin.setText("登录/注册");
            // 如果还没登录
            mBtnSetting.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            });
        }

        // 设置头像点击事件
        mIbLogin.setOnClickListener(v -> {
            if (!isLogined) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // 设置"我发布的"点击事件
        View btnMyReleased = getView().findViewById(R.id.btn_my_released_goods);
        btnMyReleased.setOnClickListener(v -> {
            if (isLogined) {
                Intent intent = new Intent(getContext(), ReleasedListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", mSharedPreferences.getString("email", "null"));   // 把邮箱传过去，作为用户标识
                intent.putExtras(bundle);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // 设置"我要做的"点击事件
        View btnMyServed = getView().findViewById(R.id.btn_my_served_goods);
        btnMyServed.setOnClickListener(v -> {
            if (isLogined) {
                Intent intent = new Intent(getContext(), ServedListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", mSharedPreferences.getString("email", "null"));   // 把邮箱传过去，作为用户标识
                intent.putExtras(bundle);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // 设置"我的收藏"点击事件
        View btnMyFavorite = getView().findViewById(R.id.btn_my_favorite_goods);
        btnMyFavorite.setOnClickListener(v -> {
            if (isLogined) {
                Intent intent = new Intent(getContext(), FavoriteListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", mSharedPreferences.getString("email", "null"));
                intent.putExtras(bundle);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // 设置订单相关点击事件
        View btnAllOrders = getView().findViewById(R.id.btn_all_orders);
        View btnPendingOrders = getView().findViewById(R.id.btn_pending_orders);
        View btnProcessingOrders = getView().findViewById(R.id.btn_processing_orders);
        View btnCompletedOrders = getView().findViewById(R.id.btn_completed_orders);

        View.OnClickListener orderClickListener = v -> {
            if (isLogined) {
                Intent intent = new Intent(getContext(), OrderListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", mSharedPreferences.getString("email", "null"));
                if (v == btnAllOrders) {
                    bundle.putString("status", "all");
                } else if (v == btnPendingOrders) {
                    bundle.putString("status", "pending_payment");
                } else if (v == btnProcessingOrders) {
                    bundle.putString("status", "pending_receipt");
                } else if (v == btnCompletedOrders) {
                    bundle.putString("status", "completed");
                }
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        };

        btnAllOrders.setOnClickListener(orderClickListener);
        btnPendingOrders.setOnClickListener(orderClickListener);
        btnProcessingOrders.setOnClickListener(orderClickListener);
        btnCompletedOrders.setOnClickListener(orderClickListener);

        // 更新统计数据
        updateStatistics();
    }

    private void updateStatistics() {
        if (isLogined) {
            // TODO: 从服务器获取统计数据
            // 这里暂时使用模拟数据
            TextView tvReleasedCount = requireView().findViewById(R.id.tv_released_count);
            TextView tvServedCount = requireView().findViewById(R.id.tv_served_count);
            TextView tvFavoriteCount = requireView().findViewById(R.id.tv_favorite_count);

            tvReleasedCount.setText("0");
            tvServedCount.setText("0");
            tvFavoriteCount.setText("0");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initListeners();
    }
}
