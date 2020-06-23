package com.labour.lar.activity;

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
 * 创建项目
 */
public class ProjectAddActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_supervisor)
    EditText edt_supervisor;
    @BindView(R.id.edt_construction)
    EditText edt_construction;
    @BindView(R.id.edt_designer)
    EditText edt_designer;
    @BindView(R.id.edt_projectfunction)
    EditText edt_projectfunction;
    @BindView(R.id.edt_buildaera)
    TextView edt_buildaera;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_project_add;
    }

    @Override
    public void afterInitLayout() {
        title_tv.setText("创建项目");
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

    public void submit() {
        String name = edt_name.getText().toString();
        String supervisor = edt_supervisor.getText().toString();
        String construction = edt_construction.getText().toString();
        String designer = edt_designer.getText().toString();
        String projectfunction = edt_projectfunction.getText().toString();
        String buildaera = edt_buildaera.getText().toString();

        if(StringUtils.isBlank(name) || StringUtils.isBlank(supervisor)
                || StringUtils.isBlank(construction)|| StringUtils.isBlank(designer)
                || StringUtils.isBlank(projectfunction)|| StringUtils.isBlank(buildaera)){
            AppToast.show(this,"请填写完整项目信息！");
            return;
        }

        final Map<String,String> param = new HashMap<>();
        param.put("token","063d91b4f57518ff");
        param.put("name",name);
        param.put("supervisor",supervisor);
        param.put("construction",construction);
        param.put("designer",designer);
        param.put("projectfunction",projectfunction);
        param.put("buildaera",buildaera);
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/project_new";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(ProjectAddActivity.this,"项目创建成功");
                    finish();
                } else {
                    AppToast.show(ProjectAddActivity.this,"项目创建失败");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(ProjectAddActivity.this,"项目创建出错!");
            }
        });
    }
}
