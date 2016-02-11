package edess.example.webviewimagecollection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhi.gif.lib.AnimatedGifImageView;
import com.abhi.gif.lib.AnimatedGifImageView.TYPE;

import edess.example.webviewimagecollection.MyLocation.LocationResult;


public class AfterLogin extends ActionBarActivity {

	Button b;
	Button FoodsCompetInfo;
	Button UploadImage;
	ImageView usageOfScale; 
	ImageView imageOfFoodOnScale;
    //EditText et,pass;
	EditText et;
    TextView tv;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    SharedPreferences settings;
    public static final String PREFS_FILE = "myPreferenceFiles";
    Toast toastCorrectEmailPlease;
    double mLongitude=0;
	 double mLatitude=0; 
	 int mPointsNum=0; // number of points - numeric value
	 String emailAdress= "bla@bla.jp"; 
	 String thePreviouslySaveadress="anormal@error.ci"; 
	 
	 TextView myPointsinWelcome;
	 TextView myWallet; 
	 String cPointsValue; // the value to be send to the PetWorld 
	 String cInWallet="rien"; // the money inside the wallet
	 int fooditemprice=0; 
	 String comingBackFromPetWord ="no"; // check whether we come from petWord or MainActivty
	 
	 String preEatingTime="2015-10-15 10:30:02"; 
	long [] CheckPreviousDogFood= new long[4]; 
	TextView timeSinceLastFeeding; 
	
	AnimatedGifImageView animatedGifImageView; 
	 
	 public static final String MyPREFERENCES = "MyPrefs" ;
	 SharedPreferences sharedpreferences;
	 SharedPreferences.Editor editor; 
	 
	 TextView mPetMessage;
	 int totalnumbTime=0;
	 int inLevel=0;// game level
    
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
        
        //first time, ask user to set notification time
        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);

        // Code to run once
        if (isFirstRun)
        {
            ActivityCompat.startActivityForResult(this, new Intent(this, ListOfAlarms.class), 0, null);

            //update preferences that FIRSTRUN is noe false
            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("FIRSTRUN", false);
            editor.commit();
        }
    
        // get useEmail from previous activity with intent 
        emailAdress = getIntent().getStringExtra("useremailyyyy"); 
        if(emailAdress==null){
        	//emailAdress= thePreviouslySaveadress;
        	settings= getApplicationContext().getSharedPreferences("myPreferenceFiles", MODE_PRIVATE);
    		emailAdress =settings.getString("userEmail", "abcd@is.naist.jp"); 
        }
        //Toast.makeText(getApplicationContext(), "email pass or not"+emailAdress, Toast.LENGTH_LONG).show();
        
        
        
        
        sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        //sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE); // the pref file for everything in the Pet World
        /*SharedPreferences.Editor editorDgState = sharedpreferences.edit();
		String dogStatHello= getResources().getString(R.string.dog_status_hello); 
		editorDgState.putString("dogState",dogStatHello ); 
		editorDgState.commit(); */
		
		
		 mPetMessage = (TextView)findViewById(R.id.tvPetMessage);
		//get the pet status/feelings from the shared pref. 
		//String petStatus = sharedpreferences.getString("dogState",getResources().getString(R.string.dog_status_hello));
		//mPetMessage.setText(petStatus);mm
		 
		 String checking =sharedpreferences.getString("Bought", "rien");
		 
			if(checking.equals("yes")){
	        	Toast.makeText(getApplicationContext(), "Your dog is eating", Toast.LENGTH_LONG ).show();
	        	String neewwPoints = sharedpreferences.getString("pointsIndb", null);
	        	String walletMoney = sharedpreferences.getString("inWallet", null);
	        	myPointsinWelcome.setText(neewwPoints);
	        	myWallet.setText(walletMoney+"¥");
	        	//mPetMessage.setText("Thanks for taking care of me ! ");
	        	animatedGifImageView.setAnimatedGif(R.drawable.dog_eating,TYPE.FIT_CENTER);
	        	comingBackFromPetWord = sharedpreferences.getString("from_pet_world", "yes"); 
			}
			else{
				System.out.println("not yet bought smthg from shopping");
			}
			
			
			if(comingBackFromPetWord.equals("no")){
				// request the database to get points and money details. 
				new GetPointsMoneyDetails().execute(new ApiConnector());
				//diffPreviousMealTime (preEatingTime); 
				//Log.e("time elapsed since last meal 1st", ""+CheckPreviousDogFood[0]+" days: "+CheckPreviousDogFood[1]+"h "+CheckPreviousDogFood[2]+"mins "+CheckPreviousDogFood[3]+"sec");
			}
			
		 
			//new GetPointsMoneyDetails().execute(new ApiConnector());
			//diffPreviousMealTime (preEatingTime);
        
        b = (Button)findViewById(R.id.ButtonLogin);  
        et = (EditText)findViewById(R.id.useremail);
        //pass= (EditText)findViewById(R.id.password);
        //tv = (TextView)findViewById(R.id.tv);
        
        FoodsCompetInfo = (Button)findViewById(R.id.btnCompetImg);
        FoodsCompetInfo.setEnabled(true);
        
        UploadImage=(Button)findViewById(R.id.btnUploadAnImage);
        UploadImage.setEnabled(true);
        
        timeSinceLastFeeding = (TextView)findViewById(R.id.tvPreviousFeed);
     
        
        
        FoodsCompetInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AfterLogin.this, FoodCompetDetails.class));
				
			}
		});
        
        UploadImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(AfterLogin.this, WebViewActivity.class));
			}
		});
        
        /*usageOfScale=(ImageView)findViewById(R.id.imageViewScale);
        usageOfScale.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showAlert();
				
			}
		});
        */

        
        LocationResult locationResult = new LocationResult(){
            @Override
            public void gotLocation(Location location){
                //Got the location!
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);
        
        Log.d("longitude info", ""+myLocation.longitude);
        Log.d("latitude info", ""+myLocation.latitude);
        
        ImageButton imgButtonCal = (ImageButton)findViewById(R.id.imageBtnCalendar); 
        
        imgButtonCal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(AfterLogin.this, CalendarFeedback.class));
				
			}
		});
        
        
        myPointsinWelcome = (TextView)findViewById(R.id.tvPoints);
        //sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String points_currr_db= sharedpreferences.getString("pointsIndb", "300"); 
        myPointsinWelcome.setText(points_currr_db);
        
        String curPointsValue = myPointsinWelcome.getText().toString(); // current numbers of points of the user
        if (!curPointsValue.equals("") && !curPointsValue.equals(".....")) {
        	mPointsNum = Integer.parseInt(curPointsValue);
        	Log.e("pointsss", ""+mPointsNum);
        }
        
        myWallet = (TextView)findViewById(R.id.tvMoneyWallet); 
        
         animatedGifImageView = ((AnimatedGifImageView)findViewById(R.id.animatedGifImageView));
      
        
        animatedGifImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intentpWorld = new Intent(AfterLogin.this, PetWorld.class); 
				intentpWorld.putExtra("hisPoints", cPointsValue);
				//intentpWorld.putExtra("hisWalletMoney", cInWallet);
				if(totalnumbTime<3){
					startActivity(intentpWorld);
				}
				else if (totalnumbTime>=3 && totalnumbTime<=13){ //numb of feeding and playingFriend is betwen [4;10] , start level2
					//editor.putString("action","nothing"); // edit "action" before going to level 2, that way the pet gif doesn't use the previous "action" saved in level1
					//editor.commit();
					startActivity(new Intent(AfterLogin.this, Level2_PetWorld.class));
				}
				else if (totalnumbTime>=14 && totalnumbTime<=28){
					startActivity(new Intent(AfterLogin.this, Level3_PetWorld.class));
				}
				else{
					startActivity(new Intent(AfterLogin.this, Level4_PetWorld.class));
				}
				
				thePreviouslySaveadress = emailAdress; 
			}
		});
        
       // allow the textbullet on the pet to open pet world activity
        LinearLayout pet_Status = (LinearLayout)findViewById(R.id.linearLayPetStatus); 
       pet_Status.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intentpWorldStatus = new Intent(AfterLogin.this, PetWorld.class); 
			intentpWorldStatus.putExtra("hisPoints", cPointsValue);
			//intentpWorld.putExtra("hisWalletMoney", cInWallet);
			if(totalnumbTime<3){
				startActivity(intentpWorldStatus);
			}
			else if (totalnumbTime>=3 && totalnumbTime<=13){ //numb of feeding and playingFriend is betwen [4;10] , start level2
				//editor.putString("action","nothing"); // edit "action" before going to level 2, that way the pet gif doesn't use the previous "action" saved in level1
				//editor.commit();
				startActivity(new Intent(AfterLogin.this, Level2_PetWorld.class));
			}
			else if (totalnumbTime>=14 && totalnumbTime<=28){
				startActivity(new Intent(AfterLogin.this, Level3_PetWorld.class));
			}
			else{
				startActivity(new Intent(AfterLogin.this, Level4_PetWorld.class));
			}
			
			thePreviouslySaveadress = emailAdress;
			
		}
	});
		
		
		ImageButton infoImageBut = (ImageButton)findViewById(R.id.imageButtonInfo); 
		infoImageBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builderInfo = new AlertDialog.Builder(AfterLogin.this);
				builderInfo.setTitle("Information - Details");
				builderInfo.setItems(R.array.list_of_options, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						switch (which) {
						case 0:
							startActivity(new Intent(AfterLogin.this, StatisticInfo.class));
							break;
						case 1:
							startActivity(new Intent(AfterLogin.this, DetailsMoneyRequest.class));
							break;
						case 2:
							startActivity(new Intent(AfterLogin.this, ContactDeveloppers.class)); 
							break;
						case 3:
							//startActivity(new Intent(AfterLogin.this, ContactDeveloppers.class));
							Toast.makeText(getApplicationContext(), "You clicked on "+which, Toast.LENGTH_LONG).show();

							break;

						default:
							Toast.makeText(getApplicationContext(), "You clicked on nothing", Toast.LENGTH_LONG).show();

							break;
						}
					}
				}); 
				AlertDialog dialog = builderInfo.create(); 
				dialog.show();
			}
		});
		
		ImageButton buttonHowTo = (ImageButton)findViewById(R.id.imageButtonHowTo); 
		buttonHowTo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builderHowto = new AlertDialog.Builder(AfterLogin.this);
				builderHowto.setTitle("HowTo - Details");
				builderHowto.setItems(R.array.list_of_options_howTo, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which){
						case 0:
							//startActivity(new Intent(AfterLogin.this, HowToTakePicture.class));
							showStoryHowTo();
							break; 
						case 1:
							//startActivity(new Intent(AfterLogin.this, HowToTakePicture.class));
							showAlert();
							break; 
						case 2:
							showStoryLine();
							//Toast.makeText(getApplicationContext(), "You clicked on "+which, Toast.LENGTH_LONG).show();
							break; 
						}
						
					}
				}); 
				AlertDialog dialogHowto = builderHowto.create(); 
				dialogHowto.show();
				
			}
		});
		
		//set options of the setting button 
		ImageButton buttonSettings = (ImageButton)findViewById(R.id.imageButtonSettings); 
		buttonSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builderHowto = new AlertDialog.Builder(AfterLogin.this);
				builderHowto.setTitle("Settings - Details");
				builderHowto.setItems(R.array.list_of_settings_options, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which){
						case 0:
							//start alarms settings
							Intent intentAlarm = new Intent(AfterLogin.this, ListOfAlarms.class);
				            startActivity(intentAlarm);
							break; 
						
						default:
							break; 
						}
						
					}
				}); 
				AlertDialog dialogSettings = builderHowto.create(); 
				dialogSettings.show();
				
			}
		});

		
		// end opttions button settings
        
		ImageButton buttonSurvey = (ImageButton)findViewById(R.id.imageButtonSurvey); 
		buttonSurvey.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builderSurvey = new AlertDialog.Builder(AfterLogin.this); 
				builderSurvey.setTitle("Surveys");
				builderSurvey.setItems(R.array.list_of_options_survey, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
							if(inLevel==1){
								startActivity(new Intent(AfterLogin.this, Survey1.class));
							}
						break;
						case 1:
							if(inLevel!=2){
								Toast.makeText(getApplicationContext(), "Survey available in Level 2", Toast.LENGTH_LONG).show();
							}
							else{
								startActivity(new Intent(AfterLogin.this, Survey2.class));
							}
							
							break;
						case 2:
							if(inLevel!=3){
								Toast.makeText(getApplicationContext(), "Survey available in Level 3", Toast.LENGTH_LONG).show();
							}
							else{
								startActivity(new Intent(AfterLogin.this, Survey3.class));
							}
							break;
						case 3:
							if(inLevel!=4){
								Toast.makeText(getApplicationContext(), "Survey available in Level 4", Toast.LENGTH_LONG).show();
							}
							else{
								startActivity(new Intent(AfterLogin.this, Survey4.class));
							}
							break;

						default:
							break;
						}
						
					}
				});
				AlertDialog dialogSurvey = builderSurvey.create(); 
				dialogSurvey.show();
				
			}
		});
		
		
    }
    
    
   
    
    public void showAlert(){
    	
    	imageOfFoodOnScale= new ImageView(this);
    	imageOfFoodOnScale.setImageResource(R.drawable.scale_kitchen);
        AfterLogin.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(AfterLogin.this);
                builder.setTitle(R.string.useofScaleTitle);
                builder.setMessage( R.string.useofScaleText)  
                       .setCancelable(false)
                       .setView(imageOfFoodOnScale)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                           }
                       });                     
                AlertDialog alert = builder.create();
                alert.show();               
            }
        });
    }
    
    public void showStoryLine(){
    	
    	final ImageView imageOfStory= new ImageView(this);
    	imageOfStory.setImageResource(R.drawable.game_story);
        AfterLogin.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(AfterLogin.this);
                builder.setTitle(R.string.storyLineTitle);
                builder.setMessage(R.string.storyLineText)  
                       .setCancelable(false)
                       .setView(imageOfStory)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                           }
                       });                     
                AlertDialog alert = builder.create();
                alert.show();               
            }
        });
    }
   
    public void showStoryHowTo(){

    	final ImageView imageOfHowTake= new ImageView(this);
    	imageOfHowTake.setImageResource(R.drawable.how_to_take_pict);
        AfterLogin.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(AfterLogin.this);
                builder.setTitle(R.string.howTakePictTitle);
                builder.setMessage(R.string.howTakePictText)  
                       .setCancelable(false)
                       .setView(imageOfHowTake)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                           }
                       });                     
                AlertDialog alert = builder.create();
                alert.show();               
            }
        });
    	
    }
    
    private class GetPointsMoneyDetails extends AsyncTask<ApiConnector, Long, JSONArray>{

		@Override
		protected JSONArray doInBackground(ApiConnector... params) {
			// TODO Auto-generated method stub
			Log.d("email_to_write_in_db", ""+emailAdress); 
			return params[0].makeGetRequest("http://160.16.101.96/~cedric-k/calorie/infoPointsMoney.php?email="+emailAdress+"&itemprice=0&friendshipprice=0&walkingprice=0");
		}
		@Override
		protected void onPostExecute(JSONArray jsonArray) {
	         //setTextToTextview(jsonArray);
			//setListAdapter(jsonArray);
			Log.d("request succeed", ""+jsonArray); 
			
			pointAndMoney(jsonArray);
	     }
		
		private void pointAndMoney(JSONArray jsonArray) {
			// TODO Auto-generated method stub
			
			try {
				/*
				 * create the JsonObject of the JsonArray received from ApiConnector. normally this is an array with one object,so 
				 * we put (0) as parameter for jsonArray.getJSONObject(0);
				*/
				JSONObject jsobj = jsonArray.getJSONObject(0);  
				String gift_poi= jsobj.getString("points"); 
				String gift_money= jsobj.getString("money");
				preEatingTime=jsobj.getString("last_hour_dog_meal"); // get the previous eating time from the database
				Log.d("db previous dog eating time", ""+preEatingTime);
				String nbTimeDogAte = jsobj.getString("numb_time_dog_ate"); 
				Log.d("nb of eating time", ""+nbTimeDogAte);
				String nbTimeDogplayed = jsobj.getString("numb_time_dog_and_friend"); 
				Log.d("nb of playing time", ""+nbTimeDogplayed);
				String previousPlayedTime = jsobj.getString("last_hour_dog_with_friend");
				Log.d("db previous dog playing+ friends time", ""+previousPlayedTime);
				
				String nbTimeDogWalked = jsobj.getString("numb_time_dog_walk"); 
				Log.d("nb of walking time", ""+nbTimeDogWalked);
				String previousWalkingTime = jsobj.getString("last_hour_dog_walk");
				Log.d("db previous dog walk time", ""+previousWalkingTime);


				// start function diftime
				diffPreviousMealTime (preEatingTime); 
				
				//function findLevel of the progress (confidence of dog)
				detGameLevel(nbTimeDogAte,nbTimeDogplayed,nbTimeDogWalked ); 
				
				myPointsinWelcome.setText(gift_poi);// set the value of number of points
				myWallet.setText(gift_money+"¥");
				thePreviouslySaveadress = emailAdress;
				cPointsValue = gift_poi; 
				cInWallet =gift_money; 
				//sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
				editor = sharedpreferences.edit();
				editor.putString("inWallet", cInWallet); //put the value of the wallet content in a shared pref
				editor.putString("pointsIndb", cPointsValue); //put the value of the wallet content in a shared pref
				editor.putString("lastEatingTime", preEatingTime); 
				editor.putString("lastPlayingTime", previousPlayedTime);
				editor.putString("lastWalkingTime", previousWalkingTime); 
				editor.commit(); 
				Log.d("your 1gift points = ", ""+cPointsValue); 
				Log.d("your 1gift money = ", ""+cInWallet); 

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
    
    private long [] diffPreviousMealTime (String preEatingTime){
    	// get the syst. current time and remove the last eating time of the dog.
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String CurSysTime = sdf.format(cal.getTime());
        Log.d("sys. curr time", ""+CurSysTime);
        Log.d("previous dog eating time", ""+preEatingTime);
        
        try {
			Date sysTimeCur = sdf.parse(CurSysTime);
			Date last_dog_eating=sdf.parse(preEatingTime);
			long diffTime = Math.abs(sysTimeCur.getTime() - last_dog_eating.getTime());
	        Log.d("difference between curTime and eating time", ""+diffTime);
	        
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
	        long [] lastFoodwasAt = new long [] {elapsedDays,elapsedHours, elapsedMinutes, elapsedSeconds}; 
	        for(int i=0; i<lastFoodwasAt.length; i++){
	        	System.out.println(lastFoodwasAt[i]);
	        	CheckPreviousDogFood[i] =lastFoodwasAt[i]; 
	        	}
			Log.e("time elapsed since last meal 2nd ", ""+CheckPreviousDogFood[0]+" days: "+CheckPreviousDogFood[1]+"h "+CheckPreviousDogFood[2]+"mins "+CheckPreviousDogFood[3]+"sec");
			
			String unTextBiz = ""+CheckPreviousDogFood[0]+"day(s) "+CheckPreviousDogFood[1]+"h "+CheckPreviousDogFood[2]+"mins "+CheckPreviousDogFood[3]+"sec";
			timeSinceLastFeeding.setText(unTextBiz);
			changePetGifImage(CheckPreviousDogFood[0], CheckPreviousDogFood[1]); 
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
    	
		return CheckPreviousDogFood;
    	
    }
    
    //function that will change the gif image according to the difference between the previous eating time and current system time
    private void changePetGifImage(long nb_of_days, long elapsedLastFeeding){
    	
    	System.out.println("nbre de jour= "+nb_of_days +"; nbre heure= "+elapsedLastFeeding);
    	//SharedPreferences.Editor editor = sharedpreferences.edit();
    	
    	// get pet activity from shop or friend rental office 
    	String dogAction= sharedpreferences.getString("action", null); 
    	System.out.println("dog action 001"+ dogAction);
    	if(dogAction=="eating"){ 
    		animatedGifImageView.setAnimatedGif(R.drawable.dog_eating,TYPE.FIT_CENTER);
    		String petStatus = getResources().getString(R.string.dog_status_eat);
    		mPetMessage.setText(petStatus);
    		//editor.remove("action"); // remove the specific value of the "action" file in the sharedpref
        	//editor.commit(); 
    	}
    	else if(dogAction=="withfriend"){
    		animatedGifImageView.setAnimatedGif(R.drawable.dog_playing_with_friends,TYPE.FIT_CENTER);
    		String petStatus = getResources().getString(R.string.dog_status_not_alone);
    		mPetMessage.setText(petStatus);
    		//editor.remove("action"); // remove the specific value of the "action" file in the sharedpref
        	//editor.commit(); 
    	}
    	
    	else if(nb_of_days> 10 || elapsedLastFeeding==0){ 
    		animatedGifImageView.setAnimatedGif(R.drawable.dog_standing,TYPE.FIT_CENTER);
    		String petStatus = getResources().getString(R.string.dog_status_hello);
    		mPetMessage.setText(petStatus);
    	}
    	else if(nb_of_days>0){ // if dog didn't eat for more than 8hours, use the dog_begging gif
    		animatedGifImageView.setAnimatedGif(R.drawable.dog_really_hungry,TYPE.FIT_CENTER); 
    		String petStatus = getResources().getString(R.string.dog_status_hungry);
    		mPetMessage.setText(petStatus);
    		
    	}
    	else if(elapsedLastFeeding>=6 ){ 
    		animatedGifImageView.setAnimatedGif(R.drawable.dog_hungry,TYPE.FIT_CENTER); 
    		String petStatus = getResources().getString(R.string.dog_status_hungry);
    		mPetMessage.setText(petStatus);
    	}
    	else if(elapsedLastFeeding>=3 && elapsedLastFeeding<6){ 
    		animatedGifImageView.setAnimatedGif(R.drawable.dog_tongue,TYPE.FIT_CENTER); 
    		String petStatus = getResources().getString(R.string.dog_status_hello);
    		mPetMessage.setText(petStatus);
    	}
    	else if(elapsedLastFeeding>=2 && elapsedLastFeeding<3){ 
    		animatedGifImageView.setAnimatedGif(R.drawable.dog_sitting,TYPE.FIT_CENTER);
    		String petStatus =getResources().getString(R.string.dog_status_lonely);
    		mPetMessage.setText(petStatus);
    	}
    	else if(elapsedLastFeeding>=1 && elapsedLastFeeding<2){ 
    		animatedGifImageView.setAnimatedGif(R.drawable.dog_eating,TYPE.FIT_CENTER);
    		String petStatus = getResources().getString(R.string.dog_status_eat);
    		mPetMessage.setText(petStatus);
    	}
    	else {
            animatedGifImageView.setAnimatedGif(R.drawable.dog_standing,TYPE.FIT_CENTER);
            String petStatus = getResources().getString(R.string.dog_status_hello);
    		mPetMessage.setText(petStatus);
        }
    }
    
    
    private void detGameLevel(String nbTimeDogAte, String nbTimeDogplayed, String nbTimeDogWalked ){
    	
    	 
    	
    	try{
    		
    		int numbTimeAte = Integer.parseInt(nbTimeDogAte);
    		int numbTimePlayed = Integer.parseInt(nbTimeDogplayed);
    		int numbTimeWalk = Integer.parseInt(nbTimeDogWalked);
    		totalnumbTime = numbTimeAte + numbTimePlayed + numbTimeWalk; 
    		System.out.println("total ate + played = " +totalnumbTime);
    		if (totalnumbTime<3){
    			inLevel=1; 
    		} 
    		else if (totalnumbTime>=4 && totalnumbTime<13 ){
    			inLevel=2; 
    		}
    		else {
    			inLevel=3; 
    		}
    		System.out.println("game level? = Level " +inLevel);

    		SharedPreferences.Editor editor = sharedpreferences.edit();
			editor.putLong("nbrTimeDogAte", numbTimeAte); //put the nbre of time dog ate in the shared pref
			editor.putLong("nbrTimeDogplayed", numbTimePlayed); //put the nbre of time dog played in the shared pref
			editor.putLong("nbrTimeDogWalk", numbTimeWalk); //put the nbre of time dog played in the shared pref
			editor.putLong("nbrTimeForLevel", totalnumbTime); //put the total nbre of time dog ate and played in the shared pref for the level 
			editor.commit(); 
    		
    	}catch(NumberFormatException ex){
    		 System.err.println("NumberFormatException in parseInt, "+ ex.getMessage());
    		
    	}
    }

    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.after_login, menu);
        return true;
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
        else if(id == R.id.alarm_settings){
            Intent intent = new Intent(AfterLogin.this, ListOfAlarms.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
