package com.labour.lar.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.astuetz.PagerSlidingTabStrip;
import com.labour.lar.BaseFragment;
import com.labour.lar.R;
import com.labour.lar.adapter.MyFragmentPagerAdapter;
import com.labour.lar.cache.UserCache;
import com.labour.lar.module.Classteam;
import com.labour.lar.module.User;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 班组
 */
public class BanZuDetailFrag extends BaseFragment {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;
    @BindView(R.id.right_header_btn)
    TextView right_header_btn;

    @BindView(R.id.ly_secret)
    View ly_secret;
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

    private BanZuDetailListFrag banZuDetailListFrag;
    private MyFragmentPagerAdapter fragmentPagerAdapter;
    private ArrayList<Fragment> frgs = new ArrayList<Fragment>();
    FragmentManager fm;
    String[] titles  = {"劳务管理","地图围栏"};

    private String project_id;
    private Classteam classteam;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_banzu_detail;
    }

    @Override
    public void initView() {
        title_tv.setText("班组详情");
        Drawable d = getResources().getDrawable(R.mipmap.jiahao);
        right_header_btn.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);

        if (classteam!= null){
            title_tv.setText(classteam.getName() + "的详情");
            txt_number_people.setText("上岗人数：" + classteam.getOndutynum() + "(" + classteam.getOnjobnum() + ")");
            txt_work_hours.setText("累计工时：" + classteam.getTotalworkday());
            txt_money.setText("发放总额：" + classteam.getTotalsalary());
            txt_pm.setText("班组长：" + classteam.getPm());
        }

        right_header_btn.setVisibility(View.INVISIBLE);
        ly_secret.setVisibility(View.GONE);

        User user = UserCache.getInstance(getContext()).get();
        if (user != null) {
            String prole = user.getProle();
            if (prole != null){
                if (prole.equals("ent_manager")
                        || prole.equals("project_manager") || prole.equals("project_quota")
                        || prole.equals("operteam_manager") || prole.equals("operteam_quota")
                        || prole.equals("classteam_manager")) {
                    User.Project project = user.getProject();
                    if (project != null && String.valueOf(project.getId()).equals(project_id)) {
                        ly_secret.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fm = this.getChildFragmentManager();
        banZuDetailListFrag = new BanZuDetailListFrag();
        banZuDetailListFrag.setClassteam(classteam);
        banZuDetailListFrag.setProjectId(project_id);

        GisMapFrag gisMapFrag = new GisMapFrag();
        gisMapFrag.setProjectId(project_id);

        frgs.add(banZuDetailListFrag);
        frgs.add(gisMapFrag);
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
     * 设置班组
     * @param classteam
     */
    public void setClassteam(Classteam classteam) {
        this.classteam = classteam;
    }

    public void setProjectId(String project_id){
        this.project_id = project_id;
    }

    public void addPerson() {
        banZuDetailListFrag.addPerson();
    }
}
