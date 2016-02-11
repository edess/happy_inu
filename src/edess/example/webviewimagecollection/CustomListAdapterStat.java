package edess.example.webviewimagecollection;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListAdapterStat extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] sub_title;
	private final String[] value_for_sbTitle;

	public CustomListAdapterStat(Context context, String[] sub_title,String[] value_for_sbTitle) {
		super(context, R.layout.list_view_each_stat,sub_title);
		// TODO Auto-generated constructor stub
		
		this.context= (Activity) context; 
		this.sub_title=sub_title;
		this.value_for_sbTitle=value_for_sbTitle;
	}
	public View getView(int position,View view,ViewGroup parent) {
		LayoutInflater inflater=context.getLayoutInflater();
		View rowView=inflater.inflate(R.layout.list_view_each_stat, null,true);
		
		TextView Subtitle = (TextView) rowView.findViewById(R.id.tvSubTitle);
		TextView ValueSubTitle = (TextView) rowView.findViewById(R.id.tvValueSubTitle);
		
		Subtitle.setText(sub_title[position]);
		ValueSubTitle.setText(value_for_sbTitle[position]);

		
		
		return rowView;
		
	};

}
