package com.dev.town.small.brewtopia.Schedule;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;

import com.dev.town.small.brewtopia.AppSettings.AppSettingsHelper;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.BrewSchema;
import com.dev.town.small.brewtopia.DataClass.CurrentUser;
import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.ScheduledBrewSchema;
import com.dev.town.small.brewtopia.DataClass.ScheduledEventSchema;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Andrew on 4/12/2016.
 */
public class SchedulerHelper {

    long calID = 1;// 1 is main cal
    private Context context;
    private Uri uri;
    private ContentValues values;
    ContentResolver cr;
    long eventID;
    AppSettingsHelper appSettingsHelper;

    public SchedulerHelper(Context context) {

        this.context = context;
        appSettingsHelper = AppSettingsHelper.getInstance(context);
    }

    public void createSchedule(BrewSchema aBrew)
    {
        if(!appSettingsHelper.GetBoolAppSettingsByName(AppSettingsHelper.SCHEDULER_AUTO_CREATE))
            return;

        //Create Schedule for Brew
        ScheduledBrewSchema sBrew = new ScheduledBrewSchema(aBrew.getBrewId(), CurrentUser.getInstance().getUser().getUserId());
        sBrew.setBrewName(aBrew.getBrewName());
        sBrew.setColor(aBrew.getStyleSchema().getBrewStyleColor());
        sBrew.setStartDate(APPUTILS.dateFormat.format(new Date()));

        //secondary
        if(aBrew.getSecondary() != 0) {
            ScheduledEventSchema secondaryEvent = new ScheduledEventSchema();
            secondaryEvent.setBrewId(aBrew.getBrewId());
            secondaryEvent.setEventText(aBrew.getBrewName() + " Secondary");
            secondaryEvent.setEventDate(sBrew.addDateTime(aBrew.getPrimary(), null));
            sBrew.getScheduledEventSchemaList().add(secondaryEvent);
        }
        //Bottling
        if(aBrew.getBottle() !=  0) {
            ScheduledEventSchema bottlingEvent = new ScheduledEventSchema();
            bottlingEvent.setBrewId(aBrew.getBrewId());
            bottlingEvent.setEventText(aBrew.getBrewName() + " Bottling");
            bottlingEvent.setEventDate(sBrew.addDateTime(aBrew.getPrimary()+aBrew.getSecondary(), null));
            sBrew.getScheduledEventSchemaList().add(bottlingEvent);
        }
        //End
        ScheduledEventSchema endBrewEvent = new ScheduledEventSchema();
        endBrewEvent.setBrewId(aBrew.getBrewId());
        endBrewEvent.setEventText(aBrew.getBrewName() + " End Brew");
        endBrewEvent.setEventDate(sBrew.addDateTime(aBrew.getPrimary()+aBrew.getSecondary()+aBrew.getBottle(), null));
        sBrew.getScheduledEventSchemaList().add(endBrewEvent);

        if(appSettingsHelper.GetBoolAppSettingsByName(AppSettingsHelper.SCHEDULER_CALENDAR_PUSH)) {


            for (ScheduledEventSchema scheduleEvent:sBrew.getScheduledEventSchemaList()) {
                Date date = new Date();
                try {
                    date = APPUTILS.dateFormat.parse(scheduleEvent.getEventDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                createCalendarEvent(date, scheduleEvent.getEventText());
                if(!(eventID==0))
                    scheduleEvent.setEventCalendarId(eventID);
            }
        }

        DataBaseManager.getInstance(context).CreateAScheduledBrew(sBrew);
    }

    public long createCalendarEvent(Date aDate, String aTitle)
    {

        if(!appSettingsHelper.GetBoolAppSettingsByName(AppSettingsHelper.SCHEDULER_CALENDAR_PUSH))
            return 0;

        try {
            long startMillis = 0;
            long endMillis = 0;
            Calendar beginTime = Calendar.getInstance();
            beginTime.setTime(aDate);
            startMillis = beginTime.getTimeInMillis();
            Calendar endTime = Calendar.getInstance();
            endTime.setTime(aDate);
            endMillis = endTime.getTimeInMillis();

            cr = context.getContentResolver();
            values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            values.put(CalendarContract.Events.TITLE, aTitle);
            values.put(CalendarContract.Events.DESCRIPTION, "Brew Scheduler");
            values.put(CalendarContract.Events.CALENDAR_ID, calID);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
            uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

            // get the event ID that is the last element in the Uri
            eventID = Long.parseLong(uri.getLastPathSegment());

            //Add alarm
            cr = context.getContentResolver();
            values = new ContentValues();
            values.put(CalendarContract.Reminders.MINUTES, 1);
            values.put(CalendarContract.Reminders.EVENT_ID, eventID);
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);

        }catch (Exception e)
        {
            return 0;
        }

        return eventID;
    }


    public boolean updateCalendarEvent(Date aDate, long aEventID)
    {
        if ( (!appSettingsHelper.GetBoolAppSettingsByName(AppSettingsHelper.SCHEDULER_CALENDAR_PUSH)) &&
                eventID == 0)
            return false;

        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(aDate);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(aDate);
        endMillis = endTime.getTimeInMillis();

        try {
            cr = context.getContentResolver();
            values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, aEventID);
            int rows = context.getContentResolver().update(uri, values, null, null);

        }catch (Exception e)
        {
            return false;
        }

        return true;
    }

    public boolean deleteCalendarEvent(long aEventID)
    {
        if ( (!appSettingsHelper.GetBoolAppSettingsByName(AppSettingsHelper.SCHEDULER_CALENDAR_PUSH)) &&
                eventID == 0)
            return false;

        try {
            cr = context.getContentResolver();
            values = new ContentValues();
            values.put(CalendarContract.Reminders.MINUTES, 1);
            values.put(CalendarContract.Reminders.EVENT_ID, aEventID);
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);

            uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, aEventID);
            int rows = context.getContentResolver().delete(uri, null, null);

        }catch (Exception e)
        {
            return false;
        }

        return true;
    }

}
