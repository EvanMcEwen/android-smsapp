package emcewen.websms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
	private static final String TAG = SMSReceiver.class.getSimpleName();
	
    public void onReceive(Context context, Intent intent) {
    	Bundle bundle = intent.getExtras();
    	SharedPreferences settings = context.getSharedPreferences("WebSMSActivity", 0);

    	if (bundle != null) {
    	        Object[] pdusObj = (Object[]) bundle.get("pdus");
    	        SmsMessage[] messages = new SmsMessage[pdusObj.length];

    	        // getting SMS information from Pdu.
    	        for (int i = 0; i < pdusObj.length; i++) {
    	                messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
    	        }

    	        for (SmsMessage currentMessage : messages) {
    	        	//Start the NewSMSService to send the SMS to the web app
    	        	Log.d(TAG,"Starting NewSMSService");
    	            Intent serviceIntent = new Intent(SMSService.NEW_SMS);
    	            serviceIntent.putExtra(SMSService.ORIGIN, currentMessage.getDisplayOriginatingAddress());
    	            serviceIntent.putExtra(SMSService.MESSAGE, currentMessage.getDisplayMessageBody());
    	            serviceIntent.putExtra(SMSService.TIMESTAMP, Long.toString(currentMessage.getTimestampMillis()));
    	            serviceIntent.putExtra(SMSService.USERNAME, settings.getString("username", "empty"));
    	            context.startService(serviceIntent);
    	        }
    	}
    }
}