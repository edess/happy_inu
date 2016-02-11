package edess.example.webviewimagecollection;

/**
 * Created by edith-lu on 10/27/15.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AlarmReceiver extends Service {
    private int notificationId = 111;
    private int number = 0;

    public AlarmReceiver() {
    }

    @Override
    public void onCreate(){
   //     Toast.makeText(this,"Alarm receiver started",Toast.LENGTH_SHORT).show();
        displayNotification();
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void displayNotification(){

        Intent intent = new Intent(this, AfterLogin.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Reminder");
        mBuilder.setContentText("Upload meal photo to earn points");
        mBuilder.setSmallIcon(R.drawable.ic_camera_enhance_black_24dp);
        //mBuilder.setNumber(++number);
        mBuilder.setPriority(1);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setAutoCancel(true);

        mBuilder.setContentIntent(resultPendingIntent);


        NotificationManager myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myNotificationManager.notify(notificationId, mBuilder.build());
    }

    @Override

    public void onDestroy() {

// TODO Auto-generated method stub

        super.onDestroy();
    }



    @Override

    public void onStart(Intent intent, int startId) {

// TODO Auto-generated method stub

        super.onStart(intent, startId);
        displayNotification();
    }


}
