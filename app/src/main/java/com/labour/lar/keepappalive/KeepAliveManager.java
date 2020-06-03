package com.labour.lar.keepappalive;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.labour.lar.Constants;
import com.labour.lar.service.LocationFenceService;
import com.labour.lar.service.LocationService;

/**
 * 一： 如果手机版本低于5.0，我们采用的方案是
 * 方案2:常规方案：普通service修改onstartcommand，onDestroy等
 * 方案3:双service，即我们常说的利用android framework层notification的一个漏洞，提高service的优先级；一个service展示一个通知，另一个service将那个通知隐藏；
 * 方案8:通过系统闹钟的方式，每隔固定时间就启动service
 *
 * 二： 如果手机版本高于等于5.0，我们直接抛弃其他方案，只使用jobservice
 */
public class KeepAliveManager {

    private Context context;
    private JobSchedulerManager mJobManager;

    public KeepAliveManager(Context context) {
        this.context = context;
    }

    public void startKeepAlive(){

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){//android 5.0 21
            Intent intent = new Intent(context, DaemonService.class);
            intent.setComponent(new ComponentName(Constants.PKG_NAME,"com.labour.lar.keepappalive.DaemonService"));
            context.startService(intent);
        } else {
            mJobManager = JobSchedulerManager.getJobSchedulerInstance(context);
            mJobManager.startJobScheduler();
       }
    }

}
