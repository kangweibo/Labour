package com.labour.lar.activity;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.SalaryListAdapter;
import com.labour.lar.adapter.TrainLearn1Adapter;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.module.Question1;
import com.labour.lar.module.Salary;
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
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 薪酬列表
 */
public class SalaryListActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.list_refresh)
    SmartRefreshLayout list_refresh;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.loading_v)
    LoadingView loadingView;
    @BindView(R.id.noresult_view)
    TextView noresult_view;

    private SalaryListAdapter salaryListAdapter;

    private List<Salary> salaryList = new ArrayList<>();


    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_train_learn;
    }

    @Override
    public void afterInitLayout() {
        title_tv.setText("薪酬发放记录");
        getUserSalaries();

        loadingView.setVisibility(View.GONE);
        noresult_view.setVisibility(View.GONE);
        salaryListAdapter = new SalaryListAdapter(this);
        listView.setAdapter(salaryListAdapter);

        list_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getUserSalaries();
            }
        });
        list_refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

            }
        });

        salaryListAdapter.setList(salaryList);
        salaryListAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.back_iv,})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
        }
    }

    /**
     * 获取数据
     */
    private void getUserSalaries() {
        UserInfo userInfo = UserInfoCache.getInstance(this).get();
        if (userInfo == null) {
            return;
        }

        String prole = userInfo.getProle();

        if (prole == null){
            return;
        }

        JSONObject param = new JSONObject();
        param.put("token","063d91b4f57518ff");
        param.put("prole",prole);
        param.put("userid",userInfo.getId());
        String jsonParams = param.toJSONString();

        String url = Constants.HTTP_BASE + "/api/get_user_salaries";
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
                    List<Salary> list = JSON.parseArray(JSON.toJSONString(jsonArray), Salary.class);


                    salaryList.clear();
                    salaryList.addAll(list);
                    showData();
                } else {
                    AppToast.show(SalaryListActivity.this,"获取考试信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                list_refresh.finishLoadMore(false);
                AppToast.show(SalaryListActivity.this,"获取考试信息出错!");
            }
        });
    }

    private void showData() {
        salaryListAdapter.notifyDataSetChanged();
    }
}
