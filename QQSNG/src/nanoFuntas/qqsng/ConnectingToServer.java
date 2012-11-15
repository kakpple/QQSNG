package nanoFuntas.qqsng;

import java.io.IOException;
import java.io.StringWriter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.tencent.android.sdk.AppInfoConfig;
import com.tencent.android.sdk.OpenApiSdk;
import com.tencent.android.sdk.Person;
import com.tencent.android.sdk.RequestInfo;
import com.tencent.android.sdk.SdkCallException;
import com.tencent.android.sdk.SdkHandler;
import com.tencent.android.sdk.common.CommConfig;
import com.tencent.android.sdk.common.CommonUtil;
import com.tencent.sdk.snsjar.Sdk2OpenSns;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class ConnectingToServer extends Activity {
	
	// kakpple test TextViews
	TextView mTV_log = null;
	TextView myId = null;
	TextView friendId = null;
	
	private boolean DEBUG = true;
	private String TAG = "ConnectingToServer";
	private String REQ_TYPE_FETCH_SELF_INFO = "FETCH_SELF_INFO";
	
	private int loginType = CommConfig.LOGIN_FROM_MSF;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        if(DEBUG) Log.d(TAG, "onCreate called");
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting_to_server);
        
        // kakpple test TextViews
        mTV_log = (TextView)findViewById(R.id.textView5);
        myId = (TextView)findViewById(R.id.textView2);
        friendId = (TextView)findViewById(R.id.textView4);
        
        OpenApiSdk.setmContext(this);
        loginType = AppInfoConfig.getLoginType(ConnectingToServer.this);
        OpenApiSdk.getInstance().doRequestLogin(new LoginSdkHandler(),loginType);
    }

    private class LoginSdkHandler implements SdkHandler{

		@Override
		public void onSuccess(String rspContent, int statusCode) {
			// TODO Auto-generated method stub
	        if(DEBUG) Log.d(TAG, "LoginSdkHandler onSuccess called");

			String fileds = "id,nickname,displayName,birthday,thumbnailUrl,bloodType,hasApp,gender,addresses,sqq,sqqType,sqqLevel,gamevip,gamevipLevel,yellow";				
			RequestInfo info = new RequestInfo();
			OpenApiSdk sdk = OpenApiSdk.getInstance();
			
			try{
				sdk.getSelf(new GetSelfSdkHandler(), fileds, info);
			}catch(SdkCallException sdkex)
			{
				CommonUtil.showAlertDialog(ConnectingToServer.this, "Interface", "Fail to use Interface, Fail info:" + sdkex.getErrorMessage() + 
						" Inner error code:" + sdkex.getInternalErrorCode(),
						"Confirm", null, null, null, null);
			}
		}

		@Override
		public void onFailure(SdkCallException exp) {
			// TODO Auto-generated method stub
	        if(DEBUG) Log.d(TAG, "LoginSdkHandler onFailure called");
		}
    }
    
    private class GetSelfSdkHandler implements SdkHandler{

		@Override
		public void onSuccess(String rspContent, int statusCode) {
			// TODO Auto-generated method stub
	        if(DEBUG) Log.d(TAG, "GetSelfSdkHandler onSuccess called");
			
			Person p = Person.fromJsonString(rspContent);	
			String selfId = p.getId();
			myId.setText(selfId);
			
			JSONObject mJSONObject = new JSONObject();
			mJSONObject.put("REQ_TYPE", REQ_TYPE_FETCH_SELF_INFO);
			mJSONObject.put("SELF_ID", selfId);
			StringWriter sw = new StringWriter();
			try {
				mJSONObject.writeJSONString(sw);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String ss = sw.toString();
			String sss = mJSONObject.toString();
			
			HttpPostAsyncTask mHttpPostAsyncTask = new HttpPostAsyncTask();
			mHttpPostAsyncTask.execute(sss);
			try{
				String jsonStr = mHttpPostAsyncTask.get();
				JSONObject jo = (JSONObject) JSONValue.parse(jsonStr);
				String s = (String) jo.get("RSP_TYPE");
				mTV_log.setText(s);
				
			} catch(Exception e){
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(SdkCallException exp) {
			// TODO Auto-generated method stub
	        if(DEBUG) Log.d(TAG, "GetSelfSdkHandler onFailure called");
		}    	
    }
    
    //kakpple test
    private class HttpPostAsyncTask extends AsyncTask<String , Void, String>{
		@Override
		protected String doInBackground(String... params) {
			// executeHttpPost exception need to be dealt with
			return HttpUrlService.execStrPost(params[0]);
		}	
    }

    private class HttpPostJsonAsyncTask extends AsyncTask<JSONObject , Void, JSONObject>{
		@Override
		protected JSONObject doInBackground(JSONObject... params) {
			// executeHttpPost exception need to be dealt with
			return HttpUrlService.execJsonPost(params[0]);
		}	
    }
   
    //kakpple test
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
        if(DEBUG) Log.d(TAG, "onDestroy called");

		int loginType = AppInfoConfig.getLoginType(ConnectingToServer.this);
		if (loginType == CommConfig.LOGIN_FROM_QQGAMEHALL) {
			OpenApiSdk.getInstance().unregisterResultReceiver();
		} else if (loginType == CommConfig.LOGIN_FROM_MSF) {
			Sdk2OpenSns.getInstance().unregisterResultReceiver();
		}
		super.onDestroy();
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_connecting_to_server, menu);
        return true;
    }
}
