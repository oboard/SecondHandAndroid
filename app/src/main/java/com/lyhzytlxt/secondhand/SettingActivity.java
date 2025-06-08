package com.lyhzytlxt.secondhand;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    private Button mBtnSignOut;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mBtnSignOut = findViewById(R.id.btn_sign_out);
        mBtnSignOut.setOnClickListener(v -> showSignOutDialog());
    }

    private void showSignOutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("退出登录")
            .setMessage("确定要退出登录吗？")
            .setPositiveButton("确定", (dialog, which) -> {
                // 清除用户信息
                mEditor.putString("email", "null");
                mEditor.apply();
                
                // 显示提示
                Toast.makeText(SettingActivity.this, "已退出登录", Toast.LENGTH_SHORT).show();
                
                // 跳转到登录页面
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            })
            .setNegativeButton("取消", null)
            .show();
    }

    @Override
    public void onBackPressed() {
        // 返回上一页
        super.onBackPressed();
        finish();
    }
}
