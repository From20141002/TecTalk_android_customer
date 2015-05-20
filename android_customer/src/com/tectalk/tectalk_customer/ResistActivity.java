
package com.tectalk.tectalk_customer;

import java.net.URI;
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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResistActivity extends ActionBarActivity {
	public Button btnIdSend;
	public EditText editId;
	
	public String url = new String("http://182.162.90.100/TecTalk/SaveCusGCM");
	boolean result = false;
	
	public String cusId;
	public String phoneId;
	
	public Toast toast;
	public OnClickListener clickIdSave;
	public Intent intent;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resist);
		
		btnIdSend = (Button) findViewById(R.id.btnIdSend);
		editId = (EditText)findViewById(R.id.editId);
		intent = getIntent();
		phoneId = intent.getExtras().getString("phone_Id");
		
		
		clickIdSave = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SaveInfo();				

			}
			
		};
		
		btnIdSend.setOnClickListener(clickIdSave);
		
	}
	
	private void SaveInfo(){
		
		Log.d("aaa", editId.getText().toString());
		Log.d("aaa", phoneId);

		
		if(editId.getText().toString().length() != 0){
				
						cusId = editId.getText().toString();
	
						new ConnectServer().execute(null,null,null);

				} else{
					toast = Toast.makeText(getApplicationContext(), "사용자 Id를 입력하세요.", Toast.LENGTH_SHORT);
					toast.show();
				}
		}
	

	
	private class ConnectServer extends AsyncTask<Void,Void,Void>{

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
				
				toast = Toast.makeText(getApplicationContext(), "Id 등록 성공", Toast.LENGTH_SHORT);
				toast.show();
				finish();
			} else{
				toast = Toast.makeText(getApplicationContext(), "아이디가 이미 있습니다.", Toast.LENGTH_SHORT);
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

