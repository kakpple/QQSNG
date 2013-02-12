package nanoFuntas.qqsng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Fragment;
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
	
	private ListView mListView = null;
	private Button mButtonStart = null;
	private ImageView mImageViewHeart = null;
	private TextView mTimer = null;
		
	private SharedPreferences mSharedPreferences = null;
	private SharedPreferences.Editor mSPeditor = null;
	private final String QQSNG_PREFERENCES = "namoFuntas.qqsng";
    private final String HEART = "HEART";
	
    private final long HEART_MAKING_TIME = 9 * 1000; // 9 seconds
    private final long ONE_SECOND = 1 * 1000; // 1000 milli second, 1 second 
    
    private HeartTimer mHeartTimer = null;
    
	@Override
    public void onStart(){
    	if(DEBUG) Log.d(TAG, "onStart()");
		
		super.onStart();
    	mListView = (ListView) getView().findViewById(R.id.listView1);
    	mButtonStart = (Button) getView().findViewById(R.id.buttonStart);
    	mImageViewHeart = (ImageView) getActivity().findViewById(R.id.imageViewHeart);
    	mTimer = (TextView) getActivity().findViewById(R.id.timer_textView);
    	
    	mSharedPreferences = getActivity().getSharedPreferences(QQSNG_PREFERENCES, 0);
    	mSPeditor = mSharedPreferences.edit();    	
    	
    	int numberOfHeart = mSharedPreferences.getInt(HEART, 5); 	
		setHeartImage(numberOfHeart);    	
    	mHeartTimer = new HeartTimer(HEART_MAKING_TIME, ONE_SECOND);
   	
    	// get, sort and display gamerLists
        Bundle bundle = getArguments();       
        ArrayList<PhotoTextItem> mItemList = bundle.getParcelableArrayList("gamerList");        
        sortListView(mItemList);
    	PhotoTextListAdapter mPhotoTextListAdapter = new PhotoTextListAdapter(getActivity(), mItemList);
        mListView.setAdapter(mPhotoTextListAdapter);                        
    	
    	// kakpple test for heart image
    	mButtonStart.setOnClickListener(new OnClickListener(){       	
    		@Override
			public void onClick(View v) {
    			int numberOfHeart = mSharedPreferences.getInt(HEART, 5);
        		if(numberOfHeart == 0){
        			Log.e(TAG, "No heart~~");
        			return;
        		}
    			
        		numberOfHeart--;
        		mSPeditor.putInt(HEART, numberOfHeart);
        		mSPeditor.commit();
        		
        		setHeartImage(numberOfHeart);
        		
            	if(!mHeartTimer.isHeartTimerRunning){
            		mHeartTimer.start();
            		mHeartTimer.isHeartTimerRunning = true;
            	}        						
			}				
    	});    	
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
		public boolean isHeartTimerRunning = false;
		
		public HeartTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		
		@Override
		public void onFinish() {        	
        	int numberOfHeart = mSharedPreferences.getInt(HEART, 5);
        	numberOfHeart++;
        	mSPeditor.putInt(HEART, numberOfHeart);
        	mSPeditor.commit();
        	setHeartImage(numberOfHeart);
        	
        	if(numberOfHeart < 5){
        		mHeartTimer.start();
        	}else if(numberOfHeart == 5){
        		isHeartTimerRunning = false;
        		mTimer.setText("MAX");
        	}
		}

		@Override
		public void onTick(long millisUntilFinished) {
			SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        	String dateFormatted = formatter.format(millisUntilFinished);            	
        	mTimer.setText("" + dateFormatted);
		}
	}
		
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	if(DEBUG) Log.d(TAG, "onCreateView()");
		
    	return inflater.inflate(R.layout.ranking_fragment, container, false);
	}
}
