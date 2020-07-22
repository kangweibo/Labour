package com.labour.lar.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseActivity;
import com.labour.lar.BaseApplication;
import com.labour.lar.Constants;
import com.labour.lar.LoginActivity;
import com.labour.lar.MainActivity;
import com.labour.lar.R;
import com.labour.lar.WelcomeActivity;
import com.labour.lar.cache.GlideCacheUtil;
import com.labour.lar.cache.UserCache;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.module.User;
import com.labour.lar.module.UserInfo;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.MCountDownTimer;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;

    @BindView(R.id.txt_modify)
    TextView txt_modify;
    @BindView(R.id.txt_clear)
    TextView txt_clear;
    @BindView(R.id.btn_quit)
    Button btn_quit;


    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_setting;
    }
    @Override
    public void afterInitLayout(){
        title_tv.setText("设置");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.back_iv,R.id.txt_modify,R.id.txt_clear,R.id.btn_quit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.txt_modify:
                startActivity(new Intent(this, PasswordActivity.class));
                break;
            case R.id.txt_clear:
                clear();
                break;
            case R.id.btn_quit:
                logout();
                break;
        }
    }

    public void clear(){
        GlideCacheUtil.getInstance().clearImageAllCache(this);
        AppToast.show(this,"缓存清理完成!");
    }

    // 退出登录
    public void logout(){
        UserCache userCache = UserCache.getInstance(this);
        User user = userCache.get();
        JSONObject jsonObject = new JSONObject();

        String prole = user.getProle();
        String rtype = "employee";

        if (prole != null){
            if (prole.equals("project_manager") || prole.equals("project_quota") || prole.equals("manager")){
                rtype = "manager";
            }

            if (prole.equals("operteam_manager") || prole.equals("operteam_quota") || prole.equals("staff")){
                rtype = "staff";
            }

            if (prole.equals("classteam_manager") || prole.equals("employee")){
                rtype = "employee";
            }
        }

        jsonObject.put("rtype", rtype);
        jsonObject.put("userid", user.getId());
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/logout";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(SettingActivity.this,"退出登录成功!");
                    quit();
                } else {
                    AppToast.show(SettingActivity.this,"退出登录失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(SettingActivity.this,"退出登录出错!");
            }
        });
    }

    // 退出登录
    private void quit(){
        UserCache userCache = UserCache.getInstance(this);
        userCache.clear();
        UserInfoCache userInfoCache = UserInfoCache.getInstance(this);
        userInfoCache.clear();

        BaseApplication.getInstance().setRelogon(true);
        startActivity(new Intent(this, LoginActivity.class));
    }
}
