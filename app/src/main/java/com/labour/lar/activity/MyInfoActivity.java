package com.labour.lar.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
//import com.labour.lar.widget.circledialog.CircleDialog;
import com.labour.lar.BaseApplication;
import com.labour.lar.Constants;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.module.UserInfo;
import com.labour.lar.widget.circledialog.callback.ConfigButton;
import com.labour.lar.widget.circledialog.callback.ConfigDialog;
import com.labour.lar.widget.circledialog.params.ButtonParams;
import com.labour.lar.widget.circledialog.params.DialogParams;
import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import com.labour.lar.permission.PermissionManager;
import com.labour.lar.util.Utils;
import com.labour.lar.widget.RoundImageView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 个人信息
 */
public class MyInfoActivity extends BaseActivity implements PermissionManager.PermissionCallbacks {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;
    @BindView(R.id.right_header_btn)
    TextView right_header_btn;

    @BindView(R.id.photo_iv)
    RoundImageView photo_iv;
    @BindView(R.id.name_et)
    EditText name_et;
    @BindView(R.id.sex_et)
    TextView sex_et;
    @BindView(R.id.phone_et)
    TextView phone_et;
    @BindView(R.id.nation_et)
    TextView nation_et;
    @BindView(R.id.birthday_et)
    TextView birthday_et;
    @BindView(R.id.address_et)
    TextView address_et;

    public static final int REQUEST_CODE_TAKE_PHOTO = 1;
    public static final int REQUEST_CODE_SELECT_IMAGE = 2;

    PermissionManager permissionManager;
    private File photoFile;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_myinfo;
    }

    @Override
    public void afterInitLayout() {
        permissionManager = PermissionManager.getInstance(this);
        permissionManager.setPermissionCallbacks(this);
        photoFile =new File(Utils.getTakePhotoPath(),"temp.png");

        title_tv.setText("个人信息");
        showUserInfo();
    }

    @OnClick({R.id.back_iv,R.id.photo_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.photo_ll:
                //showPhotoDialog();
                break;
        }
    }

    private void showPhotoDialog() {
        final String[] items = {"拍照","从相册选择"};
        new com.labour.lar.widget.circledialog.CircleDialog.Builder(this)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        //增加弹出动画
                        params.animStyle = R.style.dialogWindowAnim;
                    }
                }).setItems(items, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    permissionManager.checkPermissions(PermissionManager.takePhotoPerms,REQUEST_CODE_TAKE_PHOTO);
                } else if(position == 1){
                    permissionManager.checkPermissions(PermissionManager.selectImagesPerms,REQUEST_CODE_SELECT_IMAGE);
                }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        if(requestCode == REQUEST_CODE_TAKE_PHOTO){
            //设置图片的保存位置(兼容Android7.0)
            Uri fileUri = getUriForFile(this, photoFile);
            //创建打开本地相机的意图对象
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //指定图片保存位置
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            //开启意图
            startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);

        } else if(requestCode == REQUEST_CODE_SELECT_IMAGE){
            // 激活系统图库，选择一张图片
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
                ArrayList<String> picurls = new ArrayList<>();
                picurls.add(photoFile.getAbsolutePath());
                compressionPicture(picurls, requestCode);
            } else if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
                Uri uri = data.getData(); //获取图片uri
                ArrayList<String> picurls = new ArrayList<>();
                String imagePath = getRealPathFromURI(uri); // 根据URI找到图片真正的路径
                picurls.add(imagePath);
                compressionPicture(picurls,requestCode);
            }
        }
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
    private void compressionPicture(final ArrayList<String> picurls,final int requestCode) {
        final ArrayList<String> filelists = new ArrayList<>();
        Luban.with(this)
                .load(picurls)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                // .setTargetDir(getPath())                     // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        //  压缩开始前调用，可以在方法内启动 loading UI
                        //filelists.clear();
                        Log.i("compressionPicture", "onStart: 压缩开始");
                    }

                    @Override
                    public void onSuccess(File file) {
                        //  压缩成功后调用，返回压缩后的图片文件
                        Log.i("compressionPicture", "onSuccess: 压缩成功");
                        filelists.add(file.getAbsolutePath());
                        //压缩完成
                        if (filelists.size() == picurls.size()) {
                            for(String path : filelists){
                                //filesPresenter.uploadFile(path,null,requestCode);
                            }
                        }
//                        String url = Constants.IMAGE_HTTP_BASE + imgUrl;

                        String url = file.getAbsolutePath();
                        Log.i("idcard" , url);
                        Glide.with(MyInfoActivity.this).load(url).into(photo_iv);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  当压缩过程出现问题时调用
                        Log.i("compressionPicture", "onError: 图片压缩失败");
                    }
                }).launch();    //启动压缩
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

    /**
     * 将拍的照片添加到相册
     * @param uri 拍的照片的Uri
     */
    private void galleryAddPic(Uri uri){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        sendBroadcast(mediaScanIntent);
    }

    private void showUserInfo(){
        UserInfo userInfo = UserInfoCache.getInstance(this).get();
        if (userInfo == null) {
            return;
        }

        if (!TextUtils.isEmpty(userInfo.getPic())) {
            Glide.with(BaseApplication.getInstance()).load(Constants.HTTP_BASE + userInfo.getPic()).into(photo_iv);
        }

        name_et.setText(userInfo.getName());
        phone_et.setText(userInfo.getPhone());
        nation_et.setText(userInfo.getNation());
        birthday_et.setText(userInfo.getBirthday());
        address_et.setText(userInfo.getAddress());
    }
}
