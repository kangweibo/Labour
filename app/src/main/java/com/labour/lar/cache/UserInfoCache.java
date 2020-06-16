package com.labour.lar.cache;

import android.content.Context;
import com.labour.lar.BaseApplication;
import com.labour.lar.module.UserInfo;

public class UserInfoCache {
    private Context context;
    private ACache aCache;
    private static UserInfoCache userCache;

    private UserInfoCache(Context context){
        this.context = context;
        this.aCache = ACache.get(context,"user");
    }
    public static UserInfoCache getInstance(Context context){
        if(userCache == null){
            userCache = new UserInfoCache(context);
        }
        return userCache;
    }

    public void put(UserInfo user){
        BaseApplication app = BaseApplication.getInstance();
        if(aCache != null){
            aCache.put("key_userinfo",user);
        }
    }

    public UserInfo get(){
        UserInfo user = null;
        if(aCache != null) {
            user = (UserInfo) aCache.getAsObject("key_userinfo");
        }
        return user;
    }
}
