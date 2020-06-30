package com.labour.lar.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.SimpleTreeAdapter;
import com.labour.lar.cache.UserCache;
import com.labour.lar.module.Employee;
import com.labour.lar.module.User;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.StringUtils;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.multilevel.treelist.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 联系人
 */
public class LinkmanActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.lv_tree)
    ListView lv_tree;
    @BindView(R.id.edt_content)
    EditText edt_content;
    @BindView(R.id.edt_memo)
    EditText edt_memo;

    private String project_id;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_linkman;
    }

    @Override
    public void afterInitLayout() {
        Intent intent = getIntent();
        project_id = intent.getStringExtra("project_id");

        title_tv.setText("联系人");

        List<Node> mlist = new ArrayList<>();
        SimpleTreeAdapter mAdapter = new SimpleTreeAdapter(lv_tree, this,
                mlist, 1,R.mipmap.tree_ex,R.mipmap.tree_ec);

        lv_tree.setAdapter(mAdapter);

        mlist.add(new Node("223","0","我也是添加的root节点",new Employee()));
        mAdapter.addData(0,mlist);
    }

    @OnClick({R.id.back_iv,R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.btn_submit:
                //sendMessage();
                break;
        }
    }


    private void getOrgTree() {
        if(StringUtils.isBlank(project_id) ){
            AppToast.show(this,"项目ID为空！");
            return;
        }

        UserCache userCache = UserCache.getInstance(this);
        User user = userCache.get();

        final Map<String,String> param = new HashMap<>();

        param.put("id",project_id);
        param.put("token","063d91b4f57518ff");

        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/org_tree";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(LinkmanActivity.this,"消息发送成功");
                    finish();
                } else {
                    AppToast.show(LinkmanActivity.this,"消息发送失败");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(LinkmanActivity.this,"消息发送出错!");
            }
        });
    }
}
