package com.labour.lar.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.labour.lar.BaseFragment;
import com.labour.lar.R;
import com.labour.lar.adapter.MyFragmentPagerAdapter;
import com.labour.lar.module.Employee;
import com.labour.lar.widget.RoundImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 工人
 */
public class GongRenDetailFrag extends BaseFragment {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;
    @BindView(R.id.right_header_btn)
    TextView right_header_btn;

    @BindView(R.id.name_tv)
    TextView name_tv;
    @BindView(R.id.number_tv)
    TextView number_tv;
    @BindView(R.id.classteam_tv)
    TextView classteam_tv;
    @BindView(R.id.state_tv)
    TextView state_tv;
    @BindView(R.id.photo_iv)
    RoundImageView photo_iv;

    @BindView(R.id.psts_indicator)
    PagerSlidingTabStrip pstsIndicator;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    private MyFragmentPagerAdapter fragmentPagerAdapter;
    private ArrayList<Fragment> frgs = new ArrayList<Fragment>();
    FragmentManager fm;

    String[] titles  = {"个人资料","务工资料","考勤记录","发薪记录"};

    private Employee employee;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_gongren_detail;
    }

    @Override
    public void initView() {
        title_tv.setText("劳务工人");
        Drawable d = getResources().getDrawable(R.mipmap.jiahao);
        right_header_btn.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);
        right_header_btn.setVisibility(View.INVISIBLE);

        photo_iv.setImageResource(R.mipmap.picture);

        if (employee!= null){
            if (!TextUtils.isEmpty(employee.getName())){
                name_tv.setText(employee.getName());
            } else {
                name_tv.setText("");
            }

            String prole = employee.getProle();
            if (prole != null){
                if (prole.equals("project_manager") || prole.equals("project_quota") || prole.equals("manager")){
                    if (!TextUtils.isEmpty(employee.getProjectname())){
                        number_tv.setText("项目部：" + employee.getProjectname());
                    } else {
                        number_tv.setText("项目部：未知");
                    }
                    title_tv.setText("管理人员");
                }

                if (prole.equals("operteam_manager") || prole.equals("operteam_quota") || prole.equals("staff")){
                    if (!TextUtils.isEmpty(employee.getOperteamname())){
                        number_tv.setText("队部：" + employee.getOperteamname());
                    } else {
                        number_tv.setText("队部：未知");
                    }
                    title_tv.setText("管理人员");
                }

                if (prole.equals("classteam_manager") || prole.equals("employee")){
                    if (!TextUtils.isEmpty(employee.getClassteamname())){
                        number_tv.setText("班组：" + employee.getClassteamname());
                    } else {
                        number_tv.setText("班组：未知");
                    }
                    title_tv.setText("劳务工人");
                }
            }

            if (!TextUtils.isEmpty(employee.getDuty())){
                classteam_tv.setText("职务：" + employee.getDuty());
            } else {
                classteam_tv.setText("职务：未知");
            }

            state_tv.setText("");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fm = this.getChildFragmentManager();
        MyInfoFrag frag = new MyInfoFrag();
        frag.setUserInfo(employee.getId(), employee.getProle());

        frgs.add(frag);
        frgs.add(new GongRenDetailListFrag());
        frgs.add(new GongRenDetailListFrag());
        frgs.add(new GongRenDetailListFrag());
        fragmentPagerAdapter = new MyFragmentPagerAdapter(fm,titles,frgs);
        vpContent.setAdapter(fragmentPagerAdapter);

        pstsIndicator.setIndicatorColor(getResources().getColor(R.color.common_blue));
        pstsIndicator.setDividerColor(getResources().getColor(R.color.transparent));
        pstsIndicator.setTabPaddingLeftRight(1);
        pstsIndicator.setViewPager(vpContent);
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override public void onPageSelected(int position) {
                updateTextStyle(position);
            }
            @Override public void onPageScrollStateChanged(int state) {
            }
        });
        updateTextStyle(vpContent.getCurrentItem());
    }
    private void updateTextStyle(int position) {
        LinearLayout tabsContainer = (LinearLayout) pstsIndicator.getChildAt(0);
        for (int i = 0; i < tabsContainer.getChildCount(); i++) {
            TextView textView = (TextView) tabsContainer.getChildAt(i);
            if (position == i) {
                textView.setTextSize(16);
                textView.setTextColor(getResources().getColor(R.color.common_blue));
            } else {
                textView.setTextSize(12);
                textView.setTextColor(getResources().getColor(R.color.black));
            }
        }
    }
    @OnClick({R.id.back_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                getActivity().finish();
                break;

        }
    }

    /**
     * 设置工人
     * @param employee
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
