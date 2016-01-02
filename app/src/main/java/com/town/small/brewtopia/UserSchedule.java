package com.town.small.brewtopia;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import com.town.small.brewtopia.Calendar.MyCalendar;


public class UserSchedule extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_schedule);

        HashSet<Date> events = new HashSet<Date>();
        events.add(new Date());

        MyCalendar mc = ((MyCalendar)findViewById(R.id.calendar_view));
        mc.updateCalendar(events);

        // assign event handler
        mc.setEventHandler(new MyCalendar.EventHandler()
        {
            @Override
            public void onDayLongPress(Date date)
            {
                Log.e("UserSchedule", "Entering: setEventHandler");
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
                //Toast.makeText(getApplicationContext(), df.format(date), Toast.LENGTH_SHORT).show();
            }
        });
    }
}