package com.sysu.shen.youtour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

public class SampleCirclesSnap extends BaseSampleActivity {
	private String stopString;
	private String stopNameString;
	private String stopDetailString;
	private JSONObject stopJSON;
	private TextView stopName;
	private TextView stopDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stop_main);
		Bundle extras = getIntent().getExtras();
		stopString = extras.getString("stopString");
		try {
			stopJSON = new JSONObject(stopString);
			stopNameString = stopJSON.getString("stopName");
			stopDetailString = stopJSON.getString("stopDes");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		initView();
		initValue();

		mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

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
	}

	private void initValue() {
		stopName.setText(stopNameString);
		stopDetail.setText(stopDetailString);

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