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

import emcewen.websms.WebSMSActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class LoginTask extends AsyncTask<String,Void,String> 
{
	private ProgressDialog dialog;
	public WebSMSActivity owner;
	
	 @Override
	 protected void onPreExecute() {
		 //Start a dialog box that tells the user we are working
	     this.dialog = ProgressDialog.show(owner, "Validating Credentials", "Please Wait...", true);
	 }

	
   	@Override
   	protected String doInBackground(String... params) {
   		HttpClient httpclient = new DefaultHttpClient();
           HttpPost httppost = new HttpPost("http://sms.evanmcewen.ca/mlogin");
           JSONObject json = new JSONObject();

           try {
               // Add your data
           	json.put("username", params[0]);
           	json.put("password", params[1]);
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
   		//Get rid of the dialog box
    	this.dialog.cancel();
   		try 
   		{
   			JSONObject j = new JSONObject(result);
	        Log.d(LoginTask.class.getSimpleName(), Integer.toString(j.getInt("valid")));
	        if (j.getInt("valid") == 1)
	        	owner.pushSetup();
	        else
	        {
	        	//Login information incorrect
	    		Toast toast = Toast.makeText(owner, "Your login information was incorrect. Please try again!", Toast.LENGTH_LONG);
	    		toast.show();
	    		owner.resetLoginBoxes();
	        }
		} 
   		catch (JSONException e) 
		{
			e.printStackTrace();
		}
   	}
}