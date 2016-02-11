package edess.example.webviewimagecollection;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Survey1 extends ActionBarActivity {
	
	WebView myWebViewSurvey1; 
	 public static final String PREFS_FILE = "myPreferenceFiles";
	 SharedPreferences settings;
	
	 String email_Of_User;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_survey1);
		
		settings = getSharedPreferences(PREFS_FILE,0);
		email_Of_User= settings.getString("userEmail", "not.email"); 
		
		myWebViewSurvey1 = (WebView) findViewById(R.id.webViewSurvey1);
		 //enable Javascript
		 WebSettings webSettings = myWebViewSurvey1.getSettings();
		 webSettings.setJavaScriptEnabled(true);

		//load URL of the website

		 myWebViewSurvey1.loadUrl("http://160.16.101.96/~cedric-k/calorie/survey1.html");

		 myWebViewSurvey1.addJavascriptInterface(new WebAppInterface(this), "Android");
		 myWebViewSurvey1.setWebViewClient(new WebViewClient() {
			 public void onPageFinished(WebView view, String url) {
				 String email = email_Of_User; 
				 myWebViewSurvey1.loadUrl("javascript:init('" + email + "')");
			 }
		 });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.survey1, menu);
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
