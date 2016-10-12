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
 * Created by Vierheller on 27.09.2016.
 */

public interface EventsManager {
    List<Event> getEvents(Date startDate, Date endDate);
    List<Event> getEvents(Category category);
    List<Event> getEvents(int month,int year, int priority);
    List<Event> getEvents(List<Tag> tags);
    List<Event> getEvents();
    List<Event> getEvents(CalendarDay calDate);

    RealmResults<Event> getRealmEventList();

    Event loadEvent(String eventId);
    List<Event> searchEvents(String search);

    boolean deleteEvent(String eventId);

    boolean writeEvent(Event event);

    void open(Context context);

    void close();


}
