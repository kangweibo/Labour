package com.labour.lar.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.labour.lar.BaseFragment;
import com.labour.lar.MainActivity;
import com.labour.lar.R;
import com.labour.lar.map.LocationManager;
import com.labour.lar.widget.BottomSelectDialog;
import com.labour.lar.widget.toast.AppMsg;
import com.labour.lar.widget.toast.AppToast;

import butterknife.BindView;
import butterknife.OnClick;

public class KaoqinFrag extends BaseFragment implements AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.back_iv)
    TextView back_iv;
    @BindView(R.id.right_header_btn)
    TextView right_header_btn;

    BottomSelectDialog dialog;

    @BindView(R.id.location_tv)
    TextView location_tv;

    private LocationManager locationManager;

    @Override
    public int getFragmentLayoutId() {
        return R.layout.frag_kaoqin;
    }

    @Override
    public void initView() {
        back_iv.setVisibility(View.INVISIBLE);
        title_tv.setText("考勤打卡");
        Drawable d = getResources().getDrawable(R.mipmap.more_icon);
        right_header_btn.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationManager = new LocationManager(context,true,this,this);
        if(locationManager.createClientIfNeeded()){
            locationManager.startLocation();
        }
    }

    @OnClick({R.id.right_header_btn,R.id.sign_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_header_btn:
                showMoreDialog();
                break;
            case R.id.sign_btn:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.release();
    }

    private void showMoreDialog(){
         dialog = new BottomSelectDialog(context,new BottomSelectDialog.BottomSelectDialogListener() {
            @Override
            public int getLayout() {
                return R.layout.menu_kaoqin;
            }
            @Override
            public void initView(View view) {
                View refresh_btn = view.findViewById(R.id.refresh_btn);
                refresh_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        location_tv.setText("正在重新定位，请稍后...");
                        locationManager.startLocation();
                        dialog.dismiss();
                    }
                });
            }
            @Override
            public void onClick(Dialog dialog, int rate) {

            }
        });
        dialog.showAtLocation(mRootView,Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if(amapLocation != null) {
            Log.e("amap", "KaoqinFrag 定位成功， lat: " + amapLocation.getLatitude() + " lon: " + amapLocation.getLongitude() + " locationType: " + amapLocation.getLocationType());
            if (amapLocation.getErrorCode() == 0) {
                LatLonPoint latLonPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
                locationManager.getAddress(latLonPoint);
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
}
