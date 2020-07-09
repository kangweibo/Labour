package com.labour.lar.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.ProjectDetailListAdapter;
import com.labour.lar.adapter.ProjectListItemWarp;
import com.labour.lar.module.Exam;
import com.labour.lar.module.ExamResult;
import com.labour.lar.module.Question1;
import com.labour.lar.module.Question2;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 培训
 */
public class TrainTestActivity extends BaseActivity {

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

    ProjectDetailListAdapter projectAdapter;

    private int type; // 0:判断 1:单选

    private List<Question1> question1List = new ArrayList<>();
    private List<Question2> question2List = new ArrayList<>();

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_bankcard;
    }

    @Override
    public void afterInitLayout() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);

        if (type == 0) {
            title_tv.setText("岗前安全培训 判断题");
            getQarightorwrongs();
        } else {
            title_tv.setText("岗前安全培训 单选题");
            getQaselectones();
        }

        loadingView.setVisibility(View.GONE);
        noresult_view.setVisibility(View.GONE);
        projectAdapter = new ProjectDetailListAdapter(this);
        listView.setAdapter(projectAdapter);

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
        List<ProjectListItemWarp.ListItem> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            ProjectListItemWarp.ListItem item = new ProjectListItemWarp.ListItem();
            item.field1 = "南苑花园c座" + i;
            item.field1Content = "南苑花";
            item.field2 = "南苑花园c座";
            item.field2Content = "南苑花";
            item.field3 = "南苑花园c座";
            item.field3Content = "南苑花园";
            item.isShowArraw = false;

            list.add(item);
        }
        projectAdapter.setList(list);
        projectAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //startActivity(new Intent(context, TaskTeamDetailActivity.class));
            }
        });
    }

    @OnClick({R.id.back_iv,R.id.take_photo_iv,R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.btn_submit:

                break;
        }
    }


    private void getQarightorwrongs() {
        JSONObject param = new JSONObject();
        param.put("token","063d91b4f57518ff");
        param.put("offset",0);
        param.put("limit",10);
        String jsonParams = param.toJSONString();

        String url = Constants.HTTP_BASE + "/api/qarightorwrongs";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){

                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Question1> question1s = JSON.parseArray(JSON.toJSONString(jsonArray), Question1.class);

                    question1List.clear();
                    question1List.addAll(question1s);
                    showQuestion1();
                } else {

                    AppToast.show(TrainTestActivity.this,"获取考试信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(TrainTestActivity.this,"获取考试信息出错!");
            }
        });
    }

    private void getQaselectones() {
        JSONObject param = new JSONObject();
        param.put("token","063d91b4f57518ff");
        param.put("offset",0);
        param.put("limit",10);
        String jsonParams = param.toJSONString();

        String url = Constants.HTTP_BASE + "/api/qaselectones";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){

                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Question2> question2s = JSON.parseArray(JSON.toJSONString(jsonArray), Question2.class);

                    question2List.clear();
                    question2List.addAll(question2s);
                    showQuestion2();
                } else {

                    AppToast.show(TrainTestActivity.this,"获取成绩信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(TrainTestActivity.this,"获取成绩信息出错!");
            }
        });
    }

    private void showQuestion1() {

    }

    private void showQuestion2() {

    }
}
