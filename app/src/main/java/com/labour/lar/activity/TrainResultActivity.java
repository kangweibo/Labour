package com.labour.lar.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.TrainResultAdapter;
import com.labour.lar.module.ExamResult;
import com.labour.lar.module.Question1;
import com.labour.lar.module.Question2;
import com.labour.lar.util.AjaxResult;
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
 * 培训成绩
 */
public class TrainResultActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.list_refresh)
    SmartRefreshLayout list_refresh;
    @BindView(R.id.listView)
    ExpandableListView listView;

    @BindView(R.id.txt_exam_result)
    TextView txt_exam_result;
    @BindView(R.id.txt_exam_score)
    TextView txt_exam_score;
    @BindView(R.id.txt_exam_time)
    TextView txt_exam_time;

    private TrainResultAdapter trainAdapter;

    private List<Question1> question1List = new ArrayList<>();
    private List<Question2> question2List = new ArrayList<>();

    //group数据
    private ArrayList<TrainResultAdapter.ListGroupItem> groupList = new ArrayList<>();;
    //item数据
    private ArrayList<ArrayList<TrainResultAdapter.ListItem>> itemSet = new ArrayList<>();;

    private ExamResult examResult;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_train_result;
    }

    @Override
    public void afterInitLayout() {
        examResult = (ExamResult)getIntent().getSerializableExtra("examResult");
        title_tv.setText("考试明细");

        if (examResult == null){
            return;
        }

        if (examResult.isIspass()) {
            txt_exam_result.setText("考试结果：通过");
        } else {
            txt_exam_result.setText("考试结果：未通过");
        }

        txt_exam_score.setText("考试分数：" + examResult.getTotalscore());

        if (!TextUtils.isEmpty(examResult.getUpdated_at())) {
            txt_exam_time.setText("考试时间：" + examResult.getUpdated_at());
        }

        trainAdapter = new TrainResultAdapter(this);
        listView.setAdapter(trainAdapter);
        listView.setGroupIndicator(null);

        list_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

            }
        });
        list_refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
        list_refresh.setEnableRefresh(false);
        list_refresh.setEnableLoadMore(false);

        trainAdapter.setGroupList(groupList);
        trainAdapter.setChildList(itemSet);
        trainAdapter.notifyDataSetChanged();

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        getExamresult(examResult.getId());
    }

    private void initData() {
        String score1 = "";
        String score2 = "";;

        ArrayList<TrainResultAdapter.ListItem> itemList0 = new ArrayList<>();
        for (Question1 question1: question1List) {
            TrainResultAdapter.ListItem listItem = new TrainResultAdapter.ListItem();
            listItem.question = question1.getQuestion();
            listItem.answer = question1.getAnswer();
            listItem.empanswer = question1.getEmpanswer();
            listItem.isright = question1.isIsright();
            listItem.id = question1.getId();
            listItem.type = 0;
            itemList0.add(listItem);
        }

        ArrayList<TrainResultAdapter.ListItem> itemList1 = new ArrayList<>();
        for (Question2 question2: question2List) {
            TrainResultAdapter.ListItem listItem = new TrainResultAdapter.ListItem();

            listItem.question = question2.getQuestion();
            listItem.choicea = question2.getChoicea();
            listItem.choiceb = question2.getChoiceb();
            listItem.choicec = question2.getChoicec();
            listItem.choiced = question2.getChoiced();
            listItem.answer = question2.getAnswer();
            listItem.empanswer = question2.getEmpanswer();
            listItem.isright = question2.isIsright();
            listItem.id = question2.getId();
            listItem.type = 1;
            itemList1.add(listItem);
        }

        itemSet.clear();
        itemSet.add(itemList0);
        itemSet.add(itemList1);

        TrainResultAdapter.ListGroupItem groupItem0 = new TrainResultAdapter.ListGroupItem();
        groupItem0.field1 = "判断题（共" + question1List.size() + "题，每题" + score1 + "分）";
        TrainResultAdapter.ListGroupItem groupItem1 = new TrainResultAdapter.ListGroupItem();
        groupItem1.field1 = "单选题（共" + question2List.size() + "题，每题" + score2 + "分）";

        groupList.clear();
        groupList.add(groupItem0);
        groupList.add(groupItem1);

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

    private void getExamresult(int id) {
        JSONObject param = new JSONObject();
        param.put("token","063d91b4f57518ff");
        param.put("id",id);
        String jsonParams = param.toJSONString();

        String url = Constants.HTTP_BASE + "/api/get_examresult";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONObject jo = jr.getData();
                    ExamResult examResult = JSON.parseObject(JSON.toJSONString(jo), ExamResult.class);

                    question1List.clear();
                    question1List.addAll(examResult.getQ1s());
                    question2List.clear();
                    question2List.addAll(examResult.getQ2s());
                    initData();
                } else {

                    AppToast.show(TrainResultActivity.this,"获取考试信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(TrainResultActivity.this,"获取考试信息出错!");
            }
        });
    }
}
