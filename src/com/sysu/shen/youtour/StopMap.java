package com.sysu.shen.youtour;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.LatLngBounds.Builder;
import com.amap.api.search.core.AMapException;
import com.amap.api.search.core.LatLonPoint;
import com.amap.api.search.route.Route;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;

public class StopMap extends FragmentActivity implements OnMarkerClickListener {

	private AMap mMap;
	private UiSettings mUiSettings;

	private String stopslistString;
	private JSONArray stopsJsonArray;
	private ArrayList<LatLonPoint> LatLonPointList = new ArrayList<LatLonPoint>();

	private List<Route> routeResult;
	private Route route;
	private int mode = Route.WalkDefault;
	private Route.FromAndTo fromAndTo;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.stop_map);
		Bundle extras = getIntent().getExtras();
		stopslistString = extras.getString("stopsJarray");
		// 初始化地图
		setUpMap();
		new drawLineAsynTack().execute();
		

	}

	private class drawLineAsynTack extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(StopMap.this);
			mProgressDialog.setTitle("正在查询附近线路"); // 设置标题
			mProgressDialog.setMessage("附近线路马上为你呈现，请耐心等待..."); // 设置body信息
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(String... arg0) {
			// 分析json得到坐标生成marker
			phraseJsonToMarker();
			// 根据得到的坐标话线
			drawLine();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// 将视图放大到所有maker都能看到
			loadMarker();
			mProgressDialog.dismiss();
		}

	}

	// 将搜索得到的latLonPoint转换成LatLng才能添加的地图上
	private ArrayList<LatLng> convertArrList(LatLonPoint[] shapes) {
		ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
		for (LatLonPoint point : shapes) {
			LatLng latLngTemp = SearchPointConvert(point);
			lineShapes.add(latLngTemp);
		}
		return lineShapes;
	}

	public void drawLine() {
		LatLonPoint preStop;
		LatLonPoint nextStop;
		for (int i = 0; i < LatLonPointList.size() - 1; i++) {
			preStop = LatLonPointList.get(i);
			nextStop = LatLonPointList.get(i + 1);
			fromAndTo = new Route.FromAndTo(preStop, nextStop);
			try {
				routeResult = Route.calculateRoute(StopMap.this, fromAndTo,
						mode);
				if (routeResult != null && routeResult.size() > 0) {
					route = routeResult.get(0);
					if (route != null) {
						for (int j = 0; j < route.getStepCount(); j++) {
							mMap.addPolyline((new PolylineOptions())
									.addAll(convertArrList(route.getStep(j)
											.getShapes()))
									.color(Color.argb(180, 54, 114, 227))
									.width(5.9F));
						}
					}
				}
			} catch (AMapException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private LatLng SearchPointConvert(LatLonPoint latLonPoint) {
		return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
	}

	private void phraseJsonToMarker() {
		try {
			stopsJsonArray = new JSONArray(stopslistString);
			for (int i = 0; i < stopsJsonArray.length(); i++) {
				double longitude = stopsJsonArray.getJSONObject(i)
						.getJSONArray("locate").getDouble(0);
				double latitude = stopsJsonArray.getJSONObject(i)
						.getJSONArray("locate").getDouble(1);
				LatLonPoint llp = new LatLonPoint(longitude, latitude);
				LatLonPointList.add(llp);
				mMap.addMarker(new MarkerOptions()
						.position(SearchPointConvert(llp))
						.title(stopsJsonArray.getJSONObject(i).getString(
								"stopName"))
						.snippet(i + "")
						.icon(BitmapDescriptorFactory
								.fromBitmap(getBitMap("i"))));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadMarker() {
		// 设置所有maker显示在View中
		Builder builder = new Builder();
		for (int i = 0; i < LatLonPointList.size(); i++) {
			builder.include(SearchPointConvert(LatLonPointList.get(i)));
		}
		try {
			LatLngBounds bounds = builder.build();
			mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		Log.i("marker",
				"title:" + arg0.getTitle() + " snippet:" + arg0.getSnippet());
		return false;
	}

	public void backClicked(View v) {
		this.finish();
	}

	private void setUpMap() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (mMap != null) {
				mUiSettings = mMap.getUiSettings();
				mUiSettings.setAllGesturesEnabled(true);
				mUiSettings.setCompassEnabled(true);
				mUiSettings.setMyLocationButtonEnabled(false);
				mUiSettings.setScaleControlsEnabled(true);
				mUiSettings.setZoomControlsEnabled(true);
				mMap.setOnMarkerClickListener(this);
			}
		}
	}

	public Bitmap getBitMap(String text) {
		Bitmap bitmap = BitmapDescriptorFactory.fromResource(R.drawable.line)
				.getBitmap();
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight());
		Canvas canvas = new Canvas(bitmap);
		TextPaint textPaint = new TextPaint();
		textPaint.setTextSize(20f);
		textPaint.setColor(Color.BLACK);
		canvas.drawText(text, 20, 30, textPaint);// 设置bitmap上面的文字位置
		return bitmap;
	}

}
