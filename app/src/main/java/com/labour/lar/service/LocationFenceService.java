package com.labour.lar.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.labour.lar.Constants;
import com.labour.lar.map.MapUtil;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocationFenceService  extends Service implements GeoFenceListener {

    private String TAG = "amap";
    private GeoFenceClient geoFenceClient;
    private int mCustomID = 100;
    // 记录已经添加成功的围栏
    private volatile ConcurrentMap<String, GeoFence> fenceMap = new ConcurrentHashMap<String, GeoFence>();

    // 地理围栏的广播action
    static final String GEOFENCE_BROADCAST_ACTION = Constants.LOCATION_FENCE_RECEIVER_ACTION;

    //国家大剧院
    private static final String mPolygonFenceString1 = "116.389616,39.907136;116.390681,39.907168;116.391089,39.90717;116.391232,39.90715;116.391389,39.90705;116.391487,39.906872;116.391501,39.906679;116.39152,39.906315;116.391543,39.905835;116.391567,39.905389;116.391613,39.904339;116.391639,39.903778;116.391648,39.903532;116.39163,39.903449;116.391601,39.903244;116.391568,39.9031;116.391549,39.903001;116.39154,39.902951;116.391505,39.902903;116.387997,39.90279;116.387966,39.903003;116.387913,39.903319;116.387921,39.903662;116.387969,39.904064;116.388062,39.904783;116.388079,39.904901;116.388101,39.905084;116.388111,39.905174;116.388141,39.905407;116.38818,39.905694;116.388214,39.905926;116.388226,39.90599;116.388257,39.906222;116.3883,39.906592;116.388299,39.906697;116.38832,39.907107;116.389616,39.907136";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter fliter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        fliter.addAction(GEOFENCE_BROADCAST_ACTION);
        registerReceiver(mGeoFenceReceiver, fliter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        addFenceAll();

        return START_REDELIVER_INTENT;
    }

    private void addFenceAll() {
        if(geoFenceClient == null){
            geoFenceClient = new GeoFenceClient(this);
            geoFenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
            geoFenceClient.setGeoFenceListener(this);
            geoFenceClient.setActivateAction(GeoFenceClient.GEOFENCE_IN | GeoFenceClient.GEOFENCE_STAYED | GeoFenceClient.GEOFENCE_OUT);
            addPolygonGeoFence(mPolygonFenceString1);
        }
    }

    private void addPolygonGeoFence(String points) {
        geoFenceClient.addGeoFence(MapUtil.toAMapGeoFenceList(points), String.valueOf(mCustomID));
        //mCustomID++;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeAll();
    }
    public void removeAll() {
        try {
            if(geoFenceClient != null){
                geoFenceClient.removeGeoFence();
            }
            unregisterReceiver(mGeoFenceReceiver);
            geoFenceClient = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onGeoFenceCreateFinished(List<GeoFence> geoFenceList, int errorCode, String s) {
        if (errorCode == GeoFence.ADDGEOFENCE_SUCCESS) {
            for (GeoFence fence : geoFenceList) {
                Log.e(TAG, "fenid:" + fence.getFenceId() + " customID:" + s + " " + fenceMap.containsKey(fence.getFenceId()));
                fenceMap.putIfAbsent(fence.getFenceId(), fence);
            }

            Log.e(TAG, "回调添加成功个数:" + geoFenceList.size());
            Log.e(TAG, "回调添加围栏个数:" + fenceMap.size());
            Message message = mHandler.obtainMessage();
            message.obj = geoFenceList;
            message.what = 0;
            mHandler.sendMessage(message);
            Log.e(TAG, "添加围栏成功！！");
        } else {
            Log.e(TAG, "添加围栏失败！！！！ errorCode: " + errorCode);
            Message msg = Message.obtain();
            msg.arg1 = errorCode;
            msg.what = 1;
            mHandler.sendMessage(msg);
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(getApplicationContext(), "添加围栏成功",Toast.LENGTH_SHORT).show();
                    //mAMapGeoFence.drawFenceToMap();
                    break;
                case 1:
                    int errorCode = msg.arg1;
                    Toast.makeText(getApplicationContext(),"添加围栏失败 " + errorCode, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    String statusStr = (String) msg.obj;
                    Toast.makeText(getApplicationContext(), statusStr,Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收广播
            if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
                Bundle bundle = intent.getExtras();
                //获取围栏行为：
                int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
                //获取围栏ID:
                String fenceID = bundle.getString(GeoFence.BUNDLE_KEY_FENCEID);
                //获取自定义的围栏标识：
                String customId = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                //获取当前有触发的围栏对象：
                GeoFence fence = bundle.getParcelable(GeoFence.BUNDLE_KEY_FENCE);

                int code = bundle.getInt(GeoFence.BUNDLE_KEY_LOCERRORCODE);
                Log.e(TAG, "定位Code:"+code);
                StringBuffer sb = new StringBuffer();
                switch (status) {
                    case GeoFence.STATUS_LOCFAIL:
                        sb.append("定位失败");
                        Log.e(TAG, "定位失败"+code);
                        break;
                    case GeoFence.STATUS_IN:
                        sb.append("进入围栏 ").append(fenceID);
                        Log.e(TAG, "进入围栏"+fenceID);
                        break;
                    case GeoFence.STATUS_OUT:
                        sb.append("离开围栏 ").append(fenceID);
                        Log.e(TAG, "离开围栏"+fenceID);
                        break;
                    case GeoFence.STATUS_STAYED:
                        sb.append("停留在围栏内 ").append(fenceID);
                        break;
                    default:
                        break;
                }
                String str = sb.toString();
                Message msg = Message.obtain();
                msg.obj = str;
                msg.what = 2;
                mHandler.sendMessage(msg);
            }
        }
    };
}
