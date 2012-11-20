package nanoFuntas.qqsng;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class HubActivity extends Activity {
	private final boolean DEBUG = true;
	private final String TAG = "HubActivity";
	
	// kakpple test TextViews
	TextView tv1 = null;
	TextView tv2 = null;
	TextView tv3 = null;
	TextView tv4 = null;
	TextView tv5 = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	if(DEBUG) Log.i(TAG, "HubActivity onCreate()");
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        
        // kakpple test TextViews
        tv1 = (TextView) findViewById(R.id.textView1);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        tv4 = (TextView) findViewById(R.id.textView4);
        tv5 = (TextView) findViewById(R.id.textView5);
        
        Intent i = getIntent();
        
        String strJson = i.getStringExtra("JSON_FRIENDS_INFO");
        
        tv2.setText(strJson);
        // test log
        Log.i(TAG, strJson);
        
        JSONObject j = null;
        /*
        JSONObject jo =  (JSONObject) JSONValue.parse(strJson);
        
        JSONObject jsonFriend = (JSONObject) jo.get(Integer.toString(1));
        
        String mId = null;
        Long mHeart = null;
        Long mScore = null;
        Long mGold = null;
        
        mId= (String) jsonFriend.get("FRIEND_ID");
		mHeart = (Long) jsonFriend.get("HEART");
		mScore = (Long) jsonFriend.get("SCORE");
		mGold = (Long) jsonFriend.get("GOLD");
        
		tv2.setText(mId);
        tv3.setText(Long.toString(mHeart));
        tv4.setText(Long.toString(mScore));
        tv5.setText(Long.toString(mGold));
*/   
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_hub, menu);
        return true;
    }
}
