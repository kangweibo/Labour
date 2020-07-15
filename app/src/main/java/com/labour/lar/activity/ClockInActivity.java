package com.labour.lar.activity;

import android.support.v4.app.FragmentTransaction;
import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import com.labour.lar.fragment.KaoqinFrag;
import com.labour.lar.module.UserInfo;

/**
 * 代人打卡
 */
public class ClockInActivity extends BaseActivity {

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_clock_in;
    }

    @Override
    public void afterInitLayout() {
        UserInfo user = (UserInfo)getIntent().getSerializableExtra("user");

        if (user == null) {
            return;
        }

        KaoqinFrag frag = new KaoqinFrag();
        frag.setUser(user);

        FragmentTransaction trs = fm.beginTransaction();
        trs.add(R.id.container,frag);
        trs.commit();
    }
}
