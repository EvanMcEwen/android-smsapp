package emcewen.websms;

import emcewen.websms.receivers.AlarmReceiver;
import emcewen.websms.tasks.SyncTask;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class ConfigActivity extends Activity {
	private static final String TAG = ConfigActivity.class.getSimpleName();
	private AlarmReceiver myAlarm = null;
	/** Called when the activity is first created. */
	@Override
	//On first launch start the alarm if it hasn't already been started
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.config);
	    if (myAlarm == null)
	    	myAlarm = new AlarmReceiver(this,900000);
	}
	
    @Override
    //If the user resumes, start the alarm if it hasn't started for some reason
    protected void onResume() {
        super.onResume();
	    if (myAlarm == null)
	    	myAlarm = new AlarmReceiver(this,900000);
    }
    
    //This handles the button press event
    public void performSync(View view)
    {
		SyncTask syncTask = new SyncTask();
		syncTask.owner = this;
		syncTask.execute();
    }

    @Override
    protected void onPause() {   
        super.onPause();
    }

}
