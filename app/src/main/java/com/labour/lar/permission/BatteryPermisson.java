package com.labour.lar.permission;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

public class BatteryPermisson {

    private Context context;

    public BatteryPermisson(Context context){
        this.context = context;
    }

    //android 6.0
    public void check(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (!isIgnoringBatteryOptimizations()) {
                requestIgnoreBatteryOptimizations();
            }
        }
    }

    //申请加入白名单,系统弹窗会有影响电池续航的提醒
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestIgnoreBatteryOptimizations() {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + this.context.getPackageName()));
            ((Activity)context).startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断我们的应用是否在电池白名单中
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isIgnoringBatteryOptimizations() {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) this.context.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(this.context.getPackageName());
        }
        return isIgnoring;
    }

    //厂商后台管理 手机管家白名单设置页面
    public void requestMobileButlerSetting(){
        if(isHuawei()) goHuaweiSetting();
        if(isXiaomi()) goXiaomiSetting();
        if(isOPPO()) goOPPOSetting();
        if(isVIVO()) goVIVOSetting();
        if(isMeizu()) goMeizuSetting();
        if(isSamsung()) goSamsungSetting();
        if(isSmartisan()) goSmartisanSetting();
    }
    /**
     * 跳转到指定应用的首页
     */
    private void showActivity(@NonNull String packageName) {
        Intent intent = this.context.getPackageManager().getLaunchIntentForPackage(packageName);
        ((Activity)context).startActivity(intent);
    }

    /**
     * 跳转到指定应用的指定页面
     */
    private void showActivity(@NonNull String packageName, @NonNull String activityDir) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityDir));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ((Activity)context).startActivity(intent);
    }
    //把应用加入厂商系统的后台管理白名单
    //华为
    public boolean isHuawei() {
        if (Build.BRAND == null) {
            return false;
        } else {
            return Build.BRAND.toLowerCase().equals("huawei") || Build.BRAND.toLowerCase().equals("honor");
        }
    }
    //跳转华为手机管家 操作步骤：应用启动管理 -> 关闭应用开关 -> 打开允许自启动
    private void goHuaweiSetting() {
        try {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
        } catch (Exception e) {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.bootstart.BootStartActivity");
        }
    }

    //小米厂商判断
    public static boolean isXiaomi() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("xiaomi");
    }
    //跳转小米安全中心的自启动管理页面 操作步骤：授权管理 -> 自启动管理 -> 允许应用自启动
    private void goXiaomiSetting() {
        showActivity("com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity");
    }

    //OPPO
    //厂商判断
    public static boolean isOPPO() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("oppo");
    }
    // 跳转 OPPO 手机管家： 操作步骤：权限隐私 -> 自启动管理 -> 允许应用自启动
    private void goOPPOSetting() {
        try {
            showActivity("com.coloros.phonemanager");
        } catch (Exception e1) {
            try {
                showActivity("com.oppo.safe");
            } catch (Exception e2) {
                try {
                    showActivity("com.coloros.oppoguardelf");
                } catch (Exception e3) {
                    showActivity("com.coloros.safecenter");
                }
            }
        }
    }

    // VIVO
    // 厂商判断：
    public static boolean isVIVO() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("vivo");
    }

    //跳转 VIVO 手机管家：操作步骤：权限管理 -> 自启动 -> 允许应用自启动
    private void goVIVOSetting() {
        showActivity("com.iqoo.secure");
    }

    //魅族
    //厂商判断：
    public static boolean isMeizu() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("meizu");
    }

    //跳转魅族手机管家： 操作步骤：权限管理 -> 后台管理 -> 点击应用 -> 允许后台运行
    private void goMeizuSetting() {
        showActivity("com.meizu.safe");
    }

    //三星
    // 厂商判断：
    public static boolean isSamsung() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("samsung");
    }
    //跳转三星智能管理器：操作步骤：自动运行应用程序 -> 打开应用开关 -> 电池管理 -> 未监视的应用程序 -> 添加应用
    private void goSamsungSetting() {
        try {
            showActivity("com.samsung.android.sm_cn");
        } catch (Exception e) {
            showActivity("com.samsung.android.sm");
        }
    }

    //乐视
    //厂商判断：
    public static boolean isLeTV() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("letv");
    }
    //跳转乐视手机管家：操作步骤：自启动管理 -> 允许应用自启动
    private void goLetvSetting() {
        showActivity("com.letv.android.letvsafe",
                "com.letv.android.letvsafe.AutobootManageActivity");
    }

    //锤子
    //厂商判断：
    public static boolean isSmartisan() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("smartisan");
    }
    //跳转手机管理：权限管理 -> 自启动权限管理 -> 点击应用 -> 允许被系统启动
    private void goSmartisanSetting() {
        showActivity("com.smartisanos.security");
    }


}
