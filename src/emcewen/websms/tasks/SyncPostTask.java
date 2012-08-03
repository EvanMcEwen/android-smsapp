package emcewen.websms.tasks;

import java.io.IOException;

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

import emcewen.websms.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

public class SyncPostTask extends AsyncTask<JSONObject,Void,String> 
{	
	private static final String TAG = SyncPostTask.class.getSimpleName();
	public Context owner;
	public boolean inStatus;
	public boolean outStatus;
	public String inHash;
	public String outHash;
	public int totalNewIn = 0;
	public int totalNewOut = 0;
   	@Override
   	protected String doInBackground(JSONObject... params) {
   		HttpClient httpclient = new DefaultHttpClient();
           HttpPost httppost = new HttpPost("http://sms.evanmcewen.ca/messages");
           JSONObject json = new JSONObject();
           SharedPreferences settings = owner.getSharedPreferences("WebSMSActivity", 0);

           try {
               // Add your data
           	if (inStatus)
           	{
           		json.put("total_in_messages", totalNewIn);
           		json.put("in_messages", params[0]);
           	}
           	if (outStatus)
           	{
           		json.put("total_out_messages", totalNewOut);
           		json.put("out_messages", params[1]);
           	}
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
	        if (j.getInt("status") == 1)
	        {
	        	Log.d(TAG,"Success in SMS Sync");
	        	//Update hashes on server
        		UpdateHashTask updateHashTask = new UpdateHashTask();
        		updateHashTask.inStatus = inStatus;
        		updateHashTask.outStatus = outStatus;
        		updateHashTask.owner = this.owner;
        		updateHashTask.execute(inHash,outHash);
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
}