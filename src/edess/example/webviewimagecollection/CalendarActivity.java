package edess.example.webviewimagecollection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CalendarActivity extends ActionBarActivity {

		SharedPreferences settings;
	    public static final String PREFS_FILE = "myPreferenceFiles";
		WebView myWebView;
		String value = "2015-11-11";
		String emailAddress = "edithtalina@gmail.com";

		    @Override
		    protected void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.activity_calendar);
		        //get selected date from intent
		        //get the selected date from intent
		        Intent intent = getIntent();
		        value = intent.getStringExtra("date");
		        
		        //get userEmail @
		        settings= getApplicationContext().getSharedPreferences("myPreferenceFiles", MODE_PRIVATE);
	    		emailAddress =settings.getString("userEmail", "abcd@is.naist.jp");
	    		Log.d("check user email @", ""+emailAddress); 

		        

		        //load URL of the website
		        myWebView = (WebView) findViewById(R.id.calendarWebView);
		        //enable Javascript
		        WebSettings webSettings = myWebView.getSettings();
		        webSettings.setJavaScriptEnabled(true);
		        //myWebView.loadUrl("file:///android_asset/testhttml.html");
		        myWebView.loadUrl("http://160.16.101.96/~cedric-k/calorie/myphp.php?date="+value+"&username="+emailAddress);
		        myWebView.setWebViewClient(new WebViewClient() {
		            public void onPageFinished(WebView view, String url) {
		                myWebView.loadUrl("javascript:init('" + value + "')");
		            }
		        });
		        //myWebView.loadUrl("http://www.dailymail.co.uk");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendar, menu);
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
