package com.labour.lar.util;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Author: lx
 * CreateDate: 2019/6/29 16:56
 * Company Hebei Xiaoxiong Technology Co., Ltd.
 * Description:
 */
public class AjaxResult {
    private JSONObject jsonObj;

    public AjaxResult(String json){
        try{
            this.jsonObj = JSON.parseObject(json);
        }catch (Exception e){
            jsonObj = new JSONObject();
            jsonObj.put("status",-1);
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return jsonObj.toJSONString();
    }

    //{"code":"200","data":"","msg":"发送成功，请注意查收"}
    public JSONObject getJsonObj(){
        return jsonObj;
    }
    public JSONObject getJsonObj(String key){
        return jsonObj.getJSONObject(key);
    }
    public String getMsg() {
        return jsonObj.getString("errmsg");
    }
    public String getInfo() {
        return jsonObj.getString("info");
    }
    public JSONObject getData() {
        return jsonObj.getJSONObject("data");
    }
    public JSONArray getJSONArrayData(){
        return jsonObj.getJSONArray("data");
    }
    public String getKey() {
        return jsonObj.getString("key");
    }
    public int getStatus(){
        return jsonObj.getIntValue("status");
    }
    public int getCode(){
        return jsonObj.getIntValue("code");
    }
    public int getSuccess(){
        return jsonObj.getIntValue("success");
    }
    public String getString(String key){
        return jsonObj.getString(key);
    }
    public int getInt(String key){
        return jsonObj.getIntValue(key);
    }
}
