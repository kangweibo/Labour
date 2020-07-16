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
import com.labour.lar.cache.UserCache;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.module.Operteam;
import com.labour.lar.module.Project;
import com.labour.lar.module.User;
import com.labour.lar.module.UserInfo;
import com.labour.lar.widget.RoundImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 队部
 */
public class StaffDetailFrag extends BaseFragment {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;
    @BindView(R.id.right_header_btn)
    TextView right_header_btn;

    @BindView(R.id.name_tv)
    TextView name_tv;
    @BindView(R.id.company_tv)
    TextView company_tv;
    @BindView(R.id.type_tv)
    TextView type_tv;

    @BindView(R.id.photo_iv)
    RoundImageView photo_iv;

    @BindView(R.id.psts_indicator)
    PagerSlidingTabStrip pstsIndicator;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    private StaffDetailListFrag detailListFrag;
    private MyFragmentPagerAdapter fragmentPagerAdapter;
    private ArrayList<Fragment> frgs = new ArrayList<Fragment>();
    FragmentManager fm;
    String[] titles  = {"人员管理","地图围栏"};

    private Operteam operteam;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_banzu_detail;
    }

    @Override
    public void initView() {
        title_tv.setText("队部");
        Drawable d = getResources().getDrawable(R.mipmap.jiahao);
        right_header_btn.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);

        photo_iv.setImageResource(R.mipmap.picture);

        if (operteam!= null){
            if (!TextUtils.isEmpty(operteam.getPm())){
                name_tv.setText("队长：" + operteam.getPm());
            } else {
                name_tv.setText("队长：");
            }
            if (!TextUtils.isEmpty(operteam.getStaff_num())){
                company_tv.setText("花名册：" + operteam.getStaff_num() + "人");
            } else {
                company_tv.setText("花名册：0人");
            }

            type_tv.setText("");
        }

        right_header_btn.setVisibility(View.INVISIBLE);

        User user = UserCache.getInstance(getContext()).get();
        if (user != null) {
            String prole = user.getProle();
            if (prole != null) {
                if (prole.equals("project_manager") || prole.equals("project_quota")) {
                    User.Project project = user.getProject();
                    if (project != null && project.getId() == this.operteam.getProject_id()) {
                        right_header_btn.setVisibility(View.VISIBLE);
                    }
                }

                if (prole.equals("operteam_manager") || prole.equals("operteam_quota")){
                    User.Operteam operteam = user.getOperteam();
                    if (operteam != null && operteam.getId() == this.operteam.getId()){
                        right_header_btn.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fm = this.getChildFragmentManager();
        detailListFrag = new StaffDetailListFrag();
        detailListFrag.setOperteam(operteam);

//        GisMapFrag gisMapFrag = new GisMapFrag();
//        gisMapFrag.setProjectId(project_id);

        frgs.add(detailListFrag);
//        frgs.add(gisMapFrag);
        fragmentPagerAdapter = new MyFragmentPagerAdapter(fm,titles,frgs);
        vpContent.setAdapter(fragmentPagerAdapter);

        pstsIndicator.setIndicatorColor(getResources().getColor(R.color.common_blue));
        pstsIndicator.setDividerColor(getResources().getColor(R.color.transparent));
        pstsIndicator.setViewPager(vpContent);
        pstsIndicator.setVisibility(View.GONE);

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
    @OnClick({R.id.back_iv, R.id.right_header_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                getActivity().finish();
                break;
            case R.id.right_header_btn:
                addPerson();
                break;

        }
    }

    /**
     * 设置作业队
     * @param operteam
     */
    public void setOperteam(Operteam operteam) {
        this.operteam = operteam;
    }

    public void addPerson() {
        detailListFrag.addPerson();
    }
}
