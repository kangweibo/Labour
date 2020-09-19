package com.labour.lar.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import com.labour.lar.adapter.MineGridViewAdapter;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.module.Employee;
import com.labour.lar.module.User;
import com.labour.lar.module.UserInfo;
import com.labour.lar.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 代操作
 */
public class SubOperationActivity extends BaseActivity {

    private int REQUEST_CODE_Identified = 124;
    private int REQUEST_CODE_Bankcard = 125;
    private int REQUEST_CODE_ClockIn = 126;

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.main_gridview)
    NoScrollGridView main_gridview;

    MineGridViewAdapter mineGridViewAdapter;
    List<Integer> imgList = new ArrayList();
    List<String> list = new ArrayList();

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_sub_operation;
    }

    @Override
    public void afterInitLayout(){
        title_tv.setText("代员工操作");

        mineGridViewAdapter = new MineGridViewAdapter(this);
        initData(mineGridViewAdapter);
        main_gridview.setAdapter(mineGridViewAdapter);
        main_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = list.get(position);
                if(item.equals("代员工批量注册")){
                    startActivity(new Intent(SubOperationActivity.this, SubRegisterActivity.class));
                } else if(item.equals("代员工银行卡认证")){
                    Intent intent = new Intent(SubOperationActivity.this, InferiorsActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_Bankcard);
                } else if(item.equals("代员工打卡")){
                    Intent intent = new Intent(SubOperationActivity.this, InferiorsActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_ClockIn);
                }
            }
        });
    }

    private void initData(MineGridViewAdapter mineGridViewAdapter) {
        UserInfo userInfo = UserInfoCache.getInstance(this).get();
        if (userInfo != null){
            String prole = userInfo.getProle();

            if (prole.equals("classteam_manager")
                    || prole.equals("operteam_manager") || prole.equals("operteam_quota")
                    || prole.equals("project_manager") || prole.equals("project_quota")
                    || prole.equals("ent_manager")){
                list.add("代员工批量注册");
                imgList.add(R.mipmap.idcard_icon);
                list.add("代员工银行卡认证");
                imgList.add(R.mipmap.bankcard_icon);
                list.add("代员工打卡");
                imgList.add(R.mipmap.tab_kaoqin_checked);
            }
        }

        String[] strs = list.toArray(new String[list.size()]);
        Integer[] imgs = imgList.toArray(new Integer[imgList.size()]);

        mineGridViewAdapter.setImgs(imgs);
        mineGridViewAdapter.setStrings(strs);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 代身份认证
        if (requestCode == REQUEST_CODE_Identified && resultCode == RESULT_OK) {
            if (data != null) {
                Employee employee = (Employee)data.getSerializableExtra("employee");
                if (employee != null) {
                    subIdentified(employee);
                }
            }
        }
        // 代银行卡认证
        if (requestCode == REQUEST_CODE_Bankcard && resultCode == RESULT_OK) {
            if (data != null) {
                Employee employee = (Employee)data.getSerializableExtra("employee");
                if (employee != null) {
                    subBankcard(employee);
                }
            }
        }
        // 代打卡
        if (requestCode == REQUEST_CODE_ClockIn && resultCode == RESULT_OK) {
            if (data != null) {
                Employee employee = (Employee)data.getSerializableExtra("employee");
                if (employee != null) {
                    subClockIn(employee);
                }
            }
        }
    }

    // 代身份认证
    private void subIdentified(Employee employee) {
        User user = new User();
        user.setId(employee.getId());
        user.setProle(employee.getProle());
        user.setName(employee.getName());

        Intent intent = new Intent(this, IdentifiedActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    // 代银行卡认证
    private void subBankcard(Employee employee) {
        User user = new User();
        user.setId(employee.getId());
        user.setProle(employee.getProle());
        user.setName(employee.getName());

        Intent intent = new Intent(this, BankcardAddActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    // 代打卡
    private void subClockIn(Employee employee) {
        UserInfo user = new UserInfo();
        user.setId(employee.getId());
        user.setProle(employee.getProle());
        user.setName(employee.getName());
        user.setIdentified(employee.isIdentified());

        Intent intent = new Intent(this, ClockInActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
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
