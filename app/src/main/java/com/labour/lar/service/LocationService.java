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

public class LocationService extends Service implements AMapLocationListener {

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 每10秒定位一次
            mLocationOption.setInterval(1000);

            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
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
