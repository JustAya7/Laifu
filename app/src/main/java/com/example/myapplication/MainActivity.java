package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.fragment.DeviceFragment;
import com.example.myapplication.fragment.MenuFragment;
import com.example.myapplication.fragment.SettingFragment;
import com.example.myapplication.service.MQTTService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private RequestQueue requestQueue;
    private BottomNavigationView bottomNavigationView;
    //private RequestQueue requestQueue = Volley.newRequestQueue(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init(savedInstanceState);
    }

    public void init(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DeviceFragment()).commit();
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.navigation_item1:
                        fragment = new DeviceFragment();
                        break;
                    case R.id.navigation_item2:
                        fragment = new MenuFragment();
                        break;
                    case R.id.navigation_item3:
                        fragment = new SettingFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setItemIconTintList(null);
        //获取天气状态
        getWeatherStatus();
        //启动MQTTService
        Intent intent = new Intent(this, MQTTService.class);
        startService(intent);
    }

    /**
     * 获取天气数据
     */
    private void getWeatherStatus() {
        new Thread(() ->{
            //对比系统时间戳
            Log.d(TAG, "getWeatherStatus: success!");
            SharedPreferences sp = getSharedPreferences("weatherInfo",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            long current_time = System.currentTimeMillis()/3600000;
            long last_time = sp.getLong("weatherTime",0);
            if (current_time > last_time){
                /*//通过聚合数据API获取天气数据
                OkHttpClient okHttpClient = new OkHttpClient();
                Request weatherRequest = new Request.Builder().url(getURL()).build();
                Response weatherResponse = okHttpClient.newCall(weatherRequest).execute();
                String weatherData = weatherResponse.body().string();
                if (weatherResponse.isSuccessful()){
                    //将最新的天气数据及时间戳存储到本地
                    editor.putString("weatherJSON",weatherData);
                    editor.putLong("weatherTime", System.currentTimeMillis()/3600000);
                    editor.apply();
                    Log.d(TAG, "getWeatherStatus: save weatherInfo success!");
                    */
                /*Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = weatherData;
                    handler.sendMessage(msg);*/
                /*
                }else Log.d(TAG, "getWeatherStatus: failed!");*/
                //通过聚合数据API获取天气数据
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (com.android.volley.Request.Method.GET, getURL(), null, response -> {
                            editor.putString("weatherJSON", response.toString());
                            editor.putLong("weatherTime", System.currentTimeMillis() / 3600000);
                            editor.apply();
                            Log.d(TAG, "getWeatherStatus: save weatherInfo success!");
                        }, error -> {
                            Log.d(TAG, "getWeatherStatus: get weatherJson failed!");
                        });
                requestQueue.add(jsonObjectRequest);
            }
        }).start();
    }



    private String getURL() {
        Properties properties = new Properties();
        InputStream is = null;
        String weatherURL = "";
        try {
            is = getApplicationContext().getAssets().open("url-config.properties");
            properties.load(is);
            weatherURL = properties.getProperty("api_url_weather");
            if(!weatherURL.isEmpty())
                Log.d(TAG, "getURL: success!");;
        } catch (IOException e) {
            Log.d(TAG, "getURL: Open url-config.properties failed!");
            e.printStackTrace();
        }
        return weatherURL;
    }
}
