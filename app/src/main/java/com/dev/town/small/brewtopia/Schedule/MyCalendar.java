
package com.dev.town.small.brewtopia.Schedule;

        import android.content.Context;
        import android.content.res.TypedArray;
        import android.graphics.Color;
        import android.graphics.Typeface;
        import android.graphics.drawable.GradientDrawable;
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

        import com.dev.town.small.brewtopia.DataClass.APPUTILS;
        import com.dev.town.small.brewtopia.DataClass.ScheduledBrewSchema;
        import com.dev.town.small.brewtopia.R;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.HashSet;
        import java.util.List;

public class MyCalendar extends LinearLayout
{
    // Log cat tag
    private static final String LOG = "MyCalendar";

    // how many days to show, defaults to six weeks, 42 days
    private static final int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";
    private SimpleDateFormat formatter = APPUTILS.dateFormatCompare;

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
    private View daySelected;

    // seasons' rainbow
    int[] headerColor = new int[] {
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
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: initControl");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.my_calendar, this);


        loadDateFormat(attrs);
        assignUiElements();
        assignClickHandlers();

    }

    private void loadDateFormat(AttributeSet attrs)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: loadDateFormat");
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
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: assignUiElements");
        // layout is inflated, assign local variables to components
        header = (LinearLayout)findViewById(R.id.calendar_header);
        btnPrev = (ImageView)findViewById(R.id.calendar_prev_button);
        btnNext = (ImageView)findViewById(R.id.calendar_next_button);
        txtDate = (TextView)findViewById(R.id.calendar_date_display);
        grid = (GridView)findViewById(R.id.calendar_grid);
    }

    private void assignClickHandlers()
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: assignClickHandlers");
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
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> view, View cell, int position, long id) {
                // handle long-press
                if (eventHandler == null)
                    return false;

                eventHandler.onDayLongPress((Date) view.getItemAtPosition(position));
                toggleDaySelected(cell);
                return true;
            }
        });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> view, View cell, int position,long id)  {
                // handle press
                if (eventHandler == null)
                    return;
                eventHandler.onDayLongPress((Date) view.getItemAtPosition(position));
                toggleDaySelected(cell);
            }
        });
    }

    private void toggleDaySelected(View view)
    {
        if(daySelected == null)
            daySelected = view;
        else
            daySelected.setBackgroundColor(0x00000000);

        daySelected = view;
        GradientDrawable border = new GradientDrawable();
        border.setColor(0x1A000000); //white background
        border.setStroke(1,0xFF000000); //black border with full opacity

        daySelected.setBackground(border);
    }


    /**
     * Display dates correctly in grid
     */
    public void updateCalendarList(List<ScheduledBrewSchema> events)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: updateCalendar");
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
        int color = headerColor[season];

        header.setBackgroundColor(getResources().getColor(color));
        grid.setBackgroundColor(getResources().getColor(R.color.ColorToneL2));
    }


    private class CalendarAdapter extends ArrayAdapter<Date>
    {
        // days with events
        private HashSet<Date> eventDays;
        private List<ScheduledBrewSchema> listEventDays;
        private List<Integer> getViewPos;

        // for view inflation
        private LayoutInflater inflater;

        public CalendarAdapter(Context context, ArrayList<Date> days, List<ScheduledBrewSchema> eventDays)
        {
            super(context, R.layout.calendar_day_view, days);
            this.listEventDays = eventDays;
            inflater = LayoutInflater.from(context);
            getViewPos = new ArrayList<Integer>();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent)
        {

            //if we have seen this position (date) before we just want to return the view other wise add to list and create the new view
            if(getViewPos.contains(position) && view !=  null)
                return view;
            else
                getViewPos.add(position);

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


            ImageView ActionImage = (ImageView)view.findViewById(R.id.ActionImageView);

            //Update based on list
            if(listEventDays != null)
            {
                for (ScheduledBrewSchema eventDate : listEventDays)
                {
                    //Check id date has event if so draw it
                    if (eventDate.DateHasEvent(date))
                    {
                        // mark this day for event
                       LinearLayout  eventsLayout  = (LinearLayout)view.findViewById(R.id.eventsLayout);
                       int childCount = eventsLayout.getChildCount();

                        if(eventDate.getDisplayLevel() == 0 )
                            eventDate.setDisplayLevel(childCount);

                        if(eventDate.getDisplayLevel() != childCount)
                        {
                            int i = eventDate.getDisplayLevel() - childCount;
                            do{
                                eventsLayout.addView(CreateDateEvent(view.getContext(),Color.TRANSPARENT));
                                i--;
                            }while(i > 0);
                            childCount = eventsLayout.getChildCount();
                        }

                       eventsLayout.addView(CreateDateEvent(view.getContext(), EventColor(eventDate)));
                        view.refreshDrawableState();
                        //break;
                    }

                    //Check id date has Action if so Show it
                    if (eventDate.DateHasAction(date))
                        ActionImage.setVisibility(VISIBLE);

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
                dateText.setTextColor(Color.BLUE);
            }

            // set text
            dateText.setText(String.valueOf(date.getDate()));

            return view;
        }

        private LinearLayout CreateDateEvent(Context context, int color)
        {
            // inflate item if it does not exist yet
            LinearLayout ll = new LinearLayout(context);
            LayoutParams LLParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            LLParams.setMargins(0,2,0,2);

            ll.setLayoutParams(LLParams);
            ll.setBackgroundColor(color);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setMinimumHeight(10);
            return ll;
        }

        private int EventColor(ScheduledBrewSchema eventDate)
        {
            try {
                return Color.parseColor(eventDate.getColor());
            }
            catch (Exception e){return Color.BLUE;}
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