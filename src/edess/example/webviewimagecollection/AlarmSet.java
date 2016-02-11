package edess.example.webviewimagecollection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmSet extends ActionBarActivity {

    private int id=5;
    private TimePicker myTimePicker;
    private Long newTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_set);

        //set time picker to 24 hour mode
        myTimePicker = (TimePicker) findViewById(R.id.timePicker1);
       // myTimePicker.setIs24HourView(true);

        //get the id of clicked row - breakfast = 0, lunch = 1, dinner = 2
        Intent intent = getIntent();
        id = intent.getIntExtra("id",5);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.alarm_set, menu);
        return true;
    }

    public void updateAlarmTime(View view){
        //get user selected time
        //get the time from timepicker
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, myTimePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, myTimePicker.getCurrentMinute());
        newTime = calendar.getTimeInMillis();

        //save the new time
        //update the SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).edit();
        switch (id) {
            case 0:
                editor.putLong("Breakfast", newTime);
                editor.commit();
                break;

            case 1:
                editor.putLong("Lunch", newTime);
                editor.commit();
                break;

            case 2:
                editor.putLong("Dinner", newTime);
                editor.commit();
                break;

            default:
                break;
        }

        //go back to list of alarms and set alarm - all alarms are set in ListOfAlarms.class
        //go back to ListOfAllarms Activity
        Intent intent = new Intent(AlarmSet.this, ListOfAlarms.class);
        intent.putExtra("id", id);
        intent.putExtra("time", newTime);
        startActivity(intent);


        Toast.makeText(AlarmSet.this, "Setting alarm...", Toast.LENGTH_LONG).show();
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
