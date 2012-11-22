package nanoFuntas.qqsng;

import java.util.concurrent.ExecutionException;
import org.json.simple.JSONObject;
import android.os.AsyncTask;
import android.util.Log;

/** 
 * This ServerIface class provides applications APIs to send/get JSON data to/from Server
 */
public class ServerIface {
	// debugging info
	private static final boolean DEBUG = true;
	private static final String TAG = "ServerIface";
	
	// token 
	private static final String REQ_TYPE = "REQ_TYPE";
	private static final String REQ_SELF_INFO = "REQ_SELF_INFO";
	private static final String REQ_FRIENDS_INFO = "REQ_FRIENDS_INFO";
	private static final String SELF_ID = "SELF_ID";	
	private static final String FRIEND_ID = "FRIEND_ID";	
	private static final String RSP_TYPE = "RSP_TYPE";
	
	/**
	 * This getSelfInfo function sends selfId to server and returns self Info as JSON data to application
	 * 
	 * @param selfId, self Id
	 * @return JSONObject, that contains self info
	 */
	public static JSONObject getSelfInfo(String selfId) {
		if(DEBUG) Log.i(TAG, "getSelfInfo()");
		
		// set JSON parameters
		JSONObject jsonSelfId = new JSONObject();
		jsonSelfId.put(REQ_TYPE, REQ_SELF_INFO);
		jsonSelfId.put(SELF_ID, selfId);
		
		// Post JSON to server and receive self info response from server
		HttpPostJsonAsyncTask mHttpPostJsonAsyncTask = new HttpPostJsonAsyncTask();
		mHttpPostJsonAsyncTask.execute(jsonSelfId);				
		
		JSONObject jsonSelfInfo = null;	
		try {
			jsonSelfInfo = mHttpPostJsonAsyncTask.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return jsonSelfInfo;
	}
	
	/**
	 * This getFriendsInfo function sends friendId to server and returns self Info as JSON data to application
	 * 
	 * @param friendId String array that contains all friend ids
	 * @return JSONObject, that contains friends info
	 */
	public static JSONObject getFriendsInfo(String[] friendId) {
		if(DEBUG) Log.i(TAG, "getFriendsInfo()");
		
		JSONObject jsonFriendsId = new JSONObject();
		jsonFriendsId.put(REQ_TYPE, REQ_FRIENDS_INFO);
        
		int NumOfFriends = friendId.length -1;
		for(int i = 1; i <= NumOfFriends; i++){
			jsonFriendsId.put(Integer.toString(i), friendId[i]);
		}
				
		// Post json to server and receive one from server
		HttpPostJsonAsyncTask mHttpPostJsonAsyncTask = new HttpPostJsonAsyncTask();
		mHttpPostJsonAsyncTask.execute(jsonFriendsId);				
					
		JSONObject jsonFriendsInfo = null;	
		try {
			jsonFriendsInfo = mHttpPostJsonAsyncTask.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return jsonFriendsInfo;
	}
	
	/**
     * This HttpPostStrAsyncTask class sends/gets String data to/from server using HttpUrlService class
     * But not used by now
     */
    private static class HttpPostStrAsyncTask extends AsyncTask<String , Void, String>{
		@Override
		protected String doInBackground(String... params) {
    		if(DEBUG) Log.i(TAG, "HttpPostStrAsyncTask, doInBackground()");
			return HttpUrlService.execStrPost(params[0]);
		}	
    }

    /**
     * This HttpPostJsonAsyncTask class sends/gets JSON data to/from server using HttpUrlService class
     */
    private static class HttpPostJsonAsyncTask extends AsyncTask<JSONObject , Void, JSONObject>{
    	@Override
		protected JSONObject doInBackground(JSONObject... params) {
    		if(DEBUG) Log.i(TAG, "HttpPostJsonAsyncTask, doInBackground()");
    		return HttpUrlService.execJsonPost(params[0]);
		}	
    }
}
