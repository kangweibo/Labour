package com.labour.lar.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
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
import com.labour.lar.adapter.MemberAdapter;
import com.labour.lar.module.Employee;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.widget.BottomSelectDialog;
import com.labour.lar.widget.DialogUtil;
import com.labour.lar.widget.LoadingView;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<MemberAdapter.ListItem> list = new ArrayList<>();

    private int id;
    private int type;// 企业：0；项目：1；作业队：2；班组 3；
    private String title;

    private BottomSelectDialog dialog;
    private Employee memberSelect;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_member_org;
    }

    @Override
    public void afterInitLayout() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");

        txt_title.setText(title);

        Drawable d = getResources().getDrawable(R.mipmap.jiahao);
        right_header_btn.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);

        getData();

        loadingView.setVisibility(View.GONE);
        noresult_view.setVisibility(View.GONE);
        memberAdapter = new MemberAdapter(this);
        listView.setAdapter(memberAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Employee employee = employeeList.get(position);
                updateMember(employee, type, MemberManagerActivity.this.id);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                memberSelect = employeeList.get(position);
                showMoreDialog();
                return true;
            }
        });

        list_refresh.setEnableRefresh(false);
        list_refresh.setEnableLoadMore(false);
        memberAdapter.setList(list);
        memberAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.back_iv,R.id.right_header_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.right_header_btn:
                addMember(type, id);
                break;
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        switch (type){
            case 1:
                title_tv.setText("项目成员管理-成员列表");
                getManagers(id);
                break;
            case 2:
                title_tv.setText("作业队成员管理-成员列表");
                getStaffs(id);
                break;
            case 3:
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
            item.role = employee.getProlename();;

            list.add(item);
        }

        memberAdapter.notifyDataSetChanged();
    }

    private void addMember(int type ,int id) {
        Intent intent = new Intent(this, MemberAddActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("id", id+"");
        intent.putExtra("state", 0);
        intent.putExtra("title", title);
        startActivityForResult(intent, Constants.RELOAD);
    }

    private void updateMember(Employee person,int type ,int id) {
        if (person == null){
            return;
        }

        Intent intent = new Intent(this, MemberAddActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("id", id+"");
        intent.putExtra("member", person);
        intent.putExtra("state", 1);
        intent.putExtra("title", title);
        startActivityForResult(intent, Constants.RELOAD);
    }

    private void showMoreDialog(){
        dialog = new BottomSelectDialog(this, new BottomSelectDialog.BottomSelectDialogListener() {
            @Override
            public int getLayout() {
                return R.layout.menu_fence;
            }
            @Override
            public void initView(View view) {
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = v.getId();
                        if(id == R.id.txt_delete){
                            tryDeleteMember(memberSelect);
                        }

                        dialog.dismiss();
                    }
                };

                TextView txt_see = view.findViewById(R.id.txt_see);
                TextView txt_update = view.findViewById(R.id.txt_update);
                TextView txt_delete = view.findViewById(R.id.txt_delete);
                TextView txt_cancel = view.findViewById(R.id.txt_cancel);
                txt_see.setVisibility(View.GONE);
                txt_update.setVisibility(View.GONE);
                txt_delete.setText("删除成员");

                txt_update.setOnClickListener(onClickListener);
                txt_delete.setOnClickListener(onClickListener);
                txt_cancel.setOnClickListener(onClickListener);
            }
            @Override
            public void onClick(Dialog dialog, int rate) {

            }
        });

        dialog.showAtLocation(mRootView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void tryDeleteMember(Employee person) {
        DialogUtil.showConfirmDialog(this,"提示信息","确定删除本成员吗？",new DialogUtil.OnDialogEvent<Void>(){
            @Override
            public void onPositiveButtonClick(Void t) {
                deleteMember(person);
            }
        });
    }

    private void deleteMember(Employee person) {
        if (person == null) {
            return;
        }
        String member_id = person.getId()+"";

        final Map<String,String> param = new HashMap<>();
        param.put("token","063d91b4f57518ff");

        String api;
        if (type == 0 || type == 1){
            api = "/api/manager_delete";
            param.put("manager_id",member_id);
        } else if (type == 2){
            api = "/api/staff_delete";
            param.put("staff_id",member_id);
        } else {
            api = "/api/employee_delete";
            param.put("employee_id",member_id);
        }

        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + api;
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(MemberManagerActivity.this,"人员删除成功");
                    getData();
                } else {
                    AppToast.show(MemberManagerActivity.this,"人员删除失败");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(MemberManagerActivity.this,"人员删除出错!");
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 重新加载数据
        if (requestCode == Constants.RELOAD && resultCode == RESULT_OK) {
            getData();
        }
    }
}
