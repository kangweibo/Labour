package com.labour.lar.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.model.LatLng;
import com.labour.lar.Constants;
import com.labour.lar.map.LocationManager;

public class LocationService extends Service implements AMapLocationListener {

    private LocationManager locationManager;
    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = new LocationManager(this,false,this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(locationManager.createClientIfNeeded()){
            locationManager.startLocation();
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.release();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if(amapLocation != null){
            if (amapLocation.getErrorCode() == 0) {
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = amapLocation.getLocationType();
                Log.e("amap", "LocationService onMyLocationChange 定位成功， lat: " + amapLocation.getLatitude() + " lon: " + amapLocation.getLongitude()+ " locationType: " + locationType);

                LatLng latlng = new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
                Intent intent = new Intent(Constants.LOCATION_RECEIVER_ACTION);
                intent.setComponent(new ComponentName( getPackageName(),"com.labour.lar.receiver.LocationReceiver"));
                intent.putExtra("latlng", latlng);
                sendBroadcast(intent,Constants.LOCATION_RECEIVER_PERMISSION);
            } else {
                int errorCode = amapLocation.getErrorCode();
                String errorInfo =amapLocation.getErrorInfo();
                String errText = "定位失败," + "errorCode:"+errorCode+",errorInfo:"+errorInfo;
                Log.e("amap",errText);
            }
        }

    }

}
