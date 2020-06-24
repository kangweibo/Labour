package com.labour.lar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.labour.lar.activity.BanZuDetailActivity;
import com.labour.lar.adapter.ProjectDetailListAdapter;
import com.labour.lar.adapter.ProjectListItemWarp;
import com.labour.lar.module.Classteam;
import com.labour.lar.module.Operteam;
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

    private Operteam operteam;

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
                startActivity(intent);
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
        for(Classteam classteam : classteamList){
            ProjectListItemWarp.ListItem item = new ProjectListItemWarp.ListItem();
            item.field1 = classteam.getName();;
            item.field1Content = "-";
            item.field2 = "人数：" + classteam.getEcounts() + "人";;
            //item.field2Content = "班组80个";
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
}
