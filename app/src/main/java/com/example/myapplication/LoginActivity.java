package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private EditText et_user_name,et_psw;
    private User u = new User();
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<LoginActivity> weakReference;
        public MyHandler(LoginActivity activity){
            this.weakReference = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg){}
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }
    private void init(){
        TextView tv_register = findViewById(R.id.tv_register);
        TextView tv_find_psw = findViewById(R.id.tv_find_psw);
        Button btn_login = findViewById(R.id.btn_login);
        et_user_name=findViewById(R.id.et_user_name);
        et_psw=findViewById(R.id.et_psw);
        mContext = getApplicationContext();

        //按钮绑定点击事件
        tv_find_psw.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_register:{
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            }
            case R.id.tv_find_psw: {
                startActivity(new Intent(this,ResetPswActivity.class));
                break;
            }
            case R.id.btn_login:{
                String username = et_user_name.getText().toString().trim().toLowerCase();
                String password = et_psw.getText().toString().trim().toLowerCase();
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(this,"账号不能为空！",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(password)){
                    Toast.makeText(this,"密码不能为空！",Toast.LENGTH_SHORT).show();
                }
                else {
                    u.setUsername(username);
                    u.setPassword(password);
                    new Thread(() -> {
                        final User result;
                        UserDao userDao = new UserDao();
                        result = userDao.findUserByUserAndPsw(u);
                        myHandler.post(() -> {
                            if(result!=null) {
                                Toast.makeText(getApplicationContext(), "登陆成功！", Toast.LENGTH_SHORT).show();
                                saveLoginInfo(username,password);
                                TimerTask task = new TimerTask() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        LoginActivity.this.finish();
                                    }
                                };
                                Timer timer = new Timer();
                                timer.schedule(task,1500);
                            } else {
                                Toast.makeText(getApplicationContext(),"账号或密码错误！",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                }
            }
        }
    }

    private void saveLoginInfo(String username,String password){
        SharedPreferences userInfo = getSharedPreferences("loginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        //得到Editor后，写入需要保存的数据
        editor.putString("username",username);
        editor.putString("password",password);
        editor.apply();//提交修改
        Log.i(TAG, "保存用户信息成功");
    }
}
