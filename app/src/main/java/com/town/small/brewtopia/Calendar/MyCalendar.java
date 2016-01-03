
package com.town.small.brewtopia.Calendar;

        import android.content.Context;
        import android.content.res.TypedArray;
        import android.graphics.Color;
        import android.graphics.Typeface;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.GridView;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import com.town.small.brewtopia.DataClass.CurrentUser;
        import com.town.small.brewtopia.DataClass.DataBaseManager;
        import com.town.small.brewtopia.DataClass.ScheduledBrewSchema;
        import com.town.small.brewtopia.R;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.HashSet;
        import java.util.List;
        import java.util.Locale;

public class MyCalendar extends LinearLayout
{
    // Log cat tag
    private static final String LOG = "MyCalendar";

    // how many days to show, defaults to six weeks, 42 days
    private static final int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";
    private SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    // date format
    private String dateFormat;

    // current displayed month
    private Calendar currentDate = Calendar.getInstance();

    //event handling
    private EventHandler eventHandler = null;

    // internal components
    private LinearLayout header;
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;
    private GridView grid;

    // seasons' rainbow
    int[] rainbow = new int[] {
            R.color.summer,
            R.color.fall,
            R.color.winter,
            R.color.spring
    };

    // month-season association (northern hemisphere, sorry australia :)
    int[] monthSeason = new int[] {2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};

    public MyCalendar(Context context)
    {
        super(context);
    }

    public MyCalendar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initControl(context, attrs);
    }

    public MyCalendar(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context, AttributeSet attrs)
    {
        Log.e(LOG, "Entering: initControl");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.my_calendar, this);


        loadDateFormat(attrs);
        assignUiElements();
        assignClickHandlers();

        updateCalendar();
    }

    private void loadDateFormat(AttributeSet attrs)
    {
        Log.e(LOG, "Entering: loadDateFormat");
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try
        {
            // try to load provided date format, and fallback to default otherwise
            dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
            if (dateFormat == null)
                dateFormat = DATE_FORMAT;
        }
        finally
        {
            ta.recycle();
        }
    }
    private void assignUiElements()
    {
        Log.e(LOG, "Entering: assignUiElements");
        // layout is inflated, assign local variables to components
        header = (LinearLayout)findViewById(R.id.calendar_header);
        btnPrev = (ImageView)findViewById(R.id.calendar_prev_button);
        btnNext = (ImageView)findViewById(R.id.calendar_next_button);
        txtDate = (TextView)findViewById(R.id.calendar_date_display);
        grid = (GridView)findViewById(R.id.calendar_grid);
    }

    private void assignClickHandlers()
    {
        Log.e(LOG, "Entering: assignClickHandlers");
        // add one month and refresh UI
        btnNext.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentDate.add(Calendar.MONTH, 1);
                eventHandler.OnClickListener();
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentDate.add(Calendar.MONTH, -1);
                eventHandler.OnClickListener();
            }
        });

        // long-pressing a day
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {

            @Override
            public boolean onItemLongClick(AdapterView<?> view, View cell, int position, long id)
            {
                // handle long-press
                if (eventHandler == null)
                    return false;

                eventHandler.onDayLongPress((Date)view.getItemAtPosition(position));
                return true;
            }
        });
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar()
    {
        updateCalendar(null);
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar(HashSet<Date> events)
    {
        Log.e(LOG, "Entering: updateCalendar");
        ArrayList<Date> cells = new ArrayList<Date>();
        Calendar calendar = (Calendar)currentDate.clone();

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT)
        {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // update grid
        grid.setAdapter(new CalendarAdapter(getContext(), cells, events));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        txtDate.setText(sdf.format(currentDate.getTime()));

        // set header color according to current season
        int month = currentDate.get(Calendar.MONTH);
        int season = monthSeason[month];
        int color = rainbow[season];

        header.setBackgroundColor(getResources().getColor(color));
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendarList(List<ScheduledBrewSchema> events)
    {
        Log.e(LOG, "Entering: updateCalendar");
        ArrayList<Date> cells = new ArrayList<Date>();
        Calendar calendar = (Calendar)currentDate.clone();

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT)
        {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // update grid
        grid.setAdapter(new CalendarAdapter(getContext(), cells, events));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        txtDate.setText(sdf.format(currentDate.getTime()));

        // set header color according to current season
        int month = currentDate.get(Calendar.MONTH);
        int season = monthSeason[month];
        int color = rainbow[season];

        header.setBackgroundColor(getResources().getColor(color));
    }


    private class CalendarAdapter extends ArrayAdapter<Date>
    {
        // days with events
        private HashSet<Date> eventDays;
        private List<ScheduledBrewSchema> listEventDays;

        // for view inflation
        private LayoutInflater inflater;

        public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays)
        {
            super(context, R.layout.calendar_day_view, days);
            this.eventDays = eventDays;
            inflater = LayoutInflater.from(context);
        }

        public CalendarAdapter(Context context, ArrayList<Date> days, List<ScheduledBrewSchema> eventDays)
        {
            super(context, R.layout.calendar_day_view, days);
            this.listEventDays = eventDays;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent)
        {
            // day in question
            Date date = getItem(position);
            int day = date.getDate();
            int month = date.getMonth();
            int year = date.getYear();

            // today
            Date today = new Date();

            // inflate item if it does not exist yet
            if (view == null)
                view = inflater.inflate(R.layout.calendar_day_view, parent, false);


            // if this day has an event, specify event image
            view.setBackgroundResource(0);
            if (eventDays != null)
            {
                for (Date eventDate : eventDays)
                {
                    if (eventDate.getDate() == day &&
                            eventDate.getMonth() == month &&
                            eventDate.getYear() == year)
                    {
                        // mark this day for event
                        //view.setBackgroundResource(R.drawable.reminder);
                        break;
                    }
                }
            }

            //Update based on list
            if(listEventDays != null)
            {
                for (ScheduledBrewSchema eventDate : listEventDays)
                {
                    //TODO: Need to handle multiple events on the same day and handle day press and ability to remove a schedule
                    if (DateHasEvent(date,eventDate))
                    {
                        // mark this day for event
                       LinearLayout  eventsLayout  = (LinearLayout)view.findViewById(R.id.eventsLayout);
                       eventsLayout.addView(CreateDateEvent(view.getContext()));
                        view.refreshDrawableState();
                        break;
                    }
                }
            }

            TextView dateText = (TextView)view.findViewById(R.id.dateText);
            // clear styling
            dateText.setTypeface(null, Typeface.NORMAL);
            dateText.setTextColor(Color.BLACK);

            if (month != today.getMonth() || year != today.getYear())
            {
                // if this day is outside current month, grey it out
                dateText.setTextColor(getResources().getColor(R.color.greyed_out));
            }
            else if (day == today.getDate())
            {
                // if it is today, set it to blue/bold
                dateText.setTypeface(null, Typeface.BOLD);
                dateText.setTextColor(getResources().getColor(R.color.today));
            }

            // set text
            dateText.setText(String.valueOf(date.getDate()));

            return view;
        }

        private boolean DateHasEvent(Date d, ScheduledBrewSchema sBrew)
        {
            Date startDate = new Date();
            Date endDate = new Date();
            try {
                startDate = formatter.parse(sBrew.getStartDate());
                endDate = formatter.parse(sBrew.getEndBrewDate());
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            return d.after(startDate) && d.before(endDate);
        }

        private LinearLayout CreateDateEvent(Context context)
        {
            // inflate item if it does not exist yet
            LinearLayout ll = new LinearLayout(context);
            LayoutParams LLParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

            ll.setLayoutParams(LLParams);
            ll.setBackgroundColor(Color.CYAN);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setMinimumHeight(10);
            ll.setPadding(2,2,2,2);

            return ll;
        }
    }

    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    public interface EventHandler
    {
        void onDayLongPress(Date date);
        void OnClickListener();
    }
}