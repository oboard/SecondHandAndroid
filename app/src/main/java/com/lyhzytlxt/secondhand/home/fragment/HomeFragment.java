package com.lyhzytlxt.secondhand.home.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.lyhzytlxt.secondhand.R;
import com.lyhzytlxt.secondhand.base.BaseFragment;
import com.lyhzytlxt.secondhand.home.adapter.HomeFragmentAdapter;
import com.lyhzytlxt.secondhand.home.bean.ResultBeanData;
import com.lyhzytlxt.secondhand.utils.Constants;
import com.lyhzytlxt.secondhand.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

// 主页fragment
public class HomeFragment extends BaseFragment {
    private HomeFragmentAdapter adapter;

    private String TAG = "HomeFragment";
    private View mBtnSearch;
    private LinearLayout mBtnMobilePhone;    // 二手手机
    private LinearLayout mBtnBook;           // 二手图书
    private LinearLayout mBtnAll;            // 所有分类
    private ImageButton mIbBackTop;    // 返回最顶部
//    private RecyclerView mRvCategory;
    private RecyclerView mRvList;
    private List<ResultBeanData.ResultBean> resultBeans;
    @Override
    public View initView() {
        Log.d(TAG, "initView");
        View view = View.inflate(mContext, R.layout.fragment_home, null);
        mBtnSearch = view.findViewById(R.id.btn_search);
//        mRvCategory = view.findViewById(R.id.rv_category);
        mRvList = view.findViewById(R.id.rv_list);
        mBtnMobilePhone = view.findViewById(R.id.btn_mobile_phone);
        mBtnBook = view.findViewById(R.id.btn_book);
        mBtnAll = view.findViewById(R.id.btn_all);
        mIbBackTop = view.findViewById(R.id.ib_top);
        return view;
    }


    public void initData(){
        super.initData();
        Log.d(TAG, "initData");
        initListener();

        getDataFromNet();
    }

    public void getDataFromNet(){

        // 联网获取商品
        String url = Constants.HOME_URL;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtil.showMsg(mContext, "请求失败:"+e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        ToastUtil.showMsg(mContext, "请求成功:"+response);
                        // 对返回的json数据进行解析
                        processData(response);
                    }
                });

    }

    private void processData(String json){
        ResultBeanData resultBeanData = JSON.parseObject(json, ResultBeanData.class);
        resultBeans = resultBeanData.getResult();    // 获取所有商品列表
        // Log.d("解析成功", resultBeans.get(0).getImage());
        if (resultBeans != null){
            // 成功获取数据
            // 设置RecyclerView的适配器
            adapter = new HomeFragmentAdapter(mContext, resultBeanData);
            mRvList.setAdapter(adapter);
            GridLayoutManager manager = new GridLayoutManager(mContext, 1);
            // 设置跨度大小监听
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position <= 1){
                        // 隐藏
                        mIbBackTop.setVisibility(View.GONE);
                    }else {
                        // 出现
                        mIbBackTop.setVisibility(View.VISIBLE);
                    }
                    return 1;
                }
            });
            // 设置布局管理者
            mRvList.setLayoutManager(manager);    // 1表示只有1列
        }else{
            // 没有得到数据
        }
    }

    public void initListener(){
        mBtnSearch.setOnClickListener(v -> {
            ToastUtil.showMsg(mContext, "点击搜索按钮");
        });

        mBtnMobilePhone.setOnClickListener(v -> {
            ToastUtil.showMsg(mContext, "点击二手手机按钮");
        });

        mBtnBook.setOnClickListener(v -> {
            ToastUtil.showMsg(mContext, "点击二手图书按钮");
        });

        mBtnAll.setOnClickListener(v -> {
            ToastUtil.showMsg(mContext, "点击全部分类");
        });

        mIbBackTop.setOnClickListener(v -> {
            ToastUtil.showMsg(mContext, "点击返回顶部");
            mRvList.scrollToPosition(0);    // 滚回顶部
        });
    }
}
