package com.labour.lar.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.MineGridViewAdapter;
import com.labour.lar.cache.UserCache;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.module.Employee;
import com.labour.lar.module.User;
import com.labour.lar.module.UserInfo;
import com.labour.lar.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 项目管理
 */
public class ProjectManagerActivity extends BaseActivity {

    private int REQUEST_CODE_ClockIn = 126;

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.main_gridview)
    NoScrollGridView main_gridview;

    MineGridViewAdapter mineGridViewAdapter;
    List<Integer> imgList = new ArrayList();
    List<String> list = new ArrayList();

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_sub_operation;
    }

    @Override
    public void afterInitLayout(){
        title_tv.setText("项目及人员管理");

        mineGridViewAdapter = new MineGridViewAdapter(this);
        initData(mineGridViewAdapter);
        main_gridview.setAdapter(mineGridViewAdapter);
        main_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = list.get(position);
                if(item.equals("创建项目")){
                    addProject();
                } else if(item.equals("创建作业队")){
                    addOperteam();
                } else if(item.equals("创建班组")){
                    addClassteam();
                } else if(item.equals("二维码")){
                    showQRCode();
                } else if(item.equals("成员管理")){
                    Intent intent = new Intent(ProjectManagerActivity.this, InferiorsActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_ClockIn);
                }
            }
        });
    }

    private void initData(MineGridViewAdapter mineGridViewAdapter) {
        User user = UserCache.getInstance(this).get();
        if (user != null){
            String prole = user.getProle();

            if (prole.equals("ent_manager")){
                list.add("创建项目");
                imgList.add(R.mipmap.tab_home_checked);
            }

            if (prole.equals("project_manager")){
                list.add("创建作业队");
                imgList.add(R.mipmap.tab_home_checked);
            }

            if (prole.equals("operteam_manager")){
                list.add("创建班组");
                imgList.add(R.mipmap.tab_home_checked);
            }
        }

        list.add("二维码");
        imgList.add(R.mipmap.qr_code_icon);
        list.add("成员管理");
        imgList.add(R.mipmap.team_icon);

        String[] strs = list.toArray(new String[list.size()]);
        Integer[] imgs = imgList.toArray(new Integer[imgList.size()]);

        mineGridViewAdapter.setImgs(imgs);
        mineGridViewAdapter.setStrings(strs);
    }

    // 显示二维码
    private void showQRCode() {
        int type = 0;
        int id = 0;
        User user = UserCache.getInstance(this).get();
        if (user != null) {
            String prole = user.getProle();
            if (prole.equals("ent_manager")) {
                type = 0;
            }

            if (prole.equals("project_manager")) {
                type = 1;
                id = user.getProject().getId();
            }

            if (prole.equals("operteam_manager")) {
                type = 2;
                id = user.getOperteam().getId();
            }
        }
        Intent intent = new Intent(this, ShowQRCodeActivity2.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    private void addProject() {
        Intent intent = new Intent(this, ProjectAddActivity.class);
        startActivity(intent);
    }

    public void addOperteam() {
        User user = UserCache.getInstance(this).get();
        if (user != null) {
            if (user.getProject() != null){
                int project_id = user.getProject().getId();

                Intent intent = new Intent(this, TaskTeamAddActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("project_id", project_id+"");
                startActivityForResult(intent, Constants.RELOAD);
            }
        }
    }

    public void addClassteam() {
        User user = UserCache.getInstance(this).get();
        if (user != null) {
            if (user.getOperteam() != null){
                int operteam_id = user.getOperteam().getId();

                Intent intent = new Intent(this, BanZuAddActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("operteam_id", operteam_id + "");
                startActivityForResult(intent, Constants.RELOAD);
            }
        }
    }

    // 成员管理
    private void memberManager() {
//        User user = UserCache.getInstance(this).get();
//        if (user != null) {
//            String prole = user.getProle();
//            if (prole.equals("ent_manager")) {
//                type = 0;
//            }
//
//            if (prole.equals("project_manager")) {
//                type = 1;
//                id = user.getProject().getId();
//            }
//
//            if (prole.equals("operteam_manager")) {
//                type = 2;
//                id = user.getOperteam().getId();
//            }
//        }
//        Intent intent = new Intent(this, ShowQRCodeActivity2.class);
//        intent.putExtra("id", id);
//        intent.putExtra("type", type);
//        startActivity(intent);
    }

    @OnClick({R.id.back_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
        }
    }
}
