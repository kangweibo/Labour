//package com.labour.lar.activity;
//
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.labour.lar.BaseActivity;
//import com.labour.lar.Constants;
//import com.labour.lar.R;
//import com.labour.lar.adapter.MemberAdapter;
//import com.labour.lar.cache.UserCache;
//import com.labour.lar.module.Classteam;
//import com.labour.lar.module.Operteam;
//import com.labour.lar.module.Project;
//import com.labour.lar.module.User;
//import com.labour.lar.util.AjaxResult;
//import com.labour.lar.widget.BottomSelectDialog;
//import com.labour.lar.widget.LoadingView;
//import com.labour.lar.widget.ProgressDialog;
//import com.labour.lar.widget.toast.AppToast;
//import com.lzy.okgo.OkGo;
//import com.lzy.okgo.callback.StringCallback;
//import com.lzy.okgo.model.Response;
//import com.scwang.smartrefresh.layout.SmartRefreshLayout;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
///**
// * 成员
// */
//public class MemberOrgActivity extends BaseActivity {
//
//    @BindView(R.id.title_tv)
//    TextView title_tv;
//    @BindView(R.id.right_header_btn)
//    TextView right_header_btn;
//
//    @BindView(R.id.list_refresh)
//    SmartRefreshLayout list_refresh;
//    @BindView(R.id.listView)
//    ListView listView;
//    @BindView(R.id.loading_v)
//    LoadingView loadingView;
//    @BindView(R.id.noresult_view)
//    TextView noresult_view;
//    @BindView(R.id.txt_title)
//    TextView txt_title;
//
//    private MemberAdapter memberAdapter;
//
//    private List<MemberAdapter.ListItem> list = new ArrayList<>();
//
//    private int id;
//    private String title = "";
//    private int type;// 企业：0；项目：1；作业队：2；班组 3；
//
//    @Override
//    public int getActivityLayoutId() {
//        return R.layout.activity_member_org;
//    }
//
//    @Override
//    public void afterInitLayout() {
//        Intent intent = getIntent();
//        type = intent.getIntExtra("type", 0);
//        id = intent.getIntExtra("id", 0);
//        title = intent.getStringExtra("title");
//
//        getData();
//
//        Drawable d = getResources().getDrawable(R.mipmap.jiahao);
//        right_header_btn.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);
//
//        loadingView.setVisibility(View.GONE);
//        noresult_view.setVisibility(View.GONE);
//        memberAdapter = new MemberAdapter(this);
//        listView.setAdapter(memberAdapter);
//
//        list_refresh.setEnableRefresh(false);
//        list_refresh.setEnableLoadMore(false);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                MemberAdapter.ListItem item = list.get(position);
//                goNextLevel(item);
//            }
//        });
//
//        memberAdapter.setList(list);
//        memberAdapter.notifyDataSetChanged();
//    }
//
//    @OnClick({R.id.back_iv, R.id.right_header_btn})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.back_iv:
//                finish();
//                break;
//            case R.id.right_header_btn:
//                addItem();
//                break;
//        }
//    }
//
//    /**
//     * 获取数据
//     */
//    private void getData() {
//        switch (type){
//            case 0:
//                title_tv.setText("成员管理 项目列表");
//                getProject();
//                break;
//            case 1:
//                title_tv.setText("成员管理 作业队列表");
//                getOperteam(id);
//                break;
//            case 2:
//                title_tv.setText("成员管理 班组列表");
//                getClassteam(id);
//                break;
//        }
//
//        txt_title.setText(title);
//    }
//
//    private void getProject() {
//        UserCache userCache = UserCache.getInstance(this);
//        User user = userCache.get();
//        String prole = user.getProle();
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("token","063d91b4f57518ff");
//        jsonObject.put("dtype",prole);
//        jsonObject.put("userid",user.getId());
//        String jsonParams =jsonObject.toJSONString();
//
//        String url = Constants.HTTP_BASE + "/api/projects";
//        ProgressDialog dialog = ProgressDialog.createDialog(this);
//        dialog.show();
//
//        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
//            @Override
//            public void onSuccess(Response<String> response) {
//                dialog.dismiss();
//                AjaxResult jr = new AjaxResult(response.body());
//                if(jr.getSuccess() == 1){
//                    list_refresh.finishRefresh(true);
//
//                    JSONArray jsonArray = jr.getJSONArrayData();
//                    List<Project> projects = JSON.parseArray(JSON.toJSONString(jsonArray), Project.class);
//
//                    list.clear();
//                    for(Project project : projects){
//                        MemberAdapter.ListItem item = new MemberAdapter.ListItem();
//                        item.name = project.getName();
//                        item.id = project.getId();
//
//                        list.add(item);
//                    }
//                    showData();
//                } else {
//                    list_refresh.finishRefresh(false);
//                    AppToast.show(MemberOrgActivity.this,"获取项目信息失败!");
//                }
//            }
//            @Override
//            public void onError(Response<String> response) {
//                dialog.dismiss();
//                list_refresh.finishRefresh(false);
//                AppToast.show(MemberOrgActivity.this,"获取项目信息出错!");
//            }
//        });
//    }
//
//    private void getOperteam(int projectId) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("id",projectId);
//        String jsonParams =jsonObject.toJSONString();
//
//        String url = Constants.HTTP_BASE + "/api/operteams";
//        ProgressDialog dialog = ProgressDialog.createDialog(this);
//        dialog.show();
//
//        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
//            @Override
//            public void onSuccess(Response<String> response) {
//                dialog.dismiss();
//                AjaxResult jr = new AjaxResult(response.body());
//                if(jr.getSuccess() == 1){
//                    list_refresh.finishRefresh(true);
//
//                    JSONArray jsonArray = jr.getJSONArrayData();
//                    List<Operteam> operteams = JSON.parseArray(JSON.toJSONString(jsonArray), Operteam.class);
//
//                    list.clear();
//
//                    // 企业管理员可以管理项目部
//                    UserCache userCache = UserCache.getInstance(MemberOrgActivity.this);
//                    User user = userCache.get();
//                    String prole = user.getProle();
//                    if (prole != null && prole.equals("ent_manager")) {
//                        MemberAdapter.ListItem item0 = new MemberAdapter.ListItem();
//                        item0.name = "项目部";
//                        item0.id = projectId;
//                        item0.type = 0;
//                        list.add(item0);
//                    }
//
//                    for(Operteam operteam : operteams){
//                        MemberAdapter.ListItem item = new MemberAdapter.ListItem();
//                        item.name = operteam.getName();
//                        item.id = operteam.getId();
//                        item.type = 1;
//                        list.add(item);
//                    }
//
//                    showData();
//                } else {
//                    list_refresh.finishRefresh(false);
//                    AppToast.show(MemberOrgActivity.this,"获取作业队信息失败!");
//                }
//            }
//            @Override
//            public void onError(Response<String> response) {
//                list_refresh.finishRefresh(false);
//                dialog.dismiss();
//                AppToast.show(MemberOrgActivity.this,"获取作业队信息出错!");
//            }
//        });
//    }
//
//    private void getClassteam(int operteamId) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("id", operteamId);
//        String jsonParams =jsonObject.toJSONString();
//
//        String url = Constants.HTTP_BASE + "/api/classteams";
//        ProgressDialog dialog = ProgressDialog.createDialog(this);
//        dialog.show();
//
//        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
//            @Override
//            public void onSuccess(Response<String> response) {
//                dialog.dismiss();
//                AjaxResult jr = new AjaxResult(response.body());
//                if(jr.getSuccess() == 1){
//                    list_refresh.finishRefresh(true);
//
//                    JSONArray jsonArray = jr.getJSONArrayData();
//                    List<Classteam> classetams = JSON.parseArray(JSON.toJSONString(jsonArray), Classteam.class);
//
//                    list.clear();
//                    for(Classteam classetam : classetams){
//                        MemberAdapter.ListItem item = new MemberAdapter.ListItem();
//                        item.name = classetam.getName();
//                        item.id = classetam.getId();
//                        item.type = 2;
//                        list.add(item);
//                    }
//
//                    showData();
//                } else {
//                    list_refresh.finishRefresh(false);
//                    AppToast.show(MemberOrgActivity.this,"获取班组信息失败!");
//                }
//            }
//            @Override
//            public void onError(Response<String> response) {
//                dialog.dismiss();
//                list_refresh.finishRefresh(false);
//                AppToast.show(MemberOrgActivity.this,"获取班组信息出错!");
//            }
//        });
//    }
//
//    private void showData() {
//        memberAdapter.notifyDataSetChanged();
//    }
//
//    private void addItem() {
//        switch (type){
//            case 0:
//                addProject();
//                break;
//            case 1:
//                addOperteam();
//                break;
//            case 2:
//                addClassteam();
//                break;
//        }
//    }
//
//    private void addProject() {
//        Intent intent = new Intent(this, ProjectAddActivity.class);
//        intent.putExtra("title", title);
//        startActivity(intent);
//    }
//
//    public void addOperteam() {
//        User user = UserCache.getInstance(this).get();
//        if (user != null) {
//            if (user.getProject() != null){
//                int project_id = user.getProject().getId();
//
//                Intent intent = new Intent(this, TaskTeamAddActivity.class);
//                intent.putExtra("type", 0);
//                intent.putExtra("project_id", project_id+"");
//                intent.putExtra("title", title);
//                startActivityForResult(intent, Constants.RELOAD);
//            }
//        }
//    }
//
//    public void addClassteam() {
//        User user = UserCache.getInstance(this).get();
//        if (user != null) {
//            if (user.getOperteam() != null){
//                int operteam_id = user.getOperteam().getId();
//
//                Intent intent = new Intent(this, BanZuAddActivity.class);
//                intent.putExtra("type", 0);
//                intent.putExtra("operteam_id", operteam_id + "");
//                intent.putExtra("title", title);
//                startActivityForResult(intent, Constants.RELOAD);
//            }
//        }
//    }
//
//    private void goNextLevel(MemberAdapter.ListItem item) {
//        switch (type){
//            case 0:
//                goMemberOrgActivity(item);
//                break;
//            case 1:
//                goMemberManagerActivity(item);
//                break;
//            case 2:
//                goMemberManagerActivity(item);
//                break;
//        }
//    }
//
//    private void goMemberOrgActivity(MemberAdapter.ListItem item) {
//        Intent intent = new Intent(MemberOrgActivity.this, MemberOrgActivity.class);
//        intent.putExtra("id", item.id);
//        intent.putExtra("type", type+1);
//        intent.putExtra("title", title + "\n" + item.name);
//        startActivity(intent);
//    }
//
//    private void goMemberManagerActivity(MemberAdapter.ListItem item) {
//        Intent intent = new Intent(MemberOrgActivity.this, MemberManagerActivity.class);
//        intent.putExtra("id", item.id);
//        intent.putExtra("type", item.type);
//        intent.putExtra("title", title + "\n" + item.name);
//        startActivity(intent);
//    }
//}
