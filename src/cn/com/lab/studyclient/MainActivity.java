package cn.com.lab.studyclient;

import java.util.ArrayList;

import cn.com.lab.studyclient.adapter.MainFragmentPagerAdapter;
import cn.com.lab.studyclient.adapter.MajorListAdapter;
import cn.com.lab.studyclient.adapter.NewsArticleListAdapter;
import cn.com.lab.studyclient.adapter.SubjectListAdapter;
import cn.com.lab.studyclient.data.MajorDataEntry;
import cn.com.lab.studyclient.data.SubjectDataEntry;
import cn.com.lab.studyclient.fragment.BackableFragment;
import cn.com.lab.studyclient.fragment.ExamFragment;
import cn.com.lab.studyclient.fragment.ExercisesFragment;
import cn.com.lab.studyclient.fragment.NewsFragment;
import cn.com.lab.studyclient.fragment.StudyFragment;
import cn.com.lab.studyclient.fragment.UserInfoFragment;
import cn.com.lab.studyclient.login.LoginActivity;
import cn.com.lab.studyclient.runnable.DataTransferTask;
import cn.com.lab.studyclient.runnable.DataTransferTask.DataTransferHandler;
import cn.com.lab.studyclient.utils.Utils;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TabWidget;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener,
		OnTouchListener, OnGestureListener, OnPageChangeListener,
		OnItemClickListener {

	public static DataTransferTask dataTransferTask = null;
	public static boolean actionLock = false;

	public static final String STUDY_MAJORS_KEY = "studyMajors";
	public static final String EXERCISES_MAJORS_KEY = "exercisesMajors";
	public static final String EXAM_MAJORS_KEY = "examMajors";

	public static final int SCROLL_STATE_NONE = 0;
	public static final int SCROLL_STATE_ONSCROLL = 1;
	public static final int SCROLL_STATE_AUTOSCROLL = 2;

	public static final int LIST_FIRST_FLAG_NEWS_ARTICLE = 0;
	public static final int LIST_FIRST_FLAG_STUDY_SUBJECT = 1;
	public static final int LIST_FIRST_FLAG_EXERCISES_SUBJECT = 2;
	public static final int LIST_FIRST_FLAG_EXAM_SUBJECT = 3;
	public static final int LIST_SECOND_FLAG_MAJOR = 0;
	public static final int LIST_SECOND_FLAG_SUBJECT = 1;

	private int[] tabDrawablesId = { R.drawable.tab_button_news_drawable,
			R.drawable.tab_button_study_drawable,
			R.drawable.tab_button_exercises_drawable,
			R.drawable.tab_button_exam_drawable };
	private int[] tabOnSelectedDrawablesId = {
			R.drawable.tab_button_news_onselected,
			R.drawable.tab_button_study_onselected,
			R.drawable.tab_button_exercises_onselected,
			R.drawable.tab_button_exam_onselected };
	private int[] tabNoSelectedDrawablesId = {
			R.drawable.tab_button_news_noselected,
			R.drawable.tab_button_study_noselected,
			R.drawable.tab_button_exercises_noselected,
			R.drawable.tab_button_exam_noselected };

	private int actionBarCenterDropDownDrawableId = R.drawable.actionbar_center_dropdown;
	private int actionBarCenterDropUpDrawableId = R.drawable.actionbar_center_dropup;

	private int backgroundColorId = R.color.darkblue;
	private int foregroundColorId = R.color.blue;

	private int tabTextColorId = R.color.tab_buttons_text;
	private int tabTextOnSelectedColorId = R.color.lightblue;
	private int tabTextNoSelectedColorId = R.color.gray;

	private String[] newsArticles = { "首页", "学校新闻", "校务公告", "学术讲座", "个人通知" };
	private ArrayList<ArrayList<MajorDataEntry>> majors = null;
	private ArrayList<ArrayList<ArrayList<SubjectDataEntry>>> subjects = null;
	private ArrayList<MajorDataEntry> studyMajors = null;
	private ArrayList<ArrayList<SubjectDataEntry>> studySubjects = null;
	private ArrayList<MajorDataEntry> exercisesMajors = null;
	private ArrayList<ArrayList<SubjectDataEntry>> exercisesSubjects = null;
	private ArrayList<MajorDataEntry> examMajors = null;
	private ArrayList<ArrayList<SubjectDataEntry>> examSubjects = null;

	private String[] tabTexts = { "时讯", "学习", "练习", "考试" };

	private int screenWidth = 0;
	private int screenHeight = 0;
	private int drawableWidth = 0;
	private int drawableHeigth = 0;

	private int slideScrollRefildays = 10;
	private int slideScrollTime = 200;
	private int slideScrollState = 0;
	private int pagerScrollState = 0;
	private Handler handler = null;
	private Handler dataTransferHandler = null;
	private GestureDetector gestureDetector = null;

	public View currentView = null;
	public FrameLayout backgroundView = null;
	public View glassView = null;
	public View slideGlassView = null;

	public LinearLayout mainView = null;
	public LinearLayout actionBar = null;
	public FrameLayout actionBarLeft = null;
	public FrameLayout actionBarCenter = null;
	public FrameLayout actionBarRight = null;
	public Button userBtn = null;
	public Button closeBtn = null;
	public Button backOnLeftBtn = null;
	public Button backOnRightBtn = null;

	public Button[] actionBarCenterButtons = null;
	public Button newsArticleBtn = null;
	public Button studySubjectBtn = null;
	public Button exercisesSubjectBtn = null;
	public Button examSubjectBtn = null;
	public PopupWindow[] actionBarCenterPopups = null;
	public PopupWindow newsArticlePopup = null;
	public PopupWindow studySubjectPopup = null;
	public PopupWindow exercisesSubjectPopup = null;
	public PopupWindow examSubjectPopup = null;

	public FrameLayout contentView = null;
	public LinearLayout mainContentView = null;
	public ViewPager viewPager = null;
	public BackableFragment[] viewPagerFragments = null;
	public TabWidget tabWidget = null;
	public Button[] tabButtons = null;

	public LinearLayout userView = null;
	public Button userInfoBtn = null;
	public Button settingBtn = null;
	public Button checkUpdateBtn = null;
	public Button logoutBtn = null;
	public Button exitBtn = null;

	public BackableFragment currentContentFragment = null;
	public UserInfoFragment userInfoFragment = null;

	public class MainHandler extends Handler {

		public MainHandler() {
			super();
		}

		public MainHandler(Callback arg0) {
			super(arg0);
		}

		public MainHandler(Looper arg0) {
			super(arg0);
		}

		public MainHandler(Looper arg0, Callback arg1) {
			super(arg0, arg1);
		}

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);

		}

	}

	public class MajorsRefreshTask implements Runnable {

		@Override
		public void run() {

			if (dataTransferTask.getHandler() == null)
				handler.postDelayed(this, 200);
			else {

				dataTransferHandler = dataTransferTask.getHandler();
				Message message = dataTransferHandler
						.obtainMessage(
								DataTransferHandler.WHAT_SEND,
								DataTransferHandler.ARG1_SEND_MAIN_ACTIVITY,
								DataTransferHandler.ARG2_SEND_MAIN_ACTIVITY_LOAD_MAJORS,
								MainActivity.this);
				Bundle bundle = new Bundle();
				bundle.putString("key", STUDY_MAJORS_KEY);
				bundle.putParcelableArrayList(STUDY_MAJORS_KEY, studyMajors);
				message.setData(bundle);
				message.sendToTarget();

			}

		}

	}

	public class SubjectsRefreshTask implements Runnable {

		@Override
		public void run() {

			Message message = dataTransferHandler.obtainMessage(
					DataTransferHandler.WHAT_SEND,
					DataTransferHandler.ARG1_SEND_MAIN_ACTIVITY,
					DataTransferHandler.ARG2_SEND_MAIN_ACTIVITY_LOAD_SUBJECTS,
					MainActivity.this);
			Bundle bundle = new Bundle();
			bundle.putString("key", STUDY_MAJORS_KEY);
			bundle.putParcelableArrayList(STUDY_MAJORS_KEY, studyMajors);
			studySubjects = new ArrayList<ArrayList<SubjectDataEntry>>();
			subjects.add(studySubjects);

			for (int n = 0; n < studyMajors.size(); n++) {

				studySubjects.add(new ArrayList<SubjectDataEntry>());
				bundle.putParcelableArrayList(
						String.valueOf(studyMajors.get(n).getId()),
						studySubjects.get(n));

			}

			message.setData(bundle);
			message.sendToTarget();

		}

	}

	public class ActionBarCenterInvalidateTask implements Runnable {

		@Override
		public void run() {

			if (studyMajors.size() != 0)
				studySubjectBtn.setText(studyMajors.get(0).getName());

			if (exercisesMajors.size() != 0)
				exercisesSubjectBtn.setText(exercisesMajors.get(0).getName());

			if (examMajors.size() != 0)
				examSubjectBtn.setText(examMajors.get(0).getName());

			if (actionBarCenterPopups == null || studyMajors.size() != 0
					&& exercisesMajors.size() != 0 && examMajors.size() != 0)
				handler.postDelayed(new ActionBarCenterInvalidateTask(), 200);
			else {

				if (studySubjectPopup != null && studyMajors.size() != 0)
					((ListView) studySubjectPopup.getContentView()
							.findViewById(R.id.listView_majorList))
							.setAdapter(new MajorListAdapter(
									LIST_FIRST_FLAG_STUDY_SUBJECT, studyMajors,
									MainActivity.this));

				if (exercisesSubjectPopup != null
						&& exercisesMajors.size() != 0)
					((ListView) exercisesSubjectPopup.getContentView()
							.findViewById(R.id.listView_majorList))
							.setAdapter(new MajorListAdapter(
									LIST_FIRST_FLAG_EXERCISES_SUBJECT,
									exercisesMajors, MainActivity.this));

				if (examSubjectPopup != null && examMajors.size() != 0)
					((ListView) examSubjectPopup.getContentView().findViewById(
							R.id.listView_majorList))
							.setAdapter(new MajorListAdapter(
									LIST_FIRST_FLAG_EXAM_SUBJECT, examMajors,
									MainActivity.this));

			}

		}

	}

	private class UserViewScrollTask implements Runnable {

		private boolean scrollIn = false;
		private long start = 0;
		private long duration = 0;
		private long begin = 0;
		private long end = 0;

		public UserViewScrollTask() {

			slideScrollState = MainActivity.SCROLL_STATE_AUTOSCROLL;
			scrollIn = userView.getAlpha() > 0.5f ? true : false;
			start = System.currentTimeMillis();
			duration = (long) (scrollIn ? slideScrollTime
					* (1 - userView.getAlpha()) : slideScrollTime
					* userView.getAlpha());

		}

		public UserViewScrollTask(boolean scrollIn) {

			slideScrollState = MainActivity.SCROLL_STATE_AUTOSCROLL;
			this.scrollIn = scrollIn;
			start = System.currentTimeMillis();
			duration = (long) (scrollIn ? slideScrollTime
					* (1 - userView.getAlpha()) : slideScrollTime
					* userView.getAlpha());

		}

		@Override
		public void run() {

			begin = System.currentTimeMillis();

			float scrollRatio = (float) (slideScrollTime - duration + begin - start)
					/ (float) slideScrollTime;

			int color1 = getResources().getColor(
					scrollIn ? foregroundColorId : backgroundColorId);
			int alpha1 = Color.alpha(color1), red1 = Color.red(color1), green1 = Color
					.green(color1), blue1 = Color.blue(color1);
			int color2 = getResources().getColor(
					scrollIn ? backgroundColorId : foregroundColorId);
			int alpha2 = Color.alpha(color2), red2 = Color.red(color2), green2 = Color
					.green(color2), blue2 = Color.blue(color2);

			backgroundView.setBackgroundColor(Color.argb(
					(int) (alpha1 + (alpha2 - alpha1) * scrollRatio),
					(int) (red1 + (red2 - red1) * scrollRatio),
					(int) (green1 + (green2 - green1) * scrollRatio),
					(int) (blue1 + (blue2 - blue1) * scrollRatio)));

			userView.setPivotX(userView.getX() - userView.getWidth() * 4 / 3);
			userView.setScaleX(scrollIn ? 0.75f + scrollRatio / 4
					: 1 - scrollRatio / 4);
			userView.setPivotY(userView.getY() + userView.getHeight() / 2);
			userView.setScaleY(scrollIn ? 0.75f + scrollRatio / 4
					: 1 - scrollRatio / 4);
			userView.setAlpha(scrollIn ? scrollRatio : 1 - scrollRatio);

			mainView.setPivotX(mainView.getX() + mainView.getWidth() * 3);
			mainView.setScaleX(scrollIn ? 1 - scrollRatio / 4
					: 0.75f + scrollRatio / 4);
			mainView.setPivotY(mainView.getY() + mainView.getHeight() / 2);
			mainView.setScaleY(scrollIn ? 1 - scrollRatio / 4
					: 0.75f + scrollRatio / 4);
			mainView.setAlpha(scrollIn ? 1 - scrollRatio / 4
					: 0.75f + scrollRatio / 4);

			end = System.currentTimeMillis();

			if (end - start < duration)
				handler.postDelayed(this, slideScrollRefildays - end + begin);
			else {

				backgroundView.setBackgroundColor(getResources().getColor(
						scrollIn ? backgroundColorId : foregroundColorId));

				userView.setPivotX(userView.getX());
				userView.setScaleX(scrollIn ? 1f : 0f);
				userView.setPivotY(userView.getY() + userView.getHeight() / 2);
				userView.setScaleY(scrollIn ? 1f : 0f);
				userView.setAlpha(scrollIn ? 1f : 0f);
				userView.setVisibility(scrollIn ? View.VISIBLE : View.GONE);

				mainView.setPivotX(mainView.getX() + mainView.getWidth() * 3);
				mainView.setScaleX(scrollIn ? 0.75f : 1f);
				mainView.setPivotY(mainView.getY() + mainView.getHeight() / 2);
				mainView.setScaleY(scrollIn ? 0.75f : 1f);
				mainView.setAlpha(scrollIn ? 0.75f : 1f);

				glassView.setVisibility(scrollIn ? View.VISIBLE : View.GONE);
				glassView.setLayoutParams(scrollIn ? new LayoutParams(mainView
						.getWidth() / 4, mainView.getHeight() * 3 / 4,
						Gravity.CENTER_VERTICAL | Gravity.END) : backgroundView
						.getLayoutParams());
				glassView.setOnClickListener(scrollIn ? MainActivity.this
						: null);
				currentView = scrollIn ? userView : mainView;
				handler.removeCallbacks(this);
				slideScrollState = MainActivity.SCROLL_STATE_NONE;
				actionLock = false;

			}

		}

	}

	public Handler getHandler() {
		return handler;
	}

	public void setCurrnetNewsArticeItem(int item) {

		ListView listView = (ListView) newsArticlePopup.getContentView()
				.findViewById(R.id.listView_newsArticleList);
		NewsArticleListAdapter adapter = ((NewsArticleListAdapter) listView
				.getAdapter());
		adapter.setSelection(item);
		adapter.notifyDataSetChanged();
		newsArticleBtn.setText(newsArticles[item]);

	}

	public int getCurrentNewsArticeItem() {
		return ((NewsArticleListAdapter) ((ListView) newsArticlePopup
				.getContentView().findViewById(R.id.listView_newsArticleList))
				.getAdapter()).getSelection();
	}

	public void setCurrentSubjectId(long id) {

		int firstFlag = (int) id / 10000;
		int secondFlag = (int) (id % 10000 / 1000);

		switch (secondFlag) {

		case LIST_SECOND_FLAG_MAJOR: {

			int majorIndex = (int) (id % 1000);
			ListView listView = (ListView) actionBarCenterPopups[firstFlag]
					.getContentView().findViewById(R.id.listView_majorList);
			MajorListAdapter adapter = ((MajorListAdapter) listView
					.getAdapter());
			adapter.setSelection(majorIndex);
			adapter.notifyDataSetChanged();
			ListView subjectList = (ListView) actionBarCenterPopups[firstFlag]
					.getContentView().findViewById(R.id.listView_subjectList);

			if (subjects.get(firstFlag - 1).get(majorIndex).size() == 0) {

				subjectList.setAdapter(null);
				actionBarCenterButtons[firstFlag].setText(majors
						.get(firstFlag - 1).get(majorIndex).getName());

				switch (firstFlag) {

				case LIST_FIRST_FLAG_STUDY_SUBJECT: {

					((StudyFragment) viewPagerFragments[firstFlag])
							.setCurrentSubjectId(0);

				}
					break;

				default:
					break;

				}

				actionBarCenterPopups[firstFlag].dismiss();

			} else {

				subjectList.setAdapter(new SubjectListAdapter(firstFlag,
						majorIndex,
						subjects.get(firstFlag - 1).get(majorIndex), this));
				subjectList.setOnItemClickListener(this);

			}

		}
			break;

		case LIST_SECOND_FLAG_SUBJECT: {

			int majorIndex = (int) (id % 1000 / 100);
			int subjectIndex = (int) (id % 100);
			ListView listView = (ListView) actionBarCenterPopups[firstFlag]
					.getContentView().findViewById(R.id.listView_subjectList);
			SubjectListAdapter adapter = ((SubjectListAdapter) listView
					.getAdapter());
			adapter.setSelection(subjectIndex);
			adapter.notifyDataSetChanged();
			actionBarCenterButtons[firstFlag].setText(subjects
					.get(firstFlag - 1).get(majorIndex).get(subjectIndex)
					.getName());
			actionBarCenterPopups[firstFlag].dismiss();

			switch (firstFlag) {

			case LIST_FIRST_FLAG_STUDY_SUBJECT: {

				long subjectId = ((SubjectDataEntry) adapter
						.getItem(subjectIndex)).getId();
				((StudyFragment) viewPagerFragments[firstFlag])
						.setCurrentSubjectId(subjectId);

			}
				break;

			default:
				break;

			}

		}
			break;

		default:
			break;

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		init();

	}

	private void init() {

		Utils.init(getResources());
		dataTransferTask = new DataTransferTask();
		new Thread(dataTransferTask).start();

		setContentView(R.layout.activity_main);
		Drawable drawable = null;

		majors = new ArrayList<ArrayList<MajorDataEntry>>();
		subjects = new ArrayList<ArrayList<ArrayList<SubjectDataEntry>>>();
		studyMajors = new ArrayList<MajorDataEntry>();
		exercisesMajors = new ArrayList<MajorDataEntry>();
		examMajors = new ArrayList<MajorDataEntry>();
		majors.add(studyMajors);
		majors.add(exercisesMajors);
		majors.add(examMajors);

		Point point = new Point();
		getWindowManager().getDefaultDisplay().getSize(point);
		screenWidth = point.x;
		screenHeight = point.y;

		handler = new MainHandler();
		handler.post(new MajorsRefreshTask());
		gestureDetector = new GestureDetector(this, this);
		gestureDetector.setIsLongpressEnabled(true);

		backgroundView = (FrameLayout) findViewById(R.id.frameLayout_background);

		mainView = (LinearLayout) findViewById(R.id.linearLayout_main);
		actionBar = (LinearLayout) findViewById(R.id.linearLayout_actionBar);
		actionBarLeft = (FrameLayout) findViewById(R.id.frameLayout_actionBarLeft);
		actionBarCenter = (FrameLayout) findViewById(R.id.frameLayout_actionBarCenter);
		actionBarRight = (FrameLayout) findViewById(R.id.frameLayout_actionBarRight);
		userBtn = (Button) findViewById(R.id.button_user);
		userBtn.setOnClickListener(this);
		closeBtn = (Button) findViewById(R.id.button_close);
		closeBtn.setOnClickListener(this);
		backOnLeftBtn = new Button(this);
		backOnLeftBtn.setLayoutParams(userBtn.getLayoutParams());
		backOnLeftBtn.setGravity(userBtn.getGravity());
		backOnLeftBtn.setBackground(getResources().getDrawable(
				R.drawable.button_backonleft));
		backOnLeftBtn.setOnClickListener(this);
		backOnRightBtn = new Button(this);
		backOnRightBtn.setLayoutParams(closeBtn.getLayoutParams());
		backOnRightBtn.setGravity(closeBtn.getGravity());
		backOnRightBtn.setBackground(getResources().getDrawable(
				R.drawable.button_backonright));
		backOnRightBtn.setOnClickListener(this);

		newsArticleBtn = (Button) findViewById(R.id.button_newsArticle);
		newsArticleBtn.setText(newsArticles[0]);
		newsArticleBtn.setOnClickListener(this);
		studySubjectBtn = new Button(this);
		studySubjectBtn.setLayoutParams(newsArticleBtn.getLayoutParams());
		studySubjectBtn.setPadding(newsArticleBtn.getPaddingLeft(),
				newsArticleBtn.getPaddingTop(),
				newsArticleBtn.getPaddingRight(),
				newsArticleBtn.getPaddingBottom());
		studySubjectBtn.setGravity(newsArticleBtn.getGravity());
		studySubjectBtn.setBackground(newsArticleBtn.getBackground());
		drawable = getResources()
				.getDrawable(actionBarCenterDropDownDrawableId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		studySubjectBtn.setCompoundDrawables(null, null, drawable, null);
		studySubjectBtn.setCompoundDrawablePadding(newsArticleBtn
				.getCompoundDrawablePadding());
		studySubjectBtn.setTextColor(newsArticleBtn.getTextColors());
		studySubjectBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
		studySubjectBtn.setOnClickListener(this);
		exercisesSubjectBtn = new Button(this);
		exercisesSubjectBtn.setLayoutParams(newsArticleBtn.getLayoutParams());
		exercisesSubjectBtn.setPadding(newsArticleBtn.getPaddingLeft(),
				newsArticleBtn.getPaddingTop(),
				newsArticleBtn.getPaddingRight(),
				newsArticleBtn.getPaddingBottom());
		exercisesSubjectBtn.setGravity(newsArticleBtn.getGravity());
		exercisesSubjectBtn.setBackground(newsArticleBtn.getBackground());
		drawable = getResources()
				.getDrawable(actionBarCenterDropDownDrawableId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		exercisesSubjectBtn.setCompoundDrawables(null, null, drawable, null);
		exercisesSubjectBtn.setCompoundDrawablePadding(newsArticleBtn
				.getCompoundDrawablePadding());
		exercisesSubjectBtn.setTextColor(newsArticleBtn.getTextColors());
		exercisesSubjectBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
		exercisesSubjectBtn.setOnClickListener(this);
		examSubjectBtn = new Button(this);
		examSubjectBtn.setLayoutParams(newsArticleBtn.getLayoutParams());
		examSubjectBtn.setPadding(newsArticleBtn.getPaddingLeft(),
				newsArticleBtn.getPaddingTop(),
				newsArticleBtn.getPaddingRight(),
				newsArticleBtn.getPaddingBottom());
		examSubjectBtn.setGravity(newsArticleBtn.getGravity());
		examSubjectBtn.setBackground(newsArticleBtn.getBackground());
		drawable = getResources()
				.getDrawable(actionBarCenterDropDownDrawableId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		examSubjectBtn.setCompoundDrawables(null, null, drawable, null);
		examSubjectBtn.setCompoundDrawablePadding(newsArticleBtn
				.getCompoundDrawablePadding());
		examSubjectBtn.setTextColor(newsArticleBtn.getTextColors());
		examSubjectBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
		examSubjectBtn.setOnClickListener(this);
		actionBarCenterButtons = new Button[] { newsArticleBtn,
				studySubjectBtn, exercisesSubjectBtn, examSubjectBtn };

		contentView = (FrameLayout) findViewById(R.id.frameLayout_contentView);
		mainContentView = (LinearLayout) findViewById(R.id.linearLayout_mainContentView);
		viewPager = (ViewPager) findViewById(R.id.viewPager_main);
		viewPagerFragments = new BackableFragment[] { new NewsFragment(),
				new StudyFragment(), new ExercisesFragment(),
				new ExamFragment() };
		viewPager.setAdapter(new MainFragmentPagerAdapter(viewPagerFragments,
				getSupportFragmentManager()));
		viewPager.setOffscreenPageLimit(viewPagerFragments.length);
		viewPager.setOnPageChangeListener(this);

		tabWidget = (TabWidget) findViewById(R.id.tabWidget_main);
		tabButtons = new Button[tabTexts.length];

		for (int n = 0; n < tabButtons.length; n++) {

			tabButtons[n] = new Button(this);
			tabButtons[n].setBackgroundColor(getResources().getColor(
					R.color.transparent));
			drawable = getResources().getDrawable(tabDrawablesId[n]);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			tabButtons[n].setCompoundDrawables(null, drawable, null, null);
			tabButtons[n].setTextColor(getResources().getColorStateList(
					tabTextColorId));
			tabButtons[n].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
			tabButtons[n].setText(tabTexts[n]);
			tabWidget.addView(tabButtons[n]);
			tabButtons[n].setOnClickListener(this);

		}

		tabWidget.setCurrentTab(0);
		currentContentFragment = viewPagerFragments[0];

		currentView = mainView;

		userView = (LinearLayout) findViewById(R.id.linearLayout_user);
		userInfoBtn = (Button) findViewById(R.id.button_userInfo);
		userInfoBtn.setOnClickListener(this);
		settingBtn = (Button) findViewById(R.id.button_setting);
		settingBtn.setOnClickListener(this);
		checkUpdateBtn = (Button) findViewById(R.id.button_checkUpdate);
		checkUpdateBtn.setOnClickListener(this);
		logoutBtn = (Button) findViewById(R.id.button_logout);
		logoutBtn.setOnClickListener(this);
		exitBtn = (Button) findViewById(R.id.button_exit);
		exitBtn.setOnClickListener(this);

		userInfoFragment = new UserInfoFragment();

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		super.onWindowFocusChanged(hasFocus);

		if (hasFocus) {

			Rect rect = new Rect();
			getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
			drawableWidth = rect.width();
			drawableHeigth = rect.height();

			LayoutParams layoutParams = (LayoutParams) userView
					.getLayoutParams();
			layoutParams.width = drawableWidth * 3 / 4;
			layoutParams.height = drawableHeigth;
			userView.setLayoutParams(layoutParams);

			if (newsArticlePopup == null) {

				View newsArticleView = getLayoutInflater().inflate(
						R.layout.popupwindow_newsarticlelist, null);
				ListView newsArticleList = (ListView) newsArticleView
						.findViewById(R.id.listView_newsArticleList);
				newsArticleList.setAdapter(new NewsArticleListAdapter(
						newsArticles, this));
				newsArticleList.setOnItemClickListener(this);
				newsArticlePopup = new PopupWindow(newsArticleView,
						actionBar.getWidth() / 2, LayoutParams.WRAP_CONTENT);
				newsArticlePopup.setFocusable(true);
				newsArticlePopup.setBackgroundDrawable(new ColorDrawable(
						getResources().getColor(R.color.transparent)));
				newsArticlePopup.setOutsideTouchable(true);
				newsArticlePopup
						.setOnDismissListener(new PopupWindow.OnDismissListener() {

							@Override
							public void onDismiss() {

								Drawable drawable = getResources().getDrawable(
										actionBarCenterDropDownDrawableId);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								newsArticleBtn.setCompoundDrawables(null, null,
										drawable, null);

							}

						});

			}

			if (studySubjectPopup == null) {

				View subjectView = getLayoutInflater().inflate(
						R.layout.popupwindow_subjectlist, null);
				ListView majorList = (ListView) subjectView
						.findViewById(R.id.listView_majorList);
				majorList.setOnItemClickListener(this);
				studySubjectPopup = new PopupWindow(subjectView,
						actionBar.getWidth(), LayoutParams.WRAP_CONTENT);
				studySubjectPopup.setFocusable(true);
				studySubjectPopup.setBackgroundDrawable(new ColorDrawable(
						getResources().getColor(R.color.transparent)));
				studySubjectPopup.setOutsideTouchable(true);
				studySubjectPopup
						.setOnDismissListener(new PopupWindow.OnDismissListener() {

							@Override
							public void onDismiss() {

								Drawable drawable = getResources().getDrawable(
										actionBarCenterDropDownDrawableId);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								studySubjectBtn.setCompoundDrawables(null,
										null, drawable, null);

							}

						});

			}

			if (exercisesSubjectPopup == null) {

				View subjectView = getLayoutInflater().inflate(
						R.layout.popupwindow_subjectlist, null);
				ListView majorList = (ListView) subjectView
						.findViewById(R.id.listView_majorList);
				majorList.setOnItemClickListener(this);
				exercisesSubjectPopup = new PopupWindow(subjectView,
						actionBar.getWidth(), LayoutParams.WRAP_CONTENT);
				exercisesSubjectPopup.setFocusable(true);
				exercisesSubjectPopup.setBackgroundDrawable(new ColorDrawable(
						getResources().getColor(R.color.transparent)));
				exercisesSubjectPopup.setOutsideTouchable(true);
				exercisesSubjectPopup
						.setOnDismissListener(new PopupWindow.OnDismissListener() {

							@Override
							public void onDismiss() {

								Drawable drawable = getResources().getDrawable(
										actionBarCenterDropDownDrawableId);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								exercisesSubjectBtn.setCompoundDrawables(null,
										null, drawable, null);

							}

						});

			}

			if (examSubjectPopup == null) {

				View subjectView = getLayoutInflater().inflate(
						R.layout.popupwindow_subjectlist, null);
				ListView majorList = (ListView) subjectView
						.findViewById(R.id.listView_majorList);
				majorList.setOnItemClickListener(this);
				examSubjectPopup = new PopupWindow(subjectView,
						actionBar.getWidth(), LayoutParams.WRAP_CONTENT);
				examSubjectPopup.setFocusable(true);
				examSubjectPopup.setBackgroundDrawable(new ColorDrawable(
						getResources().getColor(R.color.transparent)));
				examSubjectPopup.setOutsideTouchable(true);
				examSubjectPopup
						.setOnDismissListener(new PopupWindow.OnDismissListener() {

							@Override
							public void onDismiss() {

								Drawable drawable = getResources().getDrawable(
										actionBarCenterDropDownDrawableId);
								drawable.setBounds(0, 0,
										drawable.getMinimumWidth(),
										drawable.getMinimumHeight());
								examSubjectBtn.setCompoundDrawables(null, null,
										drawable, null);

							}

						});

			}

			if (actionBarCenterPopups == null)
				actionBarCenterPopups = new PopupWindow[] { newsArticlePopup,
						studySubjectPopup, exercisesSubjectPopup,
						examSubjectPopup };

			if (glassView == null) {

				glassView = new View(this);
				glassView.setAlpha(0f);
				glassView.setClickable(true);
				glassView.setLongClickable(true);
				glassView.setOnClickListener(this);
				backgroundView.addView(glassView, 2,
						backgroundView.getLayoutParams());
				glassView.setVisibility(View.GONE);

			}

			if (slideGlassView == null) {

				slideGlassView = new View(this);
				slideGlassView.setAlpha(0f);
				slideGlassView.setClickable(true);
				slideGlassView.setLongClickable(true);
				slideGlassView.setOnClickListener(null);
				backgroundView.addView(slideGlassView, 2, new LayoutParams(
						drawableWidth / 20, drawableHeigth));

			}

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (!actionLock) {

			if (keyCode == KeyEvent.KEYCODE_BACK) {

				if (currentView == userView) {

					actionLock = true;
					glassView.setVisibility(View.VISIBLE);
					glassView.setLayoutParams(backgroundView.getLayoutParams());
					glassView.setOnClickListener(null);
					handler.post(new UserViewScrollTask(false));

					return true;

				} else if (currentContentFragment.back())
					return true;
				else
					return super.onKeyDown(keyCode, event);

			} else
				return super.onKeyDown(keyCode, event);

		} else
			return true;

	}

	@Override
	public void onClick(View v) {

		if (!actionLock) {

			if (v == glassView) {

				actionLock = true;
				glassView.setVisibility(View.VISIBLE);
				glassView.setLayoutParams(backgroundView.getLayoutParams());
				glassView.setOnClickListener(null);
				handler.post(new UserViewScrollTask(false));

			}

			if (v == userBtn) {

				actionLock = true;
				userView.setVisibility(View.VISIBLE);
				glassView.setVisibility(View.VISIBLE);
				glassView.setLayoutParams(backgroundView.getLayoutParams());
				glassView.setOnClickListener(null);

				handler.post(new UserViewScrollTask(true));

			}

			if (v == closeBtn)
				finish();

			if (v == newsArticleBtn) {

				Drawable drawable = getResources().getDrawable(
						actionBarCenterDropUpDrawableId);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				newsArticleBtn.setCompoundDrawables(null, null, drawable, null);
				newsArticlePopup.showAsDropDown(actionBar,
						actionBar.getWidth() / 4, -newsArticlePopup
								.getContentView().getPaddingTop());

			}

			if (v == studySubjectBtn) {

				Drawable drawable = getResources().getDrawable(
						actionBarCenterDropUpDrawableId);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				studySubjectBtn
						.setCompoundDrawables(null, null, drawable, null);
				studySubjectPopup.showAsDropDown(actionBar, 0,
						-studySubjectPopup.getContentView().getPaddingTop());

			}

			if (v == exercisesSubjectBtn) {

				Drawable drawable = getResources().getDrawable(
						actionBarCenterDropUpDrawableId);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				exercisesSubjectBtn.setCompoundDrawables(null, null, drawable,
						null);
				exercisesSubjectPopup
						.showAsDropDown(actionBar, 0, -exercisesSubjectPopup
								.getContentView().getPaddingTop());

			}

			if (v == examSubjectBtn) {

				Drawable drawable = getResources().getDrawable(
						actionBarCenterDropUpDrawableId);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				examSubjectBtn.setCompoundDrawables(null, null, drawable, null);
				examSubjectPopup.showAsDropDown(actionBar, 0, -examSubjectPopup
						.getContentView().getPaddingTop());

			}

			if (v == backOnLeftBtn || v == backOnRightBtn) {
				currentContentFragment.back();
			}

			for (int n = 0; n < tabButtons.length; n++)
				if (v == tabButtons[n])
					viewPager.setCurrentItem(n, true);

			if (v == userInfoBtn) {

				if (currentContentFragment != userInfoFragment) {

					TextView title = new TextView(this);
					title.setLayoutParams(newsArticleBtn.getLayoutParams());
					title.setGravity(newsArticleBtn.getGravity());
					title.setBackground(newsArticleBtn.getBackground());
					title.setTextColor(newsArticleBtn.getTextColors());
					title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
					title.setText(getString(R.string.user_info));
					actionBarCenter.removeAllViews();
					actionBarCenter.addView(title);
					actionBarRight.removeAllViews();
					actionBarRight.addView(backOnRightBtn);
					contentView.removeAllViews();
					FragmentTransaction fragmentTransaction = getSupportFragmentManager()
							.beginTransaction();
					fragmentTransaction.add(R.id.frameLayout_contentView,
							userInfoFragment);
					fragmentTransaction.commit();
					currentContentFragment = userInfoFragment;

				}

				actionLock = true;
				glassView.setVisibility(View.VISIBLE);
				glassView.setLayoutParams(backgroundView.getLayoutParams());
				glassView.setOnClickListener(null);

				handler.post(new UserViewScrollTask(false));

			}

			if (v == logoutBtn) {

				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				finish();

			}

			if (v == exitBtn)
				finish();

		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		if (slideScrollState != MainActivity.SCROLL_STATE_AUTOSCROLL
				&& onTouchEvent(ev))
			return true;
		else
			return super.dispatchTouchEvent(ev);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (slideScrollState == MainActivity.SCROLL_STATE_ONSCROLL
				&& event.getAction() == MotionEvent.ACTION_UP)
			handler.post(new UserViewScrollTask());

		return gestureDetector.onTouchEvent(event);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		userView.setVisibility(View.VISIBLE);
		float scrollX = e2.getX() - e1.getX();

		if (((currentView == mainView
				&& (e1.getX() < drawableWidth / 20 || (viewPager
						.getCurrentItem() == 0
						&& getCurrentNewsArticeItem() == 0 && e1.getY() > actionBar
						.getHeight()
						+ ((NewsFragment) viewPagerFragments[0]).viewPager
								.getHeight())) && scrollX > 0 && scrollX < userView
				.getWidth()) || (currentView == userView && scrollX < 0 && scrollX > -userView
				.getWidth()))) {

			actionLock = true;
			glassView.setVisibility(View.VISIBLE);
			glassView.setLayoutParams(backgroundView.getLayoutParams());
			glassView.setOnClickListener(null);

			float scrollRatio = Math.abs(scrollX) / userView.getWidth();

			int color1 = getResources().getColor(
					currentView == mainView ? foregroundColorId
							: backgroundColorId);
			int alpha1 = Color.alpha(color1), red1 = Color.red(color1), green1 = Color
					.green(color1), blue1 = Color.blue(color1);
			int color2 = getResources().getColor(
					currentView == mainView ? backgroundColorId
							: foregroundColorId);
			int alpha2 = Color.alpha(color2), red2 = Color.red(color2), green2 = Color
					.green(color2), blue2 = Color.blue(color2);

			backgroundView.setBackgroundColor(Color.argb(
					(int) (alpha1 + (alpha2 - alpha1) * scrollRatio),
					(int) (red1 + (red2 - red1) * scrollRatio),
					(int) (green1 + (green2 - green1) * scrollRatio),
					(int) (blue1 + (blue2 - blue1) * scrollRatio)));

			userView.setPivotX(userView.getX() - userView.getWidth() * 4 / 3);
			userView.setScaleX(currentView == mainView ? 0.75f + scrollRatio / 4
					: 1 - scrollRatio / 4);
			userView.setPivotY(userView.getY() + userView.getHeight() / 2);
			userView.setScaleY(currentView == mainView ? 0.75f + scrollRatio / 4
					: 1 - scrollRatio / 4);
			userView.setAlpha(currentView == mainView ? scrollRatio
					: 1 - scrollRatio);

			mainView.setPivotX(mainView.getX() + mainView.getWidth() * 3);
			mainView.setScaleX(currentView == mainView ? 1 - scrollRatio / 4
					: 0.75f + scrollRatio / 4);
			mainView.setPivotY(mainView.getY() + mainView.getHeight() / 2);
			mainView.setScaleY(currentView == mainView ? 1 - scrollRatio / 4
					: 0.75f + scrollRatio / 4);
			mainView.setAlpha(currentView == mainView ? 1 - scrollRatio / 4
					: 0.75f + scrollRatio / 4);

			slideScrollState = MainActivity.SCROLL_STATE_ONSCROLL;

			return true;

		} else
			return false;

	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

		switch (arg0) {

		case ViewPager.SCROLL_STATE_IDLE: {

			glassView.setVisibility(currentView == mainView ? View.GONE
					: View.VISIBLE);
			actionLock = false;
			break;

		}

		case ViewPager.SCROLL_STATE_DRAGGING: {

			pagerScrollState = MainActivity.SCROLL_STATE_ONSCROLL;
			actionLock = true;
			glassView.setVisibility(View.VISIBLE);
			glassView.setLayoutParams(backgroundView.getLayoutParams());
			glassView.setOnClickListener(null);
			break;

		}

		case ViewPager.SCROLL_STATE_SETTLING: {

			pagerScrollState = MainActivity.SCROLL_STATE_NONE;

			actionBarCenter.removeAllViews();
			actionBarCenter.addView(actionBarCenterButtons[viewPager
					.getCurrentItem()]);
			actionBarCenterButtons[viewPager.getCurrentItem()].setAlpha(1f);

			for (int n = 0; n < tabButtons.length; n++) {

				Drawable drawable = getResources().getDrawable(
						tabDrawablesId[n]);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				tabButtons[n].setCompoundDrawables(null, drawable, null, null);
				tabButtons[n].setTextColor(getResources().getColorStateList(
						tabTextColorId));

			}

			glassView.setVisibility(currentView == mainView ? View.GONE
					: View.VISIBLE);
			actionLock = false;
			break;

		}

		default:
			break;

		}

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

		if (pagerScrollState == MainActivity.SCROLL_STATE_ONSCROLL && arg1 != 0) {

			int currentPager = viewPager.getCurrentItem();
			boolean scrollRight = arg0 == currentPager ? true : false;
			float scrollRatio = scrollRight ? arg1 : 1 - arg1;

			actionBarCenter.removeAllViews();
			actionBarCenter
					.addView(actionBarCenterButtons[scrollRight ? scrollRatio > 0.5f ? currentPager + 1
							: currentPager
							: scrollRatio > 0.5f ? currentPager - 1
									: currentPager]);
			actionBarCenterButtons[scrollRight ? scrollRatio > 0.5f ? currentPager + 1
					: currentPager
					: scrollRatio > 0.5f ? currentPager - 1 : currentPager]
					.setAlpha(scrollRatio > 0.5f ? scrollRatio * 2 - 1
							: 1 - scrollRatio * 2);

			int color1 = getResources().getColor(tabTextOnSelectedColorId);
			int alpha1 = Color.alpha(color1), red1 = Color.red(color1), green1 = Color
					.green(color1), blue1 = Color.blue(color1);
			int color2 = getResources().getColor(tabTextNoSelectedColorId);
			int alpha2 = Color.alpha(color2), red2 = Color.red(color2), green2 = Color
					.green(color2), blue2 = Color.blue(color2);

			Drawable drawable1 = getResources().getDrawable(
					tabOnSelectedDrawablesId[currentPager]);
			drawable1.setAlpha((int) (255 * (1 - scrollRatio)));
			Drawable drawable2 = getResources().getDrawable(
					tabNoSelectedDrawablesId[currentPager]);
			drawable2.setAlpha((int) (255 * scrollRatio));
			LayerDrawable layerDrawable1 = new LayerDrawable(new Drawable[] {
					drawable1, drawable2 });
			layerDrawable1.setBounds(0, 0, layerDrawable1.getMinimumWidth(),
					layerDrawable1.getMinimumHeight());
			tabButtons[currentPager].setCompoundDrawables(null, layerDrawable1,
					null, null);
			tabButtons[currentPager].setTextColor(Color.argb(
					(int) (alpha1 + (alpha2 - alpha1) * scrollRatio),
					(int) (red1 + (red2 - red1) * scrollRatio),
					(int) (green1 + (green2 - green1) * scrollRatio),
					(int) (blue1 + (blue2 - blue1) * scrollRatio)));

			Drawable drawable3 = getResources().getDrawable(
					tabOnSelectedDrawablesId[scrollRight ? currentPager + 1
							: currentPager - 1]);
			drawable3.setAlpha((int) (255 * scrollRatio));
			Drawable drawable4 = getResources().getDrawable(
					tabNoSelectedDrawablesId[scrollRight ? currentPager + 1
							: currentPager - 1]);
			drawable4.setAlpha((int) (255 * (1 - scrollRatio)));
			LayerDrawable layerDrawable2 = new LayerDrawable(new Drawable[] {
					drawable3, drawable4 });
			layerDrawable2.setBounds(0, 0, layerDrawable2.getMinimumWidth(),
					layerDrawable2.getMinimumHeight());
			tabButtons[scrollRight ? currentPager + 1 : currentPager - 1]
					.setCompoundDrawables(null, layerDrawable2, null, null);
			tabButtons[scrollRight ? currentPager + 1 : currentPager - 1]
					.setTextColor(Color.argb((int) (alpha2 + (alpha1 - alpha2)
							* scrollRatio), (int) (red2 + (red1 - red2)
							* scrollRatio), (int) (green2 + (green1 - green2)
							* scrollRatio), (int) (blue2 + (blue1 - blue2)
							* scrollRatio)));

		}

	}

	@Override
	public void onPageSelected(int arg0) {

		tabWidget.setCurrentTab(arg0);
		currentContentFragment = viewPagerFragments[arg0];

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		int firstFlag = (int) id / 10000;

		switch (firstFlag) {

		case LIST_FIRST_FLAG_NEWS_ARTICLE: {

			int newsArticleIndex = (int) (id % 10000);
			setCurrnetNewsArticeItem(newsArticleIndex);
			((NewsFragment) viewPagerFragments[0])
					.setCurrentViewItem(newsArticleIndex);
			newsArticlePopup.dismiss();

		}
			break;

		default: {

			setCurrentSubjectId(id);

		}
			break;

		}

	}

}
