package com.junova.huizhong;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.junova.huizhong.common.CrashHandler;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.service.SendService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import okhttp3.OkHttpClient;

public class AppApplication extends Application {

    private String updateConfName = "android_update_conf.json";
    /**
     * 所以的Activity
     */
    public List<Activity> activities = new ArrayList<Activity>();

    private LocationManager locationManager;
    private String locationProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        initUpdateJsonData();
        initCommonData();
        AppConfig.context = this;
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(getApplicationContext());
        // 启动服务
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);
        OkHttpUtils.getInstance().debug("TAG");
        Intent intent = new Intent(AppApplication.this, SendService.class);
        startService(intent);
    }

    /**
     * 初始化asset的配置文件
     */
    public void initUpdateJsonData() {
        if (AppConfig.prefs == null) {
            AppConfig.prefs = getSharedPreferences(AppConfig.prfsName, Context.MODE_MULTI_PROCESS);
        }
        if (AppConfig.appConfig == null) {
            AppConfig.appConfig = getSharedPreferences(AppConfig.configName, Context.MODE_PRIVATE);
        }
        AppConfig.DATABASE = "huizhongdb";
    }

    /**
     * get all the activity
     *
     * @return List<Activity>
     */
    public List<Activity> getActivities() {
        return activities;
    }

    /**
     * @param activity add single activity in the activities
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 获取设备信息
     */
    public void initCommonData() {
        AppConfig.udId = FunctionUtils.getUdId(this);
        AppConfig.version = FunctionUtils.getAppVersion(this);
        AppConfig.apiVersion = FunctionUtils.getapiVersion();
        AppConfig.device = FunctionUtils.getDevice();
        AppConfig.os = FunctionUtils.getOs();
    }

    /**
     * 获取经纬度
     */
    public void location() {
        // 获取地理位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            // 如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            // 如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }
        // 获取Location
        Location location = locationManager.getLastKnownLocation(locationProvider);
        Log.e("location", "ccccccccccccccccccc");
        if (location != null) {
            // 不为空,显示地理位置经纬度
            AppConfig.longitude = location.getLongitude() + "";
            AppConfig.latitude = location.getLatitude() + "";
            Log.e("location", "维度：" + location.getLatitude() + "\n" + "经度：" + location.getLongitude());
        }
        locationManager.requestLocationUpdates(locationProvider, 2000, 1, new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.e("location", "..............onStatusChanged.....");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.e("location", "............onProviderEnabled.......");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.e("location", "....................");
            }

            @Override
            public void onLocationChanged(Location location) {
                AppConfig.longitude = location.getLongitude() + "";
                AppConfig.latitude = location.getLatitude() + "";
                Log.e("location", "维度：" + location.getLatitude() + "\n" + "经度：" + location.getLongitude());
            }
        });
    }
}
