package com.labour.lar.activity;

import android.support.v4.app.FragmentTransaction;

import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import com.labour.lar.fragment.BanZuDetailFrag;
import com.labour.lar.fragment.ManagerDetailFrag;
import com.labour.lar.module.Classteam;
import com.labour.lar.module.Project;

/**
 * 项目部
 */
public class ManagerDetailActivity extends BaseActivity {

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_project_detail;
    }

    @Override
    public void afterInitLayout() {
        Project project = (Project)getIntent().getSerializableExtra("project");
        ManagerDetailFrag detailFrag = new ManagerDetailFrag();
        detailFrag.setProject(project);

        FragmentTransaction trs = fm.beginTransaction();
        trs.add(R.id.container,detailFrag);
        trs.commit();
    }
}
