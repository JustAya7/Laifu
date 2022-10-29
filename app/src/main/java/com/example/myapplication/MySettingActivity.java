package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;

public class MySettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        init();
    }

    private void init() {
        RelativeLayout rl_change_psw = findViewById(R.id.rl_change_psw);
        RelativeLayout rl_logout = findViewById(R.id.rl_logout);
        TextView tv_title = findViewById(R.id.tv_main_title);
        TextView tv_back = findViewById(R.id.tv_back);
        tv_title.setText("设置");
        tv_back.setOnClickListener(view -> this.finish());
        rl_change_psw.setOnClickListener(view -> startActivity(new Intent(MySettingActivity.this,ModifyPswActivity.class)));
        rl_logout.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MySettingActivity.this);
            dialog.setIcon(R.mipmap.ic_launcher)
                    .setTitle("退出登录")
                    .setMessage("您确定要退出登录？")
                    .setCancelable(true)
                    .setPositiveButton("确定",
                            (dialog1, which) -> {
                                startActivity(new Intent(MySettingActivity.this,LoginActivity.class));
                                finish();
                            })
                    .setNegativeButton("取消",
                            (dialog12, which) -> {
                            });
            dialog.show();
        });
    }
}
