package com.labour.lar.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.labour.lar.BaseFragment;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.activity.FaceLivenessExpActivity;
import com.labour.lar.cache.TokenCache;
import com.labour.lar.cache.UserCache;
import com.labour.lar.cache.UserInfoCache;
import com.labour.lar.map.LocationManager;
import com.labour.lar.module.User;
import com.labour.lar.module.UserInfo;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.Base64Bitmap;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnClick;

public class KaoqinFrag extends BaseFragment implements AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;
    @BindView(R.id.right_header_btn)
    TextView right_header_btn;

    @BindView(R.id.location_tv)
    TextView location_tv;
    @BindView(R.id.identified_tip_tv)
    TextView identified_tip_tv;
    @BindView(R.id.shot_tip_tv)
    TextView shot_tip_tv;
    @BindView(R.id.sign_iv)
    ImageView sign_iv;

    @BindView(R.id.ly_name)
    View ly_name;
    @BindView(R.id.txt_name)
    TextView txt_name;

    @BindView(R.id.sign_btn)
    Button sign_btn;

    private LocationManager locationManager;
    private LatLonPoint latLonPoint;
    private String[] loginInTime;
    private String[] logoOutTime;
    /**
     *  1：签到
     *  2：签退
     */
    private int signState;

    // 是否在正在检测中
    private boolean isChecking = false;

    // 是否在围栏中
    private boolean insideFence = false;
    // 是否匹配人脸
    private boolean isFaceMatch = false;

    private UserInfo userInfo;

    private static final int REQUEST_CODE_FACE = 101;

    public void setUser(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_kaoqin;
    }

    @Override
    public void initView() {
        back_iv.setVisibility(View.INVISIBLE);

        if(userInfo == null){
            userInfo = UserInfoCache.getInstance(getContext()).get();
            title_tv.setText("考勤打卡");
            ly_name.setVisibility(View.GONE);
        } else {
            title_tv.setText("代员工考勤打卡");
            ly_name.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(userInfo.getName())) {
                txt_name.setText(userInfo.getName());
            }
        }

        right_header_btn.setText("刷新");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 检查是否实名认证
        if (!checkIdentified()) {
            identified_tip_tv.setVisibility(View.VISIBLE);
            shot_tip_tv.setVisibility(View.GONE);
//            return;
        }

        locationManager = new LocationManager(context,true,1000,this,this);
        if(locationManager.createClientIfNeeded()){
            locationManager.startLocation();
        }

        loadSignTime();
        //showFaceLiveness();
    }

    @OnClick({R.id.right_header_btn, R.id.sign_btn, R.id.sign_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_header_btn:
                refresh();
                break;
            case R.id.sign_btn:
                signInOut();
                break;
            case R.id.sign_iv:
                showFaceActivity();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.release();
    }

    // 刷新打卡
    private void refresh(){
        loadSignTime();
//        showFaceLiveness();
        location_tv.setText("正在重新定位，请稍后...");
        locationManager.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if(amapLocation != null) {
            Log.e("amap", "KaoqinFrag 定位成功， lat: " + amapLocation.getLatitude() + " lon: " + amapLocation.getLongitude() + " locationType: " + amapLocation.getLocationType());
            if (amapLocation.getErrorCode() == 0) {
                latLonPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
                locationManager.getAddress(latLonPoint);
                checkSignState();
                insideFence();
            }else {
                location_tv.setText("定位失败，请重试！");
            }
        } else {
            location_tv.setText("定位失败，请重试！");
        }
    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                String addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                location_tv.setText(addressName);
            } else {
                AppToast.show(context, "定位失败！");
            }
        } else {
            AppToast.show(context, "定位失败："+rCode+"！");
        }
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 获取签到时间
     * 经于伟民确认，早于中午12点，就可以签到，晚于中午12点，就可以签退
     */
    private void loadSignTime() {
        try {
            Calendar cali1 = formatTime("12:00:00");//中午

            Calendar now = Calendar.getInstance();
            now.setTimeZone(TimeZone.getDefault());
            if(now.before(cali1)) {
                signState = 1;
            } else {
                signState = 2;
            }
        } catch (ParseException e){
            e.printStackTrace();
        }

        sign_btn.setTag(signState);
        if(signState == 1){
            //sign_btn.setEnabled(true);
            sign_btn.setText("签到");
        } else if(signState == 2){
            //sign_btn.setEnabled(true);
            sign_btn.setText("签退");
        } else {
            //sign_btn.setEnabled(false);
            sign_btn.setText("签到");
        }
    }

    private void loadSignTimeEx() {
        final ProgressDialog dialog = ProgressDialog.createDialog(context);
        dialog.show();

        getTime(1, new OnLoadTimeListener() {
            @Override
            public void onSuccess(String time) {
                loginInTime = time.split("-");
                getTime(2, new OnLoadTimeListener() {
                    @Override
                    public void onSuccess(String time) {
                        dialog.dismiss();
                        logoOutTime = time.split("-");

                        signState = getSignState();
                        sign_btn.setTag(signState);
                        if(signState == 1){
                            sign_btn.setEnabled(true);
                            sign_btn.setText("签到");
                        } else if(signState == 2){
                            sign_btn.setEnabled(true);
                            sign_btn.setText("签退");
                        } else {
                            sign_btn.setEnabled(false);
                            sign_btn.setText("签到");
                        }
                    }
                    @Override
                    public void onError() {
                        dialog.dismiss();
                    }
                });
            }
            @Override
            public void onError() {
                dialog.dismiss();
            }
        });
    }

    //获取签到时间
    public void getTime(int id,OnLoadTimeListener loadTimeCallback) {
        TokenCache tokenCache = TokenCache.getInstance(context);
        final Map<String,String> param = new HashMap<>();
        param.put("token",tokenCache.get());
        param.put("id",id+"");
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/sysdict";
        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONObject jo = jr.getData();
                    String loginInTime = jo.getString("dictvalue");
                    loadTimeCallback.onSuccess(loginInTime);
                } else {
                    AppToast.show(context,"获取数据出错!");
                    loadTimeCallback.onError();
                }
            }
            @Override
            public void onError(Response<String> response) {
                loadTimeCallback.onError();
                AppToast.show(context,"获取数据出错!");
            }
        });
    }

    /**
     * 1：签到
     * 2：签退
     * 取当天时间进行比较：
     * 签到开始时间到签退开始时间都能签到
     * 签退开始时间以后到能签退
     */
    private int getSignState() {
        int signState = 0;
        try {
            Calendar cali1 = formatTime(loginInTime[0]);//签到开始时间
            Calendar cali2 = formatTime(loginInTime[1]);//签到结束时间

            Calendar calo1 = formatTime(logoOutTime[0]);//签退开始时间
            Calendar calo2 = formatTime(logoOutTime[1]);//签退结束时间

            Calendar now = Calendar.getInstance();
            now.setTimeZone(TimeZone.getDefault());
            if(now.after(cali1) && now.before(calo1)) {
                signState = 1;
            } else if(now.after(calo1)){
                signState = 2;
            }

        } catch (ParseException e){
            e.printStackTrace();
        }
        return signState;
    }

    private Calendar formatTime(String time) throws ParseException {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
        String datestr = sf1.format(new Date());
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sf.parse(datestr + " " + time +":00");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.clear(Calendar.MILLISECOND);
        Log.i("kaoqin",sf.format(cal.getTime()));
        return cal;
    }

    public static interface OnLoadTimeListener {
        public void onSuccess(String time);
        public void onError();
    }

    private void signInOut(){
        if(userInfo == null){
            AppToast.show(context,"用户信息错误！");
            return;
        }

        if(isChecking){
            AppToast.show(context,"正在检测围栏，请稍后！");
            return;
        }

        if(insideFence){
            AppToast.show(context,"当前位置不在围栏内！");
            return;
        }
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sf2 = new SimpleDateFormat("HH:mm:ss");
        String clockdate = sf1.format(new Date());
        String clockintime = sf2.format(new Date());

        int userId =userInfo.getId();

        final Map<String,String> param = new HashMap<>();
        param.put("usertype",userInfo.getProle());
        param.put("userid",userId+"");
        param.put("clockdate",clockdate);
        param.put("clockintime",clockintime);
        param.put("clockingeo",latLonPoint.getLongitude()+"," + latLonPoint.getLatitude());
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/clockin";
        if(signState == 2){
            url = Constants.HTTP_BASE + "/api/clockout";
        }
        ProgressDialog dialog = ProgressDialog.createDialog(context);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    if(signState == 1){
                        AppToast.show(context,"签到成功!");
                    } else {
                        AppToast.show(context,"签退成功!");
                    }
                    sign_iv.setImageResource(R.mipmap.kaoqin_sign_icon);

                    Intent intent = new Intent();
                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context) ;
                    intent.setAction(Constants.SIGN_CHECK_IN_OUT_RECEIVER_ACTION);
                    intent.putExtra("signState",signState);
                    localBroadcastManager.sendBroadcast(intent) ;
                } else {
                    AppToast.show(context,jr.getMsg());
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(context,"操作失败!");
            }
        });
    }

    /**
     * 检查是否实名认证
     */
    private boolean checkIdentified() {
        //UserInfo userInfo = UserInfoCache.getInstance(getContext()).get();
        if (userInfo != null && userInfo.isIdentified()) {
            return true;
        } else {
            return false;
        }
    }

    private void checkSignState() {
        if (latLonPoint != null && isFaceMatch) {
            sign_btn.setEnabled(true);
        } else {
            sign_btn.setEnabled(false);
        }
    }

    // 显示活体检测页面
//    private void showFaceLiveness(){
//        FaceLivenessFragment faceLivenessFragment = new FaceLivenessFragment();
//        faceLivenessFragment.setLivenessStrategyCallback(new ILivenessStrategyCallback() {
//            @Override
//            public void onLivenessCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
//                if (status == FaceStatusEnum.OK) {
//                    Set<Map.Entry<String, String>> sets = base64ImageMap.entrySet();
//                    String base64Data = null;
//                    for (Map.Entry<String, String> entry : sets) {
//                        base64Data = entry.getValue();
//                    }
//                    faceMatch(base64Data);
//                }
//            }
//        });
//
//        FragmentManager fm = this.getChildFragmentManager();
//        FragmentTransaction trs = fm.beginTransaction();
//        trs.replace(R.id.container, faceLivenessFragment);
//        trs.commit();
//    }

    // 显示活体检测页面
    private void showFaceActivity(){
        if (checkIdentified()) {
            Intent intent = new Intent(getContext(), FaceLivenessExpActivity.class);
            startActivityForResult(intent, REQUEST_CODE_FACE);
        } else {
            AppToast.show(context,"请先进行实名认证!");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == REQUEST_CODE_FACE) {
                if (data != null) {
                    showFaceImage(data);
                }
            }
        }
    }

    // 显示图片
    private void showFaceImage(Intent data) {
        String base64Data = data.getStringExtra("data");// 解析返回的图片成bitmap
        Bitmap bmp = Base64Bitmap.base64ToBitmap(base64Data);
        sign_iv.setImageBitmap(bmp);
        faceMatch(base64Data);
    }

    /**
     * 人脸匹配
     * @param base64Data 人脸照片
     */
    private void faceMatch(String base64Data) {
        if (TextUtils.isEmpty(base64Data)){
            return;
        }

        User user = UserCache.getInstance(getContext()).get();
        String employee_id = user.getId()+"";

        final Map<String,String> param = new HashMap<>();
        param.put("img",base64Data);
        param.put("rtype",user.getProle());
        param.put("employee_id",employee_id+"");//工人id
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/face_match";

        ProgressDialog dialog = ProgressDialog.createDialog(context);
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONObject jo = jr.getData();
                    JSONObject result =  jo.getJSONObject("result");

                    if (result != null) {
                        int score =  result.getInteger("score");

                        if (score > 70){
                            isFaceMatch = true;
                            AppToast.show(context,"身份认证成功!");
                            checkSignState();
                        } else {
                            isFaceMatch = false;
                            AppToast.show(context,"身份认证失败!");
                        }
                    }
                } else {
                    AppToast.show(context,jr.getMsg());
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(context,"操作失败!");
            }
        });
    }

    /**
     * 是否在里面
     */
    private void insideFence() {
        if (latLonPoint == null){
            return;
        }

        UserInfo user = UserInfoCache.getInstance(getContext()).get();
        if (user == null){
            return;
        }

        int fence_id;

        if (signState == 1){
            fence_id = user.getClockinfence();
        } else {
            fence_id = user.getClockoutfence();
        }

        JSONObject param = new JSONObject();
        param.put("id",fence_id);//围栏id
        param.put("lng",latLonPoint.getLongitude());
        param.put("lat",latLonPoint.getLatitude());
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/if_infence";
        ProgressDialog dialog = ProgressDialog.createDialog(context);
        dialog.show();
        isChecking = true;

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONObject jo = jr.getData();
                    if (jo != null) {
                        boolean infence =  jo.getBooleanValue("infence");
                        if (infence){
                            insideFence = true;
                            AppToast.show(context,"围栏检测成功!");
                        } else {
                            insideFence = false;
                            AppToast.show(context,"当前位置不在指定围栏中!");
                        }
                    }
                } else {
                    AppToast.show(context,"围栏检测失败!");
                    AppToast.show(context,jr.getMsg());
                }

                isChecking = false;
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(context,"围栏检测错误!");
                isChecking = false;
            }
        });
    }
}
