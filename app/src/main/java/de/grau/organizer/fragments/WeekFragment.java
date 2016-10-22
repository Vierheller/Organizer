package de.grau.organizer.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.TextView;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;

import de.grau.organizer.views.WeekViewEvent;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.grau.organizer.R;
import de.grau.organizer.activities.TabActivity;
import de.grau.organizer.classes.Event;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeekFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekFragment extends Fragment {
    private static final String LOG_TAG = WeekFragment.class.getCanonicalName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EventWeekView mWeekView;
    private LinearLayout mWeekDays;
    private LinearLayout mWeekTime;

    private TabActivity mActivity;
    public WeekFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeekFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeekFragment newInstance(String param1, String param2) {
        WeekFragment fragment = new WeekFragment();
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
        View view =  inflater.inflate(R.layout.fragment_week, container, false);
        mWeekDays = (LinearLayout) view.findViewById(R.id.week_view_days);
        mWeekTime = (LinearLayout) view.findViewById(R.id.week_view_time);
        mWeekDays.setBackgroundColor(Color.RED);
        mWeekTime.setBackgroundColor(Color.GREEN);

        mWeekView = (EventWeekView) view.findViewById(R.id.event_week_view);
//        mWeekView.setStaticEvent();
        setupWeek();

        return view;
    }

    public void completedLayout() {
        setupWeek();
    }

    private void setupWeek() {
        setupWeekTime();
        setupWeekDays();
        setupWeekView();
    }

    private void setupWeekDays() {
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for(String day: days) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(50, 50);
            param.weight = 1;
            TextView dayTV = new TextView(this.getContext());
            dayTV.setText(day);
            dayTV.setLayoutParams(param);
            dayTV.setGravity(Gravity.CENTER);
            mWeekDays.addView(dayTV);
        }
        mWeekDays.setWeightSum(days.length);
    }

    private void setupWeekTime() {
        int interval = 4;
        ArrayList<String> times = new ArrayList<>();
        for(int x = 0; x <= 24; x+=interval) {
            String formatted = String.format("%02d", x);
            String time = formatted + ":00";
            times.add(time);
        }
        for (String time : times) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 20);
            param.weight = 1;
            TextView timeTV = new TextView(this.getContext());
            timeTV.setText(time);
            timeTV.setGravity(Gravity.CENTER_HORIZONTAL);
            timeTV.setLayoutParams(param);
            mWeekTime.addView(timeTV);
        }
        mWeekTime.setWeightSum(times.size());
    }

    private void setupWeekView() {
        Calendar calStart = Calendar.getInstance();
        calStart.set(Calendar.DAY_OF_WEEK, calStart.getActualMinimum(Calendar.DAY_OF_WEEK));
        calStart.set(Calendar.HOUR_OF_DAY, calStart.getActualMinimum(Calendar.HOUR_OF_DAY));
        calStart.set(Calendar.MINUTE, calStart.getActualMinimum(Calendar.MINUTE));
        calStart.set(Calendar.SECOND, calStart.getActualMinimum(Calendar.SECOND));

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.DAY_OF_WEEK, calEnd.getActualMaximum(Calendar.DAY_OF_WEEK));
        calEnd.set(Calendar.HOUR_OF_DAY, calEnd.getActualMaximum(Calendar.HOUR_OF_DAY));
        calEnd.set(Calendar.MINUTE, calEnd.getActualMaximum(Calendar.MINUTE));
        calEnd.set(Calendar.SECOND, calEnd.getActualMaximum(Calendar.SECOND));

        List<Event> events = mActivity.eventsManager.getEvents(calStart.getTime(), calEnd.getTime());

        Log.d(LOG_TAG, "Events to show: " + events.size());
        mWeekView.setupEvents(events);
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
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

class EventWeekView extends RelativeLayout {
    private static final String LOG_TAG = EventWeekView.class.getCanonicalName();

    public EventWeekView(Context context) {
        super(context);
    }

    public EventWeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventWeekView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EventWeekView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setStaticEvent() {
        View week_view = this.inflate(this.getContext(), R.layout.week_view_event_layout, null);
        //there are 7 days so the width of one event should be equal to a day
        //the height equals the height of the view / 24 (hours per day) times the time of the event
        EventWeekView.LayoutParams params = new EventWeekView.LayoutParams(300, 300);
        params.leftMargin = 40;
        params.topMargin = 50;
        this.addView(week_view, params);
    }


    public void setupEvents(List<Event> events) {
        int width = 950;
//        int width = this.getWidth();
        Log.d(LOG_TAG, width + "");
        int height = 1200;
//        int height = this.getHeight();
        Log.d(LOG_TAG, height + "");

        for (Event event: events) {
            Log.d(LOG_TAG, "Adding: " + event);
            int day = event.getTime(Event.DateTime.START, Calendar.DAY_OF_WEEK) - 1;
            int startHour = event.getTime(Event.DateTime.START, Calendar.HOUR_OF_DAY);
            int startMinute = event.getTime(Event.DateTime.START, Calendar.MINUTE);

            //the time in between every event:
            long duration = getDateDiff(event.getStart(), event.getEnd(), TimeUnit.HOURS);
            Log.d(LOG_TAG, "day: "  +day);

            //there are 7 days so the width of one event should be equal to a day
            //the height equals the height of the view / 24 (hours per day) times the time of the event
            int eventWidth = width/7;
            int eventHeight = (int)height/24*(int)duration;
            if (eventHeight < 100) {
                eventHeight = 100;
            }
            int leftMargin = day*eventWidth;
            int topMargin = height/24*startHour + height/(24*60)*startMinute;
            Log.d(LOG_TAG, "eW: " + eventWidth + " eH: " + eventHeight + " lM: " + leftMargin + " tM: " + topMargin);


            WeekViewEvent week_view = new WeekViewEvent(this.getContext());
            week_view.setBackgroundColor(event.getPriorityColor());
            week_view.fillGui(event);
            EventWeekView.LayoutParams params = new EventWeekView.LayoutParams(eventWidth, eventHeight);
            params.leftMargin = leftMargin;
            params.topMargin = topMargin;
            this.addView(week_view, params);
        }
    }

    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
}