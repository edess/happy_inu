package edess.example.webviewimagecollection;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Created by edith-lu on 10/27/15.
 */
public class RebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            //initialize alarm manager, create intents
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent myIntent = new Intent(context, AlarmReceiver.class);

            // Get the times from SharedPreferences and set them in alarm
            SharedPreferences prefs = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
            String keys[] = {"Breakfast","Lunch","Dinner"};
            Calendar calendar = Calendar.getInstance(); Calendar c= Calendar.getInstance();

            for(int i=0;i<3;i++){
                PendingIntent alarmIntent = PendingIntent.getService(context, i, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                c.setTimeInMillis(prefs.getLong(keys[i], 0L));
                calendar.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY)); calendar.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
                alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
            }

        }
    }
}
