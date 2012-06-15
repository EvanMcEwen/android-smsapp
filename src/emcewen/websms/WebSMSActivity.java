package emcewen.websms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WebSMSActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        super.startService(new Intent(C2DMRegistrationService.REGISTER_WITH_C2DM));
    }
}