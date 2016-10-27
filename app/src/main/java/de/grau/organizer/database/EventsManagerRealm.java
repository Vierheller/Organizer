package de.grau.organizer.database;

import android.content.Context;
import android.util.Log;

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
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Vierheller on 27.09.2016.
 */

public class EventsManagerRealm implements EventsManager {
    public final String DEBUG_TAG =  getClass().getCanonicalName();
    private Realm realm;

    /**
     * Retrieves a list of events where
     * date of start is greater than startDate and smaller than endDate
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<Event> getEvents(Date startDate, Date endDate) {
        RealmQuery<Event> query = realm.where(Event.class);

        Log.d("database", startDate.toString());

        query.greaterThanOrEqualTo("start", startDate);
        query.lessThanOrEqualTo("start", endDate);
        query.equalTo("isTask", false); //event

        return  query.findAll().sort("priority", Sort.DESCENDING);
    }

    /**
     * Retrieves a List of event for given CalendarDay
     * Used for retrieving List for a selected day in monthfragment
     * @param calDate
     * @return
     */
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

        query.greaterThanOrEqualTo("start", dateStart);
        query.lessThanOrEqualTo("start", dateEnd);

        return  query.findAll().sort("start", Sort.ASCENDING);
    }

    /**
     * Retrieves a list of events where startdate
     * lies in given year and month
     * @param year
     * @param month
     * @return
     */
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

        query.greaterThanOrEqualTo("start", dateStart);
        query.lessThanOrEqualTo("start", dateEnd);
        query.isNotNull("end");

        return  query.findAll().sort("start", Sort.ASCENDING);
    }

    /**
     * Retrieves all events for a given category
     * @param category
     * @return
     */
    @Override
    public List<Event> getEvents(Category category) {
        // Build the query looking at all users:
        RealmQuery<Event> query = realm.where(Event.class);
        query.equalTo("category.id", category.getID());
        List<Event> result = query.findAll().sort("start", Sort.ASCENDING);
        return result;
    }

    /**
     * Retrieves a list of events where
     * startDate lies in given month and year
     * and priority equals given priority
     * @param month
     * @param year
     * @param priority
     * @return
     */
    @Override
    public List<Event> getEvents(int month,int year, int priority) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);


        Date dateStart = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        Date dateEnd = cal.getTime();

        RealmResults<Event> query = realm
                .where(Event.class)
                .equalTo("priority",priority)
                .greaterThanOrEqualTo("start", dateStart)
                .lessThanOrEqualTo("start", dateEnd)
                .findAll()
                .sort("start", Sort.ASCENDING);
        return query;
    }

    /**
     * Returns a list of events for given list of tags
     * @param tags
     * @return
     */
    @Override
    public List<Event> getEvents(List<Tag> tags) {
        return null;
    }

    /**
     * Returns a list of all events available in database
     * For example used in retrieving data for ListFragment
     * @return
     */
    @Override
    public List<Event> getEvents() {
        RealmQuery<Event> query = realm.where(Event.class);
        return  query.findAll().sort("start");
    }

    /**
     * Returns RealmResults of all available events stored in database
     * Those RealmResults are still activly bound to database and can be watched via Listener
     * @return
     */
    @Override
    public RealmResults<Event> getRealmEventList() {
        RealmQuery<Event> query = realm.where(Event.class);
        RealmResults events = query.findAll().sort("start");
        return events;
    }

    /**
     * Returns a specific event via given eventID
     * If no event was found null is returned
     * @param eventId
     * @return
     */
    @Override
    public Event loadEvent(String eventId) {
        RealmQuery<Event> query = realm.where(Event.class);
        query.equalTo("id", eventId);
        Event result = query.findFirst();
        return result;
    }

    @Override
    public Category getCategory(String categoryId) {
        RealmQuery<Category> query = realm.where(Category.class);
        query.equalTo("id", categoryId);
        Category result = query.findFirst();
        return result;
    }

    /**
     * Returns a RealmQuery containing all Category in database
     * containing the given category
     * @param category
     * @return
     */
    @Override
    public Category getCategoryByName(String category) {
        RealmQuery<Category> query = realm.where(Category.class);
        query.equalTo("name", category);
        return query.findFirst();
    }

    /**
     * Returns a list of all categorys
     * Sorted by name asc
     * @return
     */
    @Override
    public List<Category> loadAllCategories() {
        List<Category> categories;
        RealmQuery<Category> query = realm.where(Category.class);
        categories = query.findAllSorted("name", Sort.ASCENDING);
        return categories;
    }

    // All categories without default category
    @Override
    public List<Category> loadAllDeleteableCategories() {
        List<Category> categories;
        RealmQuery<Category> query = realm.where(Category.class);
        query.equalTo("defaultCategory", false);
        categories = query.findAllSorted("name", Sort.ASCENDING);
        return categories;
    }

    /**
     * Returns the default category "Allgemein"
     * @return
     */
    @Override
    public Category getDefaultCategory() {
        RealmQuery<Category> query = realm.where(Category.class);
        query.equalTo("defaultCategory", true);
        return query.findFirst();
    }

    /**
     * Removes a category for a given id
     *
     * @param categoryId
     * @return false if category is still in use, true if category could be deleted
     */
    @Override
    public boolean deleteCategory(String categoryId) {
        RealmQuery<Category> query = realm.where(Category.class);
        query.equalTo("id", categoryId);
        Category result = query.findFirst();
        if(result == null) {
            return false;
        } else {
            realm.beginTransaction();
            result.deleteFromRealm();
            realm.commitTransaction();
            return true;
        }
    }

    /**
     * Deletes an event from database for given id
     * @param eventId
     * @return
     */
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

    /**
     * Writes the given event to database
     * @param event
     */
    @Override
    public void writeEvent(final Event event) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(event);
        realm.commitTransaction();
    }

    /**
     * Updates the event specified by eventId with the data contained in event_data
     * @param event_data
     * @param eventId
     */
    @Override
    public void updateEvent(Event event_data, String eventId) {
        realm.beginTransaction();
        event_data.setId(eventId);
        realm.copyToRealmOrUpdate(event_data);
        realm.commitTransaction();
    }

    @Override
    public void updateCategoryOfEvent(Event event, Category category) {
        realm.beginTransaction();
        event.setCategory(category);
        realm.copyToRealmOrUpdate(event);
        realm.commitTransaction();
    }

    /**
     * Writes a given category to database
     * @param category
     */
    @Override
    public void writeCategory(final Category category) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(category);
        realm.commitTransaction();
    }

    /**
     * Initializes database with given context
     * This call is needed once in the lifecycle of the app
     * There is no migration, changes to database leads to deletion of all present data
     * @param context
     */
    public static void init(Context context) {
        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }

    /**
     * Open the database
     * Database needs to be open for any transaction
     */
    @Override
    public void open() {
        this.realm = Realm.getDefaultInstance();
    }


    /**
     * Used to perform a search for events
     * All events are returned where the field [name, description, category] contains given search string
     * Search is limited to 50 results
     * @param search
     * @param limit
     * @return
     */
    @Override
    public List<Event> searchEvents(String search, int limit){
        RealmQuery<Event> query = realm.where(Event.class);

        query.contains("name",search, Case.INSENSITIVE)
                .or().contains("description",search,Case.INSENSITIVE)
                .or().contains("category.name",search,Case.INSENSITIVE)
                .distinct("id");
        if(query.findAllSorted("name").size() > limit)
            return query.findAllSorted("name").subList(0,limit);
        else
            return query.findAllSorted("name");
    }

    /**
     * Closes the database
     * All transactions are stopped
     */
    @Override
    public void close() {
        realm.close();
        realm = null;
    }
}
