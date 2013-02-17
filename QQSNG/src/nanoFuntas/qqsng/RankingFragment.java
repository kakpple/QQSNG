package nanoFuntas.qqsng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This RankingFragment class is used by MainActivity to to show listView
 */
public class RankingFragment extends Fragment {
	private final static boolean DEBUG = true;
	private final static String TAG = "RankingFragment";
	
	private final int NUMBER_OF_MAX_HEART = 5;
	private final int HEART_GENERATION_MILLI_SEC = 10 * 1000; //10 SEC
    private final long THOUSAND_MILLI_SEC = 1 * 1000; // 1000 milli second, 1 second 

	private ListView mListView = null;
	private Button mButtonStart = null;
	private ImageView mImageViewHeart = null;
	private TextView mTextViewTimer = null;
		
	private SharedPreferences mSPNumOfHearts = null;
	private SharedPreferences.Editor mSPEditorNumOfHearts = null;	
	private final String NUMBER_OF_HEATS = "namoFuntas.qqsng.numberOfhearts";
	private final String HEART = "HEART";

	private SharedPreferences mSPLastHeartTime = null;
	private SharedPreferences.Editor mSPEditorLastHeartTime = null;
	private final String LAST_HEART_TIME = "namoFuntas.qqsng.lastHeartTime";
	private final String TIME = "TIME";
	
    private Bundle bundle = null;
    private Intent intentToGame = null;
    
    //private final long HEART_MAKING_TIME = 9 * 1000; // 9 seconds
    
    private HeartTimer mHeartTimer = null;
    
	@Override
    public void onStart(){
    	if(DEBUG) Log.d(TAG, "onStart()");
		
		super.onStart();
    	mListView = (ListView) getView().findViewById(R.id.listView1);
    	mButtonStart = (Button) getView().findViewById(R.id.buttonStart);
    	mImageViewHeart = (ImageView) getActivity().findViewById(R.id.imageViewHeart);
    	mTextViewTimer = (TextView) getActivity().findViewById(R.id.timer_textView);
    	
    	mSPNumOfHearts = getActivity().getSharedPreferences(NUMBER_OF_HEATS, 0);
    	mSPEditorNumOfHearts = mSPNumOfHearts.edit();    	
    	
    	mSPLastHeartTime = getActivity().getSharedPreferences(LAST_HEART_TIME, 0);
    	mSPEditorLastHeartTime = mSPLastHeartTime.edit();
    	
    	int numberOfHeart = mSPNumOfHearts.getInt(HEART, NUMBER_OF_MAX_HEART); 	
		setHeartImage(numberOfHeart);    	
    	mHeartTimer = new HeartTimer(HEART_GENERATION_MILLI_SEC, THOUSAND_MILLI_SEC);
   	
    	// get, sort and display gamerLists
        bundle = getArguments();  
        ArrayList<PhotoTextItem> mItemList = bundle.getParcelableArrayList("gamerList");        
        sortListView(mItemList);
    	PhotoTextListAdapter mPhotoTextListAdapter = new PhotoTextListAdapter(getActivity(), mItemList);
        mListView.setAdapter(mPhotoTextListAdapter);                        
    	
        intentToGame = new Intent(getActivity(), Game.class);
        intentToGame.putExtras(bundle);
        
        showHeartsAndTimer();
        
        mButtonStart.setOnClickListener(new OnClickListener(){       	
    		@Override
			public void onClick(View v) {
    			int numberOfHeart = mSPNumOfHearts.getInt(HEART, NUMBER_OF_MAX_HEART);
        		if(numberOfHeart == 0){
        			Log.e(TAG, "No heart~~");
        			return;
        		}
    			
        		numberOfHeart--;
        		mSPEditorNumOfHearts.putInt(HEART, numberOfHeart);
        		mSPEditorNumOfHearts.commit();
        		
        		Log.d(TAG, "kakpple, NumOfHearts=" + mSPNumOfHearts.getInt(HEART, NUMBER_OF_MAX_HEART));
        		
        		if(numberOfHeart == NUMBER_OF_MAX_HEART - 1){
        			mSPEditorLastHeartTime.putLong(TIME, System.currentTimeMillis());
        			mSPEditorLastHeartTime.commit();
        		}
        		
        		setHeartImage(numberOfHeart);
        		startActivity(intentToGame);
        		       						
			}				
    	});    	
	}
        
	private void showHeartsAndTimer() {
		// TODO Auto-generated method stub
		int numOfHearts = mSPNumOfHearts.getInt(HEART, NUMBER_OF_MAX_HEART);
		
		if(numOfHearts >= NUMBER_OF_MAX_HEART ){
			setHeartImage(NUMBER_OF_MAX_HEART);
			return;
		}
		
		long now = System.currentTimeMillis();
		long lastHeartTime = mSPLastHeartTime.getLong(TIME, 0);
		long elaspedMilliSec = now - lastHeartTime;
		int heartGenerated = (int) ( elaspedMilliSec / HEART_GENERATION_MILLI_SEC );
		
		if(heartGenerated + numOfHearts >= NUMBER_OF_MAX_HEART){
			mSPEditorNumOfHearts.putInt(HEART, NUMBER_OF_MAX_HEART);
    		mSPEditorNumOfHearts.commit();
    		setHeartImage(NUMBER_OF_MAX_HEART);
		}else{
			long remainingMilliSec = HEART_GENERATION_MILLI_SEC - elaspedMilliSec % HEART_GENERATION_MILLI_SEC;
			numOfHearts += heartGenerated;
			mSPEditorNumOfHearts.putInt(HEART, numOfHearts);
    		mSPEditorNumOfHearts.commit();
    		setHeartImage(numOfHearts);
    		new HeartTimer(remainingMilliSec, THOUSAND_MILLI_SEC).start();
    		//mHeartTimer.start();
		}
	}

	private void setHeartImage(int numberOfHeart) {
		switch(numberOfHeart){
			case 0: mImageViewHeart.setImageResource(R.drawable.heart0);
					break;
			case 1: mImageViewHeart.setImageResource(R.drawable.heart1);
					break;
			case 2: mImageViewHeart.setImageResource(R.drawable.heart2);
					break;
			case 3: mImageViewHeart.setImageResource(R.drawable.heart3);
					break;
			case 4: mImageViewHeart.setImageResource(R.drawable.heart4);
					break;
			case 5: mImageViewHeart.setImageResource(R.drawable.heart5);
					break;
			default:{
					Log.d(TAG, "abnormal heart");
			}							
		}
	}	
	
	/**
	 * This sortListView method is used to sort listView according to score.
	 * @param mItemList listView needs to be sorted.
	 */
	private void sortListView(ArrayList<PhotoTextItem> mItemList) {
		PhotoTextItem pti = null;
        int sizeOfmItemList = mItemList.size();
        for(int i=0; i<sizeOfmItemList-1; i++){
			for(int j=0; j<sizeOfmItemList-1-i; j++){
				Long l1 = Long.parseLong(mItemList.get(j).getScore());
				Long l2 = Long.parseLong(mItemList.get(j+1).getScore());
        	
				if(l1 < l2){
					// change two elements
					pti = mItemList.get(j);
					mItemList.set(j, mItemList.get(j+1));
					mItemList.set(j+1, pti);
				}
			}
		}
	}


	class HeartTimer extends CountDownTimer{
		//public boolean isHeartTimerRunning = false;
		
		public HeartTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		
		@Override
		public void onFinish() {        	
        	int numberOfHeart = mSPNumOfHearts.getInt(HEART, NUMBER_OF_MAX_HEART);
        	
        	// kakpple test log
        	//Log.d(TAG, "kakpple, onfinish() NumberofHeart="+numberOfHeart);
        	
        	
        	numberOfHeart++;
        	mSPEditorNumOfHearts.putInt(HEART, numberOfHeart);
        	mSPEditorNumOfHearts.commit();
        	setHeartImage(numberOfHeart);
        	
        	if(numberOfHeart < NUMBER_OF_MAX_HEART){
        		new HeartTimer(HEART_GENERATION_MILLI_SEC, THOUSAND_MILLI_SEC).start();
        		//mHeartTimer.start();
        		//kakpple test log
        		Log.d(TAG, "HeartTimer.start(), number of heart =" + numberOfHeart);
        	}else if(numberOfHeart >= NUMBER_OF_MAX_HEART){
        	//	isHeartTimerRunning = false;
        		mTextViewTimer.setText("MAX");
        	}
		}

		@Override
		public void onTick(long millisUntilFinished) {
			SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        	String dateFormatted = formatter.format(millisUntilFinished);            	
        	mTextViewTimer.setText("" + dateFormatted);
		}
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	if(DEBUG) Log.d(TAG, "onCreateView()");
		
    	return inflater.inflate(R.layout.ranking_fragment, container, false);
	}
}
