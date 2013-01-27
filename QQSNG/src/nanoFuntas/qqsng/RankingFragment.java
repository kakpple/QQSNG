package nanoFuntas.qqsng;

import java.util.ArrayList;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class RankingFragment extends Fragment {
    
	private ListView mListView = null;
	
	@Override
    public void onStart(){
    	super.onStart();
    	mListView = (ListView) getView().findViewById(R.id.listView1);

    	ArrayList<PhotoTextItem> mItemList = getListView();
    	PhotoTextListAdapter mPhotoTextListAdapter = new PhotoTextListAdapter(getActivity(), mItemList);
        mListView.setAdapter(mPhotoTextListAdapter);
        
        // kakpple_test 
        Bundle bundle = getArguments();
        
        ArrayList<PhotoTextItem> mPhotoTextItem;
        mPhotoTextItem = bundle.getParcelableArrayList("gamerList");
        
        String name = mPhotoTextItem.get(0).getName();
        
        //String s = bundle.getString("JSON_FRIENDS_INFO");
        Log.d("kakpple_test", name);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	         	
    	View view = inflater.inflate(R.layout.ranking_fragment, container, false);
    	
    	return view;
    }
    
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
    
}
