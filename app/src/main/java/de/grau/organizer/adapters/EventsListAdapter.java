package de.grau.organizer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import de.grau.organizer.R;
import de.grau.organizer.classes.Event;

/**
 * Created by Vierheller on 27.09.2016.
 */

public class EventsListAdapter extends ArrayAdapter<Event> {
    List<Event> eventList;

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public EventsListAdapter(Context context, int resource, List<Event> objects) {
        super(context, resource, objects);
        this.eventList = objects;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.eventslist_row, parent,false);

        Event event = eventList.get(position);
        TextView title = (TextView) view.findViewById(R.id.eventlist_title);
        TextView start = (TextView) view.findViewById(R.id.eventlist_start);
        TextView end = (TextView) view.findViewById(R.id.eventlist_end);
        TextView category = (TextView) view.findViewById(R.id.eventlist_category);
        ImageView ivPriority = (ImageView) view.findViewById(R.id.eventlist_priority);

        //ToDo check that we got everything correct here
        //End date may be null?, category is not even possible to set
        title.setText(event.getName());
        start.setText("12:00");
        end.setText("13:00");
        category.setText("Sample description");
        ivPriority.setBackgroundColor(event.getPriorityColor());

        return view;
    }

}
