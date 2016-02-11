package edess.example.webviewimagecollection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PetMydog extends ActionBarActivity {
	
	EditText nameChanged;
	TextView Petname; 
	public static final String MyPREFERENCES = "MyPrefs" ;
	 SharedPreferences sharedpreferences;
	 SharedPreferences.Editor editor; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mydog);
		
		sharedpreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
		
		TextView petPersonality = (TextView)findViewById(R.id.tvPetPersonality); 
		petPersonality.setText("reliable, friendly, trustworthy, laid back, responsive, serene, "
							+ "sensible, affable, gentle, pleasant, affection-demanding, multipurpose. some may be timid, nervous.");
		
		Petname = (TextView)findViewById(R.id.tvPetName); 
		String dogName= sharedpreferences.getString("petName", "Rex");
		//set the pet name with the name value in the shared file
		Petname.setText(dogName);
		
		Button btnChangName =(Button)findViewById(R.id.btnChangeName); 
		btnChangName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showChangeNamePopUp();
				
			}
		});
		
		ImageView petFacebk = (ImageView)findViewById(R.id.ivPetFacebook);
		petFacebk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//startActivity(new Intent(PetMydog.this, PetWebView.class));
				showFacebookMessage();
				
			}
		});
		
	}
	
public void showChangeNamePopUp(){
    	
    	//img_ofCashier= new ImageView(this);
    	//img_ofCashier.setImageResource(R.drawable.icon_cashier);
		//final int poz = foodPoz; // position of the pet food in the grid 
		
		nameChanged = new EditText(this); 
		nameChanged.setText("");
		nameChanged.setMinLines(1);
		nameChanged.setMaxLines(2);
		nameChanged.setBackgroundColor(Color.BLUE);
		nameChanged.setTextColor(Color.WHITE);
		
		int maxLength = 7; // use to set the filter that will be used to set the maximum length of the EditText
		InputFilter[] FilterArray = new InputFilter[1]; // http://stackoverflow.com/questions/24749833/android-set-max-length-of-edittext-programmatically-with-other-inputfilter
		FilterArray[0] = new InputFilter.LengthFilter(maxLength);
		nameChanged.setFilters(FilterArray);
		
        PetMydog.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(PetMydog.this);
                builder.setTitle("Change your pet name ?");
                builder.setMessage("Enter the pet new name ")  
                       .setCancelable(false)
                       .setView(nameChanged)
                       .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
            	            String newName = nameChanged.getText().toString(); 
            	            if(newName.matches("")){
            	            	Toast.makeText(getApplicationContext(), "Name not modified", Toast.LENGTH_LONG).show();

            	            }else{
            	            	Petname.setText(newName);
            	            	Petname.setTextColor(Color.RED);
            	            	editor = sharedpreferences.edit();
            					editor.putString("petName", newName);
            					editor.commit();
            					
            	            	Toast.makeText(getApplicationContext(), "New pet name set", Toast.LENGTH_LONG).show();

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


public void showFacebookMessage(){
	
	final ImageView imageOfStory= new ImageView(this);
	imageOfStory.setImageResource(R.drawable.game_story);
    PetMydog.this.runOnUiThread(new Runnable() {
        public void run() {
            AlertDialog.Builder builder = new AlertDialog.Builder(PetMydog.this);
            builder.setTitle(R.string.facebookMessageTitle);
            builder.setMessage(R.string.facebookMessage)  
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mydog, menu);
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
