package edess.example.webviewimagecollection;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.api.GoogleApi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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

public class PetWorld extends ActionBarActivity {
	
	public final int CATEGORY_ID =0;  
	 private Context mContext;  
	 Dialog dialog;
	 AlertDialog alertDialogStores;
	 TextView myPointPetWorld, myWalletPetWorld; 
	 int mPointsNumPetWorld =0, mWalletNumPetWorld=0; 
	SharedPreferences sharedpreferences;
	SharedPreferences.Editor editor; 
	AnimatedGifImageView animatedGifImageView; 
	//SharedPreferences.Editor editorMsg; 
	String realPointsInPW, realWalletinPW; 
	Float curScore; // the float that will change the appearance of the ratingBar (the stars)
	String[] subMenu; // submenu to be get from the string.xml array 
	ImageView imageThumb1, imageThumb2,imageThumb3; 
	String emailAdress; 
	SharedPreferences settings;
    public static final String PREFS_FILE = "myPreferenceFiles";
    ImageView whichLevel; 
    ImageView checkLevelObjectif; 
	
	 
	 String classNameForMenu[]={"PetMydog","PetShopping","PetCompanions","PetWalk","PetHealth","PetPlace","PetFeelings","PetAccessories"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pet_world);
		
		sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
		
    	settings= getApplicationContext().getSharedPreferences("myPreferenceFiles", MODE_PRIVATE);
    	emailAdress =settings.getString("userEmail", "abcd@is.naist.jp"); 
		
		 // get the value of the points from the AfterLogin.class
		myPointPetWorld = (TextView)findViewById(R.id.tvPointsPetWorld);
		String points_currr_db= sharedpreferences.getString("pointsIndb", "rien"); 
		myPointPetWorld.setText(points_currr_db);
        
        // get the value of the money in the wallet from the AfterLogin.class
        myWalletPetWorld = (TextView)findViewById(R.id.tvWalletPetWorld);
        String inWalletPetW = sharedpreferences.getString("inWallet", null);
        myWalletPetWorld.setText(inWalletPetW);
        
        imageThumb1 =(ImageView)findViewById(R.id.ivThumb1);
        imageThumb2 =(ImageView)findViewById(R.id.ivThumb2); 
        imageThumb3 =(ImageView)findViewById(R.id.ivThumb3); 
        
        editor = sharedpreferences.edit();
        //editor.putString("fromPetWorldFisrtTime", "oui"); 
    	//editor.commit(); 
        whichLevel =(ImageView)findViewById(R.id.ivUpgradeLevelIcon);
        checkLevelObjectif= (ImageView)findViewById(R.id.ivCheckObjectif);
        checkLevelObjectif.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showLevelObjectif();
			}
		});
        
        new GetEatPlayPointsDetails().execute(new ApiConnector());
        
        Button menuButtton = (Button)findViewById(R.id.btnMenu); 
        menuButtton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//showAlert(CATEGORY_ID);  
				switch (v.getId()) {
				 
                case R.id.btnMenu:
                    showPopUp();
                    break;
            }
				
			}
		});
       
        
			String checking =sharedpreferences.getString("Bought", null); 
			if(checking=="oui"){
            	Toast.makeText(getApplicationContext(), "Thanks you! Really sweet!", Toast.LENGTH_LONG ).show();
            	String neewwPoints = sharedpreferences.getString("pointsIndb", null);
            	myPointPetWorld.setText(neewwPoints);
            	
			}

			
        long userProgress = sharedpreferences.getLong("nbrTimeForLevel", 0); 
			
        ProgressBar progressAchiev = (ProgressBar)findViewById(R.id.progressBarAchiev); 
        progressAchiev.setProgress(0);
        progressAchiev.setMax(3);
        progressAchiev.setProgress((int) userProgress);
        TextView scoreNote = (TextView)findViewById(R.id.tvScoreNote);
        scoreNote.setText(""+userProgress+"/3");
        
        //change color of the thumbs based on the value of  userProgress (this should the achievement in current level)
        if(userProgress==1){
        	imageThumb1.setImageResource(R.drawable.dog_plate_orange);
        }
        else if(userProgress==2){
        	imageThumb1.setImageResource(R.drawable.dog_plate_orange);
        	imageThumb2.setImageResource(R.drawable.dog_plate_orange);
        }
        else if(userProgress==3){
        	imageThumb1.setImageResource(R.drawable.dog_plate_orange);
        	imageThumb2.setImageResource(R.drawable.dog_plate_orange);
        	imageThumb3.setImageResource(R.drawable.dog_plate_orange);
        }
        else{
        	imageThumb1.setImageResource(R.drawable.dog_plate_grey);
        	imageThumb2.setImageResource(R.drawable.dog_plate_grey);
        	imageThumb3.setImageResource(R.drawable.dog_plate_grey);
        }
        
      animatedGifImageView = ((AnimatedGifImageView)findViewById(R.id.animatedGifImageView1));
      animatedGifImageView.setAnimatedGif(R.drawable.dog_playing_run,TYPE.FIT_CENTER);
    	String dogAction= sharedpreferences.getString("action", null); 
    	System.out.println("dog action = "+dogAction);
    	
    	if(dogAction=="eating"){
    		animatedGifImageView.setAnimatedGif(R.drawable.dog_eating,TYPE.FIT_CENTER);
    	}
    	else if(dogAction=="withfriend"){
    		animatedGifImageView.setAnimatedGif(R.drawable.dog_playing_with_friends,TYPE.FIT_CENTER);
    	}
    	/*else if(dogAction=="running"){
    		animatedGifImageView.setAnimatedGif(R.drawable.dog_playing_run,TYPE.FIT_CENTER);
    	}*/
    	
    	 
    	
        
	}
	/*
	 * get number of feeding and playing times
	*/
	private class GetEatPlayPointsDetails extends AsyncTask<ApiConnector, Long, JSONArray>{

		@Override
		protected JSONArray doInBackground(ApiConnector... params) {
			return params[0].makeGetRequest("http://160.16.101.96/~cedric-k/calorie/infoPointsMoney.php?email="+emailAdress+"&itemprice=0&friendshipprice=0&walkingprice=0");
		}
		@Override
		protected void onPostExecute(JSONArray jsonArray) {
	         //setTextToTextview(jsonArray);
			//setListAdapter(jsonArray);
			Log.d("request succeed", ""+jsonArray); 
			
			
			try{
				JSONObject jsobj = jsonArray.getJSONObject(0);  
				String nbTimeDogAte = jsobj.getString("numb_time_dog_ate"); 
				Log.d("nb of eating time PetWorldkkkkk", ""+nbTimeDogAte);
				int numbTimeAte = Integer.parseInt(nbTimeDogAte);
				editor.putLong("nbrTimeDogAte", numbTimeAte);
				
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     }
	}
	
	/*
	pop up listview with adapter for the Menu of "pet world". 
	it requires 6 files (java class) to work correctly: activity.xml,activityjava(petWordl.java), objectItem.java, ArrayAdapter.java,
	OnItemClickListenerListViewItem.java, and list_view_row_item.xml which is the layout of each item in the list. we can modify it to
	change the content of each row of the list. 
	
	https://www.codeofaninja.com/2013/09/android-listview-with-adapter-example.html
	
	*/
	 public void showPopUp(){
		 
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
						Intent selectedIntent = new Intent(PetWorld.this,selectedSunMenu); 
			        	Log.e("pointsoutout", ""+mPointsNumPetWorld); 
						selectedIntent.putExtra("lesPoint",myPointPetWorld.getText().toString()); // send the number of points to whatever activity that start from the Menu
						if(position==0 || position==1){ // only make available shopping and petRename in Level1  
							//whichLevel.setVisibility(View.GONE);
							startActivity(selectedIntent);
						}
						else {
							//Toast.makeText(getApplicationContext(), "You clicked on "+position, Toast.LENGTH_LONG).show();
							//whichLevel.setVisibility(View.VISIBLE);
							showAlertOnlyLevel2(); 
						}
						//realPointsInPW="";
						
					}catch(ClassNotFoundException e){
						e.printStackTrace();
					}

					
				}
			}); 
	        
	        // put the ListView in the pop up
	        alertDialogStores = new AlertDialog.Builder(PetWorld.this)
	            .setView(listViewItems)
	            .setTitle("Menu")
	            .show();
	         
	    }
	
	  public void showAlertOnlyLevel2(){
	    	
	    	final ImageView imageOfSorryFace= new ImageView(this);
	    	imageOfSorryFace.setImageResource(R.drawable.icon_sorry);
	        PetWorld.this.runOnUiThread(new Runnable() {
	            public void run() {
	                AlertDialog.Builder builder = new AlertDialog.Builder(PetWorld.this);
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
	  
	  public void showLevelObjectif(){
	    	
	    	final ImageView imageOfDogEating= new ImageView(this);
	    	imageOfDogEating.setImageResource(R.drawable.objectif_lev1);
	    	PetWorld.this.runOnUiThread(new Runnable() {
	            public void run() {
	                AlertDialog.Builder builder = new AlertDialog.Builder(PetWorld.this);
	                builder.setTitle(R.string.ObjectifLevelTtile);
	                builder.setMessage(R.string.ObjectifLevel1)  
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pet_world, menu);
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
		if (id == R.id.home) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void onPause(){
		super.onPause();
		if ( alertDialogStores!=null && alertDialogStores.isShowing() ){
			alertDialogStores.dismiss();
	    }
	}
}
