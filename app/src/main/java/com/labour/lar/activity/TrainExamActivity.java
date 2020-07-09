package com.labour.lar.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.ProjectDetailListAdapter;
import com.labour.lar.adapter.ProjectListItemWarp;
import com.labour.lar.module.Exam;
import com.labour.lar.module.ExamPaper;
import com.labour.lar.module.ExamResult;
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
public class TrainExamActivity extends BaseActivity {

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

    private List<Exam> examList = new ArrayList<>();
    private List<ExamResult> examresultList = new ArrayList<>();

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_train_exam;
    }

    @Override
    public void afterInitLayout() {
//        Intent intent = getIntent();
//        type = intent.getIntExtra("type", 0);
//        classteam_id = intent.getStringExtra("classteam_id");
//        operteam_id = intent.getStringExtra("operteam_id");

        title_tv.setText("岗前安全培训");

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

    //group数据
    private ArrayList<String> mGroupList;
    //item数据
    private ArrayList<ArrayList<String>> mItemSet;
    private void initData() {
        mGroupList = new ArrayList<>();
        mGroupList.add("我的家人");
        mGroupList.add("我的朋友");
        mGroupList.add("黑名单");
        mItemSet = new ArrayList<>();
        ArrayList<String> itemList1 = new ArrayList<>();
        itemList1.add("大妹");
        itemList1.add("二妹");
        itemList1.add("三妹");
        ArrayList<String> itemList2 = new ArrayList<>();
        itemList2.add("大美");
        itemList2.add("二美");
        itemList2.add("三美");
        ArrayList<String> itemList3 = new ArrayList<>();
        itemList3.add("狗蛋");
        itemList3.add("二丫");
        mItemSet.add(itemList1);
        mItemSet.add(itemList2);
        mItemSet.add(itemList3);
    }


    @OnClick({R.id.back_iv,R.id.take_photo_iv,R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.take_photo_iv:

                break;
            case R.id.btn_submit:

                break;
        }
    }

    private void getExam(String id) {
        final Map<String,String> param = new HashMap<>();
        param.put("token","063d91b4f57518ff");
        param.put("id",id);
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/get_exam";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){

                    JSONObject jo = jr.getData();
                    ExamPaper examPaper = JSON.parseObject(JSON.toJSONString(jo), ExamPaper.class);

//                    employeeList.clear();
//                    employeeList.addAll(classetams);
//                    showEmployees();
                } else {

                    AppToast.show(TrainExamActivity.this,"获取考试信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(TrainExamActivity.this,"获取考试信息出错!");
            }
        });
    }

}
