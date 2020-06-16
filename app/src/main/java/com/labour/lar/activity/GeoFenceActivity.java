package com.labour.lar.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polygon;
import com.amap.api.maps2d.model.PolygonOptions;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.util.AjaxResult;
import com.labour.lar.util.StringUtils;
import com.labour.lar.widget.ProgressDialog;
import com.labour.lar.widget.toast.AppToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import java.util.ArrayList;
import java.util.List;
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

    @BindView(R.id.map)
    MapView mapView;

	private AMap aMap;

	private List<LatLng> listPts = new ArrayList<>();
	private Polygon polyline;
	private MarkerOptions markerOption;
	private boolean isdrawing;
	private Point lastPt = new Point();
	private int state;
	private String fence_id;
	private String project_id;

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
					setFence(project_id, listPts);
				} else {
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

	public void setFence(String project_id, List<LatLng> pts) {
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

	public void updateFence(String id, String project_id, List<LatLng> pts) {
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
