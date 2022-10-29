package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.beans.User;
import com.example.myapplication.dao.UserDao;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class ModifyPswActivity extends AppCompatActivity {
    private EditText et_original_psw,et_new_psw,et_new_psw_again;
    private String originalPsw,newPsw,newPswAgain;
    private String username,password;
    private boolean flag = false;
    private final String TAG = "ModifyPswActivity";

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<ModifyPswActivity> weakReference;
        public MyHandler(ModifyPswActivity activity){
            this.weakReference = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg){}
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_psw);
        init();
    }

    public void init(){
        TextView tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("修改密码");
        TextView tv_back = findViewById(R.id.tv_back);
        et_original_psw=findViewById(R.id.et_original_psw);
        et_new_psw=findViewById(R.id.et_new_psw);
        et_new_psw_again=findViewById(R.id.et_new_psw_again);
        Button btn_save = findViewById(R.id.btn_save);

        /**
         * 返回按钮点击事件
         * @param view
         */
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /**
         * 保存按钮点击事件
         * @param view
         */
        btn_save.setOnClickListener(view -> {
            getEditString();
            getLoginInfo();
            User user = new User(username,originalPsw);
            if (TextUtils.isEmpty(originalPsw)) {
                Toast.makeText(ModifyPswActivity.this, "请输入原始密码", Toast.LENGTH_SHORT).show();
            } else if (!originalPsw.equals(password)) {
                Toast.makeText(ModifyPswActivity.this, "原始密码错误！", Toast.LENGTH_SHORT).show();
            } else if (originalPsw.equals(newPsw)){
                Toast.makeText(ModifyPswActivity.this, "输入的新密码与原始密码不能一致", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(newPsw)) {
                Toast.makeText(ModifyPswActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(newPswAgain)) {
                Toast.makeText(ModifyPswActivity.this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
            } else if (!newPsw.equals(newPswAgain)) {
                Toast.makeText(ModifyPswActivity.this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ModifyPswActivity.this, "新密码设置成功!请重新登陆", Toast.LENGTH_SHORT).show();
                new Thread(() -> {
                    UserDao dao = new UserDao();
                    User u = new User();
                    u.setUsername(username);
                    u.setPassword(newPsw);
                    dao.updateUser(u);
                }).start();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ModifyPswActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task,2000);
            }
        });
    }


    /**
     * 获取输入数据
     */
    private void getEditString(){
        originalPsw=et_original_psw.getText().toString().trim().toLowerCase();
        newPsw=et_new_psw.getText().toString().trim().toLowerCase();
        newPswAgain=et_new_psw_again.getText().toString().trim().toLowerCase();
    }

    private void getLoginInfo(){
        SharedPreferences userInfo = getSharedPreferences("loginInfo",MODE_PRIVATE);
        this.username = userInfo.getString("username","null");
        this.password = userInfo.getString("password","null");
        Log.d(TAG, "getLoginInfo: 获取已登录信息成功！");
    }
}
