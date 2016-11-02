package de.grau.organizer.ics;

import java.util.Date;
import java.util.List;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.util.ICalDate;
import de.grau.organizer.classes.Event;


/**
 * Created by jvier on 02.11.2016.
 */

public class ICSHelper {

    public static List<VEvent> getICSEvent(String file){
        ICalendar ical = Biweekly.parse(file).first();
        if(ical!=null){
            return ical.getEvents();
        }
        return null;
    }

    public static Event convertVEventToEvent(VEvent vEvent){
        Event newEvent = new Event();
        //Name
        newEvent.setName(vEvent.getSummary().getValue());
        //StartTime
        ICalDate start = vEvent.getDateStart().getValue();
        newEvent.setStart(new Date(start.getTime()));
        ICalDate end = vEvent.getDateEnd().getValue();
        newEvent.setEnd(new Date(end.getTime()));

        return newEvent;
    }


}
