package com.labour.lar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.activity.FindPwdActivity;
import com.labour.lar.activity.RegistActivity;
import com.labour.lar.cache.UserCache;
import com.labour.lar.module.User;
import com.labour.lar.permission.PermissionManager;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.StringUtils;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends AppCompatActivity {

    protected Unbinder unbinder;
    @BindView(R.id.phone_et)
    EditText phone_et;
    @BindView(R.id.password_et)
    EditText password_et;

    PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);

        permissionManager = PermissionManager.getInstance(this);
        permissionManager.checkPermissions();
    }

    @OnClick({R.id.login_btn,R.id.regist_btn,R.id.findpwd_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                login();
                break;
            case R.id.regist_btn:
                startActivity(new Intent(this, RegistActivity.class));
                break;
            case R.id.findpwd_btn:
                startActivity(new Intent(this, FindPwdActivity.class));
                break;
        }
    }

    /**
     * 重写onRequestPermissionsResult，用于接受请求结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将请求结果传递EasyPermission库处理
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //当从软件设置界面，返回当前程序时候
            case AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag("request_tag");
    }

    public void login() {
        String phone = phone_et.getText().toString();
        String password = password_et.getText().toString();
        if(StringUtils.isBlank(phone) || StringUtils.isBlank(password)){
            AppToast.show(this,"请填写完整信息！");
            return;
        }

        final Map<String,String> param = new HashMap<>();
        param.put("username",phone);
        param.put("passwd",password);
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/login";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONObject jo = jr.getData();
                    User ub = JSON.parseObject(JSON.toJSONString(jo), User.class);
                    UserCache userCache = UserCache.getInstance(LoginActivity.this);
                    userCache.put(ub);
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                } else {
                    AppToast.show(LoginActivity.this,jr.getMsg());
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(LoginActivity.this,"登录出错!");
            }
        });
    }

}
