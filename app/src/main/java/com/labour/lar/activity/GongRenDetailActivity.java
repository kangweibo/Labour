package com.labour.lar.activity;

import android.support.v4.app.FragmentTransaction;

import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import com.labour.lar.fragment.BanZuDetailFrag;
import com.labour.lar.fragment.GongRenDetailFrag;
import com.labour.lar.fragment.TaskTeamDetailFrag;
import com.labour.lar.module.Employee;
import com.labour.lar.module.Operteam;

/**
 * 劳务工人
 */
public class GongRenDetailActivity extends BaseActivity {

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_project_detail;
    }

    @Override
    public void afterInitLayout() {
        Employee employee = (Employee)getIntent().getSerializableExtra("employee");
        GongRenDetailFrag gongRenDetailFrag = new GongRenDetailFrag();
        gongRenDetailFrag.setEmployee(employee);

        FragmentTransaction trs = fm.beginTransaction();
        trs.add(R.id.container,gongRenDetailFrag);
        trs.commit();
    }
}
