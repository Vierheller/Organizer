package de.grau.organizer.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import de.grau.organizer.views.WeekViewEvent;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
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

    private void changeCalendarWeek(int weeks) {
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
class EventWeekView extends RelativeLayout {

    public static final String DEBUG_TAG = EventWeekView.class.getCanonicalName();

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

    public WeekFragment weekFragment;
    private View mCurrentHorizon;
    private int mHeight = 510;
    private ArrayList<WeekViewEvent> eventViews = new ArrayList<>();

    private void removeOldEventViews() {
        for (WeekViewEvent eventView : eventViews) {
            this.removeView(eventView);
        }
        eventViews.clear();
    }

    /**
     * This method draws the events on the view
     * @param events A list of events that you want to draw on the view
     */
    public void setupEvents(List<Event> events) {
        removeOldEventViews();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        Log.d(DEBUG_TAG, "Density: " + density);

        //The widths of the whole device in dp minus the 50density pixels because the view has a leftMargin of 50dp
        float width = metrics.widthPixels-(int)(50*density);

        Log.d(DEBUG_TAG, "Width: " + width);
        //the mHeight of the view is not in dp so we multiply it with the density
        double eHeight = mHeight*density;

        Log.d(DEBUG_TAG, "Height: " + eHeight);

        //a loop that draws every event in the events array
        for (Event event: events) {
            Log.d(DEBUG_TAG, "Adding: " + event);
            //returns: Sun: 1, Mon: 2, ...
            //We want Mon: 0, ..., Sun: 6 so we calc day+5 % 7
            int day = (event.getTime(Event.DateTime.START, Calendar.DAY_OF_WEEK) + 5) % 7;
            int startHour = event.getTime(Event.DateTime.START, Calendar.HOUR_OF_DAY);
            int startMinute = event.getTime(Event.DateTime.START, Calendar.MINUTE);
            Log.d(DEBUG_TAG, "startHour: "  + startHour + " startMinute: " + startMinute);

            //the time in between every event:
            Date startDateEvent = event.getStart();
            long duration = getDateDiff(startDateEvent, event.getEnd(), TimeUnit.MINUTES);
            Log.d(DEBUG_TAG, "day: "  + day + " duration: " + duration);

            //there are 7 days so the width of one event should be equal to a day
            //the height equals the height of the view / 24 (hours per day) times the time of the event
            int eventWidth = calcEventWidth(width, density);
            int eventHeight = calcEventHeight(eHeight, duration);
            int leftMargin = calcLeftMargin(day, width, density);
            int topMargin =  calcTopMargin(eHeight, startHour, startMinute);
            Log.d(DEBUG_TAG, "event Width: " + eventWidth + " event Height: " + eventHeight + " left Margin: " + leftMargin + " top Margin: " + topMargin);



            drawEventWeekView(event, leftMargin, topMargin, eventWidth, eventHeight);
            //the event is higher than the view -> the event goes over more than one day -> draw the event on the next column
            while(topMargin + eventHeight > eHeight && day != 6) {
                topMargin = 0;

                //set the startDate to the next day and 00:00:00 so we can calculate a new duration from this date and the event's end date
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDateEvent);
                cal.add(Calendar.DAY_OF_WEEK, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                startDateEvent = cal.getTime();
                duration = getDateDiff(startDateEvent, event.getEnd(), TimeUnit.MINUTES);
                topMargin = 0;
                eventHeight = calcEventHeight(eHeight, duration);
                day += 1;
                leftMargin = calcLeftMargin(day, width, density);


                drawEventWeekView(event, leftMargin, topMargin, eventWidth, eventHeight);
            }

        }
    }

    /**
     * Calculates the top Margin
     * Divides the height by 24 and multiplies the hour
     * Divides the height by 24*60 and multiplies it with the minutes
     * @param height
     * @param startHour
     * @param startMinute
     * @return
     */
    private int calcTopMargin(double height, int startHour, int startMinute) {
        return (int)((height/24) * (startHour) + ((height/(24*60))*startMinute));
    }

    /**
     * Calculates the event width of the whole view
     * Divides the width by 7 and substracts the density to not fill up the whole width
     * @param width the width of the whole view
     * @param density the density of the view that can be substracted so that the events do not take up the complete width
     * @return the calculated width
     */
    private int calcEventWidth(float width, @Nullable Float density) {
        if (density == null) {
            density = new Float(0);
        }
        return (int)(width/7 - (int)(2*density));
    }

    /**
     * calculates the leftMargin of an event relative to the width of the whole view
     * Divides the width by 7 and multiplies the days
     * @param day the day (Mon 0, ..., Sun 6) of when the view starts
     * @param width the width of the whole view
     * @param density the density of the view that can be added so that they are not aligned to the complete left
     * @return the calculated margin
     */
    private int calcLeftMargin(int day, float width, @Nullable Float density) {
        if (density == null) {
            density = new Float(0);
        }
        return (int)(day*(width/7) + density);
    }

    /**
     * Calculates the height of an event relative to the whole view
     * Divides the view's height by hour and minute (24*60) and multiplies the duration
     * @param eHeight The height of the complete view
     * @param duration The length of the event in minutes
     * @return the height calculated
     */
    private int calcEventHeight(double eHeight, long duration) {
        int height = (int)((eHeight/(24*60))*(duration));
        if (height < 45) {
            height = 45;
        }
        return height;
    }

    /**
     * draws the event on the view
     * this creates an instance of WeekViewEvent that displays the event
     * @param event the event to draw
     * @param leftMargin
     * @param topMargin
     * @param eventWidth
     * @param eventHeight
     */
    private void drawEventWeekView(Event event, int leftMargin, int topMargin, int eventWidth, int eventHeight) {
        WeekViewEvent eventView = new WeekViewEvent(this.getContext(),event);
        eventView.setBackgroundColor( event.getPriorityColor() );

        //set the alpha of the view
        int alpha = 255/3;
        Log.d(DEBUG_TAG, "alpha: " + alpha);
        eventView.getBackground().setAlpha( alpha );
        //fills the eventView with the events (sets the name and the category)
        eventView.fillGui(event);
        //set the parameter so the eventWeekView is aligned correctly
        EventWeekView.LayoutParams params = new EventWeekView.LayoutParams(eventWidth, eventHeight);
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;

        eventViews.add(eventView);
        this.addView(eventView, params);
    }

    Timer mHorizonTimer;

    /**
     * timer that draws the currentHorizon and updates it every 10 minutes
     */
    public void drawCurrentSpace() {
        currentTimeHorizon();
        if( mHorizonTimer == null ) {
            mHorizonTimer = new Timer();
        } else {
            mHorizonTimer.cancel();
            mHorizonTimer.purge();
            mHorizonTimer = null;
            mHorizonTimer = new Timer();
        }
        mHorizonTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d(DEBUG_TAG, "Changing to UI Thread");
                if(weekFragment!= null ){
                    if(weekFragment.getActivity() != null){
                        weekFragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(DEBUG_TAG, "will draw horizon on UI Thread");

                                currentTimeHorizon();
                                Log.d(DEBUG_TAG, "drew horizon on UI Thread");
                            }
                        });
                    }else{
                        Log.d(DEBUG_TAG, "weekfragment is not null, but there is no parent activity");
                    }
                }else{
                    Log.d(DEBUG_TAG, "weekfragment is null");
                }
            }
        }, mHorizonInterval, mHorizonInterval);
    }

    int mHorizonInterval = 1000*60*10;

    /**
     * draws 25 (from 00:00 to 24:00) lines with equal spacing
     */
    public void drawHorizontalSpaces() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        double eHeight = (mHeight*density)/24;
        Log.d(DEBUG_TAG, "eHeight: " + eHeight);
        for (int x = 1; x<24;x++) {
            View v = new View(this.getContext());
            EventWeekView.LayoutParams params = new EventWeekView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5);
            params.topMargin = (int) (eHeight*x);
            v.setBackgroundColor(Color.parseColor("#B3B3B3"));
            this.addView(v, params);
        }

        View topView = new View(this.getContext());
        EventWeekView.LayoutParams paramsTop = new EventWeekView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5);
        Log.d(DEBUG_TAG, "Last space is " + (int) (eHeight*0));
        paramsTop.topMargin = (int) (0*eHeight);
        topView.setBackgroundColor(Color.BLACK);
        this.addView(topView, paramsTop);

        View bottomView = new View(this.getContext());
        EventWeekView.LayoutParams paramsBot = new EventWeekView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10);
        Log.d(DEBUG_TAG, "Last space is " + (int) (eHeight*24));
        paramsBot.topMargin = (int) ((24*eHeight) - (2*density));
        bottomView.setBackgroundColor(Color.BLACK);
        this.addView(bottomView, paramsBot);
    }

    /**
     * draws a horizontal magenta line that displays the current time of the device
     */
    private void currentTimeHorizon() {
        Log.d(DEBUG_TAG, "draw horizon");
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        double eHeight = (mHeight*density)/24;
        Calendar cal = Calendar.getInstance();
        int curMinutes = cal.get(Calendar.MINUTE);
        int curHours = cal.get(Calendar.HOUR_OF_DAY);

        Log.d(DEBUG_TAG, "curMin: " + curMinutes + " curHours: " + curHours);
        EventWeekView.LayoutParams paramsCur = new EventWeekView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10);
        paramsCur.topMargin = (int) ((curHours*eHeight + curMinutes*(eHeight/60)));

        if(mCurrentHorizon != null) {
            this.removeView(mCurrentHorizon);
        }
        mCurrentHorizon = new View(this.getContext());
        mCurrentHorizon.setBackgroundColor(Color.MAGENTA);
        mCurrentHorizon.setAlpha(0.5f);
        this.addView(mCurrentHorizon, paramsCur);
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
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
