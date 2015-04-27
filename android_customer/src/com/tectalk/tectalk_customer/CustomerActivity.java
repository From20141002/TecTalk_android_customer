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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class CustomerActivity extends ActionBarActivity {

	private Intent intent;
	private String cus_id;
	private String url = "http://182.162.90.100/TecTalk/GetItemInfo";
	
	private JSONArray jArray;
	private JSONObject jObject;
	
	private String text="";
	private TextView txtviewResult;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer);
		
		txtviewResult = (TextView)findViewById(R.id.txtviewResult);
		
		intent = getIntent();
		cus_id = intent.getExtras().getString("cus_id");
		
		new ConnectServer().execute(null,null,null);
		
		txtviewResult.setText(text);
		
	}
	
	private class ConnectServer extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
		
			HttpClient client = new DefaultHttpClient();
			List<NameValuePair> values = new ArrayList<NameValuePair>();
			values.add(new BasicNameValuePair("CUSTOMER_ID", cus_id));
			
			HttpParams param = client.getParams();
			HttpConnectionParams.setConnectionTimeout(param, 5000);
			HttpConnectionParams.setSoTimeout(param, 5000);
			
			try{
				
				url = url + "?" + URLEncodedUtils.format(values,"UTF-8");
				HttpGet httpGet = new HttpGet(url);
				
				HttpResponse response = client.execute(httpGet);
				String result = EntityUtils.toString(response.getEntity());
				
				//JSONObject jObject = new JSONObject(result);
				jArray = new JSONArray(result);
				jObject = new JSONObject();
				Log.d("aaa", "cus result : " + result);
				for(int i=0; i<jArray.length(); i++){
					jObject = jArray.getJSONObject(i);
					text += jObject.getString("dri_id")+"\n";
					text += jObject.getString("cus_id")+"\n";
					text += jObject.getString("item_info")+"\n";
					text += jObject.getString("item_address")+"\n";
					text += jObject.getString("item_getbyhand")+"\n";
					text += "--------------------------------\n";
				}
						
				Log.d("aaa", "text : " + text);
			}catch(Exception e){
				Log.d("aaa","error : " + e.toString());
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			txtviewResult.setText(text);
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