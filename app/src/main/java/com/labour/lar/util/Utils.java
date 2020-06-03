package com.labour.lar.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lx on 2017/7/6.
 */

public class Utils {
    public static final String BASE_DIRECTORY = "lxdata";
    public static final String CACHE_DIRECTORY = "cache";
    public static final String CACHE_AUDIO = "/audio";
    public static final String CACHE_CAMERA = "/camera";

    public static int getResurceId(String imageName, Class<?> c){
        try {
            Field field = c.getDeclaredField(imageName);
            field.setAccessible(true);
            return field.getInt(field);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getStoragePath(){
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED) && !storageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            String path = Environment.getExternalStorageDirectory().getPath() + File.separator + BASE_DIRECTORY;
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }
            return path;
        }
        return null;
    }
    public static File getCachePath(){
        String cache_path = Utils.getStoragePath() + "/" + CACHE_DIRECTORY;
        File f = new File(cache_path);
        if(!f.isDirectory()){
            f.mkdirs();
        }
        return new File(cache_path);
    }

    public static String getTakePhotoPath(){
        String path = getStoragePath() + File.separator +CACHE_CAMERA;
        File f = new File(path);
        if(!f.isDirectory()){
            f.mkdirs();
        }
        return path;
    }
    public static String getAudioPath(){
        String path = getStoragePath() + File.separator +CACHE_AUDIO;
        File f = new File(path);
        if(!f.isDirectory()){
            f.mkdirs();
        }
        return path;
    }
    public static interface DelayCallBack {
        public void callback();
    }
    public static void postDelay(final DelayCallBack ck,long delay){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ck.callback();
            }
        }, delay);
    }
    public static void  postDelay(final DelayCallBack ck){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ck.callback();
            }
        }, 1300);
    }

    public static String getAppName(int pID, Context context) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (StringUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    /**
     * Try to return the absolute file path from the given Uri  兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    /**
     *
     * @param dirPath
     * @return
     */
    public static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    public static class Version {

        public static int getAppVersionCode(Context context) {
            try {
                PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                return info.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                return 0;
            }
        }

        public static String getAppVersionName(Context context) {
            try {
                PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                return info.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                return "1.0";
            }
        }

        /**
         * 获取APK版本号(versionCode)
         *
         * @param apkPath
         * @return
         */
        public static int getApkVersionCode(Context context, String apkPath) {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
            int versionCode = 1;
            if (pi != null) {
                versionCode = pi.versionCode;
            }
            return versionCode;
        }

        /**
         * 关闭键盘
         *
         * @param activity
         */
        public static void closeKeyboard(Activity activity) {
            if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                if (activity.getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }

        /**
         * 判断是否有虚拟底部按钮
         *
         * @return
         */
        public static boolean checkDeviceHasNavigationBar(Context context) {
            boolean hasNavigationBar = false;
            Resources rs = context.getResources();
            int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
            if (id > 0) {
                hasNavigationBar = rs.getBoolean(id);
            }
            try {
                Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
                Method m = systemPropertiesClass.getMethod("get", String.class);
                String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
                if ("1".equals(navBarOverride)) {
                    hasNavigationBar = false;
                } else if ("0".equals(navBarOverride)) {
                    hasNavigationBar = true;
                }
            } catch (Exception e) {
                Log.w("Utils", e);
            }
            return hasNavigationBar;
        }

        /**
         * 获取底部虚拟按键高度
         *
         * @return
         */
        public static int getNavigationBarHeight(Context context) {
            int navigationBarHeight = 0;
            Resources rs = context.getResources();
            int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
            if (id > 0 && checkDeviceHasNavigationBar(context)) {
                navigationBarHeight = rs.getDimensionPixelSize(id);
            }
            return navigationBarHeight;
        }
    }

    /**
     * 判断本应用是否存活
     * 如果需要判断本应用是否在后台还是前台用getRunningTask
     * */
    public static boolean isAPPALive(Context mContext,String packageName){
        boolean isAPPRunning = false;
        // 获取activity管理对象
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取所有正在运行的app
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
        // 遍历，进程名即包名
        for(ActivityManager.RunningAppProcessInfo appInfo : appProcessInfoList){
            if(packageName.equals(appInfo.processName)){
                isAPPRunning = true;
                break;
            }
        }
        return isAPPRunning;
    }
}
