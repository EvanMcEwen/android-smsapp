package emcewen.websms.tasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class SyncTask extends AsyncTask<String,Void,String> 
{	
	private static final String TAG = SyncTask.class.getSimpleName();
	private String[] inBody;
	private String[] inNumber;
	private String[] inDate;
	private String[] outBody;
	private String[] outNumber;
	private String[] outDate;
	
	public Activity owner;
   	@Override
   	protected String doInBackground(String... params) {
   		HttpClient httpclient = new DefaultHttpClient();
           HttpPost httppost = new HttpPost("http://sms.evanmcewen.ca/synchash");
           JSONObject json = new JSONObject();
           SharedPreferences settings = owner.getBaseContext().getSharedPreferences("WebSMSActivity", 0);

           try {
               // Add your data
           	json.put("in_hash", getLatestHashInbox());
           	json.put("out_hash", getLatestHashOutbox());
           	json.put("username", settings.getString("username", "empty"));
           	StringEntity se = new StringEntity(json.toString());
           	se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
               httppost.setEntity(se);

               // Execute HTTP Post Request
               ResponseHandler<String> responseHandler = new BasicResponseHandler();
               String response = httpclient.execute(httppost, responseHandler);
               
               if(response!=null){
                   return response;
               }
           } catch (ClientProtocolException e) {
               // TODO Auto-generated catch block
           } catch (IOException e) {
               // TODO Auto-generated catch block
           } catch (JSONException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
   		return null;
   	}
   	protected void onPostExecute(String result)
   	{
   		try 
   		{
   			JSONObject j = new JSONObject(result);
	        Log.d(LoginTask.class.getSimpleName(), Integer.toString(j.getInt("status")));
	        if (j.getInt("status") == 1)
	        {
	        	Log.d(TAG,"Success in SMS Sync");
	        }
	        else
	        {
	        	Log.d(TAG,"Error has occured");
	        }
		} 
   		catch (JSONException e) 
		{
			e.printStackTrace();
		}
   	}
   	
   	private void grabSMS()
   	{
   		//Inbox
    	Uri uri = Uri.parse("content://sms/inbox");
    	Cursor c= owner.getBaseContext().getContentResolver().query(uri, null, null ,null,null);
    	owner.startManagingCursor(c);
    	
    	if (c.getCount() != 0)
    	{
	    	inBody = new String[c.getCount()];
	    	inNumber = new String[c.getCount()];
	    	inDate = new String[c.getCount()];
	    	                
	    	if(c.moveToFirst()){
	    	        for(int i=0;i<c.getCount();i++){
	    	                inBody[i]= c.getString(c.getColumnIndexOrThrow("body")).toString();
	    	                inNumber[i]=c.getString(c.getColumnIndexOrThrow("address")).toString();
	    	                inDate[i]=c.getString(c.getColumnIndexOrThrow("date")).toString();
	    	                c.moveToNext();
	    	        }
	    	}
    	}
    	else
    	{
    		inBody = null;
    		inNumber = null;
    		inDate = null;
    	}
    	c.close();
    	
    	//Outbox
    	uri = Uri.parse("content://sms/sent");
    	c= owner.getBaseContext().getContentResolver().query(uri, null, null ,null,null);
    	owner.startManagingCursor(c);
    	
    	if (c.getCount() != 0)
    	{
	    	outBody = new String[c.getCount()];
	    	outNumber = new String[c.getCount()];
	    	outDate = new String[c.getCount()];
	
	    	if(c.moveToFirst()){
	    	        for(int i=0;i<c.getCount();i++){
	    	                inBody[i]= c.getString(c.getColumnIndexOrThrow("body")).toString();
	    	                inNumber[i]=c.getString(c.getColumnIndexOrThrow("address")).toString();
	    	                inDate[i]=c.getString(c.getColumnIndexOrThrow("date")).toString();
	    	                c.moveToNext();
	    	        }
	    	}
    	}
    	else
    	{
    		outBody = null;
    		outNumber = null;
    		outDate = null;
    	}
    	c.close();
   	}
   	
   	private String getLatestHashInbox()
   	{
   		if (inBody != null)
   			return md5(inBody[0]);
   		return "empty";
   	}
   	
   	private String getLatestHashOutbox()
   	{
   		if (outBody != null)
   			return md5(outBody[0]);
   		return "empty";
   	}

   	public static String md5(String string) {
   	    byte[] hash;

   	    try {
   	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
   	    } catch (NoSuchAlgorithmException e) {
   	        throw new RuntimeException("Huh, MD5 should be supported?", e);
   	    } catch (UnsupportedEncodingException e) {
   	        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
   	    }

   	    StringBuilder hex = new StringBuilder(hash.length * 2);
   	    for (byte b : hash) {
   	        if ((b & 0xFF) < 0x10) hex.append("0");
   	        hex.append(Integer.toHexString(b & 0xFF));
   	    }
   	    return hex.toString();
   	}
   	
}