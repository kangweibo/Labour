package com.labour.lar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseFragment;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.MyinfoAdapter;

import com.labour.lar.module.UserInfo;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * 个人信息
 */
public class MyInfoFrag extends BaseFragment {

    @BindView(R.id.listView)
    ListView listView;

    private MyinfoAdapter myinfoAdapter;
    private List<MyinfoAdapter.ListItem> list = new ArrayList<>();

    private int userid;
    private String prole;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_myinfo;
    }

    @Override
    public void initView() {
        myinfoAdapter = new MyinfoAdapter(this.context);
        listView.setAdapter(myinfoAdapter);
        myinfoAdapter.setList(list);

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

        if (userInfo != null) {
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
        }

        myinfoAdapter.notifyDataSetChanged();
    }

    public void setUserInfo(int userid, String prole) {
        this.userid = userid;
        this.prole = prole;
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
}
