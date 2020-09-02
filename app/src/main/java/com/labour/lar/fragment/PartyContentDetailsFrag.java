package com.labour.lar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.labour.lar.BaseFragment;
import com.labour.lar.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 党建
 */
public class PartyContentDetailsFrag extends BaseFragment {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;
    @BindView(R.id.web_container)
    WebView web_container;

    private String detailHtml;
    private String title;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_party_content_details;
    }

    @Override
    public void initView() {
        if (title != null) {
            title_tv.setText(title);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showHtml(detailHtml);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHtml(String detailHtml) {
        this.detailHtml = detailHtml;
    }

    private void showHtml(String detailHtml) {
        web_container.loadDataWithBaseURL(null,detailHtml,
                "text/html", "UTF-8", null);
    }

    @OnClick({R.id.back_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                getActivity().finish();
                break;

        }
    }

    @Override
    public void onPause() {
        web_container.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        web_container.onResume();
        super.onResume();
    }
}
