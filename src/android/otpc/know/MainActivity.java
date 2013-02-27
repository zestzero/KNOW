package android.otpc.know;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button regis_btn,login_btn;
	EditText username;
	EditText password;
	Context context;
	 	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_signin);
	        initView();
	        initController();
	 }
	 	private void initView(){
	 		context  = this.getApplicationContext();
	 		regis_btn = (Button)findViewById(R.id.signup_btn);
	 		login_btn = (Button)findViewById(R.id.login_btn);
	 		username = (EditText)findViewById(R.id.user_edit);
	 		password = (EditText)findViewById(R.id.pwd_edit);
	 	}
	 	private void initController(){
	        regis_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				Intent intent = new Intent(context, RegisActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				}
			});
	        login_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String user = username.getText().toString().trim();
					String pwd = password.getText().toString().trim();
					if(user != "" && pwd != ""){
						new LoginTask().execute(new String[]{user,pwd});
					}
				}
			});
	 	}
	 	 public static String convertStreamToString(java.io.InputStream is) {
			    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
			    return s.hasNext() ? s.next() : "";
			}
	 	class LoginTask extends AsyncTask<String, Void, Void> {
	 		private String user,pwd,result,time;
			@Override
			protected Void doInBackground(String... input) {
				// TODO Auto-generated method stub
				user = input[0];
				pwd = input[1];
				Date dNow = new Date( );
			    SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
				time = ft.format(dNow).toString();
				int TIMEOUT_MILLISEC = 3600;
				try {
			        JSONObject json = new JSONObject();
			        json.put("UserName", user);
			        json.put("Password", pwd);
			        json.put("LastLogIn",  time);
			        HttpParams httpParams = new BasicHttpParams();
			        HttpConnectionParams.setConnectionTimeout(httpParams,
			                TIMEOUT_MILLISEC);
			        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			        HttpClient client = new DefaultHttpClient(httpParams);
			        String url = "http://knowideas.atwebpages.com/login.php";
			        HttpPost request = new HttpPost(url);
			        request.setEntity(new ByteArrayEntity(json.toString().getBytes(
			                "UTF8")));
			        request.setHeader("json", json.toString());
			        HttpResponse response = client.execute(request);
			        HttpEntity entity = response.getEntity();
			        // If the response does not enclose an entity, there is no need
			        if (entity != null) {
			            InputStream instream = entity.getContent();
			            result = convertStreamToString(instream);
			            Log.i("Read from server", result);
			        }
			    } catch (Throwable t) {
			    	  Log.i("Request failed: " ,t.toString());
			    	}
				return null;
			}
			 protected void onPostExecute() {
				 if(result.equalsIgnoreCase("true")){
					 Toast.makeText(getApplicationContext(), "Sign in successfully!",
				                Toast.LENGTH_SHORT).show();
					 	Intent intent = new Intent(context, UnitListActivity.class);
						//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
				 }
				 else
				 {
					 Toast.makeText(getApplicationContext(), "Username/Password incorrect",
				                Toast.LENGTH_SHORT).show();
				 }
			 }
	 		
	 	}
}
