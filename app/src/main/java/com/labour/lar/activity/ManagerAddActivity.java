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
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
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
public class ManagerAddActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_duty)
    EditText edt_duty;
    @BindView(R.id.edt_phone)
    EditText edt_phone;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.edt_prole)
    EditText edt_prole;
    @BindView(R.id.photo_iv)
    ImageView photo_iv;
    @BindView(R.id.take_photo_iv)
    ImageView take_photo_iv;

    private int type;// 0：添加；1：更新
    private String classteam_id;
    private String operteam_id;
    private String project_id;

    private File file1;
    private File file2;
    private static final int REQUEST_CODE_CAMERA = 102;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_manager_add;
    }

    @Override
    public void afterInitLayout() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        classteam_id = intent.getStringExtra("classteam_id");
        operteam_id = intent.getStringExtra("operteam_id");
        project_id = intent.getStringExtra("project_id");

        if (type == 0) {
            title_tv.setText("添加成员");
        } else {
            title_tv.setText("添加成员");
        }
    }

    @OnClick({R.id.back_iv,R.id.take_photo_iv,R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.take_photo_iv:
                file1 = Utils.getSaveFile(this,"idcard_"+new Date().getTime()+".png");
                Intent intent = new Intent(this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,file1.getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                break;
            case R.id.photo_fan_iv:
                file2 = Utils.getSaveFile(this,"bankcard_"+new Date().getTime()+".png");
                intent = new Intent(this, CameraActivity.class);
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
                        if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                            recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, file1.getAbsolutePath());
                        }
//                        else if (CameraActivity.CONTENT_TYPE_BANK_CARD.equals(contentType)) {
//                            recBankCard(file2.getAbsolutePath());
//                        }
                    }
                }
            }
        }
    }

    private void recIDCard(final String idCardSide, String filePath) {
        Log.i("idcard recIDCard", filePath);

        if (IDCardParams.ID_CARD_SIDE_FRONT.equals(idCardSide)) {
            Glide.with(this).load(file1).into(photo_iv);
        }
    }

//    private void recBankCard(String filePath) {
//        Log.i("idcard recIDCard", filePath);
//
////        if (IDCardParams.ID_CARD_SIDE_FRONT.equals(idCardSide)) {
////            Glide.with(this).load(file1).into(photo_zheng_iv);
////        } else if (IDCardParams.ID_CARD_SIDE_BACK.equals(idCardSide)) {
////            Glide.with(this).load(file2).into(photo_fan_iv);
////        }
//        final ProgressDialog dialog = ProgressDialog.createDialog(this);
//        dialog.show();
//
//        BankCardParams param = new BankCardParams();
//        param.setImageFile(new File(filePath));
//
//        BaiDuOCR.getInstance(this).recognizeBankCard(param, new OnResultListener<BankCardResult>() {
//            @Override
//            public void onResult(BankCardResult result) {
//                if (result != null) {
//                    String cardNumber = result.getBankCardNumber();
//                    String cardName = result.getBankName();
//
//                    Log.i("idcard", result.toString());
//                }
//                dialog.dismiss();
//            }
//
//            @Override
//            public void onError(OCRError error) {
//                dialog.dismiss();
//                AppToast.show(ManagerAddActivity.this,error.getMessage());
//            }
//        });
//    }

    private void submit() {
        if (type == 0){
            addPerson();
        }
    }

    private void addPerson() {
        String name = edt_name.getText().toString();
        String duty = edt_duty.getText().toString();
        String phone = edt_phone.getText().toString();
        String passwd = edt_password.getText().toString();
        String prole = edt_prole.getText().toString();

        if(StringUtils.isBlank(name) || StringUtils.isBlank(duty) || StringUtils.isBlank(phone)
                || StringUtils.isBlank(passwd) || StringUtils.isBlank(prole)){
            AppToast.show(this,"请填写完整人员信息！");
            return;
        }

        if(StringUtils.isBlank(operteam_id)){
            AppToast.show(this,"作业队id为空！");
            return;
        }

        final Map<String,String> param = new HashMap<>();
        param.put("token","063d91b4f57518ff");
        param.put("project_id",project_id);
        param.put("name",name);
        param.put("duty",duty);
        param.put("phone",phone);
        param.put("passwd",passwd);
        param.put("prole",prole);
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/manager_new";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(ManagerAddActivity.this,"人员录入成功");
                    setResult(RESULT_OK, getIntent());
                    finish();
                } else {
                    AppToast.show(ManagerAddActivity.this,"人员录入失败");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(ManagerAddActivity.this,"人员录入出错!");
            }
        });
    }
}
