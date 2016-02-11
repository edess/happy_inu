package edess.example.webviewimagecollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class StatisticInfo extends ActionBarActivity {
	
	public static final String MyPREFERENCES = "MyPrefs" ;
	 SharedPreferences sharedpreferences;
	 
	 public static final String PREFS_FILE = "myPreferenceFiles";
	 SharedPreferences settings;
	 
	 long nb_time_dog_ate=0; 
	 String last_time_dog_meal= "1999-11-12 10:10:09"; 
	 long nb_time_dog_played=0; 
	 String last_time_dog_played ="1995-11-12 10:10:09"; 
	 long nb_time_dog_walked=0; 
	 String last_time_dog_walk ="1995-11-12 10:10:09"; 
	 
	 String emailadd="abcdefg@is.naist.jp"; 
	 String numb_of_uploaded_img="0"; 
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistic_info);
		
        sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        nb_time_dog_ate =  sharedpreferences.getLong("nbrTimeDogAte", 0); 
        last_time_dog_meal = sharedpreferences.getString("lastEatingTime","2000-11-12 10:10:09");
        nb_time_dog_played =  sharedpreferences.getLong("nbrTimeDogplayed", 0); 
        last_time_dog_played = sharedpreferences.getString("lastPlayingTime","2000-11-12 10:10:09");
        nb_time_dog_walked = sharedpreferences.getLong("nbrTimeDogWalk", 0); 
        last_time_dog_walk = sharedpreferences.getString("lastWalkingTime","2000-11-12 10:10:09");
        
        settings= getApplicationContext().getSharedPreferences("myPreferenceFiles", MODE_PRIVATE);
        emailadd =settings.getString("userEmail", "abcd@is.naist.jp");
        
        new GetNumbOfUploadedImages().execute(new ApiConnector());
		
		//populateListView();
	}
	
	

	private void populateListView() {
		// create list items . later get it from string.xml
		
		String[] sub_title={"email address", "Nbr of uploaded pics", "Nbr times pet ate", "Last feeding time", "Nbr time played with friend", "Last time with friend",
				"Nbre time of outside walk", "Last time of outside walking"}; 
		String[] value_for_sbTitle={emailadd, // user email address
				numb_of_uploaded_img, // nbs of uploaded pictures
				""+nb_time_dog_ate, // nb of time the pet ate smthg
				last_time_dog_meal,// date & time of last food of the dog 
				""+nb_time_dog_played, // nb of time dog played with other dogs
				last_time_dog_played, // date & time of dog last playing with friend session
				""+nb_time_dog_walked, //nb of time dog went outside for a walk
				last_time_dog_walk // last time dog has an outside walking
				};
		
		//build adapter
		// our adapter instance
			    
	    CustomListAdapterStat adapter=new CustomListAdapterStat(this, sub_title, value_for_sbTitle);
	
		//configure the list view
	    ListView listStat = (ListView)findViewById(R.id.lvStatistic);
	    listStat.setAdapter(adapter);
		
	}
	
    private class GetNumbOfUploadedImages extends AsyncTask<ApiConnector, Long, JSONArray>{

		@Override
		protected JSONArray doInBackground(ApiConnector... params) {
			// TODO Auto-generated method stub
			return params[0].makeGetRequest("http://160.16.101.96/~cedric-k/calorie/nbOfUploadImages.php?email="+emailadd);
		}
		@Override
		protected void onPostExecute(JSONArray jsonArray) {
	         //setTextToTextview(jsonArray);
			//setListAdapter(jsonArray);
			Log.d("request of uploaded_image succeed", ""+jsonArray); 
			//numb_of_uploaded_img=jsonArray.toString();
			//System.out.println("nbre d'image uploader ="+numb_of_uploaded_img);
			
			uploadedImages(jsonArray);
			populateListView();
	     }
		
		private void uploadedImages(JSONArray jsonArray) {
			// TODO Auto-generated method stub
			
			try {
				/*
				 * create the JsonObject of the JsonArray received from ApiConnector. normally this is an array with one object,so 
				 * we put (0) as parameter for jsonArray.getJSONObject(0);
				*/
				JSONObject jsobj = jsonArray.getJSONObject(0);  
				numb_of_uploaded_img= jsobj.getString("COUNT(*)");
				System.out.println("nbre d'image uploader = "+numb_of_uploaded_img);
				 

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.statistic_info, menu);
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
