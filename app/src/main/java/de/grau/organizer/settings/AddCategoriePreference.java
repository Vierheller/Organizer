package de.grau.organizer.settings;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import de.grau.organizer.classes.Category;
import de.grau.organizer.database.EventsManagerRealm;
import de.grau.organizer.database.interfaces.EventsManager;

/**
 * Created by Vierheller on 25.10.2016.
 */
public class AddCategoriePreference extends EditTextPreference {
    private String mCurrentValue = null;
    private static final String DEBUG_TAG = AddCategoriePreference.class.getCanonicalName();
    private static EventsManager eventsManager = new EventsManagerRealm();

    public AddCategoriePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddCategoriePreference(Context context) {
        super(context);
    }

    public AddCategoriePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AddCategoriePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            String categorieName = getEditText().getText().toString();
            Log.d(DEBUG_TAG, "Closed Edittext");
            Log.d(DEBUG_TAG, categorieName);
            createCategory(categorieName, getContext());
        }
    }

    // Creates a new Category in the Database
    private static void createCategory(String category_name, Context context) {
        eventsManager.open();
        // Check if Category already exists
        if(!category_name.trim().isEmpty() && eventsManager.getCategoryByName(category_name) == null) {
            eventsManager.writeCategory(new Category(category_name));
            Toast.makeText(context, "Category has been saved!", Toast.LENGTH_LONG).show();
        }
        eventsManager.close();
    }



    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
       mCurrentValue = "";
    }

}
