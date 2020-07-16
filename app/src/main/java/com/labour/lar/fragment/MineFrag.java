package com.labour.lar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.labour.lar.BaseApplication;
import com.labour.lar.BaseFragment;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.activity.BankcardAddActivity;
import com.labour.lar.activity.ClockInActivity;
import com.labour.lar.activity.IdentifiedActivity;
import com.labour.lar.activity.InferiorsActivity;
import com.labour.lar.activity.MyInfoActivity;
import com.labour.lar.activity.SettingActivity;
import com.labour.lar.activity.ShowQRCodeActivity;
import com.labour.lar.activity.TrainActivity;
import com.labour.lar.adapter.MineGridViewAdapter;
import com.labour.lar.cache.UserCache;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.event.BaseEvent;
import com.labour.lar.event.EventManager;
import com.labour.lar.module.Employee;
import com.labour.lar.module.User;
import com.labour.lar.module.UserInfo;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.widget.NoScrollGridView;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.RoundImageView;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class MineFrag extends BaseFragment {

    private int REQUEST_CODE = 123;
    private int REQUEST_CODE_Identified = 124;
    private int REQUEST_CODE_Bankcard = 125;
    private int REQUEST_CODE_ClockIn = 126;

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;
    @BindView(R.id.right_header_btn)
    TextView right_header_btn;
    @BindView(R.id.identified_tv)
    TextView identified_tv;
    @BindView(R.id.name_tv)
    TextView name_tv;
    @BindView(R.id.brief_tv)
    TextView brief_tv;
    @BindView(R.id.status_tv)
    TextView status_tv;

    @BindView(R.id.main_gridview)
    NoScrollGridView main_gridview;

    @BindView(R.id.photo_iv)
    RoundImageView photo_iv;

    MineGridViewAdapter mineGridViewAdapter;
    List<Integer> imgList = new ArrayList();
    List<String> list = new ArrayList();

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_mine;
    }

    @Override
    public void initView() {
        identified_tv.setVisibility(View.GONE);
        back_iv.setVisibility(View.INVISIBLE);
        title_tv.setText("我的");

        refreshUserInfo();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mineGridViewAdapter = new MineGridViewAdapter(context);
        initData(mineGridViewAdapter);
        main_gridview.setAdapter(mineGridViewAdapter);
        main_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = list.get(position);
                if(item.equals("个人信息")){
                    startActivity(new Intent(context, MyInfoActivity.class));
                } else if(item.equals("工程项目")){
                    showProjectFrag();
                } else if(item.equals("加入班组") || item.equals("加入作业队") || item.equals("加入项目部")){
                    showClassteam();
                } else if(item.equals("银行卡管理")){
                    startActivity(new Intent(context, BankcardAddActivity.class));
                } else if(item.equals("代员工身份验证")){
                    Intent intent = new Intent(context, InferiorsActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_Identified);
                } else if(item.equals("代员工银行卡认证")){
                    Intent intent = new Intent(context, InferiorsActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_Bankcard);
                } else if(item.equals("代员工打卡")){
                    Intent intent = new Intent(context, InferiorsActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_ClockIn);
                } else if(item.equals("岗前安全培训")){
                    startActivity(new Intent(context, TrainActivity.class));
                } else if(item.equals("设置")){
                    startActivity(new Intent(context, SettingActivity.class));
                }
            }
        });
    }
    @OnClick({R.id.identified_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.identified_tv:
                identified();
                break;
        }
    }

    // 刷新用户界面
    private void refreshUserInfo() {
        UserInfo userInfo = UserInfoCache.getInstance(getContext()).get();
        if (userInfo == null) {
            return;
        }

        if (!TextUtils.isEmpty(userInfo.getPic())) {
            Glide.with(BaseApplication.getInstance()).load(Constants.HTTP_BASE + userInfo.getPic()).into(photo_iv);
        }

        if (!userInfo.isIdentified()){
            identified_tv.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(userInfo.getName())){
            name_tv.setText(userInfo.getName());
        }

        if (!TextUtils.isEmpty(userInfo.getStatus()) && userInfo.getStatus().equals("通过")){
            status_tv.setText("（已上岗）");
        } else {
            status_tv.setText("（未上岗）");
        }

        if (!TextUtils.isEmpty(userInfo.getPhone())){
            brief_tv.setText(userInfo.getPhone());
        }
    }

    private void showProjectFrag() {
        BaseEvent event = new BaseEvent();
        event.setCode(1);
        event.setPosition(0);
        EventManager.post(event);
    }

    private void showMessageFrag() {
        BaseEvent event = new BaseEvent();
        event.setCode(1);
        event.setPosition(1);
        EventManager.post(event);
    }

    private void initData(MineGridViewAdapter mineGridViewAdapter) {
//        int [] imgs = {R.mipmap.personal_icon,R.mipmap.xiangmu_icon,R.mipmap.kaoqinbaobiao_icon,
//                R.mipmap.xiaoxitongzhi_icon, R.mipmap.seting_icon,
//                R.mipmap.xiangmu_icon,R.mipmap.personal_icon,R.mipmap.personal_icon,
//                R.mipmap.kaoqinbaobiao_icon,R.mipmap.personal_icon
//        };
//        String[] strs = {"个人信息","工程项目","考勤报表","加入班组","设置","银行卡管理",
//                "代员工身份验证","代员工银行卡认证","代员工打卡","岗前安全培训"};

        list.add("个人信息");
        imgList.add(R.mipmap.userinfo_icon);
        list.add("工程项目");
        imgList.add(R.mipmap.tab_home_checked);
//        list.add("考勤报表");
//        imgList.add(R.mipmap.kaoqinbaobiao_icon);
        list.add("银行卡管理");
        imgList.add(R.mipmap.bankcard_icon);

        UserInfo userInfo = UserInfoCache.getInstance(getContext()).get();
        String prole = userInfo.getProle();

        if (prole.equals("classteam_manager") || prole.equals("employee")){
            list.add("加入班组");
            imgList.add(R.mipmap.team_icon);
        } else if (prole.equals("operteam_manager") || prole.equals("operteam_quota") || prole.equals("staff")){
            list.add("加入作业队");
            imgList.add(R.mipmap.team_icon);
        } else if (prole.equals("project_manager") || prole.equals("project_quota") || prole.equals("manager")){
            list.add("加入项目部");
            imgList.add(R.mipmap.team_icon);
        }

        if (prole.equals("classteam_manager") || prole.equals("operteam_manager")
                || prole.equals("project_manager")){
            list.add("代员工身份验证");
            imgList.add(R.mipmap.idcard_icon);
            list.add("代员工银行卡认证");
            imgList.add(R.mipmap.bankcard_icon);
            list.add("代员工打卡");
            imgList.add(R.mipmap.tab_kaoqin_checked);
        }

        list.add("岗前安全培训");
        imgList.add(R.mipmap.test_icon);
        list.add("设置");
        imgList.add(R.mipmap.seting2_icon);

        String[] strs = list.toArray(new String[list.size()]);
        Integer[] imgs = imgList.toArray(new Integer[imgList.size()]);

        mineGridViewAdapter.setImgs(imgs);
        mineGridViewAdapter.setStrings(strs);
    }

    private void showClassteam() {
        UserInfo userInfo = UserInfoCache.getInstance(getContext()).get();
        if (userInfo == null) {
            return;
        }

        String prole = userInfo.getProle();

        if (prole.equals("classteam_manager")
                || prole.equals("operteam_manager") || prole.equals("operteam_quota")
                || prole.equals("project_manager") || prole.equals("project_quota") ){
            showQRCode(prole);
        } else {
            showScan();
        }
    }

    // 扫码
    private void showScan() {
        Intent intent = new Intent(getContext(), CaptureActivity.class);
//        ZxingConfig config = new ZxingConfig();
//        config.setPlayBeep(true);//是否播放扫描声音 默认为true
//        config.setShake(true);//是否震动  默认为true
//        config.setDecodeBarCode(true);//是否扫描条形码 默认为true
//        config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
//        config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
//        config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
//        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
//        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void showQRCode(String prole) {
        String id = "";
        String title = "";

        JSONObject jsonObject = new JSONObject();

        if (prole.equals("classteam_manager")){
            title = "加入班组";
            jsonObject.put("classteam_id", id);
        } else if (prole.equals("operteam_manager") || prole.equals("operteam_quota")){
            title = "加入作业队";
            jsonObject.put("operteam_id", id);
        } else if (prole.equals("project_manager") || prole.equals("project_quota")){
            title = "加入项目部";
            jsonObject.put("project_id", id);
        }

        String content = jsonObject.toJSONString();
        Intent intent = new Intent(getContext(), ShowQRCodeActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                //AppToast.show(getContext(),"扫描结果为：" + content);

                try {
                    JSONObject jsonObject = JSON.parseObject(content);
                    String classteam_id = jsonObject.getString("classteam_id");
                    String operteam_id = jsonObject.getString("operteam_id");
                    String project_id = jsonObject.getString("project_id");

                    if (!TextUtils.isEmpty(classteam_id)){
                        addClassteam(classteam_id);
                    } else if (!TextUtils.isEmpty(operteam_id)){
                        addOperteam(operteam_id);
                    } else if (!TextUtils.isEmpty(project_id)){
                        addProject(project_id);
                    }
                } catch (RuntimeException e) {
                    AppToast.show(getContext(),"二维码格式出错");
                }
            }
        }

        if (requestCode == Constants.RELOAD && resultCode == RESULT_OK) {
            refreshUserInfo();
        }

        // 代身份认证
        if (requestCode == REQUEST_CODE_Identified && resultCode == RESULT_OK) {
            if (data != null) {
                Employee employee = (Employee)data.getSerializableExtra("employee");
                if (employee != null) {
                    subIdentified(employee);
                }
            }
        }
        // 代银行卡认证
        if (requestCode == REQUEST_CODE_Bankcard && resultCode == RESULT_OK) {
            if (data != null) {
                Employee employee = (Employee)data.getSerializableExtra("employee");
                if (employee != null) {
                    subBankcard(employee);
                }
            }
        }
        // 代打卡
        if (requestCode == REQUEST_CODE_ClockIn && resultCode == RESULT_OK) {
            if (data != null) {
                Employee employee = (Employee)data.getSerializableExtra("employee");
                if (employee != null) {
                    subClockIn(employee);
                }
            }
        }
    }

    // 工人加入班组
    private void addClassteam(String classteam_id) {
        UserCache userCache = UserCache.getInstance(getContext());
        User user = userCache.get();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("prole",user.getProle());
        jsonObject.put("userid",user.getId());
        jsonObject.put("classteam_id",classteam_id);

        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/join_classteam";
        ProgressDialog dialog = ProgressDialog.createDialog(this.getContext());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(getContext(),"加入班组成功!");
                } else {
                    AppToast.show(getContext(),"加入班组失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getContext(),"加入班组出错!");
            }
        });
    }

    // 加入作业队
    private void addOperteam(String operteam_id) {
        UserCache userCache = UserCache.getInstance(getContext());
        User user = userCache.get();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("prole",user.getProle());
        jsonObject.put("userid",user.getId());
        jsonObject.put("operteam_id",operteam_id);

        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/join_operteam";
        ProgressDialog dialog = ProgressDialog.createDialog(this.getContext());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(getContext(),"加入作业队成功!");
                } else {
                    AppToast.show(getContext(),"加入作业队失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getContext(),"加入作业队出错!");
            }
        });
    }

    // 加入项目部
    private void addProject(String project_id) {
        UserCache userCache = UserCache.getInstance(getContext());
        User user = userCache.get();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("prole",user.getProle());
        jsonObject.put("userid",user.getId());
        jsonObject.put("project_id",project_id);

        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/join_project";
        ProgressDialog dialog = ProgressDialog.createDialog(this.getContext());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(getContext(),"加入项目成功!");
                } else {
                    AppToast.show(getContext(),"加入项目失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getContext(),"加入项目出错!");
            }
        });
    }

    // 身份认证
    private void identified() {
        Intent intent = new Intent(getContext(), IdentifiedActivity.class);
        startActivityForResult(intent, Constants.RELOAD);
    }

    // 代身份认证
    private void subIdentified(Employee employee) {
        User user = new User();
        user.setId(employee.getId());
        user.setProle(employee.getProle());
        user.setName(employee.getName());

        Intent intent = new Intent(context, IdentifiedActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    // 代银行卡认证
    private void subBankcard(Employee employee) {
        User user = new User();
        user.setId(employee.getId());
        user.setProle(employee.getProle());
        user.setName(employee.getName());

        Intent intent = new Intent(context, BankcardAddActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    // 代打卡
    private void subClockIn(Employee employee) {
        UserInfo user = new UserInfo();
        user.setId(employee.getId());
        user.setProle(employee.getProle());
        user.setName(employee.getName());
        user.setIdentified(employee.isIdentified());

        Intent intent = new Intent(context, ClockInActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
