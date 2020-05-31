package com.labour.lar;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
import com.labour.lar.adapter.FragmentViewPagerAdapter;
import com.labour.lar.cache.UserLatLonCache;
import com.labour.lar.fragment.KaoqinFrag;
import com.labour.lar.fragment.MessageFrag;
import com.labour.lar.fragment.MineFrag;
import com.labour.lar.fragment.ProjectFrag;
import com.labour.lar.module.UserLatLon;
import com.labour.lar.service.LocationFenceService;
import com.labour.lar.service.LocationService;
import com.labour.lar.widget.MainScrollViewPager;
import com.labour.lar.widget.TabBarView;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements ConnectionClassManager.ConnectionClassStateChangeListener {

    @BindView(R.id.mainTabBarView)
    TabBarView mainTabBarView;
    @BindView(R.id.mainViewpager)
    MainScrollViewPager mainViewpager;

    private ArrayList<Fragment> frgs = new ArrayList<Fragment>();
    private FragmentViewPagerAdapter fragmentPagerAdapter;
    private int DEFAULT_SELECT_TAB = 0;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void afterInitLayout() {
        ConnectionClassManager.getInstance().register(this);
        DeviceBandwidthSampler.getInstance().startSampling();

        frgs.add(new ProjectFrag());
        frgs.add(new MessageFrag());
        frgs.add(new MineFrag());
        frgs.add(new KaoqinFrag());

        fragmentPagerAdapter = new FragmentViewPagerAdapter(fm,mainViewpager,frgs);

        mainViewpager.setCanScroll(false);
        mainViewpager.setAdapter(fragmentPagerAdapter);
        mainTabBarView.setViewPager(mainViewpager);
        mainTabBarView.selectedTab(DEFAULT_SELECT_TAB);

        Intent intent = new Intent(this,LocationService.class);
        intent.setComponent(new ComponentName( getPackageName(),"com.labour.lar.service.LocationService"));
        intent.setAction(Constants.LOCATION_SERVICE_ACTION);
        startService(intent);

        Intent fenceIntent = new Intent(this, LocationFenceService.class);
        fenceIntent.setComponent(new ComponentName( getPackageName(),"com.labour.lar.service.LocationFenceService"));
        fenceIntent.setAction(Constants.LOCATION_FENCE_SERVICE_ACTION);
        startService(fenceIntent);

        UserLatLonCache userLatLonCache = new UserLatLonCache(this);
//        userLatLonCache.clear();
        ArrayList<UserLatLon> list = userLatLonCache.get();
        if(list != null){
            Log.i("UserLatLonCache","init list.size(): "+list.size());
//            for(UserLatLon u : list){
//                Log.i("JSONArrayUserLatLon",u.toString());
//            }
        }

        ConnectionQuality cq = ConnectionClassManager.getInstance().getCurrentBandwidthQuality();
        //onBandwidthStateChange(cq);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(this.isFinishing()){
            ConnectionClassManager.getInstance().remove(this);
            DeviceBandwidthSampler.getInstance().stopSampling();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
        if(bandwidthState.equals(ConnectionQuality.UNKNOWN)){
            Toast.makeText(this,"网络有问题",Toast.LENGTH_SHORT).show();
        } else if(bandwidthState.equals(ConnectionQuality.POOR)){
            Toast.makeText(this,"网络较差",Toast.LENGTH_SHORT).show();
        } else if(bandwidthState.equals(ConnectionQuality.MODERATE)){
            Toast.makeText(this,"网络还行",Toast.LENGTH_SHORT).show();
        } else if(bandwidthState.equals(ConnectionQuality.GOOD)){
            Toast.makeText(this,"网络很好",Toast.LENGTH_SHORT).show();
        } else if(bandwidthState.equals(ConnectionQuality.EXCELLENT)){
            Toast.makeText(this,"网络超级好",Toast.LENGTH_SHORT).show();
        }
    }
}
