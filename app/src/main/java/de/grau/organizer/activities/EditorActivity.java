package de.grau.organizer.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.util.Log;

import de.grau.organizer.database.EventsManagerRealm;
import de.grau.organizer.R;
import de.grau.organizer.classes.Action;
import de.grau.organizer.classes.ContactHelper;
import de.grau.organizer.classes.Event;
import de.grau.organizer.classes.Notes;
import de.grau.organizer.database.interfaces.EventsManager;
import de.grau.organizer.notification.NotificationAlarmHandler;

public class EditorActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = EditorActivity.class.getCanonicalName();

    //GUI ELEMENTS
    EditText et_title;
    ImageButton btn_save;
    ImageButton btn_cancel;
    Button btn_pickDate;
    Button btn_pickTime;
    Button btn_addNote;
    Button btn_chooseAction;
    Button btn_pickNotifyDelay;
    Button btn_priority;
    EditText et_description;

    LinearLayout layout_notecontainer;
    List<EditText> layout_notelist;

    //DIALOGS
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    Dialog notificationTimeIntervalDialog;
    MaterialDialog priorityDialog;
    private int notificationTimeInterval =0;

    //INTENT ACTIONS AND PERMISSIONS
    private final static int CONTACT_PICKER = 1;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    //INTERNAL EVENT REPRESENTATION
    EventsManager eventsManager = new EventsManagerRealm();
    Event event = null;

    String eventID = null;
    //INTENT
    public static final String INTENT_PARAM_EVENTID = "intent_eventID";

    //HELPERS
    Calendar currentStartDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initializeGUI();
        checkIntent(getIntent());

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                eventID = null;
                event = new Event();
            } else {
                eventID = extras.getString(INTENT_PARAM_EVENTID);
            }
        } else {
            if (savedInstanceState.getSerializable(INTENT_PARAM_EVENTID) instanceof String ) {
                eventID = (String) savedInstanceState.getSerializable(INTENT_PARAM_EVENTID);
            }
        }
    }

    private void checkIntent(Intent intent) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        eventsManager.open(this);

        if (eventID != null) {
            Log.d(DEBUG_TAG, "ID: " + eventID);
            event = eventsManager.loadEvent(eventID);
            Log.d(DEBUG_TAG, event.toString());
            setupGUI();
        }
    }

    private void setupGUI() {
        if (event != null) {
            et_title.setText(event.getName(), TextView.BufferType.NORMAL);
            et_description.setText(event.getDescription(), TextView.BufferType.NORMAL);
            datePickerDialog.updateDate(event.getStartYear(), event.getStartMonth(), event.getStartDay());
            //I want to call the listener with the updateDate method so I do not have to set the btnDateText explicitly
            setBtn_pickDateText(event.getStartYear(), event.getStartMonth(), event.getStartDay());
            setPriorityButton();
            //TODO set values of other elements
        }
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
        btn_priority =          (Button) findViewById(R.id.editor_btn_priority);
        btn_save =              (ImageButton) findViewById(R.id.editor_toolbar_save);
        btn_cancel =            (ImageButton) findViewById(R.id.editor_toolbar_cancel);
        et_description=         (EditText) findViewById(R.id.editor_description);
        layout_notecontainer =  (LinearLayout) findViewById(R.id.editor_notecontainer);
        layout_notelist = new ArrayList<EditText>();

        //TODO This is only for testing
        currentStartDate = Calendar.getInstance();
        currentStartDate.set(Calendar.SECOND, 0);
        btn_pickDate.setText(currentStartDate.get(Calendar.DAY_OF_MONTH)+"."+ (int)(currentStartDate.get(Calendar.MONTH)+1) +"."+ currentStartDate.get(Calendar.YEAR));

        //Add a sigle Note for better user experience.
        addNote();

        setupDialogsDateAndTime();
        setupDialogRememberTime();
        setupDialogPriority();
        setupListeners();
        setPriorityButton();
    }

    private void setupDialogRememberTime() {
        notificationTimeIntervalDialog = new Dialog(this);
        notificationTimeIntervalDialog.setTitle("NumberPicker");
        notificationTimeIntervalDialog.setContentView(R.layout.dialog_numberpicker);

        //Getting references to Dialog UI
        final NumberPicker np = (NumberPicker) notificationTimeIntervalDialog.findViewById(R.id.dialog_numberpicker_np);
        final Button btn_accept = (Button) notificationTimeIntervalDialog.findViewById(R.id.dialog_numberpicker_accept);
        final Button btn_cancel = (Button) notificationTimeIntervalDialog.findViewById(R.id.dialog_numberpicker_cancel);

        //Setup Numberrange of numberpicker
        final String[] numbers = new String[200/5];
        for (int i=0; i<numbers.length; i++)
            numbers[i] = Integer.toString(i*5+5);
        np.setDisplayedValues(numbers);
        np.setMaxValue(numbers.length-1);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);



        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                newVal = Integer.valueOf(numbers[newVal]);
                notificationTimeInterval = newVal;
            }
        });
        notificationTimeIntervalDialog.setCanceledOnTouchOutside(true);
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRememberTimeForEvent();
                btn_pickNotifyDelay.setText(notificationTimeInterval +" min");
                notificationTimeIntervalDialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationTimeIntervalDialog.dismiss();
                btn_pickNotifyDelay.setText("None");
                notificationTimeInterval =-1;
            }
        });
    }

    private void setupDialogPriority(){
        List<Integer> priorities = new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(3);
            add(4);}};

        priorityDialog = new MaterialDialog.Builder(this)
                .title(R.string.editor_dialog_priority_title)
                .titleColorRes(R.color.colorAccent)
                .items(priorities)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        Log.d(DEBUG_TAG, "User Priority Dialog Result = "+position+1);
                        event.setPriority(position+1);
                        setPriorityButton();
                    }
                })
                .build();
    }

    private void setRememberTimeForEvent() {
    }

    private void setupDialogsDateAndTime() {
        final int year = currentStartDate.get(Calendar.YEAR);
        final int month = currentStartDate.get(Calendar.MONTH);
        final int day = currentStartDate.get(Calendar.DAY_OF_MONTH);
        int hour = currentStartDate.get(Calendar.HOUR_OF_DAY);
        int minute = currentStartDate
                .get(Calendar.MINUTE);
        datePickerDialog = new DatePickerDialog(EditorActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day_of_month) {
                setBtn_pickDateText(year, month, day_of_month);
            }
        },year, month, day);

        timePickerDialog = new TimePickerDialog(EditorActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour_of_day, int minute) {
                currentStartDate.set(Calendar.HOUR_OF_DAY, hour_of_day);
                currentStartDate.set(Calendar.MINUTE, minute);
                btn_pickTime.setText((hour_of_day<10?"0"+hour_of_day:hour_of_day)+":"+(minute<10?"0"+minute:minute));
            }
        }, hour, minute, true);

    }

    private void setBtn_pickDateText(int year, int month, int dayOfMonth) {
        currentStartDate.set(Calendar.YEAR, year);
        currentStartDate.set(Calendar.MONTH, month);
        currentStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        btn_pickDate.setText(dayOfMonth + "." + (int)(month+1) + "." + year);
    }

    private void setupListeners(){
        btn_pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        btn_pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });

        btn_addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNote();
            }
        });

        btn_pickNotifyDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationTimeIntervalDialog.show();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifyEvent()){
                    saveEvent();
                    Toast.makeText(getApplicationContext(),"Saving Event", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Event has no Title!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_chooseAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContactInfo();
            }
        });

        btn_priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priorityDialog.show();
            }
        });
    }

    private void setPriorityButton() {
        if(event != null) {
            btn_priority.setText("Priorität " + String.valueOf(event.getPriority()));
        }
        // Color noch setzen auf Prio-Farbe ?
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
                    btn_chooseAction.setText("Action: CALL");
                    ContactHelper c = new ContactHelper();
                    c.contactPicked(getApplicationContext(),data);
                    Action action = new Action();
                    action.setType(Action.TYPE_CALL);
                    action.setData(c.getNumber());
                    event.setAction(action);
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
        if (event == null) {
            event = new Event();
            //Set title
            event.setName(et_title.getText().toString());

            //Set all Notes
            event.putNotes(filterNotes());

            //Set description
            event.setDescription(et_description.getText().toString());

            //set interval
            event.setNotificationTime(notificationTimeInterval);

            //set startdate
            event.setStart(currentStartDate.getTime());

            //Save event into Database
            eventsManager.writeEvent(event);
        } else {
            //TODO update event
            Toast.makeText(getApplicationContext(),"Event cannot be changed yet", Toast.LENGTH_LONG).show();

        }

        NotificationAlarmHandler.setNotification(this, event);

        //After we saved the Event we will switch back to the last Activity
        finish();
    }

    private List<Notes> filterNotes() {
        List<Notes> retNotes = new ArrayList<Notes>();
        for (EditText et_note:
            layout_notelist) {
            //no need to add empty Notes
            if (et_note.getText().toString().isEmpty())
                continue;

            Notes curNote = new Notes();
            curNote.setText(et_note.getText().toString());
            retNotes.add(curNote);
        }
        return retNotes;
    }

    /**
     * This method validates every input and return true if the Event can be saved
     * @return isValid
     */
    private boolean verifyEvent() {
        return !et_title.getText().toString().isEmpty();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }
}
