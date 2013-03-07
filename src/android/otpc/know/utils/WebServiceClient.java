package android.otpc.know.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WebServiceClient {
	
	private static final String url = "http://knowideas.atwebpages.com/";
	
	public static JSONObject getResponse(String webservice, JSONObject requestJson){
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url+webservice);
		String result = null;
		try {
			request.setEntity(new ByteArrayEntity(requestJson.toString().getBytes("UTF8")));
			request.setHeader("json", requestJson.toString());
			
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				InputStream instream = entity.getContent();
				result = URLDecoder.decode(convertStreamToString(instream), "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return JSONParse(result);
	}
	
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		
		String line = null;
		try {
			line = reader.readLine();
			while(line != null) {
				sb.append(line);
				line = reader.readLine();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public static JSONObject JSONParse(String res) {
		if(!res.startsWith("{"))
			res = res.substring(1).trim();
		try {
			JSONObject json = new JSONObject(res);
			Log.i("WebServiceClient", "JSON Parsed.");
			return json;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}