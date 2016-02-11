package edess.example.webviewimagecollection;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class ApiConnector {
	
	//String url = "http://160.16.101.96/~cedric-k/calorie/fetchDetails.php"; 
	HttpEntity httpEntity=null;
	HttpClient httpClient; 
	HttpGet httpGet;
	
	 public JSONArray makeGetRequest(String url) {
		 
         httpClient = new DefaultHttpClient();
        httpGet = new HttpGet(url); 
                                
 
        HttpResponse httpResponse;
        try {
        	httpResponse = httpClient.execute(httpGet); 
             
            Log.d("Response of GET request", httpResponse.toString());
            httpEntity=httpResponse.getEntity();
            
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //convert HttpEntity to JSON Array
        JSONArray jsonArray=null;
        if(httpEntity!=null){
        	try{
        		String entityResponse = EntityUtils.toString(httpEntity);
        		Log.d("Entity Response: ", entityResponse);
        		jsonArray = new JSONArray(entityResponse);
        		
        	}catch (JSONException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	            
	        }catch (Exception e) {
                e.printStackTrace();
            }
     
        }
        
		return jsonArray;
 
    }

}
