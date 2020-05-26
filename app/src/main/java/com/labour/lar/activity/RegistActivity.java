package com.labour.lar.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.labour.lar.BaseActivity;
import com.labour.lar.MainActivity;
import com.labour.lar.R;
import com.labour.lar.util.MCountDownTimer;

import butterknife.BindView;
import butterknife.OnClick;

public class RegistActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;

    @BindView(R.id.phone_et)
    EditText phone_et;
    @BindView(R.id.password_et)
    EditText password_et;
    @BindView(R.id.verfiyCode_et)
    EditText verfiyCode_et;

    @BindView(R.id.getVerfyCode_tv)
    TextView getVerfyCode_tv;

    private boolean isGetVerfyCodeClicked = false;
    private MCountDownTimer countDownTimer;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_regist;
    }
    @Override
    public void afterInitLayout(){
        title_tv.setText("注册");

        this.countDownTimer = new MCountDownTimer();
        this.countDownTimer.setOnCountDownListener(new MCountDownTimer.OnCountDownListener() {
            @Override
            public void onStop() {
                isGetVerfyCodeClicked = false;
                if(getVerfyCode_tv != null){
                    getVerfyCode_tv.setText("获取验证码");
                }

            }

            @Override
            public void onTick(long second) {
                if(getVerfyCode_tv != null) {
                    String time = "发送中(%s)";
                    time = String.format(time, second);
                    getVerfyCode_tv.setText(time);
                }
            }
        });
    }

    public void cancelCountDownTimer(){
        isGetVerfyCodeClicked = false;
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelCountDownTimer();
    }

    @OnClick({R.id.back_iv,R.id.login_btn,R.id.getVerfyCode_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.login_btn:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.getVerfyCode_tv:
                if(isGetVerfyCodeClicked){
                    return;
                }

                isGetVerfyCodeClicked = true;
                if(countDownTimer != null){
                    countDownTimer.cancel();
                    countDownTimer.start();
                }

                break;
        }
    }
}
