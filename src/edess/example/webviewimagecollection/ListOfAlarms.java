package edess.example.webviewimagecollection;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.os.SystemClock;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListOfAlarms extends ActionBarActivity {
    private ListView  mainListView;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent; private PendingIntent alarmIntentLunch; private PendingIntent alarmIntentDinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_alarms);

        //if first run (calling activity is AfterLogin), set alarms then populate listview
        if(getCallingActivity()!= null) {
            setInitialAlarms();
        }
        populateListView();

    }

    @Override
    public void onResume(){
        //create an alarm in alarm manager
        super.onResume();
        Intent theIntent = getIntent();
        //if the id ==5, the previous activity is not AlarmSet
        int id = theIntent.getIntExtra("id", 5);
        long time = theIntent.getLongExtra("time", 0L);
      //  Toast.makeText(this,Integer.toString(id),Toast.LENGTH_LONG).show();
        if(id != 5) {
            updateAlarm(id, time);
        }
        populateListView();
    }

    public void updateAlarm(int id, final long time){
        alarmMgr = (AlarmManager)ListOfAlarms.this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ListOfAlarms.this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getService(ListOfAlarms.this, id+1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        c.setTimeInMillis(time);
        int hour =  c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE,  minute);
        if(now.get(Calendar.HOUR_OF_DAY) > hour || (now.get(Calendar.HOUR_OF_DAY) == hour && now.get(Calendar.MINUTE)> minute)){
            calendar.add(Calendar.DATE,1);
        }
       // alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);


        /*  for debugging purposes
        String str = "Now: "+Integer.toString(now.get(Calendar.HOUR_OF_DAY))+":"+Integer.toString(now.get(Calendar.MINUTE));
        Toast.makeText(this,str,Toast.LENGTH_LONG).show();

        String str2 = "User time: "+Integer.toString(calendar.get(Calendar.HOUR_OF_DAY))+":"+Integer.toString(calendar.get(Calendar.MINUTE));
        Toast.makeText(this,str2,Toast.LENGTH_LONG).show();


        Toast.makeText(this,"Alarm set",Toast.LENGTH_SHORT).show();
        */
    }

    public void setInitialAlarms(){
        SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).edit();

        //create intents
        alarmMgr = (AlarmManager) ListOfAlarms.this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ListOfAlarms.this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getService(ListOfAlarms.this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmIntentLunch = PendingIntent.getService(ListOfAlarms.this, 2, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmIntentDinner = PendingIntent.getService(ListOfAlarms.this, 3, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar now = Calendar.getInstance();
        //set alarms using default times
        Calendar calendar = Calendar.getInstance();
        //Breakfast time
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);
        if(now.get(Calendar.HOUR_OF_DAY) > 8 || (now.get(Calendar.HOUR_OF_DAY) ==8 && now.get(Calendar.MINUTE)> 30)){
            calendar.add(Calendar.DATE,1);
        }
        long BreakfastTime = calendar.getTimeInMillis();
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, BreakfastTime, AlarmManager.INTERVAL_DAY, alarmIntent);
        editor.putLong("Breakfast", BreakfastTime); // value to store
        editor.commit();
        //Lunch time
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 30);
        if(now.get(Calendar.HOUR_OF_DAY) > 12 || (now.get(Calendar.HOUR_OF_DAY) ==8 && now.get(Calendar.MINUTE)> 30)){
            calendar.add(Calendar.DATE,1);
        }
        long LunchTime = calendar.getTimeInMillis();
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, LunchTime, AlarmManager.INTERVAL_DAY, alarmIntent);
        editor.putLong("Lunch", LunchTime); // value to store
        editor.commit();
        //Dinner time
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 30);
        if(now.get(Calendar.HOUR_OF_DAY) > 19 || (now.get(Calendar.HOUR_OF_DAY) == 8 && now.get(Calendar.MINUTE)> 30)){
            calendar.add(Calendar.DATE,1);
        }
        long DinnerTime = calendar.getTimeInMillis();
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, DinnerTime, AlarmManager.INTERVAL_DAY, alarmIntent);
        editor.putLong("Dinner", DinnerTime); // value to store
        editor.commit();
    }

    public void populateListView(){
        mainListView = (ListView) findViewById(R.id.myAlarmsList);
        final List<String> alarms = new ArrayList<String>();

        //get the alarm times
        new Thread() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
                String keys[] = {"Breakfast", "Lunch", "Dinner"};
                Calendar c = Calendar.getInstance();

                for (int i = 0; i < 3; i++) {
                    Long value = prefs.getLong(keys[i], 0L);

                    c.setTimeInMillis(value);
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);

                    String log;
                    if (hour < 10) {
                        log = keys[i] + ",  0" + Integer.toString(hour);
                    } else {
                        log = keys[i] + ",  " + Integer.toString(hour);
                    }
                    if (minute < 10) {
                        log += ": 0" + Integer.toString(minute);
                    } else {
                        log += ": " + Integer.toString(minute);
                    }

                    alarms.add(log);
                }
            }//end run
        }.start();//end thread

        // Binding ListAdapter
        mainListView.setAdapter(new ArrayAdapter<String>(this, R.layout.alarm_row, R.id.alarmName, alarms));

        //on click listener for list item
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openAlarmSet = new Intent(getApplicationContext(), AlarmSet.class);
                //get which list item has been clicked, so the correct time is updated
                openAlarmSet.putExtra("id", position);
                startActivity(openAlarmSet);
            }
        });

    }

    public void openAfterLogin(View view){
        Intent intent = new Intent(this, AfterLogin.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_of_alarms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
