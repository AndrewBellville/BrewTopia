package com.town.small.brewtopia;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import com.town.small.brewtopia.Calendar.MyCalendar;
import com.town.small.brewtopia.DataClass.*;

public class UserSchedule extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "UserSchedule";

    private DataBaseManager dbManager;

    List<ScheduledBrewSchema> sBrewList;
    ArrayList<HashMap<String, String>> list;
    private ListView ScheduledBrewListView;

    private SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_schedule);

        dbManager = DataBaseManager.getInstance(getApplicationContext());

        sBrewList = dbManager.getAllActiveScheduledBrews(CurrentUser.getInstance().getUser().getUserName());
        MyCalendar mc = ((MyCalendar) findViewById(R.id.calendar_view));
        mc.updateCalendarList(sBrewList);

        // assign event handler
        mc.setEventHandler(new MyCalendar.EventHandler() {
            @Override
            public void onDayLongPress(Date date) {
                //Log.e("UserSchedule", "Entering: setEventHandler");
                // show returned day
                //DateFormat df = SimpleDateFormat.getDateInstance();
                //Toast.makeText(getApplicationContext(), df.format(date), Toast.LENGTH_SHORT).show();
                LoadScheduleView(date);
            }

            @Override
        public void OnClickListener(){
                updateCalendarView();
            }
        });

        ScheduledBrewListView = (ListView)findViewById(R.id.ScheduledListView);
        ScheduledBrewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //BrewSelect(position);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        updateCalendarView();
    }

    private void  updateCalendarView()
    {
        sBrewList = dbManager.getAllActiveScheduledBrews(CurrentUser.getInstance().getUser().getUserName());
        MyCalendar mc = ((MyCalendar) findViewById(R.id.calendar_view));
        mc.updateCalendarList(sBrewList);
    }

    private void LoadScheduleView(Date date) {
        Log.e(LOG, "Entering: LoadBrews");

        List<ScheduledBrewSchema> scheduledDayList = dbManager.getAllActiveScheduledBrews(CurrentUser.getInstance().getUser().getUserName());
        list = new ArrayList<HashMap<String, String>>();

        if(scheduledDayList.size() == 0 && list.size() > 0) {
            list.clear();
        }
        else if (scheduledDayList.size() > 0) {

            for(ScheduledBrewSchema sbrew : scheduledDayList)
            {
                if(DateHasEvent(date,sbrew))
                {
                    HashMap<String, String> temp = new HashMap<String, String>();
                    temp.put("Name", "Brew Name: " + sbrew.getBrewName());
                    temp.put("Date", "Started: " + sbrew.getStartDate());
                    list.add(temp);
                }
            }

            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    list,
                    R.layout.custom_row_view,
                    new String[] {"Name","Date"},
                    new int[] {R.id.text1,R.id.text2}

            );

            ScheduledBrewListView.setAdapter(adapter);
        }
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
        if((d.getDate()>= startDate.getDate() && d.getMonth()>= startDate.getMonth() && d.getYear()>= startDate.getYear()) &&
                (d.getDate()<= endDate.getDate() && d.getMonth()<= endDate.getMonth() && d.getYear()<= endDate.getYear()) )
        {
            return true;
        }
        return false;
    }
}