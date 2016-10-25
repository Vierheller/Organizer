package de.grau.organizer;

import android.app.Application;
import de.grau.organizer.database.EventsManagerRealm;

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