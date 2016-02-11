package edess.example.webviewimagecollection;

import android.content.Context;
import android.util.Log;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;
import android.content.Intent;
import android.location.Location;
import edess.example.webviewimagecollection.MyLocation.LocationResult;

/**
 * Created by edith-lu on 11/30/15.
 */
public class WebAppInterface extends WebViewClient{

    Context mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
        
        //get the coordinate
        /*LocationResult locationResult = new LocationResult(){
            @Override
            public void gotLocation(Location location){
                //Got the location!
            }
        };
        
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(c, locationResult);
        
        Log.d("longitude =", ""+myLocation.longitude);
        Log.d("latitude =", ""+myLocation.latitude);
        */
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void openAfterLogin() {
        Intent myIntent = new Intent(mContext,AfterLogin.class);
        mContext.startActivity(myIntent);
    }
}