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

import android.os.AsyncTask;
import android.util.Log;

public class NewSMSTask extends AsyncTask<String,Void,String> 
{	
	private static final String TAG = NewSMSTask.class.getSimpleName();
   	@Override
   	protected String doInBackground(String... params) {
   		HttpClient httpclient = new DefaultHttpClient();
           HttpPost httppost = new HttpPost("http://sms.evanmcewen.ca/messages");
           JSONObject json = new JSONObject();

           try {
               // Add your data
           	json.put("origin", params[0]);
           	json.put("message", params[1]);
           	json.put("timestamp", params[2]);
           	json.put("username", params[3]);
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
}