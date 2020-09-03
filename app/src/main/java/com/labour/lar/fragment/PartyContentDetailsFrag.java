package com.labour.lar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.labour.lar.BaseFragment;
import com.labour.lar.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

        web_container.setWebViewClient(new WebViewClient());
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
        web_container.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        web_container.onResume();
        super.onResume();
    }

    /**
     * 将html文本内容中包含img标签的图片，宽度变为屏幕宽度，高度根据宽度比例自适应
     **/
    public static String getNewContent(String htmltext){
        try {
            Document doc= Jsoup.parse(htmltext);
            Elements elements=doc.getElementsByTag("img");
            for (Element element : elements) {
                element.attr("width","100%").attr("height","auto");
            }

            return doc.toString();
        } catch (Exception e) {
            return htmltext;
        }
    }
}
