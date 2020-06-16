package com.labour.lar.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.labour.lar.BaseActivity;
import com.labour.lar.BaseApplication;
import com.labour.lar.LoginActivity;
import com.labour.lar.MainActivity;
import com.labour.lar.R;
import com.labour.lar.WelcomeActivity;
import com.labour.lar.cache.GlideCacheUtil;
import com.labour.lar.cache.UserCache;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.module.User;
import com.labour.lar.module.UserInfo;
import com.labour.lar.util.MCountDownTimer;
import com.labour.lar.widget.toast.AppToast;

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
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.txt_clear:
                clear();
                break;
            case R.id.btn_quit:
                quit();
                break;
        }
    }

    public void clear(){
        GlideCacheUtil.getInstance().clearImageAllCache(this);
        AppToast.show(this,"缓存清理完成!");
    }

    // 退出登录
    public void quit(){
        UserCache userCache = UserCache.getInstance(this);
        userCache.clear();
        UserInfoCache userInfoCache = UserInfoCache.getInstance(this);
        userInfoCache.clear();

        BaseApplication.getInstance().setRelogon(true);
        startActivity(new Intent(this, LoginActivity.class));
    }
}
