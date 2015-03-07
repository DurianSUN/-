package cn.com.lab.studyclient.adapter;

import java.util.ArrayList;

import cn.com.lab.studyclient.R;
import cn.com.lab.studyclient.data.StudyTitleListDataEntry;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StudyTitleListAdapter extends BaseAdapter {

	private ArrayList<StudyTitleListDataEntry> list = null;
	private Context context = null;
	private LayoutInflater layoutInflater = null;

	private static class ViewHolder {
		TextView titleTv = null;
		TextView contentTv = null;
		TextView visitorTv = null;
		TextView timeTv = null;
	}

	public StudyTitleListAdapter(ArrayList<StudyTitleListDataEntry> list,
			Context context) {

		this.list = list;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);

	}

	public void setList(ArrayList<StudyTitleListDataEntry> list) {
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

			convertView = layoutInflater.inflate(R.layout.item_study_titlelist,
					null);

			viewHolder = new ViewHolder();
			viewHolder.titleTv = (TextView) convertView
					.findViewById(R.id.textView_studyTitleItem_title);
			viewHolder.contentTv = (TextView) convertView
					.findViewById(R.id.textView_studyTitleItem_content);
			viewHolder.visitorTv = (TextView) convertView
					.findViewById(R.id.textView_studyTitleItem_visitor);
			viewHolder.timeTv = (TextView) convertView
					.findViewById(R.id.textView_studyTitleItem_time);

			convertView.setTag(viewHolder);

		} else
			viewHolder = (ViewHolder) convertView.getTag();

		viewHolder.titleTv.setText(list.get(position).getTitle());
		viewHolder.contentTv.setText(list.get(position).getContent());
		viewHolder.visitorTv.setText(list.get(position).getVisitor());
		viewHolder.timeTv.setText(list.get(position).getTime());

		return convertView;

	}

}
