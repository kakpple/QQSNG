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
	private final static long HEART_GENERATION_MILLI = 120 * 1000; //10 SEC
    private final static long THOUSAND_MILLI = 1 * 1000; // 1000 milli second, 1 second 

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
	
    private Intent intentToGame = null;
    
    private MyHandler mMyHandler = new MyHandler();
    private static boolean doneFromTimer = true; 
    private static long remainingSec = HEART_GENERATION_MILLI;
    private static HeartMonitorThread mHeartMonitorThread = null;
    
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
    	
    	int numOfHearts = mSPNumOfHearts.getInt(HEART, MAX_HEART);
		setHeartImage(numOfHearts);
		if(numOfHearts == MAX_HEART){
			mTextViewTimer.setText("MAX");
		}
   	
    	// get, sort and display gamerLists
        Bundle bundle = getArguments();  
        ArrayList<PhotoTextItem> mItemList = bundle.getParcelableArrayList("gamerList");        
        sortListView(mItemList);
    	PhotoTextListAdapter mPhotoTextListAdapter = new PhotoTextListAdapter(getActivity(), mItemList);
        mListView.setAdapter(mPhotoTextListAdapter);                        
    	
        intentToGame = new Intent(getActivity(), Game.class);
        intentToGame.putExtras(bundle);
                
        if(mHeartMonitorThread == null){
        	mHeartMonitorThread = new HeartMonitorThread("HeartMonitorThread");
        	mHeartMonitorThread.start();
        }
        
        mButtonStart.setOnClickListener(new OnClickListener(){       	
    		@Override
			public void onClick(View v) {
    			if(DEBUG) Log.d(TAG, "GameStart Buttion Clicked!");
    			
    			int numOfHearts = mSPNumOfHearts.getInt(HEART, MAX_HEART);
        		if(numOfHearts == 0){
        			Log.e(TAG, "No heart~~");
        			return;
        		}
    			
        		numOfHearts--;
        		mSPEditorNumOfHearts.putInt(HEART, numOfHearts);
        		mSPEditorNumOfHearts.commit();
        		
        		setHeartImage(numOfHearts);
        		startActivity(intentToGame);        		       						
			}				
    	});    	
	}
    
	class HeartTimer extends CountDownTimer{		
		public HeartTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		
		@Override
		public void onFinish() {        	
        	if(DEBUG) Log.d(TAG, "Class HeartTimer, onFinish()");
			int numOfHearts = mSPNumOfHearts.getInt(HEART, MAX_HEART);
        	      	
        	numOfHearts++;
        	mSPEditorNumOfHearts.putInt(HEART, numOfHearts);
        	mSPEditorNumOfHearts.commit();
        	setHeartImage(numOfHearts);
        	
			mSPEditorLastHeartTime.putLong(TIME, System.currentTimeMillis());
			mSPEditorLastHeartTime.commit();
        	
			// kakpple test
			long now = mSPLastHeartTime.getLong(TIME, 0);
        	Log.d(TAG, "HeartTimer onFinish() millis =" + now);
        	
        	if(numOfHearts >= MAX_HEART){
        		mTextViewTimer.setText("MAX");
        	}
        	
        	doneFromTimer = true;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        	String dateFormatted = formatter.format(millisUntilFinished);            	
        	mTextViewTimer.setText("" + dateFormatted);
		}
	}
		
	private class HeartMonitorThread extends Thread {
        public HeartMonitorThread(String str) {
        	super(str);
        }
        
        public void run() {
        	Log.d(TAG, "Heart Monitor Thread is Started");
        	while(true){
        		if(doneFromTimer){
        			int numOfHearts = mSPNumOfHearts.getInt(HEART, MAX_HEART);
        			if(numOfHearts < MAX_HEART){
        				long now = System.currentTimeMillis();
        				long lastHeartTime = mSPLastHeartTime.getLong(TIME, now);
        				long elaspedTime = now - lastHeartTime;
        				
        				Log.d(TAG, "now = " + now);
        				Log.d(TAG, "lastHeartTime = " + lastHeartTime);
        				Log.d(TAG, "elaspedTime = " + elaspedTime);
        				
        				if(numOfHearts + elaspedTime/HEART_GENERATION_MILLI >=5 ){
        					numOfHearts = MAX_HEART;
        					mSPEditorNumOfHearts.putInt(HEART, numOfHearts);
        		        	mSPEditorNumOfHearts.commit();
        				}else{
        					numOfHearts += elaspedTime/HEART_GENERATION_MILLI;
        					mSPEditorNumOfHearts.putInt(HEART, numOfHearts);
        		        	mSPEditorNumOfHearts.commit();
        		        	remainingSec = HEART_GENERATION_MILLI - elaspedTime % HEART_GENERATION_MILLI;
        				}
        				
        				mMyHandler.sendMessage(mMyHandler.obtainMessage());
        				doneFromTimer = false;
        			}
        		}
        	} 
        }

    }

	private class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg){	
			if(DEBUG) Log.d(TAG, "class MyHandler, handleMessage()");
			new HeartTimer(remainingSec, THOUSAND_MILLI).start();
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
