package com.labour.lar.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.labour.lar.Constants;
import com.labour.lar.keepalive.KeepLive;
import com.labour.lar.keepalive.config.IKeepLiveService;

public class KeepLiveService implements IKeepLiveService {

    private Context context;

    public KeepLiveService(Context context){
        this.context = context;
    }

    //可多次调用
    @Override
    public void onWorking() {
        startService();
    }

    //可多次调用配合onWorking使用
    @Override
    public void onStop() {
        stopService();
    }

    private void startService(){
        Intent intent = new Intent(context, LocationService.class);
        intent.setComponent(new ComponentName(context.getPackageName(),"com.labour.lar.service.LocationService"));
        intent.setAction(Constants.LOCATION_SERVICE_ACTION);
        context.startService(intent);

//        Intent fenceIntent = new Intent(context, LocationFenceService.class);
//        fenceIntent.setComponent(new ComponentName( context.getPackageName(),"com.labour.lar.service.LocationFenceService"));
//        fenceIntent.setAction(Constants.LOCATION_FENCE_SERVICE_ACTION);
//        context.startService(fenceIntent);
    }
    private void stopService(){
        Intent intent = new Intent(context, LocationService.class);
        context.stopService(intent);
        Intent fenceIntent = new Intent(context, LocationFenceService.class);
        context.stopService(fenceIntent);
    }
}
