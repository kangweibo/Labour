package com.labour.lar;

public interface Constants {
    public static final String APP_NAME = "应用名称";
    public static final String APP_DESC = "这里是简单的应用描述";
    public static final String PKG_NAME = "com.labour.lar";

    public static final int REQUEST_CODE_SELECT_IMAGE = 1;

    public static final String HTTP_BASE = "http://lhok.natapp4.cc";
    public static final String IMAGE_HTTP_BASE = "http://xlzx.cyf666.cn/public";

    public static final int DELAY_TIMES = 1200;
    public static final String PULLTYPE_DOWN = "pulldown";
    public static final String PULLTYPE_UP = "pullup";

    public static final int ERROR_CODE = -1;
    public static final int SUCCESS_CODE = 200;
    public static final String ERROR_MESSAGE= "加载数据出错";

    //public static final String WX_APP_ID = "wx7b3b870087366108";
   // public static final String WX_SECRET = "a842a4d7b05d3901ec8da0392e0fe0aa";

    //地图定位service
    public static String LOCATION_SERVICE_ACTION = "com.labour.lar.service.LocationService.ACTION";
    //定位接收
    public static String LOCATION_RECEIVER_ACTION = "com.labour.lar.receiver.LocationReceiver.ACTION";
    public static String LOCATION_RECEIVER_PERMISSION = "com.labour.lar.receiver.LocationReceiver.PERMISSION";
    //定位接收
    public static String LOCATION_MAP_RECEIVER_ACTION = "com.labour.lar.receiver.LocationReceiver.MAP_ACTION";

    //地理围栏service
    public static String LOCATION_FENCE_SERVICE_ACTION = "com.labour.lar.service.LocationFenceService.ACTION";
    //地理围栏定位接收
    public static String LOCATION_FENCE_RECEIVER_ACTION = "com.labour.lar.receiver.LocationFenceReceiver.ACTION";

    //定位接收
    public static String SIGN_CHECK_IN_OUT_RECEIVER_ACTION = "com.labour.lar.MainActivity.CheckInOutBroadcastReceiver.ACTION";

    public static final String LOGIN_ERROR_TIP = "请登录！";
    //role
    public static enum ROLE {employee,staff,manager}
}
