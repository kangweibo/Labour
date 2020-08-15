package com.labour.lar.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.EmployeeListAdapter;
import com.labour.lar.adapter.MemberAdapter;
import com.labour.lar.cache.UserCache;
import com.labour.lar.module.Classteam;
import com.labour.lar.module.Employee;
import com.labour.lar.module.Operteam;
import com.labour.lar.module.Project;
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
 * 成员管理 */
public class MemberManagerActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.right_header_btn)
    TextView right_header_btn;

    @BindView(R.id.list_refresh)
    SmartRefreshLayout list_refresh;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.loading_v)
    LoadingView loadingView;
    @BindView(R.id.noresult_view)
    TextView noresult_view;
    @BindView(R.id.txt_title)
    TextView txt_title;

    private MemberAdapter memberAdapter;

    private List<Employee> employeeList = new ArrayList<>();
    private List<MemberAdapter.ListItem> list = new ArrayList<>();;

    private String title;
    private int type;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_member_org;
    }

    @Override
    public void afterInitLayout() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        int id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");

        txt_title.setText(title);

        Drawable d = getResources().getDrawable(R.mipmap.jiahao);
        right_header_btn.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);

        getData(type, id);

        loadingView.setVisibility(View.GONE);
        noresult_view.setVisibility(View.GONE);
        memberAdapter = new MemberAdapter(this);
        listView.setAdapter(memberAdapter);

        list_refresh.setEnableRefresh(false);
        list_refresh.setEnableLoadMore(false);
        memberAdapter.setList(list);
        memberAdapter.notifyDataSetChanged();
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
     * 获取数据
     */
    private void getData(int type ,int id) {
        switch (type){
            case 0:
                title_tv.setText("项目成员管理-成员列表");
                getManagers(id);
                break;
            case 1:
                title_tv.setText("作业队成员管理-成员列表");
                getStaffs(id);
                break;
            case 2:
                title_tv.setText("班组成员管理-成员列表");
                getEmployees(id);
                break;
        }
    }

    private void getManagers(int projectId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",projectId);
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
                    AppToast.show(MemberManagerActivity.this,"获取成员信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                AppToast.show(MemberManagerActivity.this,"获取成员信息出错!");
            }
        });
    }

    private void getStaffs(int operteamId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",operteamId);
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
                    AppToast.show(MemberManagerActivity.this,"获取成员信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                AppToast.show(MemberManagerActivity.this,"获取成员信息出错!");
            }
        });
    }

    private void getEmployees(int classteamId) {
         JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",classteamId);
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
                    AppToast.show(MemberManagerActivity.this,"获取成员信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                AppToast.show(MemberManagerActivity.this,"获取成员信息出错!");
            }
        });
    }

    private void showEmployees() {
        list.clear();
        for(Employee employee : employeeList){
            MemberAdapter.ListItem item = new MemberAdapter.ListItem();
            item.name = employee.getName();;

            list.add(item);
        }

        memberAdapter.notifyDataSetChanged();
    }
}
