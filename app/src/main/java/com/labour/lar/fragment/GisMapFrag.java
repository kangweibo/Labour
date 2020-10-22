package com.labour.lar.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polygon;
import com.amap.api.maps2d.model.PolygonOptions;
import com.amap.api.maps2d.model.Text;
import com.amap.api.maps2d.model.TextOptions;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.activity.GeoFenceActivity;
import com.labour.lar.cache.UserCache;
import com.labour.lar.map.MapGeoFence;
import com.labour.lar.module.EmpsLocation;
import com.labour.lar.module.Project;
import com.labour.lar.module.User;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.StringUtils;
import com.labour.lar.widget.BottomSelectDialog;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class GisMapFrag extends Fragment implements AMap.OnMarkerClickListener, AMap.OnMapLoadedListener, AMap.OnInfoWindowClickListener {

    @BindView(R.id.map)
    MapView mapView;

    @BindView(R.id.btn_set)
    Button btn_set;

    protected Unbinder unbinder;

    private AMap aMap;

    //实时定位
    private LocalBroadcastManager localBroadcastManager;
    private MapLocationReceiver mapLocationReceiver = new MapLocationReceiver();

    //地理围栏
    private Point lastPt = new Point();;
    private Map<Polygon, MapGeoFence> polygonMap = new HashMap<>();
    private Polygon polygonSelect;

    private BottomSelectDialog dialog;
    private View mRootView;
    private String fenceId;
    private String project_id;
    private boolean isCanSet;//能否设置围栏

    private List<MapGeoFence> fences;
    private List<EmpsLocation> empsLoc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_gismap, container, false);
        unbinder = ButterKnife.bind(this, view);
        //定义了一个地图view
        mRootView = view;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。

        //初始化地图控制器对象
        setUpMapIfNeeded();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.LOCATION_MAP_RECEIVER_ACTION);
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext()) ;
        localBroadcastManager.registerReceiver( mapLocationReceiver , filter );

        UserCache userCache = UserCache.getInstance(getActivity());
        User user = userCache.get();

        if (user != null){
            String prole = user.getProle();
            if (!TextUtils.isEmpty(prole) &&
                    (prole.equals("ent_manager") || prole.equals("project_manager") || prole.equals("project_quota"))){
                isCanSet = true;
            } else {
                isCanSet = false;
                btn_set.setVisibility(View.INVISIBLE);
            }
        }

        getFence();
        getEmpsLoc();
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMapIfNeeded() {
        if (aMap == null) {
            aMap = mapView.getMap();

//        aMap.setLocationSource(this);// 设置定位监听
            aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
            aMap.getUiSettings().setZoomControlsEnabled(false);
            aMap.setMyLocationEnabled(false);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

            aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
            aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
            aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
            aMap.moveCamera(CameraUpdateFactory.zoomTo(13));//18
            //setupLocationStyle();

            setDrawMap();
        }
    }

    /**
     * 监听amap地图加载成功事件回调
     */
    @Override
    public void onMapLoaded() {
        // 设置所有maker显示在当前可视区域地图中
//        LatLngBounds bounds = new LatLngBounds.Builder()
//                .include(Constants.XIAN).include(Constants.CHENGDU)
//                .include(latlng).include(Constants.ZHENGZHOU).include(Constants.BEIJING).build();
//        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
    }
    /**
     * 监听点击infowindow窗口事件回调
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        //Toast.makeText(getContext(), "你点击了infoWindow窗口" + marker.getTitle(),Toast.LENGTH_SHORT).show();
    }
    /**
     * 对marker标注点点击响应事件
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        //Toast.makeText(getContext(), "你点击的是" + marker.getTitle(),Toast.LENGTH_SHORT).show();
        return false;
    }

    // 获取围栏
    private void getFence1() {
        if(StringUtils.isBlank(project_id)){
            return;
        }

        final Map<String,String> param = new HashMap<>();
        param.put("project_id",project_id);
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/project_geofences";
        ProgressDialog dialog = ProgressDialog.createDialog(this.getContext());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONArray jsonArray = jr.getJSONArrayData();
                    fences = JSON.parseArray(JSON.toJSONString(jsonArray), MapGeoFence.class);

                    refreshMap();
                } else {
                    AppToast.show(getContext(),"没有围栏!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getContext(),"围栏获取出错!");
            }
        });
    }

    // 获取围栏
    private void getFence() {
        if(StringUtils.isBlank(fenceId)){
            return;
        }

        final Map<String,String> param = new HashMap<>();
        param.put("id",fenceId);
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/get_geofence";
        ProgressDialog dialog = ProgressDialog.createDialog(this.getContext());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONArray jsonArray = jr.getJSONArrayData();
                    fences = JSON.parseArray(JSON.toJSONString(jsonArray), MapGeoFence.class);

                    refreshMap();
                } else {
                    AppToast.show(getContext(),"没有围栏!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getContext(),"围栏获取出错!");
            }
        });
    }

    // 获取工人位置
    private void getEmpsLoc() {
        if(StringUtils.isBlank(project_id)){
            return;
        }

        final Map<String,String> param = new HashMap<>();
        //param.put("project_id",project_id);
        param.put("token","063d91b4f57518ff");
        String jsonParams = JSON.toJSONString(param);

        String url = Constants.HTTP_BASE + "/api/today_emps_loc";
        ProgressDialog dialog = ProgressDialog.createDialog(this.getContext());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    JSONArray jsonArray = jr.getJSONArrayData();
                    empsLoc = JSON.parseArray(JSON.toJSONString(jsonArray), EmpsLocation.class);

                    refreshMap();
                } else {
                    AppToast.show(getContext(),"获取工人失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getContext(),"获取围栏工人出错!");
            }
        });
    }

    /**
     * 刷新地图
     */
    private void refreshMap() {
        aMap.clear();
        if (fences != null){
            for (MapGeoFence fence : fences) {
                drawPolygon(fence);
            }
        }

        if (empsLoc != null){
            for (EmpsLocation loc : empsLoc) {
                drawMarkers(loc);
            }
        }
    }

    /**
     * 绘制工人图标
     */
    private void drawMarkers(EmpsLocation loc) {
        try {
            if (loc.todayloc == null){
                return;
            }

            double lat = Double.parseDouble(loc.todayloc.lat);
            double lon = Double.parseDouble(loc.todayloc.lon);
            LatLng latlng = new LatLng(lat, lon);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.worker_icon);
            bitmap = Bitmap.createScaledBitmap(bitmap,90,90, true);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latlng)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                    .draggable(true);

            if (loc.classteamname != null){
                markerOptions .title(loc.classteamname);
            }

            aMap.addMarker(markerOptions);

            //文字显示标注，可以设置显示内容，位置，字体大小颜色，背景色旋转角度,Z值等
            TextOptions textOptions = new TextOptions().position(latlng)
                    .fontColor(Color.BLACK).fontSize(30)
                    .align(Text.ALIGN_CENTER_HORIZONTAL, Text.ALIGN_CENTER_VERTICAL)
                    .zIndex(2.f).typeface(Typeface.DEFAULT_BOLD);

            if (loc.name != null){
                textOptions.text(loc.name);
            }
            aMap.addText(textOptions);
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    //绘制多边形
    public void drawPolygon(MapGeoFence fence){
        List<LatLng> pts = new ArrayList<>();

        for (MapGeoFence.Points point: fence.points) {
            try {
                double lat = Double.parseDouble(point.lat);
                double lon = Double.parseDouble(point.lon);
                LatLng pt = new LatLng(lat, lon);
                pts.add(pt);

            } catch (NumberFormatException e){
                e.printStackTrace();
            }
        }

        if (pts.size() > 2) {
            Polygon polygon = aMap.addPolygon((new PolygonOptions()).addAll(pts)
                    .strokeWidth(6)
                    .strokeColor(Color.argb(255, 1, 250, 1))
                    .fillColor(Color.argb(10, 1, 1, 1)));

            polygonMap.put(polygon, fence);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pts.get(0), 16));// 设置指定的可视区域地图
            aMap.invalidate();//刷新地图
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        localBroadcastManager.unregisterReceiver(mapLocationReceiver);
    }

    public class MapLocationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            /*String action = intent.getAction();
            if (action.equals(Constants.LOCATION_MAP_RECEIVER_ACTION)){
                LatLng latlng = (LatLng)intent.getParcelableExtra("latlng");
                Log.e("amap", "MapLocationReceiver onMyLocationChange 定位成功， lat: " + latlng.latitude + " lon: " + latlng.longitude);
                //清空地图上所有已经标注的marker
                if (aMap != null) {
                    aMap.clear();
                    drawMarkers(latlng);

                    //测试
                    CameraUpdate cameraUpdate = CameraUpdateFactory.changeLatLng(latlng);
                    aMap.moveCamera(cameraUpdate);
                }
            }*/
        }

    }

    @OnClick({R.id.btn_set})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_set:
                setFence();
                break;
        }
    }

    private void setDrawMap() {
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        Point touchpt = new Point();
                        touchpt.x = (int) event.getX();
                        touchpt.y = (int) event.getY();
                        touchUp(touchpt);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        lastPt.x = (int) event.getX();
                        lastPt.y = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 手指抬起
     * @param touchpt
     */
    private void touchUp(Point touchpt) {
        int x = touchpt.x - lastPt.x;
        int y = touchpt.y - lastPt.y;
        double z = Math.sqrt(x * x + y * y);

        if ( z > 10){ // 手指移动超过10像素，不处理
            return;
        }

        if (!isCanSet){
            return;
        }

        // 坐标转换
        LatLng latlng = mapView.getMap().getProjection()
                .fromScreenLocation(touchpt);

        for (Polygon polygon : polygonMap.keySet()) {
            if (polygon.contains(latlng)) {
                polygonSelect = polygon;
                showMoreDialog();
                break;
            }
        }
    }

    private void showMoreDialog(){
        dialog = new BottomSelectDialog(getActivity(),new BottomSelectDialog.BottomSelectDialogListener() {
            @Override
            public int getLayout() {
                return R.layout.menu_fence;
            }
            @Override
            public void initView(View view) {
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = v.getId();
                        if(id == R.id.txt_see){
                            seeFence();
                        } else if(id == R.id.txt_update){
                            updateFence();
                        } else if(id == R.id.txt_delete){
                            deleteFence();
                        }

                        dialog.dismiss();
                    }
                };

                View txt_see = view.findViewById(R.id.txt_see);
                View txt_update = view.findViewById(R.id.txt_update);
                View txt_delete = view.findViewById(R.id.txt_delete);
                View txt_cancel = view.findViewById(R.id.txt_cancel);
                txt_see.setOnClickListener(onClickListener);
                txt_update.setOnClickListener(onClickListener);
                txt_delete.setOnClickListener(onClickListener);
                txt_cancel.setOnClickListener(onClickListener);
            }
            @Override
            public void onClick(Dialog dialog, int rate) {

            }
        });

        dialog.showAtLocation(mRootView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    // 设置围栏
    private void setFence() {
        if (project_id != null) {
            Intent intent = new Intent(getContext(), GeoFenceActivity.class);
            intent.putExtra("state", 0);
            intent.putExtra("project_id", project_id);
            startActivityForResult(intent, Constants.RELOAD);
        }
    }

    // 查看围栏
    private void seeFence() {
        if (polygonSelect != null){
            MapGeoFence fence = polygonMap.get(polygonSelect);
            if (fence != null){
                Intent intent = new Intent(getContext(), GeoFenceActivity.class);
                intent.putExtra("state", 2);
                intent.putExtra("fence_id", fence.id+"");
                intent.putExtra("project_id", fence.project_id+"");
                startActivity(intent);
            }
        }
    }

    // 更新围栏
    private void updateFence() {
        if (polygonSelect != null){
            MapGeoFence fence = polygonMap.get(polygonSelect);
            if (fence != null){
                Intent intent = new Intent(getContext(), GeoFenceActivity.class);
                intent.putExtra("state", 1);
                intent.putExtra("fence_id", fence.id+"");
                intent.putExtra("project_id", fence.project_id+"");
                startActivityForResult(intent, Constants.RELOAD);
            }
        }
    }

    // 删除围栏
    private void deleteFence() {
        if (polygonSelect != null){
            MapGeoFence fence = polygonMap.get(polygonSelect);
            if (fence != null){
                deleteFence(fence.id+"");
            }
        }
    }

    public void deleteFence(String id) {
        if(StringUtils.isBlank(id)){
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        String jsonParams =jsonObject.toJSONString();

        String url = Constants.HTTP_BASE + "/api/del_geofence";
        ProgressDialog dialog = ProgressDialog.createDialog(getActivity());
        dialog.show();

        OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.dismiss();
                AjaxResult jr = new AjaxResult(response.body());
                if(jr.getSuccess() == 1){
                    AppToast.show(getActivity(),"删除围栏成功!");
                    getFence();
                } else {
                    AppToast.show(getActivity(),"删除围栏失败!");
                }
            }
            @Override
            public void onError(Response<String> response) {
                dialog.dismiss();
                AppToast.show(getActivity(),"删除围栏出错!");
            }
        });
    }

    public void setFenceId(String fenceId){
        this.fenceId = fenceId;
    }

    public void setProjectId(String project_id){
        this.project_id = project_id;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 重新加载数据
        if (requestCode == Constants.RELOAD && resultCode == RESULT_OK) {
            getFence();
        }
    }
}
