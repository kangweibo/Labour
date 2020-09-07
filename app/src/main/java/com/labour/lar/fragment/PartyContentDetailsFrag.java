package com.labour.lar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.WebChromeClient;
import com.labour.lar.BaseFragment;
import com.labour.lar.R;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;

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
    @BindView(R.id.ly_container)
    LinearLayout ly_container;

    protected AgentWeb mAgentWeb;
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

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(ly_container, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebChromeClient(mWebChromeClient)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .ready()
                .get();

        showHtml(detailHtml);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient(){

        @Override
        public void onShowCustomView(View view, int requestedOrientation,
                                     CustomViewCallback callback) {
            super.onShowCustomView(view, requestedOrientation, callback);
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
        }
    };

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHtml(String detailHtml) {
        this.detailHtml = detailHtml;
    }

    private void showHtml(String detailHtml) {
        mAgentWeb.getUrlLoader().loadDataWithBaseURL(null,detailHtml,
                "text/html", "UTF-8", null);
//        web_container.loadDataWithBaseURL(null,getNewContent(detailHtml),
//                "text/html", "UTF-8", null);
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
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

    /**
     * 将html文本内容中包含img标签的图片，宽度变为屏幕宽度，高度根据宽度比例自适应
     **/
//    public static String getNewContent(String htmltext){
//        try {
//            Document doc= Jsoup.parse(htmltext);
//            Elements elements=doc.getElementsByTag("img");
//            for (Element element : elements) {
//                element.attr("width","100%").attr("height","auto");
//            }
//
//            return doc.toString();
//        } catch (Exception e) {
//            return htmltext;
//        }
//    }
}
