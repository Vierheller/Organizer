package de.grau.organizer.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import de.grau.organizer.R;
import de.grau.organizer.adapters.SectionsPagerAdapter;
import de.grau.organizer.classes.Category;
import de.grau.organizer.classes.Event;
import de.grau.organizer.database.EventsManagerRealm;
import de.grau.organizer.database.interfaces.EventsManager;

public class TabActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = AppCompatActivity.class.getCanonicalName();
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Context activityContext;
    public EventsManager eventsManager;
    public CalendarDay mCalDay = null;
    private List<Event> mSearchResults;
    private MaterialDialog md_event_selector;
    SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        activityContext = this;

        setupFragements();

        setupFloatingActionButton();

        eventsManager = new EventsManagerRealm();
        eventsManager.open();

    }

    /**
     * onNewIntent receives and processes new intents for this activity
     * @param intent used for performing a search
     */
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }


    /**
     * Method setups the Fragments of TabActivity (month, week, list)
     * It generates the tablayout with it three sections
     */
    public void setupFragements() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * Method setups the floatingActionButton
     * It is used to start a dialogselection. This dialog asks the user
     * if he wants to create a task or an event
     */
    public void setupFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);       // Eventkind selector

        md_event_selector = new MaterialDialog.Builder(this)
                .title("Typ auswählen")
                .content("Möchtest du einen Termin oder eine Aufgabe anlegen?")
                .positiveText("Aufgabe")
                .negativeText("Termin")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startEditorActivity(true);      // load without startDate
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startEditorActivity(false);     // load with startDate
                    }
                })
                .build();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                md_event_selector.show();
            }
        });
    }

    /**
     * Prepare an intent and start the TaskActivity
     * The parameter eventID will become overgiven with the intent to the TaskActivity
     *
     * @param eventID
     */
    public void startTaskActivity(String eventID){
        Intent intent = new Intent(activityContext, TaskActivity.class);
        if(eventID!=null){
            intent.putExtra(TaskActivity.INTENT_PARAM_ID, eventID);
        }
        startActivity(intent);
    }

    /**
     * Prepare an intent and start the EditorActivity
     * The parameter eventID will become overgiven with the intent to the EditorActivity
     * @param eventID
     */
    public void startEditorActivity(String eventID){
        Intent intent = new Intent(activityContext, EditorActivity.class);
        if(eventID!=null){
            intent.putExtra(EditorActivity.INTENT_PARAM_EVENTID, eventID);
        }
        startActivity(intent);
    }

    /**
     * Prepare an intent and start the EditorActivity
     * The parameter eventtype will become overgiven with the intent to the EditorActivity
     * @param eventtype
     */
    public void startEditorActivity(boolean eventtype){
        Intent intent = new Intent(activityContext, EditorActivity.class);
        intent.putExtra(EditorActivity.INTENT_PARAM_EVENTTYPE, eventtype);
        startActivity(intent);
    }

    /**
     * Prepare an intent and start the SettingsActivity
     */
    public void startSettingsActivity() {
        Intent intent = new Intent(activityContext, SettingsActivity.class);
        startActivity(intent);
    }


    /**
     * Handles the intent for the searchquery
     * @param intent
     */
    private void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            handleSearchIntent(intent);
        }
    }

    /**
     * Handles the search intent and start the search
     * It will search the database with the overgiven String and call the ShowSearchResults method
     * @param intent
     */
    private void handleSearchIntent(Intent intent) {
        String query = intent.getStringExtra(SearchManager.QUERY);

        if(!query.isEmpty()) {

            mSearchResults = eventsManager.searchEvents(query,50);

            if (mSearchResults.size() == 50) Toast.makeText(this, R.string.SearchResultsToast,Toast.LENGTH_LONG).show();

            List<String> result = new ArrayList<>();
            for(Event e: mSearchResults){
                //result.add(e.getName()+" | "+e.getStart()+" "+e.getEnd());
                result.add(e.getShortSummary());
            }

            ShowSearchResults(result);
        }else {
            Toast.makeText(this,R.string.search_no_queryToast,Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Shows a MaterialDialog with the results of the search
     * The searchResults must be setted by the parameter of this method.
     *
     * @param searchResults
     */
    private void ShowSearchResults(List<String> searchResults) {
        if (searchResults.size() > 0){
            //ToDo change currently simple List to https://github.com/afollestad/material-dialogs#simple-list-dialogs
            //ToDo coloring corresponding to Theme
            new MaterialDialog.Builder(this)
                    .title(R.string.search_title)
                    .items(searchResults)
                    .iconRes(R.drawable.ic_search_black_24dp)
                    .titleColorRes(R.color.colorPrimary)
                    .dividerColorRes(R.color.colorAccent)
                    .positiveColorRes(R.color.colorPrimary)
                    //.backgroundColorRes(R.color.colorPrimary)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            Log.d(DEBUG_TAG,"Search result chosen: "+text+". Trying to init corresponding Event");
                            Event e = mSearchResults.get(which);
                            startTaskActivity(e.getId());
                        }
                    })
                    .cancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Log.d(DEBUG_TAG,"Search dialog was canceled");
                        }
                    })
                    .positiveText(R.string.search_schließen)
                    .show();
        } else{
            Toast.makeText(this,R.string.search_no_resultToast,Toast.LENGTH_LONG).show();
        }
    }


    /**
     * This method creates the options menu of the toolbar with its entries.
     * Those entries become bound to their methods here
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView =
                (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        //Changing style of SearchView
        setupSearchView();
        
        return true;
    }

    /**
     * This method will change the color of the searchViewIcon to white
     * Reason is that it is not possible to change it by xml.
     * We are trying to keep th Theme consistent
     */
    private void setupSearchView(){
        // searchview is a parent of a LinearLayout
        if( mSearchView.getChildAt(0) instanceof LinearLayout){
            LinearLayout layout = (LinearLayout) mSearchView.getChildAt(0);
            for(int i = 0 ; i < layout.getChildCount();i++){
                //This LinearLayout contains the ImageView that is displayed in the action bar
                if(layout.getChildAt(i) instanceof AppCompatImageView){
                    AppCompatImageView iv = (AppCompatImageView) layout.getChildAt(i);
                    //Setting icon of imageView in action bar
                    iv.setImageResource(R.drawable.ic_search_white_24dp);
                }
            }
        }
    }

    /**
     * This listener will be called when an optionsitem becomes selected.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startSettingsActivity();
        }

        // This is only a developer option
        if (id == R.id.action_test_events) {
            generateTestEvents(100);
            Toast.makeText(this, R.string.createTestEventsToast,Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is only a developer feature.
     * It creates 100 random events
     *
     * @param count
     */
    public void generateTestEvents(int count){
        Category testcategory = new Category("Test Category");
        for (int i = 0; i < count; i++) {
            Random r = new Random();
            Event tmp = new Event();
            tmp.setName("Test #"+i);
            tmp.setCategory(testcategory);
            tmp.setDescription("Test Description #"+i);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH,r.nextInt(cal.getActualMaximum(Calendar.MONTH)-cal.getActualMinimum(Calendar.MONTH))+cal.getActualMinimum(Calendar.MONTH));
            cal.set(Calendar.DAY_OF_MONTH,i%cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.HOUR_OF_DAY,r.nextInt(cal.getActualMaximum(Calendar.HOUR_OF_DAY)-cal.getActualMinimum(Calendar.HOUR_OF_DAY))+cal.getActualMinimum(Calendar.HOUR_OF_DAY));
            tmp.setStart(cal.getTime());
            cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)+1);
            tmp.setEnd(cal.getTime());

            tmp.setDescription("Test Description #"+i);


            tmp.setPriority(r.nextInt(5 - 1) + 1);

            eventsManager.writeEvent(tmp);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventsManager.close();
    }
}
