package com.labour.lar.activity;

import android.support.v4.app.FragmentTransaction;

import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import com.labour.lar.fragment.ProjectDetailFrag;
import com.labour.lar.fragment.TaskTeamDetailFrag;
import com.labour.lar.module.Operteam;
import com.labour.lar.module.Project;

/**
 * 作业队
 */
public class TaskTeamDetailActivity extends BaseActivity {

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_project_detail;
    }

    @Override
    public void afterInitLayout() {
        Operteam operteam = (Operteam)getIntent().getSerializableExtra("operteam");
        String fenceId = getIntent().getStringExtra("fenceId");
        TaskTeamDetailFrag taskTeamDetailFrag = new TaskTeamDetailFrag();
        taskTeamDetailFrag.setOperteam(operteam);
        taskTeamDetailFrag.setFenceId(fenceId);

        FragmentTransaction trs = fm.beginTransaction();
        trs.add(R.id.container,taskTeamDetailFrag);
        trs.commit();
    }
}
