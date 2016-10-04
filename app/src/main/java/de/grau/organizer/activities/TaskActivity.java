package de.grau.organizer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.grau.organizer.EventsManagerRealm;
import de.grau.organizer.R;
import de.grau.organizer.classes.Event;
import de.grau.organizer.interfaces.EventsManager;

public class TaskActivity extends AppCompatActivity {
    public static final String DEBUG_LOG = TaskActivity.class.getCanonicalName();
    public static final String INTENT_PARAM_ID = "idParameter";

    private TextView lable_title;
    private Button btn_executeAction;

    private EventsManager eventsManager;
    private Event mEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeGUI();

        eventsManager = new EventsManagerRealm();

    }

    private void initializeGUI() {

    }

    private Event getEventFromIntent(Intent intent){
        //Get the EventId from the previous activity
        String id = intent.getStringExtra(INTENT_PARAM_ID);
        //If EventId is null, tell Activity that there is a mistake
        if(id == null)
            return null;

        //Load event with specific ID
        Event event = eventsManager.loadEvent(id);
        //EventsManager will return null if there was no event with that id.
        //Again tell Activity that there is a mistake
        if(event==null)
            return null;

        //If everything passed, return the event
        return event;
    }



    @Override
    protected void onStart() {
        super.onStart();
        eventsManager.open(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventsManager.close();
    }
}
