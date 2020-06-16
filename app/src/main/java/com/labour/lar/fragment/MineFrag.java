package com.labour.lar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.labour.lar.BaseApplication;
import com.labour.lar.BaseFragment;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.activity.IdentifiedActivity;
import com.labour.lar.activity.MyInfoActivity;
import com.labour.lar.activity.SettingActivity;
import com.labour.lar.adapter.MineGridViewAdapter;
import com.labour.lar.cache.UserCache;
import com.labour.lar.cache.UserInfoCache;
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
                } else if(position == 4){
                    startActivity(new Intent(context, SettingActivity.class));
                }
            }
        });

//        getUserInfo();
    }
    @OnClick({R.id.identified_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.identified_tv:
                startActivity(new Intent(context, IdentifiedActivity.class));
                break;
        }
    }

//    private void getUserInfo() {
//        UserCache userCache = UserCache.getInstance(getContext());
//        User user = userCache.get();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("token","063d91b4f57518ff");
//        jsonObject.put("dtype", user.getProle());
//        jsonObject.put("userid", user.getId());
//        String jsonParams =jsonObject.toJSONString();
//
//        String url = Constants.HTTP_BASE + "/api/user";
//        ProgressDialog dialog = ProgressDialog.createDialog(this.getContext());
//        dialog.show();
//
//        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
//            @Override
//            public void onSuccess(Response<String> response) {
//                dialog.dismiss();
//                AjaxResult jr = new AjaxResult(response.body());
//                if(jr.getSuccess() == 1){
//                    JSONObject jo = jr.getData();
//                    UserInfo userInfoOrg = JSON.parseObject(JSON.toJSONString(jo), UserInfo.class);
//                    UserInfo userInfo = dealWithPic(userInfoOrg);
//                    UserInfoCache.getInstance(getContext()).put(userInfo);
//                    refreshUserInfo();
//                } else {
//                    AppToast.show(getContext(),"获取用户信息失败!");
//                }
//            }
//            @Override
//            public void onError(Response<String> response) {
//                dialog.dismiss();
//                AppToast.show(getContext(),"获取用户信息出错!");
//            }
//        });
//    }
//
//    private UserInfo dealWithPic(UserInfo userInfo) {
//        JSONObject jsonObject = JSON.parseObject(userInfo.getPic());
//        String pic = jsonObject.getString("url");
//        userInfo.setPic(pic);
//
//        jsonObject = JSON.parseObject(userInfo.getIdpic1());
//        String Idpic1 = jsonObject.getString("url");
//        userInfo.setIdpic1(Idpic1);
//
//        jsonObject = JSON.parseObject(userInfo.getIdpic2());
//        String Idpic2 = jsonObject.getString("url");
//        userInfo.setIdpic2(Idpic2);
//
//        return userInfo;
//    }

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
}
