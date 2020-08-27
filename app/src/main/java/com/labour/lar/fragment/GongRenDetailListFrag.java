package com.labour.lar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.labour.lar.BaseFragment;
import com.labour.lar.R;
import com.labour.lar.adapter.ProjectDetailListAdapter;
import com.labour.lar.adapter.ProjectDetailListAdapter1;
import com.labour.lar.adapter.ProjectListItemWarp1;
import com.labour.lar.module.Employee;
import com.labour.lar.widget.LoadingView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 工人
 */
public class GongRenDetailListFrag extends BaseFragment {

    @BindView(R.id.list_refresh)
    SmartRefreshLayout list_refresh;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.loading_v)
    LoadingView loadingView;
    @BindView(R.id.noresult_view)
    TextView noresult_view;

    ProjectDetailListAdapter1 projectAdapter;

    private Employee employee;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_project_detail_list;
    }

    @Override
    public void initView() {

        loadingView.setVisibility(View.GONE);
        noresult_view.setVisibility(View.GONE);
        projectAdapter = new ProjectDetailListAdapter1(getContext());
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

        //测试
        List<ProjectListItemWarp1.ListItem> list = new ArrayList<>();
//        for(int i=0;i<10;i++){
//            ProjectListItemWarp.ListItem item = new ProjectListItemWarp.ListItem();
//            item.field1 = "南苑花园c座" + i;
//            item.field1Content = "南苑花";
//            item.field2 = "南苑花园c座";
//            item.field2Content = "南苑花";
//            item.field3 = "南苑花园c座";
//            item.field3Content = "南苑花园";
//            item.isShowArraw = false;
//
//            list.add(item);
//        }
        projectAdapter.setList(list);
        projectAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //startActivity(new Intent(context, TaskTeamDetailActivity.class));
            }
        });
    }

    /**
     * 设置作业队
     * @param employee
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
