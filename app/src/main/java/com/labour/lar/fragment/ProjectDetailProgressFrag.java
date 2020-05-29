package com.labour.lar.fragment;

import android.widget.ListView;
import android.widget.TextView;

import com.labour.lar.BaseFragment;
import com.labour.lar.R;
import com.labour.lar.adapter.TraceListAdapter;
import com.labour.lar.module.Trace;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 项目进度
 */
public class ProjectDetailProgressFrag extends BaseFragment {

    @BindView(R.id.lvTrace)
    ListView lvTrace;
    private List<Trace> traceList = new ArrayList<>(10);
    private TraceListAdapter adapter;
    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_project_detail_prog;
    }

    @Override
    public void initView() {
        initData();
    }
    private void initData() {
        // 模拟一些假的数据
        traceList.add(new Trace("2016-05-25 17:48:00", "[沈阳市] [沈阳和平五部]的派件已签收 感谢使用中通快递,期待再次为您服务!"));
        traceList.add(new Trace("2016-05-25 14:13:00", "[沈阳市] [沈阳和平五部]的东北大学代理点正在派件 电话:18040xxxxxx 请保持电话畅通、耐心等待"));
        traceList.add(new Trace("2016-05-25 13:01:04", "[沈阳市] 快件到达 [沈阳和平五部]"));
        traceList.add(new Trace("2016-05-25 12:19:47", "[沈阳市] 快件离开 [沈阳中转]已发往[沈阳和平五部]"));
        traceList.add(new Trace("2016-05-25 11:12:44", "[沈阳市] 快件到达 [沈阳中转]"));
        traceList.add(new Trace("2016-05-24 03:12:12", "[嘉兴市] 快件离开 [杭州中转部]已发往[沈阳中转]"));
        traceList.add(new Trace("2016-05-23 21:06:46", "[杭州市] 快件到达 [杭州汽运部]"));
        traceList.add(new Trace("2016-05-23 18:59:41", "[杭州市] 快件离开 [杭州乔司区]已发往[沈阳]"));
        traceList.add(new Trace("2016-05-23 18:35:32", "[杭州市] [杭州乔司区]的市场部已收件 电话:18358xxxxxx"));
        adapter = new TraceListAdapter(getContext(), traceList);
        lvTrace.setAdapter(adapter);
    }
}
