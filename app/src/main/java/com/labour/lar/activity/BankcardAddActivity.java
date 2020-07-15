package com.labour.lar.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.BankCardParams;
import com.baidu.ocr.sdk.model.BankCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.cache.UserCache;
import com.labour.lar.module.User;
import com.labour.lar.ocr.BaiDuOCR;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.StringUtils;
import com.labour.lar.util.Utils;
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

/**
 * 添加
 */
public class BankcardAddActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.edt_bankcard_num)
    EditText edt_bankcard_num;
    @BindView(R.id.edt_bankname)
    EditText edt_bankname;
    @BindView(R.id.photo_iv)
    ImageView photo_iv;
    @BindView(R.id.txt_name)
    TextView txt_name;

    private File file2;
    private static final int REQUEST_CODE_CAMERA = 102;

    private User user;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_bankcard;
    }

    @Override
    public void afterInitLayout() {
        user = (User)getIntent().getSerializableExtra("user");

        if (user == null) {
            user = UserCache.getInstance(this).get();
            title_tv.setText("添加银行卡");
        } else {
            title_tv.setText("代员工添加银行卡");
            txt_name.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(user.getName())) {
                txt_name.setText("员工：" + user.getName());
            }
        }
    }

    @OnClick({R.id.back_iv,R.id.take_photo_iv,R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.take_photo_iv:
                file2 = Utils.getSaveFile(this,"bankcard_"+new Date().getTime()+".png");
                Intent intent = new Intent(this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,file2.getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_BANK_CARD);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == REQUEST_CODE_CAMERA) {
                if (data != null) {
                    String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                    if (!TextUtils.isEmpty(contentType)) {
                        if (CameraActivity.CONTENT_TYPE_BANK_CARD.equals(contentType)) {
                            recBankCard(file2.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    private void recBankCard(String filePath) {
        Log.i("bankcard recBankCard", filePath);
        Glide.with(this).load(file2).into(photo_iv);

        final ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        BankCardParams param = new BankCardParams();
        param.setImageFile(new File(filePath));

        BaiDuOCR.getInstance(this).recognizeBankCard(param, new OnResultListener<BankCardResult>() {
            @Override
            public void onResult(BankCardResult result) {
                if (result != null) {
                    String cardNumber = result.getBankCardNumber();
                    String cardName = result.getBankName();

                    edt_bankcard_num.setText(cardNumber);
                    edt_bankname.setText(cardName);
                    Log.i("idcard", result.toString());
                }
                dialog.dismiss();
            }

            @Override
            public void onError(OCRError error) {
                dialog.dismiss();
                AppToast.show(BankcardAddActivity.this,error.getMessage());
            }
        });
    }

    private void submit() {
        String bankcard_num = edt_bankcard_num.getText().toString();
        String bankname = edt_bankname.getText().toString();

        if(StringUtils.isBlank(bankcard_num)){
            AppToast.show(this,"请填写银行卡号码");
            return;
        }

        if(StringUtils.isBlank(bankname)){
            AppToast.show(this,"请填写银行卡开户行");
            return;
        }

        if (user == null) {
            AppToast.show(this,"用户信息出错");
           return;
        }

        final Map<String,String> param = new HashMap<>();

        param.put("id",user.getId()+"");
        param.put("prole","063d91b4f57518ff");
        param.put("bankcard_num",bankcard_num);
        param.put("bankname",bankname);
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/user_bankinfo_update";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(BankcardAddActivity.this,"银行卡信息提交成功");
                    setResult(RESULT_OK, getIntent());
                    finish();
                } else {
                    AppToast.show(BankcardAddActivity.this,"银行卡信息提交失败");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(BankcardAddActivity.this,"银行卡信息提交出错!");
            }
        });
    }
}
