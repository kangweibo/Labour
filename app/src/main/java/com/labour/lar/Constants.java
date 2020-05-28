package com.labour.lar;

public interface Constants {

    public static final int REQUEST_CODE_SELECT_IMAGE = 1;

    public static final String HTTP_BASE = "http://xlzx.cyf666.cn/";
    public static final String IMAGE_HTTP_BASE = "http://xlzx.cyf666.cn/public";

    public static final int DELAY_TIMES = 1200;
    public static final String PULLTYPE_DOWN = "pulldown";
    public static final String PULLTYPE_UP = "pullup";

    //public static final String WX_APP_ID = "wx7b3b870087366108";
   // public static final String WX_SECRET = "a842a4d7b05d3901ec8da0392e0fe0aa";

    public static String LOCATION_SERVICE_ACTION = "com.labour.lar.service.LocationService.ACTION";
    public static String LOCATION_RECEIVER_ACTION = "com.labour.lar.receiver.LocationReceiver.ACTION";
    public static String LOCATION_RECEIVER_PERMISSION = "com.labour.lar.receiver.LocationReceiver.PERMISSION";
    public static String LOCATION_MAP_RECEIVER_ACTION = "com.labour.lar.receiver.LocationReceiver.MAP_ACTION";
}
