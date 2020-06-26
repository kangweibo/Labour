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
import com.labour.lar.activity.TaskTeamAddActivity;
import com.labour.lar.activity.TaskTeamDetailActivity;
import com.labour.lar.adapter.ProjectDetailListAdapter;
import com.labour.lar.adapter.ProjectListItemWarp;
import com.labour.lar.module.Operteam;
import com.labour.lar.module.Project;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.StringUtils;
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
                Operteam operteam = operteamList.get(position);

                Intent intent = new Intent(context, TaskTeamDetailActivity.class);
                intent.putExtra("operteam", operteam);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                operteamSelect = operteamList.get(position);
                showMoreDialog();
                return true;
            }
        });

        getOperteam();
    }

    public void setProject(Project project){
        this.project = project;
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
                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Operteam> operteams = JSON.parseArray(JSON.toJSONString(jsonArray), Operteam.class);

                    operteamList.clear();
                    operteamList.addAll(operteams);
                    showOperteams();
                } else {
                    AppToast.show(getContext(),"获取作业队信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getContext(),"获取作业队信息出错!");
            }
        });
    }

    private void showOperteams() {
        list.clear();
        for(Operteam operteam : operteamList){
            ProjectListItemWarp.ListItem item = new ProjectListItemWarp.ListItem();
            item.field1 = operteam.getName();;
            item.field1Content = "-";
            item.field2 = "人数："+"30"+"人";
            item.field2Content = "班组80个";
            item.isShowArraw = true;

            list.add(item);
        }

        projectAdapter.notifyDataSetChanged();
    }

    public void addOperteam() {
        Intent intent = new Intent(context, TaskTeamAddActivity.class);
        intent.putExtra("type", 0);
        startActivity(intent);
    }

    private void updateOperteam(Operteam operteam) {
        Intent intent = new Intent(context, TaskTeamAddActivity.class);
        intent.putExtra("type", 1);
        intent.putExtra("operteam_id", operteam.getId() + "");
        startActivity(intent);
    }

    private void deleteOperteam(Operteam operteam) {
        if (operteam == null){
            return;
        }

        JSONObject jsonObject = new JSONObject();
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
}
