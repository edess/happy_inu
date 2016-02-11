package edess.example.webviewimagecollection;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PetWebView extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pet_web_view);
		
		WebView webViewpetFck = (WebView) findViewById(R.id.webViewFacebook);
		webViewpetFck.getSettings().setJavaScriptEnabled(true);
		
		webViewpetFck.getSettings().setBuiltInZoomControls(true); 
		webViewpetFck.getSettings().setLoadWithOverviewMode(true);
		webViewpetFck.getSettings().setUseWideViewPort(true);
		webViewpetFck.getSettings().setDomStorageEnabled(true);
		webViewpetFck.loadUrl("http://www.facebook.com");
		webViewpetFck.setWebViewClient(new WebViewClient(){
			
			
			public void onPageFinished(WebView view, String url) {
		       
		        Log.d("pet", "facebook");
			} 
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pet_web_view, menu);
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
