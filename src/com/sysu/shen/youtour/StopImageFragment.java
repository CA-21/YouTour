package com.sysu.shen.youtour;

import com.sysu.shen.util.ImageLoader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public final class StopImageFragment extends Fragment {
	public ImageLoader imageLoader;
	private String imageResourceUrl;

	public static StopImageFragment newInstance(String imageUrl) {
		StopImageFragment fragment = new StopImageFragment();
		fragment.imageResourceUrl = imageUrl;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ImageView image = new ImageView(getActivity());
		image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		image.setScaleType(ScaleType.CENTER_CROP);
		LinearLayout layout = new LinearLayout(getActivity());
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		layout.setGravity(Gravity.CENTER);
		layout.addView(image);
		imageLoader = new ImageLoader(getActivity());
		imageLoader.DisplayImage(imageResourceUrl, image);

		return layout;
	}
}
