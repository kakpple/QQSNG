package nanoFuntas.qqsng;

import org.json.simple.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Game extends Activity {
	private final boolean DEBUG = true;
	private final String TAG = "Game";
    
	// kakpple test
	EditText et = null;
	Button bt = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.i(TAG, "onCreate()");
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        
        et = (EditText) findViewById(R.id.editText1);
        bt = (Button) findViewById(R.id.button1);     
        bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String s = et.getText().toString();
				double d = Double.valueOf(s);
				
				JSONObject jo = ServerIface.updateScore(d);
				String strGet = (String) jo.get("STAT_CODE");
				bt.setText(strGet);
			}  	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game, menu);
        return true;
    }
}
