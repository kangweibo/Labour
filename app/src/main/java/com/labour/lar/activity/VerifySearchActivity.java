package com.labour.lar.activity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.VerifyAdapter;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.module.Employee;
import com.labour.lar.module.UserInfo;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.widget.LoadingView;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 审核
 */
public class VerifySearchActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.list_refresh)
    SmartRefreshLayout list_refresh;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.cb_id_card)
    CheckBox cb_id_card;
    @BindView(R.id.cb_bank_card)
    CheckBox cb_bank_card;
    @BindView(R.id.cb_exam)
    CheckBox cb_exam;
    @BindView(R.id.btn_ok)
    Button btn_ok;
    @BindView(R.id.btn_cancel)
    Button btn_cancel;
    @BindView(R.id.txt_select)
    TextView txt_select;
    @BindView(R.id.ly_select)
    View ly_select;

    private VerifyAdapter verifyAdapter;

    private List<Employee> verifyList = new ArrayList<>();

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_verify_search;
    }

    @Override
    public void afterInitLayout() {
        title_tv.setText("待审核员工查询");
        ly_select.setVisibility(View.GONE);
        getAuditusers();

        verifyAdapter = new VerifyAdapter(this);
        listView.setAdapter(verifyAdapter);

        list_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getAuditusers();
            }
        });

        list_refresh.setEnableLoadMore(false);

        verifyAdapter.setList(verifyList);
        verifyAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.back_iv,R.id.txt_select,R.id.btn_cancel,R.id.btn_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.txt_select:
                showSelectView();
                break;
            case R.id.btn_cancel:
                showSelectView();
                break;
            case R.id.btn_ok:
                search();
                break;
        }
    }

    public void showSelectView() {
        if (ly_select.getVisibility() == View.VISIBLE){
            ly_select.setVisibility(View.GONE);
        } else {
            ly_select.setVisibility(View.VISIBLE);
        }
    }

    public void search() {
        showSelectView();
        if (cb_id_card.isChecked()){

        }

        if (cb_exam.isChecked()){

        }

        if (cb_bank_card.isChecked()){

        }
    }

    /**
     * 获取数据
     */
    private void getAuditusers() {
        UserInfo userInfo = UserInfoCache.getInstance(this).get();
        if (userInfo == null) {
            return;
        }

        String prole = userInfo.getProle();

        if (prole == null){
            return;
        }

        int qpara = 0;

        JSONObject param = new JSONObject();
        param.put("token","063d91b4f57518ff");
        param.put("prole",prole);
        param.put("userid",userInfo.getId());
        param.put("qpara",qpara);
        String jsonParams = param.toJSONString();

        String url = Constants.HTTP_BASE + "/api/query_auditusers";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh();
                list_refresh.finishLoadMore();

                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Employee> list = JSON.parseArray(JSON.toJSONString(jsonArray), Employee.class);

                    verifyList.clear();
                    verifyList.addAll(list);
                    showData();
                } else {
                    AppToast.show(VerifySearchActivity.this,"获取人员信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                list_refresh.finishLoadMore(false);
                AppToast.show(VerifySearchActivity.this,"获取人员信息出错!");
            }
        });
    }

    private void showData() {
        verifyAdapter.notifyDataSetChanged();
    }
}
