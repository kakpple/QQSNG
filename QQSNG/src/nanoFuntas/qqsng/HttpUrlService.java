/**
 * All copy rights reserved by nanoFuntas studio 2012.
 */
package nanoFuntas.qqsng;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.util.Log;

/**
 * HttpUrlService class serves for client to receive or send String or JSON data
 * between client and server.
 */
public class HttpUrlService {
	// debug info
	private final static boolean DEBUG = true;
	private final static String TAG = "HttpUrlService";
	// HTTP time out parameters
	private final static int HTTP_CONNECT_TIMEOUT = 30 * 1000; //30 seconds
	private final static int HTTP_READ_TIMEOUT = 30 * 1000; //30 seconds
	// URL to servlet
	private final static String strURL = "http://192.168.219.165:8080/QQSNGServer/QQSNGServlet";
	
	/**
	 * Function execStrPost sends String data received from client to server,
	 * and fetch String data from server and return it to client.
	 * 
	 * @param strParam String data received from client to be sent to server
	 * @return String data received from server
	 */
	public static String execStrPost(String strParam) {		
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
			objOutStrm.writeObject(strParam);
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
	
	/** 
	 * Function execJsonPost sends JSON data received from client to server,
	 * and fetch JSON data from server and return it to client.
	 * Function execJsonPost wraps function execStrPost 
	 * 
	 * @jsonParam JSONObject data received from client to be sent to server
	 * @return JSONObject data received from server
	 */
	public static JSONObject execJsonPost(JSONObject jsonParam) {
		String strIn = jsonParam.toString();
		String strOut = execStrPost(strIn);			
		JSONObject jsonResult = (JSONObject) JSONValue.parse(strOut);
		
		return jsonResult;
	}
}
