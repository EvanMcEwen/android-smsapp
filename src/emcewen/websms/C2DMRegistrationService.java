package emcewen.websms;

/*
 * Copyright (C) 2011, Marakana Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class C2DMRegistrationService extends IntentService {
    private static final String TAG = C2DMRegistrationService.class.getSimpleName();

    public static final String REGISTRATION_ID = "registration_id";
    public static final String PUSH_USERNAME = "emcewen.websms.PUSH_USERNAME";
    public static final String REGISTER_WITH_MYSERVER = "emcewen.websms.SERVER_REGISTER";
    public static final String REGISTER_WITH_C2DM = "emcewen.websms.C2DM_REGISTER";
    public static final String UNREGISTER_WITH_MYSERVER = "emcewen.websms.SERVER_UNREGISTER";
    public static final String UNREGISTER_WITH_C2DM = "emcewen.websms.C2DM_UNREGISTER";

    public C2DMRegistrationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        
        if (REGISTER_WITH_MYSERVER.equals(action)) {
            this.registerForPush(intent.getStringExtra(REGISTRATION_ID),intent.getStringExtra(PUSH_USERNAME));
        }
        else if (UNREGISTER_WITH_C2DM.equals(action)){
        	this.unregisterFromC2dm();
        }
        else if (REGISTER_WITH_C2DM.equals(action)){
        	this.registerToC2dm();
        }
        else if (UNREGISTER_WITH_MYSERVER.equals(action)){
        	this.unregisterForPush();
        }

    }

    private void unregisterForPush() {
    	Log.d(TAG,"Unregister with WebApp!");
    	
    	HttpClient httpclient = new DefaultHttpClient();
    	String deviceID = ((TelephonyManager)this.getSystemService(TELEPHONY_SERVICE)).getDeviceId().toString();
    	HttpDelete httpdelete = new HttpDelete("http://sms.evanmcewen.ca/push_users/" + deviceID);
    	
        try {
            // Add your data

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpdelete);
            
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
	}

	private void unregisterFromC2dm() {
            Log.d(TAG, "Unregistering from C2DM");
        Intent unregistrationIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
        unregistrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
        super.startService(unregistrationIntent);
    }
    
    private void registerToC2dm(){
    	Log.d(TAG,"Registering with C2DM");
        // create a C2DM registration intent
        Intent registrationIntent = new Intent(
          "com.google.android.c2dm.intent.REGISTER");
        // add to it our application's "signature"
        registrationIntent.putExtra("app", 
          PendingIntent.getBroadcast(this, 0, 
            new Intent(), 0));
        // role email that our app server will use
        // later to authenticate before it can use C2DM
        registrationIntent.putExtra("sender",   
          "sdnotifications@gmail.com");
        // request C2DM registration (async operation)
        super.startService(registrationIntent);
    }
    
    private void registerForPush(String regID, String username){
    	Log.d(TAG, "Register with WebApp!");
   		HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://sms.evanmcewen.ca/devices");
        JSONObject json = new JSONObject();
        TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(TELEPHONY_SERVICE);

        try {
            // Add your data
        	json.put("username", username);
        	json.put("reg_id", regID);
        	json.put("device_id", telephonyManager.getDeviceId());
        	json.put("nickname", "");
        	StringEntity se = new StringEntity(json.toString());
        	se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);

            // Execute HTTP Post Request
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = httpclient.execute(httppost, responseHandler);
            
            if(response!=null){
                Intent successIntent = new Intent(WebSMSActivity.PUSH_SUCCESS);
                super.sendBroadcast(successIntent);
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
