package com.labour.lar.service;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.labour.lar.Constants;
import com.labour.lar.module.UserLatLon;
import com.labour.lar.util.AjaxResult;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class LocationHttp {
    public static interface OnProcessResultListener {
        public void onSuccess();
        public void onError();
    }

    /**
     * 异步方法
     * @param userLatLo
     * @param onProcessResultListener
     */
    public static void requestSync(final UserLatLon userLatLo,OnProcessResultListener onProcessResultListener) {

        String jsonParams = getRequestParams(userLatLo);
        String user_url = Constants.HTTP_BASE +"/api/geoloc";
        OkGo.<String>post(user_url).upJson(jsonParams).tag("user_url").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                AjaxResult jr = new AjaxResult(response.body());
                Log.i("amap",jr.toString());
                int success = jr.getSuccess();
                if(success == 1){//操作成功
                    onProcessResultListener.onSuccess();
                } else {
                    onProcessResultListener.onError();
                }
            }
            @Override
            public void onError(Response<String> response) {
//                onProcessResultListener.onError();
            }
        });
    }

    //同步调用
    public static void request(final UserLatLon userLatLo,OnProcessResultListener onProcessResultListener){

        String jsonParams = getRequestParams(userLatLo);
        String user_url = Constants.HTTP_BASE + "/api/geoloc";
        try {
            okhttp3.Response response = OkGo.<String>post(user_url).upJson(jsonParams).tag("user_url").execute();
            ResponseBody body = response.body();
            if(body != null){
                AjaxResult jr = new AjaxResult(body.string());
                Log.i("amap",jr.toString());
                int success = jr.getSuccess();
                if(success == 1){//操作成功
                    onProcessResultListener.onSuccess();
                } else {
                    onProcessResultListener.onError();
                }
            } else {
                onProcessResultListener.onError();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static String getRequestParams(final UserLatLon userLatLo){
        final Map<String, String> param = new HashMap<String, String>();

        Constants.ROLE role = userLatLo.getRole();
        if (role == Constants.ROLE.employee) {
            param.put("employee_id", userLatLo.getUserId() + "");
        } else if (role == Constants.ROLE.staff) {
            param.put("staff_id", userLatLo.getUserId() + "");
        } else if (role == Constants.ROLE.manager) {
            param.put("manager_id", userLatLo.getUserId() + "");
        }
        param.put("lon", userLatLo.getLon());
        param.put("lat", userLatLo.getLat());
        param.put("dtime", userLatLo.getCreateTime());

        String jsonParams = JSON.toJSONString(param);
        return jsonParams;
    }
}
