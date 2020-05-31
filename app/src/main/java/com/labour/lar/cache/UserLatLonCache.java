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
    private String CACHE_KEY = "user_latlon_cache";

    public UserLatLonCache(Context context){
        aCache = ACache.get(context,CACHE_KEY);
    }

    public void put(UserLatLon userLatLon){
        if(aCache != null){
            ArrayList<UserLatLon> list = get();
            if(list == null){
                list = new ArrayList<>();
            }
            Log.i("UserLatLonCache","list.size(): "+list.size());
            list.add(userLatLon);
            aCache.put(CACHE_KEY,list);
        }
    }
    public void putList(ArrayList<UserLatLon> tlist){
        if(aCache != null){
            ArrayList<UserLatLon> list = (ArrayList<UserLatLon>)aCache.getAsObject(CACHE_KEY);
            if(list == null){
                list = new ArrayList<>();
            }
            list.addAll(tlist);
            aCache.put(CACHE_KEY,list);
        }
    }
    public ArrayList<UserLatLon> get(){
        if(aCache != null){
            ArrayList<UserLatLon> list = (ArrayList<UserLatLon>)aCache.getAsObject(CACHE_KEY);
            return list;
        }
       return null;
    }
    public void clear(){
        if(aCache != null){
            aCache.remove(CACHE_KEY);
        }
    }

    @Override
    public String toString() {
        return "UserLatLonCache{" +
                "aCache=" + aCache +
                ", context=" + context +
                ", CACHE_KEY='" + CACHE_KEY + '\'' +
                '}';
    }
}
