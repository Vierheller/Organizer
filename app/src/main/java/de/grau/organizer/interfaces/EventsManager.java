package de.grau.organizer.interfaces;

import android.content.Context;

import java.util.Date;
import java.util.List;

import de.grau.organizer.classes.Category;
import de.grau.organizer.classes.Event;
import de.grau.organizer.classes.Tag;

/**
 * Created by Vierheller on 27.09.2016.
 */

public interface EventsManager {
    List<Event> getEvents(Date startDate, Date endDate);
    List<Event> getEvents(Category category);
    List<Event> getEvents(List<Tag> tags);

    Event loadEvent(long eventId);

    boolean deleteEvent(long eventId);

    boolean writeEvent(Event event);

    void open(Context context);

    void close();
}
