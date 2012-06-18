package emcewen.websms;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class WebSMSActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //super.startService(new Intent(C2DMRegistrationService.REGISTER_WITH_C2DM));
    }
    
    public void performLogin(View view) throws JSONException
    {
    	EditText usrTXT = (EditText)findViewById(R.id.usernameTXT);
    	String username = usrTXT.getText().toString();
    	
    	EditText pwdTXT = (EditText)findViewById(R.id.passwordTXT);
    	String password = pwdTXT.getText().toString();
    	
    	if (password.equals("") || username.equals(""))
    	{
    		Toast toast = Toast.makeText(getApplicationContext(), "Please enter your login information", Toast.LENGTH_SHORT);
    		toast.show();
    	}
    	else
    	{
    		new LoginTask().execute(username,password);
    	}
    }
    
    private class LoginTask extends AsyncTask<String,Void,String> 
    {
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
        Log.d(LoginTask.class.getSimpleName(), result);
   	}
    }
    
    
}