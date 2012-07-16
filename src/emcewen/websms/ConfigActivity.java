package emcewen.websms;

import android.app.Activity;
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
    }

    @Override
    protected void onPause() {   
        super.onPause();
    }

}
