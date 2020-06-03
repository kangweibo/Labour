package com.labour.lar;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import com.labour.lar.keepappalive.KeepAliveManager;
import com.labour.lar.module.UserLatLon;
import com.labour.lar.net.ConnectionState;
import com.labour.lar.service.LocationFenceService;
import com.labour.lar.service.LocationService;
import com.labour.lar.widget.MainScrollViewPager;
import com.labour.lar.widget.TabBarView;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.mainTabBarView)
    TabBarView mainTabBarView;
    @BindView(R.id.mainViewpager)
    MainScrollViewPager mainViewpager;

    private ArrayList<Fragment> frgs = new ArrayList<Fragment>();
    private FragmentViewPagerAdapter fragmentPagerAdapter;
    private int DEFAULT_SELECT_TAB = 0;
    private ConnectionState connectionState;
    private KeepAliveManager keepAliveManager;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void afterInitLayout() {
        connectionState = new ConnectionState(this);
        connectionState.getCurrentBandwidthQuality();

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

        keepAliveManager = new KeepAliveManager(this);
        keepAliveManager.startKeepAlive();



        //启动时提交gps信息
        UserLatLonCache userLatLonCache = new UserLatLonCache(this);
        ArrayList<UserLatLon> list = userLatLonCache.get();
        if(list != null){
            Log.i("UserLatLonCache","init list.size(): "+list.size());
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
//        super.onBackPressed(); //注释super,拦截返回键功能
    }
}
