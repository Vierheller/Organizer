package de.grau.organizer;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import de.grau.organizer.classes.Category;
import de.grau.organizer.database.EventsManagerRealm;
import de.grau.organizer.database.interfaces.EventsManager;

/**
 * Created by Vierheller on 12.10.2016.
 */

public class Organizer extends Application {
    private static final String DEBUG_TAG = Organizer.class.getCanonicalName();
    private EventsManager eventsManager = new EventsManagerRealm();
    private static final String pref_key_first_run = "first_run";


    /**
     * Executed when App is opened.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        EventsManagerRealm.init(this);

        //Evaluate first run. Information is saved in SharedPreferences of the app.
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = preferences.getBoolean(pref_key_first_run, true);
        if(isFirstRun){
            Log.d(DEBUG_TAG, "First run of the app");
            onFirstRun();
            //We did everything in firstRun routine. Tell the system that next time the app is opened
            //firstrun routine should not be executed again.
            preferences.edit().putBoolean(pref_key_first_run, false).apply();
        }
    }

    /**
     * This method is called in the first run of the app.
     * It contains a number of functions to be called on first run
     */
    private void onFirstRun(){
        generateDefaultCategories();
    }

    /**
     *  Save the default Categories in Realm
     */
    private void generateDefaultCategories() {
        eventsManager.open();

        final Category newCategory = new Category("Allgemein");      // default Category
        newCategory.setDefaultCategory(true);

        eventsManager.writeCategory(newCategory);                    // default value
        eventsManager.writeCategory(new Category("Freizeit"));       // default value
        eventsManager.writeCategory(new Category("Arbeit"));         // default value

        eventsManager.close();
    }
}