package com.labour.lar.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.StringUtils;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建作业队
 */
public class TaskTeamAddActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_duty)
    EditText edt_duty;
    @BindView(R.id.edt_memo)
    EditText edt_memo;

    private int type;// 0：添加；1：更新
    private String operteam_id;
    private String project_id;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_taskteam_add;
    }

    @Override
    public void afterInitLayout() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        operteam_id = intent.getStringExtra("operteam_id");
        project_id = intent.getStringExtra("project_id");

        if (type == 0) {
            title_tv.setText("创建作业队");
        } else {
            title_tv.setText("修改作业队");
        }
    }

    @OnClick({R.id.back_iv,R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    private void submit() {
        if (type == 0){
            addOperteam();
        } else {
            updateOperteam();
        }
    }

    private void addOperteam() {
        String name = edt_name.getText().toString();
        String duty = edt_duty.getText().toString();
        String memo = edt_memo.getText().toString();

        if(StringUtils.isBlank(name) || StringUtils.isBlank(duty)
                || StringUtils.isBlank(memo)){
            AppToast.show(this,"请填写完整作业队信息！");
            return;
        }

        if(StringUtils.isBlank(project_id)){
            AppToast.show(this,"项目id为空！");
            return;
        }

        final Map<String,String> param = new HashMap<>();
        param.put("token","063d91b4f57518ff");
        param.put("project_id",project_id);
        param.put("name",name);
        param.put("duty",duty);
        param.put("memo",memo);
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/operteam_new";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(TaskTeamAddActivity.this,"作业队创建成功");
                    setResult(RESULT_OK, getIntent());
                    finish();
                } else {
                    AppToast.show(TaskTeamAddActivity.this,"作业队创建失败");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(TaskTeamAddActivity.this,"作业队创建出错!");
            }
        });
    }

    private void updateOperteam() {
        String name = edt_name.getText().toString();
        String duty = edt_duty.getText().toString();
        String memo = edt_memo.getText().toString();

        if(StringUtils.isBlank(name) || StringUtils.isBlank(duty)
                || StringUtils.isBlank(memo)){
            AppToast.show(this,"请填写完整作业队信息！");
            return;
        }

        if(StringUtils.isBlank(operteam_id)){
            AppToast.show(this,"作业队id为空！");
            return;
        }

        final Map<String,String> param = new HashMap<>();
        param.put("token","063d91b4f57518ff");
        param.put("id",operteam_id);
        param.put("name",name);
        param.put("duty",duty);
        param.put("memo",memo);
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/operteam_update";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(TaskTeamAddActivity.this,"作业队修改成功");
                    setResult(RESULT_OK, getIntent());
                    finish();
                } else {
                    AppToast.show(TaskTeamAddActivity.this,"作业队修改失败");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(TaskTeamAddActivity.this,"作业队修改出错!");
            }
        });
    }
}
