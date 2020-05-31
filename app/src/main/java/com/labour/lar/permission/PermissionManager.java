package com.labour.lar.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class PermissionManager implements EasyPermissions.PermissionCallbacks {

    //如果设置了target > 28，需要增加这个权限，否则不会弹出"始终允许"这个选择框
    private static String BACK_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";
    private String[] perms = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
    };

    private Activity mContext;
    private BatteryPermisson batteryPermisson;

    private PermissionManager(Activity mContext){
        this.mContext = mContext;
        if(Build.VERSION.SDK_INT > 28
                && mContext.getApplicationInfo().targetSdkVersion > 28) {
            perms = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    BACK_LOCATION_PERMISSION
            };
        }

        this.batteryPermisson = new BatteryPermisson(mContext);
    }
    public static PermissionManager getInstance(Activity mContext){
        return new PermissionManager(mContext);
    }
    public void checkAllPermissions(){
        if(!checkPermissions(mContext,perms)){
            requestPermissions(mContext,"当前应用缺少必要权限。请点击\"确定\"-打开所需权限",101, perms);
        }

        checkBatteryPermisson();
    }
    /**
     * 检查权限
     * @param context
     * return true:已经获取权限
     * return false: 未获取权限，主动请求权限
     */
    // @AfterPermissionGranted 是可选的
    public boolean checkPermissions(Activity context, String[] perms) {
        return EasyPermissions.hasPermissions(context, perms);
    }
    /**
     * 请求权限
     * @param context
     */
    public void requestPermissions(Activity context,String tip,int requestCode,String[] perms) {
        EasyPermissions.requestPermissions(context, tip,requestCode,perms);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(mContext, "用户授权成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(mContext, "用户授权失败",Toast.LENGTH_SHORT).show();
        /**
         * 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
         * 这时候，需要跳转到设置界面去，让用户手动开启。
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(mContext, perms)) {
            new AppSettingsDialog.Builder(mContext).setTitle("提示信息").setRationale("当前应用缺少必要权限。请点击\"确定\"-打开所需权限").build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //将请求结果传递EasyPermission库处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //电池白名单权限
    public void checkBatteryPermisson(){
        this.batteryPermisson.check();
    }

    //厂商后台管理 手机管家白名单设置页面
    public void requestMobileButlerSetting(){
        this.batteryPermisson.requestMobileButlerSetting();
    }


}
