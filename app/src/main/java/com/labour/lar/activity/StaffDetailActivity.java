package com.labour.lar.activity;

import android.support.v4.app.FragmentTransaction;
import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import com.labour.lar.fragment.StaffDetailFrag;
import com.labour.lar.module.Operteam;

/**
 * 队部
 */
public class StaffDetailActivity extends BaseActivity {

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_project_detail;
    }

    @Override
    public void afterInitLayout() {
        Operteam operteam = (Operteam)getIntent().getSerializableExtra("operteam");
        StaffDetailFrag detailFrag = new StaffDetailFrag();
        detailFrag.setOperteam(operteam);

        FragmentTransaction trs = fm.beginTransaction();
        trs.add(R.id.container,detailFrag);
        trs.commit();
    }
}
