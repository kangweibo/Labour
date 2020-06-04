package com.labour.lar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.labour.lar.adapter.FragmentViewPagerAdapter;
import com.labour.lar.cache.UserLatLonCache;
import com.labour.lar.fragment.KaoqinFrag;
import com.labour.lar.fragment.MessageFrag;
import com.labour.lar.fragment.MineFrag;
import com.labour.lar.fragment.ProjectFrag;
import com.labour.lar.keepalive.KeepLive;
import com.labour.lar.keepalive.config.ForegroundNotification;
import com.labour.lar.keepalive.config.ForegroundNotificationClickListener;
import com.labour.lar.keepalive.config.IKeepLiveService;
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
//    private KeepAliveManager keepAliveManager;

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


        //keepAliveManager = new KeepAliveManager(this);
        //keepAliveManager.startKeepAlive();



        //启动时提交gps信息
        UserLatLonCache userLatLonCache = new UserLatLonCache(this);
        ArrayList<UserLatLon> list = userLatLonCache.get();
        if(list != null){
            Log.i("UserLatLonCache","init list.size(): "+list.size());
        }

        //定义前台服务的默认样式。即标题、描述和图标
        ForegroundNotification foregroundNotification = new ForegroundNotification("测试","描述", R.mipmap.ic_launcher,
                //定义前台服务的通知点击事件
                new ForegroundNotificationClickListener() {

                    @Override
                    public void foregroundNotificationClick(Context context, Intent intent) {
                    }
                });
        //启动保活服务
        KeepLive.startWork(getApplication(), KeepLive.RunMode.ENERGY, foregroundNotification,
                //你需要保活的服务，如socket连接、定时任务等，建议不用匿名内部类的方式在这里写
                new IKeepLiveService() {
                    /**
                     * 运行中
                     * 由于服务可能会多次自动启动，该方法可能重复调用
                     */
                    @Override
                    public void onWorking() {
                        //startService();
                    }
                    /**
                     * 服务终止
                     * 由于服务可能会被多次终止，该方法可能重复调用，需同onWorking配套使用，如注册和注销broadcast
                     */
                    @Override
                    public void onStop() {
                        stopService();
                    }
                }
        );
    }
    private void startService(){
        Intent intent = new Intent(this, LocationService.class);
        intent.setComponent(new ComponentName( getPackageName(),"com.labour.lar.service.LocationService"));
        intent.setAction(Constants.LOCATION_SERVICE_ACTION);
        startService(intent);

        Intent fenceIntent = new Intent(this, LocationFenceService.class);
        fenceIntent.setComponent(new ComponentName( getPackageName(),"com.labour.lar.service.LocationFenceService"));
        fenceIntent.setAction(Constants.LOCATION_FENCE_SERVICE_ACTION);
        startService(fenceIntent);
    }
    private void stopService(){
        Intent intent = new Intent(this, LocationService.class);
        stopService(intent);
        Intent fenceIntent = new Intent(this, LocationFenceService.class);
        stopService(fenceIntent);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
//        super.onBackPressed(); //注释super,拦截返回键功能
    }
}
