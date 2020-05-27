package com.labour.lar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 */
public abstract class BaseFragment extends Fragment {

    protected Unbinder unbinder;
    protected View mRootView;
    protected Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getFragmentLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        initView();

        return mRootView;
    }

    public abstract int getFragmentLayoutId();
    public abstract void initView();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //
    public void forward(Fragment fragment, boolean hide) {
        forward(getId(), fragment, null, hide);
    }

    public void forward(int viewId, Fragment fragment, String name, boolean hide) {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        if (hide) {
            trans.hide(this);
            trans.add(viewId, fragment);
        } else {
            trans.replace(viewId, fragment);
        }

        trans.addToBackStack(name);
        trans.commitAllowingStateLoss();
    }

    public void backward() {
        getFragmentManager().popBackStack();
    }
}
