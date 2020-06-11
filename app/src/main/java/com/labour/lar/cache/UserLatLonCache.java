package com.labour.lar.cache;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.module.UserLatLon;

import java.util.ArrayList;

public class UserLatLonCache {

    private ACache aCache;
    private Context context;
    public static final String LATLON_CACHE_KEY = "user_latlon_cache";
    public static final String TEMP_LATLON_CACHE_KEY = "temp_user_latlon_cache";

    public UserLatLonCache(Context context){
        aCache = ACache.get(context,"latLon");
    }

    public synchronized void put(UserLatLon userLatLon,String key){
        if(aCache != null){
            ArrayList<UserLatLon> list = get(key);
            if(list == null){
                list = new ArrayList<>();
            }
            Log.i("UserLatLonCache","list.size(): "+list.size());
            list.add(userLatLon);
            aCache.put(key,list);
        }
    }
    public synchronized void putList(ArrayList<UserLatLon> tlist,String key){
        if(aCache != null){
            ArrayList<UserLatLon> list = (ArrayList<UserLatLon>)aCache.getAsObject(key);
            if(list == null){
                list = new ArrayList<>();
            }
            list.addAll(tlist);
            aCache.put(key,list);
        }
    }
    public synchronized ArrayList<UserLatLon> get(String key){
        if(aCache != null){
            ArrayList<UserLatLon> list = (ArrayList<UserLatLon>)aCache.getAsObject(key);
            return list;
        }
       return null;
    }
    public synchronized void remove(String key){
        if(aCache != null){
            aCache.remove(key);
        }
    }

    public synchronized void copy(String sourceKey,String distKey) {
        ArrayList<UserLatLon> slist = get(sourceKey);
        if(slist != null && slist.size() > 0){
            ArrayList<UserLatLon> dlist = get(distKey);
            if(dlist == null){
                dlist = new  ArrayList<UserLatLon>();
            }
            dlist.addAll(slist);
            aCache.put(distKey,dlist);
        }
    }

    @Override
    public String toString() {
        return "UserLatLonCache{" +
                "aCache=" + aCache +
                ", context=" + context +
                '}';
    }
}
