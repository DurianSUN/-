package cn.com.lab.studyclient.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import cn.com.lab.studyclient.MainActivity;
import cn.com.lab.studyclient.R;
import cn.com.lab.studyclient.runnable.DataTransferTask.DataTransferHandler;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class UserInfoFragment extends BackableFragment {
	
	public static final int USER_AVATAR = 0;
	public static final int USER_NAME = 1;
	public static final int USER_MAJOR = 2;
	public static final int USER_GRADE = 3;
	public static final int USER_STUDENT_ID = 4;
	public static final int USER_EMAIL = 5;

	private ArrayList<String> userInfo = null;
	
	private Handler handler = null;
	private Handler dataTransferHandler = null;

	public LinearLayout backgroundView = null;
	public ImageButton userAvatarBtn = null;
	public EditText userNameEt = null;
	public EditText userMajorEt = null;
	public EditText userGradeEt = null;
	public EditText userStudentIdEt = null;
	public EditText userEmailEt = null;
	
	public class UserInfoHandler extends Handler {

		public UserInfoHandler() {
			super();
		}

		public UserInfoHandler(Callback arg0) {
			super(arg0);
		}

		public UserInfoHandler(Looper arg0) {
			super(arg0);
		}

		public UserInfoHandler(Looper arg0, Callback arg1) {
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
						DataTransferHandler.ARG1_SEND_USER_INFO_FRAGMENT,
						DataTransferHandler.ARG2_SEND_USER_INFO_FRAGMENT_LOAD,
						UserInfoFragment.this);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("userInfo", userInfo);
				message.setData(bundle);
				message.sendToTarget();
				
			}
			
		}
		
	}
	
	public class InvalidateTask implements Runnable {

		@Override
		public void run() {
			
			userNameEt.setText(userInfo.get(USER_NAME));
			userMajorEt.setText(userInfo.get(USER_MAJOR));
			userGradeEt.setText(userInfo.get(USER_GRADE));
			userStudentIdEt.setText(userInfo.get(USER_STUDENT_ID));
			userEmailEt.setText(userInfo.get(USER_EMAIL));
			
		}
		
	}

	public Handler getHandler() {
		return handler;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_userinfo, null);

		userInfo = new ArrayList<String>();
		
		handler = new UserInfoHandler();

		backgroundView = (LinearLayout) view
				.findViewById(R.id.linearLayout_userInfoBackground);

		userAvatarBtn = (ImageButton) view
				.findViewById(R.id.imageButton_userAvatar);
		userAvatarBtn.setScaleType(ScaleType.FIT_END);
		
		InputStream in = null;
		try {

			AssetManager am = getResources().getAssets();
			in = am.open("head.jpg");
			userAvatarBtn.setImageBitmap(BitmapFactory.decodeStream(in));
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		userNameEt = (EditText) view.findViewById(R.id.editText_userName);
		userMajorEt = (EditText) view.findViewById(R.id.editText_userMajor);
		userGradeEt = (EditText) view.findViewById(R.id.editText_userGrade);
		userStudentIdEt = (EditText) view
				.findViewById(R.id.editText_userStudentId);
		userEmailEt = (EditText) view.findViewById(R.id.editText_userEmail);
		handler.post(new DataLoadTask());

		return view;

	}

	@Override
	public boolean back() {

		MainActivity activity = (MainActivity) getActivity();
		activity.actionBarCenter.removeAllViews();
		activity.actionBarCenter
				.addView(activity.actionBarCenterButtons[activity.viewPager
						.getCurrentItem()]);
		activity.actionBarRight.removeAllViews();
		activity.actionBarRight.addView(activity.closeBtn);
		activity.contentView.removeAllViews();
		FragmentTransaction fragmentTransaction = activity
				.getSupportFragmentManager().beginTransaction();
		fragmentTransaction.remove(activity.userInfoFragment);
		fragmentTransaction.commit();
		activity.contentView.addView(activity.mainContentView);
		activity.currentContentFragment = activity.viewPagerFragments[activity.viewPager
				.getCurrentItem()];

		return true;
		
	}
	//
	// private void skipToUserInfoView() {
	//
	// Animation animationA = AnimationUtils.loadAnimation(getActivity(),
	// R.anim.in_screen_left_to_right);
	// animationA.setAnimationListener(new AnimationListener() {
	//
	// @Override
	// public void onAnimationStart(Animation animation) {
	//
	// userInfoView.setVisibility(View.VISIBLE);
	// glassView.setVisibility(View.VISIBLE);
	//
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation) {
	// // TODO 自动生成的方法存根
	//
	// }
	//
	// @Override
	// public void onAnimationEnd(Animation animation) {
	//
	// glassView.setVisibility(View.GONE);
	// currentView = userInfoView;
	//
	// }
	//
	// });
	// userInfoView.startAnimation(animationA);
	// Animation animationB = AnimationUtils.loadAnimation(getActivity(),
	// R.anim.out_screen_left_to_right);
	// animationB.setAnimationListener(new AnimationListener() {
	//
	// @Override
	// public void onAnimationStart(Animation animation) {
	//
	// editUserInfoView.setVisibility(View.GONE);
	// glassView.setVisibility(View.VISIBLE);
	//
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation) {
	// // TODO 自动生成的方法存根
	//
	// }
	//
	// @Override
	// public void onAnimationEnd(Animation animation) {
	// glassView.setVisibility(View.GONE);
	// }
	//
	// });
	// editUserInfoView.startAnimation(animationB);
	//
	// }
	//
	// private void skipToEditUserInfoView() {
	//
	// if (glassView == null) {
	//
	// glassView = new View(getActivity());
	// glassView.setAlpha(0f);
	// glassView.setClickable(true);
	// glassView.setLongClickable(true);
	// backgroundView.addView(glassView, 2,
	// backgroundView.getLayoutParams());
	//
	// }
	//
	// Animation animationA = AnimationUtils.loadAnimation(getActivity(),
	// R.anim.out_screen_right_to_left);
	// animationA.setAnimationListener(new AnimationListener() {
	//
	// @Override
	// public void onAnimationStart(Animation animation) {
	//
	// userInfoView.setVisibility(View.GONE);
	// glassView.setVisibility(View.VISIBLE);
	//
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation) {
	// // TODO 自动生成的方法存根
	//
	// }
	//
	// @Override
	// public void onAnimationEnd(Animation animation) {
	// glassView.setVisibility(View.GONE);
	// }
	//
	// });
	// userInfoView.startAnimation(animationA);
	// Animation animationB = AnimationUtils.loadAnimation(getActivity(),
	// R.anim.in_screen_right_to_left);
	// animationB.setAnimationListener(new AnimationListener() {
	//
	// @Override
	// public void onAnimationStart(Animation animation) {
	//
	// editUserInfoView.setVisibility(View.VISIBLE);
	// glassView.setVisibility(View.VISIBLE);
	//
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation) {
	// // TODO 自动生成的方法存根
	//
	// }
	//
	// @Override
	// public void onAnimationEnd(Animation animation) {
	//
	// glassView.setVisibility(View.GONE);
	// currentView = editUserInfoView;
	//
	// }
	//
	// });
	// editUserInfoView.startAnimation(animationB);
	//
	// }
	//
}
