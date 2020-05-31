package com.labour.lar.version;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.labour.lar.Constants;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.StringUtils;
import com.labour.lar.util.Utils;
import com.labour.lar.version.AppVersion;
import com.labour.lar.version.VersionDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/15.
 */

public class VersionNet {
    private final String check_app_version_url = Constants.HTTP_BASE + "/Api/index/version";
    private Context context;

    public VersionNet(Context context){
        this.context = context;
    }

    /**
     * 检查版本
     */
    public void checkAppVersion(){
        final Map<String, String> param = new HashMap<String, String>();
        OkGo.<String>get(check_app_version_url).params(param).tag("check_app_version_url").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                AjaxResult jr = new AjaxResult(response.body());
                int status = jr.getStatus();
                AppVersion versionBean = null;
                if(status == 200){
                    versionBean = JSONObject.toJavaObject(jr.getJsonObj(), AppVersion.class);
                }
                int lcode = Utils.Version.getAppVersionCode(context);
                String rversion = versionBean.getData().getCode();
                if(!StringUtils.isEmpty(rversion)){
                    try{
                        float c = Float.parseFloat(rversion);
                        if(lcode < c){
                            VersionDialog.show(context,versionBean.getData().getDownload());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Response<String> response) {
            }
        });
    }
}
