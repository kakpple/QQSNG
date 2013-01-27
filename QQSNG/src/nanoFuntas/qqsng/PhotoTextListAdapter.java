package nanoFuntas.qqsng;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PhotoTextListAdapter extends BaseAdapter {

	private Context mContext = null;
	private ArrayList<PhotoTextItem> mItemList = new ArrayList<PhotoTextItem>();
	
	public PhotoTextListAdapter(Context context, ArrayList<PhotoTextItem> itemList){
		this.mContext = context;
		this.mItemList = itemList;
	}
	public void setListItems(ArrayList<PhotoTextItem> itemList){
		this.mItemList = itemList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		PhotoTextView itemView = null;
		if(convertView == null){
			itemView = new PhotoTextView(mContext, mItemList.get(position));
		}else{
			itemView = (PhotoTextView) convertView;
			
			itemView.setViewPhoto(mItemList.get(position).getPhoto());
			itemView.setViewName(mItemList.get(position).getName());
			itemView.setViewScore(mItemList.get(position).getScore());
		}
		
		return itemView;
	}

}
