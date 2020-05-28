package com.labour.lar;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.labour.lar.adapter.FragmentViewPagerAdapter;
import com.labour.lar.fragment.KaoqinFrag;
import com.labour.lar.fragment.MessageFrag;
import com.labour.lar.fragment.MineFrag;
import com.labour.lar.fragment.ProjectFrag;
import com.labour.lar.service.LocationFenceService;
import com.labour.lar.service.LocationService;
import com.labour.lar.widget.MainScrollViewPager;
import com.labour.lar.widget.TabBarView;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.mainTabBarView)
    TabBarView mainTabBarView;
    @BindView(R.id.mainViewpager)
    MainScrollViewPager mainViewpager;

    private ArrayList<Fragment> frgs = new ArrayList<Fragment>();
    private FragmentViewPagerAdapter fragmentPagerAdapter;
    private int DEFAULT_SELECT_TAB = 0;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void afterInitLayout() {
        frgs.add(new ProjectFrag());
        frgs.add(new MessageFrag());
        frgs.add(new MineFrag());
        frgs.add(new KaoqinFrag());

        fragmentPagerAdapter = new FragmentViewPagerAdapter(fm,mainViewpager,frgs);

        mainViewpager.setCanScroll(false);
        mainViewpager.setAdapter(fragmentPagerAdapter);
        mainTabBarView.setViewPager(mainViewpager);
        mainTabBarView.selectedTab(DEFAULT_SELECT_TAB);

        Intent intent = new Intent(this,LocationService.class);
        intent.setComponent(new ComponentName( getPackageName(),"com.labour.lar.service.LocationService"));
        intent.setAction(Constants.LOCATION_SERVICE_ACTION);
        startService(intent);

        Intent fenceIntent = new Intent(this, LocationFenceService.class);
        fenceIntent.setComponent(new ComponentName( getPackageName(),"com.labour.lar.service.LocationFenceService"));
        fenceIntent.setAction(Constants.LOCATION_FENCE_SERVICE_ACTION);
        startService(fenceIntent);
    }
}
