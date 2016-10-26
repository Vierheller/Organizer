package de.grau.organizer.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.grau.organizer.classes.Category;
import de.grau.organizer.database.EventsManagerRealm;
import de.grau.organizer.database.interfaces.EventsManager;

import static android.R.attr.entries;

/**
 * Created by Vierheller on 25.10.2016.
 */
public class DeleteCategoriePreference extends MultiSelectListPreference implements Preference.OnPreferenceClickListener{
    private static final String DEBUG_TAG = DeleteCategoriePreference.class.getCanonicalName();
    private static EventsManager eventsManager = new EventsManagerRealm();

    private List<Category> categories;
    private boolean[] entryChecked;
    private CharSequence[] mEntries;

    public DeleteCategoriePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setValues();
        setPositiveButtonText("Delete");
        setOnPreferenceClickListener(this);
    }

    /**
     * Updates the Dialog with categories from Database
     */
    private void setValues(){
        mEntries = getAllCategorieNames();
        entryChecked = new boolean[mEntries.length];

        setEntries(mEntries);
        setEntryValues(mEntries);
    }

    /**
     * Executed when either "close" or "ok" is clicked in dialog screen
     * @param positiveResult
     */
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            Log.d(DEBUG_TAG, "Closed Categorie");
            for (int i = 0; i<entryChecked.length; i++){
                if(entryChecked[i]==true){
                    Category clickedCategorie = categories.get(i);
                    Log.d(DEBUG_TAG,  clickedCategorie.getName());
                    deleteCategory(clickedCategorie.getID());
                }
            }
        }
        setValues();
    }

    /**
     * Override Dialog builder to setup a ChoiseClickListener. This is important to know which Categorie is selected.
     * The selected Categories will be deleted on a click on "OK"
     * @param builder
     */
    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        DialogInterface.OnMultiChoiceClickListener listener = new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int which, boolean val) {
                entryChecked[which] = val;
            }
        };
        builder.setMultiChoiceItems(mEntries, entryChecked, listener);
    }

    /**
     * Get List of all categorie names from database
     * @return
     */
    private CharSequence[] getAllCategorieNames() {
        final CharSequence[] categorie_names;         // saves only category names

        eventsManager.open();

        categories = eventsManager.loadAllCategories();         // get all categories from
        categorie_names = new CharSequence[categories.size()];  //Dialog need an array of CharSequence

        //Store each categorie names in categore_name array
        for(int i=0; i<categories.size(); i++) {
            categorie_names[i] = categories.get(i).getName();
        }

        eventsManager.close();
        return categorie_names;
    }

    /**
     * Delete a Categorie
     * @param categoryId
     * @return
     */
    private boolean deleteCategory(String categoryId) {
        boolean return_value;
        eventsManager.open();
        return_value = eventsManager.deleteCategory(categoryId);
        eventsManager.close();
        return return_value;
    }

    /**
     * This is executed when the Categorie is selected. Used to keep Dialog up to date with categories
     * @param preference
     * @return
     */
    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference == this){
            Log.d(DEBUG_TAG, "Clicked deleteDialog");
            setValues();
        }
        return true;
    }
}
