package com.labour.lar.activity;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.AttendanceListAdapter;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.module.Attendance;
import com.labour.lar.module.UserInfo;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.widget.DialogUtil;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 考勤列表
 */
public class AttendanceListActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.right_header_btn)
    TextView right_header_btn;

    @BindView(R.id.list_refresh)
    SmartRefreshLayout list_refresh;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.txt_start_date)
    TextView txt_start_date;
    @BindView(R.id.txt_end_date)
    TextView txt_end_date;
    @BindView(R.id.btn_ok)
    Button btn_ok;
    @BindView(R.id.btn_cancel)
    Button btn_cancel;
    @BindView(R.id.ly_select)
    View ly_select;

    private AttendanceListAdapter attendanceListAdapter;

    private List<Attendance> attendanceList = new ArrayList<>();

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_attendance;
    }

    @Override
    public void afterInitLayout() {
        title_tv.setText("考勤记录");
        ly_select.setVisibility(View.GONE);
        getUserAttendance();
        right_header_btn.setText("查询");

        attendanceListAdapter = new AttendanceListAdapter(this);
        listView.setAdapter(attendanceListAdapter);

        list_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getUserAttendance();
            }
        });
        list_refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

            }
        });
        list_refresh.setEnableLoadMore(false);
        attendanceListAdapter.setList(attendanceList);
        attendanceListAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.back_iv,R.id.right_header_btn,R.id.btn_ok,R.id.btn_cancel,
            R.id.txt_start_date,R.id.txt_end_date})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.right_header_btn:
            case R.id.btn_cancel:
                showSelectView();
                break;
            case R.id.btn_ok:
                search();
                showSelectView();
                break;
            case R.id.txt_start_date:
                selectStartTime();
                break;
            case R.id.txt_end_date:
                selectEndTime();
                break;
        }
    }

    private void showSelectView() {
        if (ly_select.getVisibility() == View.VISIBLE){
            ly_select.setVisibility(View.GONE);
        } else {
            ly_select.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取数据
     */
    private void getUserAttendance() {
        UserInfo userInfo = UserInfoCache.getInstance(this).get();
        if (userInfo == null) {
            return;
        }

        String prole = userInfo.getProle();

        if (prole == null){
            return;
        }

        JSONObject param = new JSONObject();
        param.put("token","063d91b4f57518ff");
        param.put("dtype",prole);
        param.put("userid",userInfo.getId());

        String jsonParams = param.toJSONString();

        String url = Constants.HTTP_BASE + "/api/user_duties";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh();
                list_refresh.finishLoadMore();

                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Attendance> list = JSON.parseArray(JSON.toJSONString(jsonArray), Attendance.class);

                    attendanceList.clear();
                    attendanceList.addAll(list);
                    showData();
                } else {
                    AppToast.show(AttendanceListActivity.this,"获取信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                list_refresh.finishLoadMore(false);
                AppToast.show(AttendanceListActivity.this,"获取信息出错!");
            }
        });
    }

    /**
     * 查询数据
     */
    private void queryUserAttendance(String startdate,String enddate) {
        UserInfo userInfo = UserInfoCache.getInstance(this).get();
        if (userInfo == null) {
            return;
        }

        String prole = userInfo.getProle();

        if (prole == null){
            return;
        }

        JSONObject param = new JSONObject();
        param.put("token","063d91b4f57518ff");
        param.put("startdate",startdate);
        param.put("enddate",enddate);
//        param.put("dtype",prole);
//        param.put("userid",userInfo.getId());
        param.put("dtype","employee");
        param.put("userid","1");
        String jsonParams = param.toJSONString();

        String url = Constants.HTTP_BASE + "/api/query_user_duties";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh();
                list_refresh.finishLoadMore();

                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Attendance> list = JSON.parseArray(JSON.toJSONString(jsonArray), Attendance.class);

                    attendanceList.clear();
                    attendanceList.addAll(list);
                    showData();
                } else {
                    AppToast.show(AttendanceListActivity.this,"获取信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                list_refresh.finishRefresh(false);
                list_refresh.finishLoadMore(false);
                AppToast.show(AttendanceListActivity.this,"获取信息出错!");
            }
        });
    }

    private void showData() {
        attendanceListAdapter.notifyDataSetChanged();
    }

    public void selectStartTime() {
        DialogUtil.showDateDialog(this,null,
                new DialogUtil.OnDialogListener<Calendar>() {
                    @Override
                    public void onPositiveButtonClick(Calendar calendar) {
                        String time = new SimpleDateFormat("yyyy-MM-dd",
                                Locale.getDefault()).format(calendar.getTime());
                        txt_start_date.setText(time);
                    }

                    @Override
                    public void onNegativeButtonClick(Calendar calendar) {

                    }
                });
    }

    public void selectEndTime() {
        DialogUtil.showDateDialog(this,null,
                new DialogUtil.OnDialogListener<Calendar>() {
                    @Override
                    public void onPositiveButtonClick(Calendar calendar) {
                        String time = new SimpleDateFormat("yyyy-MM-dd",
                                Locale.getDefault()).format(calendar.getTime());
                        txt_end_date.setText(time);
                    }

                    @Override
                    public void onNegativeButtonClick(Calendar calendar) {

                    }
                });
    }

    private void search() {
        String startdate = txt_start_date.getText().toString();
        String enddate = txt_end_date.getText().toString();

        if (TextUtils.isEmpty(startdate)){
            AppToast.show(this,"请选择开始日期！");
        }

        if (TextUtils.isEmpty(enddate)){
            AppToast.show(this,"请选择结束日期！");
        }

        queryUserAttendance(startdate, enddate);
    }
}
