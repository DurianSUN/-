package cn.com.lab.studyclient.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import cn.com.lab.studyclient.MainActivity;
import cn.com.lab.studyclient.R;
import cn.com.lab.studyclient.adapter.NewsPagerAdapter;
import cn.com.lab.studyclient.adapter.NewsTitleListAdapter;
import cn.com.lab.studyclient.data.NewsTitleListDataEntry;
import cn.com.lab.studyclient.runnable.DataTransferTask.DataTransferHandler;
import cn.com.lab.studyclient.utils.Utils;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabWidget;
import android.widget.TextView;

public class NewsFragment extends BackableFragment implements OnClickListener,
		OnTouchListener, OnScrollListener, OnPageChangeListener,
		OnItemClickListener {

	public static final String SCHOOL_NEWS_LIST_KEY = "schoolNewsList";
	public static final String SCHOOL_NOTICE_LIST_KEY = "schoolNoticeList";
	public static final String ACADEMIC_LECTURE_LIST_KEY = "academicLectureList";
	public static final String PERSONAL_NOTICE_LIST_KEY = "personalNoticeList";

	public static final int SCROLL_STATE_NONE = 0;
	public static final int SCROLL_STATE_PREPARE_REFRESH = 1;
	public static final int SCROLL_STATE_REFRESHING = 2;

	public static final int CURRENT_VIEW_MAIN = 0;
	public static final int CURRENT_VIEW_SCHOOL_NEWS = 1;
	public static final int CURRENT_VIEW_SCHOOL_NOTICE = 2;
	public static final int CURRENT_VIEW_ACADEMIC_LECTURE = 3;
	public static final int CURRENT_VIEW_PERSONAL_NOTICE = 4;

	private int listViewloadingGifDrawableId = R.drawable.loading;
	private int listViewDividerDrawableId = R.drawable.newsfragment_list_divider;
	private int listViewTextColorId = R.color.lightblack;

	private String listViewHeaderViewText = "刷新";
	private String listViewFooterViewText = "正在加载";

	private ArrayList<NewsTitleListDataEntry> schoolNewsList = null;
	private ArrayList<NewsTitleListDataEntry> schoolNoticeList = null;
	private ArrayList<NewsTitleListDataEntry> academicLectureList = null;
	private ArrayList<NewsTitleListDataEntry> personalNoticeList = null;

	private float refreshLastScrollY = 0;
	private int refreshHeaderViewHeight = 0;
	private int refreshScrollState = 0;
	private Handler handler = null;
	private Handler dataTransferHandler = null;

	public View currentView = null;
	public FrameLayout backgroundView = null;
	public WebView webView = null;
	public LinearLayout mainView = null;

	public ViewPager viewPager = null;
	public TabWidget tabWidget = null;
	public TextView newsPagerTitleTv = null;

	public View currentContentView = null;
	public FrameLayout newsContnetView = null;
	public LinearLayout newsMainContentView = null;

	public LinearLayout schoolNewsBtn = null;
	public TextView schoolNewsContentTv = null;
	public TextView schoolNewsTimeTv = null;

	public LinearLayout schoolNoticeBtn = null;
	public TextView schoolNoticeContentTv = null;
	public TextView schoolNoticeTimeTv = null;

	public LinearLayout academicLectureBtn = null;
	public TextView academicLectureContentTv = null;
	public TextView academicLectureTimeTv = null;

	public LinearLayout personalNoticeBtn = null;
	public TextView personalNoticeContentTv = null;
	public TextView personalNoticeTimeTv = null;

	public ListView newsChildContentView = null;
	public int currentChildViewFlag = 0;

	public class NewsHandler extends Handler {

		public NewsHandler() {
			super();
		}

		public NewsHandler(Callback arg0) {
			super(arg0);
		}

		public NewsHandler(Looper arg0) {
			super(arg0);
		}

		public NewsHandler(Looper arg0, Callback arg1) {
			super(arg0, arg1);
		}

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);

		}

	}

	public class DataInitTask implements Runnable {

		@Override
		public void run() {

			if (MainActivity.dataTransferTask.getHandler() == null)
				handler.postDelayed(this, 200);
			else {

				dataTransferHandler = MainActivity.dataTransferTask
						.getHandler();
				Message message1 = dataTransferHandler.obtainMessage(
						DataTransferHandler.WHAT_SEND,
						DataTransferHandler.ARG1_SEND_NEWS_FRAGMENT,
						DataTransferHandler.ARG2_SEND_NEWS_FRAGMENT_LOAD_MORE,
						NewsFragment.this);
				Bundle bundle1 = new Bundle();
				bundle1.putString("key", SCHOOL_NEWS_LIST_KEY);
				bundle1.putParcelableArrayList(SCHOOL_NEWS_LIST_KEY,
						schoolNewsList);
				message1.setData(bundle1);
				message1.sendToTarget();
				Message message2 = dataTransferHandler.obtainMessage(
						DataTransferHandler.WHAT_SEND,
						DataTransferHandler.ARG1_SEND_NEWS_FRAGMENT,
						DataTransferHandler.ARG2_SEND_NEWS_FRAGMENT_LOAD_MORE,
						NewsFragment.this);
				Bundle bundle2 = new Bundle();
				bundle2.putString("key", SCHOOL_NOTICE_LIST_KEY);
				bundle2.putParcelableArrayList(SCHOOL_NOTICE_LIST_KEY,
						schoolNoticeList);
				message2.setData(bundle2);
				message2.sendToTarget();
				Message message3 = dataTransferHandler.obtainMessage(
						DataTransferHandler.WHAT_SEND,
						DataTransferHandler.ARG1_SEND_NEWS_FRAGMENT,
						DataTransferHandler.ARG2_SEND_NEWS_FRAGMENT_LOAD_MORE,
						NewsFragment.this);
				Bundle bundle3 = new Bundle();
				bundle3.putString("key", ACADEMIC_LECTURE_LIST_KEY);
				bundle3.putParcelableArrayList(ACADEMIC_LECTURE_LIST_KEY,
						academicLectureList);
				message3.setData(bundle3);
				message3.sendToTarget();
				Message message4 = dataTransferHandler.obtainMessage(
						DataTransferHandler.WHAT_SEND,
						DataTransferHandler.ARG1_SEND_NEWS_FRAGMENT,
						DataTransferHandler.ARG2_SEND_NEWS_FRAGMENT_LOAD_MORE,
						NewsFragment.this);
				Bundle bundle4 = new Bundle();
				bundle4.putString("key", PERSONAL_NOTICE_LIST_KEY);
				bundle4.putParcelableArrayList(PERSONAL_NOTICE_LIST_KEY,
						personalNoticeList);
				message4.setData(bundle4);
				message4.sendToTarget();

			}

		}

	}

	public class InvalidateTask implements Runnable {

		@Override
		public void run() {

			if (currentContentView == newsMainContentView) {

				schoolNewsContentTv.setText(schoolNewsList.isEmpty() ? null
						: schoolNewsList.get(0).getTitle());
				schoolNewsTimeTv.setText(schoolNewsList.isEmpty() ? null
						: schoolNewsList.get(0).getTime());

				schoolNoticeContentTv.setText(schoolNoticeList.isEmpty() ? null
						: schoolNoticeList.get(0).getTitle());
				schoolNoticeTimeTv.setText(schoolNoticeList.isEmpty() ? null
						: schoolNoticeList.get(0).getTime());

				academicLectureContentTv
						.setText(academicLectureList.isEmpty() ? null
								: academicLectureList.get(0).getTitle());
				academicLectureTimeTv
						.setText(academicLectureList.isEmpty() ? null
								: academicLectureList.get(0).getTime());

				personalNoticeContentTv
						.setText(personalNoticeList.isEmpty() ? null
								: personalNoticeList.get(0).getTitle());
				personalNoticeTimeTv
						.setText(personalNoticeList.isEmpty() ? null
								: personalNoticeList.get(0).getTime());

			} else {

				synchronized (NewsFragment.this) {

					NewsTitleListAdapter adapter = (NewsTitleListAdapter) ((HeaderViewListAdapter) newsChildContentView
							.getAdapter()).getWrappedAdapter();

					switch (currentChildViewFlag) {

					case CURRENT_VIEW_SCHOOL_NEWS: {

						adapter.setList(schoolNewsList);
						adapter.notifyDataSetChanged();

					}
						break;

					case CURRENT_VIEW_SCHOOL_NOTICE: {

						adapter.setList(schoolNoticeList);
						adapter.notifyDataSetChanged();

					}
						break;

					case CURRENT_VIEW_ACADEMIC_LECTURE: {

						adapter.setList(academicLectureList);
						adapter.notifyDataSetChanged();

					}
						break;

					case CURRENT_VIEW_PERSONAL_NOTICE: {

						adapter.setList(personalNoticeList);
						adapter.notifyDataSetChanged();

					}
						break;

					default:
						break;

					}

				}

				FrameLayout headerView = (FrameLayout) newsChildContentView
						.getAdapter().getView(0, null, newsChildContentView);
				headerView.getChildAt(0).setVisibility(View.GONE);
				headerView.setPadding(headerView.getPaddingLeft(), 0,
						headerView.getPaddingRight(),
						headerView.getPaddingBottom());
				FrameLayout footerView = (FrameLayout) newsChildContentView
						.getAdapter().getView(
								newsChildContentView.getCount() - 1, null,
								newsChildContentView);
				footerView.getChildAt(0).setVisibility(View.GONE);
				footerView.setPadding(footerView.getPaddingLeft(),
						footerView.getPaddingTop(),
						footerView.getPaddingRight(), 0);

				if (newsChildContentView.getFirstVisiblePosition() == 0)
					newsChildContentView.setSelection(1);

			}

			if (refreshScrollState == SCROLL_STATE_REFRESHING)
				refreshScrollState = SCROLL_STATE_NONE;

		}

	}

	public Handler getHandler() {
		return handler;
	}

	public void setCurrentViewItem(int item) {

		if (currentView == webView) {

			backgroundView.removeAllViews();
			backgroundView.addView(mainView);
			currentView = mainView;

		}

		switch (item) {

		case CURRENT_VIEW_MAIN: {

			newsContnetView.removeAllViews();
			newsContnetView.addView(newsMainContentView);
			currentContentView = newsMainContentView;
			handler.post(new InvalidateTask());

		}
			break;

		case CURRENT_VIEW_SCHOOL_NEWS: {

			if (currentChildViewFlag != CURRENT_VIEW_SCHOOL_NEWS) {

				newsChildContentViewInit();
				newsChildContentView.setAdapter(new NewsTitleListAdapter(
						schoolNewsList, getActivity()));
				((FrameLayout) newsChildContentView.getAdapter().getView(0,
						null, newsChildContentView)).getChildAt(0)
						.setVisibility(View.GONE);
				((FrameLayout) newsChildContentView.getAdapter().getView(
						newsChildContentView.getCount() - 1, null,
						newsChildContentView)).getChildAt(0).setVisibility(
						View.GONE);
				newsChildContentView.setSelection(1);
				currentChildViewFlag = CURRENT_VIEW_SCHOOL_NEWS;

			}

			newsContnetView.removeAllViews();
			newsContnetView.addView(newsChildContentView);
			currentContentView = newsChildContentView;

		}
			break;

		case CURRENT_VIEW_SCHOOL_NOTICE: {

			if (currentChildViewFlag != CURRENT_VIEW_SCHOOL_NOTICE) {

				newsChildContentViewInit();
				newsChildContentView.setAdapter(new NewsTitleListAdapter(
						schoolNoticeList, getActivity()));
				((FrameLayout) newsChildContentView.getAdapter().getView(0,
						null, newsChildContentView)).getChildAt(0)
						.setVisibility(View.GONE);
				((FrameLayout) newsChildContentView.getAdapter().getView(
						newsChildContentView.getCount() - 1, null,
						newsChildContentView)).getChildAt(0).setVisibility(
						View.GONE);
				newsChildContentView.setSelection(1);
				currentChildViewFlag = CURRENT_VIEW_SCHOOL_NOTICE;

			}

			newsContnetView.removeAllViews();
			newsContnetView.addView(newsChildContentView);
			currentContentView = newsChildContentView;

		}
			break;

		case CURRENT_VIEW_ACADEMIC_LECTURE: {

			if (currentChildViewFlag != CURRENT_VIEW_ACADEMIC_LECTURE) {

				newsChildContentViewInit();
				newsChildContentView.setAdapter(new NewsTitleListAdapter(
						academicLectureList, getActivity()));
				((FrameLayout) newsChildContentView.getAdapter().getView(0,
						null, newsChildContentView)).getChildAt(0)
						.setVisibility(View.GONE);
				((FrameLayout) newsChildContentView.getAdapter().getView(
						newsChildContentView.getCount() - 1, null,
						newsChildContentView)).getChildAt(0).setVisibility(
						View.GONE);
				newsChildContentView.setSelection(1);
				currentChildViewFlag = CURRENT_VIEW_ACADEMIC_LECTURE;

			}

			newsContnetView.removeAllViews();
			newsContnetView.addView(newsChildContentView);
			currentContentView = newsChildContentView;

		}
			break;

		case CURRENT_VIEW_PERSONAL_NOTICE: {

			if (currentChildViewFlag != CURRENT_VIEW_PERSONAL_NOTICE) {

				newsChildContentViewInit();
				newsChildContentView.setAdapter(new NewsTitleListAdapter(
						personalNoticeList, getActivity()));
				((FrameLayout) newsChildContentView.getAdapter().getView(0,
						null, newsChildContentView)).getChildAt(0)
						.setVisibility(View.GONE);
				((FrameLayout) newsChildContentView.getAdapter().getView(
						newsChildContentView.getCount() - 1, null,
						newsChildContentView)).getChildAt(0).setVisibility(
						View.GONE);
				newsChildContentView.setSelection(1);
				currentChildViewFlag = CURRENT_VIEW_PERSONAL_NOTICE;

			}

			newsContnetView.removeAllViews();
			newsContnetView.addView(newsChildContentView);
			currentContentView = newsChildContentView;

		}
			break;

		default:
			break;

		}

	}

	private void newsChildContentViewInit() {

		android.widget.AbsListView.LayoutParams layoutParams = new android.widget.AbsListView.LayoutParams(
				new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT, Gravity.CENTER));

		TextView headerView = new TextView(getActivity());
		headerView.setLayoutParams(layoutParams);
		headerView.setGravity(Gravity.CENTER);
		headerView.setPadding((int) Utils.convertDpToPixel(10),
				(int) Utils.convertDpToPixel(10),
				(int) Utils.convertDpToPixel(10),
				(int) Utils.convertDpToPixel(10));
		headerView.setText(listViewHeaderViewText);
		headerView.setTextColor(listViewTextColorId);
		headerView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		FrameLayout headerLayout = new FrameLayout(getActivity());
		headerLayout.addView(headerView, 0);
		headerLayout.measure(
				View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		refreshHeaderViewHeight = headerLayout.getMeasuredHeight();

		TextView footerView = new TextView(getActivity());
		footerView.setLayoutParams(layoutParams);
		footerView.setGravity(Gravity.CENTER);
		footerView.setPadding((int) Utils.convertDpToPixel(10),
				(int) Utils.convertDpToPixel(10),
				(int) Utils.convertDpToPixel(10),
				(int) Utils.convertDpToPixel(10));
		footerView.setText(listViewFooterViewText);
		footerView.setTextColor(listViewTextColorId);
		footerView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		FrameLayout footerLayout = new FrameLayout(getActivity());
		footerLayout.addView(footerView, 0);

		newsChildContentView = new ListView(getActivity());
		newsChildContentView.setLayoutParams(newsMainContentView
				.getLayoutParams());
		newsChildContentView.setDivider(getActivity().getResources()
				.getDrawable(listViewDividerDrawableId));
		newsChildContentView.setDividerHeight((int) Utils.convertDpToPixel(1));
		newsChildContentView.addHeaderView(headerLayout);
		newsChildContentView.addFooterView(footerLayout);
		newsChildContentView.setOnTouchListener(this);
		newsChildContentView.setOnScrollListener(this);
		newsChildContentView.setOnItemClickListener(this);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_news, null);

		schoolNewsList = new ArrayList<NewsTitleListDataEntry>();
		schoolNoticeList = new ArrayList<NewsTitleListDataEntry>();
		academicLectureList = new ArrayList<NewsTitleListDataEntry>();
		personalNoticeList = new ArrayList<NewsTitleListDataEntry>();

		handler = new NewsHandler();
		handler.post(new DataInitTask());

		backgroundView = (FrameLayout) view
				.findViewById(R.id.frameLayout_newsBackground);
		mainView = (LinearLayout) view
				.findViewById(R.id.linearLayout_news_main);
		currentView = mainView;

		ImageView imageView1 = new ImageView(getActivity());
		ImageView imageView2 = new ImageView(getActivity());
		ImageView imageView3 = new ImageView(getActivity());
		ImageView imageView4 = new ImageView(getActivity());
		imageView1.setScaleType(ScaleType.FIT_XY);
		imageView2.setScaleType(ScaleType.FIT_XY);
		imageView3.setScaleType(ScaleType.FIT_XY);
		imageView4.setScaleType(ScaleType.FIT_XY);
		InputStream in = null;
		try {

			AssetManager am = getResources().getAssets();
			in = am.open("0910071908ae0f2038ac3f5607.jpg");
			imageView1.setImageBitmap(BitmapFactory.decodeStream(in));
			in = am.open("2-20050919_111107205_FNPbYDclcteG.jpg");
			imageView2.setImageBitmap(BitmapFactory.decodeStream(in));
			in = am.open("5ab5c9ea15ce36d35b557dc43af33a87e850b1f5.jpg");
			imageView3.setImageBitmap(BitmapFactory.decodeStream(in));
			in = am.open("6aa49c51357dac3c43a75b77.jpg");
			imageView4.setImageBitmap(BitmapFactory.decodeStream(in));
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		viewPager = (ViewPager) view.findViewById(R.id.viewPager_news);
		viewPager.setAdapter(new NewsPagerAdapter(new ImageView[] { imageView1,
				imageView2, imageView3, imageView4 }));
		viewPager.setOnPageChangeListener(this);

		tabWidget = (TabWidget) view.findViewById(R.id.tabWidget_news);
		tabWidget.setStripEnabled(false);

		newsContnetView = (FrameLayout) view
				.findViewById(R.id.frameLayout_newsContentView);
		newsMainContentView = (LinearLayout) view
				.findViewById(R.id.linearLayout_newsMainContentView);

		schoolNewsBtn = (LinearLayout) view
				.findViewById(R.id.linearLayout_schoolNews);
		schoolNewsBtn.setOnClickListener(this);
		schoolNewsContentTv = (TextView) view
				.findViewById(R.id.textView_schoolNews_content);
		schoolNewsTimeTv = (TextView) view
				.findViewById(R.id.textView_schoolNews_time);

		schoolNoticeBtn = (LinearLayout) view
				.findViewById(R.id.linearLayout_schoolNotice);
		schoolNoticeBtn.setOnClickListener(this);
		schoolNoticeContentTv = (TextView) view
				.findViewById(R.id.textView_schoolNotice_content);
		schoolNoticeTimeTv = (TextView) view
				.findViewById(R.id.textView_schoolNotice_time);

		academicLectureBtn = (LinearLayout) view
				.findViewById(R.id.linearLayout_academicLecture);
		academicLectureBtn.setOnClickListener(this);
		academicLectureContentTv = (TextView) view
				.findViewById(R.id.textView_academicLecture_content);
		academicLectureTimeTv = (TextView) view
				.findViewById(R.id.textView_academicLecture_time);

		personalNoticeBtn = (LinearLayout) view
				.findViewById(R.id.linearLayout_personalNotice);
		personalNoticeBtn.setOnClickListener(this);
		personalNoticeContentTv = (TextView) view
				.findViewById(R.id.textView_personalNotice_content);
		personalNoticeTimeTv = (TextView) view
				.findViewById(R.id.textView_personalNotice_time);

		currentContentView = newsMainContentView;

		return view;

	}

	@Override
	public boolean back() {

		if (currentView == mainView) {

			if (currentContentView == newsMainContentView)
				return false;
			else {

				((MainActivity) getActivity())
						.setCurrnetNewsArticeItem(CURRENT_VIEW_MAIN);
				setCurrentViewItem(CURRENT_VIEW_MAIN);

				return true;

			}

		} else if (webView.canGoBack()) {

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

			if (v == schoolNewsBtn) {

				((MainActivity) getActivity())
						.setCurrnetNewsArticeItem(CURRENT_VIEW_SCHOOL_NEWS);
				setCurrentViewItem(CURRENT_VIEW_SCHOOL_NEWS);

			}

			if (v == schoolNoticeBtn) {

				((MainActivity) getActivity())
						.setCurrnetNewsArticeItem(CURRENT_VIEW_SCHOOL_NOTICE);
				setCurrentViewItem(CURRENT_VIEW_SCHOOL_NOTICE);

			}

			if (v == academicLectureBtn) {

				((MainActivity) getActivity())
						.setCurrnetNewsArticeItem(CURRENT_VIEW_ACADEMIC_LECTURE);
				setCurrentViewItem(CURRENT_VIEW_ACADEMIC_LECTURE);

			}

			if (v == personalNoticeBtn) {

				((MainActivity) getActivity())
						.setCurrnetNewsArticeItem(CURRENT_VIEW_PERSONAL_NOTICE);
				setCurrentViewItem(CURRENT_VIEW_PERSONAL_NOTICE);

			}

		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN: {

		}
			break;

		case MotionEvent.ACTION_MOVE: {

			if (newsChildContentView.getFirstVisiblePosition() == 0) {

				FrameLayout headerView = (FrameLayout) newsChildContentView
						.getAdapter().getView(0, null, newsChildContentView);

				if (headerView.getTop() >= 0
						&& refreshScrollState != SCROLL_STATE_REFRESHING) {

					if (refreshScrollState == SCROLL_STATE_NONE) {

						refreshLastScrollY = event.getY();
						refreshScrollState = SCROLL_STATE_PREPARE_REFRESH;

					}

					if (headerView.getChildAt(0).getVisibility() != View.VISIBLE)
						headerView.getChildAt(0).setVisibility(View.VISIBLE);

					if (headerView.getPaddingTop() < newsChildContentView
							.getHeight() / 2) {

						for (int n = 0; n < event.getHistorySize(); n++)
							headerView
									.setPadding(
											headerView.getPaddingLeft(),
											(int) (event.getHistoricalY(n) - refreshLastScrollY),
											headerView.getPaddingRight(),
											headerView.getPaddingBottom());

					}

				}

			}

		}
			break;

		case MotionEvent.ACTION_UP: {

			FrameLayout headerView = (FrameLayout) newsChildContentView
					.getAdapter().getView(0, null, newsChildContentView);

			if (newsChildContentView.getFirstVisiblePosition() == 0) {

				if (headerView.getTop() >= 0
						&& headerView.getBottom() > refreshHeaderViewHeight
						&& refreshScrollState == SCROLL_STATE_PREPARE_REFRESH) {

					headerView.setPadding(headerView.getPaddingLeft(), 0,
							headerView.getPaddingRight(),
							headerView.getPaddingBottom());
					refreshScrollState = SCROLL_STATE_REFRESHING;

					switch (currentChildViewFlag) {

					case CURRENT_VIEW_SCHOOL_NEWS: {

						Message message = dataTransferHandler
								.obtainMessage(
										DataTransferHandler.WHAT_SEND,
										DataTransferHandler.ARG1_SEND_NEWS_FRAGMENT,
										DataTransferHandler.ARG2_SEND_NEWS_FRAGMENT_REFRESH,
										this);
						Bundle bundle = new Bundle();
						bundle.putString("key", SCHOOL_NEWS_LIST_KEY);
						bundle.putParcelableArrayList(SCHOOL_NEWS_LIST_KEY,
								schoolNewsList);
						message.setData(bundle);
						message.sendToTarget();

					}
						break;

					case CURRENT_VIEW_SCHOOL_NOTICE: {

						Message message = dataTransferHandler
								.obtainMessage(
										DataTransferHandler.WHAT_SEND,
										DataTransferHandler.ARG1_SEND_NEWS_FRAGMENT,
										DataTransferHandler.ARG2_SEND_NEWS_FRAGMENT_REFRESH,
										this);
						Bundle bundle = new Bundle();
						bundle.putString("key", SCHOOL_NOTICE_LIST_KEY);
						bundle.putParcelableArrayList(SCHOOL_NOTICE_LIST_KEY,
								schoolNoticeList);
						message.setData(bundle);
						message.sendToTarget();

					}
						break;

					case CURRENT_VIEW_ACADEMIC_LECTURE: {

						Message message = dataTransferHandler
								.obtainMessage(
										DataTransferHandler.WHAT_SEND,
										DataTransferHandler.ARG1_SEND_NEWS_FRAGMENT,
										DataTransferHandler.ARG2_SEND_NEWS_FRAGMENT_REFRESH,
										this);
						Bundle bundle = new Bundle();
						bundle.putString("key", ACADEMIC_LECTURE_LIST_KEY);
						bundle.putParcelableArrayList(
								ACADEMIC_LECTURE_LIST_KEY, academicLectureList);
						message.setData(bundle);
						message.sendToTarget();

					}
						break;

					case CURRENT_VIEW_PERSONAL_NOTICE: {

						Message message = dataTransferHandler
								.obtainMessage(
										DataTransferHandler.WHAT_SEND,
										DataTransferHandler.ARG1_SEND_NEWS_FRAGMENT,
										DataTransferHandler.ARG2_SEND_NEWS_FRAGMENT_REFRESH,
										this);
						Bundle bundle = new Bundle();
						bundle.putString("key", PERSONAL_NOTICE_LIST_KEY);
						bundle.putParcelableArrayList(PERSONAL_NOTICE_LIST_KEY,
								personalNoticeList);
						message.setData(bundle);
						message.sendToTarget();

					}
						break;

					default:
						break;

					}

				} else {

					headerView.getChildAt(0).setVisibility(View.GONE);
					headerView.setPadding(headerView.getPaddingLeft(), 0,
							headerView.getPaddingRight(),
							headerView.getPaddingBottom());
					newsChildContentView.setSelection(1);
					refreshScrollState = SCROLL_STATE_NONE;

				}

			} else if (headerView.getChildAt(0).getVisibility() == View.VISIBLE) {

				headerView.getChildAt(0).setVisibility(View.GONE);
				headerView.setPadding(headerView.getPaddingLeft(), 0,
						headerView.getPaddingRight(),
						headerView.getPaddingBottom());
				refreshScrollState = SCROLL_STATE_NONE;

			}

		}
			break;

		default:
			break;

		}

		return false;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {

			if (view.getFirstVisiblePosition() <= 1
					&& view.getLastVisiblePosition() >= view.getCount() - 2)
				((FrameLayout) view.getAdapter().getView(0, null, view))
						.getChildAt(0).setVisibility(View.VISIBLE);
			else {

				((FrameLayout) view.getAdapter().getView(0, null, view))
						.getChildAt(0).setVisibility(View.VISIBLE);
				((FrameLayout) view.getAdapter().getView(view.getCount() - 1,
						null, view)).getChildAt(0).setVisibility(View.VISIBLE);

			}

		} else if (refreshScrollState != SCROLL_STATE_REFRESHING) {

			((FrameLayout) view.getAdapter().getView(0, null, view))
					.getChildAt(0).setVisibility(View.GONE);

			if (view.getFirstVisiblePosition() == 0)
				view.setSelection(1);

			final View footerView = view.getAdapter().getView(
					view.getCount() - 1, null, view);

			if (footerView instanceof FrameLayout)
				((FrameLayout) footerView).getChildAt(0).setVisibility(
						View.GONE);
			else
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {

						if (footerView instanceof FrameLayout)
							((FrameLayout) footerView).getChildAt(0)
									.setVisibility(View.GONE);
						else
							handler.postDelayed(this, 200);

					}

				}, 200);

			if (refreshScrollState != SCROLL_STATE_NONE)
				refreshScrollState = SCROLL_STATE_NONE;

		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (firstVisibleItem > totalItemCount - visibleItemCount * 2) {

			switch (currentChildViewFlag) {

			case CURRENT_VIEW_SCHOOL_NEWS: {

				Message message = dataTransferHandler.obtainMessage(
						DataTransferHandler.WHAT_SEND,
						DataTransferHandler.ARG1_SEND_NEWS_FRAGMENT,
						DataTransferHandler.ARG2_SEND_NEWS_FRAGMENT_LOAD_MORE,
						this);
				Bundle bundle = new Bundle();
				bundle.putString("key", SCHOOL_NEWS_LIST_KEY);
				bundle.putParcelableArrayList(SCHOOL_NEWS_LIST_KEY,
						schoolNewsList);
				message.setData(bundle);
				message.sendToTarget();

			}
				break;

			case CURRENT_VIEW_SCHOOL_NOTICE: {

				Message message = dataTransferHandler.obtainMessage(
						DataTransferHandler.WHAT_SEND,
						DataTransferHandler.ARG1_SEND_NEWS_FRAGMENT,
						DataTransferHandler.ARG2_SEND_NEWS_FRAGMENT_LOAD_MORE,
						this);
				Bundle bundle = new Bundle();
				bundle.putString("key", SCHOOL_NOTICE_LIST_KEY);
				bundle.putParcelableArrayList(SCHOOL_NOTICE_LIST_KEY,
						schoolNoticeList);
				message.setData(bundle);
				message.sendToTarget();

			}
				break;

			case CURRENT_VIEW_ACADEMIC_LECTURE: {

				Message message = dataTransferHandler.obtainMessage(
						DataTransferHandler.WHAT_SEND,
						DataTransferHandler.ARG1_SEND_NEWS_FRAGMENT,
						DataTransferHandler.ARG2_SEND_NEWS_FRAGMENT_LOAD_MORE,
						this);
				Bundle bundle = new Bundle();
				bundle.putString("key", ACADEMIC_LECTURE_LIST_KEY);
				bundle.putParcelableArrayList(ACADEMIC_LECTURE_LIST_KEY,
						academicLectureList);
				message.setData(bundle);
				message.sendToTarget();

			}
				break;

			case CURRENT_VIEW_PERSONAL_NOTICE: {

				Message message = dataTransferHandler.obtainMessage(
						DataTransferHandler.WHAT_SEND,
						DataTransferHandler.ARG1_SEND_NEWS_FRAGMENT,
						DataTransferHandler.ARG2_SEND_NEWS_FRAGMENT_LOAD_MORE,
						this);
				Bundle bundle = new Bundle();
				bundle.putString("key", PERSONAL_NOTICE_LIST_KEY);
				bundle.putParcelableArrayList(PERSONAL_NOTICE_LIST_KEY,
						personalNoticeList);
				message.setData(bundle);
				message.sendToTarget();

			}
				break;

			default:
				break;

			}

		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (position > 0 && position < parent.getCount() - 1) {

			String url = ((NewsTitleListDataEntry) (parent.getAdapter())
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

}
