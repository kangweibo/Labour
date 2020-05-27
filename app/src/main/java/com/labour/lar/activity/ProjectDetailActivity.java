package com.labour.lar.activity;

import android.support.v4.app.FragmentTransaction;

import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import com.labour.lar.fragment.ProjectDetailFrag;

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
        FragmentTransaction trs = fm.beginTransaction();
        trs.add(R.id.container,new ProjectDetailFrag());
        trs.commit();
    }
}
