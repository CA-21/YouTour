package com.sysu.youtour.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.sysu.shen.youtour.R;
import com.sysu.youtour.dao.JSONFunctions;
import com.sysu.youtour.util.GlobalConst;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

public class Welcome extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("isfrist_file", this.MODE_PRIVATE);
        boolean isFirst = preferences.getBoolean("isfrist", true);
        if (isFirst) {
            createDeskShortCut();
            Toast.makeText(this, "已创建快捷方式", Toast.LENGTH_SHORT).show();
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isfrist", false);
        editor.commit();

        setContentView(R.layout.welcome);
        new GetPropertiesAsynTack(this).execute();

    }

    public void createDeskShortCut() {
        // 创建快捷方式的Intent
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建 ，如果重复的话就会有多个快捷方式了
        shortcutIntent.putExtra("duplicate", false);
        // 这个就是应用程序图标下面的名称
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        // 快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        Intent intent = new Intent(getApplicationContext(), Welcome.class); // 这个MainActivity是调用此方法的Activity
        // 下面两个属性是为了当应用程序卸载时桌面 上的快捷方式会删除
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        // 点击快捷图片，运行的程序主入口
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // 最后一步就是发送广播
        sendBroadcast(shortcutIntent);
    }

    private class GetPropertiesAsynTack extends AsyncTask<Void, Void, String> {

        /**
         * 
         */
        private static final String HOST_IP = "hostIp";

        public Activity             activity;

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
                // 从缓存中读取hostip
                result = JSONFunctions.loadFromLocal(activity, HOST_IP, GlobalConst.SDCARD_HOSTIP_DIR);
                InputStream in = new ByteArrayInputStream(result.getBytes());
                Properties p = new Properties();
                try {
                    p.load(in);
                    GlobalConst.HOST = (String) p.get("host_ip");
                    Log.d("ip", GlobalConst.HOST);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

                // 把刚刚得到的hostip缓存到本地
                JSONFunctions.saveInLocal(activity, HOST_IP, result, GlobalConst.SDCARD_HOSTIP_DIR);
            }
            Intent it = new Intent(Welcome.this, MainActivity.class);
            startActivity(it);
            Welcome.this.finish();
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
