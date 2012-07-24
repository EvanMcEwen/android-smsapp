package emcewen.websms.receivers;

import java.util.Calendar;

import emcewen.websms.tasks.SyncTask;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	public AlarmReceiver() {}
	
    public AlarmReceiver(Activity context, int timeoutInMiliseconds){
        AlarmManager alarmMgr = 
            (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, 
            PendingIntent.FLAG_UPDATE_CURRENT);
        
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), timeoutInMiliseconds,
                     pendingIntent);
    }
	
	@Override
	public void onReceive(Context context, Intent intent) {
		SyncTask syncTask = new SyncTask();
    	syncTask.owner = context;
		syncTask.execute();
	}

}
