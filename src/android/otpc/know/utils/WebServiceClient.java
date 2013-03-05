package android.otpc.know.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.ListIterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WebServiceClient {
	
	private static final String url = "http://knowideas.atwebpages.com/";
	
	public static JSONObject getResponse(String webservice, JSONObject requestJson){
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url+webservice);
		
		try {
			request.setEntity(new ByteArrayEntity(requestJson.toString().getBytes("UTF8")));
			request.setHeader("json", requestJson.toString());
			
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = URLDecoder.decode(convertStreamToString(instream), "UTF-8");
				return JSONParse(result);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
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
				sb.append(line + "\n");
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
	
	public static String GETUrl(String url) {
		HttpClient httpclient=new DefaultHttpClient();
		HttpGet httpget=new HttpGet(url);
		HttpResponse resp;
		
		try{
			Log.v("WebServiceClient","GET url:" + url);
			resp=httpclient.execute(httpget);
			HttpEntity entity=resp.getEntity();
			
			InputStream instream=entity.getContent();
			String result=convertStreamToString(instream);
			
			Log.v("WebServiceClient", "GET Result:" + result);
			
			return result;
		}
		catch (ClientProtocolException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JSONObject GETParse(String url) {
		String result = GETUrl(url);
		return JSONParse(result);
	}
	
	public static JSONArray GETParseArray(String url) {
		String urlresult = GETUrl(url);
		JSONArray result;
		try {
			result = new JSONArray(urlresult);
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("WebServiceClient", "JSON Parsed.");
		return null;
	}
	
	public static String POSTUrl(String url, List<NameValuePair> param) throws IOException {
		HttpClient httpclient=new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		HttpResponse resp;
		
//		/* ---------- recode by Chaiphet S. ---------- */
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		for(int i=0;i<param.size();i++) {
//			params.add(new BasicNameValuePair(param.get(i).getName(), param.get(i).getValue().replace(" ", "%20")));
//		}
		
		try {
			ListIterator<NameValuePair> iter = param.listIterator();
			while(iter.hasNext()) {
				
				NameValuePair nvp = iter.next();
				String name = nvp.getName();
				String value = nvp.getValue();
				value = URLEncoder.encode(value, HTTP.UTF_8);
				nvp = new BasicNameValuePair(name , value.replace("+", "%20"));
				iter.set(nvp);
			}
			UrlEncodedFormEntity paramencoded = new UrlEncodedFormEntity(param);
			paramencoded.setContentEncoding(HTTP.UTF_8);
			httppost.setEntity(paramencoded);
			Log.v("WebServiceClient","POST url:" + url);
			resp=httpclient.execute(httppost);
			HttpEntity entity=resp.getEntity();
			
			InputStream instream=entity.getContent();
			String result=convertStreamToString(instream);
			
			Log.v("WebServiceClient", "POST Result:" + result);
			
			return result;
		}
		catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.i("WebServiceClient", "ClientProtocolException");
		}
		return null;
	}
	
	public static JSONObject POSTParse(String url, List<NameValuePair> param) throws IOException {
		String result = POSTUrl(url, param);
		return JSONParse(result);
	}
	
	public static JSONObject JSONParse(String res) {
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