package de.grau.organizer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.grau.organizer.R;
import de.grau.organizer.classes.Event;

/**
 * Created by YannickRave on 22.10.16.
 */

public class WeekViewEvent extends LinearLayout implements View.OnClickListener {
    private TextView eventTextView;

    public WeekViewEvent(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.week_view_event_layout, this, true);
        onCreate();
    }

    public WeekViewEvent(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();
    }

    public WeekViewEvent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    public WeekViewEvent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        onCreate();
    }


    void onCreate() {
        eventTextView = (TextView) findViewById(R.id.textViewEvent);
        this.setOnClickListener(this);
    }

    public void fillGui(Event event) {
        eventTextView.setText(event.getName());
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(),"Event got clicked",Toast.LENGTH_LONG).show();
    }
}
