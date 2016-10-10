package de.grau.organizer.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import de.grau.organizer.classes.Event;

/**
 * Created by Vierheller on 10.10.2016.
 */

public class NotificationAlarmHandler {
    public static final String DEBUG_TAG = NotificationBroadcastReceiver.class.getCanonicalName();

    public static void setNotification(Context activityContext, Event event){
        AlarmManager alarmManager = (AlarmManager)activityContext.getSystemService(Context.ALARM_SERVICE);

        //Set an intent to define the target and set necessary parameters
        Intent intent = new Intent(activityContext, NotificationBroadcastReceiver.class);
        intent.putExtra(NotificationBroadcastReceiver.INTENT_PARAM_EVENT_ID, event.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(activityContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Get start if event
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getStart());

        //Add negative interval to the start time to get the time when notification should fire
        calendar.add(Calendar.MINUTE, (-1)*event.getNotificationTime());

        Log.d(DEBUG_TAG, "Set alarm at: "+calendar.toString());

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }



}
