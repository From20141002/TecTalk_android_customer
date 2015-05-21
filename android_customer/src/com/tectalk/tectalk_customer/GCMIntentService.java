package com.tectalk.tectalk_customer;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gcm.GCMBaseIntentService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class GCMIntentService extends GCMBaseIntentService {
	JSONArray jArray;
	JSONObject jObject;
	Toast toast;

	Intent intentNoti;
	private String url = "http://182.162.90.100/TecTalk/SaveCusGCM";
	static String msg = "";
	static String phoneDri = "";
	static String cusId = "";
	static String phoneCus = "";
	static String itemInfo = "";

	private boolean resultResist = false;
	static private String error = "";

	@Override
	protected void onError(Context arg0, String arg1) {
		Log.d("test", "error");
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMessage(Context arg0, Intent intent) {
		Log.d("test", "메시지왔당~");
		Log.d("test", intent.getExtras().getString("test"));
		CustomerActivity customerAcivity = new CustomerActivity();

		try {
			jObject = new JSONObject(intent.getExtras().getString("test"));
			intentNoti = new Intent(getApplicationContext(),
					DialogActivity.class);
			intentNoti.putExtra("test", jObject.toString());
			intentNoti.putExtra("CUSID", customerAcivity.cusId);
			PendingIntent pendingIntent = PendingIntent.getActivity(
					getApplicationContext(), 0, intentNoti,
					PendingIntent.FLAG_UPDATE_CURRENT);
			Notification notification = new Notification();
			notification.icon = R.drawable.ic_launcher;
			notification.when = new Date().getTime();
			notification.number = 10;
			notification.setLatestEventInfo(getApplicationContext(), "알림왔당~",
					"알림와써요!!!", pendingIntent);
			int notiId = 1;

			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(notiId, notification);
		} catch (Exception e) {
			Log.d("error", e.toString());
		}

		// TODO Auto-generated method stub

	}

	@Override
	protected void onRegistered(Context arg0, String phoneId) {
		// TODO Auto-generated method stub
		Log.d("test", "등록ID:" + phoneId);
		CustomerActivity customerActivity = new CustomerActivity();
		Log.d("test", "cusId 가져왔어?" + customerActivity.cusId);
		cusId = customerActivity.cusId;
		phoneCus = phoneId;

		new ConnectServer().execute(null, null, null);

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.d("test", "해지ID:" + arg1);
	}

	public GCMIntentService() {
		super("619658958148");
		Log.d("test", "GCM 서비스 생성자 실행");
	}

	private class ConnectServer extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			HttpClient client = new DefaultHttpClient();
			List<NameValuePair> values = new ArrayList<NameValuePair>();
			values.add(new BasicNameValuePair("CUSID", cusId));
			values.add(new BasicNameValuePair("PHONEID", phoneCus));

			HttpParams param = client.getParams();
			HttpConnectionParams.setConnectionTimeout(param, 5000);
			HttpConnectionParams.setSoTimeout(param, 5000);

			try {

				URI uri = new URI(url);
				HttpPost httpPost = new HttpPost(uri);
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values,
						"UTF-8");
				httpPost.setEntity(entity);
				HttpResponse response = client.execute(httpPost);
				String _result = EntityUtils.toString(response.getEntity());

				Log.d("aaa", " result : " + _result);
				if (_result.contains("success")) {
					resultResist = true;

				} else if (_result.contains("error")) {
					error = "error";

				} else {
					resultResist = false;
					error = "";

				}
			} catch (Exception e) {
				Log.d("aaa", "error : " + e.toString());

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void res) {
			super.onPostExecute(res);

			if (resultResist) {

				toast = Toast.makeText(getApplicationContext(), "Id 등록 성공",
						Toast.LENGTH_SHORT);
				toast.show();

			} else if (resultResist == false) {
				if (error.equals("")) {
					toast = Toast.makeText(getApplicationContext(),
							"아이디가 이미 있습니다.", Toast.LENGTH_SHORT);
					toast.show();
				} else if (error.contains("error")) {
					toast = Toast.makeText(getApplicationContext(),
							"오류가 발생하였습니다.", Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		}

	}
}
