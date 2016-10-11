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
import io.realm.RealmResults;
import io.realm.Sort;

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

        return  query.findAll().sort("start", Sort.ASCENDING);
    }

    @Override
    public List<Event> getEvents(Category category) {
        // Build the query looking at all users:
        RealmQuery<Event> query = realm.where(Event.class);
        query.equalTo("category.id", category.getID());
        List<Event> result = query.findAll().sort("start", Sort.ASCENDING);
        return result;
    }

    @Override
    public List<Event> getEvents(int priority) {
        // Build the query looking at all users:
        RealmQuery<Event> query = realm.where(Event.class);
        query.equalTo("priority", priority);
        List<Event> result = query.findAll().sort("start", Sort.ASCENDING);
        return result;
    }

    @Override
    public List<Event> getEvents(List<Tag> tags) {
        return null;
    }

    @Override
    public List<Event> getEvents() {
        RealmQuery<Event> query = realm.where(Event.class);
        return  query.findAll().sort("start");
    }

    @Override
    public RealmResults<Event> getRealmEventList() {
        RealmQuery<Event> query = realm.where(Event.class);
        RealmResults events = query.findAll().sort("start");
        return events;
    }

    @Override
    public Event loadEvent(String eventId) {
        RealmQuery<Event> query = realm.where(Event.class);
        query.equalTo("id", eventId);
        Event result = query.findFirst();
        return result;
    }

    @Override
    public boolean deleteEvent(String eventId) {

        RealmQuery<Event> query = realm.where(Event.class);
        query.equalTo("id", eventId);
        Event result = query.findFirst();
        if (result == null) {
            return false;
        } else {
            result.deleteFromRealm();
            return true;
        }

    }

    @Override
    public boolean writeEvent(final Event event) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(event);
        realm.commitTransaction();

        return false;
    }

    @Override
    public void open(Context context) {
        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        if (realm == null || realm.isClosed()) {
            Realm.init(context);
            RealmConfiguration config = new RealmConfiguration.Builder().build();
            Realm.setDefaultConfiguration(config);
            // Get a Realm instance for this thread
            realm = Realm.getDefaultInstance();
        }
    }

    @Override
    public void close() {
        realm.close();
        realm = null;
    }
}
