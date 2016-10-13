package de.grau.organizer.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.util.Log;

import de.grau.organizer.activities.TabActivity;
import de.grau.organizer.activities.TaskActivity;
import de.grau.organizer.database.EventsManagerRealm;
import de.grau.organizer.R;
import de.grau.organizer.classes.Event;
import de.grau.organizer.database.interfaces.EventsManager;

/**
 * Created by Vierheller on 10.10.2016.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    public static final String DEBUG_TAG = NotificationBroadcastReceiver.class.getCanonicalName();
    public static final String INTENT_PARAM_EVENT_ID = "param_title";
    public static final int NOTIFICATION_ORGANIZER_ID = 381237;
    EventsManager eventsManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(DEBUG_TAG, "Received Notification request");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        String id = intent.getStringExtra(INTENT_PARAM_EVENT_ID);
        eventsManager = new EventsManagerRealm();
        eventsManager.open();
        Event event = eventsManager.loadEvent(id);

        Notification notification = buildNotification(context, event);

        notificationManager.notify(NOTIFICATION_ORGANIZER_ID, notification);
    }

    private Notification buildNotification(Context context, Event event){

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(TabActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        Intent resultIntent = new Intent(context, TaskActivity.class);
        resultIntent.putExtra(TaskActivity.INTENT_PARAM_ID, event.getId());
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        Notification notification = new Notification.Builder(context)
                .setContentTitle(event.getName())
                .setContentText("Event in "+event.getNotificationTime()+" minutes!\r\n"+event.getDescription())
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

        return notification;
    }
}
