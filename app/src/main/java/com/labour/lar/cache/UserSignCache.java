package com.labour.lar.cache;

import android.content.Context;

import com.labour.lar.BaseApplication;
import com.labour.lar.util.PreferencesUtils;

public class UserSignCache {
    private static final String sign_key = "user_sign_key";
    public static void signIn(Context context){
        BaseApplication app = BaseApplication.getInstance();
        app.setSignState("ok");
        PreferencesUtils.putBoolean(context,sign_key,true);
    }
    public static void signOut(Context context){
        BaseApplication app = BaseApplication.getInstance();
        app.setSignState("no");
        PreferencesUtils.putBoolean(context,sign_key,false);
    }
    public static boolean isSign(Context context){
        BaseApplication app = BaseApplication.getInstance();
        if(app.getSignState() != null){
            return app.getSignState().equals("ok")?true:false;
        }
        boolean f = PreferencesUtils.getBoolean(context,sign_key);
        app.setSignState(f?"ok":"no");
        return f;
    }
}
