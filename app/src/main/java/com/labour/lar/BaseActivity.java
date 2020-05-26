package com.labour.lar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/7/1 0001.
 */

public abstract class BaseActivity extends FragmentActivity {

    protected FragmentManager fm;
    protected Unbinder unbinder;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        fm = getSupportFragmentManager();
        beforeInitLayout();
        setContentView(getActivityLayoutId());
        unbinder = ButterKnife.bind(this);
        afterInitLayout();

        //http://stackoverflow.com/questions/4341600/how-to-prevent-multiple-instances-of-an-activity-when-it-is-launched-with-differ/
        // should be in launcher activity, but all app use this can avoid the problem
        if(!isTaskRoot()){
            Intent intent = getIntent();
            String action = intent.getAction();
            if(intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)){
                finish();
                return;
            }
        }
    }
    public void beforeInitLayout(){

    }
    public void afterInitLayout(){

    }
    public abstract int getActivityLayoutId();

    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        if(this.isFinishing()){
            unbinder.unbind();
        }
    }
}
