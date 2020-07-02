package com.labour.lar.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.adapter.SimpleTreeAdapter;
import com.labour.lar.module.Linkman;
import com.labour.lar.util.AjaxResult;
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
    @BindView(R.id.right_header_btn)
    TextView right_header_btn;

    private String project_id;

    private List<Node> mlist = new ArrayList<>();
    private SimpleTreeAdapter mAdapter;
    private int mNodeId = 0;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_linkman;
    }

    @Override
    public void afterInitLayout() {
        Intent intent = getIntent();
        project_id = intent.getStringExtra("project_id");

        title_tv.setText("联系人");
        right_header_btn.setText("确定");

        mAdapter = new SimpleTreeAdapter(lv_tree, this,
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
//        if(StringUtils.isBlank(project_id) ){
//            AppToast.show(this,"项目ID为空！");
//            return;
//        }

        final Map<String,String> param = new HashMap<>();

        //param.put("id",project_id);
        param.put("id","1");
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
                    JSONObject jo = jr.getData();
                    Linkman linkman = JSON.parseObject(JSON.toJSONString(jo), Linkman.class);
                    initData(linkman);
                } else {
                    AppToast.show(LinkmanActivity.this,"组织架构获取失败");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(LinkmanActivity.this,"组织架构获取出错!");
            }
        });
    }

    // 初始化数据
    private void initData(Linkman linkman) {
        addLinkmanId(linkman, 0, "project");

        mAdapter.addData(0,mlist);
    }

    // 添加节点id，父节点id
    private void addLinkmanId(Linkman linkman, int pid, String totag) {
        int nodeId = mNodeId++;
        linkman.setNodeId(nodeId);
        linkman.setPid(pid);
        linkman.setTotag(totag);

        String name = linkman.getName();
        if (name == null){
            name = "";
        }

        Node<Integer, Linkman> node = new Node(nodeId, pid, name, linkman);
        mlist.add(node);

//        List<Linkman> managers = linkman.getManagers();
//
//        if (managers != null) {
//            for (Linkman man : managers) {
//                addLinkmanId(man, nodeId, "manager");
//            }
//        }

        List<Linkman> operteams = linkman.getOperteams();

        if (operteams != null) {
            for (Linkman man : operteams) {
                addLinkmanId(man, nodeId,"operteam");
            }
        }

//        List<Linkman> staffs = linkman.getStaffs();
//
//        if (staffs != null) {
//            for (Linkman man : staffs) {
//                addLinkmanId(man, nodeId,"staff");
//            }
//        }

        List<Linkman> classteams = linkman.getClassteams();

        if (classteams != null) {
            for (Linkman man : classteams) {
                addLinkmanId(man, nodeId,"classteam");
            }
        }

//        List<Linkman> employees = linkman.getEmployees();
//
//        if (employees != null) {
//            for (Linkman man : employees) {
//                addLinkmanId(man, nodeId,"employee");
//            }
//        }
    }

    // 确定
    private void submit() {
        ArrayList<Linkman> linkmans = new ArrayList<>();

        final List<Node> allNodes = mAdapter.getAllNodes();

        // 如果本节点被选中，则子节点不再被选中
        for (int i = 0; i < allNodes.size(); i++) {
            Node node = allNodes.get(i);
            if (node.isChecked()) {
                setChildChecked(node,false);
            }
        }

        for (int i = 0; i < allNodes.size(); i++) {
            Node node = allNodes.get(i);
            if (node.isChecked()) {
                linkmans.add((Linkman)node.bean);
            }
        }

        if (!linkmans.isEmpty()) {
            String json = linkmansToJson(linkmans);

            Intent intent = getIntent();
            intent.putExtra("linkman", json);
            setResult(RESULT_OK, intent);
            this.finish();
        } else {
            AppToast.show(this,"未选择接收方");
        }
    }

    // 设置子节点选中状态
    private <T,B>void setChildChecked(Node<T,B> node,boolean checked){
        if(!node.isLeaf()){
            for (Node childrenNode : node.getChildren()) {
                childrenNode.setChecked(checked);
                setChildChecked(childrenNode, checked);
            }
        }
    }

    private String linkmansToJson(ArrayList<Linkman> linkmans){
        JSONArray jsonArray = new JSONArray();

        for (Linkman linkman : linkmans) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("totag", linkman.getTotag());
            jsonObject.put("toid", linkman.getId());
            jsonObject.put("name", linkman.getName());

            jsonArray.add(jsonObject);
        }

        return jsonArray.toJSONString();
    }
}
