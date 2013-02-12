package nanoFuntas.qqsng;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private final static boolean DEBUG = true;
	private final static String TAG = "MainActivity";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        if(DEBUG) Log.d(TAG, "onCreate()");
		
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
            // bundle contains the following gamerList
            // ArrayList<PhotoTextItem> mPhotoTextItem = bundle.getParcelableArrayList("gamerList");
            
            mRankingFragment.setArguments(bundle);
            
            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction().add(R.id.fragment_container, mRankingFragment).commit();
        }
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}

