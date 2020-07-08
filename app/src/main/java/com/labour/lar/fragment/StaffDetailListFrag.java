package com.labour.lar.fragment;

import android.app.Dialog;
import android.content.Intent;
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
import com.labour.lar.adapter.ProjectDetailListAdapter;
import com.labour.lar.adapter.ProjectListItemWarp;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.module.Employee;
import com.labour.lar.module.Operteam;
import com.labour.lar.module.UserInfo;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.widget.BottomSelectDialog;
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
 * 队部
 */
public class StaffDetailListFrag extends BaseFragment {

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

    private Operteam operteam;
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
        projectAdapter = new ProjectDetailListAdapter(getContext());
        listView.setAdapter(projectAdapter);
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

        projectAdapter.setList(list);

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

                UserInfo userInfo = UserInfoCache.getInstance(getContext()).get();
                if (userInfo != null) {
                    String prole = userInfo.getProle();
                    String personProle = employeeSelect.getProle();

                    if (prole != null && personProle != null){
                        if ((prole.equals("project_manager") || prole.equals("project_quota"))
                            && (personProle.equals("operteam_manager") || personProle.equals("operteam_quota"))){
                            showMoreDialog();
                        } else if ((prole.equals("operteam_manager") || prole.equals("operteam_quota"))
                                && personProle.equals("staff")) {
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
        if (operteam == null){
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",operteam.getId());
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/staffs";
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
            ProjectListItemWarp.ListItem item = new ProjectListItemWarp.ListItem();
            item.field1 = employee.getName();;
            item.field1Content = "";
            item.field2 = "状态：" + employee.getStatus();
            item.field2Content = "";
            item.isShowArraw = true;

            list.add(item);
        }

        projectAdapter.notifyDataSetChanged();
    }

    /**
     * 设置作业队
     * @param operteam
     */
    public void setOperteam(Operteam operteam) {
        this.operteam = operteam;
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
                            deletePerson(employeeSelect);
                        }

                        dialog.dismiss();
                    }
                };

                TextView txt_see = view.findViewById(R.id.txt_see);
                TextView txt_update = view.findViewById(R.id.txt_update);
                TextView txt_delete = view.findViewById(R.id.txt_delete);
                TextView txt_cancel = view.findViewById(R.id.txt_cancel);
                txt_see.setVisibility(View.GONE);
                txt_update.setText("更新作业队成员");
                txt_delete.setText("删除作业队成员");

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
        intent.putExtra("type", 1);
        intent.putExtra("state", 0);
        intent.putExtra("operteam_id", operteam.getId()+"");
        startActivityForResult(intent, Constants.RELOAD);
    }

    public void updatePerson(Employee person) {
        if (person == null){
            return;
        }

        String staff_id = person.getId()+"";
        Intent intent = new Intent(context, ManagerAddActivity.class);
        intent.putExtra("type", 1);
        intent.putExtra("state", 1);
        intent.putExtra("operteam_id", operteam.getId()+"");
        intent.putExtra("staff_id", staff_id);
        startActivityForResult(intent, Constants.RELOAD);
    }

    public void deletePerson(Employee person) {
        if (person == null){
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token","063d91b4f57518ff");
        jsonObject.put("staff_id",person.getId());
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/staff_delete";
        ProgressDialog dialog = ProgressDialog.createDialog(getActivity());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(getActivity(),"删除作业队成员成功!");
                    getEmployees();
                } else {
                    AppToast.show(getActivity(),"删除作业队成员失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getActivity(),"删除作业队成员出错!");
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 重新加载数据
        if (requestCode == Constants.RELOAD && resultCode == RESULT_OK) {
            getEmployees();
        }
    }
}
