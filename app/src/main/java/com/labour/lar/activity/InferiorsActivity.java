package com.labour.lar.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.ProjectDetailListAdapter;
import com.labour.lar.adapter.ProjectListItemWarp;
import com.labour.lar.cache.UserCache;
import com.labour.lar.module.Employee;
import com.labour.lar.module.User;
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
 * 下属人员
 */
public class InferiorsActivity extends BaseActivity {

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

    ProjectDetailListAdapter projectAdapter;
    private List<Employee> employeeList = new ArrayList<>();
    private List<ProjectListItemWarp.ListItem> list = new ArrayList<>();

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_inferiors;
    }

    @Override
    public void afterInitLayout() {
        title_tv.setText("选择员工");

        loadingView.setVisibility(View.GONE);
        noresult_view.setVisibility(View.GONE);
        projectAdapter = new ProjectDetailListAdapter(this);
        listView.setAdapter(projectAdapter);

        list_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getInferiors();
            }
        });
        list_refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
        list_refresh.setEnableLoadMore(false);

        projectAdapter.setList(list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Employee employee = employeeList.get(position);

                Intent intent = getIntent();
                intent.putExtra("employee", employee);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        getInferiors();
    }

    @OnClick({R.id.back_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
        }
    }

    /**
     * 获取下属人员
     */
    private void getInferiors() {
        UserCache userCache = UserCache.getInstance(this);
        User user = userCache.get();

        String prole = user.getProle();

        if (prole != null){
            if (prole.equals("project_manager") || prole.equals("project_quota")){
                if (user.getProject() != null) {
                    getManagers(user.getProject().getId());
                }
            } if (prole.equals("operteam_manager") || prole.equals("operteam_quota")){
                if (user.getOperteam() != null) {
                    getStaffs(user.getOperteam().getId());
                }
            } else if (prole.equals("classteam_manager")) {
                if (user.getClassteam() != null) {
                    getEmployees(user.getClassteam().getId());
                }
            }
        }
    }

    private void getManagers(int id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/managers";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    list_refresh.finishRefresh(true);
                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Employee> classetams = JSON.parseArray(JSON.toJSONString(jsonArray), Employee.class);

                    employeeList.clear();
                    employeeList.addAll(classetams);
                    showEmployees();
                } else {
                    list_refresh.finishRefresh(false);
                    AppToast.show(InferiorsActivity.this,"获取成员信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                AppToast.show(InferiorsActivity.this,"获取成员信息出错!");
            }
        });
    }

    private void getStaffs(int id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/staffs";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    list_refresh.finishRefresh(true);
                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Employee> classetams = JSON.parseArray(JSON.toJSONString(jsonArray), Employee.class);

                    employeeList.clear();
                    employeeList.addAll(classetams);
                    showEmployees();
                } else {
                    list_refresh.finishRefresh(false);
                    AppToast.show(InferiorsActivity.this,"获取成员信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                AppToast.show(InferiorsActivity.this,"获取成员信息出错!");
            }
        });
    }

    private void getEmployees(int id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/employees";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    list_refresh.finishRefresh(true);
                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Employee> classetams = JSON.parseArray(JSON.toJSONString(jsonArray), Employee.class);

                    employeeList.clear();
                    employeeList.addAll(classetams);
                    showEmployees();
                } else {
                    list_refresh.finishRefresh(false);
                    AppToast.show(InferiorsActivity.this,"获取成员信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                AppToast.show(InferiorsActivity.this,"获取成员信息出错!");
            }
        });
    }

    private void showEmployees() {
        list.clear();
        for(Employee employee : employeeList){
            ProjectListItemWarp.ListItem item = new ProjectListItemWarp.ListItem();
            item.field1 = employee.getName();;
            item.field1Content = "";
            item.field2 = "手机号：" + employee.getPhone();
            item.field2Content = "";
            item.isShowArraw = true;

            list.add(item);
        }

        projectAdapter.notifyDataSetChanged();
    }
}
