package nanoFuntas.qqsng;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.tencent.android.sdk.AppInfoConfig;
import com.tencent.android.sdk.FriendList;
import com.tencent.android.sdk.OpenApiSdk;
import com.tencent.android.sdk.Person;
import com.tencent.android.sdk.RequestInfo;
import com.tencent.android.sdk.SdkCallException;
import com.tencent.android.sdk.SdkHandler;
import com.tencent.android.sdk.common.CommConfig;
import com.tencent.android.sdk.common.CommonUtil;
import com.tencent.sdk.snsjar.Sdk2OpenSns;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

/**
 * This ConnectingToServer activity gets self ID and Friends ID from QQ server 
 * and send self and friends id to game server and gets self and friends info of Score, etc as JSON,
 * and finally sends ArrayList<PhotoTextItem> typed gamerList which include self and friends info of Photo/Name/Score to MainActivity
 */
public class ConnectingToServer extends Activity {
	private final boolean DEBUG = true;
	private final String TAG = "ConnectingToServer";
	
	private int loginType = CommConfig.LOGIN_FROM_MSF;
	
	private Intent intentToMainActivity = null;
	private ArrayList<PhotoTextItem> gamerList = new ArrayList<PhotoTextItem>();
	
	// isGetSelfDone and isGetFriendsDone flag is used to check if GetSelfSdkHandler and GetFriendsSdkHandler is over
	private boolean isGetSelfDone = false;
	private boolean isGetFriendsDone = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        if(DEBUG) Log.d(TAG, "onCreate called");
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting_to_server);

        intentToMainActivity = new Intent(ConnectingToServer.this, MainActivity.class);
        
        // login to QQ server
        OpenApiSdk.setmContext(this);
        loginType = AppInfoConfig.getLoginType(ConnectingToServer.this);
        OpenApiSdk.getInstance().doRequestLogin(new LoginSdkHandler(),loginType);
        
        // Monitor thread is used to make sure that both GetSelfSdkHandler and GetFriendsSdkHandler are over 
        // and starts MainActivity, sending gamerList to the MainActivity
        new MonitorThread("monitorThread").start();
    }

    private class LoginSdkHandler implements SdkHandler{
		@Override
		public void onSuccess(String rspContent, int statusCode) {
	        if(DEBUG) Log.d(TAG, "LoginSdkHandler onSuccess called");

			String fileds = "id,nickname,displayName,birthday,thumbnailUrl,bloodType,hasApp,gender,addresses,sqq,sqqType,sqqLevel,gamevip,gamevipLevel,yellow";				
			RequestInfo info = new RequestInfo();
			OpenApiSdk sdk = OpenApiSdk.getInstance();
			
			// initialize isGetSelfDone flag to false
	        isGetSelfDone = false;
			
	        // get self info from QQ server
			try{
				sdk.getSelf(new GetSelfSdkHandler(), fileds, info);
			}catch(SdkCallException sdkex){
				CommonUtil.showAlertDialog(ConnectingToServer.this, "个人信息接口", "接口调用失败,错误信息:" + sdkex.getMessage(),
						"确定", null, null, null, null);
			}
			
			// initialize isGetFriendsDone flag to false
	        isGetFriendsDone = false;
			
	        // get friends info from QQ server
			Integer start = 0;
			Integer count = 10;//请注意最大20	
			String ids = "301BB0F1B1204D93A801859A006460D1,606C16BADF574E34BA5051989081FA5E,8BC9A0B9FC9B06F8ACC7C7AFE736E271";				

			try{	
				sdk.getFriendList(new GetFriendsSdkHandler(), fileds,  start, count, info , ids);	
			}catch(SdkCallException sdkex){
				CommonUtil.showAlertDialog(ConnectingToServer.this, "接口调用", "接口调用失败,错误信息:" + sdkex.getErrorMessage() + 
					" 内部错误码:" + sdkex.getInternalErrorCode(),
					"确定", null, null, null, null);
			}
			
		}

		@Override
		public void onFailure(SdkCallException exp) {
			// TODO Auto-generated method stub
	        if(DEBUG) Log.d(TAG, "LoginSdkHandler onFailure called");
		}
    }
    
	/**
	 * 个人资料获取接口调用结果处理类
	 */   
    private class GetSelfSdkHandler implements SdkHandler{

		@Override
		public void onSuccess(String rspContent, int statusCode) {
	        if(DEBUG) Log.d(TAG, "GetSelfSdkHandler onSuccess called");
	        
	        String selfId = null;
	        String selfName = null;
	        // get self ID
			try{
				Person p = Person.fromJsonString(rspContent);	
				selfId = p.getId();
				selfName = p.getDisplayName();	
			} catch (Exception e) {
				CommonUtil.showAlertDialog(ConnectingToServer.this, "个人信息接口", "接口调用失败,错误信息:" + e.getMessage(),
						"确定", null, null, null, null);
			}

			JSONObject jsonSelfInfo = ServerIface.getSelfInfo(selfId);
			
			PhotoTextItem gamerSelf = new PhotoTextItem();
			gamerSelf.setName(selfName);
			Long score = (Long) jsonSelfInfo.get("SCORE");
			gamerSelf.setScore(score.toString());			
			gamerList.add(gamerSelf);
			
			isGetSelfDone = true;
			// put JSON self info get from server to intentToHubActivity intent as data
			// intentToHubActivity.putExtra("JSON_SELF_INFO", jsonSelfInfo.toString());			
		}		

		@Override
		public void onFailure(SdkCallException exp) {
			// TODO Auto-generated method stub
	        if(DEBUG) Log.d(TAG, "GetSelfSdkHandler onFailure called");
		}    	
    }
    
	/**
	 * 好友关系链获取接口调用结果处理类
	 */
	class GetFriendsSdkHandler implements SdkHandler{	
		
		@Override
		public void onSuccess(String rspContent , int statusCode){			
	        if(DEBUG) Log.d(TAG, "GetFriendsSdkHandler onSuccess called");
	        
			String[] friendId = null;
			String[] friendName = null;
			int numOfFriends = -1;
			
			try {
				FriendList fList = FriendList.fromJsonString(rspContent);	
				numOfFriends = fList.getFriendList().size();
				friendId = new String[numOfFriends + 1];
				friendName = new String[numOfFriends + 1];
				
				int i = 1;
				for ( Person p: fList.getFriendList()){
					friendId[i] = p.getId();
					friendName[i] = p.getDisplayName();
					i++;
				}
			} catch (Exception e) {
				CommonUtil.showAlertDialog(ConnectingToServer.this, "朋友信息接口", "接口调用失败,错误信息:" + e.getMessage(),
						"确定", null, null, null, null);
			}
						
			JSONObject jsonFriendsInfo = ServerIface.getFriendsInfo(friendId);
			
			// put JSON friends info get from server to intentToHubActivity intent as data
			//intentToHubActivity.putExtra("JSON_FRIENDS_INFO", jsonFriendsInfo.toString());
			PhotoTextItem gamerFriend = null;
			for(int i = 1; i < numOfFriends + 1; i++){
				JSONObject jsonFriend = (JSONObject) jsonFriendsInfo.get(Integer.toString(i));
				Long score = (Long) jsonFriend.get("SCORE");
				
				gamerFriend = new PhotoTextItem();
				gamerFriend.setName(friendName[i]);
				gamerFriend.setScore(score.toString());				
				gamerList.add(gamerFriend);	
			}
			
			isGetFriendsDone = true;
		}
		
		public void onFailure(SdkCallException e){
			CommonUtil.showAlertDialog(ConnectingToServer.this, "接口调用", "接口调用失败,错误信息:" + e.getMessage() 
					+ " 内部错误码：" + e.getInternalErrorCode(),
					"确定", null, null, null, null);
		}
	}
	
	/**
     * This Monitor thread is used to check if GetSelfSdkHandler and GetFriendsSdkHandler is over.
     * After GetSelfSdkHandler and GetFriendsSdkHandler is over, self and friends info is added to gamerList accordingly.
     * After both are done, gamerList is passed to MainActivity. 
     */
    class MonitorThread extends Thread {
        public MonitorThread(String str) {
        	super(str);
        }
        
        public void run() {
        	Log.d(TAG, "Monitor Thread is Started");
        	while(true){
		        if( (isGetSelfDone == true) && (isGetFriendsDone == true) ){
		        	Bundle bundle = new Bundle();
		    		bundle.putParcelableArrayList("gamerList", gamerList);
		   			intentToMainActivity.putExtras(bundle);
		   			startActivity(intentToMainActivity);
		   			finish();
		   			break;
	        	}
	        } 
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
