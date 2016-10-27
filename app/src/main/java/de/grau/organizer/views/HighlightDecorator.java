package de.grau.organizer.views;

import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Calendar;
import java.util.List;


import de.grau.organizer.classes.Event;

/**
 * Decorate a day by making the text big and bold
 */
public class HighlightDecorator implements DayViewDecorator {

    private List<Event> mEvents;
    private int eventIterator = 0;
    private int mColor;
    private Calendar nextDate;

    public HighlightDecorator(List<Event> events) {

        this.mEvents = events;

        if(events.size() > 0) {
            Event firstEvent = events.get(0);
            this.mColor = firstEvent.getPriorityColor();
            this.nextDate = Calendar.getInstance();
            this.nextDate.setTime(firstEvent.getStart());
            eventIterator = 1;
        }


        Log.d("HightlightDecorator", "Initialized with events: " + mEvents.size());

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return shouldHighlight(day.getCalendar());
    }

    private boolean updateNextDate(Calendar calendarDay) {
        //if there exist more events then set the newDate, otherwise nextDate is null
        //if the size is not bigger than the eventIterator, it does not exist a nextDate so return false
        if(mEvents.size() > eventIterator) {
            Event nextEvent = mEvents.get(eventIterator);
            //check if the new event is later than the current day
            //we can check if the next event is later because the list is sorted
            //if it is not later then the event is on the same day
            //increment the eventIterator and try to find an event that is higher
            eventIterator++;
            if (calendarDay.get(Calendar.DAY_OF_YEAR) < nextEvent.getTime(Event.DateTime.START, Calendar.DAY_OF_YEAR)) {
                this.nextDate.setTime(nextEvent.getStart());
                return true;
            } else {
                return updateNextDate(calendarDay);
            }
        } else {
            return false;
        }
    }

    private boolean shouldHighlight(Calendar calendarDay) {
        //check if the nextDate and the calendarDay have the same day of the year
        //if yes, set a new nextDate and return true
        //if no, then do not highlight / return false
        if (nextDate != null) {
            Calendar currentCalendar = Calendar.getInstance();
            if (calendarDay.get(Calendar.DAY_OF_YEAR) == nextDate.get(Calendar.DAY_OF_YEAR)) {
                //a event on this day was found! Highlight this day

                //try to update the next date
                //if a next day was set then it was set
                //otherwise set the nextDate to null
                if(!updateNextDate(calendarDay)) {
                   this.nextDate = null;
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(8, this.mColor));
    }
}
