package com.example.myapplication;

import android.app.Application;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.common.BaiduMapSDKException;


public class DemoApplication extends Application {
    private static final String Tag = "DemoApplication";

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(Tag," Create success!");


        /**
         * 配置百度地图SDK
         */
        SDKInitializer.setAgreePrivacy(getApplicationContext(),true);
        //不加会闪退
        LocationClient.setAgreePrivacy(true);
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}
