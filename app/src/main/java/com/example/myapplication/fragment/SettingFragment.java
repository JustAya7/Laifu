package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.ModifyPswActivity;
import com.example.myapplication.MySettingActivity;
import com.example.myapplication.R;

public class SettingFragment extends Fragment{

    private RelativeLayout rl_my_setting;
    private final String TAG = "SettingFragment";

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting,container,false);
        rl_my_setting = view.findViewById(R.id.rl_my_setting);
        TextView tv_user = view.findViewById(R.id.tv_user_id);

        tv_user.setText("账号："+ getLoginInfo());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rl_my_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MySettingActivity.class));
            }
        });
    }

    private String getLoginInfo(){
        SharedPreferences userInfo = getActivity().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String username = userInfo.getString("username","null");
        Log.i(TAG, "读取用户信息！");
        return username;
    }
}
