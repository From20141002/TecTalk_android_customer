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

	private String cus_id;
	private String item_info;
	private String url_get = "http://182.162.90.100/TecTalk/GetDriInfo";
	private String url_set = "http://182.162.90.100/TecTalk/SetDriInfo";
	private String result_get;
	private String result_set;
	private String text = "";
	private String text_set = "";
	
	private TextView txtViewResult;

	private JSONArray jArray_get;
	private JSONObject jObject_get;
	private JSONArray jArray_set;
	private JSONObject jObject_set;

	private ListView listViewResult;
	private ArrayAdapter<String> itemAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);

		itemAdapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.activity_customer2);

		listViewResult = (ListView) findViewById(R.id.listViewResult);

		listViewResult.setAdapter(itemAdapter);

		intent = getIntent();
		cus_id = intent.getExtras().getString("cus_id");
		item_info = intent.getExtras().getString("item_info");

		new ConnectServer().execute(null, null, null);
	}

	private class ConnectServer extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			HttpClient client = new DefaultHttpClient();
			List<NameValuePair> values_get = new ArrayList<NameValuePair>();
			List<NameValuePair> values_set = new ArrayList<NameValuePair>();
			values_get.add(new BasicNameValuePair("CUSTOMER_ID", cus_id));
			values_get.add(new BasicNameValuePair("ITEM_INFO", item_info));

			HttpParams param = client.getParams();
			HttpConnectionParams.setConnectionTimeout(param, 5000);
			HttpConnectionParams.setSoTimeout(param, 5000);

			try {

				url_get = url_get + "?"
						+ URLEncodedUtils.format(values_get, "UTF-8");
				HttpGet httpGet = new HttpGet(url_get);
				HttpGet httpSet = new HttpGet(url_set);
				HttpResponse response_get = client.execute(httpGet);

				result_get = EntityUtils.toString(response_get.getEntity());
				// JSONObject jObject = new JSONObject(result);
				jArray_get = new JSONArray(result_get);
				jObject_get = new JSONObject();
				for (int i = 0; i < jArray_get.length(); i++) {
					jObject_get = jArray_get.getJSONObject(i);
					text = jObject_get.getString("dri_info");
					Log.d("aaa", "text : " + text);
				}
				
				Log.d("aaa", "cus result : " + result_get);
				
				values_set.add(new BasicNameValuePair("DRIVER_INFO", text));

				url_set = url_set + "?"
						+ URLEncodedUtils.format(values_set, "UTF-8");

				HttpResponse response_set = client.execute(httpSet);
				
				result_set = EntityUtils.toString(response_set.getEntity());
				jArray_set = new JSONArray(result_set);
				jObject_set = new JSONObject();
				
				for (int i = 0; i < jArray_set.length(); i++) {
					jObject_set = jArray_set.getJSONObject(i);
					text_set += jObject_set.getString("dri_id");
					text_set += jObject_set.getString("dri_name");
					text_set += jObject_set.getString("dri_phone");
					text_set += jObject_set.getString("dri_company");
					Log.d("aaa", "text : " + text_set);
				}
				

				Log.d("aaa", "text : " + text);

				Log.d("aaa", "text : " + text);
			} catch (Exception e) {
				Log.d("aaa", "error : " + e.toString());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result_) {
			// TODO Auto-generated method stub
			super.onPostExecute(result_);
			txtViewResult.setText(text_set);

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
