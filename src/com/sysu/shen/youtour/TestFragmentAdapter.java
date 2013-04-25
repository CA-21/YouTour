package com.sysu.shen.youtour;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.viewpagerindicator.IconPagerAdapter;

class TestFragmentAdapter extends FragmentPagerAdapter implements
		IconPagerAdapter {
	private int[] Images = new int[] { R.drawable.blue,
			R.drawable.green,
			R.drawable.red,
			R.drawable.yellow

	};

	private int mCount = Images.length;

	public TestFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return TestFragment.newInstance(Images[position]);
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