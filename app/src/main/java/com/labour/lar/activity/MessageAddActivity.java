package com.labour.lar.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.cache.UserCache;
import com.labour.lar.module.User;
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
 * 发送消息
 */
public class MessageAddActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_content)
    EditText edt_content;
    @BindView(R.id.edt_memo)
    EditText edt_memo;

//    private String operteam_id;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_message_add;
    }

    @Override
    public void afterInitLayout() {
        Intent intent = getIntent();
//        operteam_id = intent.getStringExtra("operteam_id");

        title_tv.setText("消息发送");
    }

    @OnClick({R.id.back_iv,R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.btn_submit:
                sendMessage();
                break;
        }
    }


    private void sendMessage() {
        String title = edt_name.getText().toString();
        String content = edt_content.getText().toString();
        String memo = edt_memo.getText().toString();

        if(StringUtils.isBlank(title) || StringUtils.isBlank(content)
                || StringUtils.isBlank(memo)){
            AppToast.show(this,"请填写完整信息！");
            return;
        }

        UserCache userCache = UserCache.getInstance(this);
        User user = userCache.get();

        final Map<String,String> param = new HashMap<>();

        param.put("title",title);
        param.put("content",content);
        param.put("fromtag",user.getProle());
        param.put("fromid",user.getId()+"");
        param.put("totag","");
        param.put("toid","");

        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/send_message";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(MessageAddActivity.this,"消息发送成功");
                    finish();
                } else {
                    AppToast.show(MessageAddActivity.this,"消息发送失败");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(MessageAddActivity.this,"消息发送出错!");
            }
        });
    }
}
