package com.tectalk.tectalk_customer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
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

import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class JoinActivity extends ActionBarActivity {
	
	
	Button btnSave;
	EditText cusId;
	EditText cusName;
	EditText cusPhone;
	EditText cusPasswd;
	String url = new String("http://182.162.90.100/TecTalk/CusInfoSave.jsp");
	boolean result = false;
	
	String cus_id;
	String cus_name;
	String cus_phone;
	String cus_passwd;
	
	Toast toast;
	OnClickListener clickSave;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
		
		btnSave = (Button) findViewById(R.id.btnSave);
		cusId = (EditText)findViewById(R.id.cusId);
		cusName = (EditText)findViewById(R.id.cusName);
		cusPhone =(EditText)findViewById(R.id.cusPhone);
		cusPasswd = (EditText)findViewById(R.id.cusPasswd);
		
		
		clickSave = new OnClickListener() {
			public void onClick(View v) {
				SaveInfo();				

			}
		};
		
		btnSave.setOnClickListener(clickSave);
		
	}
	
	private void SaveInfo(){
		
		Log.d("aaa", cusId.getText().toString());
		Log.d("aaa", cusName.getText().toString());
		Log.d("aaa", cusPhone.getText().toString());
		Log.d("aaa", cusPasswd.getText().toString());
		
		if(cusId.getText().toString().length() != 0){
			if(cusName.getText().toString().length() !=0){
				if(cusPhone.getText().toString().length() !=0){
					if(cusPasswd.getText().toString().length() != 0){
						cus_id = cusId.getText().toString();
						cus_name = cusName.getText().toString();
						cus_phone = cusPhone.getText().toString();
						cus_passwd = cusPasswd.getText().toString();
						new ConnectServer().execute(null,null,null);
					} else {
						toast = Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT);
					    toast.show();
					}
				} else{
					toast = Toast.makeText(getApplicationContext(), "휴대폰 번호를 입력하세요", Toast.LENGTH_SHORT);
					toast.show();
				}
			} else{
				toast = Toast.makeText(getApplicationContext(), "이름을 입력하세요", Toast.LENGTH_SHORT);
				toast.show();
			}
			
		} else{
			toast = Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	private class ConnectServer extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			
			HttpClient client = new DefaultHttpClient();
			List<NameValuePair> values = new ArrayList<NameValuePair>();
			values.add(new BasicNameValuePair("CUSID", cus_id));
			values.add(new BasicNameValuePair("CUSNAME", cus_name));
			values.add(new BasicNameValuePair("CUSPHONE", cus_phone));
			values.add(new BasicNameValuePair("CUSPW", cus_passwd));
			
			HttpParams param = client.getParams();
			HttpConnectionParams.setConnectionTimeout(param, 5000);
			HttpConnectionParams.setSoTimeout(param, 5000);
			
			
			try {
				
				URI uri = new URI(url);
				HttpPost httpPost = new HttpPost(uri);
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values, "UTF-8");
				httpPost.setEntity(entity);
				HttpResponse response = client.execute(httpPost);
				String _result = EntityUtils.toString(response.getEntity());
				
				Log.d("aaa", " result : " + _result);
				if(_result.contains("success")){
					result = true;
				} else result = false;
			} catch(Exception e){
				Log.d("aaa","error : " + e.toString());
				
			} return null;
		}
		
		@Override
		protected void onPostExecute(Void res){
			super.onPostExecute(res);
			
			if(result){
				
				toast = Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT);
				toast.show();
				finish();
			} else{
				toast = Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.join, menu);
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

	/*class loadJsp extends AsyncTask<Void, String, Void> {

		@SuppressWarnings("deprecation")
		@Override
		protected Void doInBackground(Void... params) {
			try {
				URL url = new URL("http://182.162.90.100/CusInfoSave.jsp");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.
				
				

				
			} catch (IOException e) {
			}
			return null;
		}

	} */

}
