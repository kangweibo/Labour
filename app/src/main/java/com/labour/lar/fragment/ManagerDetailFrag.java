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
import com.labour.lar.module.Project;
import com.labour.lar.module.User;
import com.labour.lar.module.UserInfo;
import com.labour.lar.widget.RoundImageView;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 项目部
 */
public class ManagerDetailFrag extends BaseFragment {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;
    @BindView(R.id.right_header_btn)
    TextView right_header_btn;

//    @BindView(R.id.name_tv)
//    TextView name_tv;
//    @BindView(R.id.company_tv)
//    TextView company_tv;
//    @BindView(R.id.type_tv)
//    TextView type_tv;
//    @BindView(R.id.txt_team)
//    TextView txt_team;
//
//    @BindView(R.id.photo_iv)
//    RoundImageView photo_iv;

    @BindView(R.id.txt_number_people)
    TextView txt_number_people;
    @BindView(R.id.txt_work_hours)
    TextView txt_work_hours;
    @BindView(R.id.txt_money)
    TextView txt_money;
    @BindView(R.id.txt_pm)
    TextView txt_pm;

    @BindView(R.id.psts_indicator)
    PagerSlidingTabStrip pstsIndicator;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    private ManagerDetailListFrag detailListFrag;
    private MyFragmentPagerAdapter fragmentPagerAdapter;
    private ArrayList<Fragment> frgs = new ArrayList<Fragment>();
    private FragmentManager fm;
    private String[] titles  = {"人员管理","地图围栏"};

    private Project project;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_banzu_detail;
    }

    @Override
    public void initView() {
        title_tv.setText("项目部");
        Drawable d = getResources().getDrawable(R.mipmap.jiahao);
        right_header_btn.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);

//        photo_iv.setImageResource(R.mipmap.picture);

        if (project!= null){
            title_tv.setText(project.getName() + "-项目部详情");
            txt_number_people.setText("上岗人数：" + project.getOndutynum_xmb() + "(" + project.getOnjobnum_xmb() + ")");
            txt_work_hours.setText("累计工时：" + project.getTotalworkday_xmb());
            txt_money.setText("发放总额：" + project.getTotalsalary_xmb());
            txt_pm.setText("项目经理：" + project.getPm());

//            name_tv.setText("项目部");
//
//            if (!TextUtils.isEmpty(project.getName())){
//                txt_team.setText(project.getName());
//            } else {
//                txt_team.setText("");
//            }
//
//            if (!TextUtils.isEmpty(project.getPm())){
//                company_tv.setText("项目经理：" + project.getPm());
//            } else {
//                company_tv.setText("项目经理：");
//            }
//
//            if (!TextUtils.isEmpty(project.getManager_num())){
//                type_tv.setText("花名册：" + project.getManager_num() + "人");
//            } else {
//                type_tv.setText("花名册：0人");
//            }
        }

        right_header_btn.setVisibility(View.INVISIBLE);

        User user = UserCache.getInstance(getContext()).get();
        if (user != null) {
//            String prole = user.getProle();
//            if (prole != null) {
//                if (prole.equals("project_manager") || prole.equals("project_quota")) {
//                    User.Project project = user.getProject();
//                    if (project != null && project.getId() == this.project.getId()) {
//                        right_header_btn.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fm = this.getChildFragmentManager();
        detailListFrag = new ManagerDetailListFrag();
        detailListFrag.setProject(project);

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
     * 设置项目
     * @param project
     */
    public void setProject(Project project) {
        this.project = project;
    }

    private void addPerson() {
        detailListFrag.addPerson();
    }
}
