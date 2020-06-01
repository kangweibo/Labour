package com.labour.lar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.labour.lar.activity.FindPwdActivity;
import com.labour.lar.activity.RegistActivity;
import com.labour.lar.permission.PermissionManager;

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
                startActivity(new Intent(this,MainActivity.class));
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
}
