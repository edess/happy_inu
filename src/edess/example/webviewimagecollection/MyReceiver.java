package edess.example.webviewimagecollection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		Intent service1 = new Intent(context, AlarmReceiver.class);
	       context.startService(service1);
	       Log.d("Receiver", "Receiver has been called");
	}

}
