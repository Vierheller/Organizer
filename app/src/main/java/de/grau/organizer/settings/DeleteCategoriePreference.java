package de.grau.organizer.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.MultiSelectListPreference;
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
public class DeleteCategoriePreference extends MultiSelectListPreference {
    private static final String DEBUG_TAG = DeleteCategoriePreference.class.getCanonicalName();
    private static EventsManager eventsManager = new EventsManagerRealm();

    private List<Category> categories;
    private boolean[] entryChecked;
    private CharSequence[] mEntries;

    public DeleteCategoriePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

//        setEntries(getAllCategorieNames());
        mEntries = getAllCategorieNames();
        entryChecked = new boolean[mEntries.length];

        setEntries(mEntries);
        setEntryValues(mEntries);
    }
    
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
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        DialogInterface.OnMultiChoiceClickListener listener = new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int which, boolean val) {
                entryChecked[which] = val;
            }
        };
        builder.setMultiChoiceItems(mEntries, entryChecked, listener);
    }

    // Get List of all categorie names
    private CharSequence[] getAllCategorieNames() {
        final CharSequence[] categorie_names;         // saves only category names
                    // saves the hole category

        eventsManager.open();

        categories = eventsManager.loadAllCategories();     // get all categories from
        categorie_names = new CharSequence[categories.size()];

        for(int i=0; i<categories.size(); i++) {
            categorie_names[i] = categories.get(i).getName();
        }

        eventsManager.close();
        return categorie_names;
    }

    // Delete a Categorie
    private boolean deleteCategory(String categoryId) {
        boolean return_value;
        eventsManager.open();
        return_value = eventsManager.deleteCategory(categoryId);
        eventsManager.close();
        return return_value;
    }
}
