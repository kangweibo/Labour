package com.labour.lar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.labour.lar.BaseFragment;
import com.labour.lar.R;
import com.labour.lar.activity.ProjectDetailActivity;
import com.labour.lar.adapter.MessageAdapter;
import com.labour.lar.adapter.ProjectAdapter;
import com.labour.lar.module.Project;
import com.labour.lar.widget.LoadingView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MessageFrag extends BaseFragment {

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

    private MessageAdapter messageAdapter;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_message;
    }

    @Override
    public void initView() {
        back_iv.setVisibility(View.INVISIBLE);
        title_tv.setText("消息");
        loadingView.setVisibility(View.GONE);
        noresult_view.setVisibility(View.GONE);
        messageAdapter = new MessageAdapter(getContext());
        listView.setAdapter(messageAdapter);
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

        //测试
        List<Project> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add(new Project());
        }
        messageAdapter.setList(list);
        messageAdapter.notifyDataSetChanged();

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                startActivity(new Intent(context, ProjectDetailActivity.class));
//            }
//        });
    }
}
