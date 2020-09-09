package com.labour.lar.activity;

import android.support.v4.app.FragmentTransaction;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.fragment.PartyContentDetailsFrag;
import com.labour.lar.module.Party;
import com.labour.lar.module.Safety;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

/**
 * 项目
 */
public class PartyContentDetailActivity extends BaseActivity {

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_project_detail;
    }

    @Override
    public void afterInitLayout() {
        int id = getIntent().getIntExtra("id", 0);
        int type = getIntent().getIntExtra("type", 0);

        if (type == 0) {
            getParty(id);
        } else {
            getSafety(id);
        }
    }

    private void getParty(int id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token","063d91b4f57518ff");
        jsonObject.put("id",id);

        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/party";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONObject jsonData = jr.getData();
                    Party party = JSON.parseObject(JSON.toJSONString(jsonData), Party.class);
                    showHtml("党建内容", party.getContent(), party.isIsvideo());
                } else {
                    AppToast.show(PartyContentDetailActivity.this,"获取信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(PartyContentDetailActivity.this,"获取信息出错!");
            }
        });
    }

    private void getSafety(int id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token","063d91b4f57518ff");
        jsonObject.put("id",id);

        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/safety";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONObject jsonData = jr.getData();
                    Safety safety = JSON.parseObject(JSON.toJSONString(jsonData), Safety.class);
                    showHtml(safety.getSafetype(), safety.getContent(), safety.isIsvideo());
                } else {
                    AppToast.show(PartyContentDetailActivity.this,"获取信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(PartyContentDetailActivity.this,"获取信息出错!");
            }
        });
    }

    private void showHtml(String title, String detailHtml, boolean isvideo) {
        PartyContentDetailsFrag partyContentDetailsFrag = new PartyContentDetailsFrag();
        partyContentDetailsFrag.setTitle(title);
        partyContentDetailsFrag.setHtml(detailHtml);
        partyContentDetailsFrag.setIsvideo(isvideo);

        FragmentTransaction trs = fm.beginTransaction();
        trs.add(R.id.container,partyContentDetailsFrag);
        trs.commit();
    }
}
