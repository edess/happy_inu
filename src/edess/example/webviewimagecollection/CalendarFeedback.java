package edess.example.webviewimagecollection;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.view.View;
import java.util.Calendar;
import java.util.Locale;
import android.view.ViewGroup;


public class CalendarFeedback extends ActionBarActivity {

	    private static final String[] days = new String[]{
            "S(日)", "M(月)", "T(火)", "W(水)","Th(木)", "F(金)", "S(土)"
    };

    private int selectedMonth;
    private int selectedYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_feedback);

        //populate the labels grid with days of the week
        GridView labels = (GridView) findViewById(R.id.calendarLabels);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.calendar_labels, R.id.day, days);
        labels.setAdapter(adapter1);

        //setup rest of calendar - month name and dates
        Calendar date = Calendar.getInstance();
        selectedMonth = date.get(Calendar.MONTH);
        selectedYear = date.get(Calendar.YEAR);
        setupGridView(date);
    }

    public void setupGridView(Calendar date){
        //set the month name
        String monthName = date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)+" "+Integer.toString(date.get(Calendar.YEAR));
        TextView monthNameView = (TextView)findViewById(R.id.monthName);
        monthNameView.setText(monthName);

        //setup the dates
        final int month = date.get(Calendar.MONTH);
        final int year = date.get(Calendar.YEAR);
        GridView gridView = (GridView) findViewById(R.id.calendarview);
        MyAdapter adapter = new MyAdapter(this, month, year);
        gridView.setAdapter(adapter);

        //add on click listener
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //selected date
                String date = ((TextView) v.findViewById(R.id.date)).getText().toString();

                if(!date.equals("")){
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear,selectedMonth,Integer.parseInt(date));
                    Intent intent = new Intent(CalendarFeedback.this, CalendarActivity.class);
                    String data = Integer.toString(year)+"-"+Integer.toString(month+1)+"-"+date;
                    intent.putExtra("date",data);
                    startActivity(intent);
                }
            }
        });
    }

    public void previousMonth(View view){
        //subtract 1 month from current selected month
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,selectedMonth);
        calendar.set(Calendar.YEAR,selectedYear);
        calendar.add(Calendar.MONTH, -1);

        //update selectedMonth and selectedYear
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedYear = calendar.get(Calendar.YEAR);

        //update month name in textview and calendar gridview
        setupGridView(calendar);
    }

    public void nextMonth(View view){
        //add 1 month from current selected month
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,selectedMonth);
        calendar.set(Calendar.YEAR,selectedYear);
        calendar.add(Calendar.MONTH, 1);

        //update selectedMonth and selectedYear
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedYear = calendar.get(Calendar.YEAR);

        //update month name in textview and calendar gridview
        setupGridView(calendar);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendar_feedback, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
