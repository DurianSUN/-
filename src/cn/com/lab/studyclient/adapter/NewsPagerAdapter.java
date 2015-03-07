package cn.com.lab.studyclient.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class NewsPagerAdapter extends PagerAdapter {

	private ImageView[] list = null;

	public NewsPagerAdapter(ImageView[] list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(list[position]);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		container.addView(list[position]);
		return list[position];

	}

}
