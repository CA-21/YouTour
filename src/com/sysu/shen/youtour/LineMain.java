package com.sysu.shen.youtour;

import org.json.JSONException;
import org.json.JSONObject;

import com.sysu.shen.util.ImageLoader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class LineMain extends Activity {
	private ImageLoader imageLoader1;
	private ImageLoader imageLoader2;
	private ImageView lineThumb;
	private TextView lineAddress;
	private TextView lineTitle;
	private RatingBar rateBar;
	private TextView lineSummary;
	private TextView stopNum;
	private TextView lineTime;
	private TextView lineLength;
	private TextView lineLanguage;

	private ImageView authorThumb;
	private TextView authorName;
	private TextView authorBio;
	private TextView authorType;

	private String lineString;
	private JSONObject lineJson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_main);
		Bundle extras = getIntent().getExtras();
		lineString = extras.getString("lineString");
		try {
			lineJson = new JSONObject(lineString);
		} catch (JSONException e) {
			Log.e("linemain", "praselinejsonerror:" + e.toString());
		}
		initView();
		initValue();
	}

	private void initView() {
		imageLoader1 = new ImageLoader(this.getApplicationContext());
		imageLoader2 = new ImageLoader(this.getApplicationContext());
		lineThumb = (ImageView) findViewById(R.id.line_image);
		lineTitle = (TextView) findViewById(R.id.title);
		lineAddress = (TextView) findViewById(R.id.adress);
		rateBar = (RatingBar) findViewById(R.id.rating_bar);
		stopNum = (TextView) findViewById(R.id.total_stop);
		lineTime = (TextView) findViewById(R.id.total_time);
		lineLength = (TextView) findViewById(R.id.total_length);
		lineLanguage = (TextView) findViewById(R.id.line_language);
		lineSummary = (TextView) findViewById(R.id.line_summary);
		authorThumb = (ImageView) findViewById(R.id.author_image);
		authorName = (TextView) findViewById(R.id.author_name);
		authorBio = (TextView) findViewById(R.id.author_bio);
		authorType = (TextView) findViewById(R.id.autor_type);

	}

	private void initValue() {
		try {
			imageLoader1.DisplayImage(lineJson.getString("coverThumbnail"),
					lineThumb);
			imageLoader2.DisplayImage(lineJson.getString("authorThumb"),
					authorThumb);
			lineAddress.setText(lineJson.getString("address"));
			lineTitle.setText(lineJson.getString("lineName"));
			Float score = Float.parseFloat(lineJson.getString("totalScore"))
					/ Float.parseFloat(lineJson.getString("totalPeople"));
			rateBar.setRating(score / 2);
			lineSummary.setText(lineJson.getString("lineSummary"));
			int stopNumInt = lineJson.getJSONArray("stops").length();
			stopNum.setText(stopNumInt + "");
			lineTime.setText(timeConvert(Integer.parseInt(lineJson
					.getString("duration"))));
			lineLength.setText(lineJson.getString("lineLength") + "km");
			lineLanguage.setText(lineJson.getString("language"));
			authorName.setText(lineJson.getString("author"));
			authorBio.setText(lineJson.getString("authorBio"));
			authorType.setText(lineJson.getString("authorType"));

		} catch (JSONException e) {
			Log.v("linemain", "jsonegeterroe" + e.toString());
		}

	}

	public String timeConvert(int time) {
		String timeString = "";
		if (time / 24 / 60 != 0)
			timeString = timeString + time / 24 / 60 + "d";
		if (time / 60 % 24 != 0)
			timeString = timeString + time / 60 % 24 + 'h';
		if (time % 60 != 0)
			timeString = timeString + time % 60 + 'm';
		return timeString;
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
	 * 点击收藏
	 * 
	 * @param v
	 */
	public void collectBttonClicked(View v) {

	}

	/**
	 * 点击线路更多
	 * 
	 * @param v
	 */
	public void stopDetailClicked(View v) {

	}

	/**
	 * 点击作者更多
	 * 
	 * @param v
	 */
	public void authorMoreClicked(View v) {

	}

}
