package com.labour.lar.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polygon;
import com.amap.api.maps2d.model.PolygonOptions;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.map.MapUtil;

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
//    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
//    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
//    private static final int NO_STROKE_COLOR = Color.argb(0, 0, 0, 0);
//    private static final int NO_FILL_COLOR = Color.argb(0, 0, 0, 0);

    //实时定位
    private LocalBroadcastManager localBroadcastManager;
    private MapLocationReceiver mapLocationReceiver = new MapLocationReceiver();

    //地理围栏
//    private AMapGeoFence mAMapGeoFence;

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

//        mAMapGeoFence = new AMapGeoFence(getContext(), aMap, handler);
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
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .draggable(true));
        marker.showInfoWindow();// 设置默认显示一个infowinfow
    }
    //朝阳公园
    private static final String mPolygonFenceString1 = "116.48954,39.949035;116.48956,39.946986;116.489545,39.945793;116.489545,39.945151;116.489547,39.944534;116.489537,39.942016;116.489542,39.940404;116.489532,39.939772;116.489516,39.936447;116.489476,39.935253;116.489468,39.933894;116.488858,39.933901;116.488646,39.933904;116.486819,39.933926;116.486029,39.933919;116.482808,39.933924;116.481209,39.933928;116.477237,39.93393;116.474738,39.93395;116.474632,39.934851;116.474466,39.935205;116.474103,39.935351;116.473723,39.935406;116.473488,39.935378;116.473412,39.935423;116.4734,39.93551;116.473558,39.935659;116.473987,39.936095;116.474344,39.936446;116.474609,39.936707;116.474815,39.936986;116.474899,39.937155;116.474915,39.93733;116.474916,39.937721;116.474919,39.93805;116.4749,39.938376;116.474871,39.938953;116.474855,39.939258;116.474816,39.93977;116.474781,39.940558;116.474724,39.941285;116.474689,39.941889;116.474649,39.942447;116.474628,39.942775;116.474608,39.943028;116.474601,39.943117;116.474551,39.943673;116.474499,39.944207;116.474488,39.944354;116.474467,39.944524;116.474415,39.945015;116.47436,39.945305;116.474335,39.945401;116.474247,39.945691;116.47414,39.946063;116.47393,39.946832;116.473854,39.94711;116.473716,39.94752;116.47362,39.947742;116.473991,39.947865;116.474236,39.947932;116.474385,39.947925;116.474506,39.94798;116.474887,39.948182;116.475212,39.948319;116.475795,39.948419;116.476327,39.948544;116.476614,39.948657;116.477115,39.948675;116.477472,39.948712;116.477793,39.948754;116.478074,39.948851;116.478588,39.948964;116.478705,39.949009;116.478752,39.949069;116.478818,39.949282;116.47883,39.949374;116.478824,39.949446;116.478789,39.949503;116.478648,39.949599;116.478616,39.949629;116.478538,39.949775;116.478451,39.949898;116.478378,39.94997;116.478336,39.950033;116.478318,39.950072;116.478328,39.950211;116.478418,39.950352;116.478481,39.95041;116.47855,39.950433;116.478575,39.95046;116.478593,39.950512;116.478417,39.950964;116.478236,39.951219;116.478168,39.951292;116.477863,39.951713;116.476722,39.953685;116.476775,39.953867;116.479074,39.954626;116.480838,39.955196;116.483278,39.955976;116.483806,39.956138;116.484569,39.956407;116.485044,39.956637;116.486039,39.95704;116.486266,39.957121;116.487219,39.957523;116.487311,39.95754;116.487474,39.957491;116.48762,39.957241;116.487655,39.957173;116.487715,39.957055;116.487805,39.956968;116.488078,39.956501;116.488223,39.956233;116.488432,39.955773;116.488625,39.955281;116.4887,39.95508;116.488843,39.954612;116.489033,39.953949;116.489154,39.95345;116.489218,39.953167;116.489278,39.952847;116.489303,39.952651;116.489309,39.952553;116.489299,39.952429;116.489257,39.952256;116.48923,39.952153;116.489223,39.951986;116.489266,39.951738;116.489317,39.951608;116.48936,39.95151;116.489402,39.951426;116.489434,39.951363;116.489466,39.951212;116.489493,39.950603;116.48954,39.94958;116.48954,39.949035";
    //绘制多边形
    public void drawPolygon(){
        List<LatLng> pointList = MapUtil.toAMapList(mPolygonFenceString1);
        LatLng firstLatLng = pointList.get(0);
        List<Polygon> polygonList = new ArrayList<Polygon>();
        PolygonOptions polygonOption = new PolygonOptions();
        polygonOption.addAll(pointList);
        polygonOption.fillColor(getResources().getColor(R.color.fence_fill));
        polygonOption.strokeColor(getResources().getColor(R.color.fence_stroke));
        polygonOption.strokeWidth(4);
        Polygon polygon = aMap.addPolygon(polygonOption);
        polygonList.add(polygon);
        if(firstLatLng != null){
            CameraUpdate cameraUpdate = CameraUpdateFactory.changeLatLng(firstLatLng);
            aMap.moveCamera(cameraUpdate);
        }
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
//        mAMapGeoFence.removeAll();
        localBroadcastManager.unregisterReceiver(mapLocationReceiver);
    }

    /*Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
//                    Toast.makeText(getApplicationContext(), "添加围栏成功",
//                            Toast.LENGTH_SHORT).show();
//                    mAMapGeoFence.drawFenceToMap();
                    drawPolygon();
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
    };*/


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
