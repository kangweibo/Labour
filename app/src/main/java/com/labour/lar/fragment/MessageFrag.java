package com.labour.lar.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseFragment;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.activity.MessageAddActivity;
import com.labour.lar.adapter.MessageAdapter;
import com.labour.lar.cache.UserCache;
import com.labour.lar.module.FZMessage;
import com.labour.lar.module.User;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.widget.LoadingView;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MessageFrag extends BaseFragment {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;
    @BindView(R.id.right_header_btn)
    TextView right_header_btn;

    @BindView(R.id.list_refresh)
    SmartRefreshLayout list_refresh;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.loading_v)
    LoadingView loadingView;
    @BindView(R.id.noresult_view)
    TextView noresult_view;

    private MessageAdapter messageAdapter;

    List<FZMessage> messageList = new ArrayList<>();

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_message;
    }

    @Override
    public void initView() {
        back_iv.setVisibility(View.INVISIBLE);
        title_tv.setText("消息");
        Drawable d = getResources().getDrawable(R.mipmap.jiahao);
        right_header_btn.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);

        loadingView.setVisibility(View.GONE);
        noresult_view.setVisibility(View.GONE);
        messageAdapter = new MessageAdapter(getContext());
        listView.setAdapter(messageAdapter);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        list_refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });

        messageAdapter.setList(messageList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //startActivity(new Intent(context, ProjectDetailActivity.class));
            }
        });

        getMessages();
    }

    @OnClick({R.id.right_header_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_header_btn:
                addMessage();
                break;

        }
    }

    private void getMessages() {
        UserCache userCache = UserCache.getInstance(getContext());
        User user = userCache.get();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tag",user.getProle());
        jsonObject.put("id",user.getId());
//        jsonObject.put("tag","employee");
//        jsonObject.put("id","28");
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/get_messages";
        ProgressDialog dialog = ProgressDialog.createDialog(this.getContext());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<FZMessage> msgs = JSON.parseArray(JSON.toJSONString(jsonArray), FZMessage.class);

                    messageList.clear();
                    messageList.addAll(msgs);
                    messageAdapter.notifyDataSetChanged();
                } else {
                    AppToast.show(getContext(),"获取消息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getContext(),"获取消息出错!");
            }
        });
    }

    private void addMessage() {
        Intent intent = new Intent(context, MessageAddActivity.class);
        startActivity(intent);
    }
}
