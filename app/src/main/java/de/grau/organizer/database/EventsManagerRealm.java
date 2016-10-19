package de.grau.organizer.database;

import android.content.Context;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Date;
import java.util.List;
import java.util.Calendar;

import de.grau.organizer.classes.Category;
import de.grau.organizer.classes.Event;
import de.grau.organizer.classes.Tag;
import de.grau.organizer.database.interfaces.EventsManager;
import io.realm.Case;
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
    public List<Event> getEvents(CalendarDay calDate) {
        RealmQuery<Event> query = realm.where(Event.class);

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, calDate.getYear());
        cal.set(Calendar.MONTH, calDate.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, calDate.getDay());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

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
    public List<Event> getEvents(int year, int month){
        RealmQuery<Event> query = realm.where(Event.class);

        Calendar cal = Calendar.getInstance();
        //Anfang des Monats
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        Date dateStart = cal.getTime();

        //Ende des Monats
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        Date dateEnd = cal.getTime();

        query.greaterThan("start", dateStart);
        query.lessThan("start", dateEnd);
        query.isNotNull("end");

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
    public List<Event> getEvents(int month,int year, int priority) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.MINUTE, month);
        cal.set(Calendar.MINUTE, year);

        Date dateStart = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        Date dateEnd = cal.getTime();

        RealmResults<Event> query = realm
                .where(Event.class)
                .equalTo("priority",priority)
                .greaterThan("start", dateStart)
                .lessThan("start", dateEnd)
                .findAll()
                .sort("start", Sort.ASCENDING);
        return query;
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
    public Category loadCategory(String categoryId) {
        RealmQuery<Category> query = realm.where(Category.class);
        query.equalTo("id", categoryId);
        return query.findFirst();
    }

    @Override
    public List<Category> loadAllCategories() {
        RealmQuery<Category> query = realm.where(Category.class);
        return query.findAll();
    }

    @Override
    public Category getDefaultCategory() {
        RealmQuery<Category> query = realm.where(Category.class);
        query.equalTo("name", "Allgemein");                 // Default category with name Allgemein
        return query.findFirst();
    }

    @Override
    public long countCategory() {
        RealmQuery<Category> query = realm.where(Category.class);
        return query.count();
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
    public void writeEvent(final Event event) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(event);
        realm.commitTransaction();
    }

    @Override
    public void updateEvent(Event event_data, String eventId) {
        realm.beginTransaction();
        event_data.setId(eventId);
        realm.copyToRealmOrUpdate(event_data);
        realm.commitTransaction();
    }

    @Override
    public void writeCategory(final Category category) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(category);
        realm.commitTransaction();
    }

    public static void init(Context context) {
        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }

    @Override
    public void open() {
        this.realm = Realm.getDefaultInstance();
    }


    @Override
    public List<Event> searchEvents(String search){
        RealmQuery<Event> query = realm.where(Event.class);

        query.contains("name",search, Case.INSENSITIVE).or().contains("description",search,Case.INSENSITIVE).distinct("id");

        return query.findAllSorted("name");
    }

    @Override
    public void close() {
        realm.close();
        realm = null;
    }
}
