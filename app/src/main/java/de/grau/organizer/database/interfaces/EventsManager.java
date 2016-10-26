package de.grau.organizer.database.interfaces;

import android.content.Context;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Date;
import java.util.List;

import de.grau.organizer.classes.Category;
import de.grau.organizer.classes.Event;
import de.grau.organizer.classes.Tag;
import io.realm.RealmResults;

/**
 * Interface for an EventsManager
 * Used to abstract the actual EventsManager instance from a specific database
 */

public interface EventsManager {

    List<Event> getEvents(Date startDate, Date endDate);
    List<Event> getEvents(Category category);
    List<Event> getEvents(int month,int year, int priority);
    List<Event> getEvents(List<Tag> tags);
    List<Event> getEvents();
    List<Event> getEvents(CalendarDay calDate);
    List<Event> getEvents(int year, int month);
    List<Event> searchEvents(String search, int limit);
    RealmResults<Event> getRealmEventList();

    Event loadEvent(String eventId);

    Category getDefaultCategory();

    Category getCategory(String categoryId);

    Category getCategoryByName(String name);

    List<Category> loadAllCategories();

    List<Category> loadAllDeleteableCategories();

    boolean deleteCategory(String categoryId);

    boolean deleteEvent(String eventId);

    void writeEvent(Event event);

    void updateEvent(Event event, String eventId);

    void updateCategoryOfEvent(Event event, Category category);

    void writeCategory(Category category);

    void open();

    void close();
}
