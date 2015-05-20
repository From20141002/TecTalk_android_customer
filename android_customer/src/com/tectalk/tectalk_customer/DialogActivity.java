package com.tectalk.tectalk_customer;

import java.util.ArrayList;
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

import com.tectalk.tectalk_customer.R;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DialogActivity extends Activity {

TextView txtMsg;
	
	Button btnO;
	Button btnX;
	
	Intent intent;
	
	String msg;
	String phoneDri;
	String url = "http://182.162.90.100/TecTalk/Getgetbyhand";
	
	Toast toast;
	
	String getByHand = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dialog);
		
		intent =getIntent();
		msg = intent.getExtras().getString("msg");
		phoneDri = intent.getExtras().getString("phoneDri");
		Log.d("intent",intent.getExtras().getString("msg"));

		txtMsg = (TextView)findViewById(R.id.txtMsg);
		btnO = (Button)findViewById(R.id.btnO);
		btnX = (Button)findViewById(R.id.btnX);
		txtMsg.setText(msg);
		
		btnO.setOnClickListener(onClickListenerO);
		btnX.setOnClickListener(onClickListenerX);
		
		
		
	}
	
	private OnClickListener onClickListenerO = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			getByHand = "1";
			new ConnectServer().execute(null,null,null);
			NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(1);
			toast = Toast.makeText(getApplicationContext(),"상품을 직접 수령합니다.",Toast.LENGTH_SHORT);
			toast.show();
			finish();
			
			
			
		}
		
	};
	
	private OnClickListener onClickListenerX = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			getByHand = "0";
			new ConnectServer().execute(null,null,null);
			NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(1);
			toast = Toast.makeText(getApplicationContext(),"상품을 대리 수령합니다.",Toast.LENGTH_SHORT);
			toast.show();
			finish();
			
		}
		
	};
	
	
	private class ConnectServer extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			HttpClient client = new DefaultHttpClient();
			List<NameValuePair> values = new ArrayList<NameValuePair>();
			values.add(new BasicNameValuePair("cusId", "ysjleader"));
			values.add(new BasicNameValuePair("getbyhand", getByHand));
			values.add(new BasicNameValuePair("phoneDri", phoneDri));
			values.add(new BasicNameValuePair("item_info", "OBJECT"));

			HttpParams param = client.getParams();
			HttpConnectionParams.setConnectionTimeout(param, 5000);
			HttpConnectionParams.setSoTimeout(param, 5000);

			try {

		//		url = url + "?" + URLEncodedUtils.format(values, "UTF-8");
				//HttpGet httpGet = new HttpGet(url);

				//HttpResponse response = client.execute(httpGet);
				HttpPost httpPost = new HttpPost(url);
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values, "UTF-8");
				httpPost.setEntity(entity);
				HttpResponse response = client.execute(httpPost);
				String _result = EntityUtils.toString(response.getEntity());
				
				Log.d("aaa", " result : " + _result);
				Log.d("aaaaa","성공한듯???");
		

			} catch (Exception e) {
				Log.d("aaa", "error : " + e.toString());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result_) {
			// TODO Auto-generated method stub
			super.onPostExecute(result_);
			

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dialog, menu);
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