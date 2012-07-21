package emcewen.websms.services;

import emcewen.websms.tasks.NewSMSSendTask;
import emcewen.websms.tasks.NewSMSTask;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class SMSService extends IntentService {

    private static final String TAG = SMSService.class.getSimpleName();
    public static final String NEW_SMS = "emcewen.websms.SMSService.NEW_SMS_TO_SYNC";
    public static final String NEW_SMS_TO_SEND = "emcewen.websms.SMSService.NEW_SMS_TO_SEND";
    public static final String ORIGIN = "emcewen.websms.SMSService.SMS_ORIGIN";
    public static final String MESSAGE = "emcewen.websms.SMSService.SMS_MESSAGE";
    public static final String TIMESTAMP = "emcewen.websms.SMSService.SMS_TIMESTAMP";
    public static final String USERNAME = "emcewen.websms.SMSService.USERNAME";
    
    public SMSService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

        if (NEW_SMS.equals(action)) {
        	this.syncNewSMS(intent.getExtras());
        }
        else if (NEW_SMS_TO_SEND.equals(action)) {
        	this.sendNewSMS(intent.getStringExtra("msg_id"));
        }
    }
    
    private void sendNewSMS(String msg_id)
    {
    	Log.d(TAG,"Starting NEW SMS To Send Sync Task");
    	NewSMSSendTask smsSendTask = new NewSMSSendTask();
    	smsSendTask.owner = this;
    	smsSendTask.execute(msg_id);
    }
    
    private void syncNewSMS(Bundle bundle)
    {
    	Log.d(TAG,"Starting SMS Sync Task");
		NewSMSTask smsTask = new NewSMSTask();
		smsTask.owner = this;
		smsTask.execute(bundle.getString(ORIGIN),bundle.getString(MESSAGE),bundle.getString(TIMESTAMP),bundle.getString(USERNAME));
    }

}
