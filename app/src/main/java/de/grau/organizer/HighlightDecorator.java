package de.grau.organizer;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


import de.grau.organizer.classes.Event;

/**
 * Decorate a day by making the text big and bold
 */
public class HighlightDecorator implements DayViewDecorator {

    private List<Event> mEvents;
    private int mColor;


    public HighlightDecorator(List<Event> events) {


        if(events.size() > 0) {
            mColor = events.get(0).getPriorityColor();
        }

        this.mEvents = events;

        Log.d("abc", "mEvent size " + mEvents.size());

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return shouldHighlight(day.getCalendar());
    }

    private boolean shouldHighlight(Calendar calendarDay) {
        Calendar currentCalendar = Calendar.getInstance();


        for (Event currentEvent: mEvents) {
            currentCalendar.setTime(currentEvent.getStart());
            //compare if both dates are on same day.
            boolean sameYear = currentCalendar.get(Calendar.YEAR) == calendarDay.get(Calendar.YEAR);
            boolean sameDay = currentCalendar.get(Calendar.DAY_OF_YEAR) == calendarDay.get(Calendar.DAY_OF_YEAR);
            if(sameDay && sameYear){
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
