package com.tectalk.tectalk_customer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.google.android.gcm.GCMRegistrar;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MainActivity extends ActionBarActivity {

	EditText putId;
	EditText putPasswd;
	Button btnJoin;
	Button btnLogin;
	CheckBox checkAutoLogin;

	String cusId;
	String cusPasswd;
	Toast toast;

	String url = "http://182.162.90.100/TecTalk/LoginCus";
	boolean result = false;
	static boolean checked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences pref = getSharedPreferences("pref",
				Activity.MODE_PRIVATE);
		Log.d("pref","boolean값은 "+checked);

		if (checked) {
			cusId = pref.getString("CUSID", cusId);
			Log.d("autologin","자동 로그인 실행 "+cusId);
			Intent intent = new Intent(getApplicationContext(),
					CustomerActivity.class);
			intent.putExtra("CUSID", cusId);
			startActivity(intent);
			finish();
		} else {
			setContentView(R.layout.activity_main);

			putId = (EditText) findViewById(R.id.putId);
			putPasswd = (EditText) findViewById(R.id.putPasswd);
			btnJoin = (Button) findViewById(R.id.btnJoin);
			btnLogin = (Button) findViewById(R.id.btnLogin);
			checkAutoLogin = (CheckBox) findViewById(R.id.CheckAutoLogin);
			checkAutoLogin.setChecked(checked);
			checkAutoLogin.setOnCheckedChangeListener(onCheckChanged);

			OnClickListener clickJoin = new OnClickListener() {
				public void onClick(View v) {
					Intent intentJoin = new Intent(getApplicationContext(),
							JoinActivity.class);
					startActivity(intentJoin);
				}
			};

			OnClickListener clickLogin = new OnClickListener() {
				public void onClick(View v) {
					getCusInfo();

				}
			};
			btnJoin.setOnClickListener(clickJoin);
			btnLogin.setOnClickListener(clickLogin);
		}

	}

	public OnCheckedChangeListener onCheckChanged = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {

				toast = Toast.makeText(getApplicationContext(),
						"자동 로그인 기능을 사용합니다.", Toast.LENGTH_SHORT);
				toast.show();

				checked = isChecked;

			} else { // 푸쉬 받지않기

				toast = Toast.makeText(getApplicationContext(),
						"자동 로그인 기능을 사용하지 않습니다.", Toast.LENGTH_SHORT);
				toast.show();

				checked = isChecked;

			}

		}

	};

	private void getCusInfo() {
		if (putId.getText().toString().length() != 0) {
			if (putPasswd.getText().toString().length() != 0) {
				cusId = putId.getText().toString();
				cusPasswd = putPasswd.getText().toString();

				new ConnectServer().execute(null, null, null);
			} else {
				toast = Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요",
						Toast.LENGTH_SHORT);
				toast.show();
			}
		} else {
			toast = Toast.makeText(getApplicationContext(), "아이디를 입력하세요",
					Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private class ConnectServer extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			HttpClient client = new DefaultHttpClient();
			List<NameValuePair> values = new ArrayList<NameValuePair>();
			values.add(new BasicNameValuePair("CUSID", cusId));
			values.add(new BasicNameValuePair("CUSPW", cusPasswd));

			HttpParams param = client.getParams();
			HttpConnectionParams.setConnectionTimeout(param, 5000);
			HttpConnectionParams.setSoTimeout(param, 5000);

			try {
				url = url + "?" + URLEncodedUtils.format(values, "UTF-8");
				HttpGet httpGet = new HttpGet(url);

				HttpResponse response = client.execute(httpGet);
				String _result = EntityUtils.toString(response.getEntity());
				Log.d("aaa", "result : " + _result);
				if (_result.contains("success")) {
					result = true;
				} else
					result = false;

			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void res) {
			super.onPostExecute(res);
			if (result) {
				Intent intent = new Intent(getApplicationContext(),
						CustomerActivity.class);
				intent.putExtra("CUSID", cusId);
				SharedPreferences pref = getSharedPreferences("pref",
						Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("CUSID", cusId);
				editor.putString("CUSPASSWD", cusPasswd);
				editor.commit();
				startActivity(intent);
				finish();
			} else {
				toast = Toast.makeText(getApplicationContext(), "로그인 실패",
						Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
