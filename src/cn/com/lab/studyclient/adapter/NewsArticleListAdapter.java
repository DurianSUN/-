package cn.com.lab.studyclient.adapter;

import cn.com.lab.studyclient.MainActivity;
import cn.com.lab.studyclient.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsArticleListAdapter extends BaseAdapter {

	private String[] list = null;
	private Context context = null;
	private LayoutInflater layoutInflater = null;
	private int selection = 0;

	private static class ViewHolder {
		FrameLayout frameLayout = null;
		TextView textView = null;
		ImageView imageView = null;
	}

	public NewsArticleListAdapter(String[] list, Context context) {

		this.list = list;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);

	}

	public void setSelection(int position) {
		selection = position;
	}

	public int getSelection() {
		return selection;
	}

	@Override
	public int getCount() {
		return list.length;
	}

	@Override
	public Object getItem(int position) {
		return list[position];
	}

	@Override
	public long getItemId(int position) {
		return MainActivity.LIST_FIRST_FLAG_NEWS_ARTICLE * 10000 + position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;

		if (convertView == null) {

			convertView = layoutInflater.inflate(R.layout.item_newsarticlelist,
					null);

			viewHolder = new ViewHolder();
			viewHolder.frameLayout = (FrameLayout) convertView
					.findViewById(R.id.frameLayout_newsArticeItemBackground);
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.textView_newsArticleItem);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.imageView_newsArticleItemRight);

			convertView.setTag(viewHolder);

		} else
			viewHolder = (ViewHolder) convertView.getTag();

		viewHolder.textView.setText(list[position]);

		if (position == selection) {

			viewHolder.frameLayout.setBackgroundColor(context.getResources()
					.getColor(R.color.whitegray));
			viewHolder.frameLayout.setPadding(
					viewHolder.imageView.getPaddingLeft() * 2, 0, 0, 0);
			viewHolder.textView.setFocusable(true);
			viewHolder.textView.setFocusableInTouchMode(true);
			viewHolder.textView.requestFocus();
			viewHolder.imageView.setVisibility(View.VISIBLE);

		} else {

			viewHolder.frameLayout.setBackgroundColor(context.getResources()
					.getColor(R.color.white));
			viewHolder.frameLayout.setPadding(0, 0, 0, 0);
			viewHolder.textView.setFocusable(false);
			viewHolder.textView.setFocusableInTouchMode(false);
			viewHolder.imageView.setVisibility(View.GONE);

		}

		return convertView;

	}

}
