package com.labour.lar.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polygon;
import com.amap.api.maps2d.model.PolygonOptions;
import com.amap.api.maps2d.model.Text;
import com.amap.api.maps2d.model.TextOptions;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.map.MapGeoFence;
import com.labour.lar.module.EmpsLocation;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.StringUtils;
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

/**
 * 添加围栏.
 */
public class GeoFenceActivity extends Activity {

	@BindView(R.id.title_tv)
	TextView title_tv;
	@BindView(R.id.back_iv)
	TextView back_iv;
	@BindView(R.id.right_header_btn)
	TextView right_header_btn;
	@BindView(R.id.btn_sign)
	Button btn_sign;
	@BindView(R.id.btn_reset)
	Button btn_reset;
	@BindView(R.id.ly_bottom)
	View ly_bottom;

    @BindView(R.id.map)
    MapView mapView;

	private AMap aMap;

	private List<LatLng> listPts = new ArrayList<>();
	private Polygon polyline;
	private MarkerOptions markerOption;
	private boolean isdrawing;
	private Point lastPt = new Point();
	private int state; // 0：添加； 1：更新；2：查看
	private String fence_id;
	private String project_id;
	private List<MapGeoFence> fences = new ArrayList<>();
	private List<EmpsLocation> empsLoc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		state = intent.getIntExtra("state", 0);
		fence_id = intent.getStringExtra("fence_id");
		project_id = intent.getStringExtra("project_id");

		setContentView(R.layout.activity_geofence);
        ButterKnife.bind(this);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		title_tv.setText("设置围栏");
		right_header_btn.setText("提交");

		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}

		setDrawMap();

		if (state == 1){ // 更新
			title_tv.setText("更新围栏");
			getFence();
		} else if (state == 2){ // 查看
			title_tv.setText("查看围栏");
			right_header_btn.setVisibility(View.INVISIBLE);
			ly_bottom.setVisibility(View.GONE);
			getFence();
			getEmpsLoc();
		}
	}

	@OnClick({R.id.btn_reset,R.id.btn_sign,R.id.back_iv,R.id.right_header_btn})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_reset:
				reset();
				break;
			case R.id.btn_sign:
				startSign();
				break;
			case R.id.back_iv:
                finish();
                break;
			case R.id.right_header_btn:
				if (state == 0) {
					addFence(project_id, listPts);
				} else if (state == 1) {
					updateFence(fence_id, project_id, listPts);
				}
                break;
			default:
				break;
		}
	}

	private void startSign() {
		isdrawing = true;
	}

	private void reset() {
		isdrawing = false;
		listPts.clear();
		aMap.clear();
	}

	private void setUpMap() {
		//aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Constants.BEIJING, 5));// 设置指定的可视区域地图
	}

	private void setDrawMap() {
		aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
			@Override
			public void onTouch(MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						if (isdrawing) {
							Point touchpt = new Point();
							touchpt.x = (int) event.getX();
							touchpt.y = (int) event.getY();

							touchUp(touchpt);
						}
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

		// 坐标转换
		LatLng latlng = mapView.getMap().getProjection()
				.fromScreenLocation(touchpt);
		listPts.add(latlng);
		// 地图上绘制
		addMarkersToMap(latlng);
		drawLine(listPts);
	}

	/**
	 * 地图上绘制线
	 * @param pts
	 */
	private void drawLine(List<LatLng> pts) {
		if (pts.size() >= 2) {
			if (pts.size() == 2) {
				polyline = aMap.addPolygon((new PolygonOptions()).addAll(pts)
						.strokeWidth(6)
						.strokeColor(Color.argb(255, 1, 250, 1))
						.fillColor(Color.argb(50, 1, 1, 1)));
			} else {
				polyline.setPoints(pts);
			}

			aMap.invalidate();//刷新地图
		}
	}

	/**
	 * 在地图上添加marker
	 */
	private void addMarkersToMap(LatLng latlng) {
		markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.poi_location))
				.position(latlng)
				.draggable(true);
		aMap.addMarker(markerOption);
	}

	// 添加围栏
	private void addFence(String project_id, List<LatLng> pts) {
		if(StringUtils.isBlank(project_id)){
			return;
		}

		JSONArray jsonArray = new JSONArray();

		for (LatLng pt : pts) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("lat", pt.latitude);
			jsonObj.put("lon", pt.longitude);
			jsonArray.add(jsonObj);
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("project_id",project_id);
		jsonObject.put("geotype", "polygon");
		jsonObject.put("points", jsonArray);
		String jsonParams =jsonObject.toJSONString();

		String url = Constants.HTTP_BASE + "/api/defgeofence";
		ProgressDialog dialog = ProgressDialog.createDialog(this);
		dialog.show();

		OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
			@Override
			public void onSuccess(Response<String> response) {
				dialog.dismiss();
				AjaxResult jr = new AjaxResult(response.body());
				if(jr.getSuccess() == 1){
					AppToast.show(GeoFenceActivity.this,"设置围栏成功!");
					finish();
				} else {
					AppToast.show(GeoFenceActivity.this,"设置围栏失败!");
				}
			}
			@Override
			public void onError(Response<String> response) {
				dialog.dismiss();
				AppToast.show(GeoFenceActivity.this,"围栏设置出错!");
			}
		});
	}

	// 更新围栏
	private void updateFence(String id, String project_id, List<LatLng> pts) {
		if(StringUtils.isBlank(id) || StringUtils.isBlank(project_id)){
			return;
		}

		JSONArray jsonArray = new JSONArray();

		for (LatLng pt : pts) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("lat", pt.latitude);
			jsonObj.put("lon", pt.longitude);
			jsonArray.add(jsonObj);
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id",id);
		jsonObject.put("project_id",project_id);
		jsonObject.put("points", jsonArray);
		String jsonParams =jsonObject.toJSONString();

		String url = Constants.HTTP_BASE + "/api/update_geofence";
		ProgressDialog dialog = ProgressDialog.createDialog(this);
		dialog.show();

		OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
			@Override
			public void onSuccess(Response<String> response) {
				dialog.dismiss();
				AjaxResult jr = new AjaxResult(response.body());
				if(jr.getSuccess() == 1){
					AppToast.show(GeoFenceActivity.this,"设置围栏成功!");
					finish();
				} else {
					AppToast.show(GeoFenceActivity.this,"设置围栏失败!");
				}
			}
			@Override
			public void onError(Response<String> response) {
				dialog.dismiss();
				AppToast.show(GeoFenceActivity.this,"围栏设置出错!");
			}
		});
	}

	// 获取围栏
	private void getFence() {
		if(StringUtils.isBlank(fence_id)){
			return;
		}

		final Map<String,String> param = new HashMap<>();
		param.put("id",fence_id);
		String jsonParams = JSON.toJSONString(param);

		String url = Constants.HTTP_BASE + "/api/get_geofence";
		ProgressDialog dialog = ProgressDialog.createDialog(this);
		dialog.show();

		OkGo.<String>post(url).upJson(jsonParams).tag("request_tag").execute(new StringCallback() {
			@Override
			public void onSuccess(Response<String> response) {
				dialog.dismiss();
				AjaxResult jr = new AjaxResult(response.body());
				if(jr.getSuccess() == 1){
					JSONObject jsonObject = jr.getData();
					MapGeoFence fence = JSON.parseObject(JSON.toJSONString(jsonObject), MapGeoFence.class);
					fences.clear();
					fences.add(fence);
					refreshMap();
				} else {
					AppToast.show(GeoFenceActivity.this,"没有围栏!");
				}
			}
			@Override
			public void onError(Response<String> response) {
				dialog.dismiss();
				AppToast.show(GeoFenceActivity.this,"围栏获取出错!");
			}
		});
	}

	// 获取围栏内工人位置
	private void getEmpsLoc() {
		if(StringUtils.isBlank(fence_id)){
			return;
		}

		final Map<String,String> param = new HashMap<>();
		param.put("id",fence_id);
		String jsonParams = JSON.toJSONString(param);

		String url = Constants.HTTP_BASE + "/api/fence_today_emps";
		ProgressDialog dialog = ProgressDialog.createDialog(this);
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
					AppToast.show(GeoFenceActivity.this,"获取工人为空");
				}
			}
			@Override
			public void onError(Response<String> response) {
				dialog.dismiss();
				AppToast.show(GeoFenceActivity.this,"获取工人出错!");
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

			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pts.get(0), 16));// 设置指定的可视区域地图
			aMap.invalidate();//刷新地图
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
			bitmap = Bitmap.createScaledBitmap(bitmap,100,100, true);
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
					.zIndex(1.f).typeface(Typeface.DEFAULT_BOLD);

			if (loc.name != null){
				textOptions.text(loc.name);
			}
			aMap.addText(textOptions);
		} catch (NumberFormatException e){
			e.printStackTrace();
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
}
