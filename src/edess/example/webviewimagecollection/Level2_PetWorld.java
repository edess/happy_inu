package edess.example.webviewimagecollection;

import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.abhi.gif.lib.AnimatedGifImageView;
import com.abhi.gif.lib.AnimatedGifImageView.TYPE;

public class Level2_PetWorld extends ActionBarActivity {
	
	String[] subMenu;
	String classNameForMenu[]={"PetMydog","PetShopping","PetCompanions","PetWalk","PetHealth","PetPlace","PetFeelings","PetAccessories"};
	AlertDialog alertDialogStores;
	AnimatedGifImageView animatedGifImageViewL2;
	SharedPreferences sharedpreferences;
	SharedPreferences.Editor editor;
	TextView myPointPetWorldL2, myWalletPetWorldL2; 
	ImageView pet_plate_icon1, pet_plate_icon2,pet_friend_icon;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level2__pet_world);
		
		
		sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
		//editor.putString("action","nothing"); // edit "action" before going to level 2, that way the pet gif doesn't use the previous "action" saved in level1
		//editor.commit();
		long userProgress = sharedpreferences.getLong("nbrTimeForLevel", 0); 
		
		ImageView checkLevel2Objectif= (ImageView)findViewById(R.id.ivCheckObjectifLev2);
		checkLevel2Objectif.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showLevel2Objectif();
			}
		});
		
		 Button menuButttonLev = (Button)findViewById(R.id.btnMenuLev2); 
	        menuButttonLev.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//showAlert(CATEGORY_ID);  
					switch (v.getId()) {
					 
	                case R.id.btnMenuLev2:
	                    showPopUpMenu();
	                    break;
	            }
					
				}
			});
	        
	        animatedGifImageViewL2 = ((AnimatedGifImageView)findViewById(R.id.animatedGifImageView1Lev2));
	        animatedGifImageViewL2.setAnimatedGif(R.drawable.dog_sitting,TYPE.FIT_CENTER);
	      	String dogActionL2= sharedpreferences.getString("actionL2", "nothing"); 
	      	System.out.println("dog action L2"+ dogActionL2);
	      	
	      	if(dogActionL2.equals("eating")){
	      		animatedGifImageViewL2.setAnimatedGif(R.drawable.dog_eating,TYPE.FIT_CENTER);
	      	}
	      	else if(dogActionL2.equals("withfriend")){
	      		animatedGifImageViewL2.setAnimatedGif(R.drawable.dog_playing_with_friends,TYPE.FIT_CENTER);
	      	}
	      	else{
		        animatedGifImageViewL2.setAnimatedGif(R.drawable.dog_sitting,TYPE.FIT_CENTER);

	      	}
	      	
	      	ProgressBar progressAchievL2 = (ProgressBar)findViewById(R.id.progressBarAchievLev2); 
	      	progressAchievL2.setProgress(0);
	      	progressAchievL2.setMax(10);
	      	progressAchievL2.setProgress((int) userProgress);
	        TextView scoreNote = (TextView)findViewById(R.id.tvScoreNoteLev2);
	        scoreNote.setText(""+userProgress+"/10");
	        
	        //get the number of points from db
	        myPointPetWorldL2 = (TextView)findViewById(R.id.tvPointsPetWorldLev2);
			String points_curin_db= sharedpreferences.getString("pointsIndb", "rien"); 
			myPointPetWorldL2.setText(points_curin_db);
			
			// get the value of the money in the wallet from the AfterLogin.class
	        myWalletPetWorldL2 = (TextView)findViewById(R.id.tvWalletPetWorldLev2);
	        String WalletPetW = sharedpreferences.getString("inWallet", null);
	        myWalletPetWorldL2.setText(WalletPetW);
	        
	        //achievements images-icons
	        pet_plate_icon1 =(ImageView)findViewById(R.id.ivpetPlateLev2);
	        pet_plate_icon2 =(ImageView)findViewById(R.id.ivpetPlate2Lev2); 
	        pet_friend_icon =(ImageView)findViewById(R.id.ivpetFrdsLev2); 
	      	
	        //change pet-friend icon based on nb of time the pet played with other dogs
	        long nbTime_played_WithFr = sharedpreferences.getLong("nbrTimeDogplayed", 0); 
	        if(nbTime_played_WithFr==1){
	        	pet_friend_icon.setImageResource(R.drawable.dog_got_friend_grey_orange);
	        }
	        else{
	        	pet_friend_icon.setImageResource(R.drawable.dog_got_friend_orange);
	        }
	        
	        // change pet_plate icon based on nb time the pet ate since the beginning of the game and remove the 3 feeding time of level1
	        long nbTime_dog_get_food = sharedpreferences.getLong("nbrTimeDogAte", 0); 
	        int nbTime_dog_had_food = (int)nbTime_dog_get_food; // just convert the variable of type long to int, because of the Switch
	        System.out.println("nbTime_dog_had_food " + nbTime_dog_had_food);
	        
	        switch (nbTime_dog_had_food) {
			case 4:
			case 5:
				pet_plate_icon1.setImageResource(R.drawable.dog_plate_grey_orange2);
				pet_plate_icon2.setImageResource(R.drawable.dog_plate_grey);
				break;
			case 6:
			case 7:
				pet_plate_icon1.setImageResource(R.drawable.dog_plate_orange);
				pet_plate_icon2.setImageResource(R.drawable.dog_plate_grey);
				break;
			case 8:
			case 9:
			case 10:
				pet_plate_icon1.setImageResource(R.drawable.dog_plate_orange);
				pet_plate_icon2.setImageResource(R.drawable.dog_plate_grey_orange2);
				break;
			case 11:
			case 12:
				pet_plate_icon1.setImageResource(R.drawable.dog_plate_orange);
				pet_plate_icon2.setImageResource(R.drawable.dog_plate_orange);
				break;
			
			default:
				break;
			}
	        
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
						Intent selectedIntent = new Intent(Level2_PetWorld.this,selectedSunMenu); 
			        	//Log.e("pointsoutout", ""+mPointsNumPetWorld); 
						//selectedIntent.putExtra("lesPoint",myPointPetWorld.getText().toString()); // send the number of points to whatever activity that start from the Menu
						if(position==0 || position==1 ||position==2){ // only make available shopping and petRename in Level1  
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
	        alertDialogStores = new AlertDialog.Builder(Level2_PetWorld.this)
	            .setView(listViewItems)
	            .setTitle("Menu")
	            .show();
	         
	    }

	protected void showLevel2Objectif() {
		final ImageView imageOfDogEating= new ImageView(this);
    	imageOfDogEating.setImageResource(R.drawable.objectif_lev2);
    	Level2_PetWorld.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Level2_PetWorld.this);
                builder.setTitle(R.string.ObjectifLevelTtile);
                builder.setMessage(R.string.ObjectifLevel2)  
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
	
	  public void showAlertOnlyLevel2(){
	    	
	    	final ImageView imageOfSorryFace= new ImageView(this);
	    	imageOfSorryFace.setImageResource(R.drawable.icon_sorry);
	        Level2_PetWorld.this.runOnUiThread(new Runnable() {
	            public void run() {
	                AlertDialog.Builder builder = new AlertDialog.Builder(Level2_PetWorld.this);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level2__pet_world, menu);
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
	public void onPause(){
		super.onPause();
		if ( alertDialogStores!=null && alertDialogStores.isShowing() ){
			alertDialogStores.dismiss();
	    }
	}
}
