package com.town.small.brewtopia.Schedule;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.town.small.brewtopia.CustomListAdapter;
import com.town.small.brewtopia.R;
import com.town.small.brewtopia.Schedule.AddEditViewSchedule;
import com.town.small.brewtopia.Schedule.MyCalendar;
import com.town.small.brewtopia.DataClass.*;
import com.town.small.brewtopia.Schedule.ScheduleActivityData;

public class UserSchedule extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "UserSchedule";

    private DataBaseManager dbManager;

    List<ScheduledBrewSchema> sBrewList;
    ArrayList<ScheduledBrewSchema> list;
    private ListView ScheduledBrewListView;
    private String userName;

    private SimpleDateFormat formatter = APPUTILS.dateFormatCompare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_schedule);

        dbManager = DataBaseManager.getInstance(getApplicationContext());
        userName = CurrentUser.getInstance().getUser().getUserName();

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
                ScheduleSelect(list.get(position));
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
        clearListDate();
        sBrewList = dbManager.getAllActiveScheduledBrews(CurrentUser.getInstance().getUser().getUserName());
        MyCalendar mc = ((MyCalendar) findViewById(R.id.calendar_view));
        mc.updateCalendarList(sBrewList);
    }

    private void LoadScheduleView(Date date) {
        Log.e(LOG, "Entering: LoadBrews");

        List<ScheduledBrewSchema> scheduledDayList = dbManager.getAllActiveScheduledBrews(CurrentUser.getInstance().getUser().getUserName());
        list = new ArrayList<ScheduledBrewSchema>();

        if(scheduledDayList.size() == 0 && list.size() > 0) {
            list.clear();
        }
        else {

            for(ScheduledBrewSchema sbrew : scheduledDayList)
            {
                if(sbrew.DateHasEvent(date))
                {
                    if(sbrew.DateHasAction(date))
                        sbrew.setShowAsAlert(true);

                    list.add(sbrew);
                }

            }

            //instantiate custom adapter
            CustomSListAdapter adapter = new CustomSListAdapter(list, this.getApplicationContext());
            adapter.hasColor(true);
            adapter.setEventHandler(new CustomSListAdapter.EventHandler() {
                @Override
                public void OnDeleteClickListener(int aScheduleId) {
                    dbManager.deleteBrewScheduledById(aScheduleId);
                    updateCalendarView();
                }
            });

            ScheduledBrewListView.setAdapter(adapter);
        }
    }

    private void ScheduleSelect(ScheduledBrewSchema aSBrew)
    {
        Intent intent = new Intent(this, AddEditViewSchedule.class);

        //Set what Schedule was selected before we load Schedule Activity
        ScheduleActivityData.getInstance().setScheduledBrewSchema(aSBrew);

        //start next activity
        startActivity(intent);
    }

    private void clearListDate()
    {
        Date d = new Date();
        d.setTime(0);
        LoadScheduleView(d);
    }
}