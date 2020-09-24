package com.labour.lar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.activity.IdentifiedActivity;
import com.labour.lar.adapter.FragmentViewPagerAdapter;
import com.labour.lar.cache.UserCache;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.cache.UserLatLonCache;
import com.labour.lar.cache.UserSignCache;
import com.labour.lar.event.BaseEvent;
import com.labour.lar.event.EventManager;
import com.labour.lar.fragment.KaoqinFrag;
import com.labour.lar.fragment.MessageFrag;
import com.labour.lar.fragment.MineFrag;
import com.labour.lar.fragment.PartyBuildFrag;
import com.labour.lar.fragment.ProjectFrag;
import com.labour.lar.keepalive.KeepLive;
import com.labour.lar.keepalive.config.ForegroundNotification;
import com.labour.lar.keepalive.config.ForegroundNotificationClickListener;
import com.labour.lar.module.User;
import com.labour.lar.module.UserInfo;
import com.labour.lar.module.UserLatLon;
import com.labour.lar.service.KeepLiveService;
import com.labour.lar.service.LocationHttp;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.ConnectionState;
import com.labour.lar.widget.DialogUtil;
import com.labour.lar.widget.MainScrollViewPager;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.TabBarView;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        EventManager.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventManager.unregister(this);
    }

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void afterInitLayout() {
        connectionState = new ConnectionState(this);
        connectionState.getCurrentBandwidthQuality();

        initFragment();

        //keepAliveManager = new KeepAliveManager(this);
        //keepAliveManager.startKeepAlive();

        //启动保活服务
//        KeepLive.startWork(getApplication(), KeepLive.RunMode.ENERGY,
//                new ForegroundNotification("劳务管理","劳务管理程序正在运行", R.mipmap.ic_launcher, //定义前台服务的默认样式。即标题、描述和图标
//                        //定义前台服务的通知点击事件
//                        new ForegroundNotificationClickListener() {
//                            @Override
//                            public void foregroundNotificationClick(Context context, Intent intent) {
//                            }
//                        }),
//                //你需要保活的服务，如socket连接、定时任务等，建议不用匿名内部类的方式在这里写
//                new KeepLiveService(this)
//        );

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.SIGN_CHECK_IN_OUT_RECEIVER_ACTION);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(checkInOutBroadcastReceiver, filter);

        //提交缓存的经纬度数据
        DaemoConsumerUserLatLon daemoThread = new DaemoConsumerUserLatLon();
        daemoThread.start();
    }

    private void initFragment() {
        frgs.clear();
        frgs.add(new ProjectFrag());
        frgs.add(new MessageFrag());
        frgs.add(new MineFrag());
        frgs.add(new KaoqinFrag());
        frgs.add(new PartyBuildFrag());

        fragmentPagerAdapter = new FragmentViewPagerAdapter(fm,mainViewpager,frgs);

        mainViewpager.setCanScroll(false);
        mainViewpager.setAdapter(fragmentPagerAdapter);
        mainTabBarView.setViewPager(mainViewpager);
        mainTabBarView.selectedTab(DEFAULT_SELECT_TAB);

        // 获取用户信息
        //getUserInfo();
        checkUserInfo();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 重新加载页面
        if (BaseApplication.getInstance().isRelogon()){
            initFragment();
        }
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

//    private void getUserInfo() {
//        UserCache userCache = UserCache.getInstance(this);
//        User user = userCache.get();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("token","063d91b4f57518ff");
//        jsonObject.put("dtype", user.getProle());
//        jsonObject.put("userid", user.getId());
//        String jsonParams =jsonObject.toJSONString();
//
//        String url = Constants.HTTP_BASE + "/api/user";
//        ProgressDialog dialog = ProgressDialog.createDialog(this);
//        dialog.show();
//
//        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
//            @Override
//            public void onSuccess(Response<String> response) {
//                dialog.dismiss();
//                AjaxResult jr = new AjaxResult(response.body());
//                if(jr.getSuccess() == 1){
//                    JSONObject jo = jr.getData();
//                    UserInfo userInfoOrg = JSON.parseObject(JSON.toJSONString(jo), UserInfo.class);
//                    UserInfo userInfo = dealWithPic(userInfoOrg);
//                    UserInfoCache.getInstance(MainActivity.this).put(userInfo);
//                    checkUserInfo(MainActivity.this, userInfo);
//                } else {
//                    AppToast.show(MainActivity.this,"获取用户信息失败!");
//                }
//            }
//            @Override
//            public void onError(Response<String> response) {
//                dialog.dismiss();
//                AppToast.show(MainActivity.this,"获取用户信息出错!");
//            }
//        });
//    }
//
//    private UserInfo dealWithPic(UserInfo userInfo) {
//        JSONObject jsonObject = JSON.parseObject(userInfo.getPic());
//        String pic = jsonObject.getString("url");
//        userInfo.setPic(pic);
//
//        jsonObject = JSON.parseObject(userInfo.getIdpic1());
//        String Idpic1 = jsonObject.getString("url");
//        userInfo.setIdpic1(Idpic1);
//
//        jsonObject = JSON.parseObject(userInfo.getIdpic2());
//        String Idpic2 = jsonObject.getString("url");
//        userInfo.setIdpic2(Idpic2);
//
//        return userInfo;
//    }

    private void checkUserInfo() {
        UserInfo userInfo = UserInfoCache.getInstance(this).get();

        if (userInfo == null || userInfo.isIdentified()){
            return;
        }

        DialogUtil.showConfirmDialog(this,"提示信息","你还未身份认证，是否去认证",
                false, new DialogUtil.OnDialogEvent<Void>(){
            @Override
            public void onPositiveButtonClick(Void t) {
                startActivity(new Intent(MainActivity.this, IdentifiedActivity.class));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        int code = event.getCode();
        if (code == 1) {
            int index = event.getPosition();
            setShowFragment(index);
        }
    }

    private void setShowFragment(int index) {
        mainTabBarView.selectedTab(index);
    }

}
