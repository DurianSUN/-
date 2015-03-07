package cn.com.lab.studyclient.login;

import java.io.IOException;

import org.json.JSONException;

import cn.com.lab.studyclient.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class RegisterActivity extends Activity {
	private ImageView regis_head;
	private EditText edit_name;
	private EditText edit_pass;
	private EditText edit_checkpass;
	private String name;
	private String pass;
	private String check_pass;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.part_login_register);

		initWidgets();
		edit_name.setHintTextColor(getResources().getColor(R.color.deepwhite));
		edit_pass.setHintTextColor(getResources().getColor(R.color.deepwhite));
		edit_checkpass.setHintTextColor(getResources().getColor(
				R.color.deepwhite));

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.part_login_head1);
		regis_head.setImageBitmap(Util.toRound(bitmap));
	}

	private void initWidgets() {
		regis_head = (ImageView) findViewById(R.id.regis_head);
		edit_name = (EditText) findViewById(R.id.regis_name);
		edit_pass = (EditText) findViewById(R.id.regis_pass);
		edit_checkpass = (EditText) findViewById(R.id.regis_check_pass);
	}

	public void back(View v) {
		startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
		overridePendingTransition(R.anim.part_login_login_in,
				R.anim.part_login_regis_out);
	}

	public void register(View v) {
		name = edit_name.getText().toString();
		pass = edit_pass.getText().toString();
		check_pass = edit_checkpass.getText().toString();
		if (name.trim().equals("") || pass.trim().equals("")
				|| check_pass.trim().equals("")) {
			Util.alertDialog(RegisterActivity.this, "输入不能为空！");
			return;
		}
		if (!pass.trim().equals(check_pass.trim())) {
			Util.alertDialog(RegisterActivity.this, "两次密码不一致");
		} else {
			new Thread(regisRun).start();
		}
	}

	private Handler handler = new Handler() {
		@Override
		// 当有消息发送出来的时候就执行Handler的这个方法
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Util.showText(RegisterActivity.this, "注册成功，返回登录页面");
				Intent i = new Intent(RegisterActivity.this,
						LoginActivity.class);
				i.putExtra("name", name);
				i.putExtra("pass", pass);
				setResult(10, i);
				finish();
				break;
			case 1:
				Util.alertDialog(RegisterActivity.this, "学号不存在 ");
				break;
			case 2:
				Util.alertDialog(RegisterActivity.this, "用户ID已注册 ");
				break;
			case 3:
				Util.alertDialog(RegisterActivity.this, "密码长度不符 ");
				break;
			case 4:
				Util.alertDialog(RegisterActivity.this, "密码含非法字符 ");
				break;
			case Util.JSONExceptionCode:
				Util.alertDialog(RegisterActivity.this, "数据解析错误" + msg.obj);
				break;
			case Util.IOExceptionCode:
				Util.alertDialog(RegisterActivity.this, "无法连接服务器" + msg.obj);
				break;
			default:
				break;
			}
		}
	};

	Runnable regisRun = new Runnable() {
		@Override
		public void run() {
			// 你要执行的方法
			String returnStr;
			Message msg = new Message();
			try {
				returnStr = Util.getRegis(name, pass);
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
}
