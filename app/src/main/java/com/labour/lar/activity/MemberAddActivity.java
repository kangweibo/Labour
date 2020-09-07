package com.labour.lar.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.module.Employee;
import com.labour.lar.module.UserInfo;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.StringUtils;
import com.labour.lar.widget.BottomListDialog;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加人员
 */
public class MemberAddActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_duty)
    EditText edt_duty;
    @BindView(R.id.edt_phone)
    EditText edt_phone;
//    @BindView(R.id.edt_password)
//    EditText edt_password;
    @BindView(R.id.txt_prole)
    TextView txt_prole;

    private int type;// 0：项目；1：作业队；2：班组
    private int state;// 0：添加；1：修改
    private String org_id;

    private String member_id;
    private Employee person;

    private List<String> proles = new ArrayList<>();
    private Map<String, String> prolesMap = new HashMap<>();

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_member_add;
    }

    @Override
    public void afterInitLayout() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        state = intent.getIntExtra("state", 0);
        String title = intent.getStringExtra("title");
        org_id = intent.getStringExtra("id");

        txt_title.setText(title);
        String strState, strType;
        if (state == 0) {
            strState = "添加";
        } else {
            strState = "修改";

            person = (Employee)intent.getSerializableExtra("member");
            if (person != null) {
                member_id = person.getId()+"";
                edt_name.setText(person.getName());
                edt_duty.setText(person.getDuty());
                edt_phone.setText(person.getPhone());
                txt_prole.setText(person.getProlename());
            }
        }

        if (type == 0) {
            strType = "项目成员";
        } else if (type == 1){
            strType = "作业队成员";
        } else {
            strType = "班组成员";
        }

        title_tv.setText(strState + strType);

        initData();
    }

    @OnClick({R.id.back_iv,R.id.txt_prole,R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.txt_prole:
                showMoreDialog();
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    private void submit() {
        if (state == 0) {
            addPerson();
        } else {
            updatePerson();
        }
    }

    private void initData() {
        UserInfo userInfo = UserInfoCache.getInstance(this).get();
        if (userInfo == null) {
            return;
        }

        String prole = userInfo.getProle();

        if (prole == null){
            return;
        }

        if (type == 0) {
            proles.add("项目经理");
            proles.add("项目定额员");
            proles.add("项目成员");

            prolesMap.put("项目经理", "project_manager");
            prolesMap.put("项目定额员", "project_quota");
            prolesMap.put("项目成员", "manager");
        } else if (type == 1){
            if (prole.equals("project_manager") || prole.equals("project_quota")){
                proles.add("作业队长");
                proles.add("作业队定额员");
                proles.add("作业队成员");
            }

            prolesMap.put("作业队长", "operteam_manager");
            prolesMap.put("作业队定额员", "operteam_quota");
            prolesMap.put("作业队成员", "staff");
        } else {
            if (prole.equals("operteam_manager") || prole.equals("operteam_quota")){
                proles.add("班组长");
                proles.add("工人");
            }

            prolesMap.put("班组长", "classteam_manager");
            prolesMap.put("工人", "employee");
        }
    }

    private void showMoreDialog(){
        BottomListDialog dialog = new BottomListDialog(this, "请选择角色", proles,
                new BottomListDialog.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String prole = proles.get(position);
                txt_prole.setText(prole);
            }
        });

        dialog.showAtLocation(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void addPerson() {
        String name = edt_name.getText().toString();
        String duty = edt_duty.getText().toString();
        String phone = edt_phone.getText().toString();
//        String passwd = edt_password.getText().toString();
        String prole = txt_prole.getText().toString();
        String realProle = prolesMap.get(prole);

        if(StringUtils.isBlank(name) || StringUtils.isBlank(duty) || StringUtils.isBlank(phone)
                || StringUtils.isBlank(prole)){
            AppToast.show(this,"请填写完整人员信息！");
            return;
        }

        if(StringUtils.isBlank(realProle)){
            AppToast.show(this,"角色出错！");
            return;
        }

        final Map<String,String> param = new HashMap<>();
        param.put("token","063d91b4f57518ff");
        param.put("name",name);
        param.put("duty",duty);
        param.put("phone",phone);
//        param.put("passwd",passwd);
        param.put("prole",realProle);

        String api;
        if (type == 0){
            api = "/api/manager_new";
            param.put("project_id",org_id);
        } else if (type == 1){
            api = "/api/staff_new";
            param.put("operteam_id",org_id);
        } else {
            api = "/api/employee_new";
            param.put("classteam_id",org_id);
        }

        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + api;
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(MemberAddActivity.this,"人员录入成功");
                    setResult(RESULT_OK, getIntent());
                    finish();
                } else {
                    AppToast.show(MemberAddActivity.this,"人员录入失败");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(MemberAddActivity.this,"人员录入出错!");
            }
        });
    }

    private void updatePerson() {
        String name = edt_name.getText().toString();
        String duty = edt_duty.getText().toString();
        String phone = edt_phone.getText().toString();
//        String passwd = edt_password.getText().toString();
        String prole = txt_prole.getText().toString();
        String realProle = prolesMap.get(prole);

        if(StringUtils.isBlank(name) || StringUtils.isBlank(duty) || StringUtils.isBlank(phone)
                || StringUtils.isBlank(prole)){
            AppToast.show(this,"请填写完整人员信息！");
            return;
        }

        if(StringUtils.isBlank(realProle)){
            AppToast.show(this,"角色出错！");
            return;
        }

        final Map<String,String> param = new HashMap<>();
        param.put("token","063d91b4f57518ff");
        param.put("name",name);
        param.put("duty",duty);
        param.put("phone",phone);
//        param.put("passwd",passwd);
        param.put("prole",realProle);

        String api;
        if (type == 0){
            api = "/api/manager_update";
            param.put("project_id",org_id);
            param.put("manager_id",member_id);
        } else if (type == 1){
            api = "/api/staff_update";
            param.put("operteam_id",org_id);
            param.put("staff_id",member_id);
        } else {
            api = "/api/employee_update";
            param.put("classteam_id",org_id);
            param.put("employee_id",member_id);
        }

        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + api;
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(MemberAddActivity.this,"人员修改成功");
                    setResult(RESULT_OK, getIntent());
                    finish();
                } else {
                    AppToast.show(MemberAddActivity.this,"人员修改失败");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(MemberAddActivity.this,"人员修改出错!");
            }
        });
    }
}
