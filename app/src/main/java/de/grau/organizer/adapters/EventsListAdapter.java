package de.grau.organizer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.grau.organizer.R;
import de.grau.organizer.classes.Event;

/**
 * Created by Vierheller on 27.09.2016.
 */

public class EventsListAdapter extends ArrayAdapter<Event> {
    List<Event> eventList;

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
        TextView textView = (TextView) view.findViewById(R.id.eventlist_title);

        textView.setText(event.getName());
        return view;
    }
}
