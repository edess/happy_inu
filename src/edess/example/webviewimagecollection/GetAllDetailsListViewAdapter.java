package edess.example.webviewimagecollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GetAllDetailsListViewAdapter extends BaseAdapter {
	
	private JSONArray dataArray;
	private Activity activity;
	private static LayoutInflater inflater=null;
	
	public GetAllDetailsListViewAdapter(JSONArray jsonArray, Activity a){
		
		this.dataArray=jsonArray;
		this.activity=a; 
		
		inflater=(LayoutInflater)this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.dataArray.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// set up convert view if it is null
		ListCell cell;
		if(convertView==null)
		{
			convertView=inflater.inflate(R.layout.get_all_foods_details_list_view_cell, null);
			cell = new ListCell();
			cell.foodType=(TextView)convertView.findViewById(R.id.textView_FoodType);
			cell.numbNeedImg=(TextView)convertView.findViewById(R.id.textView_Still_Need);
			cell.numbAcceptImg=(TextView)convertView.findViewById(R.id.textView_Already_Accepted);
			cell.support=(TextView)convertView.findViewById(R.id.textView_Support);
			cell.imageFoodIcon=(ImageView)convertView.findViewById(R.id.imageView_FoodIcon);
			
			convertView.setTag(cell);
		}
		else
		{
			cell=(ListCell)convertView.getTag(); 
		}
		
		try
		{
			JSONObject jsonObject = this.dataArray.getJSONObject(position);
			cell.foodType.setText(jsonObject.getString("Food_Type"));
			cell.numbNeedImg.setText(jsonObject.getString("Numb_Need_images"));
			cell.numbAcceptImg.setText(jsonObject.getString("Numb_Accepted_images"));
			cell.support.setText(jsonObject.getString("Support"));
			
			String foodNameForIcon = jsonObject.getString("Food_Type");
			if(foodNameForIcon.equals("Ramen"))
			{
				cell.imageFoodIcon.setImageResource(R.drawable.ramen);
			}
			else if(foodNameForIcon.equals("Fried rice"))
			{
				cell.imageFoodIcon.setImageResource(R.drawable.fried_rice);
			}
			else if(foodNameForIcon.equals("Kake soba"))
			{
				cell.imageFoodIcon.setImageResource(R.drawable.kakesoba);
			}
			else if(foodNameForIcon.equals("Kake udon"))
			{
				cell.imageFoodIcon.setImageResource(R.drawable.kakeudon);
			}
			else if(foodNameForIcon.equals("Curry rice"))
			{
				cell.imageFoodIcon.setImageResource(R.drawable.curryrice);
			}
			else if(foodNameForIcon.equals("Oyakodon"))
			{
				cell.imageFoodIcon.setImageResource(R.drawable.oyakodon);
			}
			else if(foodNameForIcon.equals("Miso soup"))
			{
				cell.imageFoodIcon.setImageResource(R.drawable.miso_soup);
			}
			else if(foodNameForIcon.equals("Tamagodon"))
			{
				cell.imageFoodIcon.setImageResource(R.drawable.tamagodon);
			}
			else if(foodNameForIcon.equals("Rice"))
			{
				cell.imageFoodIcon.setImageResource(R.drawable.rice);
			}
			else if(foodNameForIcon.equals("Kitsune soba"))
			{
				cell.imageFoodIcon.setImageResource(R.drawable.kitsunesoba);
			}
			else if(foodNameForIcon.equals("Kitsune udon"))
			{
				cell.imageFoodIcon.setImageResource(R.drawable.kitsuneudon);
			}
			else if(foodNameForIcon.equals("Katsudon"))
			{
				cell.imageFoodIcon.setImageResource(R.drawable.katsudon);
			}
			else if(foodNameForIcon.equals("Curry udon"))
			{
				cell.imageFoodIcon.setImageResource(R.drawable.kareudon);
			}
			else if(foodNameForIcon.equals("Katsu curry"))
			{
				cell.imageFoodIcon.setImageResource(R.drawable.katsucurry);
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		
		return convertView;
	}
	
	private class ListCell{
		
		private TextView foodType;
		private TextView numbNeedImg;
		private TextView numbAcceptImg;
		private TextView support;
		private ImageView imageFoodIcon; 
	}

}
