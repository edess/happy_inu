package edess.example.webviewimagecollection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	
	private Calendar today;
	private int[] noDays = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

private int selectedMonth; private int selectedYear;
private List<Integer> dates = new ArrayList<Integer>();
private Context mContext;


public MyAdapter(Context context, int month, int year){
    mContext = context;
    today = Calendar.getInstance();
    selectedMonth = month;
    selectedYear = year;


    Calendar firstDayOfMonth = Calendar.getInstance();
    firstDayOfMonth.set(year,month,1);
    int dayOfWeek1 = firstDayOfMonth.get(Calendar.DAY_OF_WEEK);

    for(int j=1; j<dayOfWeek1;j++){
        dates.add(0);
    }

    int count = 0;
    if(year%4==0 && month==1){
        count = 29;
    }
    else{
        count=noDays[month];
    }

   for(int i=0; i<count;i++){
        dates.add(i+1);
    }
}

@Override
public int getCount() {
    return dates.size();
}

@Override
public Object getItem(int position) {
    return null;
}

@Override
public long getItemId(int position) {
    return 0;
}

@Override
public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.calendar_item, parent, false);
    }

    //get the views
    TextView dateText = (TextView) convertView.findViewById(R.id.date);
//      TextView uploadsText = (TextView) convertView.findViewById(R.id.uploads);

    int date = dates.get(position);
    if(date == 0 ){
        dateText.setText("");
    //    uploadsText.setText("");
    }
    else{
        dateText.setText(Integer.toString(date));
    }


    //if date is today, highlight it yellow
    if(date == today.get(Calendar.DATE) && selectedMonth == today.get(Calendar.MONTH) && selectedYear == today.get(Calendar.YEAR)){
        dateText.setBackgroundColor(Color.parseColor("#F2F5A9"));
    //    uploadsText.setBackgroundColor(Color.parseColor("#F2F5A9"));
    }

    //if uploadsText > 0, highlight date green


    return convertView;
}
}
