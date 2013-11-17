package com.sysu.youtour.controller;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.sysu.shen.youtour.R;
import com.sysu.youtour.dao.ImageLoader;
import com.sysu.youtour.util.GlobalConst;
import com.sysu.youtour.util.PreferencesUtils;
import com.sysu.youtour.util.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class LineMain extends Activity {
    private ImageLoader       imageLoader1;

    private ImageLoader       imageLoader2;

    private ImageView         lineThumb;

    private TextView          lineAddress;

    private TextView          lineTitle;

    private RatingBar         rateBar;

    private TextView          lineSummary;

    private TextView          stopNum;

    private TextView          lineTime;

    private TextView          lineLength;

    private TextView          lineLanguage;

    private ImageView         authorThumb;

    private TextView          authorName;

    private TextView          authorBio;

    private TextView          authorType;

    private String            lineString;

    private JSONObject        lineJson;

    private int               stopNumInt;

    private String            lineID;

    private ImageView         downloadButton;

    private ArrayList<String> needDownloadURLS;

    private DownloadManager   downloadManager;

    private CompleteReceiver  completeReceiver;

    private int               nowDownTotal = 0;

    private Thread            downLoadThread;

    @SuppressLint("InlinedApi")
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

        completeReceiver = new CompleteReceiver();
        /** register download success broadcast **/
        registerReceiver(completeReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        initView();
        initValue();
    }

    private void initView() {
        try {
            lineID = lineJson.getString(GlobalConst._ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        imageLoader1 = new ImageLoader(this.getApplicationContext(), GlobalConst.LINE_THUMBNAIL_DIR_NAME);
        imageLoader2 = new ImageLoader(this.getApplicationContext(), lineID);
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
        downloadButton = (ImageView) findViewById(R.id.download);

    }

    private void initValue() {
        try {
            imageLoader1.DisplayImage(lineJson.getString("coverThumbnail"), lineThumb);
            imageLoader2.DisplayImage(lineJson.getString("authorImage"), authorThumb);
            lineAddress.setText(lineJson.getString("mapAddress"));
            lineTitle.setText(lineJson.getString("lineName"));
            Float score = Float.parseFloat(lineJson.getString("totalScore"))
                    / Float.parseFloat(lineJson.getString("totalPeople"));
            rateBar.setRating(score / 2);
            lineSummary.setText("\t" + lineJson.getString("lineSummary"));
            stopNumInt = lineJson.getJSONArray("stops").length();
            stopNum.setText(stopNumInt + "");
            lineTime.setText(timeConvert(Integer.parseInt(lineJson.getString("duration"))));
            lineLength.setText(lineJson.getString("lineLength") + "km");
            lineLanguage.setText(lineJson.getString("language"));
            authorName.setText(lineJson.getString("author"));
            authorBio.setText(lineJson.getString("authorBio"));
            authorType.setText(lineJson.getString("authorType"));
            // Log.i("lineJson", lineJson.toString());
            needDownloadURLS = Utils.checkLineDownloaded(lineID);
            if (needDownloadURLS.size() == 0) {
                downloadButton.setBackgroundResource(R.drawable.selector_downloadedbutton);
            } else {
                downloadButton.setBackgroundResource(R.drawable.selector_downloadbutton);
            }

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
    public void collectClicked(View v) {

    }

    /**
     * 点击线路更多
     * 
     * @param v
     */
    public void stopDetailClicked(View v) {
        if (stopNumInt == 0) {
            Toast.makeText(this, "这个线路没有站点信息哦", Toast.LENGTH_SHORT).show();
            return;
        }
        // 判断是否wifi环境
        ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileCon = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileCon.isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setTitle("设置WIFI更精彩");
            builder.setMessage("小游检测到您正使用移动网络，站点包括很多图片和音频哦，还是设置一下WIFI吧！");
            builder.setPositiveButton("马上设置", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    final ComponentName cn = new ComponentName("com.android.settings",
                            "com.android.settings.wifi.WifiSettings");
                    intent.setComponent(cn);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("继续浏览", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Intent it = new Intent(LineMain.this, StopsList.class);
                    try {
                        it.putExtra("stopJArray", lineJson.getJSONArray("stops").toString());
                        it.putExtra("lineName", lineTitle.getText());
                        it.putExtra("lineID", lineID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(it);
                }
            });
            builder.create();
            builder.show();
        } else {
            Intent it = new Intent(LineMain.this, StopsList.class);
            try {
                it.putExtra("stopJArray", lineJson.getJSONArray("stops").toString());
                it.putExtra("lineName", lineTitle.getText());
                it.putExtra("lineID", lineID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            startActivity(it);
        }

    }

    /**
     * 点击作者更多
     * 
     * @param v
     */
    public void authorMoreClicked(View v) {

    }

    @SuppressLint("NewApi")
    public void downloadClicked(View v) {

        if (needDownloadURLS.size() == 0) {
            Toast.makeText(this, "已下载！", Toast.LENGTH_SHORT).show();
        } else {
            if (needDownloadURLS.size() != nowDownTotal) {
                // 判断是否wifi环境
                ConnectivityManager connManager = (ConnectivityManager) this
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnected()) {

                    NetworkInfo mobileCon = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    if (mobileCon.isConnected()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setIcon(android.R.drawable.ic_dialog_info);
                        builder.setTitle("设置WIFI省流量");
                        builder.setMessage("站点包括很多图片和音频哦，离线下载还是先设置一下WIFI吧！");
                        builder.setNeutralButton("马上设置", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                final ComponentName cn = new ComponentName("com.android.settings",
                                        "com.android.settings.wifi.WifiSettings");
                                intent.setComponent(cn);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                        builder.create();
                        builder.show();
                    } else {
                        downloadButton.setClickable(false);
                        Toast.makeText(getApplicationContext(), "正在离线下载，请稍后。。", Toast.LENGTH_LONG).show();
                        downLoadThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                downloadAllResource(needDownloadURLS);
                            }
                        });
                        downLoadThread.start();

                    }
                } else {
                    Toast.makeText(this, "离线下载需要连上wifi哦！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * @param arrayList
     */
    @SuppressLint("NewApi")
    protected void downloadAllResource(ArrayList<String> urls) {
        for (int i = 0; i < urls.size(); i++) {
            downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            String url = urls.get(i);
            String downloadDir = null;
            String downloadName = null;
            if (url.split("-")[0].equals(GlobalConst.LINE_THUNMNAIL_IDENTIFY)) {
                downloadDir = "youtour/cache/" + GlobalConst.LINE_THUMBNAIL_DIR_NAME.hashCode();
            } else {
                downloadDir = "youtour/cache/" + lineID.hashCode();
            }
            downloadName = url.split("-")[1];
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadName));
            request.setDestinationInExternalPublicDir(downloadDir, downloadName.hashCode() + "");
            request.setTitle("正在下载" + lineTitle.getText() + "资源" + i);
            request.setDescription(url.split("-")[0]);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            long downloadId = downloadManager.enqueue(request);
            PreferencesUtils.putLong(this.getApplicationContext(), lineID + "-" + i, downloadId);
        }
    }

    class CompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            nowDownTotal++;
            if (nowDownTotal == needDownloadURLS.size()) {
                downloadButton.setBackgroundResource(R.drawable.selector_downloadedbutton);
                downloadButton.setClickable(true);
                Toast.makeText(getApplicationContext(), "已完成下载！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(completeReceiver);
    }
}
