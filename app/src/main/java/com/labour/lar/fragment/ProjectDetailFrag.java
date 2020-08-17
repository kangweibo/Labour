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
 * 项目
 */
public class ProjectDetailFrag extends BaseFragment {

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
//    @BindView(R.id.mianji_tv)
//    TextView mianji_tv;
//
//    @BindView(R.id.photo_iv)
//    RoundImageView photo_iv;

    @BindView(R.id.txt_start_date)
    TextView txt_start_date;
    @BindView(R.id.txt_end_date)
    TextView txt_end_date;
    @BindView(R.id.txt_time_scale)
    TextView txt_time_scale;
    @BindView(R.id.txt_number_people)
    TextView txt_number_people;
    @BindView(R.id.txt_work_hours)
    TextView txt_work_hours;
    @BindView(R.id.txt_money)
    TextView txt_money;
    @BindView(R.id.txt_money_total)
    TextView txt_money_total;
    @BindView(R.id.txt_money_scale)
    TextView txt_money_scale;
    
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
        //photo_iv.setImageResource(R.mipmap.picture);

        if (project!= null){
            if (!TextUtils.isEmpty(project.getName())){
                title_tv.setText(project.getName() + "项目详情");
            }
            if (!TextUtils.isEmpty(project.getStartdate())){
                txt_start_date.setText("开工日期：" + project.getStartdate());
            } else {
                txt_start_date.setText("开工日期：无");
            }
            if (!TextUtils.isEmpty(project.getStartdate())){
                txt_start_date.setText("结束日期：" + project.getStartdate());
            } else {
                txt_start_date.setText("结束日期：无");
            }

            if (!TextUtils.isEmpty(project.getEntname())){
                txt_time_scale.setText("比例：" + "86%");
            } else {
                txt_time_scale.setText("比例：");
            }

            if (!TextUtils.isEmpty(project.getBuildaera())){
                txt_number_people.setText("上岗人数：" + project.getOndutynum() + "(" + project.getOnjobnum() + ")");
            } else {
                txt_number_people.setText("上岗人数：");
            }

            if (!TextUtils.isEmpty(project.getBuildaera())){
                txt_work_hours.setText("累计工时：" + project.getBuildaera());
            } else {
                txt_work_hours.setText("累计工时：");
            }

            if (!TextUtils.isEmpty(project.getBuildaera())){
                txt_money.setText("发放总额：" + project.getBuildaera());
            } else {
                txt_money.setText("发放总额：");
            }
            if (!TextUtils.isEmpty(project.getBuildaera())){
                txt_money_total.setText("合同总额：" + project.getBuildaera());
            } else {
                txt_money_total.setText("合同总额：");
            }
            if (!TextUtils.isEmpty(project.getBuildaera())){
                txt_money_scale.setText("发放比例：" + project.getBuildaera());
            } else {
                txt_money_scale.setText("发放比例：");
            }
//            if (!TextUtils.isEmpty(project.getName())){
//                name_tv.setText(project.getName());
//            } else {
//                name_tv.setText("");
//            }

//            if (!TextUtils.isEmpty(project.getEntname())){
//                company_tv.setText(project.getEntname());
//            } else {
//                company_tv.setText("");
//            }
//            if (!TextUtils.isEmpty(project.getStartdate())){
//                type_tv.setText("开工日期：" + project.getStartdate());
//            } else {
//                type_tv.setText("开工日期：无");
//            }
//            if (!TextUtils.isEmpty(project.getAll_num())){
//                mianji_tv.setText("工人总数：" + project.getAll_num() + "人");
//            } else {
//                mianji_tv.setText("工人总数：0人");
//            }
        }

        right_header_btn.setVisibility(View.INVISIBLE);

        User user = UserCache.getInstance(getContext()).get();
        if (user != null) {
            String prole = user.getProle();
            if (prole != null) {
                if (prole.equals("project_manager") || prole.equals("project_quota")) {
                    User.Project project = user.getProject();
                    if (project != null && project.getId() == this.project.getId()) {
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

        projectDetailListFrag = new ProjectDetailListFrag();
        GisMapFrag gisMapFrag = new GisMapFrag();
        projectDetailListFrag.setProject(project);
        gisMapFrag.setProjectId(project.getId()+"");

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
