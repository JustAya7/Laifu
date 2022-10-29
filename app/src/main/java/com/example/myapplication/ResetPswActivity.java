package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.beans.User;
import com.example.myapplication.dao.UserDao;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class ResetPswActivity extends AppCompatActivity {
    private EditText et_username;
    private String username;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<LoginActivity> weakReference;
        public MyHandler(ResetPswActivity activity){
            this.weakReference = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg){}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_psw);
        init();
    }

    public void init(){
        TextView tv_back = findViewById(R.id.tv_back);
        TextView tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("重置密码");
        et_username = findViewById(R.id.et_user_name);
        Button btn_ok = findViewById(R.id.btn_ok);

        /**
         * 返回按钮点击事件
         */
        tv_back.setOnClickListener(view -> {
            startActivity(new Intent(ResetPswActivity.this,LoginActivity.class));
            finish();
        });

        /**
         * 确定按钮点击事件
         */
        btn_ok.setOnClickListener(view -> {
            getEditString();
            if (TextUtils.isEmpty(username))
                Toast.makeText(ResetPswActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
            else {
                User u = new User();
                u.setUsername(username);
                new Thread(() ->{
                    final User result;
                    UserDao userDao = new UserDao();
                    result = userDao.findUserByUser(u);
                    myHandler.post(() ->{
                        if (result!=null){
                            new Thread(()->{
                                User user = new User();
                                user.setUsername(username);
                                user.setPassword("12345678");
                                userDao.updateUser(user);
                            }).start();
                            Toast.makeText(getApplicationContext(),"密码重置为12345678！",Toast.LENGTH_SHORT).show();
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    ResetPswActivity.this.finish();
                                }
                            };
                            Timer timer = new Timer();
                            timer.schedule(task,2000);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"用户名不存在！",Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            }
        });
    }

    /**
     * 获取输入字段
     */
    public void getEditString(){
        username =et_username.getText().toString().trim().toLowerCase();
    }

}
