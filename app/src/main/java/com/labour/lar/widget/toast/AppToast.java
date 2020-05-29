package com.labour.lar.widget.toast;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.labour.lar.R;

/**
 * Created by Administrator on 2017/7/1 0001.
 */

public class AppToast {

    public static void show(Context context, String msg) {
        AppMsg appMsg = AppMsg.makeText((Activity) context, msg, new AppMsg.Style(1300, R.drawable.app_msg_alert_bg));
        appMsg.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        appMsg.show();
    }

    public static void show(Context context, int msgResId) {
        AppMsg appMsg = AppMsg.makeText((Activity) context, msgResId, new AppMsg.Style(1300, R.drawable.app_msg_alert_bg));
        appMsg.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        appMsg.show();
    }
    public static void show(Context context, String msg,int duration) {
        AppMsg appMsg = AppMsg.makeText((Activity) context, msg, new AppMsg.Style(duration, R.drawable.app_msg_alert_bg));
        appMsg.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        appMsg.show();
    }
}
