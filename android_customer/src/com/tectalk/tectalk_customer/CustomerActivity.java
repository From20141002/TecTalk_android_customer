package com.tectalk.tectalk_customer;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tectalk.tectalk_customer.R;
import com.google.android.gcm.GCMRegistrar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

public class CustomerActivity extends ActionBarActivity {

	public static Context mContext = null;
	public CheckBox cb_setting = null;
	public static String PROJECT_ID = "619658958148";
	public Toast toast;

	private Intent intent;
	public static String cusId = "";
	private String url = "http://182.162.90.100/TecTalk/GetItemInfo";
//	private String url2 = "http://182.162.90.100/TecTalk/SaveCusGCM";
	private String result;
	private String text = "";
	private String item_info = "";
	private static String phoneId = "";

	private JSONArray jArray;
	private JSONObject jObject;
	
	//private boolean resultResist = false;

	private ArrayList<String> arrayList = new ArrayList<String>();

	private ListView listViewResult;
	private ArrayAdapter<String> itemAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer);
		mContext = this;

		itemAdapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.activity_customer2);

		listViewResult = (ListView) findViewById(R.id.listViewResult);

		listViewResult.setAdapter(itemAdapter);

		listViewResult.setOnItemClickListener(onClickListItem);

		intent = getIntent();
		cusId = intent.getExtras().getString("cus_id");
		cb_setting = (CheckBox) findViewById(R.id.checkBox);
		cb_setting.setChecked(!GCMRegistrar.getRegistrationId(mContext).equals(
				""));

		cb_setting.setOnCheckedChangeListener(onCheckChanged);

		new ConnectServer().execute(null, null, null);

	}

	public OnCheckedChangeListener onCheckChanged = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {

				Log.d("test", "푸쉬 메시지를 받습니다.");

				GCMRegistrar.checkDevice(mContext);
				GCMRegistrar.checkManifest(mContext);

				if (GCMRegistrar.getRegistrationId(mContext).equals("")) {
					GCMRegistrar.register(mContext, PROJECT_ID);
					phoneId = GCMRegistrar.getRegistrationId(mContext);
					Log.d("test","왜이럴까"+phoneId);
					//new ConnectServer2().execute(null,null,null);
				}Log.d("test","값이 널이니?"+phoneId);

			}

			// 푸쉬 받지않기

			else {

				toast = Toast.makeText(getApplicationContext(),
						"푸쉬 메시지를 받지 않습니다.", Toast.LENGTH_SHORT);
				toast.show();
				GCMRegistrar.unregister(mContext);

			}

		}

	};

	private OnItemClickListener onClickListItem = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Intent intent_item = new Intent(getApplicationContext(),
					MoreActivity.class);

			intent_item.putExtra("dri_id", arrayList.get(position));
			startActivity(intent_item);

		}

	};

	private class ConnectServer extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			HttpClient client = new DefaultHttpClient();
			List<NameValuePair> values = new ArrayList<NameValuePair>();
			values.add(new BasicNameValuePair("CUSTOMER_ID", cusId));

			HttpParams param = client.getParams();
			HttpConnectionParams.setConnectionTimeout(param, 5000);
			HttpConnectionParams.setSoTimeout(param, 5000);

			try {

				url = url + "?" + URLEncodedUtils.format(values, "UTF-8");
				HttpGet httpGet = new HttpGet(url);

				HttpResponse response = client.execute(httpGet);
				result = EntityUtils.toString(response.getEntity());

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

			try {
				// JSONObject jObject = new JSONObject(result);
				jArray = new JSONArray(result);
				jObject = new JSONObject();
				Log.d("aaa", "cus result : " + result);
				for (int i = 0; i < jArray.length(); i++) {
					jObject = jArray.getJSONObject(i);
					text = jObject.getString("item_info");
					itemAdapter.add(text);
					arrayList.add(jObject.getString("dri_id"));
					Log.d("aaa", "text : " + text + i);
				}
			} catch (Exception e) {
			}

		}

	}
	
	/*private class ConnectServer2 extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			
			HttpClient client = new DefaultHttpClient();
			List<NameValuePair> values = new ArrayList<NameValuePair>();
			values.add(new BasicNameValuePair("CUS_ID", cusId));
			values.add(new BasicNameValuePair("PHONE_ID", phoneId));

			
			HttpParams param = client.getParams();
			HttpConnectionParams.setConnectionTimeout(param, 5000);
			HttpConnectionParams.setSoTimeout(param, 5000);
			
			
			try {
				
				URI uri = new URI(url2);
				HttpPost httpPost = new HttpPost(uri);
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values, "UTF-8");
				httpPost.setEntity(entity);
				HttpResponse response = client.execute(httpPost);
				String _result = EntityUtils.toString(response.getEntity());
				
				Log.d("aaa", " result : " + _result);
				if(_result.contains("success")){
					resultResist = true;
				} else resultResist = false;
			} catch(Exception e){
				Log.d("aaa","error : " + e.toString());
				
			} return null;
		}
		
		@Override
		protected void onPostExecute(Void res){
			super.onPostExecute(res);
			
			if(resultResist){
				
				toast = Toast.makeText(getApplicationContext(), "Id 등록 성공", Toast.LENGTH_SHORT);
				toast.show();
				finish();
			} else{
				toast = Toast.makeText(getApplicationContext(), "아이디가 이미 있습니다.", Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		
		
	}*/

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