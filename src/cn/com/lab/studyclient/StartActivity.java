package cn.com.lab.studyclient;

import cn.com.lab.studyclient.login.LoginActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ImageView imageView = new ImageView(this);
		imageView
				.setImageDrawable(getResources().getDrawable(R.drawable.start));
		imageView.setScaleType(ScaleType.FIT_XY);
		setContentView(imageView);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				startActivity(new Intent(StartActivity.this,
						LoginActivity.class));
				finish();

			}

		}, 2000);

	}

}
