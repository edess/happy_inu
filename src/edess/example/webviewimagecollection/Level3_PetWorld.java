package edess.example.webviewimagecollection;

import java.util.Locale;

import org.json.JSONArray;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abhi.gif.lib.AnimatedGifImageView;
import com.abhi.gif.lib.AnimatedGifImageView.TYPE;

public class Level3_PetWorld extends ActionBarActivity {
	
	AnimatedGifImageView animatedGifImageViewL3;
	public static final String MyPREFERENCES = "MyPrefs" ;
	SharedPreferences sharedpreferences;
	SharedPreferences.Editor editor;
	String[] subMenu;
	String classNameForMenu[]={"PetMydog","PetShopping","PetCompanions","PetWalk","PetHealth","PetPlace","PetFeelings","PetAccessories"};
	AlertDialog alertDialogStores;
	TextView myPointPetWorldL3, myWalletPetWorldL3; 
	
	int numPoint_inLev3=0;
	int walking_price = 300; //price in points for taking a walk 
	
	SharedPreferences settings;
    public static final String PREFS_FILE = "myPreferenceFiles";
    String emailAddress;
    getTimeDiffFromLastPurch gtdf_PrevWalk; 
    String Previous_sanppo_time; 
    
    ImageView pet_plate_icon1L3, pet_plate_icon2L3,pet_friend_iconL3, pet_walk_outside; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level3__pet_world);
		
		sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
		editor = sharedpreferences.edit();
		
		// get useremail @ from shared pref "myPreferenceFiles"
		settings= getApplicationContext().getSharedPreferences("myPreferenceFiles", MODE_PRIVATE);
		emailAddress =settings.getString("userEmail", "abcd@is.naist.jp");
		
		gtdf_PrevWalk= new getTimeDiffFromLastPurch(); // the class I have created form computing time difference between purchases
		
		//determine the last time the dog went to a walk. I use it to prevent player to go walking more than 1 time a day
		
		Long nbrTimePet_walk = sharedpreferences.getLong("nbrTimeDogWalk", 0); 
		if(nbrTimePet_walk==0){
			Previous_sanppo_time="2015-07-17 17:17:17";//set previous_eating time to a default value for the first time running by a new user
        	System.out.println("tatatatat "+Previous_sanppo_time);
        }
        else {
        	Previous_sanppo_time = sharedpreferences.getString("lastPlayingTime", "0"); 
        	System.out.println("bibibibibi "+Previous_sanppo_time);
        }
		
		ImageView checkLevel2Objectif= (ImageView)findViewById(R.id.ivCheckObjectifLev3);
		checkLevel2Objectif.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showLevel3Objectif();
			}
		});
		
		//get the nbrTime for level purpose to set the progress bar
		long userProgressL2 = sharedpreferences.getLong("nbrTimeForLevel", 0);
		long userProgressL3 = userProgressL2-13; // remove the 13 points coming from the L1 & L2 and set the counter to 0 in lev3  
		ProgressBar progressAchievL2 = (ProgressBar)findViewById(R.id.progressBarAchievLev3); 
      	progressAchievL2.setProgress(0);
      	progressAchievL2.setMax(15);
      	progressAchievL2.setProgress((int) userProgressL3);
        TextView scoreNote = (TextView)findViewById(R.id.tvScoreNoteLev3);
        scoreNote.setText(""+userProgressL3+"/15");
        
        //menu button of the level
        Button menuButttonLev3 = (Button)findViewById(R.id.btnMenuLev3); 
        menuButttonLev3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//showAlert(CATEGORY_ID);  
				switch (v.getId()) {
				 
                case R.id.btnMenuLev3:
                    showPopUpMenu();
                    break;
            }
				
			}
		});
        
      //get the number of points from db
        myPointPetWorldL3 = (TextView)findViewById(R.id.tvPointsPetWorldLev3);
		String points_curin_db= sharedpreferences.getString("pointsIndb", "rien"); 
		myPointPetWorldL3.setText(points_curin_db);
		
		//convert the string value of points currently in db to integer 
		if (!points_curin_db.equals("") && !points_curin_db.equals(".....")) {
			numPoint_inLev3 = Integer.parseInt(points_curin_db);
			System.out.println("nbre de point now in lev3= "+numPoint_inLev3);
        }
		else{
			Log.d("check string value of ", "myPointsNumComp"); 
		}
		
		// get the value of the money in the wallet from the AfterLogin.class
        myWalletPetWorldL3 = (TextView)findViewById(R.id.tvWalletPetWorldLev3);
        String WalletPetW = sharedpreferences.getString("inWallet", null);
        myWalletPetWorldL3.setText(WalletPetW);
        
        // gifImage
        animatedGifImageViewL3 = ((AnimatedGifImageView)findViewById(R.id.animatedGifImageView1Lev3));
        animatedGifImageViewL3.setAnimatedGif(R.drawable.dog_playing_run,TYPE.FIT_CENTER);
      	String dogActionL3= sharedpreferences.getString("actionL2", "nothing"); 
      	System.out.println("dog action L2"+ dogActionL3);
      	
      	/*if(dogActionL2.equals("eating")){
      		animatedGifImageViewL2.setAnimatedGif(R.drawable.dog_eating,TYPE.FIT_CENTER);
      	}
      	else if(dogActionL2.equals("withfriend")){
      		animatedGifImageViewL2.setAnimatedGif(R.drawable.dog_playing_with_friends,TYPE.FIT_CENTER);
      	}
      	else{
	        animatedGifImageViewL2.setAnimatedGif(R.drawable.dog_sitting,TYPE.FIT_CENTER);

      	}*/
      	
      	//achievements images-icons
      	pet_plate_icon1L3 =(ImageView)findViewById(R.id.ivpetPlateLev3);
        pet_plate_icon2L3 =(ImageView)findViewById(R.id.ivpetPlate2Lev3); 
        pet_friend_iconL3 =(ImageView)findViewById(R.id.ivpetFrdsLev3);
        pet_walk_outside = (ImageView)findViewById(R.id.ivpetWalkLev3);
        
      	 // change pet_plate icon based on nb time the pet ate since the beginning of the game and remove the 3+8 feeding times of level1 and level2
        long nbTime_dog_get_food = sharedpreferences.getLong("nbrTimeDogAte", 0); 
        int nbTime_dog_had_food = (int)nbTime_dog_get_food; // just convert the variable of type long to int, because of the Switch
        System.out.println("nbTime_dog_had_food " + nbTime_dog_had_food);
        
        switch (nbTime_dog_had_food) {
		case 13:
		case 14:
			pet_plate_icon1L3.setImageResource(R.drawable.dog_plate_grey_orange2);
			pet_plate_icon2L3.setImageResource(R.drawable.dog_plate_grey);
			break;
		case 15:
		case 16:
		case 17:
			pet_plate_icon1L3.setImageResource(R.drawable.dog_plate_orange);
			pet_plate_icon2L3.setImageResource(R.drawable.dog_plate_grey);
			break;
		case 18:
		case 19:
		case 20:
			pet_plate_icon1L3.setImageResource(R.drawable.dog_plate_orange);
			pet_plate_icon2L3.setImageResource(R.drawable.dog_plate_grey_orange2);
			break;
		case 21:
		case 22:
			pet_plate_icon1L3.setImageResource(R.drawable.dog_plate_orange);
			pet_plate_icon2L3.setImageResource(R.drawable.dog_plate_orange);
			break;
		
		default:
			break;
		}
		
        //change friends_icon based on numb of time dog played with friend (I should remove the 2 times he was with friend in level 2 )
        
        long nbTime_dog_withFriend = sharedpreferences.getLong("nbrTimeDogplayed", 0); 
        int nbTime_dog_playWith_friend = (int)nbTime_dog_withFriend; // just convert the variable of type long to int, because of the Switch
        
        switch (nbTime_dog_playWith_friend) {
		case 3:
		case 4:
			pet_friend_iconL3.setImageResource(R.drawable.dog_got_friend_grey_orange);
			break;
		case 5:
			pet_friend_iconL3.setImageResource(R.drawable.dog_got_friend_orange);
			break;

		default:
			break;
		}
        
        //change dog_sanppo icon based on the number of time go went out
        long nbTime_dog_walk_out = sharedpreferences.getLong("nbrTimeDogWalk", 0); 
        int nbTime_dog_went_outside = (int)nbTime_dog_walk_out; // just convert the variable of type long to int, because of the Switch
        
        switch (nbTime_dog_went_outside) {
		case 1:
			pet_walk_outside.setImageResource(R.drawable.dog_walk_grey_orange);
			break;
		case 2:
			pet_walk_outside.setImageResource(R.drawable.dog_walk_orange);
			break;

		default:
			pet_walk_outside.setImageResource(R.drawable.dog_walk_orange);
			break;
		}
	}

	protected void showLevel3Objectif() {
		final ImageView imageOfDogEating= new ImageView(this);
    	imageOfDogEating.setImageResource(R.drawable.objectif_lev2);
    	Level3_PetWorld.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Level3_PetWorld.this);
                builder.setTitle(R.string.ObjectifLevelTtile);
                builder.setMessage(R.string.ObjectifLevel3)  
                       .setCancelable(false)
                       .setView(imageOfDogEating)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                           }
                       });                     
                AlertDialog alert = builder.create();
                alert.show();               
            }
        });
		
	}
	
	public void showPopUpMenu() {
		 
		 //get items of the array sub_menu_array in the strings.xml
		 subMenu = getResources().getStringArray(R.array.sub_menu_array);
       
	        // add your items, this can be done programatically
	        // your items can be from a database
	        ObjectItem[] ObjectItemData = new ObjectItem[8];
	         
	        ObjectItemData[0] = new ObjectItem(91, "Dog");
	        ObjectItemData[1] = new ObjectItem(92, "Shopping");
	        ObjectItemData[2] = new ObjectItem(93, "Companions");
	        ObjectItemData[3] = new ObjectItem(94, "Walking");
	        ObjectItemData[4] = new ObjectItem(95, "Health");
	        ObjectItemData[5] = new ObjectItem(96, "Places");
	        ObjectItemData[6] = new ObjectItem(97, "Feelings");
	        ObjectItemData[7] = new ObjectItem(98, "Accessories");

	        Log.e("local lang", ""+Locale.getDefault().toString()) ; //ok I checked the language. 
	        /*	        
				from here I check the locale language of the device, and if it ja_JP(japanese language)
				I set the adapter with the objectItemDataJapan. 
	        */
	        String language = Locale.getDefault().toString();
	        if (language=="ja_JP"){
	        	
	        	ObjectItemData[0] = new ObjectItem(71, "私の犬");
		        ObjectItemData[1] = new ObjectItem(72, "買い物");
		        ObjectItemData[2] = new ObjectItem(73, "ペットの友達を獲得する");
		        ObjectItemData[3] = new ObjectItem(74, "ペットと散歩する");
		        ObjectItemData[4] = new ObjectItem(75, "ペットの健康");
		        ObjectItemData[5] = new ObjectItem(76, "ペットと共に有名な場所に訪れる");
		        ObjectItemData[6] = new ObjectItem(77, "ペットの調子");
		        ObjectItemData[7] = new ObjectItem(78, "ペットのアクセサリー");
	        	
	        }
	        
	        
	        
	        
	        // our adapter instance
	        ArrayAdapterItem adapter = new ArrayAdapterItem(this, R.layout.list_view_row_item, ObjectItemData);
	         
	        // create a new ListView, set the adapter and item click listener
	        ListView listViewItems = new ListView(this); // we defined the listview that way because we didn't create it in the xml. I want it to be display as pop up
	        listViewItems.setAdapter(adapter);
	        listViewItems.setOnItemClickListener(new OnItemClickListenerListViewItem_old()); // add the behavior when an element of the list is clicked 
	        listViewItems.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					// TODO Auto-generated method stub
					//startActivity(new Intent(PetWorld.this, Shopping.class));
					
					String openSubMenu = classNameForMenu[position]; 
					try{
						Class selectedSunMenu = Class.forName("edess.example.webviewimagecollection." + openSubMenu);
						Intent selectedIntent = new Intent(Level3_PetWorld.this,selectedSunMenu); 
			        	//Log.e("pointsoutout", ""+mPointsNumPetWorld); 
						//selectedIntent.putExtra("lesPoint",myPointPetWorld.getText().toString()); // send the number of points to whatever activity that start from the Menu
						if(position==0 || position==1 ||position==2){ // only make available shopping and petRename in Level1  
							//whichLevel.setVisibility(View.GONE);
							startActivity(selectedIntent);
						}
						else if(position==3){
							showPopupTakeAwalk(); 
							
						}
						else {
							//Toast.makeText(getApplicationContext(), "You clicked on "+position, Toast.LENGTH_LONG).show();
							//whichLevel.setVisibility(View.VISIBLE);
							showAlertOnlyLevel3(); 
						}
						//realPointsInPW="";
						
					}catch(ClassNotFoundException e){
						e.printStackTrace();
					}

					
				}
			}); 
	        
	        // put the ListView in the pop up
	        alertDialogStores = new AlertDialog.Builder(Level3_PetWorld.this)
	            .setView(listViewItems)
	            .setTitle("Menu")
	            .show();
	         
	    }
	
	  public void showAlertOnlyLevel3(){
	    	
	    	final ImageView imageOfSorryFace= new ImageView(this);
	    	imageOfSorryFace.setImageResource(R.drawable.icon_sorry);
	        Level3_PetWorld.this.runOnUiThread(new Runnable() {
	            public void run() {
	                AlertDialog.Builder builder = new AlertDialog.Builder(Level3_PetWorld.this);
	                builder.setTitle("SORRY!!");
	                builder.setMessage(R.string.sorryNextLevel)  
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
	  
	  public void showPopupTakeAwalk(){
			
						//https://bhavyanshu.me/tutorials/create-custom-alert-dialog-in-android/08/20/2015/
			
					AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
				    LayoutInflater inflater = this.getLayoutInflater();
				    final View dialogView = inflater.inflate(R.layout.custom_lay_for_walk_with_pet, null);
				    dialogBuilder.setView(dialogView);
				
				    //final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
				    final TextView tvWalkMsg =(TextView) dialogView.findViewById(R.id.tvWalkMessage);
				    tvWalkMsg.setText(R.string.whengoing_toawalk);
				    
				    dialogBuilder.setTitle(R.string.whengoing_toawalk_title);
				    //dialogBuilder.setMessage("Enter your name and choose the name of who you're looking for!!");
				    
				    //to do when Go walking is push
				    dialogBuilder.setPositiveButton("Go walking", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int whichButton) {
				        	//check first when was last walking time before validating another walk. 
				        	long walkT[] =  gtdf_PrevWalk.TimeDiffFromLastPurch(Previous_sanppo_time);
				        	Log.e("TAG_DEBUG", ""+walkT[0]);
				        	if(walkT[0]>1){ //put this after testing: walkT[0]>1
				        		
				        		// check secondly that player has more or 300 points to go for a walk
					        	int newPoints = numPoint_inLev3 -walking_price;
					        	if(newPoints>=0){
					        		myPointPetWorldL3.setText(String.valueOf(newPoints));
					        		new updatePointsMoneyInDB().execute(new ApiConnector());
					        		editor.putString("pointsIndb",String.valueOf(newPoints) ); // send the new number of point to PetWorld.java
					        		editor.commit();
					        		Toast.makeText(getApplicationContext(), "Thanks for walking out with your pet", Toast.LENGTH_LONG).show();
					        		System.out.println("Thanks for walking out with your pet");
					        		
					        		//set the gifimageview to invisible, so the player think the pet wet outside for walk
					        		new Handler().postDelayed(new Runnable() { 
					        		      @Override 
					        		      public void run() { 
					        		    	  animatedGifImageViewL3.setVisibility(View.INVISIBLE);
					        		      } 
					        		    }, 1000); 
					        	}
					        	else{
					        		Toast.makeText(getApplicationContext(), "Sorry, you don't have enough point to go outside for a walk", Toast.LENGTH_LONG).show();
					        	}
				        	}
				        	else{
				        		//Toast.makeText(getApplicationContext(), "Sorry, you already went for sanppo today", Toast.LENGTH_LONG).show();
				        		showAlertAlreadySanpoToday();
				        	}
				        	
				        }
				    });
				    
				    //to do when Cancel is push
				    dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int whichButton) {
				            //pass
				        }
				    });
				    AlertDialog b = dialogBuilder.create();
				    b.show();
		}
	  
	  private class updatePointsMoneyInDB extends AsyncTask<ApiConnector, Long, JSONArray>{

			@Override
			protected JSONArray doInBackground(ApiConnector... params) {
				// TODO Auto-generated method stub
				return params[0].makeGetRequest("http://160.16.101.96/~cedric-k/calorie/infoPointsMoney.php?email="+emailAddress+"&friendshipprice=0"+"&itemprice=0&walkingprice="+walking_price);

			}
			@Override
			protected void onPostExecute(JSONArray jsonArray) {
		         //setTextToTextview(jsonArray);
				//setListAdapter(jsonArray);
				Log.d("request succeed", ""+jsonArray); 
				
				//pointAndMoney(jsonArray);
		     }
			
		}
	  
	  public void showAlertAlreadySanpoToday(){
			final ImageView imageOfSorryFace= new ImageView(this);
			imageOfSorryFace.setImageResource(R.drawable.icon_sorry);
		    Level3_PetWorld.this.runOnUiThread(new Runnable() {
		        public void run() {
		            AlertDialog.Builder builder = new AlertDialog.Builder(Level3_PetWorld.this);
		            builder.setTitle(R.string.sorryPurchTitle);
		            builder.setMessage(R.string.sorryGoWalk)  
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
		getMenuInflater().inflate(R.menu.level3__pet_world, menu);
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
