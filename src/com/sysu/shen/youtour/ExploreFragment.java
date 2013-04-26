package com.sysu.shen.youtour;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sysu.shen.util.GlobalConst;
import com.sysu.shen.util.JSONFunctions;
import com.sysu.shen.util.Myadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ExploreFragment extends ListFragment {
	// 标记没有联网
	public final static int NO_NETWORK = 0;
	// ListView list;
	private Myadapter adapter;
	private JSONArray jarray;
	String URLString = GlobalConst.EXPLORE_URL;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main_tab_explore, container, false);

		// 异步更新列表
		new GetJSONAsynTack(this.getActivity()).execute(URLString);

		return v;

	}

	// 处理线程中抛出的massage
	private Handler mhandle = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NO_NETWORK:
				Toast.makeText(ExploreFragment.this.getActivity(),
						"连接网络才能看到更多哦！", Toast.LENGTH_LONG).show();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	private class GetJSONAsynTack extends AsyncTask<String, Void, Void> {

		public Activity activity;

		public GetJSONAsynTack(Activity activity2) {
			activity = activity2;
		}

		@Override
		protected Void doInBackground(String... strings) {
			String URLString = strings[0];

			// 判断是否联网
			final ConnectivityManager conMgr = (ConnectivityManager) activity
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
			if (activeNetwork != null && activeNetwork.isConnected()) {
				jarray = JSONFunctions.getJsonFromNetwork(activity, URLString);
			} else {
				Message m = new Message();
				m.what = NO_NETWORK;
				mhandle.sendMessage(m);
				jarray = JSONFunctions.getJSONFromFile(activity, URLString);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			JSONObject line = null;
			ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
			if (jarray != null) {
				Log.v("log_tag", "jarray.length():" + jarray.length());
				try {
					for (int i = 0; i < jarray.length(); i++) {
						line = jarray.getJSONObject(i);
						HashMap<String, String> lineMap = JSONFunctions
								.praseJSONToMap(line);
						mylist.add(lineMap);
					}
				} catch (Exception e) {
					Log.v("log_tag", "analyseJsonexception:" + e.toString());
				}
				adapter = new Myadapter(activity, mylist);
				setListAdapter(adapter);
				getListView().setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent it = new Intent(getActivity().getBaseContext(), LineMain.class);
						try {
							it.putExtra("lineString",
									jarray.getJSONObject(position).toString());
						} catch (JSONException e) {
							Log.v("exploreFragment",
									"get onelinejsonobject exception:"
											+ e.toString());
						}
						startActivity(it);

					}
				});

			}
			super.onPostExecute(result);
		}
	}

}
