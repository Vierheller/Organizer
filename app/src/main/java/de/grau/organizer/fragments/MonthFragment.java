package de.grau.organizer.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.grau.organizer.HighlightDecorator;
import de.grau.organizer.R;
import de.grau.organizer.activities.TabActivity;
import de.grau.organizer.adapters.EventsListAdapter;
import de.grau.organizer.classes.Event;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MonthFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MonthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {
    private static final String LOG_TAG = MonthFragment.class.getCanonicalName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TabActivity mActivity;
    private ListView mListView;
    private MaterialCalendarView mCalendarView;
    private EventsListAdapter mListViewAdapter;

    private OnFragmentInteractionListener mListener;

    public MonthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthFragment newInstance(String param1, String param2) {
        MonthFragment fragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        selectDate(date);
    }

    private void selectDate(CalendarDay date){
        List<Event> events = mActivity.eventsManager.getEvents(date);
        //TODO: Load events into listView
        for (Event event : events) {
            Log.d(LOG_TAG, event.toString());
        }
        if ( events != null ) {
            mListViewAdapter.setEventList(events);
        } else {
            mListViewAdapter.setEventList(new ArrayList<Event>());
        }
        mListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.mActivity = (TabActivity)getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        mCalendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        mListView = (ListView) view.findViewById(R.id.month_list_view);

        mListViewAdapter = new EventsListAdapter(mActivity, R.layout.eventslist_row, new ArrayList<Event>());

        mListView.setAdapter(mListViewAdapter);

        mCalendarView.setOnDateChangedListener(this);
        mCalendarView.setOnMonthChangedListener(this);

        //Set today as selected day
        mCalendarView.setDateSelected(Calendar.getInstance(), true);
        selectDate(CalendarDay.from(Calendar.getInstance()));

        Calendar cal = Calendar.getInstance();
        mCalendarView.getCurrentDate().copyTo(cal);

        setupCalendar(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));


        return view;
    }

    private void setupCalendar(int month, int year) {

        HighlightDecorator prio4 = new HighlightDecorator(mActivity.eventsManager.getEvents(month,year,4),4);
        HighlightDecorator prio3 = new HighlightDecorator(mActivity.eventsManager.getEvents(month,year,3),3);
        HighlightDecorator prio2 = new HighlightDecorator(mActivity.eventsManager.getEvents(month,year,2),2);
        HighlightDecorator prio1 = new HighlightDecorator(mActivity.eventsManager.getEvents(month,year,1),1);
        mCalendarView.addDecorators(prio4,prio3,prio2,prio1);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        Calendar cal = Calendar.getInstance();
        date.copyTo(cal);
        setupCalendar(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
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
