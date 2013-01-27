package nanoFuntas.qqsng;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            
            // Create an instance of ExampleFragment
            Fragment mRankingFragment = new RankingFragment();
            
            // Receive intent from ConnectingToServer activity extract bundle from intent, and send this bundle to RangkingFragment  
            Intent intentFromConnectingToServer = getIntent();
            Bundle bundle = intentFromConnectingToServer.getExtras();
            
            ArrayList<PhotoTextItem> mPhotoTextItem;
            mPhotoTextItem = bundle.getParcelableArrayList("gamerList");
            
            String name = mPhotoTextItem.get(1).getName();
            String score = mPhotoTextItem.get(1).getScore();
            
            Log.d("Kakpple_test", name);
            Log.d("Kakpple_test", score);
            
            mRankingFragment.setArguments(bundle);
            
            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            //mRankingFragment.setArguments(getIntent().getExtras());
            
            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction().add(R.id.fragment_container, mRankingFragment).commit();
        }
    }

	/*
    private ArrayList<PhotoTextItem> getListView(){
    	ArrayList<PhotoTextItem> itemList = new ArrayList<PhotoTextItem>();
    	PhotoTextItem item = null;
    	Drawable photo = getResources().getDrawable(R.drawable.ic_launcher);

    	item = new PhotoTextItem();
    	item.setPhoto(photo);
    	item.setName("name1");
    	item.setScore("score1");
    	itemList.add(item);
    	
    	item = new PhotoTextItem();
    	item.setPhoto(photo);
    	item.setName("name2");
    	item.setScore("score2");
    	itemList.add(item);
    	
    	item = new PhotoTextItem();
    	item.setPhoto(photo);
    	item.setName("name3");
    	item.setScore("score3");
    	itemList.add(item);
    	
    	item = new PhotoTextItem();
    	item.setPhoto(photo);
    	item.setName("name4");
    	item.setScore("score4");
    	itemList.add(item);
    	
    	item = new PhotoTextItem();
    	item.setPhoto(photo);
    	item.setName("name5");
    	item.setScore("score5");
    	itemList.add(item);
    	
    	item = new PhotoTextItem();
    	item.setPhoto(photo);
    	item.setName("name6");
    	item.setScore("score6");
    	itemList.add(item);
    	
    	item = new PhotoTextItem();
    	item.setPhoto(photo);
    	item.setName("name7");
    	item.setScore("score7");
    	itemList.add(item);
    	
    	item = new PhotoTextItem();
    	item.setPhoto(photo);
    	item.setName("name8");
    	item.setScore("score8");
    	itemList.add(item);
    	
    	item = new PhotoTextItem();
    	item.setPhoto(photo);
    	item.setName("name9");
    	item.setScore("score9");
    	itemList.add(item);
    	
    	item = new PhotoTextItem();
    	item.setPhoto(photo);
    	item.setName("name9");
    	item.setScore("score9");
    	itemList.add(item);

    	item = new PhotoTextItem();
    	item.setPhoto(photo);
    	item.setName("name9");
    	item.setScore("score9");
    	itemList.add(item);
    	
    	item = new PhotoTextItem();
    	item.setPhoto(photo);
    	item.setName("name9");
    	item.setScore("score9");
    	itemList.add(item);
    	
    	item = new PhotoTextItem();
    	item.setPhoto(photo);
    	item.setName("name9");
    	item.setScore("score9");
    	itemList.add(item);
    	
    	item = new PhotoTextItem();
    	item.setPhoto(photo);
    	item.setName("name9");
    	item.setScore("score9");
    	itemList.add(item);
    	
    	item = new PhotoTextItem();
    	item.setPhoto(photo);
    	item.setName("name9");
    	item.setScore("score9");
    	itemList.add(item);
    	
    	item = new PhotoTextItem();
    	item.setPhoto(photo);
    	item.setName("name9");
    	item.setScore("score9");
    	itemList.add(item);
    	return itemList;
    	
    }
    */
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}

