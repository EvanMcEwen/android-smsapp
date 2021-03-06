package emcewen.websms;

import emcewen.websms.services.C2DMRegistrationService;
import emcewen.websms.services.SMSService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class C2DMReceiver extends BroadcastReceiver {
    private static final String TAG = C2DMReceiver.class.getSimpleName();

    private static final String C2DM_DATA_ACTION = "action";

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
        	//Handle registration related activities
            handleRegistration(context, intent);
        } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
        	//received a PUSH message. Let's figure out what to do.
            handleMessage(context, intent);
        } else {
            Log.w(TAG, "Unexpected intent: " + intent);
        }
    }

    private void handleRegistration(Context context, Intent intent) {
        String registrationId = intent.getStringExtra("registration_id");
        String error = intent.getStringExtra("error");
        String unregistered = intent.getStringExtra("unregistered");
        if (error != null) {
        	//Some sort of error has occured with registering with the C2DM servers
            Log.w(TAG, "Registration failed: " + error);
        } else if (unregistered != null) {
        	//Needs implementation
        	//We have been unregistered from the C2DM servers
        	//So we should handle this on our servers accordingly
                Log.d(TAG, "Unregistered: " + unregistered);
            //context.startService(new Intent(C2DMRegistrationService.UNREGISTER_WITH_MYSERVER));
        } else if (registrationId != null) {
        	//We have a successful C2DM registration yay!
                Log.d(TAG, "Registered with registration ID [" + registrationId + "]");
                
            //Tell the main Activity that it can register with the web application
                Intent broadcast = new Intent(WebSMSActivity.PUSH_READY);
                broadcast.putExtra(C2DMRegistrationService.REGISTRATION_ID,registrationId);
                context.sendBroadcast(broadcast);
                Log.d(TAG, "Broadcast Sent: [" + WebSMSActivity.PUSH_READY + "]");
        } else {
                Log.d(TAG, "Other registration response: " + intent.getExtras());
        }
    }

    private void handleMessage(Context context, Intent intent) {
        Log.d(TAG, "Handling C2DM notification");
        
        Bundle extras = intent.getExtras();
        //We've received a push notification telling us theres an SMS ready to send
        if (extras.getString(C2DM_DATA_ACTION).equals("NEW_SMS_TO_SEND"))
        {
        	Intent serviceIntent = new Intent(SMSService.NEW_SMS_TO_SEND);
        	serviceIntent.putExtra("msg_id", extras.getString("msg_id"));
        	context.startService(serviceIntent);
        }
    }
}
