package de.grau.organizer.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.grau.organizer.classes.Category;
import de.grau.organizer.classes.Tag;
import de.grau.organizer.database.EventsManagerRealm;
import de.grau.organizer.R;
import de.grau.organizer.classes.Action;
import de.grau.organizer.classes.ContactHelper;
import de.grau.organizer.classes.Event;
import de.grau.organizer.classes.Notes;
import de.grau.organizer.database.interfaces.EventsManager;
import de.grau.organizer.notification.NotificationAlarmHandler;
import io.realm.RealmList;


public class EditorActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = EditorActivity.class.getCanonicalName();

    //GUI ELEMENTS
    private EditText et_title;
    private EditText et_description;
    private ImageButton btn_save;
    private ImageButton btn_cancel;
    private Button btn_pickDateStart;
    private Button btn_pickTime;
    private Button btn_pickDate_fin;
    private Button btn_pickTime_fin;
    private Button btn_addNote;
    private Button btn_chooseAction;
    private Button btn_pickNotifyDelay;
    private Button btn_priority;
    private Button btn_tag;
    private Button btn_category;
    private Switch sw_allDay;
    private LinearLayout layout_enddate;
    private LinearLayout layout_notecontainer;
    private TextView tv_tags;
    private List<EditText> layout_notelist;

    //DIALOGS
    private DatePickerDialog datePickerDialogStart;
    private DatePickerDialog datePickerDialogEnd;
    private TimePickerDialog timePickerDialog;
    private TimePickerDialog timeEndPickerDialog;
    private Dialog notificationTimeIntervalDialog;
    private MaterialDialog priorityDialog;
    private MaterialDialog tagDialog;
    private MaterialDialog categoryDialog;

    //VALUES
    //notificationTimeInterval is the
    private int notificationTimeInterval = -1;      //-1 no notification, positive exists notification
    private boolean useRememberNotification = false;

    //INTENT ACTIONS AND PERMISSIONS
    private final static int CONTACT_PICKER = 1;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    //INTERNAL EVENT REPRESENTATION
    private EventsManager eventsManager = new EventsManagerRealm();
    private Event realm_event = null;
    private Event temp_event = null;
    private Category mCategory;
    private int mPriority = 4;           //default value
    private Tag mTag;
    private boolean mEventtype;          // true = "Aufgabe", false = "Termin"
    private RealmList<Tag> mTagList;
    private String eventID = null;

    //INTENT
    public static final String INTENT_PARAM_EVENTID = "intent_eventID";
    public static final String INTENT_PARAM_EVENTTYPE = "intent_eventType";

    //HELPERS
    private Calendar currentStartDate;
    private Calendar currentEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Open database connection
        eventsManager.open();

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // check intent for eventID and eventtype
        checkIntent(getIntent(), savedInstanceState);

        // Initialize the default Categories
        generateDefaultCategories();

        // This method references every necessary GUI-Element
        initializeGUI();

        // check if editmode or createmode
        checkEditorMode();

        // hide some UI elements in taskmode (no endtime)
        hideFinTime();


    }

    // This method references every necessary GUI-Element
    private void initializeGUI(){
        et_title =              (EditText) findViewById(R.id.editor_et_title);
        sw_allDay   =           (Switch) findViewById(R.id.sw_allDay);
        btn_pickDateStart =     (Button) findViewById(R.id.editor_btn_pickdate);
        btn_pickTime =          (Button) findViewById(R.id.editor_btn_picktime);
        btn_pickDate_fin =      (Button) findViewById(R.id.editor_btn_pickdate_fin);
        btn_pickTime_fin =      (Button) findViewById(R.id.editor_btn_picktime_fin);
        btn_pickNotifyDelay =   (Button) findViewById(R.id.editor_btn_picknotifydelay);
        btn_chooseAction =      (Button) findViewById(R.id.editor_btn_chooseaction);
        btn_addNote =           (Button) findViewById(R.id.editor_btn_addnote);
        btn_priority =          (Button) findViewById(R.id.editor_btn_priority);
        btn_tag =               (Button) findViewById(R.id.editor_btn_tags);
        btn_category =          (Button) findViewById(R.id.editor_btn_category);
        btn_save =              (ImageButton) findViewById(R.id.editor_toolbar_save);
        btn_cancel =            (ImageButton) findViewById(R.id.editor_toolbar_cancel);
        et_description=         (EditText) findViewById(R.id.editor_description);
        tv_tags =               (TextView) findViewById(R.id.editor_tags);
        layout_enddate =        (LinearLayout) findViewById(R.id.layout_enddate);
        layout_notecontainer =  (LinearLayout) findViewById(R.id.editor_notecontainer);
        layout_notelist = new ArrayList<EditText>();

        //TODO This is only for testing
        currentStartDate = Calendar.getInstance();
        currentStartDate.set(Calendar.SECOND, 0);
        btn_pickDateStart.setText(currentStartDate.get(Calendar.DAY_OF_MONTH)+"."+ (int)(currentStartDate.get(Calendar.MONTH)+1) +"."+ currentStartDate.get(Calendar.YEAR));

        currentEndDate = Calendar.getInstance();
        currentEndDate.set(Calendar.SECOND, 0);
        btn_pickDate_fin.setText(currentEndDate.get(Calendar.DAY_OF_MONTH)+"."+ (int)(currentEndDate.get(Calendar.MONTH)+1) +"."+ currentEndDate.get(Calendar.YEAR));

        addNote();      //Add a single Note for better user experience.

        setupDialogsDateAndTime();
        setupDialogRememberTime();
        setupDialogPriority();
        setupDialogTag();
        setupListeners();
        setPriorityButton(4);           // set default priority
        setCategoryButton(null);        // set default category
        setTagTextLine();
    }

    // check intent for eventID and eventtype
    private void checkIntent(Intent intent, Bundle savedInstanceState) {
        if (savedInstanceState == null) {                                                   // ToDo are those checks really necessary????
            Bundle extras = intent.getExtras();
            if(extras == null) {
                eventID = null;
            } else {
                eventID = extras.getString(INTENT_PARAM_EVENTID);
                mEventtype = extras.getBoolean(INTENT_PARAM_EVENTTYPE);
            }
        } else {
            if (savedInstanceState.getSerializable(INTENT_PARAM_EVENTID) instanceof String ) {
                eventID = (String) savedInstanceState.getSerializable(INTENT_PARAM_EVENTID);
                mEventtype = (Boolean) savedInstanceState.getSerializable(INTENT_PARAM_EVENTTYPE);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // Save the default Categories in Realm
    private void generateDefaultCategories() {
        if (eventsManager.getDefaultCategory() == null)  {        // should only be true at the first run of the app
            saveCategory("Allgemein");      // sample value
            saveCategory("Freizeit");       // sample value
            saveCategory("Arbeit");         // sample value
        }
    }

    private void saveCategory(String name) {
        mCategory = new Category(name);
        eventsManager.writeCategory(mCategory);
    }

    private void checkEditorMode() {
        if (eventID != null) {                          // true = Editmode, false = Createmode
            Log.d(DEBUG_TAG, "ID: " + eventID);
            realm_event = eventsManager.loadEvent(eventID);
            Log.d(DEBUG_TAG, realm_event.toString());

            setupGUIForUpdatingEvent();
        }
    }

    private void setupGUIForUpdatingEvent() {       // It is only for edit mode
        if (realm_event != null) {
            et_title.setText(realm_event.getName(), TextView.BufferType.NORMAL);
            et_description.setText(realm_event.getDescription(), TextView.BufferType.NORMAL);
            datePickerDialogStart.updateDate(realm_event.getTime(Event.DateTime.START, Calendar.YEAR), realm_event.getTime(Event.DateTime.START, Calendar.MONTH), realm_event.getTime(Event.DateTime.START, Calendar.DAY_OF_MONTH));
            //I want to call the listener with the updateDate method so I do not have to set the btnDateText explicitly
            setBtn_pickDateText(btn_pickDateStart, currentStartDate, realm_event.getTime(Event.DateTime.START, Calendar.YEAR), realm_event.getTime(Event.DateTime.START, Calendar.MONTH), realm_event.getTime(Event.DateTime.START, Calendar.DAY_OF_MONTH));
            mTagList.addAll(realm_event.getTags());
            setPriorityButton(realm_event.getPriority());
            mCategory = realm_event.getCategory();
            setCategoryButton(mCategory);
            setTagTextLine();
            mEventtype = realm_event.getEventtype();
            if (!mEventtype) {
                datePickerDialogEnd.updateDate(realm_event.getTime(Event.DateTime.END, Calendar.YEAR), realm_event.getTime(Event.DateTime.END, Calendar.MONTH), realm_event.getTime(Event.DateTime.END, Calendar.DAY_OF_MONTH));
                setBtn_pickDateText(btn_pickDate_fin, currentEndDate, realm_event.getTime(Event.DateTime.END, Calendar.YEAR), realm_event.getTime(Event.DateTime.END, Calendar.MONTH), realm_event.getTime(Event.DateTime.END, Calendar.DAY_OF_MONTH));
            }
            //If notificationtime is grater that 0, then there is a notification set for this event
            if(realm_event.getNotificationTime()>=0){
                useRememberNotification = true;
                notificationTimeInterval = realm_event.getNotificationTime();
                //We further need to set the ui of notificationTimeIntervalDialog to display the notificationTime
                NumberPicker np = (NumberPicker) notificationTimeIntervalDialog.findViewById(R.id.dialog_numberpicker_np);
                np.setValue(notificationTimeInterval/5);
                btn_pickNotifyDelay.setText(String.format("%d min", notificationTimeInterval));
            }
        }
    }

    // Hide some layout components for tasks
    private void hideFinTime() {
        if(mEventtype) {            // if realm_event is a task
            btn_pickDate_fin.setVisibility(View.GONE);
            btn_pickTime_fin.setVisibility(View.GONE);
            layout_enddate.setVisibility(View.GONE);
        }
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
            numbers[i] = Integer.toString(i*5);
        np.setDisplayedValues(numbers);
        np.setMaxValue(numbers.length-1);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);

        notificationTimeIntervalDialog.setCanceledOnTouchOutside(true);
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRememberTimeForEvent();
                //getValue returns the selected row. need to map to actual value that is based on array "numbers".
                String selectedNumber = numbers[np.getValue()];
                notificationTimeInterval = Integer.valueOf(selectedNumber);
                btn_pickNotifyDelay.setText(notificationTimeInterval +" min");
                useRememberNotification = true;
                notificationTimeIntervalDialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_pickNotifyDelay.setText("None");
                useRememberNotification = false;
                notificationTimeInterval = -1;
                notificationTimeIntervalDialog.dismiss();
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
                .content("Please choose a priority.")
                .titleColorRes(R.color.colorAccent)
                .items(priorities)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        mPriority = position+1;
                        Log.d(DEBUG_TAG, "User Priority Dialog Result = "+mPriority);
                        setPriorityButton(mPriority);
                    }
                })
                .build();
    }

    private void setupDialogTag() {
        mTagList = new RealmList<>();
        tagDialog = new MaterialDialog.Builder(this)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .title("Add Tag")
                .content("Please insert a tag.")
                .input("my tag...", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        mTag = new Tag(input.toString());
                        mTagList.add(mTag);
                        Log.d(DEBUG_TAG, "Tag hinzugefügt: "+mTag.getName());
                        setTagTextLine();
                    }
                })
                .negativeText("Cancel")
                .build();
    }

    private void setupDialogCategory() {
        final List<String> categorie_names;         // saves only category names
        final List<Category> categories;            // saves the hole category

        categorie_names = new ArrayList<>();
        categories = eventsManager.loadAllCategories();     // get all categories from DB

        for(int i=0; i<categories.size(); i++) {
            categorie_names.add(categories.get(i).getName());
        }

        categoryDialog = new MaterialDialog.Builder(this)
                .title("Category")
                .content("Please choose a category.")
                .items(categorie_names)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        mCategory = categories.get(position);
                        setCategoryButton(mCategory);
                    }
                })
                .negativeText("Cancel")
                .build();
    }

    private void setCategoryButton(Category category) {
        if(category == null) {
            category = eventsManager.getDefaultCategory();     // search Default Category
        }

        mCategory = category;
        btn_category.setText(category.getName());
    }

    private void setRememberTimeForEvent() {
        // ToDo?
    }

    private void setupDialogsDateAndTime() {
        final int year = currentStartDate.get(Calendar.YEAR);
        final int month = currentStartDate.get(Calendar.MONTH);
        final int day = currentStartDate.get(Calendar.DAY_OF_MONTH);
        int hour = currentStartDate.get(Calendar.HOUR_OF_DAY);
        int minute = currentStartDate.get(Calendar.MINUTE);

        datePickerDialogStart = new DatePickerDialog(EditorActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day_of_month) {
                setBtn_pickDateText(btn_pickDateStart, currentStartDate, year, month, day_of_month);
            }
        },year, month, day);

        datePickerDialogEnd = new DatePickerDialog(EditorActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day_of_month) {
                setBtn_pickDateText(btn_pickDate_fin, currentEndDate, year, month, day_of_month);
            }
        },year, month, day);

        initStartTimePicker(hour, minute);
        initEndTimePicker(hour, minute);
    }

    private void initStartTimePicker(int hour, int minute) {
        btn_pickTime.setText(String.format("%02d:%02d",hour,minute));

        timePickerDialog = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        currentStartDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        currentStartDate.set(Calendar.MINUTE, minute);
                        btn_pickTime.setText(String.format("%02d:%02d",hourOfDay,minute));
                        timeEndPickerDialog.setMinTime(hourOfDay,minute,second);
                    }
                },
                currentStartDate.get(Calendar.HOUR_OF_DAY),
                currentStartDate.get(Calendar.MINUTE),
                true
        );
/*        timePickerDialog = new TimePickerDialog(EditorActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour_of_day, int minute) {
                currentStartDate.set(Calendar.HOUR_OF_DAY, hour_of_day);
                currentStartDate.set(Calendar.MINUTE, minute);

                btn_pickTime.setText(String.format("%02d:%02d",hour_of_day,minute));
                initEndTimePicker(hour_of_day,minute);
            }
        }, hour, minute, true);*/
    }

    private void initEndTimePicker(int hour, int minute) {
        btn_pickTime_fin.setText(String.format("%02d:%02d",hour,minute));

        timeEndPickerDialog = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        currentEndDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        currentEndDate.set(Calendar.MINUTE, minute);
                        btn_pickTime_fin.setText(String.format("%02d:%02d",hourOfDay,minute));
                    }
                },
                currentEndDate.get(Calendar.HOUR_OF_DAY),
                currentEndDate.get(Calendar.MINUTE),
                true
        );
        timeEndPickerDialog.setMinTime(currentStartDate.get(Calendar.HOUR_OF_DAY),currentStartDate.get(Calendar.MINUTE),currentStartDate.get(Calendar.SECOND));


        /*timeEndPickerDialog = new TimePickerDialog(EditorActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour_of_day, int minute) {
                currentEndDate.set(Calendar.HOUR_OF_DAY, hour_of_day);
                currentEndDate.set(Calendar.MINUTE, minute);
                btn_pickTime_fin.setText(String.format("%02d:%02d",hour_of_day,minute));
            }
        }, hour, minute, true);
*/
    }


    private void setBtn_pickDateText(Button button, Calendar date, int year, int month, int dayOfMonth) {
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        button.setText(dayOfMonth + "." + (int)(month+1) + "." + year);
    }

    private void setupListeners(){
        btn_pickDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialogStart.show();
            }
        });

        sw_allDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    changeVisibilityofTimePicker("User switched to allDay, disabling TimePicker", View.INVISIBLE, false);
                } else{
                    changeVisibilityofTimePicker("User switched to on same day, enabling TimePicker", View.VISIBLE, true);
                }
            }
        });

        btn_pickDate_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialogEnd.getDatePicker().setMinDate(currentStartDate.getTime().getTime());
                datePickerDialogEnd.show();
            }
        });

        btn_pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show(getFragmentManager(),"timeStartPickerDialog");
            }
        });

        btn_pickTime_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeEndPickerDialog.show(getFragmentManager(),"timeEndPickerDialog");
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

        btn_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagDialog.show();
            }
        });

        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseCategory();
            }
        });
    }

    private void changeVisibilityofTimePicker(String msg, int invisible, boolean enabled) {
        Log.d(DEBUG_TAG, msg);
        btn_pickTime_fin.setVisibility(invisible);
        btn_pickTime_fin.setEnabled(enabled);
        btn_pickTime.setVisibility(invisible);
        btn_pickTime.setEnabled(enabled);
    }

    // set Category to the current event
    private void chooseCategory() {
        setupDialogCategory();
        categoryDialog.show();
    }

    private void setTagTextLine() {
        tv_tags.setText("");

        if(mTagList != null) {
            for (int i = 0; i < mTagList.size(); i++) {
                tv_tags.setText(tv_tags.getText() + " " + "#" + mTagList.get(i).getName());
            }
        }
    }

    private void setPriorityButton(int priority) {
        btn_priority.setText("Priorität " + String.valueOf(priority));
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
                    realm_event.setAction(action);
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

     // This method calls the EventsManager to store the current Event
    private void saveEvent() {
        //Extra temp_event to save an update into realm. We cannot overwrite an already existing realm_event
        temp_event = new Event();

        //Set title
        temp_event.setName(et_title.getText().toString());

        //Set all Notes
        temp_event.putNotes(filterNotes());

        //Set description
        temp_event.setDescription(et_description.getText().toString());

        //set startdate
        temp_event.setStart(currentStartDate.getTime());

        if(!mEventtype) {
            temp_event.setEnd(currentEndDate.getTime());
        }

        //set notification interval
        temp_event.setNotificationTime(notificationTimeInterval);

        //set priority
        temp_event.setPriority(mPriority);

        //set eventtype
        temp_event.setEventtype(mEventtype);

        //set tags
        temp_event.setTags(mTagList);

        //set category
        temp_event.setCategory(mCategory);

        if(realm_event == null) {
            //Save realm_event into Database
            realm_event = new Event();
            realm_event = temp_event;
            eventsManager.writeEvent(realm_event);

            //Creates notification
            if(useRememberNotification){
                NotificationAlarmHandler.setNotification(this, temp_event);
            }

            Toast.makeText(getApplicationContext(),"Saved Event", Toast.LENGTH_LONG).show();
        } else {

            //Cancels previous notification in any case.
            // Nothing happens if no notification was set. Making sure that there can't be any unexpected
            NotificationAlarmHandler.cancelNotification(this, temp_event);

            //Update realm_event into Database
            eventsManager.updateEvent(temp_event, eventID);

            //Creates notification if wanted
            if(useRememberNotification){
                NotificationAlarmHandler.setNotification(this, temp_event);
            }

            Toast.makeText(getApplicationContext(),"Updated Event", Toast.LENGTH_LONG).show();

            temp_event = null;
        }
        //After we saved the Event we will switch back to the last Activity
        finish();
    }

    //Method to filter notes with text. We don't want to store empty notes
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventsManager.close();
    }
}
