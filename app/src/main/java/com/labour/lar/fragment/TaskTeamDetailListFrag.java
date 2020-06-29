package com.labour.lar.fragment;

import android.app.Dialog;
import android.content.Context;
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
import com.labour.lar.activity.BanZuAddActivity;
import com.labour.lar.activity.BanZuDetailActivity;
import com.labour.lar.adapter.ProjectDetailListAdapter;
import com.labour.lar.adapter.ProjectListItemWarp;
import com.labour.lar.module.Classteam;
import com.labour.lar.module.Operteam;
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
 * 作业队
 */
public class TaskTeamDetailListFrag extends BaseFragment {

    @BindView(R.id.list_refresh)
    SmartRefreshLayout list_refresh;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.loading_v)
    LoadingView loadingView;
    @BindView(R.id.noresult_view)
    TextView noresult_view;

    ProjectDetailListAdapter projectAdapter;
    private List<Classteam> classteamList = new ArrayList<>();
    private List<ProjectListItemWarp.ListItem> list = new ArrayList<>();;

    private Classteam classteamSelect;
    private Operteam operteam;
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
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
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
                Classteam classteam = classteamList.get(position);

                Intent intent = new Intent(context, BanZuDetailActivity.class);
                intent.putExtra("classteam", classteam);
                intent.putExtra("project_id", operteam.getProject_id()+"");
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                classteamSelect = classteamList.get(position);
                showMoreDialog();
                return true;
            }
        });

        getClassteam();
    }

    private void getClassteam() {
        if (operteam == null){
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",operteam.getId());
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/classteams";
        ProgressDialog dialog = ProgressDialog.createDialog(this.getContext());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Classteam> classetams = JSON.parseArray(JSON.toJSONString(jsonArray), Classteam.class);

                    classteamList.clear();
                    classteamList.addAll(classetams);
                    showClassteams();
                } else {
                    AppToast.show(getContext(),"获取班组信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getContext(),"获取班组信息出错!");
            }
        });
    }

    private void showClassteams() {
        list.clear();

//        ProjectListItemWarp.ListItem item0 = new ProjectListItemWarp.ListItem();
//        item0.field1 = "项目部";
//        item0.field1Content = "共"+ project.getManagers_num() +"人";
//        item0.field2 = "项目经理："+ "";
//        item0.field2Content = "成员" + project.getBudget() + "个";
//        item0.field3 = "作业队："+ project.getOperteams_num() + "个";
//        item0.field3Content = "班组" + project.getOperteams_num() +"个";
//        item0.isShowArraw = true;
//        item0.isShowTwo = true;
//
//        list.add(item0);

        for(Classteam classteam : classteamList){
            ProjectListItemWarp.ListItem item = new ProjectListItemWarp.ListItem();
            item.field1 = classteam.getName();;
            item.field1Content = "";
            item.field2 = "人数：" + classteam.getEmployees_num() + "人";;
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

    public void addClassteam() {
        Intent intent = new Intent(context, BanZuAddActivity.class);
        intent.putExtra("type", 0);
        intent.putExtra("operteam_id", operteam.getId() + "");
        startActivityForResult(intent, Constants.RELOAD);
    }

    private void updateClassteam(Classteam classteam) {
        Intent intent = new Intent(context, BanZuAddActivity.class);
        intent.putExtra("type", 1);
        intent.putExtra("classteam_id", classteam.getId() + "");
        startActivityForResult(intent, Constants.RELOAD);
    }

    private void deleteClassteam(Classteam classteam) {
        if (classteam == null){
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token","063d91b4f57518ff");
        jsonObject.put("classteam_id",classteam.getId());
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/classteam_delete";
        ProgressDialog dialog = ProgressDialog.createDialog(getActivity());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(getActivity(),"删除班组成功!");
                    getClassteam();
                } else {
                    AppToast.show(getActivity(),"删除班组失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getActivity(),"删除班组出错!");
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
                            updateClassteam(classteamSelect);
                        } else if(id == R.id.txt_delete){
                            deleteClassteam(classteamSelect);
                        }

                        dialog.dismiss();
                    }
                };

                TextView txt_see = view.findViewById(R.id.txt_see);
                TextView txt_update = view.findViewById(R.id.txt_update);
                TextView txt_delete = view.findViewById(R.id.txt_delete);
                TextView txt_cancel = view.findViewById(R.id.txt_cancel);
                txt_see.setVisibility(View.GONE);
                txt_update.setText("更新班组");
                txt_delete.setText("删除班组");

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
            getClassteam();
        }
    }
}
