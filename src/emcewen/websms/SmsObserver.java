package emcewen.websms;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class SmsObserver extends ContentObserver {
	
    private Context mContext;
    
    private String contactId = "", contactName = "";
    private String smsBodyStr = "", phoneNoStr = "";
    private long smsDatTime = System.currentTimeMillis();
    static final Uri SMS_STATUS_URI = Uri.parse("content://sms");
    
	public SmsObserver(Handler handler, Context ctx) {
		super(handler);
		mContext = ctx;
	}

	public boolean deliverSelfNotifications() {
		return true;
	}

	public void onChange(boolean selfChange) {
		try{
			Log.e("Info","Notification on SMS observer");
			Cursor sms_sent_cursor = mContext.getContentResolver().query(SMS_STATUS_URI, null, null, null, null);
			if (sms_sent_cursor != null) {
				if (sms_sent_cursor.moveToFirst()) {
					String protocol = sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("protocol"));
					Log.e("Info","protocol : " + protocol);
					//for send  protocol is null
					if(protocol == null){
						/*
						String[] colNames = sms_sent_cursor.getColumnNames();		        		
						if(colNames != null){
							for(int k=0; k<colNames.length; k++){
								Log.e("Info","colNames["+k+"] : " + colNames[k]);
							}
						}
						*/
						int type = sms_sent_cursor.getInt(sms_sent_cursor.getColumnIndex("type"));
						Log.e("Info","SMS Type : " + type);
						// for actual state type=2
						if(type == 2){
							Log.e("Info","Id : " + sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("_id")));
							Log.e("Info","Thread Id : " + sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("thread_id")));
							Log.e("Info","Address : " + sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("address")));
							Log.e("Info","Person : " + sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("person")));
							Log.e("Info","Date : " + sms_sent_cursor.getLong(sms_sent_cursor.getColumnIndex("date")));
							Log.e("Info","Read : " + sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("read")));
							Log.e("Info","Status : " + sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("status")));
							Log.e("Info","Type : " + sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("type")));
							Log.e("Info","Rep Path Present : " + sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("reply_path_present")));
							Log.e("Info","Subject : " + sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("subject")));
							Log.e("Info","Body : " + sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("body")));
							Log.e("Info","Err Code : " + sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("error_code")));
							
							smsBodyStr = sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("body")).trim();
							phoneNoStr = sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("address")).trim();
							smsDatTime = sms_sent_cursor.getLong(sms_sent_cursor.getColumnIndex("date"));
							
							Log.e("Info","SMS Content : "+smsBodyStr);
							Log.e("Info","SMS Phone No : "+phoneNoStr);
							Log.e("Info","SMS Time : "+smsDatTime);
						}
					}
				}
			}
			else
				Log.e("Info","Send Cursor is Empty");
		}
		catch(Exception sggh){
			Log.e("Error", "Error on onChange : "+sggh.toString());
		}
		super.onChange(selfChange);
	}//fn onChange
	
}//End of class SmsObserver

