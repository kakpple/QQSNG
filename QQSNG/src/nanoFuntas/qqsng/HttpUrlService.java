package nanoFuntas.qqsng;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HttpUrlService {
	
	private final static boolean DEBUG = true;
	private final static String TAG = "HttpUrlService";
	
	private final static int HTTP_CONNECT_TIMEOUT = 30 * 1000; //30 sec
	private final static int HTTP_READ_TIMEOUT = 30 * 1000; //30 sec
	
	private final static String strURL = "http://192.168.219.174:8080/QQSNGServer/QQSNGServlet";
	
	public static String execStrPost(String strPram) {
		
		if(DEBUG) Log.i(TAG, "execStrPost");
		
		String strResult = null;
		URL mURL = null;
		HttpURLConnection mHttpURLConn = null;
		OutputStream outStrm = null;
		ObjectOutputStream objOutStrm = null;
		InputStream inStrm = null;
		ObjectInputStream objInStrm = null;
		int httpStatCode = 0;
		
		try{
			mURL = new URL(strURL);
			mHttpURLConn = (HttpURLConnection) mURL.openConnection();
			
			// HTTP setting
			mHttpURLConn.setDoOutput(true);
			mHttpURLConn.setDoInput(true);
			mHttpURLConn.setUseCaches(false);		
			mHttpURLConn.setRequestMethod("POST");
			mHttpURLConn.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
			mHttpURLConn.setReadTimeout(HTTP_READ_TIMEOUT);
	
			mHttpURLConn.connect();
			
			// write output data to be sent
			outStrm = mHttpURLConn.getOutputStream();
			objOutStrm = new ObjectOutputStream(outStrm);
			objOutStrm.writeObject(strPram);
			objOutStrm.flush();
					
			// Log HTTP status code for debugging
			httpStatCode = mHttpURLConn.getResponseCode();
			if(DEBUG) Log.i( TAG, "HTTP status code: " + Integer.toString(httpStatCode) );
			
			// get data from server and parse it to string
			inStrm = mHttpURLConn.getInputStream();
			objInStrm = new ObjectInputStream(inStrm);	
			strResult = (String) objInStrm.readObject();
			
		} catch(IOException e){
			e.printStackTrace();
			
		} catch(ClassNotFoundException e){
			e.printStackTrace();
			
		} finally{
			try{
				// release resources
				if(mHttpURLConn != null) mHttpURLConn.disconnect();
				if(outStrm != null) outStrm.close();
				if(objOutStrm != null) objOutStrm.close();
				if(inStrm != null) inStrm.close();
				if(objInStrm != null) objInStrm.close();
				
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return strResult;
	}
	
	public JSONObject execJsonPost(JSONObject jsonParam) throws JSONException{
		JSONObject jsonResult = null;
		
		String strIn = jsonParam.toString();
		String strOut = execStrPost(strIn);
		jsonResult = new JSONObject(strOut);
		
		return jsonResult;
	}
}
