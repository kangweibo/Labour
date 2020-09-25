package com.labour.lar.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseFragment;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.MyinfoAdapter;

import com.labour.lar.cache.UserCache;
import com.labour.lar.module.User;
import com.labour.lar.module.UserInfo;
import com.labour.lar.permission.PermissionManager;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.Base64Bitmap;
import com.labour.lar.util.Utils;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.circledialog.callback.ConfigButton;
import com.labour.lar.widget.circledialog.callback.ConfigDialog;
import com.labour.lar.widget.circledialog.params.ButtonParams;
import com.labour.lar.widget.circledialog.params.DialogParams;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 个人信息
 */
public class MyInfoFrag extends BaseFragment implements PermissionManager.PermissionCallbacks {

    @BindView(R.id.listView)
    ListView listView;

    private MyinfoAdapter myinfoAdapter;
    private List<MyinfoAdapter.ListItem> list = new ArrayList<>();

    private boolean isMyself;
    private int userid;
    private String prole;

    public static final int REQUEST_CODE_TAKE_PHOTO = 1;
    public static final int REQUEST_CODE_SELECT_IMAGE = 2;
    private PermissionManager permissionManager;
    private File photoFile;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_myinfo;
    }

    @Override
    public void initView() {
        permissionManager = PermissionManager.getInstance(getActivity());
        permissionManager.setPermissionCallbacks(this);
        photoFile =new File(Utils.getTakePhotoPath(),"temp.png");

        myinfoAdapter = new MyinfoAdapter(this.context);
        listView.setAdapter(myinfoAdapter);
        myinfoAdapter.setList(list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isMyself && position == 0) {
                    showPhotoDialog();
                }
            }
        });
        getUserInfo(userid, prole);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void showUserInfo(UserInfo userInfo){
        if (userInfo == null) {
            return;
        }

        MyinfoAdapter.ListItem item0 = new MyinfoAdapter.ListItem();
        item0.type = "头像";
        item0.pic = userInfo.getPic();
        item0.showPic = true;

        MyinfoAdapter.ListItem item1 = new MyinfoAdapter.ListItem();
        item1.type = "姓名";
        item1.value = userInfo.getName();

        MyinfoAdapter.ListItem item2 = new MyinfoAdapter.ListItem();
        item2.type = "性别";
        item2.value = userInfo.getGender();

        MyinfoAdapter.ListItem item3 = new MyinfoAdapter.ListItem();
        item3.type = "电话";
        item3.value = userInfo.getPhone();

        MyinfoAdapter.ListItem item4 = new MyinfoAdapter.ListItem();
        item4.type = "民族";
        item4.value = userInfo.getNation();

        MyinfoAdapter.ListItem item5 = new MyinfoAdapter.ListItem();
        item5.type = "生日";
        item5.value = userInfo.getBirthday();

        MyinfoAdapter.ListItem item6 = new MyinfoAdapter.ListItem();
        item6.type = "身份证";
        item6.value = userInfo.getIdcard();

        MyinfoAdapter.ListItem item7 = new MyinfoAdapter.ListItem();
        item7.type = "银行卡";
        item7.value = userInfo.getBankcard();

        MyinfoAdapter.ListItem item8 = new MyinfoAdapter.ListItem();
        item8.type = "开户行";
        item8.value = userInfo.getBank();

        MyinfoAdapter.ListItem item9 = new MyinfoAdapter.ListItem();
        item9.type = "住址";
        item9.value = userInfo.getAddress();

        list.clear();
        list.add(item0);
        list.add(item1);
        list.add(item2);
        list.add(item3);
        list.add(item4);
        list.add(item5);
        list.add(item6);
        list.add(item7);
        list.add(item8);
        list.add(item9);

        UserInfo.Project project = userInfo.getProject();
        UserInfo.Operteam operteam = userInfo.getOperteam();
        UserInfo.Classteam classteam = userInfo.getClassteam();

        if (operteam != null) {
            project = operteam.getProject();
        }

        if (classteam != null) {
            operteam = classteam.getOperteam();
            if (operteam != null) {
                project = operteam.getProject();
            }
        }

        if (project != null){
            MyinfoAdapter.ListItem item10 = new MyinfoAdapter.ListItem();
            item10.type = "所属项目";
            item10.value = project.getName();
            list.add(item10);
        }

        if (operteam != null) {
            MyinfoAdapter.ListItem item10 = new MyinfoAdapter.ListItem();
            item10.type = "所属作业队";
            item10.value = operteam.getName();
            list.add(item10);
        }

        if (classteam != null) {
            MyinfoAdapter.ListItem item10 = new MyinfoAdapter.ListItem();
            item10.type = "所属班组";
            item10.value = classteam.getName();
            list.add(item10);
        }

        MyinfoAdapter.ListItem item11 = new MyinfoAdapter.ListItem();
        item11.type = "员工状态";
        item11.value = userInfo.getStatus();

        MyinfoAdapter.ListItem item12 = new MyinfoAdapter.ListItem();
        item12.type = "上岗日期";
        item12.value = userInfo.getAuditdate();

        MyinfoAdapter.ListItem item13 = new MyinfoAdapter.ListItem();
        item13.type = "累计工时";
        item13.value = userInfo.getTotalworkday()+"人天";

        MyinfoAdapter.ListItem item14 = new MyinfoAdapter.ListItem();
        item14.type = "发放总额";
        item14.value = userInfo.getTotalsalary()+"元";

        MyinfoAdapter.ListItem item15 = new MyinfoAdapter.ListItem();
        item15.type = "职责";
        item15.value = userInfo.getDuty();

        list.add(item11);
        list.add(item12);
        list.add(item13);
        list.add(item14);
        list.add(item15);

        myinfoAdapter.notifyDataSetChanged();
    }

    public void setUserInfo(int userid, String prole,boolean isMyself) {
        this.userid = userid;
        this.prole = prole;
        this.isMyself = isMyself;
    }

    private void getUserInfo(int userid, String prole) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token","063d91b4f57518ff");
        jsonObject.put("dtype", prole);
        jsonObject.put("userid", userid);
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/user";
        ProgressDialog dialog = ProgressDialog.createDialog(getContext());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONObject jo = jr.getData();
                    UserInfo userInfoOrg = JSON.parseObject(JSON.toJSONString(jo), UserInfo.class);
                    UserInfo userInfo = dealWithPic(userInfoOrg);
                    showUserInfo(userInfo);
                } else {
                    AppToast.show(getContext(),"获取用户信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getContext(),"获取用户信息出错!");
            }
        });
    }

    private UserInfo dealWithPic(UserInfo userInfo) {
        JSONObject jsonObject = JSON.parseObject(userInfo.getPic());
        String pic = jsonObject.getString("url");
        userInfo.setPic(pic);

        jsonObject = JSON.parseObject(userInfo.getIdpic1());
        String Idpic1 = jsonObject.getString("url");
        userInfo.setIdpic1(Idpic1);

        jsonObject = JSON.parseObject(userInfo.getIdpic2());
        String Idpic2 = jsonObject.getString("url");
        userInfo.setIdpic2(Idpic2);

        return userInfo;
    }

    private void showPhotoDialog() {
        final String[] items = {"拍照","从相册选择"};
        new com.labour.lar.widget.circledialog.CircleDialog.Builder(getActivity())
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
            Uri fileUri = getUriForFile(getContext(), photoFile);
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

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
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
        Luban.with(getContext())
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
                        updateFacepic(file);
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

    private void updateFacepic(File file) {
        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
        String base64Data = Base64Bitmap.bitmapToBase64(bmp);

        if (TextUtils.isEmpty(base64Data)){
            return;
        }
        base64Data = "data:image/png;base64," + base64Data;

        User user = UserCache.getInstance(getContext()).get();
        JSONObject param = new JSONObject();
        param.put("token","063d91b4f57518ff");
        param.put("userid",user.getId());
        param.put("prole",user.getProle());
        param.put("avatar",base64Data);
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/update_facepic";

        ProgressDialog dialog = ProgressDialog.createDialog(context);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(context,"头像上传成功!");
                    // 重新获取用户信息
                    getUserInfo(userid, prole);
                } else {
                    AppToast.show(context,jr.getMsg());
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(context,"操作失败!");
            }
        });
    }
}
