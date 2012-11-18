package nanoFuntas.qqsng;

import java.util.concurrent.ExecutionException;

import org.json.simple.JSONObject;

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
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class ConnectingToServer extends Activity {
	private final boolean DEBUG = true;
	private final String TAG = "ConnectingToServer";
	
	private final String REQ_TYPE = "REQ_TYPE";
	private final String REQ_SELF_INFO = "REQ_SELF_INFO";
	private final String SELF_ID = "SELF_ID";	
	private final String RSP_TYPE = "RSP_TYPE";
	
	private int loginType = CommConfig.LOGIN_FROM_MSF;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        if(DEBUG) Log.d(TAG, "onCreate called");
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting_to_server);
        
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
	        
	        // get self ID
			Person p = Person.fromJsonString(rspContent);	
			String selfId = p.getId();
			
			// set JSON parameters
			JSONObject jsonSelfId = new JSONObject();
			jsonSelfId.put(REQ_TYPE, REQ_SELF_INFO);
			jsonSelfId.put(SELF_ID, selfId);
			
			// Post json to server and receive one from server
			HttpPostJsonAsyncTask mHttpPostJsonAsyncTask = new HttpPostJsonAsyncTask();
			mHttpPostJsonAsyncTask.execute(jsonSelfId);				
			
			JSONObject jsonSelfInfo = null;	
			try {
				jsonSelfInfo = mHttpPostJsonAsyncTask.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Intent i = new Intent(ConnectingToServer.this, HubActivity.class);
			i.putExtra("JSON", jsonSelfInfo.toString());
			startActivity(i);
			
			finish();
			
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
