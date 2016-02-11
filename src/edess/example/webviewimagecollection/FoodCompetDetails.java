package edess.example.webviewimagecollection;

import org.json.JSONArray;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class FoodCompetDetails extends ActionBarActivity {
	
	
	//TextView reponseTextView;
	private ListView getAllDetailsListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_compet_details);
		
		//reponseTextView=(TextView)findViewById(R.id.textViewReponse);
		getAllDetailsListView=(ListView)findViewById(R.id.getAllFoodsDetailsListView);
		
		new GetAllFoodDetails().execute(new ApiConnector());
		getAllDetailsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "Ready to upload image of XX", Toast.LENGTH_LONG).show();
				startActivity(new Intent(FoodCompetDetails.this, WebViewActivity.class));
			}
		});
		
	}

	private class GetAllFoodDetails extends AsyncTask<ApiConnector, Long, JSONArray>{

		@Override
		protected JSONArray doInBackground(ApiConnector... params) {
			// TODO Auto-generated method stub
			return params[0].makeGetRequest("http://160.16.101.96/~cedric-k/calorie/fetchDetails.php");
		}
		@Override
		protected void onPostExecute(JSONArray jsonArray) {
	         //setTextToTextview(jsonArray);
			setListAdapter(jsonArray);
	     }
		
	}

	
	public void setListAdapter(JSONArray jsonArray){
		
		this.getAllDetailsListView.setAdapter(new GetAllDetailsListViewAdapter(jsonArray, this));
		
		
	}
	


	
}
