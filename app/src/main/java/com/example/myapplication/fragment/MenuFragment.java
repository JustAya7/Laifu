package com.example.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.LocationActivity;
import com.example.myapplication.R;
import com.example.myapplication.beans.WeatherBean;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Objects;

public class MenuFragment extends Fragment{

    private static final String TAG = "MenuFragment";
    private RelativeLayout rl_weather;
    private TextView tv_weather_temp;
    private TextView tv_weather_status;
    private TextView tv_weather_location;
    private ImageView iv_weather;
    private LinearLayout ll_location;
    private LinearLayout ll_video;
    private LinearLayout ll_message;
    private LinearLayout ll_history;
    private LinearLayout ll_news;
    private LinearLayout ll_scan;
    //handler接收天气信息
    /*private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:{
                    String s = msg.obj.toString();
                    WeatherBean wb = new GsonBuilder().create().fromJson(s,WeatherBean.class);
                    String temp = wb.getResult().getRealtime().getTemperature();
                    String info = wb.getResult().getRealtime().getInfo();
                    String humidity = wb.getResult().getRealtime().getHumidity();
                    String aqi = wb.getResult().getRealtime().getAqi();
                    tv_weather_temp.setText(temp + "°C");
                    tv_weather_status.setText(info + " | " + aqi);
                }
                case 1:
            }
        }
    };*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu,container,false);
        rl_weather = view.findViewById(R.id.rl_weather);
        tv_weather_temp = view.findViewById(R.id.tv_weather_temp);
        tv_weather_status = view.findViewById(R.id.tv_weather_status);
        tv_weather_location = view.findViewById(R.id.tv_weather_location);
        iv_weather = view.findViewById(R.id.iv_weather);
        ll_location = view.findViewById(R.id.ll_menu_location);
        ll_video = view.findViewById(R.id.ll_menu_video);
        ll_message = view.findViewById(R.id.ll_menu_message);
        ll_history = view.findViewById(R.id.ll_menu_history);
        ll_news = view.findViewById(R.id.ll_menu_news);
        ll_scan = view.findViewById(R.id.ll_menu_scan);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        ll_location.setOnClickListener(view -> startActivity(new Intent(getActivity(), LocationActivity.class)));
        ll_video.setOnClickListener(view -> {
            Uri uri = Uri.parse("http://192.168.43.66/mjpeg/1");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        /*ll_message.setOnClickListener(this);
        ll_history.setOnClickListener(this);
        ll_news.setOnClickListener(this);*/
        ll_scan.setOnClickListener(view -> {
            IntentIntegrator intentIntegrator = IntentIntegrator.forSupportFragment(this);
            intentIntegrator.initiateScan();
        });
        initWeather();
    }

    private void initWeather() {
        SharedPreferences sp = Objects.requireNonNull(getActivity()).getSharedPreferences("weatherInfo", Context.MODE_PRIVATE);
        String s = sp.getString("weatherJSON","");
        WeatherBean wb = new GsonBuilder().create().fromJson(s,WeatherBean.class);
        int error_code = wb.getError_code();
        if (error_code == 0){
            String temp = wb.getResult().getRealtime().getTemperature();
            String info = wb.getResult().getRealtime().getInfo();
            String humidity = wb.getResult().getRealtime().getHumidity();
            String aqi = wb.getResult().getRealtime().getAqi();
            String city = wb.getResult().getCity();
            int icon = Integer.parseInt(wb.getResult().getRealtime().getWid());
            switchIcon(icon);
            tv_weather_temp.setText(temp + "°C");
            tv_weather_status.setText(info + " | " +  "空气质量：" +getAirQuality(aqi));
            tv_weather_location.setText(city + "市");
        }
    }

    private void switchIcon(int icon) {
        switch (icon){
            case 0:
                iv_weather.setBackgroundResource(R.drawable.ic_weather_sunny);
                break;
            case 1:
            case 2:
                iv_weather.setBackgroundResource(R.drawable.ic_weather_cloudy);
                break;
            case 4:
            case 5:
                iv_weather.setBackgroundResource(R.drawable.ic_weather_thunder);
                break;
            case 7:
                iv_weather.setBackgroundResource(R.drawable.ic_weather_drizzle);
                break;
            case 8:
                iv_weather.setBackgroundResource(R.drawable.ic_weather_raining);
                break;
            case 9:
            case 10:
            case 11:
            case 12:
                iv_weather.setBackgroundResource(R.drawable.ic_weather_showers);
                break;
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
                iv_weather.setBackgroundResource(R.drawable.ic_weather_snowing);
                break;
            case 18:
            case 51:
                iv_weather.setBackgroundResource(R.drawable.ic_weather_mist);
                break;
            default:
                iv_weather.setBackgroundResource(R.drawable.ic_weather_cloudy);
                break;
        }
    }

    private String getAirQuality(String aqi) {
        int a = Integer.parseInt(aqi);
        String air_quality = "暂无空气数据";
        if (a <= 50){
            air_quality = "优";
        }else if (a <= 100){
            air_quality = "良";
        }else if (a <= 150){
            air_quality = "轻度污染";
        }else if (a <= 200){
            air_quality = "中度污染";
        }else if (a <= 300){
            air_quality = "重度污染";
        }else{
            air_quality = "严重污染";
        }
        return air_quality;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获取解析结果
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "取消扫描", Toast.LENGTH_LONG).show();
            } else {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(result.getContents())));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
