package edess.example.webviewimagecollection;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") public class PetCompanions extends ActionBarActivity {
	
	
	String[] friendDogsName={"Doggy", "Achicko", "Lucy", "Zoe","Max","Rocky","Jack","Toby","Molly","Murphy"}; 
	int[] friendDogsPriceNum={100, 200, 300, 400,500,600,700,800,900,1000}; // should be the same value as in the array of string friendDogsPrice

	TextView myCur_points_inCompan; 
	TextView wall_in_comp; 

	SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "MyPrefs" ;
	int myPointsNumCompgn=0; 
	int friendship_price=0; 
	SharedPreferences settings;
    public static final String PREFS_FILE = "myPreferenceFiles";
    String emailAdress; 
    long userProgress=0;
    
    Calendar calend;
    SimpleDateFormat sdf; 
    String previous_Playing_Time; 
    getTimeDiffFromLastPurch gtdflPurch; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_companions);
		
		settings= getApplicationContext().getSharedPreferences("myPreferenceFiles", MODE_PRIVATE);
		emailAdress =settings.getString("userEmail", "abcd@is.naist.jp"); 
		
		populateListView();
		
		sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
		//get the level of current player
		userProgress = sharedpreferences.getLong("nbrTimeForLevel", 0); 
		System.out.println("userProgress = "+userProgress );
		
		// get the points and money values from the shared pref file
		myCur_points_inCompan =(TextView)findViewById(R.id.tvPointsInCompan); 
		String points_currrC= sharedpreferences.getString("pointsIndb", "rien"); 
		myCur_points_inCompan.setText(points_currrC);
		
		wall_in_comp = (TextView)findViewById(R.id.tvWalletInCompan);
		String wall_currr= sharedpreferences.getString("inWallet", "queDalle"); 
		wall_in_comp.setText(wall_currr);
		
		//get the integer value of points for purchases
		if (!points_currrC.equals("") && !points_currrC.equals(".....")) {
			myPointsNumCompgn = Integer.parseInt(points_currrC);
     
        }
		else{
			Log.d("check string value of ", "myPointsNumComp"); 
		}
		
		
		/*
		Intent iin= getIntent();
	    Bundle b = iin.getExtras();
	 
	        if(b!=null)
	        { 
	            String j =(String) b.getString("lesPoint","0");
	           // int edess = getIntent().getIntExtra("lesPoint", 0); 
	            //String edess = getIntent().getStringExtra("lesPoint"); 
	            myCur_points_inCompan.setText(j);
	        }*/
		calend = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long nbrTimePet_withFriend = sharedpreferences.getLong("nbrTimeDogplayed", 0); 
        Log.e("1st time fromPetW", ""+nbrTimePet_withFriend); 
		
		if(nbrTimePet_withFriend==0){
			previous_Playing_Time="2015-07-17 17:17:17";//set previous_eating time to a default value for the first time running by a new user
        	System.out.println("tatatatat "+previous_Playing_Time);
        }
        else {
        	previous_Playing_Time = sharedpreferences.getString("lastPlayingTime", "0"); 
        	System.out.println("bibibibibi "+previous_Playing_Time);
        }
		
		 gtdflPurch= new getTimeDiffFromLastPurch(); // the class I have created form computing time difference between purchases
		
		
		
	}

	private void populateListView() {
		// create list of item
		
		Integer[]img_id={R.drawable.golden_retr, R.drawable.pitbull, R.drawable.chihuahua,R.drawable.poodle,
				R.drawable.german_sh,R.drawable.bulldog,R.drawable.boxer_dog,R.drawable.doberman,
				R.drawable.cavalier, R.drawable.alaskanmalamutes};// id of the image each available dog friend
		
		
		String[]friend_age={"2","5","4","1","3","2","5","2","1","4"};
		String[] friendDogsBreed={"G.Retriever", "Pitbull", "Chihuahua", "Poodle","German S.","Bulldog","Boxer","Doberman","Cavalier","Alaskan"}; 
		String[] friendDogsPrice={"100", "200", "300", "400","500","600","700","800","900","1000"}; 
		String[] friendDogsSex={"M", "M", "F", "F","M","M","M","M","F","M"};
		
	
		//build adapter
		// our adapter instance
	    
	    CustomListAdapterDogFr adapter=new CustomListAdapterDogFr(this, friendDogsName, img_id,friend_age,friendDogsBreed, friendDogsPrice,friendDogsSex );
	
		//configure the list view
	    ListView listDogCompgn = (ListView)findViewById(R.id.listViewComp);
	    listDogCompgn.setAdapter(adapter);
	    listDogCompgn.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "You clicked on "+position, Toast.LENGTH_LONG).show();

				long purT[] =  gtdflPurch.TimeDiffFromLastPurch(previous_Playing_Time);
				Log.e("last PurchTime was in ", ""+purT[0]+"day"+purT[1]+"h "+purT[2]+"mins");
				System.out.println("papappappap "+previous_Playing_Time);
				
				if (purT[0]>1){ // value of day should be higher than 1: meaning the user can't buy(get) friends for his pet 2 times in the same day
					showBuyFriendsPopUp(position);
				}
				else{
					showAlertAlreadyHaveFriend(); 
				}
				
			}
		});

	}
	
public void showBuyFriendsPopUp( int foodPoz){
    	
    	final ImageView img_ofPlayingTogth= new ImageView(this);
    	img_ofPlayingTogth.setImageResource(R.drawable.playing_together_2);
		final int poz = foodPoz; // position of the pet food in the grid 
        PetCompanions.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(PetCompanions.this);
                builder.setTitle(R.string.callPetFriendTitle+friendDogsName[poz]+" ?");
                builder.setMessage(R.string.callPetFriendMsg+friendDogsName[poz])  
                       .setCancelable(false)
                       .setView(img_ofPlayingTogth)
                       .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
               				int newPoints = myPointsNumCompgn - friendDogsPriceNum[poz];
               				if(newPoints>=0){
               					myCur_points_inCompan.setText(String.valueOf(newPoints));
               					Toast.makeText(getApplicationContext(), "Thanks you! We will send "+friendDogsName[poz]+" soon", Toast.LENGTH_LONG).show();

                   				//hasBoughtFood= "Yes"; 
                   				
                   				//send price of the food item to the db to substract it from the existing points
                   				friendship_price= friendDogsPriceNum[poz]; 
                   				Log.d("price of friend to send to db", ""+friendship_price); 
                   				new updatePointsMoneyInDB().execute(new ApiConnector());
                   				
                   			//sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                   				SharedPreferences.Editor editor = sharedpreferences.edit();
                   				editor.putString("Bought", "oui"); 
                   				editor.putString("action", "withfriend"); // what animated gif will be played
                   				editor.putString("pointsIndb",String.valueOf(newPoints) ); // send the new number of point to PetWorld.java
                   				editor.putString("dogState", getResources().getString(R.string.dog_status_not_alone)+" "+friendDogsName[poz]);// set the text of dog status/feelings
                   				//editor.putString("from_pet_world", "yes"); 
                   				editor.commit();
                   				if(userProgress>=4 && userProgress<=10){
                   					editor.putString("actionL2", "withfriend");
                   					editor.commit();
                   				}
                   				/*else{
                   					Log.d("which level", "not level 2"); 
                   				}*/
                   				

               					}
               				else{
                   				Toast.makeText(getApplicationContext(), "Sorry, you don't have enough points"
                   						+ " to buy this food.  ", Toast.LENGTH_LONG).show();

               				}
               				
                  				
                           }
                       })
                       .setNegativeButton("NO", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
                       
                	
                AlertDialog alert = builder.create();
                alert.show();               
            }
        });
    }

private class updatePointsMoneyInDB extends AsyncTask<ApiConnector, Long, JSONArray>{

	@Override
	protected JSONArray doInBackground(ApiConnector... params) {
		// TODO Auto-generated method stub
		return params[0].makeGetRequest("http://160.16.101.96/~cedric-k/calorie/infoPointsMoney.php?email="+emailAdress+"&friendshipprice="+friendship_price+"&itemprice=0&walkingprice=0");

	}
	@Override
	protected void onPostExecute(JSONArray jsonArray) {
         //setTextToTextview(jsonArray);
		//setListAdapter(jsonArray);
		Log.d("request succeed", ""+jsonArray); 
		
		//pointAndMoney(jsonArray);
     }
	
}

public void showAlertAlreadyHaveFriend(){
	final ImageView imageOfSorryFace= new ImageView(this);
	imageOfSorryFace.setImageResource(R.drawable.icon_sorry);
    PetCompanions.this.runOnUiThread(new Runnable() {
        public void run() {
            AlertDialog.Builder builder = new AlertDialog.Builder(PetCompanions.this);
            builder.setTitle(R.string.sorryPurchTitle);
            builder.setMessage(R.string.sorryCallFriend)  
                   .setCancelable(false)
                   .setView(imageOfSorryFace)
                   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                       }
                   });                     
            AlertDialog alert = builder.create();
            alert.show();               
        }
    });
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.companions, menu);
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
