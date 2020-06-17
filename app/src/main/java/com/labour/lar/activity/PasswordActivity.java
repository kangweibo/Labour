package com.labour.lar.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.labour.lar.BaseActivity;
import com.labour.lar.R;
import com.labour.lar.util.StringUtils;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改密码
 */
public class PasswordActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;
    @BindView(R.id.edt_old_password)
    EditText edt_old_password;
    @BindView(R.id.edt_new_password)
    EditText edt_new_password;
    @BindView(R.id.edt_re_password)
    EditText edt_re_password;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_password;
    }
    @Override
    public void afterInitLayout(){
        title_tv.setText("修改登录密码");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag("request_tag");
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
        String old_password = edt_old_password.getText().toString();
        String new_password = edt_new_password.getText().toString();
        String re_password = edt_re_password.getText().toString();

        if(StringUtils.isBlank(old_password) || StringUtils.isBlank(new_password) || StringUtils.isBlank(re_password)){
            AppToast.show(this,"请填写完整信息！");
            return;
        }

        if(!new_password.equals(re_password)){
            AppToast.show(this,"两次输入密码不一致！");
            return;
        }

//        final Map<String,String> param = new HashMap<>();
//        param.put("name",name);
//        param.put("passwd",password);
//        param.put("gender",sex);
//        param.put("phone",phone);
//        param.put("role",role);
//        String jsonParams = JSON.toJSONString(param);
//
//        String url = Constants.HTTP_BASE + "/api/register";
//        ProgressDialog dialog = ProgressDialog.createDialog(this);
//        dialog.show();
//
//        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
//            @Override
//            public void onSuccess(Response<String> response) {
//                dialog.dismiss();
//                AjaxResult jr = new AjaxResult(response.body());
//                if(jr.getSuccess() == 1){
//                    JSONObject jo = jr.getData();
//                    User ub = JSON.parseObject(JSON.toJSONString(jo), User.class);
//                    UserCache userCache = UserCache.getInstance(PasswordActivity.this);
//                    userCache.put(ub);
//                    startActivity(new Intent(PasswordActivity.this,MainActivity.class));
//                    finish();
//                } else {
//                    AppToast.show(PasswordActivity.this,jr.getMsg());
//                }
//            }
//            @Override
//            public void onError(Response<String> response) {
//                dialog.dismiss();
//                AppToast.show(PasswordActivity.this,"提交出错!");
//            }
//        });
    }
}
