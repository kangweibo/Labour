package com.labour.lar.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
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
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.event.BaseEvent;
import com.labour.lar.event.EventManager;
import com.labour.lar.module.UserInfo;
import com.labour.lar.widget.NoScrollGridView;
import com.labour.lar.widget.RoundImageView;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.encode.CodeCreator;

import butterknife.BindView;
import butterknife.OnClick;

public class MineFrag extends BaseFragment {

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
                    addClassteam();
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

    private void addClassteam() {
        UserInfo userInfo = UserInfoCache.getInstance(getContext()).get();
        if (userInfo == null) {
            return;
        }

        String prole = userInfo.getProle();
        showQRCode();

        if (prole.equals("")){
            showQRCode();
        } else if (prole.equals("1")) {
            showScan();
        }
    }

    int REQUEST_CODE = 123;
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
}
