package de.grau.organizer.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.security.cert.Certificate;
import java.util.Calendar;
import java.util.Date;

import de.grau.organizer.classes.Notes;
import de.grau.organizer.database.EventsManagerRealm;
import de.grau.organizer.R;
import de.grau.organizer.classes.Action;
import de.grau.organizer.classes.Event;
import de.grau.organizer.database.interfaces.EventsManager;
import de.grau.organizer.notification.NotificationAlarmHandler;
import io.realm.RealmList;

public class TaskActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = TaskActivity.class.getCanonicalName();
    public static final String INTENT_PARAM_ID = "idParameter";

    private TextView tv_title;
    private TextView tv_description;
    private TextView tv_startDate;
    private TextView tv_startTime;
    private TextView tv_endDate;
    private TextView tv_endTime;
    private TextView tv_notes;

    private Button btn_executeAction;
    private Button btn_delete;
    private FloatingActionButton btn_edit;


    private EventsManager eventsManager;
    private Event mEvent;

    private Context mActivity;

    //INTENT ACTIONS AND PERMISSIONS
    private final static int CONTACT_PICKER = 1;
    private static final int PERMISSIONS_REQUEST_CALL_PHONE = 101;

    @Override
    protected void onStart() {
        super.onStart();

        mEvent = getEventFromIntent(getIntent());

        //If there was a fail while getting Event, do not fill UI
        if (mEvent == null) {
            return;
        }

        fillGUI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mActivity = this;

        initializeGUI();

        eventsManager = new EventsManagerRealm();
        eventsManager.open();

    }

    private void initializeGUI() {
        tv_title            =       (TextView) findViewById(R.id.task_tv_title);
        tv_description      =       (TextView) findViewById(R.id.task_tv_description);
        tv_startDate        =       (TextView) findViewById(R.id.task_tv_startDate);
        tv_startTime        =       (TextView) findViewById(R.id.task_tv_startTIme);
        tv_endDate          =       (TextView) findViewById(R.id.task_tv_endDate);
        tv_endTime          =       (TextView) findViewById(R.id.task_tv_endTime);
        tv_notes            =       (TextView) findViewById(R.id.task_tv_notes);
        btn_delete          =       (Button) findViewById(R.id.task_btn_delete);
        btn_executeAction   =       (Button) findViewById(R.id.task_btn_executeaction);
        btn_edit            =       (FloatingActionButton) findViewById(R.id.task_btn_change);

        setupListeners();
    }

    private void setupListeners() {

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, EditorActivity.class);
                intent.putExtra(EditorActivity.INTENT_PARAM_EVENTID, mEvent.getId());
                startActivity(intent);
            }
        });

        btn_executeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Action action = mEvent.getAction();

                if (action == null) {
                    Snackbar snackbar = null;
                    snackbar = Snackbar.make(v, "No Action set for this Event!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    switch (action.getType()) {
                        case Action.TYPE_CALL:
                            call();
                            break;
                        default:
                            Snackbar snackbar = null;
                            snackbar = Snackbar.make(v, "Action found but not executable!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                    }
                }

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cur_eventName = mEvent.getName();

                //TODO: Decide when there is a notification-alarm -- for now 0
                if(mEvent.getNotificationTime() != 0){
                    NotificationAlarmHandler.cancelNotification(TaskActivity.this, mEvent);
                }
                eventsManager.deleteEvent(mEvent.getId());
                finish();
                Toast.makeText(getApplicationContext(),"Event "+cur_eventName+" deleted", Toast.LENGTH_LONG).show();
            }
        });
    }

    //TODO
    private void call(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CALL_PHONE);
            return;
        }else{
            String uri = "tel:" + mEvent.getAction().getData().trim();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        if(requestCode == PERMISSIONS_REQUEST_CALL_PHONE
                && checkArrayContainsPermission(permissions, Manifest.permission.CALL_PHONE)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            call();
        } else {
            Toast.makeText(this, "Permission not granted cannot proceed with executing action", Toast.LENGTH_SHORT).show();
        }
    }


    // This method checks the permissions-array of onRequestPermissionResult for granted permissions
    boolean checkArrayContainsPermission(String[] array, String requestedPermission){
        for (String permission: array) {
            //This checks is the searched permission is granted
            if(permission.equals(requestedPermission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Fills the GUI of TaskAktivity with the Information from the corresponding Event.
     */
    private void fillGUI(){
        tv_title.setText(mEvent.getName());
        if(mEvent.getAction() != null && !mEvent.getAction().getData().isEmpty()){
            btn_executeAction.setText("Call: "+mEvent.getAction().getData());
        }
        tv_startDate.setText(mEvent.getTime(Event.DateTime.START, Calendar.DAY_OF_MONTH) + "." + (mEvent.getTime(Event.DateTime.START, Calendar.MONTH)) + "." + (mEvent.getTime(Event.DateTime.START, Calendar.YEAR)));
        setCorrectTime(mEvent.getTime(Event.DateTime.START, Calendar.HOUR_OF_DAY)+"", mEvent.getTime(Event.DateTime.START, Calendar.MINUTE)+"", tv_startTime );
        if(mEvent.getEventtype()){
            tv_endTime.setVisibility(View.GONE);
            tv_endDate.setVisibility(View.GONE);
        }else {
            tv_endDate.setText(mEvent.getTime(Event.DateTime.END, Calendar.DAY_OF_MONTH) + "." + (mEvent.getTime(Event.DateTime.END, Calendar.MONTH)) + "." + (mEvent.getTime(Event.DateTime.END, Calendar.YEAR)));
            setCorrectTime(mEvent.getTime(Event.DateTime.END, Calendar.HOUR_OF_DAY)+"",mEvent.getTime(Event.DateTime.END, Calendar.MINUTE)+"", tv_endTime );
        }
        tv_description.setText(mEvent.getDescription());
        addNotesToGui();
    }

    /**
     * Displays the current time in HH:mm.
     * Adds a zero too the String if it has the length one.
     *
     * @param hour Hour of Time which should be displayed
     * @param min   Minute of Time, which should be displayed
     * @param textView TextView in which the Text is going to be shown.
     */
    private void setCorrectTime(String hour, String min, TextView textView ){
        if(hour.length() < 2){
            hour = "0"+hour;}
        if(min.length() < 2){
            min = "0" + min;
        }
        textView.setText(hour +":"+ min);
    }

    /**
     * Reads all Notes from the Event and adds them to the Notes Textview.
     */
    private void addNotesToGui() {
        Log.d(DEBUG_TAG, "Number of Notes is: " +  mEvent.getNotes().size());
        String notes = "";
        for(int i = 0; i <mEvent.getNotes().size(); i++){
          notes = notes + "\n" + mEvent.getNotes().get(i).getText();
        }
        tv_notes.setText(notes);
    }

    private Event getEventFromIntent(Intent intent){
        //Get the EventId from the previous mActivity
        String id = intent.getStringExtra(INTENT_PARAM_ID);

        //If EventId is null, tell Activity that there is a mistake
        if(id == null){
            Log.d(DEBUG_TAG , "Id is null");
            return null;
        } else {
            Log.d(DEBUG_TAG , "ID: "+id);
        }

        //Load event with specific ID
        Event event = eventsManager.loadEvent(id);
        Log.d(DEBUG_TAG, event.toString());
        //EventsManager will return null if there was no event with that id.
        //Again tell Activity that there is a mistake
        if(event==null) {
            return null;
        }

        //If everything passed, return the event
        return event;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventsManager.close();
    }
}
