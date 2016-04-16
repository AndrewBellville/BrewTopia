package com.town.small.brewtopia.Schedule;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.town.small.brewtopia.Login;
import com.town.small.brewtopia.R;
import com.town.small.brewtopia.Schedule.AddEditViewSchedule;
import com.town.small.brewtopia.Schedule.MyCalendar;
import com.town.small.brewtopia.DataClass.*;
import com.town.small.brewtopia.Schedule.ScheduleActivityData;
import com.town.small.brewtopia.Utilites.SlidingUpPaneLayout;

public class UserSchedule extends Fragment {

    // Log cat tag
    private static final String LOG = "UserSchedule";

    private DataBaseManager dbManager;

    List<ScheduledBrewSchema> sBrewList;
    ArrayList<ScheduledBrewSchema> list;
    SlidingUpPaneLayout slidingUpPaneLayout;
    private ListView ScheduledBrewListView;
    private String userName;

    MyCalendar mc;


    private SimpleDateFormat formatter = APPUTILS.dateFormatCompare;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_user_schedule,container,false);

        dbManager = DataBaseManager.getInstance(getActivity());
        try {
            userName = CurrentUser.getInstance().getUser().getUserName();

            sBrewList = dbManager.getAllActiveScheduledBrews(CurrentUser.getInstance().getUser().getUserId());
            mc = ((MyCalendar) view.findViewById(R.id.calendar_view));
            mc.updateCalendarList(sBrewList);

            final float density = getResources().getDisplayMetrics().density;

            slidingUpPaneLayout = (SlidingUpPaneLayout) view.findViewById(R.id.sliding_layout);
            slidingUpPaneLayout.setParallaxDistance((int) (200 * density));
            slidingUpPaneLayout.setShadowResourceTop(R.drawable.shadow_top);

            slidingUpPaneLayout.openPane();

            // assign event handler
            mc.setEventHandler(new MyCalendar.EventHandler() {
                @Override
                public void onDayLongPress(Date date) {
                    if(slidingUpPaneLayout.isOpen())
                        LoadScheduleView(date);
                }

                @Override
            public void OnClickListener(){
                    updateCalendarView();
                }
            });

            ScheduledBrewListView = (ListView)view.findViewById(R.id.ScheduledListView);
            ScheduledBrewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    ScheduleSelect(list.get(position));
                }
            });



        }
        catch (Exception e){
            // if  we fail to get user name open login activity
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }

        return view;

    }

    @Override
    public void setMenuVisibility(boolean isShown) {
        if(isShown)
        {
            updateCalendarView();
            slidingUpPaneLayout.openPane();
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        updateCalendarView();
        slidingUpPaneLayout.openPane();
    }

    private void  updateCalendarView()
    {
        clearListDate();
        sBrewList = dbManager.getAllActiveScheduledBrews(CurrentUser.getInstance().getUser().getUserId());
        mc.updateCalendarList(sBrewList);
    }

    private void LoadScheduleView(Date date) {
        Log.e(LOG, "Entering: LoadBrews");

        List<ScheduledBrewSchema> scheduledDayList = dbManager.getAllActiveScheduledBrews(CurrentUser.getInstance().getUser().getUserId());
        list = new ArrayList<ScheduledBrewSchema>();

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
        CustomSListAdapter adapter = new CustomSListAdapter(list, getActivity());
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

    private void ScheduleSelect(ScheduledBrewSchema aSBrew)
    {
        Intent intent = new Intent(getActivity(), AddEditViewSchedule.class);

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