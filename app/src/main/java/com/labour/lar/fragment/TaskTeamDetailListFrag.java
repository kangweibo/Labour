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
import com.labour.lar.activity.BanZuAddActivity;
import com.labour.lar.activity.BanZuDetailActivity;
import com.labour.lar.activity.StaffDetailActivity;
import com.labour.lar.adapter.TaskTeamDetailListAdapter;
import com.labour.lar.adapter.TaskTeamListItemWarp;
import com.labour.lar.cache.UserCache;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.module.Classteam;
import com.labour.lar.module.Operteam;
import com.labour.lar.module.User;
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

    private TaskTeamDetailListAdapter taskteamAdapter;
    private List<Classteam> classteamList = new ArrayList<>();
    private List<TaskTeamListItemWarp.ListItem> list = new ArrayList<>();;

    private Classteam classteamSelect;
    private Operteam operteam;
    private BottomSelectDialog dialog;

    private boolean isShowOperteamSecret;
    private boolean isShowClassteamSecret;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_project_detail_list;
    }

    @Override
    public void initView() {

        loadingView.setVisibility(View.GONE);
        noresult_view.setVisibility(View.GONE);
        taskteamAdapter = new TaskTeamDetailListAdapter(getContext());
        listView.setAdapter(taskteamAdapter);

        User user = UserCache.getInstance(getContext()).get();
        if (user != null) {
            String prole = user.getProle();
            if (prole != null){
                if (prole.equals("ent_manager") ||prole.equals("project_manager") || prole.equals("project_quota")
                        || prole.equals("operteam_manager") || prole.equals("operteam_quota")) {
                    isShowOperteamSecret = true;
                    isShowClassteamSecret = true;
                }

                if (prole.equals("classteam_manager")){
                    isShowClassteamSecret = true;
                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getClassteam();
            }
        });
        list_refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
        list_refresh.setEnableLoadMore(false);

        taskteamAdapter.setList(list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(context, StaffDetailActivity.class);
                    intent.putExtra("operteam", operteam);
                    startActivity(intent);
                } else {
                    Classteam classteam = classteamList.get(position-1);

                    Intent intent = new Intent(context, BanZuDetailActivity.class);
                    intent.putExtra("classteam", classteam);
                    intent.putExtra("project_id", operteam.getProject_id() + "");
                    intent.putExtra("fenceId", operteam.getClockinfence() + "");
                    startActivity(intent);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(context, StaffDetailActivity.class);
                    intent.putExtra("operteam", operteam);
                    startActivity(intent);
                } else {
                    classteamSelect = classteamList.get(position-1);

                    UserInfo userInfo = UserInfoCache.getInstance(getContext()).get();
                    if (userInfo != null) {
                        String prole = userInfo.getProle();

                        if (prole != null) {
                            if (prole.equals("operteam_manager") || prole.equals("operteam_quota")) {
                                showMoreDialog();
                            }
                        }
                    }
                }
                return true;
            }
        });

        getClassteam();
    }

    private void getClassteam() {
        User user = UserCache.getInstance(getContext()).get();

        if (operteam == null){
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",operteam.getId());
        jsonObject.put("userid",user.getId());
        jsonObject.put("dtype",user.getProle());
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/user_classteams";
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
                    List<Classteam> classetams = JSON.parseArray(JSON.toJSONString(jsonArray), Classteam.class);

                    classteamList.clear();
                    classteamList.addAll(classetams);
                    showClassteams();
                } else {
                    list_refresh.finishRefresh(false);
                    AppToast.show(getContext(),"获取班组信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                AppToast.show(getContext(),"获取班组信息出错!");
            }
        });
    }

    private void showClassteams() {
        list.clear();

        TaskTeamListItemWarp.ListItem item0 = new TaskTeamListItemWarp.ListItem();
        item0.field_1_1 = "作业队队部";;
        item0.field_1_2 = "班组数：" + operteam.getClassteam_num() +"个";
        item0.field_2_1 = "上岗人数：" + operteam.getOndutynum_db() + "(" + operteam.getOnjobnum_db() + ")";
        item0.field_2_2 = "队长：" + operteam.getPm();
        item0.field_3_1 = "累计工时："+ operteam.getTotalworkday_db();
        item0.field_3_2 = "发放总额：" + operteam.getTotalsalary_db();
        item0.isShowArraw = true;

        if (isShowOperteamSecret){
            item0.type = 3;
        } else {
            item0.type = 2;
        }
        list.add(item0);

        for(Classteam classteam : classteamList){
            TaskTeamListItemWarp.ListItem item = new TaskTeamListItemWarp.ListItem();
            item.field_1_1 = classteam.getName();
            item.field_1_2 = "";
            item.field_2_1 = "上岗人数：" + classteam.getOndutynum() + "(" + classteam.getOnjobnum() + ")";
            item.field_2_2 = "班组长：" + classteam.getPm();
            item.field_3_1 = "累计工时："+ classteam.getTotalworkday();
            item.field_3_2 = "发放总额：" + classteam.getTotalsalary();
            item.isShowArraw = true;
            if (isShowClassteamSecret){
                item.type = 3;
            } else {
                item.type = 2;
            }

            list.add(item);
        }

        taskteamAdapter.notifyDataSetChanged();
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
