package cn.com.lab.studyclient.adapter;

import java.util.ArrayList;

import cn.com.lab.studyclient.R;
import cn.com.lab.studyclient.data.NewsTitleListDataEntry;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewsTitleListAdapter extends BaseAdapter {

	private ArrayList<NewsTitleListDataEntry> list = null;
	private Context context = null;
	private LayoutInflater layoutInflater = null;

	private static class ViewHolder {
		TextView titleTv = null;
		TextView contentTv = null;
		TextView timeTv = null;
	}

	public NewsTitleListAdapter(ArrayList<NewsTitleListDataEntry> list,
			Context context) {

		this.list = list;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);

	}

	public void setList(ArrayList<NewsTitleListDataEntry> list) {
		this.list = list;
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
		return list.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;

		if (convertView == null) {

			convertView = layoutInflater.inflate(R.layout.item_news_titlelist,
					null);

			viewHolder = new ViewHolder();
			viewHolder.titleTv = (TextView) convertView
					.findViewById(R.id.textView_newsTitleItem_title);
			viewHolder.contentTv = (TextView) convertView
					.findViewById(R.id.textView_newsTitleItem_content);
			viewHolder.timeTv = (TextView) convertView
					.findViewById(R.id.textView_newsTitleItem_time);

			convertView.setTag(viewHolder);

		} else
			viewHolder = (ViewHolder) convertView.getTag();

		viewHolder.titleTv.setText(list.get(position).getTitle());
		viewHolder.contentTv.setText(list.get(position).getContent());
		viewHolder.timeTv.setText(list.get(position).getTime());

		return convertView;

	}

}
