package com.labour.lar.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.interfaces.MapCameraMessage;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.Text;
import com.amap.api.maps2d.model.TextOptions;
import com.labour.lar.BaseFragment;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.util.AMapGeoFence;
import com.labour.lar.util.MapUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GisMapFrag extends Fragment implements AMap.OnMarkerClickListener, AMap.OnMapLoadedListener, AMap.OnInfoWindowClickListener {

    @BindView(R.id.map)
    MapView mapView;

    protected Unbinder unbinder;

    private AMap aMap;
//    private AMapLocationClient mlocationClient;
//    private AMapLocationClientOption mLocationOption;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private static final int NO_STROKE_COLOR = Color.argb(0, 0, 0, 0);
    private static final int NO_FILL_COLOR = Color.argb(0, 0, 0, 0);

    //实时定位
    private LocalBroadcastManager localBroadcastManager;
    private MapLocationReceiver mapLocationReceiver = new MapLocationReceiver();

    //地理围栏
    private AMapGeoFence mAMapGeoFence;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_gismap, container, false);
        unbinder = ButterKnife.bind(this, view);
        //定义了一个地图view
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。

        //初始化地图控制器对象
        setUpMapIfNeeded();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.LOCATION_MAP_RECEIVER_ACTION);
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext()) ;
        localBroadcastManager.registerReceiver( mapLocationReceiver , filter );

        mAMapGeoFence = new AMapGeoFence(getContext(), aMap, handler);
    }
    /**
     * 设置一些amap的属性
     */
    private void setUpMapIfNeeded() {
        if (aMap == null) {
            aMap = mapView.getMap();

//        aMap.setLocationSource(this);// 设置定位监听
            aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
            aMap.setMyLocationEnabled(false);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

            aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
            aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
            aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
            aMap.moveCamera(CameraUpdateFactory.zoomTo(13));//18
            //setupLocationStyle();
        }
    }

   /* private void setupLocationStyle(){
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.location_marker));//gps_point
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(NO_STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(1.0f);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(NO_FILL_COLOR);
        //设置定位频次方法，单位：毫秒，默认值：1000毫秒，如果传小于1000的任何值将按照1000计算。该方法只会作用在会执行连续定位的工作模式上。
        myLocationStyle.interval(1000 * 30);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }*/

    /**
     * 监听amap地图加载成功事件回调
     */
    @Override
    public void onMapLoaded() {
        // 设置所有maker显示在当前可视区域地图中
//        LatLngBounds bounds = new LatLngBounds.Builder()
//                .include(Constants.XIAN).include(Constants.CHENGDU)
//                .include(latlng).include(Constants.ZHENGZHOU).include(Constants.BEIJING).build();
//        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
    }
    /**
     * 监听点击infowindow窗口事件回调
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getContext(), "你点击了infoWindow窗口" + marker.getTitle(),Toast.LENGTH_SHORT).show();
    }
    /**
     * 对marker标注点点击响应事件
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        Toast.makeText(getContext(), "你点击的是" + marker.getTitle(),Toast.LENGTH_SHORT).show();
        return false;
    }
    /**
     * 在地图上添加marker
     */
//    private void addMarkersToMap() {
//
//
//
////        aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
////                .position(Constants.CHENGDU).title("成都市")
////                .snippet("成都市:30.679879, 104.064855").draggable(true));
//
//        MarkerOptions markerOption = new MarkerOptions();
//        markerOption.position(Constants.XIAN);
//        markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");
//        markerOption.draggable(true);
//        markerOption.icon(BitmapDescriptorFactory
//                .fromResource(R.drawable.arrow));
//        marker2 = aMap.addMarker(markerOption);
//        marker2.showInfoWindow();
//        // marker旋转90度
//        marker2.setRotateAngle(90);
//
//        // 动画效果
//        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
//        giflist.add(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        giflist.add(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        giflist.add(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//        aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//                .position(Constants.ZHENGZHOU).title("郑州市").icons(giflist)
//                .draggable(true).period(10));
//
//        //drawMarkers();// 添加10个带有系统默认icon的marker
//    }

    /**
     * 绘制系统默认的1种marker背景图片
     */
    public void drawMarkers(LatLng latlng) {
        Marker marker = aMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title("好好学习")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .draggable(true));
        marker.showInfoWindow();// 设置默认显示一个infowinfow
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mAMapGeoFence.removeAll();
        localBroadcastManager.unregisterReceiver(mapLocationReceiver);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
//                    Toast.makeText(getApplicationContext(), "添加围栏成功",
//                            Toast.LENGTH_SHORT).show();
                    mAMapGeoFence.drawFenceToMap();
                    break;
                case 1:
                    int errorCode = msg.arg1;
                    Toast.makeText(getContext(),
                            "添加围栏失败 " + errorCode, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    String statusStr = (String) msg.obj;
                    Toast.makeText(getContext(), statusStr,
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    /* *//**
     * 定位成功后回调函数
     *//*
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                Log.e("amap", "onMyLocationChange 定位成功， lat: " + amapLocation.getLatitude() + " lon: " + amapLocation.getLongitude());
                int errorCode = amapLocation.getErrorCode();
                String errorInfo =amapLocation.getErrorInfo();
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = amapLocation.getLocationType();
                *//*
                errorCode
                errorInfo
                locationType
                *//*
                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType );
                //mLocationErrText.setVisibility(View.GONE);
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                aMap.moveCamera(CameraUpdateFactory.zoomTo(18));

                //测试
                //清空地图上所有已经标注的marker
                if (aMap != null) {
                    aMap.clear();
                }
                LatLng latlng = new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
                drawMarkers(latlng);
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("amap",errText);
                //mLocationErrText.setVisibility(View.VISIBLE);
                //mLocationErrText.setText(errText);
            }
        }
    }

    *//**
     * 激活定位
     *//*
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getContext());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    *//**
     * 停止定位
     *//*
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }*/

    public class MapLocationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            /*String action = intent.getAction();
            if (action.equals(Constants.LOCATION_MAP_RECEIVER_ACTION)){
                LatLng latlng = (LatLng)intent.getParcelableExtra("latlng");
                Log.e("amap", "MapLocationReceiver onMyLocationChange 定位成功， lat: " + latlng.latitude + " lon: " + latlng.longitude);
                //清空地图上所有已经标注的marker
                if (aMap != null) {
                    aMap.clear();
                    drawMarkers(latlng);

                    //测试
                    CameraUpdate cameraUpdate = CameraUpdateFactory.changeLatLng(latlng);
                    aMap.moveCamera(cameraUpdate);
                }
            }*/
        }

    }
}
