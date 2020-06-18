package com.labour.lar.map;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;

/**
 * 定位管理器
 */
public class LocationManager {

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Context context;
    private AMapLocationListener locationListener;
    private GeocodeSearch.OnGeocodeSearchListener onGeocodeSearchListener;
    private boolean once = false;
    private long interval = 1000;

    private GeocodeSearch geocoderSearch;

    /**
     *
     * @param context
     * @param once
     * @param locationListener 定位
     */
    public LocationManager(Context context, boolean once,long interval,AMapLocationListener locationListener) {
        this.context = context;
        this.once = once;
        this.interval = interval;
        this.locationListener = locationListener;
    }

    /**
     *
     * @param context
     * @param once
     * @param locationListener 定位
     * @param onGeocodeSearchListener 地理反编码查询
     */
    public LocationManager(Context context, boolean once, long interval, AMapLocationListener locationListener, GeocodeSearch.OnGeocodeSearchListener onGeocodeSearchListener ) {
        this.context = context;
        this.once = once;
        this.interval = interval;
        this.locationListener = locationListener;
        this.onGeocodeSearchListener = onGeocodeSearchListener;
    }

    public boolean createClientIfNeeded() {
        if(mlocationClient == null){
            mlocationClient = new AMapLocationClient(context);
            mLocationOption = new AMapLocationClientOption();
            // 每10秒定位一次
            mLocationOption.setInterval(interval);
            //获取一次定位结果：
            //该方法默认为false。
            mLocationOption.setOnceLocation(once);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            //设置定位监听
            mlocationClient.setLocationListener(locationListener);
        }
        return mlocationClient == null?false:true;
    }

    public void startLocation() {
        if (mlocationClient != null) {
            mlocationClient.startLocation();
        }
    }

    public void release(){
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
        geocoderSearch = null;
    }

    /**
     * 地理反编码查询
     */
    public void createGeoCodeSearchIfNeeded(){
        if(geocoderSearch == null){
            geocoderSearch = new GeocodeSearch(context);
            geocoderSearch.setOnGeocodeSearchListener(onGeocodeSearchListener);
        }
    }
    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        createGeoCodeSearchIfNeeded();
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }
}
