package edess.example.webviewimagecollection;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

public class CalendarFood extends ActionBarActivity {
	
	CalendarView mCalendar; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_food);
		
		//initialization of the calendar
		initializeCalendar(); 
	}
	
	public void initializeCalendar(){
		// http://examples.javacodegeeks.com/android/core/widget/android-calendarview-example/
		
		mCalendar = (CalendarView)findViewById(R.id.calendarView1); 
		
		mCalendar.setShowWeekNumber(false); // set wheter to show the week number
		
		mCalendar.setOnDateChangeListener(new OnDateChangeListener() {
			
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
				// TODO Auto-generated method stub
				 Toast.makeText(getApplicationContext(), dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
			}
		});
	}

	
}
