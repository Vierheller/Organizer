package de.grau.organizer.settings;

import android.content.Context;
import android.preference.MultiSelectListPreference;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.grau.organizer.classes.Category;
import de.grau.organizer.database.EventsManagerRealm;
import de.grau.organizer.database.interfaces.EventsManager;

/**
 * Created by Vierheller on 25.10.2016.
 */
public class DeleteCategoriePreference extends MultiSelectListPreference {
    private static final String DEBUG_TAG = DeleteCategoriePreference.class.getCanonicalName();
    private static EventsManager eventsManager = new EventsManagerRealm();

    public DeleteCategoriePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

//        setEntries(getAllCategorieNames());
        CharSequence[] mEntries = getAllCategorieNames();
        setEntries(mEntries);

    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            Log.d(DEBUG_TAG, "Closed Categorie");
        }
    }

    // Get List of all categorie names
    private static CharSequence[] getAllCategorieNames() {
        final CharSequence[] categorie_names;         // saves only category names
        final List<Category> categories;            // saves the hole category

        eventsManager.open();

        categories = eventsManager.loadAllCategories();     // get all categories from
        categorie_names = new CharSequence[categories.size()];

        for(int i=0; i<categories.size(); i++) {
            categorie_names[i] = categories.get(i).getName();
        }

        eventsManager.close();
        return categorie_names;
    }
}
