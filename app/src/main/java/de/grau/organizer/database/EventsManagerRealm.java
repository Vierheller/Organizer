package de.grau.organizer.database;

import android.content.Context;

<<<<<<< Updated upstream:app/src/main/java/de/grau/organizer/database/EventsManagerRealm.java
import java.util.ArrayList;
=======
import com.prolificinteractive.materialcalendarview.CalendarDay;

>>>>>>> Stashed changes:app/src/main/java/de/grau/organizer/EventsManagerRealm.java
import java.util.Date;
import java.util.List;
import java.util.Calendar;

import de.grau.organizer.classes.Category;
import de.grau.organizer.classes.Event;
import de.grau.organizer.classes.Tag;
import de.grau.organizer.database.interfaces.EventsManager;
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

    public List<Event> getEvents(CalendarDay calDate) {
        RealmQuery<Event> query = realm.where(Event.class);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, calDate.getDay() - 1);
        cal.set(Calendar.MINUTE, calDate.getMonth());
        cal.set(Calendar.MINUTE, calDate.getYear());

        Date dateStart = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        Date dateEnd = cal.getTime();

        query.greaterThan("start", dateStart);
        query.lessThan("start", dateEnd);

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
            realm.beginTransaction();
            result.deleteFromRealm();
            realm.commitTransaction();
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
    public List<String> searchEvents(String search){
        RealmQuery<Event> query = realm.where(Event.class);

        query.contains("name",search);

        List<Event> tmp = query.findAllSorted("name");
        List<String> result = new ArrayList<>();
        for(Event e: tmp){
            result.add(e.getName()+" | "+e.getStart()+" "+e.getEnd());
        }
        return result;
    }

    @Override
    public void close() {
        realm.close();
        realm = null;
    }
}
