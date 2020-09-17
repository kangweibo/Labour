package com.labour.lar.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.labour.lar.BaseActivity;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.cache.UserCache;
import com.labour.lar.module.Classteam;
import com.labour.lar.module.Operteam;
import com.labour.lar.module.Project;
import com.labour.lar.module.User;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.yzq.zxinglibrary.encode.CodeCreator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 二维码界面
 */
public class ShowQRCodeActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.right_header_btn)
    TextView right_header_btn;

    @BindView(R.id.txt_title)
    TextView txt_title;

    @BindView(R.id.banner)
    Banner banner;

    private List<Bitmap> images = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<Integer> ids = new ArrayList<>();
    private String title = "";
    private int type;// 企业：0；项目：1；作业队：2；班组 3；
    private int projectId;
    private int operteamId;
    private int classteamId;

    @Override
    public int getActivityLayoutId() {
        return R.layout.activity_show_qrcode;
    }
    @Override
    public void afterInitLayout(){
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        int id = intent.getIntExtra("id", 0);
        projectId = intent.getIntExtra("projectId", -1);
        operteamId = intent.getIntExtra("operteamId", -1);
        classteamId = intent.getIntExtra("classteamId", -1);
        title = intent.getStringExtra("title");

        txt_title.setText(title);
        initData(type, id);

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);

        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(false);

        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (type < 2) {
                    int id = ids.get(position);
                    Intent intent = new Intent(ShowQRCodeActivity.this, ShowQRCodeActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("type", type + 1);
                    intent.putExtra("title", title + "\n" + titles.get(position));
                    startActivity(intent);
                }
            }
        });
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.back_iv,R.id.right_header_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
            case R.id.right_header_btn:
                break;
        }
    }

    /**
     * 初始化显示数据
     * @param type 企业：0；项目：1；作业队：2；班组 3；
     * @param id
     */
    private void initData(int type ,int id){
        switch (type){
            case 0:
            case 1:
                title_tv.setText("项目二维码");
                getProject(projectId);
                break;
            case 2:
                title_tv.setText("作业队二维码");
                getOperteam(id, operteamId);
                break;
            case 3:
                title_tv.setText("班组二维码");
                getClassteam(id, classteamId);
                break;
        }
    }

    // 初始化显示数据
    private void showData(List<String> contents){
        images.clear();

        for (String content : contents) {
            Bitmap logo = getBitmap(content);
            images.add(logo);
        }

        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    private Bitmap getBitmap(String content){
        if (!TextUtils.isEmpty(content)) {
            //Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.icon);
            return CodeCreator.createQRCode(content, 400, 400, null);
        } else {
            return null;
        }
    }

    public static class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);
        }
    }

    private void getProject(int projectId) {
        UserCache userCache = UserCache.getInstance(this);
        User user = userCache.get();

        String prole = user.getProle();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token","063d91b4f57518ff");
        jsonObject.put("dtype",prole);
        jsonObject.put("userid",user.getId());
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/projects";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Project> projects = JSON.parseArray(JSON.toJSONString(jsonArray), Project.class);
                    List<String> contents = new ArrayList<>();

                    ids.clear();
                    titles.clear();

                    for (Project project : projects){
                        if (projectId == -1 || projectId == project.getId()) {
                            JSONObject jsonObject = new JSONObject();
                            String id = project.getId() + "";
                            jsonObject.put("project_id", id);
                            String content = jsonObject.toJSONString();
                            contents.add(content);

                            ids.add(project.getId());
                            titles.add(project.getName());
                        }
                    }

                    showData(contents);
                } else {
                    AppToast.show(ShowQRCodeActivity.this,"获取项目信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(ShowQRCodeActivity.this,"获取项目信息出错!");
            }
        });
    }

    private void getOperteam(int id, int operteamId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/operteams";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Operteam> operteams = JSON.parseArray(JSON.toJSONString(jsonArray), Operteam.class);
                    List<String> contents = new ArrayList<>();

                    ids.clear();
                    titles.clear();

                    for (Operteam operteam : operteams){
                        if (operteamId == -1 || operteamId == operteam.getId()) {
                            JSONObject jsonObject = new JSONObject();
                            String id = operteam.getId() + "";
                            jsonObject.put("operteam_id", id);
                            String content = jsonObject.toJSONString();
                            contents.add(content);

                            ids.add(operteam.getId());
                            titles.add(operteam.getName());
                        }
                    }

                    showData(contents);
                } else {
                    AppToast.show(ShowQRCodeActivity.this,"获取作业队信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(ShowQRCodeActivity.this,"获取作业队信息出错!");
            }
        });
    }

    private void getClassteam(int id, int classetam_id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/classteams";
        ProgressDialog dialog = ProgressDialog.createDialog(this);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONArray jsonArray = jr.getJSONArrayData();
                    List<Classteam> classetams = JSON.parseArray(JSON.toJSONString(jsonArray), Classteam.class);
                    List<String> contents = new ArrayList<>();

                    ids.clear();
                    titles.clear();

                    for (Classteam classetam : classetams){
                        if (classetam_id == -1 || classetam_id == classetam.getId()) {
                            JSONObject jsonObject = new JSONObject();
                            String id = classetam.getId() + "";
                            jsonObject.put("classetam_id", id);
                            String content = jsonObject.toJSONString();
                            contents.add(content);

                            ids.add(classetam.getId());
                            titles.add(classetam.getName());
                        }
                    }

                    showData(contents);
                } else {
                    AppToast.show(ShowQRCodeActivity.this,"获取班组信息失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(ShowQRCodeActivity.this,"获取班组信息出错!");
            }
        });
    }
}
