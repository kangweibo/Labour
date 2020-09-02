package com.labour.lar.fragment;

import android.content.Intent;
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
import com.labour.lar.activity.PartyContentDetailActivity;
import com.labour.lar.adapter.MyGridViewAdapter;
import com.labour.lar.adapter.PartytypeAdapter;
import com.labour.lar.module.Parties;
import com.labour.lar.module.Partytype;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.widget.NoScrollGridView;
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

/**
 * 党群建设
 */
public class PartyMassBuildFrag extends BaseFragment {

    @BindView(R.id.main_gridview)
    NoScrollGridView main_gridview;

    @BindView(R.id.list_refresh)
    SmartRefreshLayout list_refresh;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.ly_title)
    View ly_title;

    private MyGridViewAdapter mineGridViewAdapter;
    private List<Integer> imgList = new ArrayList();
    private List<String> titleList = new ArrayList();

    private List<Partytype> partytypeList= new ArrayList();

    private PartytypeAdapter partytypeAdapter;
    private List<Parties> partiesList = new ArrayList<>();
    private List<PartytypeAdapter.ListItem> list = new ArrayList<>();;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_party_mass_build;
    }

    @Override
    public void initView() {
        partytypeAdapter = new PartytypeAdapter(getContext());
        listView.setAdapter(partytypeAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mineGridViewAdapter = new MyGridViewAdapter(context);
        main_gridview.setAdapter(mineGridViewAdapter);
        main_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPartytypes(position);
            }
        });

        getPartytypes();

        list_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

            }
        });
        list_refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
        //list_refresh.setEnableRefresh(false);
        list_refresh.setEnableLoadMore(false);

        partytypeAdapter.setList(list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Parties parties = partiesList.get(position);
                Intent intent = new Intent(getContext(), PartyContentDetailActivity.class);
                intent.putExtra("id", parties.getId());
                intent.putExtra("type", 0);
                startActivity(intent);
            }
        });
    }

    private void initData(List<Partytype> partytypes) {
        for (Partytype partytype : partytypes) {
            titleList.add(partytype.getName());
            if (partytype.getName().contains("党")){
                imgList.add(R.mipmap.tab_dj_checked);
            } else {
                imgList.add(R.mipmap.thought_icon);
            }
        }

        String[] strs = titleList.toArray(new String[titleList.size()]);
        Integer[] imgs = imgList.toArray(new Integer[imgList.size()]);

        mineGridViewAdapter.setImgs(imgs);
        mineGridViewAdapter.setStrings(strs);
        mineGridViewAdapter.notifyDataSetChanged();

        if (partytypes.size() > 0) {
            showPartytypes(0);
            ly_title.setVisibility(View.VISIBLE);
        } else {
            ly_title.setVisibility(View.GONE);
        }
    }


    private void showPartytypes(int position) {
        Partytype partytype = partytypeList.get(position);
        txt_title.setText(partytype.getName());
        getParties(partytype.getId());
    }

    private void getPartytypes() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token","063d91b4f57518ff");

        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/partytypes";
        ProgressDialog dialog = ProgressDialog.createDialog(this.getContext());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Partytype> partytypes = JSON.parseArray(JSON.toJSONString(jsonArray), Partytype.class);
                    partytypeList.clear();
                    partytypeList.addAll(partytypes);
                    initData(partytypeList);
                } else {
                    AppToast.show(getContext(),"获取信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getContext(),"获取信息出错!");
            }
        });
    }

    private void getParties(int typeid) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token","063d91b4f57518ff");
        jsonObject.put("typeid",typeid);
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/parties";
        ProgressDialog dialog = ProgressDialog.createDialog(this.getContext());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    list_refresh.finishRefresh(true);

                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Parties> parties = JSON.parseArray(JSON.toJSONString(jsonArray), Parties.class);

                    partiesList.clear();
                    partiesList.addAll(parties);
                    showParties();
                } else {
                    list_refresh.finishRefresh(false);
                    AppToast.show(getContext(),"获取信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                list_refresh.finishRefresh(false);
                dialog.dismiss();
                AppToast.show(getContext(),"获取信息出错!");
            }
        });
    }

    private void showParties() {
        list.clear();

        for(Parties parties : partiesList){
            PartytypeAdapter.ListItem item = new PartytypeAdapter.ListItem();
            item.title = parties.getTitle();
            item.content =  parties.getUpdated_at();

            item.type = 1;
            list.add(item);
        }

        partytypeAdapter.notifyDataSetChanged();
    }
}
