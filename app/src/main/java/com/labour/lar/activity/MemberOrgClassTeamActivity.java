package com.labour.lar.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.MemberAdapter;
import com.labour.lar.module.Classteam;
import com.labour.lar.module.Employee;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.widget.BottomSelectDialog;
import com.labour.lar.widget.DialogUtil;
import com.labour.lar.widget.LoadingView;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 成员
 */
public class MemberOrgClassTeamActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;
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
    @BindView(R.id.txt_title)
    TextView txt_title;

    private MemberAdapter memberAdapter;

    private List<MemberAdapter.ListItem> list = new ArrayList<>();
    private List<Classteam> classteamList = new ArrayList<>();
    private Classteam classteamSelect;
    private BottomSelectDialog dialog;

    private int id;
    private String title = "";
    private int type;// 企业：0；项目：1；作业队：2；班组 3；

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_member_org;
    }

    @Override
    public void afterInitLayout() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");

        getData();

        Drawable d = getResources().getDrawable(R.mipmap.jiahao);
        right_header_btn.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);

        loadingView.setVisibility(View.GONE);
        noresult_view.setVisibility(View.GONE);
        memberAdapter = new MemberAdapter(this);
        listView.setAdapter(memberAdapter);

        list_refresh.setEnableRefresh(false);
        list_refresh.setEnableLoadMore(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MemberAdapter.ListItem item = list.get(position);
                goNextLevel(item);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                classteamSelect = classteamList.get(position-1);
                showMoreDialog();
                return true;
            }
        });

        memberAdapter.setList(list);
        memberAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.back_iv, R.id.right_header_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.right_header_btn:
                addItem();
                break;
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        title_tv.setText("成员管理 班组列表");
        getClassteam(id);

        txt_title.setText(title);
    }

    private void getClassteam(int operteamId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", operteamId);
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/classteams";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    list_refresh.finishRefresh(true);

                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Classteam> classetams = JSON.parseArray(JSON.toJSONString(jsonArray), Classteam.class);

                    list.clear();
                    for(Classteam classetam : classetams){
                        MemberAdapter.ListItem item = new MemberAdapter.ListItem();
                        item.name = classetam.getName();
                        item.id = classetam.getId();
                        item.type = 2;
                        list.add(item);
                    }

                    showData();
                } else {
                    list_refresh.finishRefresh(false);
                    AppToast.show(MemberOrgClassTeamActivity.this,"获取班组信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                AppToast.show(MemberOrgClassTeamActivity.this,"获取班组信息出错!");
            }
        });
    }

    private void showData() {
        memberAdapter.notifyDataSetChanged();
    }

    private void addItem() {
        addClassteam();
    }

    private void goNextLevel(MemberAdapter.ListItem item) {
        goMemberManagerActivity(item);
    }

    private void goMemberManagerActivity(MemberAdapter.ListItem item) {
        Intent intent = new Intent(MemberOrgClassTeamActivity.this, MemberManagerActivity.class);
        intent.putExtra("id", item.id);
        intent.putExtra("type", item.type);
        intent.putExtra("title", title + "\n" + item.name);
        startActivity(intent);
    }

    private void showMoreDialog(){
        dialog = new BottomSelectDialog(this, new BottomSelectDialog.BottomSelectDialogListener() {
            @Override
            public int getLayout() {
                return R.layout.menu_fence;
            }
            @Override
            public void initView(View view) {
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = v.getId();
                        if(id == R.id.txt_update){
                            updateClassteam(classteamSelect);
                        } else if(id == R.id.txt_delete){
                            tryDeleteClassteam(classteamSelect);
                        }

                        dialog.dismiss();
                    }
                };

                TextView txt_see = view.findViewById(R.id.txt_see);
                TextView txt_update = view.findViewById(R.id.txt_update);
                TextView txt_delete = view.findViewById(R.id.txt_delete);
                TextView txt_cancel = view.findViewById(R.id.txt_cancel);
                txt_see.setVisibility(View.GONE);
                txt_update.setText("更新作业队");
                txt_delete.setText("删除作业队");

                txt_update.setOnClickListener(onClickListener);
                txt_delete.setOnClickListener(onClickListener);
                txt_cancel.setOnClickListener(onClickListener);
            }
            @Override
            public void onClick(Dialog dialog, int rate) {

            }
        });

        dialog.showAtLocation(mRootView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public void addClassteam() {
        Intent intent = new Intent(this, BanZuAddActivity.class);
        intent.putExtra("type", 0);
        intent.putExtra("operteam_id", id + "");
        intent.putExtra("title", title);
        startActivityForResult(intent, Constants.RELOAD);
    }

    private void updateClassteam(Classteam classteam) {
        if (classteam == null){
            return;
        }

        Intent intent = new Intent(this, BanZuAddActivity.class);
        intent.putExtra("type", 1);
        intent.putExtra("classteam", classteam);
        startActivityForResult(intent, Constants.RELOAD);
    }

    private void tryDeleteClassteam(Classteam classteam) {
        DialogUtil.showConfirmDialog(this,"提示信息","确定删除班组吗？",new DialogUtil.OnDialogEvent<Void>(){
            @Override
            public void onPositiveButtonClick(Void t) {
                deleteClassteam(classteam);
            }
        });
    }

    private void deleteClassteam(Classteam classteam) {
        if (classteam == null){
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token","063d91b4f57518ff");
        jsonObject.put("id",classteam.getId());
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/classteam_delete";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(MemberOrgClassTeamActivity.this,"删除班组成功!");
                    getClassteam(id);
                } else {
                    AppToast.show(MemberOrgClassTeamActivity.this,"删除班组失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(MemberOrgClassTeamActivity.this,"删除班组出错!");
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 重新加载数据
        if (requestCode == Constants.RELOAD && resultCode == RESULT_OK) {
            getData();
        }
    }
}
