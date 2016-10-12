package de.grau.organizer.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;


import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import de.grau.organizer.database.EventsManagerRealm;
import de.grau.organizer.adapters.SectionsPagerAdapter;
import de.grau.organizer.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        activityContext = this;
//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditorActivity(null);
            }
        });

        eventsManager = new EventsManagerRealm();
    }

    public void startTaskActivity(String eventID){
        Intent intent = new Intent(activityContext, TaskActivity.class);
        if(eventID!=null){
            intent.putExtra(TaskActivity.INTENT_PARAM_ID, eventID);
        }
        startActivity(intent);
    }

    public void startEditorActivity(String eventID){
        Intent intent = new Intent(activityContext, EditorActivity.class);
        if(eventID!=null){
            intent.putExtra(EditorActivity.INTENT_PARAM_EVENTID, eventID);
        }
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(DEBUG_TAG, "OnStart");
        eventsManager.open(this);

        Intent intent = getIntent();
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            handleSearchIntent(intent);
        }
    }

    private void handleSearchIntent(Intent intent) {
        String query = intent.getStringExtra(SearchManager.QUERY);
        if(!query.isEmpty()) {
            List<String> searchResults = eventsManager.searchEvents(query);
            ShowSearchResults(searchResults);
        }else {
            Toast.makeText(this,R.string.search_no_query,Toast.LENGTH_LONG).show();
        }

    }

    private void ShowSearchResults(List<String> searchResults) {
        if (searchResults.size() > 0){
            //ToDo change currently simple List to https://github.com/afollestad/material-dialogs#simple-list-dialogs
            //ToDo coloring corresponding to Theme
            new MaterialDialog.Builder(this)
                    .title(R.string.search_title)
                    .items(searchResults)
                    .titleColorRes(R.color.colorAccent)
                    //.backgroundColorRes(R.color.colorPrimary)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            Log.d(DEBUG_TAG,"Search result chosen: "+text+". Trying to open corresponding Event");

                        }
                    })
                    .cancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Log.d(DEBUG_TAG,"Search dialog was canceled");
                        }
                    })
                    .show();
        } else{
            Toast.makeText(this,R.string.search_no_result,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(DEBUG_TAG, "OnStop");
        eventsManager.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
