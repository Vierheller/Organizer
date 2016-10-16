package de.grau.organizer;

import android.app.Application;

import de.grau.organizer.database.EventsManagerRealm;
import de.grau.organizer.database.interfaces.EventsManager;

/**
 * Created by Vierheller on 12.10.2016.
 */

public class Organizer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EventsManagerRealm.init(this);
    }
}