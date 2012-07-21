package emcewen.websms.tasks;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import emcewen.websms.services.SMSService;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;

public class NewSMSSendTask extends AsyncTask<String,Void,String> 
{	
	private static final String TAG = NewSMSSendTask.class.getSimpleName();
	public SMSService owner;
   	@Override
   	protected String doInBackground(String... params) {
   		HttpClient httpclient = new DefaultHttpClient();
           HttpGet httpget = new HttpGet("http://sms.evanmcewen.ca/outmessages/" + params[0]);

           try {
	           // Execute HTTP Post Request
	           ResponseHandler<String> responseHandler = new BasicResponseHandler();
	           String response = httpclient.execute(httpget, responseHandler);
	           
	           if(response!=null){
	               return response;
	           }
           } catch (ClientProtocolException e) {
               // TODO Auto-generated catch block
           } catch (IOException e) {
               // TODO Auto-generated catch block
           }
   		return null;
   	}
   	protected void onPostExecute(String result) 
   	{
   		try 
   		{
   			JSONObject j = new JSONObject(result);
	        Log.d(TAG, j.getString("message"));
        	Log.d(TAG,"Success in SMS Sync");

        	SmsManager smsMgr = SmsManager.getDefault();
        	ArrayList<String> messages = smsMgr.divideMessage(j.getString("message"));
        	final String SMS_ADDRESS_PARAM="SMS_ADDRESS_PARAM";
        	final String SMS_DELIVERY_MSG_PARAM="SMS_DELIVERY_MSG_PARAM";
        	final String SMS_SENT_ACTION="com.tilab.msn.SMS_SENT";
        	        ArrayList<PendingIntent> listOfIntents = new ArrayList<PendingIntent>();
        	        for (int i=0; i < messages.size(); i++){
        	                Intent sentIntent = new Intent(SMS_SENT_ACTION);
        	                sentIntent.putExtra(SMS_ADDRESS_PARAM, j.getString("destination"));
        	                sentIntent.putExtra(SMS_DELIVERY_MSG_PARAM, (messages.size() > 1)? "Part " +  i + " of SMS " : "SMS ");
        	                PendingIntent pi = PendingIntent.getBroadcast(owner.getBaseContext(), 0, sentIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        	                listOfIntents.add(pi);
        	        }
        	        
        	        smsMgr.sendMultipartTextMessage(j.getString("destination"), null, messages, listOfIntents, null);                          
        	ContentValues values = new ContentValues();
        	values.put("address", j.getString("destination"));
        	values.put("body", j.getString("message"));
        	owner.getBaseContext().getContentResolver().insert(Uri.parse("content://sms/sent"), values);
        	
        	UpdateHashTask updateHashTask = new UpdateHashTask();
        	updateHashTask.owner = this.owner;
        	updateHashTask.inStatus = false;
        	updateHashTask.outStatus = true;
        	updateHashTask.execute("",md5(j.getString("message")));
        	
		} 
   		catch (JSONException e) 
		{
			e.printStackTrace();
		}
   	}
   	
   	private String md5(String s) {
   	    try {
   	        // Create MD5 Hash
   	        MessageDigest digest = java.security.MessageDigest
   	                .getInstance("MD5");
   	        digest.update(s.getBytes());
   	        byte messageDigest[] = digest.digest();
   	 
   	        // Create Hex String
   	        StringBuffer hexString = new StringBuffer();
   	        for (int i = 0; i < messageDigest.length; i++) {
   	            String h = Integer.toHexString(0xFF & messageDigest[i]);
   	            while (h.length() < 2)
   	                h = "0" + h;
   	            hexString.append(h);
   	        }
   	        return hexString.toString();
   	 
   	    } catch (NoSuchAlgorithmException e) {
   	        e.printStackTrace();
   	    }
   	    return "";
   	}
   	
}