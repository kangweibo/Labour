package com.labour.lar.cache;

import android.content.Context;

public class TokenCache {
    private static TokenCache tokenCache;
    private Context context;
    private TokenCache(Context context){
        this.context = context;
    }
    public static TokenCache getInstance(Context context){
        if(tokenCache == null){
            tokenCache = new TokenCache(context);
        }
        return tokenCache;
    }
    public String get(){
        return "063d91b4f57518ff";
    }
}
