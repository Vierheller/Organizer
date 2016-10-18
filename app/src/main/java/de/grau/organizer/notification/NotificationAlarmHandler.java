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

        Intent notificationIntent = createIntentByEvent(activityContext, event);

        PendingIntent pendingIntent = createPendingIntentWithIntent(activityContext, notificationIntent);

        //Get start time of the event
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getStart());

        //Add negative interval to the start time to get the time when notification should fire
        calendar.add(Calendar.MINUTE, (-1)*event.getNotificationTime());

        Log.d(DEBUG_TAG, "Set alarm for event "+event.getName()+" with id "+event.getId()+" at: "+calendar.toString());

        //actually setting the systemevent
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public static void cancelNotification(Context activityContext, Event event) {
        AlarmManager alarmManager = (AlarmManager)activityContext.getSystemService(Context.ALARM_SERVICE);

        //pendingIntent must be exactly the same like the pendingIntent which was fired
        Intent intent = createIntentByEvent(activityContext, event);
        PendingIntent pendingIntent = createPendingIntentWithIntent(activityContext, intent);

        //Cancels a system event and does nothing if there was no notification
        alarmManager.cancel(pendingIntent);
    }

    private static Intent createIntentByEvent(Context context, Event event){
        //Set an intent to define the target and set necessary parameters
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        intent.putExtra(NotificationBroadcastReceiver.INTENT_PARAM_EVENT_ID, event.getId());
        return intent;
    }

    private static PendingIntent createPendingIntentWithIntent(Context context, Intent intent){
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
