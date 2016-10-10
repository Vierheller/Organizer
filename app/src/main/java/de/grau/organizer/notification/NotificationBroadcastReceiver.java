package de.grau.organizer.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.renderscript.Sampler;
import android.util.Log;

import de.grau.organizer.EventsManagerRealm;
import de.grau.organizer.R;
import de.grau.organizer.classes.Event;
import de.grau.organizer.interfaces.EventsManager;

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
        eventsManager.open(context);
        Event event = eventsManager.loadEvent(id);

        Notification notification = buildNotification(context, event);

        notificationManager.notify(NOTIFICATION_ORGANIZER_ID, notification);
    }

    private Notification buildNotification(Context context, Event event){
        Notification notification = new Notification.Builder(context)
                .setContentTitle(event.getName())
                .setContentText(event.getDescription())
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        return notification;
    }
}
