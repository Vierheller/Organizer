package de.grau.organizer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.grau.organizer.R;

public class EditorActivity extends AppCompatActivity {
    EditText et_title;
    Button btn_save;
    Button btn_pickDate;
    Button btn_pickTime;
    Button btn_addNote;
    Button btn_chooseAction;
    Button btn_pickNotifyDelay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        initialize();
    }

    /**
     * This method references every necessary GUI-Element
     */
    private void initialize(){
        et_title =              (EditText) findViewById(R.id.editor_event_name);
        btn_pickDate =          (Button) findViewById(R.id.editor_btn_pickdate);
        btn_pickTime =          (Button) findViewById(R.id.editor_btn_picktime);
        btn_pickNotifyDelay =   (Button) findViewById(R.id.editor_btn_picknotifydelay);
        btn_chooseAction =      (Button) findViewById(R.id.editor_btn_chooseaction);
        btn_addNote =           (Button) findViewById(R.id.editor_btn_addnote);
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

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifyEvent()){
                    saveEvent();
                }
            }
        });
    }

    /**
     * This method calls the {@EventsManager} to store the current Event
     */
    private void saveEvent() {
    }

    /**
     * This method validates every input and return true if the Event can be saved
     * @return isValid
     */
    private boolean verifyEvent() {
        return true;
    }
}
