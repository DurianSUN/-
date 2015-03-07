package cn.com.lab.studyclient.login;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class Util {
	private static final String hostAddress = "http://113.251.170.134/";
	public static final int JSONExceptionCode = 99;
	public static final int IOExceptionCode = 100;

	public static String getLogin(String name, String pass)
			throws JSONException, IOException {
		String request = hostAddress + "studyclient/login?user_student_id="
				+ name + "&password=" + pass;
		return conOperation(request, "login_return");
	}

	public static String getRegis(String name, String pass) throws IOException,
			JSONException {
		String request = hostAddress + "studyclient/register?user_student_id="
				+ name + "&password=" + pass;
		return conOperation(request, "register_return");
	}

	private static String conOperation(String request, String jsonRequst)
			throws IOException, JSONException {
		String returnStr = "";
		URL url = new URL(request);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setReadTimeout(5000);
		if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
			InputStreamReader isr = new InputStreamReader(con.getInputStream(),
					"utf-8");
			int i;
			String content = "";
			while (((i = isr.read()) != -1)) {
				content = content + (char) i;
			}
			isr.close();
			JSONObject json = new JSONObject(content);
			returnStr = json.getString(jsonRequst);
		}
		return returnStr;
	}

	public static Bitmap toRound(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);

		paint.setColor(color);
		canvas.drawOval(rectF, paint); // »­Ò»¸öÔ²
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static List<View> getAllChildViews(View view) {
		List<View> allchildren = new ArrayList<View>();
		if (view instanceof ViewGroup) {
			ViewGroup vp = (ViewGroup) view;
			for (int i = 0; i < vp.getChildCount(); i++) {
				View viewchild = vp.getChildAt(i);
				allchildren.add(viewchild);
				allchildren.addAll(getAllChildViews(viewchild));
			}
		}
		return allchildren;
	}

	public static void alertDialog(Context c, String alertString) {
		AlertDialog.Builder builder = new Builder(c);
		builder.setMessage(alertString);
		builder.create().show();
	}

	public static void showText(Context c, String str) {
		Toast.makeText(c, str, Toast.LENGTH_SHORT).show();
	}
}
