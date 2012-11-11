package nanoFuntas.qqsng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class HttpClientService {
	private static HttpClient mHttpClient = null;
	private static HttpPost mHttpPost = null;
	private static HttpResponse mHttpResponse = null;
	
	// yeongsu.wu added for connection timeout test start
	private static final int HTTP_TIMEOUT = 30 * 1000;
	
	boolean DEBUG = true;
	final String TAG = "HttpClientService";
	// Address for server servlet
	final static String strUrl = "http://192.168.219.120:8080/QQSNGServer/QQSNGServlet";
	
	public String executeHttpPost(ArrayList<NameValuePair> postParameters){
        if(DEBUG) Log.d(TAG, "wrapPostParameters()");
		UrlEncodedFormEntity formEntity = null;
		StringEntity se = null;
		if(mHttpClient == null){
			mHttpClient = new DefaultHttpClient();
			// yeongsu.wu added for connection timeout test start
			final HttpParams mHttpParams = mHttpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(mHttpParams, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(mHttpParams, HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(mHttpParams, HTTP_TIMEOUT);
			// yeongsu.wu added for connection timeout test end
		}
		mHttpPost = new HttpPost(strUrl);

		try{
			formEntity = new UrlEncodedFormEntity(postParameters);
			/*se = new StringEntity(postParameters);*/
		} catch (UnsupportedEncodingException e1){
			e1.printStackTrace();
		}		
		mHttpPost.setEntity(formEntity); 
		/*mHttpPost.setEntity(se); */
		
				
		try {
			mHttpResponse = mHttpClient.execute(mHttpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getOutputFromHttpResponse(mHttpResponse);
	}

	private String getOutputFromHttpResponse(HttpResponse response){
        if(DEBUG) Log.d(TAG, "getOutputFromHttpResponse()");

		HttpEntity entity = response.getEntity();								
		
		InputStreamReader isr = null;
		BufferedReader br = null;
		StringBuffer sb = null;
		
		try {
			isr = new InputStreamReader(entity.getContent());
			br = new BufferedReader(isr);
			sb = new StringBuffer("");
			String line = "";
			
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();				
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(isr != null)
				try {
					isr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(br != null)
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}				
		return sb.toString();
	}
}
