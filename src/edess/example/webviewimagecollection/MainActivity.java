package edess.example.webviewimagecollection;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

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
    String email_of_my_user; 
    
    Toast toastCorrectEmailPlease;
    CheckBox rememberMe; 
    
    SharedPreferences settings;
    SharedPreferences.Editor editor; 
    public static final String PREFS_FILE = "myPreferenceFiles";
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //get the screen density of the device
        float screenDensity = getResources().getDisplayMetrics().density; 
        System.out.println("device screen density = "+screenDensity);
        
        settings = getSharedPreferences(PREFS_FILE,0);
		editor = settings.edit();
		
        rememberMe =(CheckBox)findViewById(R.id.checkBoxRememberMe);  
        b = (Button)findViewById(R.id.ButtonLogin);  
        et = (EditText)findViewById(R.id.useremail);
        
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
                
				editor.putString("userEmail", et.getText().toString()); 
				Log.e("the user email tratrata: ", et.getText().toString());
				editor.commit();
				
				
				//check correctness of email entered by user
				Boolean emailAd = isValidEmailAddress(et.getText().toString()); 
            	if(emailAd==true){
            		//FoodsCompetInfo.setEnabled(true);
            		//UploadImage.setEnabled(true);
            		dialog = ProgressDialog.show(MainActivity.this, "", 
                            "Validating user...", true);
            	
	                 new Thread(new Runnable() {
	                        public void run() {
	                        	
	                            login(); 
	                        	
	                        	//startActivity(new Intent(MainActivity.this, AfterLogin.class));
	                            dialog.dismiss();
	                        }
	                      }).start();
	                 //remember the correct email entered
	                 if(rememberMe.isChecked()){
	                	 
	                	 email_of_my_user = settings.getString("userEmail", "elder@dev.dv");
	                	 //Toast.makeText(getApplicationContext(), "email_address saved", Toast.LENGTH_SHORT).show();
	                	 
	                 }
                 	
	                
	               
            	}else{
            		toastCorrectEmailPlease= Toast.makeText(getApplicationContext(),R.string.onlyNAISTemail, Toast.LENGTH_LONG);
            		toastCorrectEmailPlease.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 50);
            		toastCorrectEmailPlease.getView().setBackgroundColor(Color.RED);
            		toastCorrectEmailPlease.show();
            	}
            }
        });
        
        
    }
        
    
    
    public static boolean isValidEmailAddress(String emailAddress) {
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid email address
        //emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.naist+\\.[jp]{2,4}$";
        //emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[naist]+\\.[jp]{2,4}$";

        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(emailAddress);
        if (!matcher.find()) {
          return false;
        }
        return true;
      }
    
    public void login(){
    	
    	//startActivity(new Intent(MainActivity.this, AfterLogin.class));
    	Intent intent = new Intent(MainActivity.this, AfterLogin.class); 
        intent.putExtra("useremailyyyy", et.getText().toString());
        startActivity(intent);
    	
    	
    }
    
    public void getUserEmail(){
    	String msgUseNaist = getResources().getString(R.string.enter_email); 
    	email_of_my_user = settings.getString("userEmail", msgUseNaist  ); 
    	if(email_of_my_user!=null){
    		//Toast.makeText(getApplicationContext(), "email_receive", Toast.LENGTH_SHORT).show();
    		et.setText(email_of_my_user); 
    	}
    }
    
    public void onStart(){
     	super.onStart();
     	//read username and password from SharedPreferences
     	getUserEmail();
     }
   
}
