package com.sysu.youtour.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sysu.shen.youtour.R;
import com.sysu.youtour.dao.JSONFunctions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MytourTabDownload extends ListFragment {

    private Myadapter         adapter;

    private ArrayList<String> jarray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mytourtab_download, container, false);
        return v;
    }

    @Override
    public void onResume() {
        initView();
        super.onResume();
    }

    public void initView() {

        String line = null;
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        jarray = JSONFunctions.getDownloadedJSONFromFile(getActivity());
        if (jarray != null) {
            Log.v("log_tag", "jarray.length():" + jarray.size());
            try {
                for (int i = 0; i < jarray.size(); i++) {
                    line = jarray.get(i).substring(1, jarray.get(i).length() - 1);
                    HashMap<String, String> lineMap = JSONFunctions.praseJSONToMap(new JSONObject(line));
                    mylist.add(lineMap);
                }
            } catch (Exception e) {
                Log.v("log_tag", "analyseJsonexception:" + e.toString());
            }
            adapter = new Myadapter(this.getActivity(), mylist);
            setListAdapter(adapter);
            getListView().setOnItemClickListener(new OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent it = new Intent(getActivity().getBaseContext(), LineMain.class);
                    it.putExtra("lineString", jarray.get(position).substring(1, jarray.get(position).length() - 1));
                    startActivity(it);

                }
            });

        }
    }
}
