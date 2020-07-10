package com.labour.lar.activity;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.TrainLearn1Adapter;
import com.labour.lar.module.Question1;
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
import butterknife.OnClick;

/**
 * 培训学习
 */
public class TrainLearn1Activity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.list_refresh)
    SmartRefreshLayout list_refresh;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.loading_v)
    LoadingView loadingView;
    @BindView(R.id.noresult_view)
    TextView noresult_view;

    private TrainLearn1Adapter trainLearn1Adapter;

    private List<Question1> question1List = new ArrayList<>();
    private int offset;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_train_learn;
    }

    @Override
    public void afterInitLayout() {
        title_tv.setText("岗前安全培训 判断题");
        getQarightorwrongs(true);

        loadingView.setVisibility(View.GONE);
        noresult_view.setVisibility(View.GONE);
        trainLearn1Adapter = new TrainLearn1Adapter(this);
        listView.setAdapter(trainLearn1Adapter);

        list_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getQuestion(true);
            }
        });
        list_refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                getQuestion(false);
            }
        });

        trainLearn1Adapter.setList(question1List);
        trainLearn1Adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.back_iv,})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
        }
    }

    /**
     * 获取数据
     * @param refresh 是否全部刷新
     */
    private void getQuestion(boolean refresh) {
        getQarightorwrongs(refresh);
    }

    private void getQarightorwrongs(boolean refresh) {
        if (refresh){
            offset = 0;
        }

        JSONObject param = new JSONObject();
        param.put("token","063d91b4f57518ff");
        param.put("offset",offset);
        param.put("limit",10);
        String jsonParams = param.toJSONString();

        String url = Constants.HTTP_BASE + "/api/qarightorwrongs";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh();
                list_refresh.finishLoadMore();

                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Question1> question1s = JSON.parseArray(JSON.toJSONString(jsonArray), Question1.class);

                    if (refresh){
                        question1List.clear();
                    }

                    offset += question1s.size();
                    question1List.addAll(question1s);
                    showQuestion1();
                } else {
                    AppToast.show(TrainLearn1Activity.this,"获取考试信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                list_refresh.finishLoadMore(false);
                AppToast.show(TrainLearn1Activity.this,"获取考试信息出错!");
            }
        });
    }

    private void showQuestion1() {
        trainLearn1Adapter.notifyDataSetChanged();
    }
}
