package com.labour.lar.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseFragment;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.activity.GongRenDetailActivity;
import com.labour.lar.activity.ManagerAddActivity;
import com.labour.lar.adapter.EmployeeListAdapter;
import com.labour.lar.cache.UserCache;
import com.labour.lar.module.Classteam;
import com.labour.lar.module.Employee;
import com.labour.lar.module.User;
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
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * 班组
 */
public class BanZuDetailListFrag extends BaseFragment {

    @BindView(R.id.list_refresh)
    SmartRefreshLayout list_refresh;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.loading_v)
    LoadingView loadingView;
    @BindView(R.id.noresult_view)
    TextView noresult_view;

    private EmployeeListAdapter employeeListAdapter;
    private List<Employee> employeeList = new ArrayList<>();
    private List<EmployeeListAdapter.ListItem> list = new ArrayList<>();

    private Classteam classteam;
    private Employee employeeSelect;
    private BottomSelectDialog dialog;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_project_detail_list;
    }

    @Override
    public void initView() {
        loadingView.setVisibility(View.GONE);
        noresult_view.setVisibility(View.GONE);
        employeeListAdapter = new EmployeeListAdapter(getContext());
        listView.setAdapter(employeeListAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getEmployees();
            }
        });
        list_refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
        list_refresh.setEnableLoadMore(false);

        employeeListAdapter.setList(list);

        employeeListAdapter.setOnButtonClickListener(new EmployeeListAdapter.OnButtonClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Employee employee = employeeList.get(position);
                auditEmployee(employee);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Employee employee = employeeList.get(position);

                Intent intent = new Intent(context, GongRenDetailActivity.class);
                intent.putExtra("employee", employee);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                employeeSelect = employeeList.get(position);

                UserCache userCache = UserCache.getInstance(getContext());
                User user = userCache.get();
                if (user != null) {
                    String prole = user.getProle();
                    String personProle = employeeSelect.getProle();

                    if (prole != null && personProle != null){
                        if ((prole.equals("operteam_manager") || prole.equals("operteam_quota"))
                                && personProle.equals("classteam_manager")){
                            showMoreDialog();
                        } else if (prole.equals("classteam_manager")
                                && personProle.equals("employee")) {
                            showMoreDialog();
                        }
                    }
                }

                return true;
            }
        });

        getEmployees();
    }

    private void getEmployees() {
        if (classteam == null){
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",classteam.getId());
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/employees";
        ProgressDialog dialog = ProgressDialog.createDialog(this.getContext());
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
                    AppToast.show(getContext(),"获取成员信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                AppToast.show(getContext(),"获取成员信息出错!");
            }
        });
    }

    private void showEmployees() {
        list.clear();
        for(Employee employee : employeeList){
            EmployeeListAdapter.ListItem item = new EmployeeListAdapter.ListItem();
            item.field1 = employee.getName();;
            item.field1Content = "";
            item.field2 = "手机号：" + employee.getPhone();
            item.field2Content = "";
            item.isShowArraw = true;

            UserCache userCache = UserCache.getInstance(getContext());
            User user = userCache.get();

            if (user != null) {
                User.Classteam myClassteam = user.getClassteam();
                if (myClassteam != null){
                    int myClassteamId = myClassteam.getId();
                    int classteamId = classteam.getId();
                    String prole = user.getProle();

                    // 本班组组长
                    if (prole != null && prole.equals("classteam_manager")
                            && myClassteamId == classteamId){
                        if (employee.getStatus() == null || !employee.getStatus().equals("已审核")) {
                            item.isShowPass = true;
                        }
                    }
                }
            }

            list.add(item);
        }

        employeeListAdapter.notifyDataSetChanged();
    }

    /**
     * 设置班组
     * @param classteam
     */
    public void setClassteam(Classteam classteam) {
        this.classteam = classteam;
    }

    private void showMoreDialog(){
        dialog = new BottomSelectDialog(getActivity(),new BottomSelectDialog.BottomSelectDialogListener() {
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
                        if(id == R.id.txt_update){
                            updatePerson(employeeSelect);
                        } else if(id == R.id.txt_delete){
                            delete(employeeSelect);
                        } else if(id == R.id.txt_see){
                            callPhone(employeeSelect);
                        }

                        dialog.dismiss();
                    }
                };

                TextView txt_see = view.findViewById(R.id.txt_see);
                TextView txt_update = view.findViewById(R.id.txt_update);
                TextView txt_delete = view.findViewById(R.id.txt_delete);
                TextView txt_cancel = view.findViewById(R.id.txt_cancel);
                txt_see.setText("拨打电话");
                txt_update.setText("更新成员");
                txt_delete.setText("删除成员");

                txt_see.setOnClickListener(onClickListener);
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

    public void addPerson() {
        Intent intent = new Intent(context, ManagerAddActivity.class);
        intent.putExtra("type", 2);
        intent.putExtra("state", 0);
        intent.putExtra("classteam_id", classteam.getId()+"");
        startActivityForResult(intent, Constants.RELOAD);
    }

    public void updatePerson(Employee person) {
        if (person == null){
            return;
        }

        String employee_id = person.getId()+"";
        Intent intent = new Intent(context, ManagerAddActivity.class);
        intent.putExtra("type", 2);
        intent.putExtra("state", 1);
        intent.putExtra("classteam_id", classteam.getId()+"");
        intent.putExtra("employee_id", employee_id);
        startActivityForResult(intent, Constants.RELOAD);
    }

    private void delete(Employee person) {
        DialogUtil.showConfirmDialog(getContext(),"提示信息","确认删除成员吗？",new DialogUtil.OnDialogEvent<Void>(){
            @Override
            public void onPositiveButtonClick(Void t) {
                deletePerson(person);
            }
        });
    }

    public void deletePerson(Employee person) {
        if (person == null){
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token","063d91b4f57518ff");
        jsonObject.put("employee_id",person.getId());
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/employee_delete";
        ProgressDialog dialog = ProgressDialog.createDialog(getActivity());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(getActivity(),"删除班组成员成功!");
                    getEmployees();
                } else {
                    AppToast.show(getActivity(),"删除班组成员失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getActivity(),"删除班组成员出错!");
            }
        });
    }

    // 审核
    private void auditEmployee(Employee person) {
        if (person == null){
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token","063d91b4f57518ff");
        jsonObject.put("rtype","employee");
        jsonObject.put("employeeid",person.getId());
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/employee_audit";
        ProgressDialog dialog = ProgressDialog.createDialog(getActivity());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(getActivity(),"班组成员审核通过!");
                    getEmployees();
                } else {
                    JSONObject jo = jr.getJsonObj();
                    String errmsg = jo.getString("errmsg");
                    AppToast.show(getActivity(),errmsg);
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getActivity(),"班组成员审核出错!");
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 重新加载数据
        if (requestCode == Constants.RELOAD && resultCode == RESULT_OK) {
            getEmployees();
        }
    }

    private void callPhone(Employee person){
        if (person == null){
            return;
        }
        String phoneNum = person.getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
}
