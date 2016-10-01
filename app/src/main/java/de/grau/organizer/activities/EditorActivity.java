package de.grau.organizer.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.grau.organizer.EventsManagerRealm;
import de.grau.organizer.R;
import de.grau.organizer.classes.Action;
import de.grau.organizer.classes.ContactHelper;
import de.grau.organizer.classes.Event;
import de.grau.organizer.classes.Notes;
import de.grau.organizer.interfaces.EventsManager;

public class EditorActivity extends AppCompatActivity {
    //GUI ELEMENTS
    EditText et_title;
    Button btn_save;
    Button btn_pickDate;
    Button btn_pickTime;
    Button btn_addNote;
    Button btn_chooseAction;
    Button btn_pickNotifyDelay;
    EditText et_description;

    LinearLayout layout_notecontainer;
    List<EditText> layout_notelist;

    //INTENT ACTIONS AND PERMISSIONS
    private final static int CONTACT_PICKER = 1;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    //INTERNAL EVENT REPRESENTATION
    EventsManager eventsManager = new EventsManagerRealm();
    Event event = new Event();

    //INTENT
    public static final String INTENT_PARAM_EVENTID = "intent_eventID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        initializeGUI();
        checkIntent(getIntent());
    }

    private void checkIntent(Intent intent) {

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

    /**
     * This method references every necessary GUI-Element
     */
    private void initializeGUI(){
        et_title =              (EditText) findViewById(R.id.editor_et_title);
        btn_pickDate =          (Button) findViewById(R.id.editor_btn_pickdate);
        btn_pickTime =          (Button) findViewById(R.id.editor_btn_picktime);
        btn_pickNotifyDelay =   (Button) findViewById(R.id.editor_btn_picknotifydelay);
        btn_chooseAction =      (Button) findViewById(R.id.editor_btn_chooseaction);
        btn_addNote =           (Button) findViewById(R.id.editor_btn_addnote);
        btn_save =              (Button) findViewById(R.id.editor_btn_saveevent);
        et_description=         (EditText) findViewById(R.id.editor_description);
        layout_notecontainer =  (LinearLayout) findViewById(R.id.editor_notecontainer);
        layout_notelist = new ArrayList<EditText>();
        addNote();
        setupListeners();
    }

    private void setupListeners(){
        btn_pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNote();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Saving Event", Toast.LENGTH_LONG).show();
                if(verifyEvent()){
                    saveEvent();
                }
            }
        });

        btn_chooseAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContactInfo();
            }
        });
    }

    private void addNote() {
        EditText et = new EditText(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 10, 0, 0);
        et.setLayoutParams(layoutParams);

        et.setHint(R.string.layout_editor_notehint);

        layout_notelist.add(et);
        layout_notecontainer.addView(et);
    }

    private void getContactInfo() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            Intent contactPickerIntent =
                    new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(contactPickerIntent, CONTACT_PICKER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be using multiple startActivityForReslut
            switch (requestCode) {
                case CONTACT_PICKER:
                    ContactHelper c = new ContactHelper();
                    c.contactPicked(getApplicationContext(),data);
                    Action action = new Action();
                    action.setData(c.getNumber());
                    event.addAction(action);
                    break;
            }
        } else {
            //Log.e("MainActivity", "Failed to pick contact");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                Intent contactPickerIntent =
                        new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent, CONTACT_PICKER);
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * This method calls the {@EventsManager} to store the current Event
     */
    private void saveEvent() {
        //Set title
        event.setName(et_title.getText().toString());

        //Set all Notes
        for (EditText et_note:
             layout_notelist) {
            //no need to add empty Notes
            if(et_note.getText().toString().isEmpty())
                continue;

            Notes curNote = new Notes();
            curNote.setText(et_note.getText().toString());
            event.putNote(curNote);
        }
        //Set description
        event.setDescription(et_description.getText().toString());

        //Save event into Database
        eventsManager.writeEvent(event);

        //After we saved the Event we will switch back to the last Activity
        finish();
    }

    /**
     * This method validates every input and return true if the Event can be saved
     * @return isValid
     */
    private boolean verifyEvent() {
        return et_title.getText().toString() != null;
    }
}
