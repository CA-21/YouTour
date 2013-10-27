package com.sysu.shen.youtour;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.sysu.shen.util.GlobalConst;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Toast;

public class Welcome extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		new GetPropertiesAsynTack(this).execute();
		ImageView logo = (ImageView) findViewById(R.id.fengqilogo);
		Animation logo_animation = AnimationUtils.loadAnimation(Welcome.this,
				R.anim.push_left_in);
		logo.setAnimation(logo_animation);
		logo_animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Intent it = new Intent(Welcome.this, MainActivity.class);
				startActivity(it);
				Welcome.this.finish();
				// overridePendingTransition(R.anim.slide_out_left,
				// R.anim.slide_in_right);
			}
		});

	}

	private class GetPropertiesAsynTack extends AsyncTask<Void, Void, String> {

		public Activity activity;

		public GetPropertiesAsynTack(Activity activity1) {
			activity = activity1;
		}

		@Override
		protected String doInBackground(Void... nothing) {

			// 判断是否联网
			final ConnectivityManager conMgr = (ConnectivityManager) activity
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
			if (activeNetwork != null && activeNetwork.isConnected()) {
				return loadHostIp();
			} else {
				return null;
			}

		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				Toast.makeText(this.activity, "无法建立网络连接", Toast.LENGTH_SHORT).show();
			} else {
				InputStream in = new ByteArrayInputStream(result.getBytes());
				Properties p = new Properties();
				try {
					p.load(in);
					GlobalConst.HOST = (String) p.get("host_ip");
					Log.d("ip", GlobalConst.HOST);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String loadHostIp() {
		DefaultHttpClient client = new DefaultHttpClient();
		String responseBody = null;
		HttpResponse httpResponse = null;
		ByteArrayOutputStream out = null;
		int statusCode;

		try {
			HttpGet httpget = new HttpGet(GlobalConst.HOST_URL);
			httpResponse = client.execute(httpget);
			statusCode = httpResponse.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				out = new ByteArrayOutputStream();
				httpResponse.getEntity().writeTo(out);
				responseBody = out.toString("UTF-8");
			}

		} catch (Exception e) {
			Log.e("welcome", e.toString());

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					Log.e("welcome", e.toString());
				}
			}
		}
		return responseBody;

	}
}
