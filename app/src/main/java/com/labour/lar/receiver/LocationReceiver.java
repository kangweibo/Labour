package com.labour.lar.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.amap.api.maps2d.model.LatLng;
import com.labour.lar.Constants;

//广播接收器的onReceive()方法是在主线程进行，是会阻塞主线程的，如果onReceive方法在10秒内没有处理完，则应用就会出现ANR
public class LocationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Constants.LOCATION_RECEIVER_ACTION)){
            LatLng latlng = (LatLng)intent.getParcelableExtra("latlng");
            Log.e("amap", "LocationReceiver onMyLocationChange 定位成功， lat: " + latlng.latitude + " lon: " + latlng.longitude);

            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context) ;
            intent.setAction(Constants.LOCATION_MAP_RECEIVER_ACTION);
            localBroadcastManager.sendBroadcast(intent) ;
        }
    }

}
