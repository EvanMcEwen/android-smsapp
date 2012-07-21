package emcewen.websms.tasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class UpdateHashTask extends AsyncTask<String,Void,String> 
{	
	private static final String TAG = UpdateHashTask.class.getSimpleName();
	public boolean inStatus;
	public boolean outStatus;
	public Activity owner;
   	@Override
   	protected String doInBackground(String... params) {
   		HttpClient httpclient = new DefaultHttpClient();
   		SharedPreferences settings = owner.getBaseContext().getSharedPreferences("WebSMSActivity", 0);
           HttpPut httpput = new HttpPut("http://sms.evanmcewen.ca/synchashes/1");
           JSONObject json = new JSONObject();

           try {
               // Add your data
           	json.put("in_hash", params[0]);
           	json.put("out_hash", params[1]);
           	json.put("in_status", inStatus);
           	json.put("out_status", outStatus);
           	json.put("username", settings.getString("username", "empty"));
           	StringEntity se = new StringEntity(json.toString());
           	se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
           	httpput.setEntity(se);

               // Execute HTTP Post Request
               ResponseHandler<String> responseHandler = new BasicResponseHandler();
               String response = httpclient.execute(httpput, responseHandler);
               
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
	        	Log.d(TAG,"Success in Hash Update");
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