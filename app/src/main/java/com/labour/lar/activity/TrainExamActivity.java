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
import com.labour.lar.adapter.TrainPaperAdapter;
import com.labour.lar.module.Exam;
import com.labour.lar.module.ExamPaper;
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
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 培训
 */
public class TrainExamActivity extends BaseActivity {

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

    @BindView(R.id.txt_exam_time)
    TextView txt_exam_time;
    @BindView(R.id.txt_time_h)
    TextView txt_time_h;
    @BindView(R.id.txt_time_m)
    TextView txt_time_m;
    @BindView(R.id.txt_time_s)
    TextView txt_time_s;

    private TrainPaperAdapter trainAdapter;

    private List<Question1> question1List = new ArrayList<>();
    private List<Question2> question2List = new ArrayList<>();

    //group数据
    private ArrayList<TrainPaperAdapter.ListGroupItem> groupList = new ArrayList<>();;
    //item数据
    private ArrayList<ArrayList<TrainPaperAdapter.ListItem>> itemSet = new ArrayList<>();;

    private Exam exam;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_train_exam;
    }

    @Override
    public void afterInitLayout() {
        exam = (Exam)getIntent().getSerializableExtra("exam");

        if (exam != null && !TextUtils.isEmpty(exam.getTitle())) {
            title_tv.setText(exam.getTitle());
        } else {
            title_tv.setText("测试题");
        }

        if (exam != null && !TextUtils.isEmpty(exam.getExamtime())) {
            txt_exam_time.setText("考试时长" + exam.getExamtime() + "分钟");
            txt_time_m.setText(exam.getExamtime());
        }

        loadingView.setVisibility(View.GONE);
        noresult_view.setVisibility(View.GONE);
        trainAdapter = new TrainPaperAdapter(this);
        listView.setAdapter(trainAdapter);
        listView.setGroupIndicator(null);

        list_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getExam();
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

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        getExam();
    }

    private void initData() {
        TrainPaperAdapter.ListGroupItem groupItem0 = new TrainPaperAdapter.ListGroupItem();
        groupItem0.field1 = "判断题";
        TrainPaperAdapter.ListGroupItem groupItem1 = new TrainPaperAdapter.ListGroupItem();
        groupItem1.field1 = "单选题";

        groupList.clear();
        groupList.add(groupItem0);
        groupList.add(groupItem1);

        ArrayList<TrainPaperAdapter.ListItem> itemList0 = new ArrayList<>();
        for (Question1 question1: question1List) {
            TrainPaperAdapter.ListItem listItem = new TrainPaperAdapter.ListItem();
            listItem.question = question1.getQuestion();
            listItem.type = 0;
            itemList0.add(listItem);
        }

        ArrayList<TrainPaperAdapter.ListItem> itemList1 = new ArrayList<>();
        for (Question2 question2: question2List) {
            TrainPaperAdapter.ListItem listItem = new TrainPaperAdapter.ListItem();

            listItem.question = question2.getQuestion();
            listItem.choicea = question2.getChoicea();
            listItem.choiceb = question2.getChoiceb();
            listItem.choicec = question2.getChoicec();
            listItem.choiced = question2.getChoiced();
            listItem.type = 1;
            itemList1.add(listItem);
        }

        itemSet.clear();
        itemSet.add(itemList0);
        itemSet.add(itemList1);
        trainAdapter.notifyDataSetChanged();

        for (int i = 0; i < groupList.size(); i++) {
            listView.expandGroup(i);
        }
    }

    @OnClick({R.id.back_iv,R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.btn_submit:

                break;
        }
    }

    private void getExam() {
        if (exam == null) {
            return;
        }

        JSONObject param = new JSONObject();
        param.put("token","063d91b4f57518ff");
        param.put("id",exam.getId());
        String jsonParams = param.toJSONString();

        String url = Constants.HTTP_BASE + "/api/get_exam";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(true);
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONObject jo = jr.getData();
                    ExamPaper examPaper = JSON.parseObject(JSON.toJSONString(jo), ExamPaper.class);

                    question1List.clear();
                    question1List.addAll(examPaper.getQ1s());
                    question2List.clear();
                    question2List.addAll(examPaper.getQ2s());
                    initData();
                } else {
                    AppToast.show(TrainExamActivity.this,"获取考试信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                AppToast.show(TrainExamActivity.this,"获取考试信息出错!");
            }
        });
    }

}
