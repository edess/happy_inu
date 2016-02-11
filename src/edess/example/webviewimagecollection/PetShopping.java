package edess.example.webviewimagecollection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") public class PetShopping extends ActionBarActivity {
	
	GridView grid_view; 
	TextView foodDescription; 
	private String PetFoodName[]={"Pro plan savor","sweet potato ", "Ami dog","ALPO", "Pedigree complete",
			"MAX","Cowboy cook","Biscotti","Pedigree joints","food name 10"}; 
	ImageView img_ofCashier;
	TextView myCur_Points; 
	int petFoodPrice[]={200,200,300,400,600,600,700,800,1000,1100}; // this array contains the price of the food in the same order as PetFoodName; 
	int mPointsNumPetShop=0; 
	String hasBoughtFood="Not yet"; 
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	SharedPreferences.Editor editor; 
	int previousSelectedPosition = 0;
	View previousSelectedView=null; 
	String emailAdress; 
	int fooditemprice=0; // to be send with the eamil address to the db for update of the column "points"
	long [] CheckPreviousDogFoodTime= new long[4]; 
	String previous_eat_time; 
	Calendar cal; 
	SimpleDateFormat sdf; 
	
	SharedPreferences settings;
    public static final String PREFS_FILE = "myPreferenceFiles";
    Toast toastMessage; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping);
		
		//1st sharedpref file for the user email and keep some settings
		settings= getApplicationContext().getSharedPreferences("myPreferenceFiles", MODE_PRIVATE);
		emailAdress =settings.getString("userEmail", "abcd@is.naist.jp"); 
		Log.d("emailuser in shop", ""+emailAdress); 
		
	    //getActionBar().setDisplayHomeAsUpEnabled(true);
		foodDescription=(TextView)findViewById(R.id.tvFoodDescr1); 
		
		sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
		 editor = sharedpreferences.edit();
		 
		 //editor.putString("fromPetWorldFisrtTime", "oui"); 
		 //editor.commit();
		//get the points before clearing the share pref files. 
		myCur_Points =(TextView)findViewById(R.id.tvCurrentPoints); 
		String points_currr= sharedpreferences.getString("pointsIndb", "rien"); 
		myCur_Points.setText(points_currr);
		
		if (!points_currr.equals("") && !points_currr.equals(".....")) {
        	mPointsNumPetShop = Integer.parseInt(points_currr);
     
        }
		
		//previous_eat_time = sharedpreferences.getString("lastEatingTime", "0"); 
		
        
		cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long nbrTimePetAte = sharedpreferences.getLong("nbrTimeDogAte", 0); 
        Log.e("1st time fromPetW", ""+nbrTimePetAte); 
        
        if(nbrTimePetAte==0){
        	previous_eat_time="2015-07-17 17:17:17";//set previous_eating time to a default value for the first time running by a new user
        	System.out.println("blabla bla "+previous_eat_time);
        }
        else {
        	previous_eat_time = sharedpreferences.getString("lastEatingTime", "0"); 
        	System.out.println("mamamamaam "+previous_eat_time);
        }

		grid_view = (GridView)findViewById(R.id.gridView1); 
		grid_view.setAdapter(new ImageAdapterForGridShop(this));
		//grid_view.setDrawSelectorOnTop(true);
		grid_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				
				//Toast.makeText(getBaseContext(), "clicked on "+ PetFoodName[position], Toast.LENGTH_LONG).show();
				DescrEachFood(position);
				
				//to put a selector around the view touched by the user 
				previousSelectedView=(View)grid_view.getChildAt(previousSelectedPosition); 
				if(position!=previousSelectedPosition){
					
					previousSelectedView.setBackgroundColor(Color.TRANSPARENT);// get the previous selected item and remove the selector around him
					//Toast.makeText(getBaseContext(), "1st time selected", Toast.LENGTH_LONG).show();
					previousSelectedPosition = position;
					view.setBackgroundColor(Color.RED);
					//view.setSelected(true);
					previousSelectedView = view; 
				}
				else{
					long purT[] =  getTimeDiffPurch(previous_eat_time);
					Log.e("last PurchTime was in ", ""+purT[0]+"day"+purT[1]+"h "+purT[2]+"mins");
					System.out.println("papappappap "+previous_eat_time);
					
					if (purT[0]>=1 ){// if Purchdif hour is higher than 3 hours, allows purchase
						showBuyPopUp(position); // popUp to validate food item selection
						
						}
					else if(purT[1]>3){
						showBuyPopUp(position);
						
					}
					else if (purT[0]<1 || purT[1]<3){
						showAlertStillEating();
						//Toast.makeText(getApplicationContext(), "the blem is here", Toast.LENGTH_LONG).show();
					   
					}
					
				
				}
				
			}
		});
		
		/*grid_view.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "You selected "+ position, Toast.LENGTH_LONG).show();
				showBuyPopUp(position); // popUp to validate food item selection

				return false;
			}
		});*/
		
		ImageView exitShop = (ImageView)findViewById(R.id.imageViewExitShop); 
		exitShop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentExit = new Intent(PetShopping.this, PetWorld.class);
				intentExit.putExtra("boughtFood", "Yes");
				startActivity(intentExit);
				
			}
		});
		
		
        	
        
        	

	}
	
public void showBuyPopUp( int foodPoz){
    	
    	img_ofCashier= new ImageView(this);
    	img_ofCashier.setImageResource(R.drawable.icon_cashier);
		final int poz = foodPoz; // position of the pet food in the grid 
        PetShopping.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(PetShopping.this);
                builder.setTitle("Okay to buy "+PetFoodName[poz]+" ?");
                builder.setMessage("Your pet will really like it!! \n Price = "+petFoodPrice[poz]+"pts") 
                       .setCancelable(false)
                       .setView(img_ofCashier)
                       .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
               				int newPoints = mPointsNumPetShop-petFoodPrice[poz];
               		
        					
		               				if(newPoints>=0 ){
		                	            myCur_Points.setText(String.valueOf(newPoints));
		                   				
		                   				hasBoughtFood= "Yes"; 
		                   				//send price of the food item to the db to substract it from the existing points
		                   				fooditemprice= petFoodPrice[poz]; 
		                   				Log.d("price to send to db", ""+fooditemprice); 
		                   				new updatePointsMoneyInDB().execute(new ApiConnector());
		                   				
		                   			//sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
		                   				//SharedPreferences.Editor editor = sharedpreferences.edit();
		                   				editor.putString("Bought", "oui"); 
		                   				editor.putString("action","eating"); // what animated gif will be played
		                   				editor.putString("pointsIndb",String.valueOf(newPoints) ); // send the new number of point to PetWorld.java
		                   				
		                   				editor.putString("dogState", getResources().getString(R.string.dog_status_eat));// set the text of dog status/feelings
		                   				//editor.putString("from_pet_world", "yes"); 
		                            	//editor.putString("fromPetWorldFisrtTime", "no"); 
		                            	editor.commit();
		                   				
		        				        previous_eat_time = sdf.format(cal.getTime());
		        				        Log.e("previous_eat_time after buy", ""+previous_eat_time); 

		            					//startActivity(new Intent(PetShopping.this, PetWorld.class)); 
		        				        toastMessage= Toast.makeText(getApplicationContext(), R.string.toastdogEating, Toast.LENGTH_LONG); 
		        				        toastMessage.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 50);
		        				        toastMessage.getView().setBackgroundColor(Color.BLUE);
		        				        toastMessage.show();
		
		               					}
		               				
		               				else{
		               					toastMessage=Toast.makeText(getApplicationContext(), R.string.notEnoughPoints, Toast.LENGTH_LONG);
		               					toastMessage.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 50);
		        				        toastMessage.getView().setBackgroundColor(Color.RED);
		        				        toastMessage.show();
		
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

	protected void DescrEachFood(int foodGridPosition) {
		// TODO Auto-generated method stub
		
		if(foodGridPosition==0){
			foodDescription.setText("Adult Chicken & Brown Rice Recipe");
		}
		
		if(foodGridPosition==1){
			foodDescription.setText("ANGUS BEEF, WILD BOAR, LAMB, HERITAGE PORK AND BISON");
		}
		if(foodGridPosition==2){
			foodDescription.setText("RANCH-RAISED VENISON, BISON AND ELK, CAGE-FREE DUCK AND QUAIL, AND STEELHEAD TROUT.");
		}
		if(foodGridPosition==3){
			foodDescription.setText("A VARIED DIET OF FRESH WHOLE MEATS, WITH SMALL AMOUNTS OF FRESH FRUITS, BERRIES AND GRASSES.");
		}
		if(foodGridPosition==4){
			foodDescription.setText("LOADED WITH FRESH CAGE-FREE CHICKEN AND TURKEY, NEST-LAID EGGS AND WILD-CAUGHT FISH.");
		}
		if(foodGridPosition==5){
			foodDescription.setText("FOODS FROM FRESH REGIONAL INGREDIENTS.");
		}
		if(foodGridPosition==6){
			foodDescription.setText("FOR PUPPIES:RICH DIET OF FRESH WHOLE MEATS, WITH CALORIES AND MINERALS. ");
		}
		if(foodGridPosition==7){
			foodDescription.setText("SMALL BREED ADULT FISH & BROWN RICE RECIPE");
		}
		if(foodGridPosition==8){
			foodDescription.setText("FOR PUPPY: LAMB & OATMEAL RECIPE");
		}
		
	}
	
	private class updatePointsMoneyInDB extends AsyncTask<ApiConnector, Long, JSONArray>{

		@Override
		protected JSONArray doInBackground(ApiConnector... params) {
			// TODO Auto-generated method stub
			return params[0].makeGetRequest("http://160.16.101.96/~cedric-k/calorie/infoPointsMoney.php?email="+emailAdress+"&itemprice="+fooditemprice+"&friendshipprice=0");

		}
		@Override
		protected void onPostExecute(JSONArray jsonArray) {
	         //setTextToTextview(jsonArray);
			//setListAdapter(jsonArray);
			Log.d("request succeed", ""+jsonArray); 
			
			//pointAndMoney(jsonArray);
	     }
		
	}
	
	public long [] getTimeDiffPurch (String previous_eat_time){
		//get the previous eating time and process it to avoid multiple purchase
				
				Log.e("previous_eat_time", ""+previous_eat_time); 
				Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        String CurSysTime = sdf.format(cal.getTime()); // get the system current time
		        Log.d("sys. curr time", ""+CurSysTime);
		        
		        try {
					Date sysTimeCur = sdf.parse(CurSysTime);
					Date last_dog_eating=sdf.parse(previous_eat_time);
					long diffTime = Math.abs(sysTimeCur.getTime() - last_dog_eating.getTime()); // get the diff between current systTime and previous dog meal time, in sdf format
			        Log.d("difference between curTime and eating time", ""+diffTime);
			        
			        //convert the diff time in days, hours, min, sec
			        long secondsInMilli = 1000;
			        long minutesInMilli = secondsInMilli * 60;
			        long hoursInMilli = minutesInMilli * 60;
			        long daysInMilli = hoursInMilli * 24;
			 
			        long elapsedDays = diffTime / daysInMilli;
			        diffTime = diffTime % daysInMilli;
			 
			        long elapsedHours = diffTime / hoursInMilli;
			        diffTime = diffTime % hoursInMilli;
			 
			        long elapsedMinutes = diffTime / minutesInMilli;
			        diffTime = diffTime % minutesInMilli;
			 
			        long elapsedSeconds = diffTime / secondsInMilli;
			 
			        System.out.printf("%d days, %d hours, %d minutes, %d seconds%n", elapsedDays,elapsedHours, elapsedMinutes, elapsedSeconds);
			        long [] lastFoodwasAt = new long [] {elapsedDays,elapsedHours, elapsedMinutes, elapsedSeconds}; // put d, h, m, s in a array for analysis
			        for(int i=0; i<lastFoodwasAt.length; i++){
			        	System.out.println(lastFoodwasAt[i]);
			        	CheckPreviousDogFoodTime[i] =lastFoodwasAt[i]; 
			        	}
					//Log.e("time elapsed since last meal 2nd ", ""+CheckPreviousDogFoodTime[0]+" days: "+CheckPreviousDogFood[1]+"h "+CheckPreviousDogFood[2]+"mins "+CheckPreviousDogFood[3]+"sec");
					
					String unTextBizT = ""+CheckPreviousDogFoodTime[0]+"day(s) "+CheckPreviousDogFoodTime[1]+"h "+CheckPreviousDogFoodTime[2]+"mins "+CheckPreviousDogFoodTime[3]+"sec";
					Log.d("CheckPrevious_DogFoodTime", unTextBizT);
					//timeSinceLastFeeding.setText(unTextBiz);
					//changePetGifImage(CheckPreviousDogFood[0], CheckPreviousDogFood[1]); 
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		       return CheckPreviousDogFoodTime;  
	}
	
	
	public void showAlertStillEating(){
		final ImageView imageOfSorryFace= new ImageView(this);
    	imageOfSorryFace.setImageResource(R.drawable.icon_sorry);
        PetShopping.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(PetShopping.this);
                builder.setTitle(R.string.sorryPurchTitle);
                builder.setMessage(R.string.sorryPurch)  
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
		getMenuInflater().inflate(R.menu.shopping, menu);
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
