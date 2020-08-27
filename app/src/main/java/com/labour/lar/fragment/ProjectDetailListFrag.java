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
import com.labour.lar.activity.ManagerDetailActivity;
import com.labour.lar.activity.TaskTeamAddActivity;
import com.labour.lar.activity.TaskTeamDetailActivity;
import com.labour.lar.adapter.ProjectDetailListAdapter;
import com.labour.lar.adapter.ProjectListItemWarp;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.module.Manager;
import com.labour.lar.module.Operteam;
import com.labour.lar.module.Project;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

public class ProjectDetailListFrag extends BaseFragment {

    @BindView(R.id.list_refresh)
    SmartRefreshLayout list_refresh;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.loading_v)
    LoadingView loadingView;
    @BindView(R.id.noresult_view)
    TextView noresult_view;

    private ProjectDetailListAdapter projectAdapter;
    private Project project;
    private List<Manager> managerList = new ArrayList<>();
    private List<Operteam> operteamList = new ArrayList<>();
    private List<ProjectListItemWarp.ListItem> list = new ArrayList<>();;
    private Operteam operteamSelect;

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
                //getManagers();
                getOperteam();
            }
        });
        list_refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
        //list_refresh.setEnableRefresh(false);
        list_refresh.setEnableLoadMore(false);

        projectAdapter.setList(list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(context, ManagerDetailActivity.class);
                    intent.putExtra("project", project);
                    startActivity(intent);
                } else{
                    Operteam operteam = operteamList.get(position-1);
                    Intent intent = new Intent(context, TaskTeamDetailActivity.class);
                    intent.putExtra("operteam", operteam);
                    startActivity(intent);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {
                    operteamSelect = operteamList.get(position-1);
                    UserInfo userInfo = UserInfoCache.getInstance(getContext()).get();
                    if (userInfo != null) {
                        String prole = userInfo.getProle();

                        if (prole != null){
                            if (prole.equals("project_manager") || prole.equals("project_quota")){
                                showMoreDialog();
                            }
                        }
                    }
                }
                return true;
            }
        });

        getManagers();
        getOperteam();
    }

    public void setProject(Project project){
        this.project = project;
    }

    private void getManagers() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",project.getId());
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/managers";
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
                    List<Manager> managers = JSON.parseArray(JSON.toJSONString(jsonArray), Manager.class);

                    managerList.clear();
                    managerList.addAll(managers);
                    showOperteams();
                } else {
                    list_refresh.finishRefresh(false);
                    AppToast.show(getContext(),"获取项目部信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                AppToast.show(getContext(),"获取项目部信息出错!");
            }
        });
    }
    private void getOperteam() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",project.getId());
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/operteams";
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
                    List<Operteam> operteams = JSON.parseArray(JSON.toJSONString(jsonArray), Operteam.class);

                    operteamList.clear();
                    operteamList.addAll(operteams);
                    showOperteams();
                } else {
                    list_refresh.finishRefresh(false);
                    AppToast.show(getContext(),"获取作业队信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                list_refresh.finishRefresh(false);
                dialog.dismiss();
                AppToast.show(getContext(),"获取作业队信息出错!");
            }
        });
    }

    private void showOperteams() {
        list.clear();

        ProjectListItemWarp.ListItem item0 = new ProjectListItemWarp.ListItem();
        item0.field_1_1 = "项目部";
        item0.field_1_2 = "项目经理："+ project.getPm();
        item0.field_1_3 = "作业队："+ project.getOperteam_num() + "个";
        item0.field_2_1 = "开工日期：" + project.getStartdate();
        item0.field_2_2 = "结束日期：" + project.getEnddate();

        DecimalFormat df = new DecimalFormat("0.0");
        String time_scale = "0.0";
        String strDate = project.getStartdate();
        String duration = project.getDuration();
        try {
            double dDuratio = Double.parseDouble(duration);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
            Date now = new Date();
            double s = (now.getTime() - date.getTime()) / (24 * 60 * 60 * 1000.0) / dDuratio;
            time_scale = df.format(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

        item0.field_2_3 = "比例：" + time_scale + "%";
        item0.field_3_1 = "上岗人数：" + project.getOndutynum() + "(" + project.getOnjobnum() + ")";
        item0.field_3_2 = "累计工时："+ project.getPm();
        item0.field_3_3 = "";
        item0.field_4_1 = "发放总额：" + project.getTotalsalary();
        item0.field_4_2 = "";
        item0.field_4_3 = "";
        item0.isShowArraw = true;
        item0.type = 4;

        list.add(item0);

        for(Operteam operteam : operteamList){
            ProjectListItemWarp.ListItem item = new ProjectListItemWarp.ListItem();
            item.field_1_1 = operteam.getName();
            item.field_1_2 = "队长："+ operteam.getPm();
            item.field_1_3 = "班组：" + operteam.getClassteam_num() +"个";
            item.field_2_1 = "开工日期：" + operteam.getStartdate();
            item.field_2_2 = "结束日期：" + operteam.getEnddate();

            time_scale = "0.0";
            strDate = operteam.getStartdate();
            duration = operteam.getDuration();
            try {
                double dDuratio = Double.parseDouble(duration);
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
                Date now = new Date();
                double s = (now.getTime() - date.getTime()) / (24 * 60 * 60 * 1000.0) / dDuratio;
                time_scale = df.format(s);
            } catch (Exception e) {
                e.printStackTrace();
            }

            item.field_2_3 = "比例：" + time_scale + "%";
            item.field_3_1 = "上岗人数：" + operteam.getOndutynum() + "(" + operteam.getOnjobnum() + ")";
            item.field_3_2 = "累计工时："+ operteam.getPm();
            item.field_3_3 = "";
            item.field_4_1 = "发放总额：" + operteam.getTotalsalary();
            item.field_4_2 = "合同总额：" + operteam.getBudget();

            String totalsalary = operteam.getTotalsalary();
            String budget = operteam.getBudget();
            String scale = "0.0";
            try {
                double dSalary = Double.parseDouble(totalsalary);
                double dBudget = Double.parseDouble(budget);
                double s = dSalary * 100 / dBudget;
                scale = df.format(s);
            } catch (Exception e){
                e.printStackTrace();
            }

            item.field_4_3 = "比例：" + scale + "%";
            item.isShowArraw = true;
            item.type = 4;

            list.add(item);
        }

        projectAdapter.notifyDataSetChanged();
    }

    public void addOperteam() {
        Intent intent = new Intent(context, TaskTeamAddActivity.class);
        intent.putExtra("type", 0);
        intent.putExtra("project_id", project.getId()+"");
        startActivityForResult(intent, Constants.RELOAD);
    }

    private void updateOperteam(Operteam operteam) {
        Intent intent = new Intent(context, TaskTeamAddActivity.class);
        intent.putExtra("type", 1);
        intent.putExtra("operteam_id", operteam.getId() + "");
        startActivityForResult(intent, Constants.RELOAD);
    }

    private void deleteOperteam(Operteam operteam) {
        if (operteam == null){
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token","063d91b4f57518ff");
        jsonObject.put("id",operteam.getId());
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/operteam_delete";
        ProgressDialog dialog = ProgressDialog.createDialog(getActivity());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(getActivity(),"删除作业队成功!");
                    getOperteam();
                } else {
                    AppToast.show(getActivity(),"删除作业队失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getActivity(),"删除作业队出错!");
            }
        });
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
                            updateOperteam(operteamSelect);
                        } else if(id == R.id.txt_delete){
                            deleteOperteam(operteamSelect);
                        }

                        dialog.dismiss();
                    }
                };

                TextView txt_see = view.findViewById(R.id.txt_see);
                TextView txt_update = view.findViewById(R.id.txt_update);
                TextView txt_delete = view.findViewById(R.id.txt_delete);
                TextView txt_cancel = view.findViewById(R.id.txt_cancel);
                txt_see.setVisibility(View.GONE);
                txt_update.setText("更新作业队");
                txt_delete.setText("删除作业队");

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 重新加载数据
        if (requestCode == Constants.RELOAD && resultCode == RESULT_OK) {
            getOperteam();
        }
    }
}
