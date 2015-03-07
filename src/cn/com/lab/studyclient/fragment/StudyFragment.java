package cn.com.lab.studyclient.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.com.lab.studyclient.MainActivity;
import cn.com.lab.studyclient.R;
import cn.com.lab.studyclient.adapter.StudyTitleListAdapter;
import cn.com.lab.studyclient.data.StudyTitleListDataEntry;
import cn.com.lab.studyclient.runnable.DataTransferTask.DataTransferHandler;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

public class StudyFragment extends BackableFragment implements OnClickListener,
		OnItemClickListener {

	public static final int CURRENT_ORDER_BY_RECOMMEND = 0;
	public static final int CURRENT_ORDER_BY_TIME = 1;
	public static final int CURRENT_ORDER_BY_VISITOR = 2;

	private int currentOrderType = 0;
	private ArrayList<StudyTitleListDataEntry> currentContentList = null;

	private Handler handler = null;
	private Handler dataTransferHandler = null;

	public View currentView = null;
	public FrameLayout backgroundView = null;
	public WebView webView = null;
	public LinearLayout mainView = null;
	public Button orderBtn = null;
	public Button orderRecommendBtn = null;
	public Button orderTimeBtn = null;
	public Button orderVisitorBtn = null;
	public Button filterBtn = null;
	public Button searchBtn = null;
	public PopupWindow orderPopup = null;
	public PopupWindow filterPopup = null;
	public ListView contentView = null;

	public long currentSubjectId = 0;

	public class StudyHandler extends Handler {

		public StudyHandler() {
			super();
		}

		public StudyHandler(Callback arg0) {
			super(arg0);
		}

		public StudyHandler(Looper arg0) {
			super(arg0);
		}

		public StudyHandler(Looper arg0, Callback arg1) {
			super(arg0, arg1);
		}

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);

		}

	}

	public class DataLoadTask implements Runnable {

		@Override
		public void run() {

			if (MainActivity.dataTransferTask.getHandler() == null)
				handler.postDelayed(this, 200);
			else {

				dataTransferHandler = MainActivity.dataTransferTask
						.getHandler();
				Message message = dataTransferHandler.obtainMessage(
						DataTransferHandler.WHAT_SEND,
						DataTransferHandler.ARG1_SEND_STUDY_FRAGMENT,
						DataTransferHandler.ARG2_SEND_STUDY_FRAGMENT_LOAD,
						StudyFragment.this);
				Bundle bundle = new Bundle();
				bundle.putString("key", String.valueOf(currentSubjectId));
				bundle.putParcelableArrayList(String.valueOf(currentSubjectId),
						currentContentList);
				message.setData(bundle);
				message.sendToTarget();

			}

		}

	}

	public class InvalidateTask implements Runnable {

		@Override
		public void run() {

			Collections.sort(currentContentList,
					new Comparator<StudyTitleListDataEntry>() {

						@Override
						public int compare(StudyTitleListDataEntry lhs,
								StudyTitleListDataEntry rhs) {

							int lr = lhs.getRecommendStar();
							int rr = rhs.getRecommendStar();
							int lt = Integer.parseInt(lhs.getTime()
									.replace("年", "").replace("月", "")
									.replace("日", "").trim());
							int rt = Integer.parseInt(rhs.getTime()
									.replace("年", "").replace("月", "")
									.replace("日", "").trim());
							int lv = Integer.parseInt(lhs.getVisitor());
							int rv = Integer.parseInt(rhs.getVisitor());

							switch (currentOrderType) {

							case CURRENT_ORDER_BY_RECOMMEND:
								if (lr != rr)
									return rr - lr;
								else if (lt != rt)
									return rt - lt;
								else
									return rv - lv;

							case CURRENT_ORDER_BY_TIME:
								if (lt != rt)
									return rt - lt;
								else if (lr != rr)
									return rr - lr;
								else
									return rv - lv;

							case CURRENT_ORDER_BY_VISITOR:
								if (lv != rv)
									return rv - lv;
								else if (lt != rt)
									return rt - lt;
								else
									return rr - lr;

							default:
								break;

							}
							return 0;
						}

					});
			contentView.setAdapter(new StudyTitleListAdapter(
					currentContentList, getActivity()));

		}

	}

	public Handler getHandler() {
		return handler;
	}

	public void setCurrentSubjectId(long id) {

		if (currentView == webView) {

			backgroundView.removeAllViews();
			backgroundView.addView(mainView);
			currentView = mainView;

		}

		currentSubjectId = id;
		handler.post(new DataLoadTask());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_study, null);

		handler = new StudyHandler();

		backgroundView = (FrameLayout) view
				.findViewById(R.id.frameLayout_studyBackground);
		mainView = (LinearLayout) view
				.findViewById(R.id.linearLayout_study_main);
		currentView = mainView;

		orderBtn = (Button) view.findViewById(R.id.button_order);
		orderBtn.setOnClickListener(this);
		filterBtn = (Button) view.findViewById(R.id.button_filter);
		filterBtn.setOnClickListener(this);
		searchBtn = (Button) view.findViewById(R.id.button_search);
		searchBtn.setOnClickListener(this);
		contentView = (ListView) view
				.findViewById(R.id.listView_study_titleList);
		contentView.setOnItemClickListener(this);

		currentContentList = new ArrayList<StudyTitleListDataEntry>();
		handler.post(new DataLoadTask());

		return view;

	}

	@Override
	public boolean back() {

		if (currentView == mainView)
			return false;
		else if (webView.canGoBack()) {

			webView.goBack();
			return true;

		} else {

			backgroundView.removeAllViews();
			backgroundView.addView(mainView);
			currentView = mainView;
			return true;

		}

	}

	@Override
	public void onClick(View v) {

		if (!MainActivity.actionLock) {

			if (v == orderBtn) {

				if (orderPopup == null) {

					View orderView = getActivity().getLayoutInflater().inflate(
							R.layout.popupwindow_study_order, null);
					orderRecommendBtn = (Button) orderView
							.findViewById(R.id.button_orderByRecommend);
					orderRecommendBtn.setOnClickListener(this);
					orderTimeBtn = (Button) orderView
							.findViewById(R.id.button_orderByTime);
					orderTimeBtn.setOnClickListener(this);
					orderVisitorBtn = (Button) orderView
							.findViewById(R.id.button_orderByVisitor);
					orderVisitorBtn.setOnClickListener(this);
					orderPopup = new PopupWindow(orderView,
							orderBtn.getWidth() * 6 / 5,
							LayoutParams.WRAP_CONTENT);
					orderPopup.setFocusable(true);
					orderPopup.setBackgroundDrawable(new ColorDrawable(
							getResources().getColor(R.color.transparent)));
					orderPopup.setOutsideTouchable(true);

				}

				orderPopup.showAsDropDown(orderBtn,
						-orderBtn.getWidth() * 6 / 10, -orderPopup
								.getContentView().getPaddingTop());

			}

			if (v == orderRecommendBtn) {

				currentOrderType = CURRENT_ORDER_BY_RECOMMEND;
				handler.post(new InvalidateTask());
				orderPopup.dismiss();

			}

			if (v == orderTimeBtn) {

				currentOrderType = CURRENT_ORDER_BY_TIME;
				handler.post(new InvalidateTask());
				orderPopup.dismiss();

			}

			if (v == orderVisitorBtn) {

				currentOrderType = CURRENT_ORDER_BY_VISITOR;
				handler.post(new InvalidateTask());
				orderPopup.dismiss();

			}

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		String url = ((StudyTitleListDataEntry) (parent.getAdapter())
				.getItem(position)).getWebUrl();
		webView = new WebView(getActivity());
		webView.setLayoutParams(mainView.getLayoutParams());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url);
				return true;

			}

		});
		webView.loadUrl(url);
		backgroundView.removeAllViews();
		backgroundView.addView(webView);
		currentView = webView;

	}

}
