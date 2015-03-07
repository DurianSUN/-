package cn.com.lab.studyclient.login;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import cn.com.lab.studyclient.MainActivity;
import cn.com.lab.studyclient.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class LoginActivity extends Activity {
	public static String user_student_id;
	private ImageView head_image;
	private EditText edit_name;
	private EditText edit_pass;
	private String name;
	private String pass;
	private List<String> data;
	private List<String> read_data;
	private ToggleButton remember_btn;
	private TextView forget_text;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.part_login_login);

		initWidgets();
		initLogin();
	}

	private void initLogin() {
		data = new ArrayList<String>();
		readData(getBaseContext());
		if (read_data != null && read_data.size() != 0) {
			edit_name.setText(read_data.get(0));
			edit_pass.setText(read_data.get(1));
			remember_btn.setChecked(true);
		} else {
			remember_btn.setChecked(false);
		}
	}

	private void initWidgets() {
		head_image = (ImageView) findViewById(R.id.imageView1);
		remember_btn = (ToggleButton) findViewById(R.id.imageButton1);
		forget_text = (TextView) findViewById(R.id.textView2);
		forget_text.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		edit_name = (EditText) findViewById(R.id.editText1);
		edit_pass = (EditText) findViewById(R.id.editText2);
		edit_name.setHintTextColor(getResources().getColor(R.color.holowhite));
		edit_pass.setHintTextColor(getResources().getColor(R.color.holowhite));

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.part_login_head1);
		head_image.setImageBitmap(Util.toRound(bitmap));

		edit_name.setText("12345");
		edit_pass.setText("12345");
	}

	public void login(View v) {
		name = edit_name.getText().toString();
		pass = edit_pass.getText().toString();
		if (name.trim().equals("") || pass.trim().equals("")) {
			Util.showText(LoginActivity.this, "输入不能为空");
			return;
		}
		new Thread(loginRun).start();
	}

	private Handler handler = new Handler() {

		@Override
		// 当有消息发送出来的时候就执行Handler的这个方法
		public void handleMessage(Message msg) { // 处理UI
			switch (msg.what) {
			case 0:
				Util.showText(LoginActivity.this, "登陆成功");
				if (remember_btn.isChecked()) {
					data.add(name);
					data.add(pass);
					saveData(getBaseContext());
				} else {
					data.clear();
					saveData(getBaseContext());
				}
				user_student_id = edit_name.getText().toString();
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
				finish();
				break;
			case 1:
				Util.alertDialog(LoginActivity.this, "用户ID未注册  ");
				break;
			case 2:
				Util.alertDialog(LoginActivity.this, "密码错误");
				break;
			case Util.JSONExceptionCode:
				Util.alertDialog(LoginActivity.this, "数据解析错误" + msg.obj);
				break;
			case Util.IOExceptionCode:
				Util.alertDialog(LoginActivity.this, "无法连接服务器" + msg.obj);
				break;
			default:
				break;
			}
		}
	};

	Runnable loginRun = new Runnable() {
		@Override
		public void run() {
			String returnStr;
			Message msg = new Message();
			try {
				returnStr = Util.getLogin(name, pass);
				if (!returnStr.equals("")) {
					int resultCode = Integer.valueOf(returnStr);
					msg.what = resultCode;
					handler.sendMessage(msg);
				} else {
					msg.what = Util.JSONExceptionCode;
					handler.sendMessage(msg);
				}
			} catch (JSONException e) {
				msg.what = Util.JSONExceptionCode;
				msg.obj = e;
				handler.sendMessage(msg);
			} catch (IOException e) {
				msg.what = Util.IOExceptionCode;
				msg.obj = e;
				handler.sendMessage(msg);
			}

		}
	};

	public void register(View v) {
		Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
		startActivityForResult(i, 100);
		overridePendingTransition(R.anim.part_login_regis_in,
				R.anim.part_login_login_out);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 10) {
			String temp_name = data.getStringExtra("name");
			String temp_pass = data.getStringExtra("pass");
			edit_name.setText(temp_name);
			edit_pass.setText(temp_pass);
		}
	}

	public void forget(View v) {
		// 转到忘记密码获取页面
	}

	public void back(View v) {
		finish();
	}

	public void remember(View v) {
		if (remember_btn.isChecked()) {
			remember_btn.setChecked(false);
		} else {
			remember_btn.setChecked(true);
		}
	}

	public synchronized void saveData(Context c) {
		FileOutputStream fos = null;
		try {
			c.openFileInput("data.dat");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			fos = c.openFileOutput("data.dat", Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			List<String> list = new ArrayList<String>();
			if (data.size() != 0) {
				for (int i = 0; i < data.size(); i++) {
					String txt = data.get(i);
					list.add(txt);
				}
			}
			oos.writeObject(list);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean readData(Context c) {
		FileInputStream fis = null;
		try {
			fis = c.openFileInput("data.dat");
			if (fis != null) {
				ObjectInputStream ois = new ObjectInputStream(fis);
				try {
					@SuppressWarnings("unchecked")
					ArrayList<String> list = (ArrayList<String>) ois
							.readObject();
					read_data = new ArrayList<String>();
					if (list.size() != 0) {
						for (int i = 0; i < list.size(); i++) {
							String txt = list.get(i);
							read_data.add(txt);
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
