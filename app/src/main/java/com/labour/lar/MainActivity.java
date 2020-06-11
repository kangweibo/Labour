package com.labour.lar;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.labour.lar.adapter.FragmentViewPagerAdapter;
import com.labour.lar.cache.UserLatLonCache;
import com.labour.lar.cache.UserSignCache;
import com.labour.lar.fragment.GisMapFrag;
import com.labour.lar.fragment.KaoqinFrag;
import com.labour.lar.fragment.MessageFrag;
import com.labour.lar.fragment.MineFrag;
import com.labour.lar.fragment.ProjectFrag;
import com.labour.lar.keepalive.KeepLive;
import com.labour.lar.keepalive.config.ForegroundNotification;
import com.labour.lar.keepalive.config.ForegroundNotificationClickListener;
import com.labour.lar.keepalive.config.IKeepLiveService;
import com.labour.lar.module.UserLatLon;
import com.labour.lar.service.KeepLiveService;
import com.labour.lar.service.LocationHttp;
import com.labour.lar.util.ConnectionState;
import com.labour.lar.service.LocationFenceService;
import com.labour.lar.service.LocationService;
import com.labour.lar.widget.MainScrollViewPager;
import com.labour.lar.widget.TabBarView;

import java.util.ArrayList;
import java.util.Iterator;

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
    private LocalBroadcastManager localBroadcastManager;
    private CheckInOutBroadcastReceiver checkInOutBroadcastReceiver = new CheckInOutBroadcastReceiver();

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

        //启动保活服务
        KeepLive.startWork(getApplication(), KeepLive.RunMode.ENERGY,
                new ForegroundNotification("测试","描述", R.mipmap.ic_launcher, //定义前台服务的默认样式。即标题、描述和图标
                        //定义前台服务的通知点击事件
                        new ForegroundNotificationClickListener() {
                            @Override
                            public void foregroundNotificationClick(Context context, Intent intent) {
                            }
                        }),
                //你需要保活的服务，如socket连接、定时任务等，建议不用匿名内部类的方式在这里写
                new KeepLiveService(this)
        );

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.SIGN_CHECK_IN_OUT_RECEIVER_ACTION);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(checkInOutBroadcastReceiver, filter);

        //提交缓存的经纬度数据
        DaemoConsumerUserLatLon daemoThread = new DaemoConsumerUserLatLon();
        daemoThread.start();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
//        super.onBackPressed(); //注释super,拦截返回键功能
    }

    //签到后重新启动一次定位
    class CheckInOutBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Constants.SIGN_CHECK_IN_OUT_RECEIVER_ACTION)){
                int signState = intent.getIntExtra("signState",-1);
                //开始启动gps记录
                if(signState == 1) { //1：签到 , 2：签退
                    UserSignCache.signIn(context);
                    //启动后台定位
                    new KeepLiveService(MainActivity.this).onWorking();
                } else {
                    UserSignCache.signOut(context);
                }
            }
        }
    }

    @Override
    public void onPause() {
        if(this.isFinishing()){
            localBroadcastManager.unregisterReceiver(checkInOutBroadcastReceiver);
        }
        super.onPause();
    }

    class DaemoConsumerUserLatLon extends Thread {

        @Override
        public void run() {
            //启动时提交gps信息 单独处理缓存数据避免和定位缓存冲突
            UserLatLonCache userLatLonCache = new UserLatLonCache(MainActivity.this);
            userLatLonCache.copy(UserLatLonCache.LATLON_CACHE_KEY,UserLatLonCache.TEMP_LATLON_CACHE_KEY);
            userLatLonCache.remove(UserLatLonCache.LATLON_CACHE_KEY);

            ArrayList<UserLatLon> list = userLatLonCache.get(UserLatLonCache.TEMP_LATLON_CACHE_KEY);
            if(list != null && list.size() > 0) {
                Log.i("UserLatLonCache","init list.size(): "+list.size());
                Iterator<UserLatLon> it = list.iterator();
                while(it.hasNext()){
                    //同步提交数据
                    LocationHttp.request(it.next(), new LocationHttp.OnProcessResultListener() {
                        @Override
                        public void onSuccess() {
                            it.remove();
                        }
                        @Override
                        public void onError() {
                        }
                    });
                }
                if(list.size() > 0){
                    userLatLonCache.putList(list,UserLatLonCache.TEMP_LATLON_CACHE_KEY);
                } else {
                    userLatLonCache.remove(UserLatLonCache.TEMP_LATLON_CACHE_KEY);
                }
            }

        }
    }
}
