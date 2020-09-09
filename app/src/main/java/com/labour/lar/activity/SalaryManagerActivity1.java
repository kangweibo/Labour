package com.labour.lar.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.labour.lar.BaseActivity;
import com.labour.lar.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 薪酬管理
 */
public class SalaryManagerActivity1 extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_salary_manager;
    }

    @Override
    public void afterInitLayout(){
        title_tv.setText("薪酬管理");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.back_iv,R.id.txt_bankcard,R.id.txt_salary})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.txt_bankcard:
                startActivity(new Intent(this, BankcardAddActivity.class));
                break;
            case R.id.txt_salary:
                startActivity(new Intent(this, SalaryListActivity.class));
                break;
        }
    }
}
