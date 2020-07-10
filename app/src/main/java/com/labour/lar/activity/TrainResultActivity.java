package com.labour.lar.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.module.Exam;
import com.labour.lar.module.ExamPaper;
import com.labour.lar.module.ExamResult;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 培训成绩
 */
public class TrainResultActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.edt_bankcard_num)
    EditText edt_bankcard_num;
    @BindView(R.id.edt_bankname)
    EditText edt_bankname;
    @BindView(R.id.photo_iv)
    ImageView photo_iv;

    private static final int REQUEST_CODE_CAMERA = 102;

    private List<Exam> examList = new ArrayList<>();
    private List<ExamResult> examresultList = new ArrayList<>();

    private ExamResult examResult;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_train;
    }

    @Override
    public void afterInitLayout() {
        examResult = (ExamResult)getIntent().getSerializableExtra("examResult");
        title_tv.setText("添加银行卡");
    }

    @OnClick({R.id.back_iv,R.id.take_photo_iv,R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.take_photo_iv:

                break;
            case R.id.btn_submit:

                break;
        }
    }

    private void getExamresult(String id) {
        JSONObject param = new JSONObject();
        param.put("token","063d91b4f57518ff");
        param.put("offset",0);
        param.put("limit",10);
        String jsonParams = param.toJSONString();

        String url = Constants.HTTP_BASE + "/api/get_examresult";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){

                    JSONObject jo = jr.getData();
                    ExamPaper examPaper = JSON.parseObject(JSON.toJSONString(jo), ExamPaper.class);

//                    employeeList.clear();
//                    employeeList.addAll(classetams);
//                    showEmployees();
                } else {

                    AppToast.show(TrainResultActivity.this,"获取考试信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(TrainResultActivity.this,"获取考试信息出错!");
            }
        });
    }

}
