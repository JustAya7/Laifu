package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationActivity extends AppCompatActivity {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private boolean isFirstLoc = true;
    private MyLocationData locData;
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = null;
    private double latitude = 39.077;
    private double longitude = 117.117098 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mMapView = findViewById(R.id.bmapView);
        Button btn_back = findViewById(R.id.btn_location_back);
        btn_back.setOnClickListener(view -> finish());
        initMap();
        initReceiver();
    }

    private void initMap(){
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        setMapCenter(longitude,latitude);
    }

    private void initReceiver() {
        MqttReceiver mqttReceiver = new MqttReceiver();
        IntentFilter intentFilter = new IntentFilter("mqttmsg");
        this.registerReceiver(mqttReceiver,intentFilter);
    }

    /*class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            MyLocationData locData = new MyLocationData.Builder().accuracy(0).direction(0).latitude(117.118807).longitude(39.078244).build();
            mBaiduMap.setMyLocationData(locData);

            *//**
     *当首次定位时，记得要放大地图，便于观察具体的位置
     * LatLng是缩放的中心点，这里注意一定要和上面设置给地图的经纬度一致；
     * MapStatus.Builder 地图状态构造器
     *//*
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(117.118807, 39.078244);
                MapStatus.Builder builder = new MapStatus.Builder();
                //设置缩放中心点；缩放比例；
                builder.target(ll).zoom(18.0f);
                //给地图设置状态
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }*/

    private void setMapCenter(double longitude, double latitude) {
        MyLocationData locData = new MyLocationData.Builder().accuracy(0).direction(0).latitude(latitude).longitude(longitude).build();
        mBaiduMap.setMyLocationData(locData);

        /**
         *当首次定位时，记得要放大地图，便于观察具体的位置
         * LatLng是缩放的中心点，这里注意一定要和上面设置给地图的经纬度一致；
         * MapStatus.Builder 地图状态构造器
         */
        if (isFirstLoc) {
            Toast.makeText(this,"第一次定位！",Toast.LENGTH_SHORT).show();
            isFirstLoc = false;
            LatLng ll = new LatLng(latitude,longitude);
            MapStatus.Builder builder = new MapStatus.Builder();
            //设置缩放中心点；缩放比例；
            builder.target(ll).zoom(20.0f);
            //给地图设置状态
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume(){
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mMapView.onDestroy();
    }

    private class MqttReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String mqttmsg = intent.getStringExtra("msg");
            try {
                JSONObject msg = new JSONObject(mqttmsg);
                JSONObject location = msg.getJSONObject("location");
                double longitude = location.getDouble("longitude");
                double latitude = location.getDouble("latitude");
                setMapCenter(longitude,latitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
