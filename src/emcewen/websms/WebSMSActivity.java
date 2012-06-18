package emcewen.websms;

import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class WebSMSActivity extends Activity {
    /** Called when the activity is first created. */
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
        //super.startService(new Intent(C2DMRegistrationService.REGISTER_WITH_C2DM));
    }
    
    public void resetLoginBoxes()
    {
    	EditText usrTXT = (EditText)findViewById(R.id.usernameTXT);
    	EditText pwdTXT = (EditText)findViewById(R.id.passwordTXT);
    	
    	usrTXT.setText("");
    	pwdTXT.setText("");
    }
    
}