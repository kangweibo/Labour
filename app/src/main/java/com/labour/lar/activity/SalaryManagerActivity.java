package com.labour.lar.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import com.labour.lar.adapter.MineGridViewAdapter;
import com.labour.lar.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 薪酬管理
 */
public class SalaryManagerActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.main_gridview)
    NoScrollGridView main_gridview;

    MineGridViewAdapter mineGridViewAdapter;
    List<Integer> imgList = new ArrayList();
    List<String> list = new ArrayList();

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_salary_manager;
    }

    @Override
    public void afterInitLayout(){
        title_tv.setText("薪酬管理");

        mineGridViewAdapter = new MineGridViewAdapter(this);
        initData(mineGridViewAdapter);
        main_gridview.setAdapter(mineGridViewAdapter);
        main_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = list.get(position);
                if(item.equals("银行卡管理")){
                    startActivity(new Intent(SalaryManagerActivity.this, BankcardAddActivity.class));
                } else if(item.equals("薪酬发放记录")){
                    startActivity(new Intent(SalaryManagerActivity.this, SalaryListActivity.class));
                }
            }
        });
    }

    private void initData(MineGridViewAdapter mineGridViewAdapter) {
        list.add("银行卡管理");
        imgList.add(R.mipmap.bankcard_icon);
        list.add("薪酬发放记录");
        imgList.add(R.mipmap.salary_icon);

        String[] strs = list.toArray(new String[list.size()]);
        Integer[] imgs = imgList.toArray(new Integer[imgList.size()]);

        mineGridViewAdapter.setImgs(imgs);
        mineGridViewAdapter.setStrings(strs);
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
