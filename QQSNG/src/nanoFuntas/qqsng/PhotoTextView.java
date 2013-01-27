package nanoFuntas.qqsng;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PhotoTextView extends LinearLayout {
	private ImageView mPhoto;
	private TextView mName;
	private TextView mScore;
	
	public PhotoTextView(Context context, PhotoTextItem mItem) {
		super(context);
		// TODO Auto-generated constructor stub
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_view, this, true);
		
		mPhoto = (ImageView) findViewById(R.id.photo);
		mName = (TextView) findViewById(R.id.name);
		mScore = (TextView) findViewById(R.id.score);
		
		mPhoto.setImageDrawable(mItem.getPhoto());
		mName.setText(mItem.getName());
		mScore.setText(mItem.getName());
	}
	
	public void setViewPhoto(Drawable photo){
		mPhoto.setImageDrawable(photo);
	}
	
	public void setViewName(String name){
		mName.setText(name);
	}
	
	public void setViewScore(String score){
		mScore.setText(score);
	}
}
