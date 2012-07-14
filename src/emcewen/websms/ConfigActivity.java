package emcewen.websms;

import org.json.JSONException;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ConfigActivity extends Activity {
	private static final String TAG = ConfigActivity.class.getSimpleName();
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.config);
	}
	
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    public void performSync(View view)
    {
    	Uri uri = Uri.parse("content://sms/inbox");
    	Cursor c= getContentResolver().query(uri, null, null ,null,null);
    	startManagingCursor(c);
    	
    	String[] body = new String[c.getCount()];
    	String[] number = new String[c.getCount()];
    	String[] date = new String[c.getCount()];
    	                
    	if(c.moveToFirst()){
    	        for(int i=0;i<c.getCount();i++){
    	                body[i]= c.getString(c.getColumnIndexOrThrow("body")).toString();
    	                number[i]=c.getString(c.getColumnIndexOrThrow("address")).toString();
    	                date[i]=c.getString(c.getColumnIndexOrThrow("date")).toString();
    	                c.moveToNext();
    	        }
    	}
    	c.close();
    	
    }

    @Override
    protected void onPause() {   
        super.onPause();
    }

}
