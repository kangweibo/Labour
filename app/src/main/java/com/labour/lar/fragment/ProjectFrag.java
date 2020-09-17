package com.labour.lar.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import com.labour.lar.activity.ProjectAddActivity;
import com.labour.lar.activity.ProjectDetailActivity;
import com.labour.lar.adapter.ProjectAdapter;
import com.labour.lar.cache.UserCache;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ProjectFrag extends BaseFragment {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;
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

    ProjectAdapter projectAdapter;
    List<Project> projectList = new ArrayList<>();
    private List<ProjectAdapter.ListItem> list = new ArrayList<>();;
    Handler handler;
    boolean isShowSecret;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_project;
    }

    @Override
    public void initView() {
        back_iv.setVisibility(View.INVISIBLE);
        title_tv.setText("项目");
        Drawable d = getResources().getDrawable(R.mipmap.jiahao);
        right_header_btn.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);
        right_header_btn.setVisibility(View.GONE);

        loadingView.setVisibility(View.GONE);
        noresult_view.setVisibility(View.GONE);
        projectAdapter = new ProjectAdapter(getContext());
        listView.setAdapter(projectAdapter);

        UserCache userCache = UserCache.getInstance(getContext());
        User user = userCache.get();
        if (user != null) {
            String prole = user.getProle();

            if (prole != null){
                if (prole.equals("ent_manager") || prole.equals("project_manager") || prole.equals("project_quota")){
                    isShowSecret = true;
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
                getProject();
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
                Project project = projectList.get(position);

                Intent intent = new Intent(context, ProjectDetailActivity.class);
                intent.putExtra("project", project);
                startActivity(intent);
            }
        });

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getProject();
            }
        },1000);
    }

    @OnClick({R.id.right_header_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_header_btn:
                addProject();
                break;
        }
    }

    private void getProject() {
        UserCache userCache = UserCache.getInstance(getContext());
        User user = userCache.get();

        String prole = user.getProle();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token","063d91b4f57518ff");
        jsonObject.put("dtype",prole);
        jsonObject.put("userid",user.getId());
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/projects";
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
                    List<Project> projects = JSON.parseArray(JSON.toJSONString(jsonArray), Project.class);

                    projectList.clear();
                    projectList.addAll(projects);

                    showProjects();
                } else {
                    list_refresh.finishRefresh(false);
                    AppToast.show(getContext(),"获取项目信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                AppToast.show(getContext(),"获取项目信息出错!");
            }
        });
    }

    private void showProjects() {
        list.clear();
        DecimalFormat df = new DecimalFormat("0.0");
        String time_scale = "0.0";

        for(Project project : projectList){
            ProjectAdapter.ListItem item = new ProjectAdapter.ListItem();
            item.field_1_1 = project.getName();
            item.field_1_2 = "在建";
            item.field_2_1 = "开工日期：" + project.getStartdate();
            item.field_2_2 = "结束日期：" + project.getEnddate();

            time_scale = "0.0";
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

            item.field_3_1 = "上岗人数：" + project.getOndutynum() + "(" + project.getOnjobnum() + ")";
            item.field_3_2 = "累计工时："+ project.getTotalworkday();

            item.field_4_1 = "合同总额：" + project.getBudget();
            item.field_4_2 = "发放总额：" + project.getTotalsalary();

            String totalsalary = project.getTotalsalary();
            String budget = project.getBudget();
            String scale = "0.0";
            try {
                double dSalary = Double.parseDouble(totalsalary);
                double dBudget = Double.parseDouble(budget);
                double s = dSalary * 100 / dBudget;
                scale = df.format(s);
            } catch (Exception e){
                e.printStackTrace();
            }

            item.field_5_1 = "时间比例：" + time_scale + "%";
            item.field_5_2 = "发放比例：" + scale + "%";

            if (isShowSecret){
                item.type = 5;
            } else {
                item.type = 3;
            }

            list.add(item);
        }

        projectAdapter.notifyDataSetChanged();
    }
    private void addProject() {
        Intent intent = new Intent(context, ProjectAddActivity.class);
        startActivity(intent);
    }
}
