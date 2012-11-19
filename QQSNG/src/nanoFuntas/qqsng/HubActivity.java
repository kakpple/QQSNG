package nanoFuntas.qqsng;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class HubActivity extends Activity {

	// kakpple test TextViews
	TextView tv1 = null;
	TextView tv2 = null;
	TextView tv3 = null;
	TextView tv4 = null;
	TextView tv5 = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        
        // kakpple test TextViews
        tv1 = (TextView) findViewById(R.id.textView1);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        tv4 = (TextView) findViewById(R.id.textView4);
        tv5 = (TextView) findViewById(R.id.textView5);
        
        Intent i = getIntent();
        
        String strJson = i.getStringExtra("JSON");
        JSONObject jo =  (JSONObject) JSONValue.parse(strJson);
        
        String mId = null;
        Long mHeart = null;
        Long mScore = null;
        Long mGold = null;
        
        mId= (String) jo.get("SELF_ID");
		mHeart = (Long) jo.get("HEART");
		mScore = (Long) jo.get("SCORE");
		mGold = (Long) jo.get("GOLD");
        
		tv2.setText(mId);
        tv3.setText(Long.toString(mHeart));
        tv4.setText(Long.toString(mScore));
        tv5.setText(Long.toString(mGold));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_hub, menu);
        return true;
    }
}
