package com.labour.lar.activity;

import android.support.v4.app.FragmentTransaction;
import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import com.labour.lar.fragment.BanZuDetailFrag;
import com.labour.lar.module.Classteam;

/**
 * 班组
 */
public class BanZuDetailActivity extends BaseActivity {

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_project_detail;
    }

    @Override
    public void afterInitLayout() {
        Classteam classteam = (Classteam)getIntent().getSerializableExtra("classteam");
        String project_id = getIntent().getStringExtra("project_id");
        BanZuDetailFrag banZuDetailFrag = new BanZuDetailFrag();
        banZuDetailFrag.setClassteam(classteam);
        banZuDetailFrag.setProjectId(project_id);

        FragmentTransaction trs = fm.beginTransaction();
        trs.add(R.id.container,banZuDetailFrag);
        trs.commit();
    }
}
