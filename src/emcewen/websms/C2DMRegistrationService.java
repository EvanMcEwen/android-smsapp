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
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class C2DMRegistrationService extends IntentService {
    private static final String TAG = C2DMRegistrationService.class.getSimpleName();

    public static final String REGISTRATION_ID = "registration_id";
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
            this.registerForPush(intent.getStringExtra(REGISTRATION_ID));
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
    
    private void registerForPush(String regID){
    	Log.d(TAG, "Register with WebApp!");
    	
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://sms.evanmcewen.ca/push_users");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("push_user[regID]", regID));
            nameValuePairs.add(new BasicNameValuePair("push_user[deviceID]", ((TelephonyManager)this.getSystemService(TELEPHONY_SERVICE)).getDeviceId().toString()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    	
    	//For now just unregister right away
    	//this.unregisterFromC2dm();
    }
}
