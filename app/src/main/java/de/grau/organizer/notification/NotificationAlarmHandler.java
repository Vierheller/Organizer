package de.grau.organizer.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import de.grau.organizer.classes.Event;

/**
 * Handler class for Notification Alarm
 * This class is able to set a notication as well as canceling it
 *
 */

public class NotificationAlarmHandler {
    public static final String DEBUG_TAG = NotificationBroadcastReceiver.class.getCanonicalName();

    /**
     * Static method to set a Notification for given event
     * Notification time is selected using startDate an notifiaction time of given event
     * @param activityContext
     * @param event
     */
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

    /**
     * Static method for canceling an existing notification
     * Notifcation is canceled for given event data
     * @param activityContext
     * @param event
     */
    public static void cancelNotification(Context activityContext, Event event) {
        AlarmManager alarmManager = (AlarmManager)activityContext.getSystemService(Context.ALARM_SERVICE);

        //pendingIntent must be exactly the same like the pendingIntent which was fired
        Intent intent = createIntentByEvent(activityContext, event);
        PendingIntent pendingIntent = createPendingIntentWithIntent(activityContext, intent);

        //Cancels a system event and does nothing if there was no notification
        alarmManager.cancel(pendingIntent);
    }

    /**
     * Static method for creating an intent based on an event
     * intent has extra INTENT_PARAM_EVENT_ID with the value of the events id
     * @param context
     * @param event
     * @return
     */
    private static Intent createIntentByEvent(Context context, Event event){
        //Set an intent to define the target and set necessary parameters
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        intent.putExtra(NotificationBroadcastReceiver.INTENT_PARAM_EVENT_ID, event.getId());
        return intent;
    }

    /**
     * Static method for updating a given intent
     * @param context
     * @param intent
     * @return
     */
    private static PendingIntent createPendingIntentWithIntent(Context context, Intent intent){
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
