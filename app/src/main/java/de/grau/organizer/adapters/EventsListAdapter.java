package de.grau.organizer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.grau.organizer.R;
import de.grau.organizer.classes.Event;

/**
 * Simple Adapter for event data, extends ArrayAdapter<Event>
 * Adapter may be used to fill a list
 */

public class EventsListAdapter extends ArrayAdapter<Event> {
    List<Event> eventList;
    private boolean hasDate;
    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    /**
     * Default constructor for EventListAdapter
     * List of events is set for this adapter instance
     * @param context
     * @param resource
     * @param objects
     * @param hasDate
     */
    public EventsListAdapter(Context context, int resource, List<Event> objects, boolean hasDate) {
        super(context, resource, objects);
        this.eventList = objects;
        this.hasDate = hasDate;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    /**
     * Overridden getView
     * Gets all ui Elements for the given rowLayout and sets the data corresponding to the selected event
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layoutId = 0;
        if(hasDate) {
            layoutId = R.layout.eventslist_row_with_date;
        }else{
            layoutId = R.layout.eventslist_row;
        }

        View view = inflater.inflate(layoutId,parent,false);

        Event event = eventList.get(position);
        TextView title = (TextView) view.findViewById(R.id.eventlist_title);
        TextView startDate = (TextView) view.findViewById(R.id.eventlist_startDate);
        TextView endDate = (TextView) view.findViewById(R.id.eventlist_endDate);
        TextView start = (TextView) view.findViewById(R.id.eventlist_start);
        TextView end = (TextView) view.findViewById(R.id.eventlist_end);
        TextView category = (TextView) view.findViewById(R.id.eventlist_category);
        ImageView ivPriority = (ImageView) view.findViewById(R.id.eventlist_priority);

        title.setText(event.getName());

        start.setText("");

        if (event.getStart()!=null){
            start.setText(String.format(Locale.GERMANY,"%02d:%02d",event.getTime(Event.DateTime.START, Calendar.HOUR_OF_DAY),event.getTime(Event.DateTime.START,Calendar.MINUTE)));
            if(hasDate) {
                startDate.setText("");
                startDate.setText(event.getTime(Event.DateTime.START, Calendar.DAY_OF_MONTH) + "." + event.getTime(Event.DateTime.START, Calendar.MONTH) + 1 + "." + event.getTime(Event.DateTime.START, Calendar.YEAR));
            }
        }
        end.setText("");
        if ((event.getEnd()!=null) && (!event.getTask())){
            end.setText(String.format(Locale.GERMANY,"%02d:%02d",event.getTime(Event.DateTime.END, Calendar.HOUR_OF_DAY),event.getTime(Event.DateTime.END,Calendar.MINUTE)));
            if(hasDate) {
                endDate.setText("");
                endDate.setText(event.getTime(Event.DateTime.END, Calendar.DAY_OF_MONTH) + "." + event.getTime(Event.DateTime.END, Calendar.MONTH) + 1 + "." + event.getTime(Event.DateTime.END, Calendar.YEAR));
            }
        }
        category.setText("");
        if(event.getCategory() != null){
            category.setText(event.getCategory().getName());
        }
        ivPriority.setBackgroundColor(event.getPriorityColor());
        return view;
    }
}
