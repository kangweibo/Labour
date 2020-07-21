package com.labour.lar.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.labour.lar.module.UserInfo;
import com.labour.lar.ocr.BaiDuIDCardResult;
import com.labour.lar.ocr.BaiDuOCR;
import com.labour.lar.util.Base64Bitmap;
import com.labour.lar.util.Utils;
import com.labour.lar.widget.DialogUtil;
import com.labour.lar.widget.circledialog.CircleDialog;
import com.labour.lar.widget.circledialog.callback.ConfigButton;
import com.labour.lar.widget.circledialog.callback.ConfigDialog;
import com.labour.lar.widget.circledialog.params.ButtonParams;
import com.labour.lar.widget.circledialog.params.DialogParams;
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

import java.io.File;
import java.util.Date;
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
    @BindView(R.id.repassword_et)
    EditText repassword_et;
    @BindView(R.id.role_rg)
    RadioGroup role_rg;

    @BindView(R.id.verfiyCode_et)
    EditText verfiyCode_et;
    @BindView(R.id.getVerfyCode_tv)
    TextView getVerfyCode_tv;

    @BindView(R.id.login_btn)
    Button login_btn;
    @BindView(R.id.cb_agree)
    CheckBox cb_agree;
    @BindView(R.id.txt_agreement)
    TextView txt_agreement;

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

        cb_agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    login_btn.setEnabled(true);
                } else {
                    login_btn.setEnabled(false);
                }
            }
        });
//        showIdentifie(this);
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

    @OnClick({R.id.back_iv,R.id.login_btn,R.id.sex_et,R.id.getVerfyCode_tv,R.id.txt_agreement})
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
            case R.id.txt_agreement:
                startAgreement();
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
//        String name = name_et.getText().toString();
//        String sex = sex_et.getText().toString();
        String phone = phone_et.getText().toString();
        String password = password_et.getText().toString();
        String re_password = repassword_et.getText().toString();
        String role = checkedRole;

        if(StringUtils.isBlank(phone) || StringUtils.isBlank(password)){
            AppToast.show(this,"请填写完整信息！");
            return;
        }

        if(StringUtils.isBlank(re_password)){
            AppToast.show(this,"请再次输入密码！");
            return;
        }

        if(!password.equals(re_password)){
            AppToast.show(this,"两次输入密码不一致！");
            return;
        }

        final Map<String,String> param = new HashMap<>();
//        param.put("name",name);
//        param.put("gender",sex);
        param.put("passwd",password);
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
//                    JSONObject jo = jr.getData();
//                    User ub = JSON.parseObject(JSON.toJSONString(jo), User.class);
//                    UserCache userCache = UserCache.getInstance(RegistActivity.this);
//                    userCache.put(ub);
//                    startActivity(new Intent(RegistActivity.this,MainActivity.class));
                    AppToast.show(RegistActivity.this,"注册成功!");
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

    private void showIdentifie(Context context) {
        DialogUtil.showConfirmDialog(context,"提示信息","为了您方便注册，是否进行身份信息识别",new DialogUtil.OnDialogEvent<Void>(){
            @Override
            public void onPositiveButtonClick(Void t) {
                scanIdentifie();
            }
        });
    }

    private static final int REQUEST_CODE_CAMERA = 102;
    File file1;

    private void scanIdentifie() {
        file1 = Utils.getSaveFile(this,"idcard_"+new Date().getTime()+".png");
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,file1.getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == REQUEST_CODE_CAMERA) {
                if (data != null) {
                    String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                    if (!TextUtils.isEmpty(contentType)) {
                        if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                            recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, file1.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    private void recIDCard(final String idCardSide, String filePath) {
        final ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(20);
        param.setDetectDirection(true);

        BaiDuOCR.getInstance(this).recognizeIDCard(param, new OnResultListener<BaiDuIDCardResult>() {
            @Override
            public void onResult(BaiDuIDCardResult result) {
                if (result != null) {

                    if(IDCardParams.ID_CARD_SIDE_FRONT.equals(idCardSide)){
                        name_et.setText(result.getName().getWords());
                        sex_et.setText(result.getGender().getWords());
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onError(OCRError error) {
                dialog.dismiss();
                AppToast.show(RegistActivity.this,error.getMessage());
            }
        });
    }

    private void startAgreement() {
        Intent intent = new Intent(this, AgreementActivity.class);
        startActivity(intent);
    }
}
