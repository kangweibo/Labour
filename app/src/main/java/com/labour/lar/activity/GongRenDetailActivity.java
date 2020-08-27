package com.labour.lar.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import com.labour.lar.fragment.MyInfoFrag;
import com.labour.lar.module.Employee;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 劳务工人
 */
public class GongRenDetailActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @Override
    public int getActivityLayoutId() {
        //return R.layout.activity_project_detail;
        return R.layout.activity_myinfo;
    }

    @Override
    public void afterInitLayout() {
        title_tv.setText("个人信息");

        Employee employee = (Employee)getIntent().getSerializableExtra("employee");
        MyInfoFrag frag = new MyInfoFrag();
        frag.setUserInfo(employee.getId(), employee.getProle(), false);

        FragmentTransaction trs = fm.beginTransaction();
        trs.add(R.id.container,frag);
        trs.commit();

//        GongRenDetailFrag gongRenDetailFrag = new GongRenDetailFrag();
//        gongRenDetailFrag.setEmployee(employee);
//        FragmentTransaction trs = fm.beginTransaction();
//        trs.add(R.id.container,gongRenDetailFrag);
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
