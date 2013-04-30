package com.sysu.shen.youtour;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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
	private ImageButton btn_play;
	private SeekBar seekBar_volume;

	private MediaPlayer mediaPlayer;
	private MediaMetadataRetriever retriever;
	private String audioURL;
	private AudioManager audioManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stop_main);
		Bundle extras = getIntent().getExtras();
		stopslistString = extras.getString("stopsJarray");
		position = extras.getString("position");
		try {
			stopsJSONArray = new JSONArray(stopslistString);
			stopJSON = stopsJSONArray
					.getJSONObject(Integer.parseInt(position) - 1);
			stopNameString = stopJSON.getString("stopName");
			stopDetailString = stopJSON.getString("stopDes");
			stopImages = stopJSON.getJSONArray("stopImages");
//			audioURL = stopJSON.getString("stopAudio");
			audioURL = "http://support.k-designed.net/test-z/music/Accordossie.mp3";
			stopImagesArray = new ArrayList<String>();

			for (int i = 0; i < stopImages.length(); i++) {
				stopImagesArray.add(stopImages.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		initView();
		initValue();

		mAdapter = new StopMainFragmentAdapter(getSupportFragmentManager(),
				stopImagesArray);

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
		btn_play = (ImageButton) findViewById(R.id.btn_play);
		seekBar_volume = (SeekBar) findViewById(R.id.songProgressBar);
	}

	@SuppressLint("NewApi")
	private void initValue() {
		stopName.setText(stopNameString);
		stopDetail.setText(stopDetailString);
		stopNum.setText(position);

		retriever = new MediaMetadataRetriever();
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

		seekBar_volume.setMax(maxVolume);
		seekBar_volume.setProgress(curVolume);

		seekBar_volume
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
								progress, 0);

					}
				});

		btn_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Log.i("audioURL", audioURL);
					mediaPlayer.setDataSource(audioURL);
					retriever.setDataSource(audioURL);
					mediaPlayer.prepare(); // might take long! (for buffering,
											// etc)
					mediaPlayer.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

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