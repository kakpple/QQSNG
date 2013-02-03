package nanoFuntas.qqsng;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * This RankingFragment class is used by MainActivity to to show listView
 */
public class RankingFragment extends Fragment {
	private final static boolean DEBUG = true;
	private final static String TAG = "RankingFragment";
	
	private ListView mListView = null;
	private Button mButtonStart = null;
	private ImageView mImageViewHeart = null;
	
	@Override
    public void onStart(){
    	if(DEBUG) Log.d(TAG, "onStart()");
		
		super.onStart();
    	mListView = (ListView) getView().findViewById(R.id.listView1);
    	mButtonStart = (Button) getView().findViewById(R.id.buttonStart);
    	mImageViewHeart = (ImageView) getActivity().findViewById(R.id.imageViewHeart);
    	
    	mImageViewHeart.setImageResource(R.drawable.heart5);
    	
    	// kakpple test for heart image
    	mButtonStart.setOnClickListener(new OnClickListener(){
        	int i = 0;
    		@Override
			public void onClick(View v) {
				switch(i++%6){
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
							Log.d(TAG, "abnormal i/5");
					}							
				}
			}	
    	});
    	
        Bundle bundle = getArguments();       
        ArrayList<PhotoTextItem> mItemList = bundle.getParcelableArrayList("gamerList");        
        sortListView(mItemList);
    	PhotoTextListAdapter mPhotoTextListAdapter = new PhotoTextListAdapter(getActivity(), mItemList);
        mListView.setAdapter(mPhotoTextListAdapter);                        
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
