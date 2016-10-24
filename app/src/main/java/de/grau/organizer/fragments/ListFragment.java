package de.grau.organizer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

import de.grau.organizer.R;
import de.grau.organizer.activities.TabActivity;
import de.grau.organizer.adapters.EventsListAdapter;
import de.grau.organizer.adapters.MyRecyclerViewAdapter;
import de.grau.organizer.adapters.listeners.OnClickListener;
import de.grau.organizer.classes.Event;
import de.grau.organizer.notification.NotificationAlarmHandler;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction eventsDataSet.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String DEBUG_TAG = ListFragment.class.getCanonicalName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MyRecyclerViewAdapter mAdapter;

    private TabActivity mActivity;

    //Everything for RecyclerView
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private OnFragmentInteractionListener mListener;

    private List<Event> eventsDataSet;


    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mActivity = (TabActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Get each event row from Database
        RealmResults<Event> realmList  = mActivity.eventsManager.getRealmEventList();
        eventsDataSet = realmList;

        //This is a listener to keep DataSet always up to Date
        realmList.addChangeListener(new RealmChangeListener<RealmResults<Event>>() {
            @Override
            public void onChange(RealmResults<Event> element) {
                mAdapter.notifyDataSetChanged();
            }
        });

        // specify an adapter (see also next example)
        mAdapter = new MyRecyclerViewAdapter(eventsDataSet, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);



        setupListeners();

        return view;
    }

    private void setupListeners() {
        mAdapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                String id = eventsDataSet.get(position).getId();
                Log.d(DEBUG_TAG, id);
                mActivity.startTaskActivity(id);
            }
        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String id = eventsDataSet.get(i).getId();
//                Log.d(DEBUG_TAG, id);
//                mActivity.startTaskActivity(id);
//            }
//        });
//
        registerForContextMenu(mRecyclerView);
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int listElementPosition = item.getOrder();
        Event selectedEvent = eventsDataSet.get(listElementPosition);
        switch(item.getItemId()) {
            case MyRecyclerViewAdapter.ACTION_EDIT_ID:
                mActivity.startEditorActivity(selectedEvent.getId());
                return true;
            case MyRecyclerViewAdapter.ACTION_NOTIFICATION_ID:
                NotificationAlarmHandler.cancelNotification(mActivity, selectedEvent);
                mActivity.eventsManager.deleteEvent(selectedEvent.getId());
                //Snackbar.make(listView, "Sucessfully deleted!", Snackbar.LENGTH_LONG).show();
                return true;
            case MyRecyclerViewAdapter.ACTION_DELETE_ID:
                NotificationAlarmHandler.cancelNotification(mActivity, selectedEvent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        return;
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
