package nanoFuntas.qqsng;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
	TextView tv6 = null;
	TextView tv7 = null;
	TextView tv8 = null;
	TextView tv9 = null;
	TextView tv10 = null;
	Button bt = null;
	
	// kakpple test Strings
    String mId = null;
    Long mHeart = null;
    Long mScore = null;
    Long mGold = null;

	
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
        tv6 = (TextView) findViewById(R.id.textView6);
        tv7 = (TextView) findViewById(R.id.textView7);
        tv8 = (TextView) findViewById(R.id.textView8);
        tv9 = (TextView) findViewById(R.id.textView9);
        tv10 = (TextView) findViewById(R.id.textView10);
        bt = (Button) findViewById(R.id.button1);
        Intent i = getIntent();
        
        bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        // show self info
        String strJsonSelf = i.getStringExtra("JSON_SELF_INFO");
        JSONObject jsonSelf = (JSONObject) JSONValue.parse(strJsonSelf);
        
        mId= (String) jsonSelf.get("SELF_ID");
		mHeart = (Long) jsonSelf.get("HEART");
		mScore = (Long) jsonSelf.get("SCORE");
		mGold = (Long) jsonSelf.get("GOLD");
        
		tv2.setText(mId);
        tv3.setText(Long.toString(mHeart));
        tv4.setText(Long.toString(mScore));
        tv5.setText(Long.toString(mGold));        
        
        // show friend info
        String strJsonFriend = i.getStringExtra("JSON_FRIENDS_INFO");
        JSONObject joFriend =  (JSONObject) JSONValue.parse(strJsonFriend);        
        JSONObject jsonFriend = (JSONObject) joFriend.get(Integer.toString(4));
                
        mId= (String) jsonFriend.get("FRIEND_ID");
		mHeart = (Long) jsonFriend.get("HEART");
		mScore = (Long) jsonFriend.get("SCORE");
		mGold = (Long) jsonFriend.get("GOLD");
        
		tv7.setText(mId);
        tv8.setText(Long.toString(mHeart));
        tv9.setText(Long.toString(mScore));
        tv10.setText(Long.toString(mGold));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_hub, menu);
        return true;
    }
}
