package com.sysu.shen.youtour;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

public class StopMain extends BaseSampleActivity {
	private String stopslistString;
	private String stopNameString;
	private String stopDetailString;
	private JSONArray stopsJSONArray;
	private JSONObject stopJSON;
	private TextView stopName;
	private TextView stopDetail;
	private String position;
	private TextView stopNum;
	private ArrayList<String> stopImagesArray;
	private JSONArray stopImages;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stop_main);
		Bundle extras = getIntent().getExtras();
		stopslistString = extras.getString("stopsJarray");
		position = extras.getString("position");
		try {
			stopsJSONArray = new JSONArray(stopslistString);
			stopJSON = stopsJSONArray.getJSONObject(Integer.parseInt(position)-1);
			stopNameString = stopJSON.getString("stopName");
			stopDetailString = stopJSON.getString("stopDes");
			stopImages = stopJSON.getJSONArray("stopImages");
			stopImagesArray = new ArrayList<String>();
			for (int i = 0; i < stopImages.length(); i++) {
				stopImagesArray.add(stopImages.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		initView();
		initValue();

		mAdapter = new StopMainFragmentAdapter(getSupportFragmentManager(),stopImagesArray);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator = indicator;
		indicator.setViewPager(mPager);
		indicator.setSnap(true);
	}

	private void initView() {
		stopName = (TextView) findViewById(R.id.stop_name);
		stopDetail = (TextView) findViewById(R.id.stop_detail);
		stopNum = (TextView) findViewById(R.id.number);
	}

	private void initValue() {
		stopName.setText(stopNameString);
		stopDetail.setText(stopDetailString);
		stopNum.setText(position);

	}

	/**
	 * 点击返回
	 * 
	 * @param v
	 */
	public void backClicked(View v) {
		this.finish();
	}

	/**
	 * 点击地图
	 * 
	 * @param v
	 */
	public void mapclicked(View v) {

	}

	/**
	 * 点击前一个站点
	 * 
	 * @param v
	 */
	public void prebuttonClicked(View v) {

	}

	/**
	 * 点击后一个站点
	 * 
	 * @param v
	 */
	public void nextbuttonClicked(View v) {

	}

	/**
	 * 点击分享
	 * 
	 * @param v
	 */
	public void shareclicked(View v) {

	}
}