package com.labour.lar.cache;

import android.content.Context;

import com.labour.lar.ActivityManager;
import com.labour.lar.BaseApplication;
import com.labour.lar.module.User;

public class UserCache {
    private Context context;
    private ACache aCache;
    private static UserCache userCache;

    private UserCache(Context context){
        this.context = context;
        this.aCache = ACache.get(context,"user");
    }
    public static UserCache getInstance(Context context){
        if(userCache == null){
            userCache = new UserCache(context);
        }
        return userCache;
    }

    public void put(User user){
        BaseApplication app = BaseApplication.getInstance();
        app.setUser(user);
        if(aCache != null){
            aCache.put("key_user",user);
        }
    }
    public User get(){
        BaseApplication app = BaseApplication.getInstance();
        User user = app.getUser();
        if(user == null){
            if(aCache != null) {
                user = (User) aCache.getAsObject("key_user");
                app.setUser(user);
            }
        }
        return user;
    }

    public void clear(){
        aCache.clear();
    }
}
