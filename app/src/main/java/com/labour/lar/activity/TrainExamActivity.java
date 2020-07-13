package com.labour.lar.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.TrainPaperAdapter;
import com.labour.lar.cache.UserCache;
import com.labour.lar.module.Exam;
import com.labour.lar.module.ExamPaper;
import com.labour.lar.module.ExamResult;
import com.labour.lar.module.Question1;
import com.labour.lar.module.Question2;
import com.labour.lar.module.User;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.MCountDownTimer;
import com.labour.lar.widget.DialogUtil;
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

    private MCountDownTimer countDownTimer;
    private DecimalFormat df = new DecimalFormat("00");

    //group数据
    private ArrayList<TrainPaperAdapter.ListGroupItem> groupList = new ArrayList<>();;
    //item数据
    private ArrayList<ArrayList<TrainPaperAdapter.ListItem>> itemSet = new ArrayList<>();;

    private Exam exam;
    private ExamPaper examPaper;

    boolean isPause = true;

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
            try {
                txt_exam_time.setText("考试时长" + exam.getExamtime() + "分钟");

                int time = Integer.parseInt(exam.getExamtime());
                long hour = time / 60;
                int minute = time % 60;

                txt_time_m.setText(df.format(hour));
                txt_time_h.setText(df.format(minute));

                countDownTimer = new MCountDownTimer(time * 60 * 1000, 1000);
                countDownTimer.setOnCountDownListener(new MCountDownTimer.OnCountDownListener() {
                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onTick(long second) {
                        if (isPause){
                            return;
                        }

                        long h = second / 3600;
                        long m = second / 60;
                        long s = second % 60;

                        Log.d("countDownTimer", "onStop: countDownTimer.onTick()");

                        txt_time_h.setText(df.format(h));
                        txt_time_m.setText(df.format(m));
                        txt_time_s.setText(df.format(s));
                    }
                });

                countDownTimer.start();
            } catch (NumberFormatException e){

            }
        }

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

        getExam();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (countDownTimer != null){
            countDownTimer.start();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isPause = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null){
            countDownTimer.setOnCountDownListener(null);
            countDownTimer.cancel();
        }
    }

    private void initData() {
        String score1 = "";
        String score2 = "";;

        ArrayList<TrainPaperAdapter.ListItem> itemList0 = new ArrayList<>();
        for (Question1 question1: question1List) {
            TrainPaperAdapter.ListItem listItem = new TrainPaperAdapter.ListItem();

            score1 = question1.getScore();
            listItem.question = question1.getQuestion();
            listItem.id = question1.getId();
            listItem.type = 0;
            itemList0.add(listItem);
        }

        ArrayList<TrainPaperAdapter.ListItem> itemList1 = new ArrayList<>();
        for (Question2 question2: question2List) {
            TrainPaperAdapter.ListItem listItem = new TrainPaperAdapter.ListItem();

            score2 = question2.getScore();
            listItem.question = question2.getQuestion();
            listItem.choicea = question2.getChoicea();
            listItem.choiceb = question2.getChoiceb();
            listItem.choicec = question2.getChoicec();
            listItem.choiced = question2.getChoiced();
            listItem.id = question2.getId();
            listItem.type = 1;
            itemList1.add(listItem);
        }

        itemSet.clear();
        itemSet.add(itemList0);
        itemSet.add(itemList1);

        TrainPaperAdapter.ListGroupItem groupItem0 = new TrainPaperAdapter.ListGroupItem();
        groupItem0.field1 = "判断题（共" + question1List.size() + "题，每题" + score1 + "分）";
        TrainPaperAdapter.ListGroupItem groupItem1 = new TrainPaperAdapter.ListGroupItem();
        groupItem1.field1 = "单选题（共" + question2List.size() + "题，每题" + score2 + "分）";

        groupList.clear();
        groupList.add(groupItem0);
        groupList.add(groupItem1);

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
                submit();
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
                    examPaper = JSON.parseObject(JSON.toJSONString(jo), ExamPaper.class);

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

    // 提交考试
    private void submit() {
        DialogUtil.showConfirmDialog(this,"提示信息","确认交卷吗？",new DialogUtil.OnDialogEvent<Void>(){
            @Override
            public void onPositiveButtonClick(Void t) {
                commitExam();
            }
        });
    }

    // 提交考试
    private void commitExam() {
        if (exam == null) {
            return;
        }

        if (countDownTimer != null){
            countDownTimer.cancel();
        }

        UserCache userCache = UserCache.getInstance(this);
        User user = userCache.get();

        JSONObject param = new JSONObject();
        param.put("token","063d91b4f57518ff");
        param.put("examid",exam.getId());
        param.put("empolyeeid",user.getId());
        param.put("answer",getAnswer());
        String jsonParams = param.toJSONString();

        String url = Constants.HTTP_BASE + "/api/commit_exam";
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
                    showResult(examResult);
                } else {
                    AppToast.show(TrainExamActivity.this,"提交考试信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                AppToast.show(TrainExamActivity.this,"提交考试信息出错!");
            }
        });
    }

    private String getAnswer() {
        JSONObject answer = new JSONObject();

        JSONArray array1 = new JSONArray();
        JSONArray array2 = new JSONArray();

        for (TrainPaperAdapter.ListItem item : itemSet.get(0)) {
            if (!TextUtils.isEmpty(item.answer)) {
                JSONObject answer1 = new JSONObject();
                answer1.put("id", item.id);
                answer1.put("answer", item.answer);
                array1.add(answer1);
            }
        }

        for (TrainPaperAdapter.ListItem item : itemSet.get(1)) {
            if (!TextUtils.isEmpty(item.answer)) {
                JSONObject answer2 = new JSONObject();
                answer2.put("id", item.id);
                answer2.put("answer", item.answer);
                array2.add(answer2);
            }
        }

        answer.put("answer1",array1);
        answer.put("answer2",array2);

        return answer.toJSONString();
    }

    //显示结果
    private void showResult(ExamResult examResult) {
        String result = "得分" + examResult.getTotalscore();

        if (examResult.isIspass()) {
            result = result + "\n通过";
        } else {
            result = result + "\n未通过";
        }

        DialogUtil.showAlertDialog(this,"考试结果",result,
                new DialogUtil.OnDialogEvent<Void>(){
            @Override
            public void onPositiveButtonClick(Void t) {
                finish();
            }
        });
    }
}
