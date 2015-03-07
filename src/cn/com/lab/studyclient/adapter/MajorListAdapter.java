package cn.com.lab.studyclient.adapter;

import java.util.ArrayList;

import cn.com.lab.studyclient.MainActivity;
import cn.com.lab.studyclient.R;
import cn.com.lab.studyclient.data.MajorDataEntry;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MajorListAdapter extends BaseAdapter {

	private ArrayList<MajorDataEntry> list = null;
	private Context context = null;
	private LayoutInflater layoutInflater = null;
	private int selection = 0;
	private int listFlag = 0;

	private static class ViewHolder {
		FrameLayout frameLayout = null;
		TextView textView = null;
		ImageView imageView = null;
	}

	public MajorListAdapter(int listFlag, ArrayList<MajorDataEntry> list,
			Context context) {

		this.listFlag = listFlag;
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
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return listFlag * 10000 + MainActivity.LIST_SECOND_FLAG_MAJOR * 1000
				+ position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;

		if (convertView == null) {

			convertView = layoutInflater.inflate(R.layout.item_majorlist, null);

			viewHolder = new ViewHolder();
			viewHolder.frameLayout = (FrameLayout) convertView
					.findViewById(R.id.frameLayout_majorItemBackground);
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.textView_majorItem);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.imageView_majorItemRight);

			convertView.setTag(viewHolder);

		} else
			viewHolder = (ViewHolder) convertView.getTag();

		viewHolder.textView.setText(list.get(position).getName());

		if (position == selection) {

			viewHolder.frameLayout.setBackgroundColor(context.getResources()
					.getColor(R.color.whitegray));
			viewHolder.frameLayout.setPadding(
					viewHolder.imageView.getPaddingLeft() * 2, 0, 0, 0);
			viewHolder.textView.setFocusable(true);
			viewHolder.textView.setFocusableInTouchMode(true);
			viewHolder.textView.requestFocus();

		} else {

			viewHolder.frameLayout.setBackgroundColor(context.getResources()
					.getColor(R.color.white));
			viewHolder.frameLayout.setPadding(0, 0, 0, 0);
			viewHolder.textView.setFocusable(false);
			viewHolder.textView.setFocusableInTouchMode(false);

		}

		return convertView;

	}

}
