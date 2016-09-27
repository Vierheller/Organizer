package de.grau.organizer;

import android.content.Context;

import java.util.Date;
import java.util.List;

import de.grau.organizer.classes.Category;
import de.grau.organizer.classes.Event;
import de.grau.organizer.classes.Tag;
import de.grau.organizer.interfaces.EventsManager;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;

/**
 * Created by Vierheller on 27.09.2016.
 */

public class EventsManagerRealm implements EventsManager {
    private Realm realm;


    @Override
    public List<Event> getEvents(Date startDate, Date endDate) {
        RealmQuery<Event> query = realm.where(Event.class);

        query.greaterThan("start", startDate);
        query.lessThan("end", endDate);

        return  query.findAll();
    }

    @Override
    public List<Event> getEvents(Category category) {
        return null;
    }

    @Override
    public List<Event> getEvents(List<Tag> tags) {
        return null;
    }

    @Override
    public Event loadEvent(long eventId) {
        return null;
    }

    @Override
    public boolean deleteEvent(long eventId) {
        return false;
    }

    @Override
    public boolean writeEvent(final Event event) {

        realm.beginTransaction();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(event);
            }
        });

        realm.commitTransaction();

        return false;
    }

    @Override
    public void open(Context context) {
        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
        Realm.setDefaultConfiguration(realmConfig);

        // Get a Realm instance for this thread
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void close() {
        realm.close();
    }
}
