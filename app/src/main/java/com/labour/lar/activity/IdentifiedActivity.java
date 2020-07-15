package com.labour.lar.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.cache.UserCache;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.module.User;
import com.labour.lar.module.UserInfo;
import com.labour.lar.ocr.BaiDuIDCardResult;
import com.labour.lar.ocr.BaiDuOCR;
import com.labour.lar.permission.PermissionManager;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.Base64Bitmap;
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

public class IdentifiedActivity extends BaseActivity implements PermissionManager.PermissionCallbacks {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;
    @BindView(R.id.right_header_btn)
    TextView right_header_btn;

    @BindView(R.id.photo_zheng_iv)
    ImageView photo_zheng_iv;
//    @BindView(R.id.zheng_tv)
//    TextView zheng_tv;

    @BindView(R.id.photo_fan_iv)
    ImageView photo_fan_iv;
//    @BindView(R.id.fan_tv)
//    TextView fan_tv;
    @BindView(R.id.photo_iv)
    ImageView photo_iv;
    @BindView(R.id.take_photo_fan_iv)
    ImageView take_photo_fan_iv;
    @BindView(R.id.take_photo_zheng_iv)
    ImageView take_photo_zheng_iv;
    @BindView(R.id.take_photo_iv)
    ImageView take_photo_iv;
    @BindView(R.id.edt_idcard_name)
    EditText edt_idcard_name;
    @BindView(R.id.edt_idcard_gender)
    EditText edt_idcard_gender;
    @BindView(R.id.edt_idcard_no)
    EditText edt_idcard_no;
    @BindView(R.id.edt_idcard_address)
    EditText edt_idcard_address;
    @BindView(R.id.txt_name)
    TextView txt_name;

    File file1;
    File file2;
    private static final int REQUEST_CODE_CAMERA = 102;
    PermissionManager permissionManager;
    private Map<String,String> idCardInfo = new HashMap<>();
    private User user;
    private boolean isOneself;//是否是自己认证

    private boolean identifiedFront;//正面认证
    private boolean identifiedBack;//反面认证
    private boolean isFaceMatch;//人脸与头像是否匹配

    private String bmpIdcardPhoto;
    private String bmpTakePhoto;
    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_identified;
    }

    @Override
    public void afterInitLayout() {
        permissionManager = PermissionManager.getInstance(this);
        permissionManager.setPermissionCallbacks(this);
        user = (User)getIntent().getSerializableExtra("user");

        if (user == null) {
            user = UserCache.getInstance(this).get();
            title_tv.setText("身份验证");
            isOneself = true;
        } else {
            title_tv.setText("代员工身份验证");
            txt_name.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(user.getName())) {
                txt_name.setText("员工：" + user.getName());
            }
        }
    }

//    /**
//     * 用明文ak，sk初始化
//     */
//    private void initAccessTokenWithAkSk() {
//        BaiDuOCR baiDuOCR = BaiDuOCR.getInstance(this);
//        baiDuOCR.setAutoCacheToken(true);
//        baiDuOCR.initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
//            @Override
//            public void onResult(AccessToken result) {
//            }
//            @Override
//            public void onError(OCRError error) {
//                error.printStackTrace();
//            }
//        }, getApplicationContext(),  "GNPUuASB8wKIVGkH2xHtQcl4", "GaE6gY29BaFkUvOrtcBMwj54Gi8Z63BT");
////        }, getApplicationContext(),  "ufsbEibCCMRZ8Ro6It3osFWw", "lMdcyLXMONMUpLFYDy2GqYaPMKqENDOD");
//    }

    @OnClick({R.id.back_iv,R.id.take_photo_fan_iv,R.id.take_photo_zheng_iv,R.id.take_photo_iv,R.id.submit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.take_photo_zheng_iv:
                file1 = Utils.getSaveFile(this,"idcard_"+new Date().getTime()+".png");
                Intent intent = new Intent(IdentifiedActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,file1.getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                break;
            case R.id.take_photo_fan_iv:
                file2 = Utils.getSaveFile(this,"idcard_"+new Date().getTime()+".png");
                intent = new Intent(IdentifiedActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,file2.getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                break;
            case R.id.take_photo_iv:
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent1.putExtra("android.intent.extras.CAMERA_FACING", 1);//前置摄像头
                startActivityForResult(intent1,1);
                break;
            case R.id.submit_btn:
                save();
                break;
        }
    }

    @Override
    public void onPause() {
        if(this.isFinishing()){
            BaiDuOCR.getInstance(this).release();
            OkGo.getInstance().cancelTag("request_tag");
        }
        super.onPause();
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

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
                        } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                            recIDCard(IDCardParams.ID_CARD_SIDE_BACK, file2.getAbsolutePath());
                        }
                    }
                }
            }
            if (requestCode == 1) {
                saveCameraImage(data);
            }
        }
    }

    private void recIDCard(final String idCardSide, String filePath) {
        Log.i("idcard recIDCard" , filePath);

        if(IDCardParams.ID_CARD_SIDE_FRONT.equals(idCardSide)){
            Glide.with(IdentifiedActivity.this).load(file1).into(photo_zheng_iv);
        } else if(IDCardParams.ID_CARD_SIDE_BACK.equals(idCardSide)){
            Glide.with(IdentifiedActivity.this).load(file2).into(photo_fan_iv);
        }

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
                    if(user != null) {
                        idCardInfo.put("userid", user.getId()+"");
                        idCardInfo.put("prole", user.getProle());
                    }

                    if(IDCardParams.ID_CARD_SIDE_FRONT.equals(idCardSide)){
                        String photo = result.getPhoto();
                        if(photo != null) {
                            photo_iv.setVisibility(View.VISIBLE);
                            Bitmap bitmap = Base64Bitmap.base64ToBitmap(photo);
                            photo_iv.setImageBitmap(bitmap);

                            idCardInfo.put("avatar","data:image/png;base64," + photo);
                            bmpIdcardPhoto = photo;

                            faceMatch();
                        }
                        edt_idcard_no.setText(result.getIdNumber().getWords());
                        idCardInfo.put("idcard",result.getIdNumber().getWords());
                        if(photo_zheng_iv.getDrawable() != null){
                            Bitmap bitmap1 =((BitmapDrawable)photo_zheng_iv.getDrawable()).getBitmap();
                            idCardInfo.put("idpic1","data:image/jpeg;base64," + Base64Bitmap.bitmapToBase64(bitmap1));
                        }

                        if (result.getName() != null) {
                            edt_idcard_name.setText(result.getName().getWords());
                            idCardInfo.put("name",result.getName().getWords());
                        }
                        if (result.getGender() != null) {
                            edt_idcard_gender.setText(result.getGender().getWords());
                            idCardInfo.put("gender",result.getGender().getWords());
                        }
                        if (result.getAddress() != null) {
                            edt_idcard_address.setText(result.getAddress().getWords());
                            idCardInfo.put("address",result.getAddress().getWords());
                        }
                        if (result.getEthnic() != null) {
                            idCardInfo.put("nation",result.getEthnic().getWords());
                        }
                        if (result.getBirthday() != null) {
                            idCardInfo.put("birthday",result.getBirthday().getWords());
                        }

                        if (result.getName() == null ||TextUtils.isEmpty(result.getName().getWords())
                                || result.getGender() == null || TextUtils.isEmpty(result.getGender().getWords())
                                || result.getIdNumber() == null || TextUtils.isEmpty(result.getIdNumber().getWords())
                                || result.getAddress() == null || TextUtils.isEmpty(result.getAddress().getWords())){
                            AppToast.show(IdentifiedActivity.this,"身份证正面识别失败，请重新拍摄");
                            photo_zheng_iv.setImageBitmap(null);
                            identifiedFront = false;
                        } else {
                            identifiedFront = true;
                        }
                    }
                    if(IDCardParams.ID_CARD_SIDE_BACK.equals(idCardSide)){
                        if(photo_fan_iv.getDrawable() != null) {
                            Bitmap bitmap2 = ((BitmapDrawable) photo_fan_iv.getDrawable()).getBitmap();
                            idCardInfo.put("idpic2","data:image/jpeg;base64," + Base64Bitmap.bitmapToBase64(bitmap2));

                            if (result.getSignDate() != null) {
                                idCardInfo.put("signDate", result.getSignDate().getWords());
                            }
                            if (result.getExpiryDate() != null) {
                                idCardInfo.put("expiryDate", result.getExpiryDate().getWords());
                            }
                            if (result.getIssueAuthority() != null) {
                                idCardInfo.put("issueAuthority", result.getIssueAuthority().getWords());
                            }
                        }

                        if (result.getSignDate() == null || TextUtils.isEmpty(result.getSignDate().getWords())
                                || result.getExpiryDate() == null || TextUtils.isEmpty(result.getExpiryDate().getWords())
                                || result.getIssueAuthority() == null || TextUtils.isEmpty(result.getIssueAuthority().getWords())){
                            AppToast.show(IdentifiedActivity.this,"身份证反面识别失败，请重新拍摄");
                            photo_fan_iv.setImageBitmap(null);
                            identifiedBack = false;
                        } else {
                            identifiedBack = true;
                        }
                    }
                    Log.i("idcard", result.toString());
                }
                dialog.dismiss();
            }

            @Override
            public void onError(OCRError error) {
                dialog.dismiss();
                AppToast.show(IdentifiedActivity.this,error.getMessage());
            }
        });
    }

    public void save() {
        if(user == null){
            AppToast.show(this,Constants.LOGIN_ERROR_TIP);
            return;
        }

        if(!identifiedFront){
            AppToast.show(this,"身份证正面照有误，请重新上传！");
            return;
        }

        if(!identifiedBack){
            AppToast.show(this,"身份证背面照有误，请重新上传！");
            return;
        }

        if(!isFaceMatch){
            AppToast.show(IdentifiedActivity.this,"身份证正面照与本人头像相似度过低!");
            return;
        }

        String idCardNo = edt_idcard_no.getText().toString();
        String name = edt_idcard_name.getText().toString();
        String gender = edt_idcard_gender.getText().toString();
        String address = edt_idcard_address.getText().toString();
        String avatar = idCardInfo.get("avatar");

        if(StringUtils.isBlank(idCardNo) || StringUtils.isBlank(name)
                || StringUtils.isBlank(gender)|| StringUtils.isBlank(address)){
            AppToast.show(this,"请填写完整信息！");
            return;
        }
        if(StringUtils.isBlank(avatar)){
            AppToast.show(this,"照片拍摄不正确！");
            return;
        }

        String jsonParams = JSON.toJSONString(idCardInfo);
        String url = Constants.HTTP_BASE + "/api/identify";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(IdentifiedActivity.this,"身份认证成功!");

                    if (isOneself){ //自己认证
                        JSONObject jo = jr.getData();
                        UserInfo userInfo = JSON.parseObject(JSON.toJSONString(jo), UserInfo.class);
                        updateUserInfo(userInfo);
                    }

                    setResult(RESULT_OK, getIntent());
                    finish();
                } else {
                    AppToast.show(IdentifiedActivity.this,"身份认证失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(IdentifiedActivity.this,"身份认证出错!");
            }
        });
    }

    // 更新用户信息
    private void updateUserInfo(UserInfo userInfo) {
        UserInfoCache userCache = UserInfoCache.getInstance(IdentifiedActivity.this);
        userCache.put(userInfo);
    }

    // 显示图片
    private void saveCameraImage(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bmp = (Bitmap) bundle.get("data");// 解析返回的图片成bitmap
            take_photo_iv.setImageBitmap(bmp);
            int maxSize = 102400;

            if (bmp != null) {
                Log.d("saveCameraImage", "getHeight: " + bmp.getHeight() + " getWidth: " + bmp.getWidth());
                Log.d("saveCameraImage", "saveCameraImage: " + bmp.getByteCount());

                int size = bmp.getByteCount();

                if (size > maxSize){
                    double scale = Math.sqrt(maxSize * 1.0 / size);

                    Matrix matrix = new Matrix();
                    matrix.setScale((float) scale, (float) scale);
                    bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                            bmp.getHeight(), matrix, true);
                }

                Log.d("saveCameraImage", "getHeight: " + bmp.getHeight() + " getWidth: " + bmp.getWidth());
                Log.d("saveCameraImage", "saveCameraImage: " + bmp.getByteCount());

                String photo = Base64Bitmap.bitmapToBase64(bmp);
                bmpTakePhoto = photo;

                faceMatch();
            }
        }
    }

    /**
     * 图片匹配
     */
    private void faceMatch() {
        if (TextUtils.isEmpty(bmpIdcardPhoto) || TextUtils.isEmpty(bmpTakePhoto)){
            return;
        }

        final Map<String,String> param = new HashMap<>();
        param.put("img1",bmpIdcardPhoto);
        param.put("img2",bmpTakePhoto);

        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/face_match2";

        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONObject jo = jr.getData();
                    JSONObject result =  jo.getJSONObject("result");

                    if (result != null) {
                        int score =  result.getInteger("score");

                        if (score > 70){
                            isFaceMatch = true;
                            AppToast.show(IdentifiedActivity.this,"头像匹配成功!");
                        } else {
                            isFaceMatch = false;
                            AppToast.show(IdentifiedActivity.this,"身份证正面照与本人头像相似度过低!");
                        }
                    }
                } else {
                    AppToast.show(IdentifiedActivity.this,jr.getMsg());
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(IdentifiedActivity.this,"照片匹配操作失败!");
            }
        });
    }
}
