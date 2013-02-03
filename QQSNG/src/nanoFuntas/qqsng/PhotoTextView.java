package nanoFuntas.qqsng;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This PhotoTextView class is item view for data type PhotoTextItem to be added to ListView of RangkingFragment
 */
public class PhotoTextView extends LinearLayout {	
	private ImageView mPhoto;
	private TextView mName;
	private TextView mScore;
	
	public PhotoTextView(Context context, PhotoTextItem mItem) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_view, this, true);
		
		mPhoto = (ImageView) findViewById(R.id.photo);
		mName = (TextView) findViewById(R.id.name);
		mScore = (TextView) findViewById(R.id.score);
		
		mPhoto.setImageBitmap(mItem.getPhoto());
		mName.setText(mItem.getName());
		mScore.setText(mItem.getScore());
	}
	
	public void setViewPhoto(Bitmap photo){
		mPhoto.setImageBitmap(photo);
	}
	
	public void setViewName(String name){
		mName.setText(name);
	}
	
	public void setViewScore(String score){
		mScore.setText(score);
	}
}
