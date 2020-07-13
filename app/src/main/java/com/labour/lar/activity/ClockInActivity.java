package com.labour.lar.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import com.labour.lar.fragment.KaoqinFrag;
import com.labour.lar.module.User;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 代人打卡
 */
public class ClockInActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_clock_in;
    }

    @Override
    public void afterInitLayout() {
        User user = (User)getIntent().getSerializableExtra("user");

        if (user == null) {
            return;
        }

        title_tv.setText("代员工考勤打卡");

        KaoqinFrag frag = new KaoqinFrag();
        //frag.setProject(project);

        FragmentTransaction trs = fm.beginTransaction();
        trs.add(R.id.container,frag);
        trs.commit();
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
