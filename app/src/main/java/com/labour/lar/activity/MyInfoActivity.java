package com.labour.lar.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import com.labour.lar.BaseActivity;
import com.labour.lar.cache.UserCache;
import com.labour.lar.fragment.MyInfoFrag;
import com.labour.lar.R;
import com.labour.lar.module.User;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人信息
 */
public class MyInfoActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;
    @BindView(R.id.right_header_btn)
    TextView right_header_btn;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_myinfo;
    }

    @Override
    public void afterInitLayout() {
        title_tv.setText("个人信息");

        UserCache userCache = UserCache.getInstance(this);
        User user = userCache.get();

        MyInfoFrag frag = new MyInfoFrag();
        frag.setUserInfo(user.getId(), user.getProle());
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
