package com.labour.lar.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.module.UserInfo;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.StringUtils;
import com.labour.lar.util.Utils;
import com.labour.lar.widget.BottomListDialog;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    @BindView(R.id.txt_prole)
    TextView txt_prole;
    @BindView(R.id.photo_iv)
    ImageView photo_iv;
    @BindView(R.id.take_photo_iv)
    ImageView take_photo_iv;

    private int type;// 0：项目；1：作业队；2：班组
    private int state;// 0：添加；1：修改
    private String classteam_id;
    private String operteam_id;
    private String project_id;

    private String employee_id;
    private String staff_id;
    private String manager_id;

    private File file1;
    private File file2;
    private static final int REQUEST_CODE_CAMERA = 102;

    private List<String> proles = new ArrayList<>();
    private Map<String, String> prolesMap = new HashMap<>();

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_manager_add;
    }

    @Override
    public void afterInitLayout() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        state = intent.getIntExtra("state", 0);
        classteam_id = intent.getStringExtra("classteam_id");
        operteam_id = intent.getStringExtra("operteam_id");
        project_id = intent.getStringExtra("project_id");
        employee_id = intent.getStringExtra("employee_id");
        staff_id = intent.getStringExtra("staff_id");
        manager_id = intent.getStringExtra("manager_id");

        String strState, strType;
        if (state == 0) {
            strState = "添加";
        } else {
            strState = "修改";
        }

        if (type == 0) {
            strType = "项目成员";
        } else if (type == 1){
            strType = "作业队成员";
        } else {
            strType = "班组成员";
        }

        title_tv.setText(strState + strType);

        initData();
    }

    @OnClick({R.id.back_iv,R.id.take_photo_iv,R.id.txt_prole,R.id.btn_submit})
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
            case R.id.txt_prole:
                showMoreDialog();
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
        if (state == 0) {
            addPerson();
        } else {
            updatePerson();
        }
    }

    private void initData() {
        UserInfo userInfo = UserInfoCache.getInstance(this).get();
        if (userInfo == null) {
            return;
        }

        String prole = userInfo.getProle();

        if (prole == null){
            return;
        }

        if (type == 0) {
            proles.add("项目经理");
            proles.add("项目定额员");
            proles.add("项目成员");

            prolesMap.put("项目经理", "project_manager");
            prolesMap.put("项目定额员", "project_quota");
            prolesMap.put("项目成员", "manager");
        } else if (type == 1){
            if (prole.equals("project_manager") || prole.equals("project_quota")){
                proles.add("作业队长");
                proles.add("作业队定额员");
            } else {
                proles.add("作业队成员");
            }

            prolesMap.put("作业队长", "operteam_manager");
            prolesMap.put("作业队定额员", "operteam_quota");
            prolesMap.put("作业队成员", "staff");
        } else {
            if (prole.equals("project_manager") || prole.equals("project_quota")){
                proles.add("班组长");
            } else {
                proles.add("工人");
            }

            prolesMap.put("班组长", "classteam_manager");
            prolesMap.put("工人", "employee");
        }
    }

    private void showMoreDialog(){
        BottomListDialog dialog = new BottomListDialog(this, "请选择角色", proles,
                new BottomListDialog.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String prole = proles.get(position);
                txt_prole.setText(prole);
            }
        });

        dialog.showAtLocation(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void addPerson() {
        String name = edt_name.getText().toString();
        String duty = edt_duty.getText().toString();
        String phone = edt_phone.getText().toString();
        String passwd = edt_password.getText().toString();
        String prole = txt_prole.getText().toString();
        String realProle = prolesMap.get(prole);

        if(StringUtils.isBlank(name) || StringUtils.isBlank(duty) || StringUtils.isBlank(phone)
                || StringUtils.isBlank(passwd) || StringUtils.isBlank(prole)){
            AppToast.show(this,"请填写完整人员信息！");
            return;
        }

        if(StringUtils.isBlank(realProle)){
            AppToast.show(this,"角色出错！");
            return;
        }

        final Map<String,String> param = new HashMap<>();
        param.put("token","063d91b4f57518ff");
        param.put("name",name);
        param.put("duty",duty);
        param.put("phone",phone);
        param.put("passwd",passwd);
        param.put("prole",realProle);

        String api;
        if (type == 0){
            api = "/api/manager_new";
            param.put("project_id",project_id);
        } else if (type == 1){
            api = "/api/staff_new";
            param.put("operteam_id",operteam_id);
        } else {
            api = "/api/employee_new";
            param.put("classteam_id",classteam_id);
        }

        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + api;
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

    private void updatePerson() {
        String name = edt_name.getText().toString();
        String duty = edt_duty.getText().toString();
        String phone = edt_phone.getText().toString();
        String passwd = edt_password.getText().toString();
        String prole = txt_prole.getText().toString();
        String realProle = prolesMap.get(prole);

        if(StringUtils.isBlank(name) || StringUtils.isBlank(duty) || StringUtils.isBlank(phone)
                || StringUtils.isBlank(passwd) || StringUtils.isBlank(prole)){
            AppToast.show(this,"请填写完整人员信息！");
            return;
        }

        if(StringUtils.isBlank(realProle)){
            AppToast.show(this,"角色出错！");
            return;
        }

        final Map<String,String> param = new HashMap<>();
        param.put("token","063d91b4f57518ff");
        param.put("name",name);
        param.put("duty",duty);
        param.put("phone",phone);
        param.put("passwd",passwd);
        param.put("prole",realProle);

        String api;
        if (type == 0){
            api = "/api/manager_update";
            param.put("project_id",project_id);
            param.put("manager_id",manager_id);
        } else if (type == 1){
            api = "/api/staff_update";
            param.put("operteam_id",operteam_id);
            param.put("staff_id",staff_id);
        } else {
            api = "/api/employee_update";
            param.put("classteam_id",classteam_id);
            param.put("employee_id",employee_id);
        }

        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + api;
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(ManagerAddActivity.this,"人员修改成功");
                    setResult(RESULT_OK, getIntent());
                    finish();
                } else {
                    AppToast.show(ManagerAddActivity.this,"人员修改失败");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(ManagerAddActivity.this,"人员修改出错!");
            }
        });
    }
}
