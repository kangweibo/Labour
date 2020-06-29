package com.labour.lar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.labour.lar.BaseApplication;
import com.labour.lar.BaseFragment;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.activity.IdentifiedActivity;
import com.labour.lar.activity.MyInfoActivity;
import com.labour.lar.activity.SettingActivity;
import com.labour.lar.activity.ShowQRCodeActivity;
import com.labour.lar.adapter.MineGridViewAdapter;
import com.labour.lar.cache.UserCache;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.event.BaseEvent;
import com.labour.lar.event.EventManager;
import com.labour.lar.module.User;
import com.labour.lar.module.UserInfo;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.widget.NoScrollGridView;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.RoundImageView;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class MineFrag extends BaseFragment {

    private int REQUEST_CODE = 123;

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;
    @BindView(R.id.right_header_btn)
    TextView right_header_btn;
    @BindView(R.id.identified_tv)
    TextView identified_tv;
    @BindView(R.id.name_tv)
    TextView name_tv;
    @BindView(R.id.brief_tv)
    TextView brief_tv;

    @BindView(R.id.main_gridview)
    NoScrollGridView main_gridview;

    @BindView(R.id.photo_iv)
    RoundImageView photo_iv;

    MineGridViewAdapter mineGridViewAdapter;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_mine;
    }

    @Override
    public void initView() {
        identified_tv.setVisibility(View.GONE);
        back_iv.setVisibility(View.INVISIBLE);
        title_tv.setText("我的");

        refreshUserInfo();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mineGridViewAdapter = new MineGridViewAdapter(context);
        main_gridview.setAdapter(mineGridViewAdapter);
        main_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    startActivity(new Intent(context, MyInfoActivity.class));
                } else if(position == 1){
                    showProjectFrag();
                } else if(position == 3){
                    showClassteam();
                } else if(position == 4){
                    startActivity(new Intent(context, SettingActivity.class));
                }
            }
        });
    }
    @OnClick({R.id.identified_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.identified_tv:
                startActivity(new Intent(context, IdentifiedActivity.class));
                break;
        }
    }

    // 刷新用户界面
    private void refreshUserInfo() {
        UserInfo userInfo = UserInfoCache.getInstance(getContext()).get();
        if (userInfo == null) {
            return;
        }

        if (!TextUtils.isEmpty(userInfo.getPic())) {
            Glide.with(BaseApplication.getInstance()).load(Constants.HTTP_BASE + userInfo.getPic()).into(photo_iv);
        }

        if (!userInfo.isIdentified()){
            identified_tv.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(userInfo.getName())){
            name_tv.setText(userInfo.getName());
        }

        if (!TextUtils.isEmpty(userInfo.getPhone())){
            brief_tv.setText(userInfo.getPhone());
        }
    }

    private void showProjectFrag() {
        BaseEvent event = new BaseEvent();
        event.setCode(1);
        event.setPosition(0);
        EventManager.post(event);
    }

    private void showMessageFrag() {
        BaseEvent event = new BaseEvent();
        event.setCode(1);
        event.setPosition(1);
        EventManager.post(event);
    }

    private void showClassteam() {
        UserInfo userInfo = UserInfoCache.getInstance(getContext()).get();
        if (userInfo == null) {
            return;
        }

        String prole = userInfo.getProle();

        if (prole.equals("classteam_manager")){
            showQRCode();
        } else if (prole.equals("employee")) {
            showScan();
        }
    }


    private void showScan() {
        Intent intent = new Intent(getContext(), CaptureActivity.class);
//        ZxingConfig config = new ZxingConfig();
//        config.setPlayBeep(true);//是否播放扫描声音 默认为true
//        config.setShake(true);//是否震动  默认为true
//        config.setDecodeBarCode(true);//是否扫描条形码 默认为true
//        config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
//        config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
//        config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
//        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
//        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void showQRCode() {
        String content = "123456";
        Intent intent = new Intent(getContext(), ShowQRCodeActivity.class);
        intent.putExtra("content", content);
        startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                AppToast.show(getContext(),"扫描结果为：" + content);
            }
        }
    }

    // 工人加入班组
    private void addClassteam(String classteam_id) {
        UserCache userCache = UserCache.getInstance(getContext());
        User user = userCache.get();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("prole",user.getProle());
        jsonObject.put("userid",user.getId());
        jsonObject.put("classteam_id",classteam_id);

        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/join_classteam";
        ProgressDialog dialog = ProgressDialog.createDialog(this.getContext());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){

                    AppToast.show(getContext(),"加入班组成功!");
                } else {
                    AppToast.show(getContext(),"加入班组失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getContext(),"加入班组出错!");
            }
        });
    }
}
