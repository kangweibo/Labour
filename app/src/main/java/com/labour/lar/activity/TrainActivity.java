package com.labour.lar.activity;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.TrainAdapter;
import com.labour.lar.cache.UserCache;
import com.labour.lar.module.Exam;
import com.labour.lar.module.ExamResult;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 培训
 */
public class TrainActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.list_refresh)
    SmartRefreshLayout list_refresh;
    @BindView(R.id.listView)
    ExpandableListView listView;
    @BindView(R.id.loading_v)
    LoadingView loadingView;
    @BindView(R.id.noresult_view)
    TextView noresult_view;

    private TrainAdapter trainAdapter;

    private List<Exam> examList = new ArrayList<>();
    private List<ExamResult> examresultList = new ArrayList<>();

    //group数据
    private ArrayList<TrainAdapter.ListGroupItem> groupList = new ArrayList<>();;
    //item数据
    private ArrayList<ArrayList<TrainAdapter.ListItem>> itemSet = new ArrayList<>();;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_train;
    }

    @Override
    public void afterInitLayout() {
        title_tv.setText("岗前安全培训");

        loadingView.setVisibility(View.GONE);
        noresult_view.setVisibility(View.GONE);
        trainAdapter = new TrainAdapter(this);
        listView.setAdapter(trainAdapter);
        listView.setGroupIndicator(null);

        list_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getExams();
                getExamsResults();
            }
        });
        list_refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
        list_refresh.setEnableLoadMore(false);

        trainAdapter.setGroupList(groupList);
        trainAdapter.setChildList(itemSet);
        trainAdapter.notifyDataSetChanged();

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        initData();
        getExams();
        getExamsResults();
    }

    private void initData() {
        TrainAdapter.ListGroupItem groupItem0 = new TrainAdapter.ListGroupItem();
        groupItem0.field1 = "培训";
        TrainAdapter.ListGroupItem groupItem1 = new TrainAdapter.ListGroupItem();
        groupItem1.field1 = "考试";
        TrainAdapter.ListGroupItem groupItem2 = new TrainAdapter.ListGroupItem();
        groupItem2.field1 = "历史成绩";

        groupList.clear();
        groupList.add(groupItem0);
        groupList.add(groupItem1);
        groupList.add(groupItem2);

        ArrayList<TrainAdapter.ListItem> itemList0 = new ArrayList<>();
        for (int i=0; i<2; i++) {
            TrainAdapter.ListItem listItem = new TrainAdapter.ListItem();
            listItem.field1 = "培训";

            if (i == 1){
                listItem.field2 = "判断题";
            } else {
                listItem.field2 = "单选题";
            }

            listItem.field3 = null;
            listItem.field4 = null;
            listItem.field5 = null;
            listItem.field6 = null;
            listItem.field7 = null;
            listItem.arraw = "测试";
            listItem.resId = R.mipmap.train_icon;
            itemList0.add(listItem);
        }

        ArrayList<TrainAdapter.ListItem> itemList1 = new ArrayList<>();
        for (Exam exam: examList) {
            TrainAdapter.ListItem listItem = new TrainAdapter.ListItem();
            listItem.field1 = exam.getTitle();
            listItem.field2 = exam.getTitle();
            listItem.field3 = null;
            listItem.field4 = exam.getTitle();
            listItem.field5 = null;
            listItem.field6 = exam.getTitle();
            listItem.field7 = null;
            listItem.arraw = "测试";
            listItem.resId = R.mipmap.exam_icon;
            itemList1.add(listItem);
        }

        ArrayList<TrainAdapter.ListItem> itemList2 = new ArrayList<>();
        for (ExamResult result: examresultList) {
            TrainAdapter.ListItem listItem = new TrainAdapter.ListItem();

            if (result.isIspass()) {
                listItem.field1 = "通过";
            } else {
                listItem.field1 = "未通过";
            }

            listItem.field2 = "满分";
            listItem.field3 = "对题";
            listItem.field4 = "得分";
            listItem.field5 = "错题";
            listItem.field6 = null;
            listItem.field7 = null;
            listItem.arraw = "详细";
            listItem.resId = R.mipmap.achievement_icon;
            itemList2.add(listItem);
        }

        itemSet.clear();
        itemSet.add(itemList0);
        itemSet.add(itemList1);
        itemSet.add(itemList2);
        trainAdapter.notifyDataSetChanged();

        for (int i = 0; i < groupList.size(); i++) {
            listView.expandGroup(i);
        }
    }

    @OnClick({R.id.back_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
        }
    }

    private void getExams() {
        final Map<String,String> param = new HashMap<>();
        param.put("token","063d91b4f57518ff");
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/exams";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(true);

                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Exam> exams = JSON.parseArray(JSON.toJSONString(jsonArray), Exam.class);

                    examList.clear();
                    examList.addAll(exams);
                    initData();
                } else {

                    AppToast.show(TrainActivity.this,"获取考试信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                AppToast.show(TrainActivity.this,"获取考试信息出错!");
            }
        });
    }

    private void getExamsResults() {
        UserCache userCache = UserCache.getInstance(this);
        User user = userCache.get();

        if (user == null) {
            return;
        }

        final Map<String,String> param = new HashMap<>();
        param.put("token","063d91b4f57518ff");
        param.put("employeeid",user.getId()+"");
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/examresults";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(true);

                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<ExamResult> examresults = JSON.parseArray(JSON.toJSONString(jsonArray), ExamResult.class);

                    examresultList.clear();
                    examresultList.addAll(examresults);
                    initData();
                } else {

                    AppToast.show(TrainActivity.this,"获取成绩信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                AppToast.show(TrainActivity.this,"获取成绩信息出错!");
            }
        });
    }
}
