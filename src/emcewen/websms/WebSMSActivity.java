package emcewen.websms;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class WebSMSActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //super.startService(new Intent(C2DMRegistrationService.REGISTER_WITH_C2DM));
    }
    
    public void performLogin(View view)
    {
    	EditText usrTXT = (EditText)findViewById(R.id.usernameTXT);
    	String username = usrTXT.getText().toString();
    	
    	EditText pwdTXT = (EditText)findViewById(R.id.passwordTXT);
    	String password = pwdTXT.getText().toString();
    }
}