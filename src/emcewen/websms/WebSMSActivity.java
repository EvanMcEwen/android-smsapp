package emcewen.websms;

import org.json.JSONException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class WebSMSActivity extends Activity {
    /** Called when the activity is first created. */
	private static final String TAG = WebSMSActivity.class.getSimpleName();
	public static String PUSH_READY = "emcewen.websms.READY_FOR_PUSH_REGISTRATION";
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
        	Log.d(TAG,intent.getAction());
        	//Get the C2DM RegistrationID
        	String registrationID = intent.getStringExtra(C2DMRegistrationService.REGISTRATION_ID);
        	//Get the Username
        	EditText usrTXT = (EditText)findViewById(R.id.usernameTXT);
        	String username = usrTXT.getText().toString();
        	//Register with our server
            Intent serviceIntent = new Intent(C2DMRegistrationService.REGISTER_WITH_MYSERVER);
            serviceIntent.putExtra(C2DMRegistrationService.REGISTRATION_ID, registrationID);
            serviceIntent.putExtra(C2DMRegistrationService.PUSH_USERNAME, username);
            context.startService(serviceIntent);
        }
    };
    
    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(PUSH_READY);
        registerReceiver(receiver, filter);
        
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        
        super.onPause();
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void performLogin(View view) throws JSONException
    {
    	EditText usrTXT = (EditText)findViewById(R.id.usernameTXT);
    	String username = usrTXT.getText().toString();
    	
    	EditText pwdTXT = (EditText)findViewById(R.id.passwordTXT);
    	String password = pwdTXT.getText().toString();
    	
    	if (password.equals("") || username.equals(""))
    	{
    		Toast toast = Toast.makeText(getApplicationContext(), "Please enter your login information", Toast.LENGTH_SHORT);
    		toast.show();
    	}
    	else
    	{
    		LoginTask loginTask = new LoginTask();
    		loginTask.owner = this;
    		loginTask.execute(username,password);
    	}
    }
    
    public void pushSetup()
    {
        super.startService(new Intent(C2DMRegistrationService.REGISTER_WITH_C2DM));
    }
    
    public void resetLoginBoxes()
    {
    	EditText usrTXT = (EditText)findViewById(R.id.usernameTXT);
    	EditText pwdTXT = (EditText)findViewById(R.id.passwordTXT);
    	
    	usrTXT.setText("");
    	pwdTXT.setText("");
    }
    
}