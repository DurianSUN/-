package cn.com.lab.studyclient.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

	private Fragment[] fragments = null;

	public MainFragmentPagerAdapter(Fragment[] fragments,
			FragmentManager fragmentManager) {

		super(fragmentManager);
		this.fragments = fragments;

	}

	@Override
	public int getCount() {
		return fragments.length;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments[arg0];
	}

}
