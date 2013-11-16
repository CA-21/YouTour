package com.sysu.youtour.controller;

import java.io.InputStream;

import com.sysu.shen.youtour.R;
import com.sysu.youtour.dao.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public final class StopImageFragment extends Fragment {
    private ImageLoader imageLoader;

    private String      imageResourceUrl;

    private String      lineID;

    public static StopImageFragment newInstance(String imageUrl, String ID) {
        StopImageFragment fragment = new StopImageFragment();
        fragment.imageResourceUrl = imageUrl;
        fragment.lineID = ID;
        fragment.imageLoader = new ImageLoader(fragment.getActivity(), fragment.lineID);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ImageView image = new ImageView(getActivity());
        image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        image.setScaleType(ScaleType.CENTER_CROP);
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.setGravity(Gravity.CENTER);
        layout.addView(image);
        // 判断是否wifi环境
        // ConnectivityManager connManager = (ConnectivityManager)
        // getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // NetworkInfo mWifi =
        // connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        // if (mWifi.isConnected()) {
        // new DownloadImageTask(image).execute(imageResourceUrl);
        // }
        // else{

        imageLoader.DisplayImage(imageResourceUrl, image);
        // }

        return layout;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            bmImage.setImageResource(R.drawable.loding_image);
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                Toast.makeText(getActivity(), "网络连接出现问题了T^T", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
