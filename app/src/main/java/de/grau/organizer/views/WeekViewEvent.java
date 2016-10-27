package de.grau.organizer.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.grau.organizer.R;
import de.grau.organizer.activities.TaskActivity;
import de.grau.organizer.classes.Event;

/**
 * Created by YannickRave on 22.10.16.
 */

public class WeekViewEvent extends LinearLayout {
    private TextView eventNameTextView;
    private TextView eventTimeTextView;
    private Event mEvent;

    public WeekViewEvent(Context context, Event event) {
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
        eventNameTextView = (TextView) findViewById(R.id.textViewEvent);
        eventTimeTextView = (TextView) findViewById(R.id.textViewTime);
    }

    public void fillGui(Event event) {
        mEvent = event;
        eventNameTextView.setText(event.getName());
        if (event.getCategory() != null) {
            eventTimeTextView.setText(event.getCategory().getName());
        }

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //start activity
                if (mEvent != null ) {
                    Intent intent = new Intent(getContext(), TaskActivity.class);
                    intent.putExtra(TaskActivity.INTENT_PARAM_ID, mEvent.getId());
                    getContext().startActivity(intent);
                } else {
                    Toast.makeText(getContext(), R.string.ErrorToast, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
