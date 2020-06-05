package com.labour.lar.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.circledialog.CircleDialog;
import com.circledialog.callback.ConfigButton;
import com.circledialog.callback.ConfigDialog;
import com.circledialog.params.ButtonParams;
import com.circledialog.params.DialogParams;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.MainActivity;
import com.labour.lar.R;
import com.labour.lar.cache.UserCache;
import com.labour.lar.module.User;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.MCountDownTimer;
import com.labour.lar.util.StringUtils;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RegistActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;
    @BindView(R.id.name_et)
    EditText name_et;
    @BindView(R.id.sex_et)
    TextView sex_et;
    @BindView(R.id.phone_et)
    EditText phone_et;
    @BindView(R.id.password_et)
    EditText password_et;
    @BindView(R.id.role_rg)
    RadioGroup role_rg;

    @BindView(R.id.verfiyCode_et)
    EditText verfiyCode_et;
    @BindView(R.id.getVerfyCode_tv)
    TextView getVerfyCode_tv;

    private String checkedRole = "manager";

    //暂时没用
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
        role_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.xiangmubu_rb){
                    checkedRole = "manager";
                } else if(checkedId == R.id.zuoyedui_rb){
                    checkedRole = "staff";
                } else if(checkedId == R.id.gongren_rb){
                    checkedRole = "empoyee";
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
        OkGo.getInstance().cancelTag("request_tag");
    }

    @OnClick({R.id.back_iv,R.id.login_btn,R.id.sex_et,R.id.getVerfyCode_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.login_btn:
                save();
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
            case R.id.sex_et:
                showSexDialog();
                break;
        }
    }
    private void showSexDialog(){
        final String[] items = {"男", "女"};
        new CircleDialog.Builder(this)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        //增加弹出动画
                        params.animStyle = R.style.dialogWindowAnim;
                    }
                }).setItems(items, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sex_et.setText(items[position]);
            }
        }).setNegative("取消", null)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        //取消按钮字体颜色
                        params.textColor = Color.RED;
                    }
                }).show();
    }

    public void save() {
        String name = name_et.getText().toString();
        String sex = sex_et.getText().toString();
        String phone = phone_et.getText().toString();
        String password = password_et.getText().toString();
        String role = checkedRole;
        if(StringUtils.isBlank(name) || StringUtils.isBlank(sex) || StringUtils.isBlank(phone) || StringUtils.isBlank(password)){
            AppToast.show(this,"请填写完整信息！");
            return;
        }

        final Map<String,String> param = new HashMap<>();
        param.put("name",name);
        param.put("passwd",password);
        param.put("gender",sex);
        param.put("phone",phone);
        param.put("role",role);
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/register";
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
                    UserCache userCache = new UserCache(RegistActivity.this);
                    userCache.put(ub);
                    startActivity(new Intent(RegistActivity.this,MainActivity.class));
                    finish();
                } else {
                    AppToast.show(RegistActivity.this,jr.getMsg());
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(RegistActivity.this,"提交出错!");
            }
        });
    }

}
