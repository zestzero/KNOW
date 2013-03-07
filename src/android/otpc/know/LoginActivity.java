package android.otpc.know;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
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
	Button regis_btn, login_btn;
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
				if (user != "" && pwd != "") {
					new LoginTask().execute(new String[] { user, pwd });
				}
			}
		});
	}

	public static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	class LoginTask extends AsyncTask<String, Void, JSONObject> {
		private String user, pwd;

		@SuppressLint("SimpleDateFormat") @Override
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

		protected void onPostExecute(JSONObject result) {
			if(result == null){
				Toast.makeText(getApplicationContext(), "Webservice error", Toast.LENGTH_LONG).show();
				return;
			}
			
			try {
				if (result.getString("result").equalsIgnoreCase("true")) {
					Toast.makeText(getApplicationContext(), "Sign in successfully!", Toast.LENGTH_LONG).show();
//					Intent intent = new Intent(context, UnitListActivity.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					context.startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "Username/Password incorrect", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "Webservice error", Toast.LENGTH_LONG).show();
			}
		}

	}
}
