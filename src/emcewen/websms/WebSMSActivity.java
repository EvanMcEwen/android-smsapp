package emcewen.websms;

import org.json.JSONException;

import emcewen.websms.services.C2DMRegistrationService;
import emcewen.websms.tasks.LoginTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class WebSMSActivity extends Activity {
    /** Called when the activity is first created. */
	private static final String TAG = WebSMSActivity.class.getSimpleName();
	public static String PUSH_READY = "emcewen.websms.READY_FOR_PUSH_REGISTRATION";
	public static String PUSH_SUCCESS = "emcewen.websms.PUSH_REGISTRATION_SUCCESS";
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
        	Log.d(TAG,intent.getAction());
        	
        	//C2DM Registration was successful. Let's tell the web app
        	if (intent.getAction().equals(PUSH_READY))
        	{
	        	//Get the C2DM RegistrationID
	        	String registrationID = intent.getStringExtra(C2DMRegistrationService.REGISTRATION_ID);
	        	//Get the Username
	        	EditText usrTXT = (EditText)findViewById(R.id.usernameTXT);
	        	String username = usrTXT.getText().toString();
	        	//Register with our server
	            Intent serviceIntent = new Intent(C2DMRegistrationService.REGISTER_WITH_MYSERVER);
	            serviceIntent.putExtra(C2DMRegistrationService.REGISTRATION_ID, registrationID);
	            serviceIntent.putExtra(C2DMRegistrationService.PUSH_USERNAME, username);
	            Log.d(TAG,registrationID);
	            Log.d(TAG,username);
	            context.startService(serviceIntent);
        	}
        	
        	//We're all setup!
        	if (intent.getAction().equals(PUSH_SUCCESS))
        	{
        		//Mark us as logged in
                SharedPreferences settings = getPreferences(0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("logged_in", true);
                editor.commit();
                
        		//Move onto the configuration page
        		Intent configActivity = new Intent();
        		configActivity.setClass(context, ConfigActivity.class);
        		context.startActivity(configActivity);
        		finish();
        	}
        }
    };
    
    @Override
    protected void onResume() {
    	//Setup our Broadcast filters manually
        IntentFilter filter = new IntentFilter();
        filter.addAction(PUSH_READY);
        filter.addAction(PUSH_SUCCESS);
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
    	SharedPreferences settings = getPreferences(0);
    	//If we aren't logged in at the moment
    	if (!settings.getBoolean("logged_in", false))
    	{
	        setContentView(R.layout.main);
	        EditText usrTXT = (EditText)findViewById(R.id.usernameTXT);
	        usrTXT.setText(settings.getString("username", ""));
    	}
    	else
    	{
    		//Move onto the configuration page
    		Intent configActivity = new Intent();
    		configActivity.setClass(this, ConfigActivity.class);
    		startActivity(configActivity);
    		finish();
    	}
    }
    
    //Handles the button press event
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
    
    //Start the registration service with C2DM
    public void pushSetup()
    {
    	//Save our username
    	EditText usrTXT = (EditText)findViewById(R.id.usernameTXT);
        SharedPreferences settings = getPreferences(0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", usrTXT.getText().toString());
        editor.commit();
        
        super.startService(new Intent(C2DMRegistrationService.REGISTER_WITH_C2DM));
    }
    
    //Called upon incorrect login
    public void resetLoginBoxes()
    {
    	EditText usrTXT = (EditText)findViewById(R.id.usernameTXT);
    	EditText pwdTXT = (EditText)findViewById(R.id.passwordTXT);
    	
    	usrTXT.setText("");
    	pwdTXT.setText("");
    }
    
}