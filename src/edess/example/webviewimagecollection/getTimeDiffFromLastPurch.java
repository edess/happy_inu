package edess.example.webviewimagecollection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class getTimeDiffFromLastPurch {
	
	
	long []CheckDiffPreviousTime= new long[4]; 
	
	//default const
	public getTimeDiffFromLastPurch( ){
	
	}
	
	
	
	public long [] TimeDiffFromLastPurch( String PreviousTime){	
		
	Log.e("previous_action_time", ""+PreviousTime); 
	Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String CurSysTime = sdf.format(cal.getTime()); // get the system current time
    Log.d("sys. curr time", ""+CurSysTime);
    
    try {
		Date sysTimeCur = sdf.parse(CurSysTime);
		Date last_dog_eating=sdf.parse(PreviousTime);
		long diffTime = Math.abs(sysTimeCur.getTime() - last_dog_eating.getTime()); // get the diff between current systTime and previous dog meal time, in sdf format
        Log.d("difference between curTime and previous action time", ""+diffTime);
        
        //convert the diff time in days, hours, min, sec
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
 
        long elapsedDays = diffTime / daysInMilli;
        diffTime = diffTime % daysInMilli;
 
        long elapsedHours = diffTime / hoursInMilli;
        diffTime = diffTime % hoursInMilli;
 
        long elapsedMinutes = diffTime / minutesInMilli;
        diffTime = diffTime % minutesInMilli;
 
        long elapsedSeconds = diffTime / secondsInMilli;
 
        System.out.printf("%d days, %d hours, %d minutes, %d seconds%n", elapsedDays,elapsedHours, elapsedMinutes, elapsedSeconds);
        long [] lastFoodwasAt = new long [] {elapsedDays,elapsedHours, elapsedMinutes, elapsedSeconds}; // put d, h, m, s in a array for analysis
        for(int i=0; i<lastFoodwasAt.length; i++){
        	System.out.println(lastFoodwasAt[i]);
        	CheckDiffPreviousTime[i] =lastFoodwasAt[i]; 
        	}
		//Log.e("time elapsed since last meal 2nd ", ""+CheckPreviousDogFoodTime[0]+" days: "+CheckPreviousDogFood[1]+"h "+CheckPreviousDogFood[2]+"mins "+CheckPreviousDogFood[3]+"sec");
		
		String unTextBizT = ""+CheckDiffPreviousTime[0]+"day(s) "+CheckDiffPreviousTime[1]+"h "+CheckDiffPreviousTime[2]+"mins "+CheckDiffPreviousTime[3]+"sec";
		Log.d("CheckPrevious_actionAlpha Time", unTextBizT);
		//timeSinceLastFeeding.setText(unTextBiz);
		//changePetGifImage(CheckPreviousDogFood[0], CheckPreviousDogFood[1]); 
		
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
   return CheckDiffPreviousTime; 
		
	}

}
