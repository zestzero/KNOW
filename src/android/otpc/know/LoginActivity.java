package android.otpc.know;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.otpc.know.utils.WebServiceClient;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private Button regis_btn, login_btn;
	private EditText username, password;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);
		
		initView();
		initController();
	}

	private void initView() {
		context = this.getApplicationContext();
		regis_btn = (Button) findViewById(R.id.signup_btn);
		login_btn = (Button) findViewById(R.id.login_btn);
		username = (EditText) findViewById(R.id.user_edit);
		password = (EditText) findViewById(R.id.pwd_edit);
	}

	private void initController() {
		regis_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, RegisActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
		
		login_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String user = username.getText().toString().trim();
				String pwd = password.getText().toString().trim();
				if (user != null && pwd != null && user.length() > 0 && pwd.length() > 0) {
					new LoginTask().execute(new String[] { user, pwd });
				} else {
					Toast.makeText(context, "กรุณาใส่ username/password ให้ครบถ้วน", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	class LoginTask extends AsyncTask<String, Void, JSONObject> {
		private String user, pwd;
		private ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(LoginActivity.this, null, "signing in", true, false);
		}

		@Override
		protected JSONObject doInBackground(String... input) {
			user = input[0];
			pwd = input[1];
			
			JSONObject requestJson = new JSONObject();
			try {
				requestJson.put("username", user);
				requestJson.put("password", pwd);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			JSONObject responseJson = WebServiceClient.getResponse("login2.php", requestJson);
			
			return responseJson;
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
			if(result == null){
				Toast.makeText(getApplicationContext(), "Webservice error", Toast.LENGTH_LONG).show();
				return;
			}
			
			try {
				if (result.getString("result").equalsIgnoreCase("true")) {
					Toast.makeText(getApplicationContext(), "Sign in successfully!", Toast.LENGTH_LONG).show();
					
					Intent intent = new Intent(context, NewsFeedActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("username", username.getText().toString());
					intent.putExtra("userid", result.getString("userId"));
					context.startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "Username/Password incorrect", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "Webservice error", Toast.LENGTH_LONG).show();
			}
			
			progressDialog.dismiss();
		}

	}
}
