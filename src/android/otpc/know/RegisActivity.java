package android.otpc.know;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisActivity extends Activity {
	EditText username,pwd,pwd_con,email;
	Button confirmBtn,cancelBtn;
	USER user;
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_regis);
	        iniView();  
	        iniController();
	 }
	 private void iniView(){
		 	username= (EditText)findViewById(R.id.user_edit);
			pwd		= (EditText)findViewById(R.id.pwd_edit);
			pwd_con	= (EditText)findViewById(R.id.confirm_pwd_edit);
			email	= (EditText)findViewById(R.id.email_edit);
			confirmBtn = (Button)findViewById(R.id.confirm_btn);
			cancelBtn = (Button)findViewById(R.id.cancel_btn);
			user = new USER();
	 }
	 private void iniController(){
	        confirmBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					user.setUser(username.getText().toString().trim());
					user.setPwd(pwd.getText().toString().trim());
					user.setEmail(email.getText().toString().trim());
					Date dNow = new Date( );
				    SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
				    user.setTime(ft.format(dNow).toString());
				    if(pwd.getText().toString().trim().equals(pwd_con.getText().toString().trim())){
				    	new RegisterTask().execute("");
				    }
				    else
				    	Toast.makeText(getApplicationContext(), "Password does not match"+pwd.getText().toString().trim()+"AND"+pwd_con.getText().toString().trim(),
				                Toast.LENGTH_LONG).show();
				}
			});
	        cancelBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
	 }
	 public static String convertStreamToString(java.io.InputStream is) {
		    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		    return s.hasNext() ? s.next() : "";
		}
	 @SuppressWarnings("unused")
	private byte[] encrypPwd(char[] pwd) throws 
	 NoSuchAlgorithmException, 
	 InvalidKeySpecException, 
	 NoSuchPaddingException, 
	 InvalidKeyException, 
	 InvalidParameterSpecException, 
	 IllegalBlockSizeException, 
	 BadPaddingException, 
	 UnsupportedEncodingException{
		 /* Derive the key, given password and salt. */
		 byte[] salt = null;
		 SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		 KeySpec spec = new PBEKeySpec(pwd, salt, 65536, 256);
		 SecretKey tmp = factory.generateSecret(spec);
		 SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
		 /* Encrypt the message. */
		 Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		 cipher.init(Cipher.ENCRYPT_MODE, secret);
		 AlgorithmParameters params = cipher.getParameters();
		 byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		 byte[] ciphertext = cipher.doFinal("Hello, World!".getBytes("UTF-8"));
		 return ciphertext;
	 }
	 @SuppressWarnings("unused")
	private String decrypPwd(char[] pwd) throws 
	InvalidKeyException, 
	InvalidAlgorithmParameterException, 
	NoSuchAlgorithmException, 
	NoSuchPaddingException, 
	UnsupportedEncodingException, 
	IllegalBlockSizeException, 
	BadPaddingException{
		 Key secret = null;
		 byte[] iv = null,ciphertext = null;
		 /* Decrypt the message, given derived key and initialization vector. */
		 Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
		String plaintext = new String(cipher.doFinal(ciphertext), "UTF-8");
		 System.out.println(plaintext);
		return plaintext;
	 }
	 class RegisterTask extends AsyncTask<String, Void, USER> {
		 String result =null;
		@Override
		protected USER doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			int TIMEOUT_MILLISEC = 3600;
			try {
		        JSONObject json = new JSONObject();
		        json.put("UpdateTime", user.getTime());
		        json.put("UserName", user.getUser());
		        json.put("Password", user.getPwd());
		        json.put("Email", user.getEmail());
		        json.put("LastLogIn",  user.getTime());
		        HttpParams httpParams = new BasicHttpParams();
		        HttpConnectionParams.setConnectionTimeout(httpParams,
		                TIMEOUT_MILLISEC);
		        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
		        HttpClient client = new DefaultHttpClient(httpParams);
		        String url = "http://knowideas.atwebpages.com/regis.php";
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
		 protected void onPostExecute(USER user) {
		        // TODO: check this.exception 
		        // TODO: do something with the feed
			 if(result.equalsIgnoreCase("true")){
				 Toast.makeText(getApplicationContext(), "Sign up successfully!",
			                Toast.LENGTH_SHORT).show();
				 finish();
			 }
			 else
			 {
				 Toast.makeText(getApplicationContext(), "Error occur: "+result,
			                Toast.LENGTH_SHORT).show();
			 }
		    }
	 }
	 class USER {
		String username,pwd,email,time;
		USER(){
		}
		public void setUser(String user){
			this.username = user;
		}
		public String getUser(){
			return this.username;
		}
		public void setPwd(String pwd){
			this.pwd = pwd;
		}
		public String getPwd(){
			return this.pwd;
		}
		public void setEmail(String email){
			this.email = email;
		}
		public String getEmail(){
			return this.email;
		}
		public void setTime(String time){
			this.time = time;
		}
		public String getTime(){
			return this.time;
		}
	 }
}
