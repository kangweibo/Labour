package com.labour.lar.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.DepartmentTreeAdapter;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.module.Department;
import com.labour.lar.module.UserInfo;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.multilevel.treelist.Node;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 部门
 */
public class DepartmentActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;

    @BindView(R.id.lv_tree)
    ListView lv_tree;
    @BindView(R.id.right_header_btn)
    TextView right_header_btn;

    private List<Node> mlist = new ArrayList<>();
    private DepartmentTreeAdapter mAdapter;
    private int mNodeId = 0;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_linkman;
    }

    @Override
    public void afterInitLayout() {
        title_tv.setText("选择部门");
        right_header_btn.setText("确定");

        mAdapter = new DepartmentTreeAdapter(lv_tree, this,
                mlist, 1,R.mipmap.tree_ex,R.mipmap.tree_ec);

        lv_tree.setAdapter(mAdapter);

        getOrgTree();
    }

    @OnClick({R.id.back_iv,R.id.right_header_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.right_header_btn:
                submit();
                break;
        }
    }

    private void getOrgTree() {
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
        param.put("userprole",prole);
        param.put("userid",userInfo.getId());
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/user_orgtree";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONObject jo = jr.getData();
                    Department department = JSON.parseObject(JSON.toJSONString(jo), Department.class);
                    initData(department);
                } else {
                    AppToast.show(DepartmentActivity.this,"组织架构获取失败");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(DepartmentActivity.this,"组织架构获取出错!");
            }
        });
    }

    // 初始化数据
    private void initData(Department department) {
        addLinkmanId(department, 0);
        mAdapter.addData(0,mlist);
    }

    // 添加节点id，父节点id
    private void addLinkmanId(Department department, int pid) {
        int nodeId = mNodeId++;
        department.setNodeId(nodeId);
        department.setPid(pid);

        String name = department.getName();
        if (name == null){
            name = "";
        }

        Node<Integer, Department> node = new Node(nodeId, pid, name, department);
        mlist.add(node);

        List<Department> childrens = department.getChildren();

        if (childrens != null) {
            for (Department man : childrens) {
                addLinkmanId(man, nodeId);
            }
        }
    }

    // 确定
    private void submit() {
        ArrayList<Department> departments = new ArrayList<>();

        final List<Node> allNodes = mAdapter.getAllNodes();

        for (int i = 0; i < allNodes.size(); i++) {
            Node node = allNodes.get(i);
            if (node.isChecked()) {
                departments.add((Department)node.bean);
            }
        }

        if (!departments.isEmpty()) {
            String json = departmentToJson(departments.get(0));
            Intent intent = getIntent();
            intent.putExtra("department", json);
            setResult(RESULT_OK, intent);
            this.finish();
        } else {
            AppToast.show(this,"未选择部门");
        }
    }

    private String departmentToJson(Department department){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", department.getId());
        jsonObject.put("name", department.getName());
        jsonObject.put("level", department.getLevel());

        return jsonObject.toJSONString();
    }
}
