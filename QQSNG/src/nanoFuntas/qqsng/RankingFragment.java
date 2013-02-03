package nanoFuntas.qqsng;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class RankingFragment extends Fragment {
	private final static boolean DEBUG = true;
	private final static String TAG = "RankingFragment";
	
	private ListView mListView = null;
	
	@Override
    public void onStart(){
    	if(DEBUG) Log.d(TAG, "onStart()");
		
		super.onStart();
    	mListView = (ListView) getView().findViewById(R.id.listView1);

        Bundle bundle = getArguments();       
        ArrayList<PhotoTextItem> mItemList = bundle.getParcelableArrayList("gamerList");
    	PhotoTextListAdapter mPhotoTextListAdapter = new PhotoTextListAdapter(getActivity(), mItemList);
        mListView.setAdapter(mPhotoTextListAdapter);                        
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	if(DEBUG) Log.d(TAG, "onCreateView()");
		
    	return inflater.inflate(R.layout.ranking_fragment, container, false);
	}
 
}
