package cn.com.lab.studyclient.fragment;

import cn.com.lab.studyclient.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ExercisesFragment extends BackableFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_exercises, null);

	}

	@Override
	public boolean back() {
		// TODO 自动生成的方法存根
		return false;
	}

}
