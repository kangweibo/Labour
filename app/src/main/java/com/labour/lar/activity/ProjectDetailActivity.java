package com.labour.lar.activity;

import android.support.v4.app.FragmentTransaction;

import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import com.labour.lar.fragment.ProjectDetailFrag;
import com.labour.lar.module.Project;

/**
 * 项目
 */
public class ProjectDetailActivity extends BaseActivity {

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_project_detail;
    }

    @Override
    public void afterInitLayout() {
        Project project = (Project)getIntent().getSerializableExtra("project");
        ProjectDetailFrag projectDetailFrag = new ProjectDetailFrag();
        projectDetailFrag.setProject(project);

        FragmentTransaction trs = fm.beginTransaction();
        trs.add(R.id.container,projectDetailFrag);
        trs.commit();
    }
}
