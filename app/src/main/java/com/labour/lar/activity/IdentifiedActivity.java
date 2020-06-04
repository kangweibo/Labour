package com.labour.lar.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
import com.bumptech.glide.Glide;
import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import com.labour.lar.permission.PermissionManager;
import com.labour.lar.util.FileUtil;
import com.labour.lar.util.Utils;
import com.labour.lar.widget.toast.AppMsg;
import com.labour.lar.widget.toast.AppToast;

import java.io.File;

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
    @BindView(R.id.zheng_tv)
    TextView zheng_tv;
    @BindView(R.id.photo_fan_iv)
    ImageView photo_fan_iv;
    @BindView(R.id.fan_tv)
    TextView fan_tv;

    File file1;
    File file2;
    private static final int REQUEST_CODE_CAMERA = 102;
    PermissionManager permissionManager;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_identified;
    }

    @Override
    public void afterInitLayout() {
        permissionManager = PermissionManager.getInstance(this);
        permissionManager.setPermissionCallbacks(this);
        title_tv.setText("身份验证");
        file1 = FileUtil.getSaveFile(this,"identified1_temp.png");
        file2 = FileUtil.getSaveFile(this,"identified2_temp.png");

        initAccessTokenWithAkSk();
        //  初始化本地质量控制模型,释放代码在onDestory中
        //  调用身份证扫描必须加上 intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true); 关闭自动初始化和释放本地模型
        CameraNativeHelper.init(this, OCR.getInstance(this).getLicense(),
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
                        String msg;
                        switch (errorCode) {
                            case CameraView.NATIVE_SOLOAD_FAIL:
                                msg = "加载so失败，请确保apk中存在ui部分的so";
                                break;
                            case CameraView.NATIVE_AUTH_FAIL:
                                msg = "授权本地质量控制token获取失败";
                                break;
                            case CameraView.NATIVE_INIT_FAIL:
                                msg = "本地质量控制";
                                break;
                            default:
                                msg = String.valueOf(errorCode);
                        }
                        AppToast.show(IdentifiedActivity.this,msg);
//                        infoTextView.setText("本地质量控制初始化错误，错误原因： " + msg);
                    }
                });

    }
    /**
     * 用明文ak，sk初始化
     */
    private void initAccessTokenWithAkSk() {
        OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();

            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                //("AK，SK方式获取token失败", error.getMessage());
            }
        }, getApplicationContext(),  "ufsbEibCCMRZ8Ro6It3osFWw", "lMdcyLXMONMUpLFYDy2GqYaPMKqENDOD");
    }

    @OnClick({R.id.back_iv,R.id.photo_zheng_iv,R.id.photo_fan_iv,R.id.submit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.photo_zheng_iv:
                Log.i("idcard onClick" , file1.getAbsolutePath());

                Intent intent = new Intent(IdentifiedActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,file1.getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                break;
            case R.id.photo_fan_iv:
                intent = new Intent(IdentifiedActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,file2.getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                break;
        }
    }

    @Override
    public void onPause() {
        if(this.isFinishing()){
            CameraNativeHelper.release();
            OCR.getInstance(this).release();
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
        }
    }
    private Uri getUriForFile(Context context, File file) {
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= 24) {
            //参数：authority 需要和清单文件中配置的保持完全一致：${applicationId}.xxx
            fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        //不能直接调用contentprovider的接口函数，需要使用contentresolver对象，通过URI间接调用contentprovider
        if (cursor == null) {
            // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    private void recIDCard(String idCardSide, String filePath) {
        Log.i("idcard recIDCard" , filePath);
        if(CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(idCardSide)){
            Glide.with(IdentifiedActivity.this).load(file1).into(photo_zheng_iv);
        } else if(CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(idCardSide)){
            Glide.with(IdentifiedActivity.this).load(file2).into(photo_fan_iv);
        }
        Glide.with(IdentifiedActivity.this).load(file1).into(photo_fan_iv);

        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(20);

        OCR.getInstance(this).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    AppToast.show(IdentifiedActivity.this, result.toString());
                }
            }

            @Override
            public void onError(OCRError error) {
                AppToast.show(IdentifiedActivity.this,error.getMessage());
            }
        });
    }
}
