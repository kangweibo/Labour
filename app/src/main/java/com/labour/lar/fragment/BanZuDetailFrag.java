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
import com.labour.lar.module.Classteam;
import com.labour.lar.widget.RoundImageView;

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
        title_tv.setText("班组");
        Drawable d = getResources().getDrawable(R.mipmap.jiahao);
        right_header_btn.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);

        photo_iv.setImageResource(R.mipmap.picture);

        if (classteam!= null){
            if (!TextUtils.isEmpty(classteam.getName())){
                name_tv.setText(classteam.getName());
            } else {
                name_tv.setText("");
            }
            if (!TextUtils.isEmpty(classteam.getMemo())){
                company_tv.setText("组长：" + classteam.getMemo());
            } else {
                company_tv.setText("组长：");
            }
            if (!TextUtils.isEmpty(classteam.getAll_num())){
                type_tv.setText("花名册：" + classteam.getAll_num() + "人");
            } else {
                type_tv.setText("花名册：0人");
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fm = this.getChildFragmentManager();
        BanZuDetailListFrag banZuDetailListFrag = new BanZuDetailListFrag();
        banZuDetailListFrag.setClassteam(classteam);

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
    @OnClick({R.id.back_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                getActivity().finish();
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
}
