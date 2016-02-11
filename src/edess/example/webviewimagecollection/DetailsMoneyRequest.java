package edess.example.webviewimagecollection;

import java.util.Calendar;

import javax.mail.Session;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class DetailsMoneyRequest extends ActionBarActivity {
	
	Button btnSetDate; 
	Button btnSetTime; 
	int year_x, month_x, day_x; 
	int hour_x, minute_x;
	static final int DIALOG_ID=0;
	static final int DIALOG_ID_TIME=1;
	
	TextView showSelectedDate, showSelectedTime;
	AutoCompleteTextView textViewAutoComp; 
	
	Session session=null; 
	ProgressDialog pdialog =null; 
	Context context = null; 
	String[] rec;
	String subject, textMessage, partName, partComments;  
	EditText participantName, participantComments; 
	String participantEmail="edesstest@test.is.naist.jp"; 
	//String lab_of_participant; 
	Button submitMessage;
	View titleView; 
	
	
	//details about DatePicker implementation coming from : https://www.youtube.com/watch?v=czKLAx750N0 
	//details about TimePicker implementation coming from : https://www.youtube.com/watch?v=GDAjPcXRZho
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details_money_request);
		
		
		
		final Calendar cal= Calendar.getInstance();
		year_x= cal.get(Calendar.YEAR);
		month_x=cal.get(Calendar.MONTH);
		day_x=cal.get(Calendar.DAY_OF_MONTH);
		
		showDialogButtonClick(); // to display DatePicker
		showTimePickerDialg(); // to display TimePicker
		
		 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, LABO_NAME);
         textViewAutoComp = (AutoCompleteTextView)findViewById(R.id.autoCompleteTvLaboName);
         textViewAutoComp.setAdapter(adapter);
         textViewAutoComp.setThreshold(1);
       
         //lab_of_participant=textViewAutoComp.getText().toString(); 
         
         showSelectedDate=(TextView)findViewById(R.id.tvShowDate);
         showSelectedTime=(TextView)findViewById(R.id.tvShowTime); 
         
         participantName=(EditText)findViewById(R.id.editTextName);
         participantComments=(EditText)findViewById(R.id.editTextComment);
        
         submitMessage = (Button)findViewById(R.id.btnSubmit);
        
         
         submitUserRequest(); // to send the participant request email to my gmail address
         
         
        
	}
	
	private static final String[] LABO_NAME = new String[] {
        "Ubiquitous Computing Systems", "Dependable System", "Computing Architecture", "Foundations of Software", "Software Engineering",
        "Network System", "Interactive Media Design", "Internet Engineering", "Internet Archictecture and Systems", "Computational Linguistics",
        "Augmented Human Communication", "Vision and Media Computing", "Optical Media Interface", "Ambient Intelligence","Robotics",
        "Intelligent System Control", "Large-Scale System Management", "Mathematical Informatical Informatics", "Image-based Computational Biomedecine",
        "Computational System Biology", "Neural Computation"
    };

	public void showDialogButtonClick(){
		btnSetDate=(Button)findViewById(R.id.btnSetDate);
		btnSetDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DIALOG_ID);
				
			}
		});
	}
	
	@Override
	protected Dialog onCreateDialog(int id){
		
		if(id==DIALOG_ID)
			return new DatePickerDialog(this, dpickerListner, year_x,month_x,day_x); 
		if(id==DIALOG_ID_TIME)
			return new TimePickerDialog(this,kTimePickerListner, hour_x, minute_x, true);
		return null; 
		
	}
	
	private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			// TODO Auto-generated method stub
			
			year_x= year; 
			month_x= monthOfYear+1;
			day_x=dayOfMonth;
			
			//Toast.makeText(getApplicationContext(), year_x+"/"+month_x +"/"+day_x, Toast.LENGTH_SHORT).show();
			
			showSelectedDate.setText(year_x+"/"+month_x +"/"+day_x);
			
		}
	};
	// next lines are for timePicker
	private TimePickerDialog.OnTimeSetListener kTimePickerListner = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			
			hour_x=hourOfDay;
			minute_x=minute; 
			
			String AM_PM ;
	        if(hourOfDay < 12) {
	            AM_PM = "AM";
	        } else { 
	            AM_PM = "PM";
	        } 
	        
			//Toast.makeText(getApplicationContext(), hour_x+" : "+minute_x, Toast.LENGTH_SHORT).show();
			
			showSelectedTime.setText(hour_x+" : "+minute_x +" "+AM_PM);
			
			
		}
	};
	
	// next lines are for timePicker
	public void showTimePickerDialg(){
		btnSetTime=(Button)findViewById(R.id.btnSetTime);
		btnSetTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				showDialog(DIALOG_ID_TIME);
			}
		});
	}
	
	//details about sending an email using JAVA Mail API in android: https://www.youtube.com/watch?v=UNPFWCNMJPU
	public void submitUserRequest(){
		
		
		 
		 submitMessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				rec= new String[] { "akpa.elder.zx6@is.naist.jp" }; 
				subject = "Request for reward"; 
				partName=participantName.getText().toString();
				partComments=participantComments.getText().toString();
				
				textMessage="Hello, I'm "+partName+" from the lab of "+textViewAutoComp.getText().toString()+ ".\n I am requesting the reward"
						+ " for my food picture. I'll be in lab on " + showSelectedDate.getText().toString()+" "
								+ "at "+showSelectedTime.getText().toString()+"\n Comments: "+partComments; 
				
				// if I want to send the mail through the built-in mail app of android
				Intent emailIntent = new Intent(Intent.ACTION_SEND,Uri.parse("mailto:"));
				emailIntent.putExtra(Intent.EXTRA_EMAIL, rec); 
				//emailIntent.putExtra(Intent.EXTRA_CC, rec);
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
				emailIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
				
				emailIntent.setType("message/rfc822");
				startActivity(Intent.createChooser(emailIntent, "Select your email client..."));
				
				
				// if I want to send the mail directly from the app, without using Gmail : http://www.jondev.net/articles/Sending_Emails_without_User_Intervention_%28no_Intents%29_in_Android
				/*Mail m = new Mail("akpaelder@gmail.com", "something");
				
				String[] toArr = {"akpa.elder.zx6@is.naist.jp", "akpaelder@yahoo.fr"}; 
			      m.setTo(toArr); 
			      m.setFrom("akpaelder@gmail.com"); 
			      m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device."); 
			      m.setBody("Email body."); 
			 
			      try { 
			        m.addAttachment("/sdcard/filelocation"); 
			 
			        if(m.send()) { 
			          Toast.makeText(DetailsMoneyRequest.this, "Email was sent successfully.", Toast.LENGTH_LONG).show(); 
			        } else { 
			          Toast.makeText(DetailsMoneyRequest.this, "Email was not sent.", Toast.LENGTH_LONG).show(); 
			        } 
			      } catch(Exception e) { 
			        //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show(); 
			        Log.e("MailApp", "Could not send email", e); 
			      } */
				
			}
		});
		
	}
	
}
