package nanoFuntas.qqsng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
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
	
	private final static int MAX_HEART = 5;
	private final static int HEART_GENERATION_SEC = 120; //10 SEC
    private final static long THOUSAND_MILLI = 1000; // 1000 milli second, 1 second 
    
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
	private final String LH_TIME = "LH_TIME";

	private SharedPreferences mSPRemainingTime = null;
	private SharedPreferences.Editor mSPEditorRemainingTime = null;
	private final String REMAINING_TIME = "namoFuntas.qqsng.remainingTime";
	private final String RE_TIME = "RE_TIME";
	
    private Intent intentToGame = null;
    
    private MyHandler mMyHandler = new MyHandler();
    private static boolean doneFromTimer = true; 
    private static int remainingSec = HEART_GENERATION_SEC;
    private static HeartMonitorThread mHeartMonitorThread = null;    
    private static boolean heart5to4 = false;
    private static int numOfHearts;
    
    @Override
    public void onStart(){
    	if(DEBUG) Log.d(TAG, "onStart()");
		
		super.onStart();
    	mListView = (ListView) getView().findViewById(R.id.listView1);
    	mButtonStart = (Button) getView().findViewById(R.id.buttonStart);
    	mImageViewHeart = (ImageView) getActivity().findViewById(R.id.imageViewHeart);
    	mTextViewTimer = (TextView) getActivity().findViewById(R.id.timer_textView);
    	    	
    	// get, sort and display gamerLists
        Bundle bundle = getArguments();  
        ArrayList<PhotoTextItem> mItemList = bundle.getParcelableArrayList("gamerList");        
        sortListView(mItemList);
    	PhotoTextListAdapter mPhotoTextListAdapter = new PhotoTextListAdapter(getActivity(), mItemList);
        mListView.setAdapter(mPhotoTextListAdapter);  
    	
        intentToGame = new Intent(getActivity(), Game.class);
        intentToGame.putExtras(bundle);
        
    	mSPNumOfHearts = getActivity().getSharedPreferences(NUMBER_OF_HEATS, 0);
    	mSPEditorNumOfHearts = mSPNumOfHearts.edit();    	    	
    	mSPLastHeartTime = getActivity().getSharedPreferences(LAST_HEART_TIME, 0);
    	mSPEditorLastHeartTime = mSPLastHeartTime.edit();
    	mSPRemainingTime = getActivity().getSharedPreferences(REMAINING_TIME, 0);
    	mSPEditorRemainingTime = mSPRemainingTime.edit();
    	
    	numOfHearts = mSPNumOfHearts.getInt(HEART, MAX_HEART);
		setHeartImage(numOfHearts);
		if(numOfHearts == MAX_HEART){
			mTextViewTimer.setText("MAX");
		}   	                      
                
        if(mHeartMonitorThread == null){
        	mHeartMonitorThread = new HeartMonitorThread("HeartMonitorThread");
        	mHeartMonitorThread.start();
        }
        
        mButtonStart.setOnClickListener(new OnClickListener(){       	
    		@Override
			public void onClick(View v) {
    			if(DEBUG) Log.d(TAG, "GameStart Buttion Clicked!");
    			
    			numOfHearts = mSPNumOfHearts.getInt(HEART, MAX_HEART);
        		if(numOfHearts == 0){
        			Log.e(TAG, "No heart~~");
        			return;
        		}
    			
        		numOfHearts--;
        		if(numOfHearts == 4){
        			heart5to4 = true;
        		}
        		
        		mSPEditorNumOfHearts.putInt(HEART, numOfHearts);
        		mSPEditorNumOfHearts.commit();
        		        		        		
        		setHeartImage(numOfHearts);
        		startActivity(intentToGame);        		       						
			}				
    	});    	
	}
    
	private class HeartMonitorThread extends Thread {
		int now; 
		int lastHeartTime;
		int lastRemainingTime;
		int elaspedTime;
		int elaspedTimeSinceLastHeartIncreased;
		
		public HeartMonitorThread(String str) {
        	super(str);
        }
        
        public void run() {
        	Log.d(TAG, "Heart Monitor Thread is Started");
        	while(true){
        		if(doneFromTimer && mSPNumOfHearts.getInt(HEART, MAX_HEART) < MAX_HEART){        			        			
        			numOfHearts = mSPNumOfHearts.getInt(HEART, MAX_HEART);
        				
        			remainingSec = HEART_GENERATION_SEC;
        			if( heart5to4 ){
        				heart5to4 = false;
        				Log.d(TAG, "heart5to4");
        			}else{	
	        			now = (int) (System.currentTimeMillis() / THOUSAND_MILLI);
	        			lastHeartTime = (int) (mSPLastHeartTime.getLong(LH_TIME, now) / THOUSAND_MILLI);
	        			lastRemainingTime = (int) (mSPRemainingTime.getLong(RE_TIME, 0) / THOUSAND_MILLI);
	        			elaspedTime = now - lastHeartTime;
	        			elaspedTimeSinceLastHeartIncreased = HEART_GENERATION_SEC - lastRemainingTime + elaspedTime;
	        				
	        			Log.d(TAG, "now = " + now);
	        			Log.d(TAG, "lastHeartTime = " + lastHeartTime);
	        			Log.d(TAG, "elaspedTime = " + elaspedTime);
	        			Log.d(TAG, "lastRemainingTime = " + lastRemainingTime);
	        				
	        			if(numOfHearts + elaspedTimeSinceLastHeartIncreased / HEART_GENERATION_SEC >= MAX_HEART ){
	        				numOfHearts = MAX_HEART;
	        				mSPEditorNumOfHearts.putInt(HEART, numOfHearts);
	        		       	mSPEditorNumOfHearts.commit();
	        		       	doneFromTimer = true;
	        		       	continue;
	        			}else{
	        				numOfHearts += elaspedTimeSinceLastHeartIncreased / HEART_GENERATION_SEC;
	        				mSPEditorNumOfHearts.putInt(HEART, numOfHearts);
	        		       	mSPEditorNumOfHearts.commit();
	        		       	remainingSec = HEART_GENERATION_SEC - elaspedTimeSinceLastHeartIncreased % HEART_GENERATION_SEC;
	        			}
	        		}
        				
        			mMyHandler.sendMessage(mMyHandler.obtainMessage());
        			doneFromTimer = false;
        			
        		}
        	} 
        }

    }

	
	class HeartTimer extends CountDownTimer{		
		SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
		String dateFormatted = null;
		
		public HeartTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		
		@Override
		public void onFinish() {        	
        	if(DEBUG) Log.d(TAG, "Class HeartTimer, onFinish()");
			numOfHearts = mSPNumOfHearts.getInt(HEART, MAX_HEART);
        	      	
        	numOfHearts++;
        	mSPEditorNumOfHearts.putInt(HEART, numOfHearts);
        	mSPEditorNumOfHearts.commit();
        	setHeartImage(numOfHearts);
        	
			mSPEditorLastHeartTime.putLong(LH_TIME, System.currentTimeMillis());
			mSPEditorLastHeartTime.commit();
        	
			mSPEditorRemainingTime.putLong(RE_TIME, HEART_GENERATION_SEC * THOUSAND_MILLI);
			mSPEditorRemainingTime.commit(); 
			
        	if(numOfHearts >= MAX_HEART){
        		mTextViewTimer.setText("MAX");
        	}
        	
        	doneFromTimer = true;
		}
		
		@Override
		public void onTick(long millisUntilFinished) {			
        	dateFormatted = formatter.format(millisUntilFinished);            	
        	mTextViewTimer.setText("" + dateFormatted);
        	
        	mSPEditorLastHeartTime.putLong(LH_TIME, System.currentTimeMillis());
			mSPEditorLastHeartTime.commit();
        	
			mSPEditorRemainingTime.putLong(RE_TIME, millisUntilFinished);
			mSPEditorRemainingTime.commit();        	
		}
	}
		

	private class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg){	
			if(DEBUG) Log.d(TAG, "class MyHandler, handleMessage()");
			new HeartTimer(remainingSec * THOUSAND_MILLI, THOUSAND_MILLI).start();
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
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	if(DEBUG) Log.d(TAG, "onCreateView()");
		
    	return inflater.inflate(R.layout.ranking_fragment, container, false);
	}
}
