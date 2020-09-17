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
import com.labour.lar.module.User;
import com.labour.lar.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 项目管理
 */
public class ProjectManagerActivity extends BaseActivity {

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
        String title = getIntent().getStringExtra("title");
        title_tv.setText(title);

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
                } else if(item.contains("成员管理")){
                    memberManager();
                }
            }
        });
    }

    private void initData(MineGridViewAdapter mineGridViewAdapter) {
        User user = UserCache.getInstance(this).get();
        if (user != null){
            String prole = user.getProle();

            if (prole.equals("ent_manager")){
//                list.add("创建项目");
//                imgList.add(R.mipmap.tab_home_checked);
                list.add("项目/成员管理");
                imgList.add(R.mipmap.team_icon);
            }

            if (prole.equals("project_manager")){
//                list.add("创建作业队");
//                imgList.add(R.mipmap.tab_home_checked);
//                list.add("作业队/成员管理");
                list.add("项目/成员管理");
                imgList.add(R.mipmap.team_icon);
            }

            if (prole.equals("operteam_manager")){
//                list.add("创建班组");
//                imgList.add(R.mipmap.tab_home_checked);
                list.add("作业队/成员管理");
//                list.add("班组/成员管理");
                imgList.add(R.mipmap.team_icon);
            }
        }

        list.add("二维码");
        imgList.add(R.mipmap.qr_code_icon);
//        list.add("成员管理");
//        imgList.add(R.mipmap.team_icon);

        String[] strs = list.toArray(new String[list.size()]);
        Integer[] imgs = imgList.toArray(new Integer[imgList.size()]);

        mineGridViewAdapter.setImgs(imgs);
        mineGridViewAdapter.setStrings(strs);
    }

    // 显示二维码
    private void showQRCode() {
        Intent intent = new Intent(this, ShowQRCodeActivity.class);
        String title = "";
        int type = 0;
        int id = 0;
        User user = UserCache.getInstance(this).get();
        if (user != null) {
            String prole = user.getProle();
            if (prole.equals("ent_manager")) {
                type = 0;
                User.Ent ent = user.getEnt();
                if (ent != null) {
                    title = ent.getName();
                }
            }

            if (prole.equals("project_manager")) {
                type = 0;
                id = user.getProject().getId();
                User.Project project = user.getProject();
                if (project != null) {
                    title = project.getEnt().getName() + "\n" + project.getName();
                    intent.putExtra("projectId", project.getId());
                }
            }

            if (prole.equals("operteam_manager")) {
                type = 1;
                id = user.getOperteam().getId();
                User.Operteam operteam = user.getOperteam();
                if (operteam != null) {
                    title = operteam.getProject().getEnt().getName() + "\n"
                            + operteam.getProject().getName() + "\n"
                            + operteam.getName();
                    intent.putExtra("operteamId", operteam.getId());
                }
            }
        }

        intent.putExtra("id", id);
        intent.putExtra("type", type);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    private String getShowTitle() {
        String title = "";

        User user = UserCache.getInstance(this).get();
        if (user != null) {
            String prole = user.getProle();
            if (prole.equals("ent_manager")) {
                User.Ent ent = user.getEnt();
                if (ent != null) {
                    title = ent.getName();
                }
            }

            if (prole.equals("project_manager")) {
                User.Project project = user.getProject();
                if (project != null) {
                    title = project.getEnt().getName() + "\n" + project.getName();
                }
            }

            if (prole.equals("operteam_manager")) {
                User.Operteam operteam = user.getOperteam();
                if (operteam != null) {
                    title = operteam.getProject().getEnt().getName() + "\n"
                            + operteam.getProject().getName() + "\n"
                            + operteam.getName();
                }
            }
        }

        return title;
    }

    private void addProject() {
        User user = UserCache.getInstance(this).get();
        if (user != null) {
            if (user.getEnt() != null){
                int ent_id = user.getEnt().getId();

                Intent intent = new Intent(this, ProjectAddActivity.class);
                intent.putExtra("ent_id", ent_id+"");
                intent.putExtra("title", getShowTitle());
                startActivity(intent);
            }
        }
    }

    public void addOperteam() {
        User user = UserCache.getInstance(this).get();
        if (user != null) {
            if (user.getProject() != null){
                int project_id = user.getProject().getId();

                Intent intent = new Intent(this, TaskTeamAddActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("project_id", project_id+"");
                intent.putExtra("title", getShowTitle());
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
                intent.putExtra("title", getShowTitle());
                startActivityForResult(intent, Constants.RELOAD);
            }
        }
    }

    // 成员管理
    private void memberManager() {
        UserCache userCache = UserCache.getInstance(this);
        User user = userCache.get();

        String prole = user.getProle();
        int type = 0;
        int id = 0;

        if (prole != null){
            if (prole.equals("ent_manager")){
                type = 0;
                User.Ent ent = user.getEnt();
                if (ent != null) {
                    id = ent.getId();
                }
            }

            if (prole.equals("project_manager") || prole.equals("project_quota")){
                type = 1;
                User.Project project = user.getProject();
                if (project != null) {
                    id = project.getId();
                }
            }

            if (prole.equals("operteam_manager") || prole.equals("operteam_quota")){
                type = 2;
                User.Operteam operteam = user.getOperteam();
                if (operteam != null) {
                    id = operteam.getId();
                }
            }
        }

        Intent intent;

        switch (type){
            case 0:
                intent = new Intent(ProjectManagerActivity.this,
                        MemberOrgProjectActivity.class);
                break;
            case 1:
                intent = new Intent(ProjectManagerActivity.this,
                        MemberOrgTaskTeamActivity.class);
                break;
            default:
                intent = new Intent(ProjectManagerActivity.this,
                        MemberOrgClassTeamActivity.class);

        }

        intent.putExtra("id", id);
        intent.putExtra("type", type);
        intent.putExtra("title", getShowTitle());
        startActivity(intent);
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
