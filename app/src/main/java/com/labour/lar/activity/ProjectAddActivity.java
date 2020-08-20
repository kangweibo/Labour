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
import com.labour.lar.widget.DialogUtil;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建项目
 */
public class ProjectAddActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_budget)
    EditText edt_budget;
    @BindView(R.id.edt_project_time)
    EditText edt_project_time;
    @BindView(R.id.txt_start_time)
    TextView txt_start_time;
    @BindView(R.id.txt_end_time)
    TextView txt_end_time;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_project_add;
    }

    @Override
    public void afterInitLayout() {
        title_tv.setText("创建项目");

        String title = getIntent().getStringExtra("title");
        txt_title.setText(title);
    }

    @OnClick({R.id.back_iv,R.id.btn_submit,R.id.txt_start_time,R.id.txt_end_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.btn_submit:
                submit();
                break;
            case R.id.txt_start_time:
                selectStartTime();
                break;
            case R.id.txt_end_time:
                selectEndTime();
                break;
        }
    }

    public void submit() {
        String name = edt_name.getText().toString();
        String budget = edt_budget.getText().toString();
        String project_time = edt_project_time.getText().toString();
        String start_time = txt_start_time.getText().toString();
        String end_time = txt_end_time.getText().toString();

        if(StringUtils.isBlank(name) || StringUtils.isBlank(budget)
                || StringUtils.isBlank(project_time)|| StringUtils.isBlank(start_time)
                || StringUtils.isBlank(end_time)){
            AppToast.show(this,"请填写完整项目信息！");
            return;
        }

        final Map<String,String> param = new HashMap<>();
        param.put("token","063d91b4f57518ff");
        param.put("name",name);
//        param.put("supervisor",supervisor);
//        param.put("construction",construction);
//        param.put("designer",designer);
//        param.put("projectfunction",projectfunction);
//        param.put("buildaera",buildaera);
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

    public void selectStartTime() {
        DialogUtil.showDateDialog(this,null,
                new DialogUtil.OnDialogListener<Calendar>() {
            @Override
            public void onPositiveButtonClick(Calendar calendar) {
                String time = new SimpleDateFormat("yyyy-MM-dd",
                        Locale.getDefault()).format(calendar.getTime());
                txt_start_time.setText(time);
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
                txt_end_time.setText(time);
            }

            @Override
            public void onNegativeButtonClick(Calendar calendar) {

            }
        });
    }
}
