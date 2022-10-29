package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.beans.User;
import com.example.myapplication.dao.UserDao;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_user_name,et_psw,et_psw_again;

    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<RegisterActivity> weakReference;
        public MyHandler(RegisterActivity activity){
            this.weakReference = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg){}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }
    private void init(){
        TextView tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("注册");
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        RelativeLayout rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.TRANSPARENT);
        Button btn_register = (Button) findViewById(R.id.btn_register);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_psw = (EditText) findViewById(R.id.et_psw);
        et_psw_again = (EditText) findViewById(R.id.et_psw_again);

        tv_back.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    /**
     * 按钮点击事件
     */
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_register:{
                String username = et_user_name.getText().toString().trim().toLowerCase();
                String psw = et_psw.getText().toString().trim().toLowerCase();
                String pswAgain = et_psw_again.getText().toString().trim().toLowerCase();
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(RegisterActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(psw)){
                    Toast.makeText(RegisterActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(pswAgain)){
                    Toast.makeText(RegisterActivity.this,"请再次输入密码",Toast.LENGTH_SHORT).show();
                }else if (!psw.equals(pswAgain)){
                    Toast.makeText(RegisterActivity.this,"输入两次的密码不一样",Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(() -> {
                        UserDao dao = new UserDao();
                        User u = new User();
                        u.setUsername(username);
                        u.setPassword(psw);
                        int r = dao.addUser(u);
                        myHandler.post(() -> {
                            if (r == 0){
                                Toast.makeText(getApplicationContext(),"重复的用户名！",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(),"注册成功！即将返回登录页面！",Toast.LENGTH_SHORT).show();
                                TimerTask task = new TimerTask() {
                                    @Override
                                    public void run() {
                                        RegisterActivity.this.finish();
                                    }
                                };
                                Timer timer = new Timer();
                                timer.schedule(task,2000);
                            }
                        });
                    }).start();
                }
            }
        }
    }
}
