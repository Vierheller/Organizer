package de.grau.organizer.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import de.grau.organizer.database.EventsManagerRealm;
import de.grau.organizer.R;
import de.grau.organizer.classes.Action;
import de.grau.organizer.classes.Event;
import de.grau.organizer.database.interfaces.EventsManager;
import de.grau.organizer.notification.NotificationAlarmHandler;

public class TaskActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = TaskActivity.class.getCanonicalName();
    public static final String INTENT_PARAM_ID = "idParameter";

    private TextView tv_title;
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

        mEvent = getEventFromIntent(getIntent());

        //If there was a fail while getting Event, do not fill UI
        if (mEvent == null)
            return;

        fillGUI();

    }

    private void initializeGUI() {
        tv_title            =       (TextView) findViewById(R.id.task_tv_title);
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
                Snackbar snackbar = null;

                if (action == null) {
                    snackbar = Snackbar.make(v, "No Action set for this Event!", Snackbar.LENGTH_LONG);
                } else {
                    switch (action.getType()) {
                        case Action.TYPE_CALL:
                            call();
                            break;
                        default:
                            snackbar = Snackbar.make(v, "Action found but not executable!", Snackbar.LENGTH_LONG);
                    }
                }
                snackbar.show();
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

        switch (requestCode){
            case PERMISSIONS_REQUEST_CALL_PHONE:
                if(checkArrayContainsPermission(permissions, Manifest.permission.CALL_PHONE) && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    call();
                }else{
                    Toast.makeText(this, "Permission not granted cannot proceed with executing action", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    /**
     * This method checks the permissions-array of onRequestPermissionResult for granted permissions
     * @param array
     * @param requestedPermission
     * @return
     */
    boolean checkArrayContainsPermission(String[] array, String requestedPermission){
        for (String permission:
             array) {
            //This checks is the searched permission is granted
            if(permission.equals(requestedPermission))
                return true;
        }
        return false;
    }

    private void fillGUI(){
        tv_title.setText(mEvent.getName());
        if(mEvent.getAction() != null && !mEvent.getAction().getData().isEmpty()){
            btn_executeAction.setText("Call: "+mEvent.getAction().getData());
        }
    }

    private Event getEventFromIntent(Intent intent){
        //Get the EventId from the previous mActivity
        String id = intent.getStringExtra(INTENT_PARAM_ID);

        //If EventId is null, tell Activity that there is a mistake
        if(id == null){
            Log.d(DEBUG_TAG , "Id is null");
            return null;
        }else{
            Log.d(DEBUG_TAG , "ID: "+id);
        }


        //Load event with specific ID
        Event event = eventsManager.loadEvent(id);
        Log.d(DEBUG_TAG, event.toString());
        //EventsManager will return null if there was no event with that id.
        //Again tell Activity that there is a mistake
        if(event==null)
            return null;

        //If everything passed, return the event
        return event;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventsManager.close();
    }
}
