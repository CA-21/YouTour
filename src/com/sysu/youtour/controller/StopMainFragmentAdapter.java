package com.sysu.youtour.controller;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.viewpagerindicator.IconPagerAdapter;

class StopMainFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    private ArrayList<String> images = new ArrayList<String>();

    private int               mCount;

    private String            lineID;

    public StopMainFragmentAdapter(FragmentManager fm, ArrayList<String> stopImagesArray, String ID) {
        super(fm);
        images = stopImagesArray;
        mCount = images.size();
        lineID = ID;
    }

    @Override
    public Fragment getItem(int position) {
        return StopImageFragment.newInstance(images.get(position), lineID);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getIconResId(int index) {
        // TODO Auto-generated method stub
        return 0;
    }
}