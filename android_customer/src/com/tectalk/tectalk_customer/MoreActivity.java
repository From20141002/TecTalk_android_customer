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
import org.json.JSONArray;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MoreActivity extends ActionBarActivity {
	private Intent intent;

	private String dri_id;
	private String url_set = "http://182.162.90.100/TecTalk/SetDriInfo";
	private String result_set;
	private String text = "";
	private String text_set = "";

	private TextView txtviewResult;

	private JSONArray jArray_set;
	private JSONObject jObject_set;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		txtviewResult = (TextView) findViewById(R.id.txtviewResult);

		intent = getIntent();
		dri_id = intent.getExtras().getString("dri_id");
		Log.d("aaaa", "dri_id = " + dri_id);

		new ConnectServer().execute(null, null, null);
	}

	private class ConnectServer extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			HttpClient client = new DefaultHttpClient();
			List<NameValuePair> values_set = new ArrayList<NameValuePair>();
			values_set.add(new BasicNameValuePair("DRIVER_ID", dri_id));

			HttpParams param = client.getParams();
			HttpConnectionParams.setConnectionTimeout(param, 5000);
			HttpConnectionParams.setSoTimeout(param, 5000);

			try {
				url_set = url_set + "?"
						+ URLEncodedUtils.format(values_set, "UTF-8");
				HttpGet httpSet = new HttpGet(url_set);
				HttpResponse response_set = client.execute(httpSet);

				result_set = EntityUtils.toString(response_set.getEntity());
				jObject_set = new JSONObject(result_set);

				text_set += jObject_set.getString("dri_id");
				text_set += jObject_set.getString("dri_name");
				text_set += jObject_set.getString("dri_phone");
				text_set += jObject_set.getString("dri_company");
				Log.d("aaaa", "text : " + text_set);


			} catch (Exception e) {
				Log.d("aaa", "error : " + e.toString());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result_) {
			// TODO Auto-generated method stub
			super.onPostExecute(result_);
			txtviewResult.setText(text_set);

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
