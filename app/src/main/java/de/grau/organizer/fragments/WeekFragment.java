package de.grau.organizer.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import de.grau.organizer.views.EventWeekView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

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
    public static final int HOUR_HEIGHT = 10;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EventWeekView mWeekView;
    private LinearLayout mWeekDays;
    private LinearLayout mWeekTime;
    private ScrollView mScrollView;
    private TextView mWeekInfoTextView;

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

    private ImageButton mLeftDecrementButton;
    private ImageButton mRightIncrementButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_week, container, false);
        mWeekDays = (LinearLayout) view.findViewById(R.id.week_view_days);
        mWeekTime = (LinearLayout) view.findViewById(R.id.week_view_time);
        mScrollView = (ScrollView) view.findViewById(R.id.week_fragment_scroll);

        mLeftDecrementButton = (ImageButton) view.findViewById(R.id.left_decrement_week_button);
        mRightIncrementButton = (ImageButton) view.findViewById(R.id.right_increment_week_button);
        mWeekInfoTextView = (TextView) view.findViewById(R.id.week_info_text_view);

        mLeftDecrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCalendarWeek(-1);
            }
        });

        mRightIncrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCalendarWeek(1);
            }
        });

        mWeekView = (EventWeekView) view.findViewById(R.id.event_week_view);
        mWeekView.weekFragment = this;
        setupWeek();

        return view;
    }

    private void scrollToPosition(final int destinationPos, int delay) {
        Handler h = new Handler();

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, destinationPos);
            }
        }, delay);
    }

    public void completedLayout() {
        setupWeek();
    }

    private void setupWeek() {
        mWeekView.drawHorizontalSpaces();
        setupWeekTime();
        setupWeekDays();
        setupWeekView();
        mWeekView.drawCurrentSpace();
    }

    private void setupWeekDays() {
        String[] days = {getString(R.string.monday), getString(R.string.tuesday),
                getString(R.string.wednesday), getString(R.string.thursday),
                getString(R.string.friday), getString(R.string.saturday), getString(R.string.sunday)};
        for(String day: days) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(50, 50);
            param.weight = 1;
            TextView dayTV = new TextView(this.getContext());
            dayTV.setText(day);
            dayTV.setTypeface(null,Typeface.BOLD);
            dayTV.setLayoutParams(param);
            dayTV.setGravity(Gravity.CENTER);
            mWeekDays.addView(dayTV);
        }
        mWeekDays.setWeightSum(days.length);
    }

    private void setupWeekTime() {
        int interval = 2;
        ArrayList<String> times = new ArrayList<>();
        for(int x = 0; x <= 24; x+=interval) {
            String formatted = String.format(Locale.GERMANY,"%02d", x);
            String time = formatted + ":00";
            times.add(time);
        }
        for (String time : times) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, interval*HOUR_HEIGHT);
            param.weight = 1;
            TextView timeTV = new TextView(this.getContext());
            timeTV.setText(time);
            timeTV.setGravity(Gravity.CENTER_HORIZONTAL);
            timeTV.setLayoutParams(param);
            mWeekTime.addView(timeTV);
        }
        mWeekTime.setWeightSum(times.size());
    }

    private Calendar mCalStart;
    private Calendar mCalEnd;

    private void setCalendar() {
        mCalStart = GregorianCalendar.getInstance(Locale.GERMANY);
        mCalStart.setFirstDayOfWeek(Calendar.MONDAY);
        mCalStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        mCalStart.set(Calendar.HOUR_OF_DAY, mCalStart.getActualMinimum(Calendar.HOUR_OF_DAY));
        mCalStart.set(Calendar.MINUTE, mCalStart.getActualMinimum(Calendar.MINUTE));
        mCalStart.set(Calendar.SECOND, mCalStart.getActualMinimum(Calendar.SECOND));

        mCalEnd = GregorianCalendar.getInstance(Locale.GERMANY);
        mCalEnd.setFirstDayOfWeek(Calendar.MONDAY);
        mCalEnd.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        mCalEnd.set(Calendar.HOUR_OF_DAY, mCalEnd.getActualMaximum(Calendar.HOUR_OF_DAY));
        mCalEnd.set(Calendar.MINUTE, mCalEnd.getActualMaximum(Calendar.MINUTE));
        mCalEnd.set(Calendar.SECOND, mCalEnd.getActualMaximum(Calendar.SECOND));
    }

    /**
     * This method can be used to redraw the events or increment, decrement the view
     * @param weeks 0 to redraw, 1 to go to the next week, -1 is the week before the current set week
     */
    public void changeCalendarWeek(@IntRange(from=-1,to=1) int weeks) {
        mCalStart.add(Calendar.WEEK_OF_YEAR, weeks);
        mCalEnd.add(Calendar.WEEK_OF_YEAR, weeks);

        List<Event> events = mActivity.eventsManager.getEvents(mCalStart.getTime(), mCalEnd.getTime());
        SimpleDateFormat dt = new SimpleDateFormat("dd.MM");
        String dateStart = dt.format(mCalStart.getTime());
        SimpleDateFormat dty = new SimpleDateFormat("dd.MM.yyyy");
        String dateEnd = dty.format(mCalEnd.getTime());
        Log.d(LOG_TAG, "Events to show: " + events.size());
        mWeekInfoTextView.setText("Woche: " + mCalStart.get(Calendar.WEEK_OF_YEAR) + "\n" + dateStart + " - " + dateEnd);
        mWeekView.setupEvents(events);
        mWeekView.drawCurrentSpace();
    }

    private void setupWeekView() {
        setCalendar();
        changeCalendarWeek(0);
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
