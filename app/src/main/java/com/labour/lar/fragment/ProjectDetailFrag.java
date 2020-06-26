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
import com.labour.lar.module.Project;
import com.labour.lar.widget.RoundImageView;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 项目
 */
public class ProjectDetailFrag extends BaseFragment {

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
    @BindView(R.id.mianji_tv)
    TextView mianji_tv;

    @BindView(R.id.photo_iv)
    RoundImageView photo_iv;

    @BindView(R.id.psts_indicator)
    PagerSlidingTabStrip pstsIndicator;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    private MyFragmentPagerAdapter fragmentPagerAdapter;
    private ArrayList<Fragment> frgs = new ArrayList<Fragment>();
    FragmentManager fm;
    String[] titles  = {"劳务管理","地图围栏","项目进度"};

    ProjectDetailListFrag projectDetailListFrag;

    private Project project;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_project_detail;
    }

    @Override
    public void initView() {
        title_tv.setText("项目详情");
        Drawable d = getResources().getDrawable(R.mipmap.jiahao);
        right_header_btn.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);
        photo_iv.setImageResource(R.mipmap.picture);

        if (project!= null){
            if (!TextUtils.isEmpty(project.getName())){
                name_tv.setText(project.getName());
            } else {
                name_tv.setText("");
            }
            if (!TextUtils.isEmpty(project.getSupervisor())){
                company_tv.setText(project.getSupervisor());
            } else {
                company_tv.setText("");
            }
            if (!TextUtils.isEmpty(project.getProjectfunction())){
                type_tv.setText(project.getProjectfunction());
            } else {
                type_tv.setText("");
            }
            if (!TextUtils.isEmpty(project.getBuildaera())){
                mianji_tv.setText(project.getBuildaera());
            } else {
                mianji_tv.setText("");
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fm = this.getChildFragmentManager();

        projectDetailListFrag = new ProjectDetailListFrag();
        GisMapFrag gisMapFrag = new GisMapFrag();
        projectDetailListFrag.setProject(project);
        gisMapFrag.setProject(project);

        frgs.add(projectDetailListFrag);
        frgs.add(gisMapFrag);
        frgs.add(new ProjectDetailProgressFrag());
        fragmentPagerAdapter = new MyFragmentPagerAdapter(fm,titles,frgs);
        vpContent.setAdapter(fragmentPagerAdapter);

        pstsIndicator.setIndicatorColor(getResources().getColor(R.color.common_blue));
        pstsIndicator.setDividerColor(getResources().getColor(R.color.transparent));
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
    @OnClick({R.id.back_iv,R.id.right_header_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                getActivity().finish();
                break;
            case R.id.right_header_btn:
                addOperteam();
                break;

        }
    }

    public void setProject(Project project){
        this.project = project;
    }

    private void addOperteam() {
        if (projectDetailListFrag != null){
            projectDetailListFrag.addOperteam();
        }
    }
}
