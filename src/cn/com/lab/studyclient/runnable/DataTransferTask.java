package cn.com.lab.studyclient.runnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.lab.studyclient.MainActivity;
import cn.com.lab.studyclient.data.MajorDataEntry;
import cn.com.lab.studyclient.data.NewsTitleListDataEntry;
import cn.com.lab.studyclient.data.StudyTitleListDataEntry;
import cn.com.lab.studyclient.data.SubjectDataEntry;
import cn.com.lab.studyclient.fragment.NewsFragment;
import cn.com.lab.studyclient.fragment.StudyFragment;
import cn.com.lab.studyclient.fragment.UserInfoFragment;
import cn.com.lab.studyclient.login.LoginActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

public class DataTransferTask implements Runnable {

	private static final String hostAddress = "http://113.251.170.134/";
	private static final String authorization = "studyclient:123456";

	private static final int DATA_DOWN_LOAD_STATE_NONE = 0;
	private static final int DATA_DOWN_LOAD_STATE_LOADING = 1;
	private static final int DATA_DOWN_LOAD_STATE_FINISH = 2;

	private int majorsDataDownLoadState = 0;
	private int subjectsDataDownLoadState = 0;
	private int newsTitleListDataDownLoadState = 0;
	private int studyTitleListDataDownLoadState = 0;

	private HashMap<String, Long> newsTitleListDataRequestIdMap = null;
	private HashMap<String, ArrayList<NewsTitleListDataEntry>> newsTitleListDataMap = null;
	private HashMap<String, ArrayList<MajorDataEntry>> majorsDataMap = null;
	private HashMap<Long, ArrayList<SubjectDataEntry>> subjectsDataMap = null;
	private HashMap<Long, ArrayList<StudyTitleListDataEntry>> studyTitleListDataMap = null;
	private Handler handler = null;

	public class DataTransferHandler extends Handler {

		public static final int WHAT_SEND = 1;
		public static final int ARG1_SEND_MAIN_ACTIVITY = 0;
		public static final int ARG2_SEND_MAIN_ACTIVITY_LOAD_MAJORS = 1;
		public static final int ARG2_SEND_MAIN_ACTIVITY_LOAD_SUBJECTS = 2;
		public static final int ARG1_SEND_NEWS_FRAGMENT = 1;
		public static final int ARG2_SEND_NEWS_FRAGMENT_REFRESH = 1;
		public static final int ARG2_SEND_NEWS_FRAGMENT_LOAD_MORE = 2;
		public static final int ARG1_SEND_STUDY_FRAGMENT = 2;
		public static final int ARG2_SEND_STUDY_FRAGMENT_LOAD = 1;
		public static final int ARG1_SEND_EXERCISES_FRAGMENT = 3;
		public static final int ARG1_SEND_EXAM_FRAGMENT = 4;
		public static final int ARG1_SEND_USER_INFO_FRAGMENT = 5;
		public static final int ARG2_SEND_USER_INFO_FRAGMENT_LOAD = 1;

		public DataTransferHandler() {
			super();
		}

		public DataTransferHandler(Callback arg0) {
			super(arg0);
		}

		public DataTransferHandler(Looper arg0) {
			super(arg0);
		}

		public DataTransferHandler(Looper arg0, Callback arg1) {
			super(arg0, arg1);
		}

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case WHAT_SEND: {

				switch (msg.arg1) {

				case ARG1_SEND_MAIN_ACTIVITY: {

					switch (msg.arg2) {

					case ARG2_SEND_MAIN_ACTIVITY_LOAD_MAJORS: {

						if (majorsDataDownLoadState == DATA_DOWN_LOAD_STATE_FINISH) {

							Bundle bundle = msg.getData();
							String key = bundle.getString("key");
							ArrayList<MajorDataEntry> majors = bundle
									.getParcelableArrayList(key);
							majors.clear();
							majors.addAll(majorsDataMap.get(key));

							((MainActivity) msg.obj)
									.getHandler()
									.post(((MainActivity) msg.obj).new ActionBarCenterInvalidateTask());
							((MainActivity) msg.obj)
									.getHandler()
									.post(((MainActivity) msg.obj).new SubjectsRefreshTask());

						} else
							((MainActivity) msg.obj)
									.getHandler()
									.postDelayed(
											((MainActivity) msg.obj).new MajorsRefreshTask(),
											200);

					}
						break;

					case ARG2_SEND_MAIN_ACTIVITY_LOAD_SUBJECTS: {

						if (subjectsDataDownLoadState == DATA_DOWN_LOAD_STATE_FINISH) {

							Bundle bundle = msg.getData();
							String key = bundle.getString("key");
							ArrayList<MajorDataEntry> majors = bundle
									.getParcelableArrayList(key);

							for (int n = 0; n < majors.size(); n++) {

								ArrayList<SubjectDataEntry> subjects = bundle
										.getParcelableArrayList(String
												.valueOf(majors.get(n).getId()));
								subjects.clear();
								subjects.addAll(subjectsDataMap.get(majors.get(
										n).getId()));

							}

						} else
							((MainActivity) msg.obj)
									.getHandler()
									.postDelayed(
											((MainActivity) msg.obj).new SubjectsRefreshTask(),
											200);

					}
						break;

					default:
						break;

					}

				}
					break;

				case ARG1_SEND_NEWS_FRAGMENT: {

					Bundle bundle = msg.getData();
					String key = bundle.getString("key");
					ArrayList<NewsTitleListDataEntry> list = bundle
							.getParcelableArrayList(key);

					synchronized (msg.obj) {

						switch (msg.arg2) {

						case ARG2_SEND_NEWS_FRAGMENT_REFRESH: {

							try {

								newsTitleListDataDownLoadState = DATA_DOWN_LOAD_STATE_LOADING;
								HttpURLConnection conn = (HttpURLConnection) new URL(
										hostAddress
												+ "studyclient/news_title_list_data?article="
												+ key
												+ "&request_mode=refresh&request_data_id=null")
										.openConnection();
								conn.setConnectTimeout(5000);
								conn.setRequestMethod("GET");
								conn.setRequestProperty(
										"Authorization",
										"Basic "
												+ Base64.encodeToString(
														authorization
																.getBytes(),
														Base64.NO_WRAP));
								conn.connect();

								if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

									InputStreamReader inputStreamReader = new InputStreamReader(
											conn.getInputStream());
									BufferedReader bufferedReader = new BufferedReader(
											inputStreamReader);
									StringBuffer stringBuffer = new StringBuffer();
									String line = null;

									if ((line = bufferedReader.readLine()) != null)
										stringBuffer.append(line);

									bufferedReader.close();
									inputStreamReader.close();

									JSONArray jsonArray = new JSONArray(
											stringBuffer.toString());
									list.clear();

									for (int n = 0; n < jsonArray.length(); n++) {

										JSONObject jsonObject = jsonArray
												.getJSONObject(n);
										list.add(new NewsTitleListDataEntry(
												jsonObject.getLong("data_id"),
												jsonObject.getString("title"),
												jsonObject.getString("content"),
												jsonObject.getString("time"),
												jsonObject.getString("picture"),
												jsonObject.getString("web_url")));

										if (n == jsonArray.length() - 1)
											newsTitleListDataRequestIdMap
													.put(key, jsonObject
															.getLong("data_id"));

									}

									newsTitleListDataMap.get(key).clear();

								}

								conn.disconnect();
								newsTitleListDataDownLoadState = DATA_DOWN_LOAD_STATE_FINISH;

							} catch (IOException | JSONException e) {
								e.printStackTrace();
							}

							((NewsFragment) msg.obj)
									.getHandler()
									.post(((NewsFragment) msg.obj).new InvalidateTask());

						}
							break;

						case ARG2_SEND_NEWS_FRAGMENT_LOAD_MORE: {

							ArrayList<NewsTitleListDataEntry> listTemp = newsTitleListDataMap
									.get(key);

							if (listTemp.size() > 0) {

								list.addAll(listTemp);
								listTemp.clear();
								((NewsFragment) msg.obj)
										.getHandler()
										.post(((NewsFragment) msg.obj).new InvalidateTask());

							}

						}
							break;

						default:
							break;

						}

					}

				}
					break;

				case ARG1_SEND_STUDY_FRAGMENT: {

					switch (msg.arg2) {

					case ARG2_SEND_STUDY_FRAGMENT_LOAD: {

						if (studyTitleListDataDownLoadState == DATA_DOWN_LOAD_STATE_FINISH) {

							Bundle bundle = msg.getData();
							String key = bundle.getString("key");
							ArrayList<StudyTitleListDataEntry> list = bundle
									.getParcelableArrayList(key);

							if ((((StudyFragment) msg.obj).currentSubjectId) == 0) {

								list.clear();
								Set<Long> set = studyTitleListDataMap.keySet();
								Iterator<Long> iterator = set.iterator();

								while (iterator.hasNext())
									list.addAll(studyTitleListDataMap
											.get(iterator.next()));

							} else {

								list.clear();
								list.addAll(studyTitleListDataMap.get(Long
										.valueOf(key)));

							}

							((StudyFragment) msg.obj)
									.getHandler()
									.post(((StudyFragment) msg.obj).new InvalidateTask());

						} else
							((StudyFragment) msg.obj)
									.getHandler()
									.postDelayed(
											((StudyFragment) msg.obj).new DataLoadTask(),
											200);

					}
						break;

					default:
						break;

					}

				}
					break;

				case ARG1_SEND_USER_INFO_FRAGMENT: {

					switch (msg.arg2) {

					case ARG2_SEND_USER_INFO_FRAGMENT_LOAD: {
						
						try {
							
							Bundle bundle = msg.getData();
							ArrayList<String> userInfo = bundle.getStringArrayList("userInfo");
							HttpURLConnection conn = (HttpURLConnection) new URL(
									hostAddress
											+ "studyclient/userinfo?user_id="
											+ LoginActivity.user_student_id)
									.openConnection();
							conn.setConnectTimeout(5000);
							conn.setRequestMethod("GET");
							conn.setRequestProperty(
									"Authorization",
									"Basic "
											+ Base64.encodeToString(
													authorization
															.getBytes(),
													Base64.NO_WRAP));
							conn.connect();

							if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

								InputStreamReader inputStreamReader = new InputStreamReader(
										conn.getInputStream());
								BufferedReader bufferedReader = new BufferedReader(
										inputStreamReader);
								StringBuffer stringBuffer = new StringBuffer();
								String line = null;

								if ((line = bufferedReader.readLine()) != null)
									stringBuffer.append(line);

								bufferedReader.close();
								inputStreamReader.close();

								JSONObject jsonObject = new JSONObject(
										stringBuffer.toString());
								userInfo.clear();
								userInfo.add(jsonObject.getString("user_avatar"));
								userInfo.add(jsonObject.getString("user_name"));
								userInfo.add(jsonObject.getString("user_major"));
								userInfo.add(jsonObject.getString("user_grade"));
								userInfo.add(String.valueOf(LoginActivity.user_student_id));
								userInfo.add(jsonObject.getString("user_email"));

							}

							conn.disconnect();

						} catch (IOException | JSONException e) {
							e.printStackTrace();
						}

						((UserInfoFragment) msg.obj)
								.getHandler()
								.post(((UserInfoFragment) msg.obj).new InvalidateTask());
						
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
				break;

			default:
				super.handleMessage(msg);
				break;

			}

		}

	}

	private class NewsTitleLisDataDownLoadTask implements Runnable {

		@Override
		public void run() {

			while (true) {

				Set<String> set = newsTitleListDataMap.keySet();
				Iterator<String> iterator = set.iterator();

				while (iterator.hasNext()) {

					String key = iterator.next();
					ArrayList<NewsTitleListDataEntry> list = newsTitleListDataMap
							.get(key);

					if (list.isEmpty()) {

						try {

							if (newsTitleListDataDownLoadState != DATA_DOWN_LOAD_STATE_LOADING) {

								newsTitleListDataDownLoadState = DATA_DOWN_LOAD_STATE_LOADING;
								Long requestId = newsTitleListDataRequestIdMap
										.get(key);
								String requestMode = requestId == null ? "refresh"
										: "load_more";
								HttpURLConnection conn = (HttpURLConnection) new URL(
										hostAddress
												+ "studyclient/news_title_list_data?article="
												+ key + "&request_mode="
												+ requestMode
												+ "&request_data_id="
												+ requestId).openConnection();
								conn.setConnectTimeout(5000);
								conn.setRequestMethod("GET");
								conn.setRequestProperty(
										"Authorization",
										"Basic "
												+ Base64.encodeToString(
														authorization
																.getBytes(),
														Base64.NO_WRAP));
								conn.connect();

								if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

									InputStreamReader inputStreamReader = new InputStreamReader(
											conn.getInputStream());
									BufferedReader bufferedReader = new BufferedReader(
											inputStreamReader);
									StringBuffer stringBuffer = new StringBuffer();
									String line = null;

									if ((line = bufferedReader.readLine()) != null)
										stringBuffer.append(line);

									bufferedReader.close();
									inputStreamReader.close();

									JSONArray jsonArray = new JSONArray(
											stringBuffer.toString());
									ArrayList<NewsTitleListDataEntry> dataEntries = (ArrayList<NewsTitleListDataEntry>) newsTitleListDataMap
											.get(key);

									for (int n = 0; n < jsonArray.length(); n++) {

										JSONObject jsonObject = jsonArray
												.getJSONObject(n);
										dataEntries
												.add(new NewsTitleListDataEntry(
														jsonObject
																.getLong("data_id"),
														jsonObject
																.getString("title"),
														jsonObject
																.getString("content"),
														jsonObject
																.getString("time"),
														jsonObject
																.getString("picture"),
														jsonObject
																.getString("web_url")));

										if (n == jsonArray.length() - 1)
											newsTitleListDataRequestIdMap
													.put(key, jsonObject
															.getLong("data_id"));

									}

								}

								conn.disconnect();
								newsTitleListDataDownLoadState = DATA_DOWN_LOAD_STATE_FINISH;

							}

						} catch (IOException | JSONException e) {
							e.printStackTrace();
						}

					}

				}

			}

		}

	}

	private class StudyTitleListDataDownLoadTask implements Runnable {

		@Override
		public void run() {

			try {

				if (majorsDataDownLoadState != DATA_DOWN_LOAD_STATE_LOADING) {

					majorsDataDownLoadState = DATA_DOWN_LOAD_STATE_LOADING;
					majorsDataDownLoad(MainActivity.STUDY_MAJORS_KEY);
					majorsDataDownLoadState = DATA_DOWN_LOAD_STATE_FINISH;

					ArrayList<MajorDataEntry> majors = majorsDataMap
							.get(MainActivity.STUDY_MAJORS_KEY);

					if (subjectsDataDownLoadState != DATA_DOWN_LOAD_STATE_LOADING
							&& studyTitleListDataDownLoadState != DATA_DOWN_LOAD_STATE_LOADING) {

						subjectsDataDownLoadState = DATA_DOWN_LOAD_STATE_LOADING;
						studyTitleListDataDownLoadState = DATA_DOWN_LOAD_STATE_LOADING;

						for (int n = 0; n < majors.size(); n++) {

							subjectsDataDownLoad(majors.get(n).getId());
							ArrayList<SubjectDataEntry> subjects = subjectsDataMap
									.get(majors.get(n).getId());

							for (int m = 0; m < subjects.size(); m++)
								studyTitleListDataDownLoad(subjects.get(m)
										.getId());

						}

						subjectsDataDownLoadState = DATA_DOWN_LOAD_STATE_FINISH;
						studyTitleListDataDownLoadState = DATA_DOWN_LOAD_STATE_FINISH;

					}

				}

			} catch (IOException | JSONException e) {
				e.printStackTrace();
			}

		}

	}

	public DataTransferTask() {

		newsTitleListDataRequestIdMap = new HashMap<String, Long>();
		newsTitleListDataMap = new HashMap<String, ArrayList<NewsTitleListDataEntry>>();
		newsTitleListDataMap.put(NewsFragment.SCHOOL_NEWS_LIST_KEY,
				new ArrayList<NewsTitleListDataEntry>());
		newsTitleListDataMap.put(NewsFragment.SCHOOL_NOTICE_LIST_KEY,
				new ArrayList<NewsTitleListDataEntry>());
		newsTitleListDataMap.put(NewsFragment.ACADEMIC_LECTURE_LIST_KEY,
				new ArrayList<NewsTitleListDataEntry>());
		newsTitleListDataMap.put(NewsFragment.PERSONAL_NOTICE_LIST_KEY,
				new ArrayList<NewsTitleListDataEntry>());

		majorsDataMap = new HashMap<String, ArrayList<MajorDataEntry>>();
		subjectsDataMap = new HashMap<Long, ArrayList<SubjectDataEntry>>();
		studyTitleListDataMap = new HashMap<Long, ArrayList<StudyTitleListDataEntry>>();

	}

	public Handler getHandler() {
		return handler;
	}

	private void majorsDataDownLoad(String key) throws MalformedURLException,
			IOException, JSONException {

		HttpURLConnection conn = (HttpURLConnection) new URL(hostAddress
				+ "studyclient/majors").openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		conn.setRequestProperty(
				"Authorization",
				"Basic "
						+ Base64.encodeToString(authorization.getBytes(),
								Base64.NO_WRAP));
		conn.connect();

		if (majorsDataMap.get(key) == null)
			majorsDataMap.put(key, new ArrayList<MajorDataEntry>());

		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

			InputStreamReader inputStreamReader = new InputStreamReader(
					conn.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line = null;

			if ((line = bufferedReader.readLine()) != null)
				stringBuffer.append(line);

			bufferedReader.close();
			inputStreamReader.close();

			JSONArray jsonArray = new JSONArray(stringBuffer.toString());
			ArrayList<MajorDataEntry> dataEntries = (ArrayList<MajorDataEntry>) majorsDataMap
					.get(key);

			for (int n = 0; n < jsonArray.length(); n++) {

				JSONObject jsonObject = jsonArray.getJSONObject(n);
				dataEntries.add(new MajorDataEntry(jsonObject
						.getLong("major_id"), jsonObject
						.getString("major_name")));

			}

		}

		conn.disconnect();

	}

	private void subjectsDataDownLoad(long majorId)
			throws MalformedURLException, IOException, JSONException {

		HttpURLConnection conn = (HttpURLConnection) new URL(hostAddress
				+ "studyclient/subjects?major_id=" + majorId).openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		conn.setRequestProperty(
				"Authorization",
				"Basic "
						+ Base64.encodeToString(authorization.getBytes(),
								Base64.NO_WRAP));
		conn.connect();

		if (subjectsDataMap.get(majorId) == null)
			subjectsDataMap.put(majorId, new ArrayList<SubjectDataEntry>());

		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

			InputStreamReader inputStreamReader = new InputStreamReader(
					conn.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line = null;

			if ((line = bufferedReader.readLine()) != null)
				stringBuffer.append(line);

			bufferedReader.close();
			inputStreamReader.close();

			JSONArray jsonArray = new JSONArray(stringBuffer.toString());
			ArrayList<SubjectDataEntry> dataEntries = (ArrayList<SubjectDataEntry>) subjectsDataMap
					.get(majorId);

			for (int n = 0; n < jsonArray.length(); n++) {

				JSONObject jsonObject = jsonArray.getJSONObject(n);
				dataEntries.add(new SubjectDataEntry(jsonObject
						.getLong("subject_id"), jsonObject
						.getString("subject_name")));

			}

		}

		conn.disconnect();

	}

	private void studyTitleListDataDownLoad(long subjectId)
			throws MalformedURLException, IOException, JSONException {

		HttpURLConnection conn = (HttpURLConnection) new URL(hostAddress
				+ "studyclient/study_title_list_data?subject_id=" + subjectId)
				.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		conn.setRequestProperty(
				"Authorization",
				"Basic "
						+ Base64.encodeToString(authorization.getBytes(),
								Base64.NO_WRAP));
		conn.connect();

		if (studyTitleListDataMap.get(subjectId) == null)
			studyTitleListDataMap.put(subjectId,
					new ArrayList<StudyTitleListDataEntry>());

		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

			InputStreamReader inputStreamReader = new InputStreamReader(
					conn.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line = null;

			if ((line = bufferedReader.readLine()) != null)
				stringBuffer.append(line);

			bufferedReader.close();
			inputStreamReader.close();

			JSONArray jsonArray = new JSONArray(stringBuffer.toString());
			ArrayList<StudyTitleListDataEntry> dataEntries = (ArrayList<StudyTitleListDataEntry>) studyTitleListDataMap
					.get(subjectId);

			for (int n = 0; n < jsonArray.length(); n++) {

				JSONObject jsonObject = jsonArray.getJSONObject(n);
				dataEntries.add(new StudyTitleListDataEntry(jsonObject
						.getLong("data_id"), jsonObject.getString("title"),
						jsonObject.getString("content"), jsonObject
								.getString("visitor"), jsonObject
								.getString("time"), jsonObject
								.getInt("recommend_star"), jsonObject
								.getString("web_url")));

			}

		}

		conn.disconnect();

	}

	@Override
	public void run() {

		new Thread(new NewsTitleLisDataDownLoadTask()).start();
		new Thread(new StudyTitleListDataDownLoadTask()).start();

		Looper.prepare();
		handler = new DataTransferHandler();
		Looper.loop();

	}

}
