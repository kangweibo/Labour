package com.labour.lar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.labour.lar.activity.FindPwdActivity;
import com.labour.lar.activity.RegistActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity {

    protected Unbinder unbinder;
    @BindView(R.id.phone_et)
    EditText phone_et;
    @BindView(R.id.password_et)
    EditText password_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
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
}
