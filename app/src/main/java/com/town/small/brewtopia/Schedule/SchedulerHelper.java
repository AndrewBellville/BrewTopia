package com.town.small.brewtopia.Schedule;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;

import com.town.small.brewtopia.AppSettings.AppSettingsHelper;
import com.town.small.brewtopia.DataClass.APPUTILS;
import com.town.small.brewtopia.DataClass.BrewSchema;
import com.town.small.brewtopia.DataClass.CurrentUser;
import com.town.small.brewtopia.DataBase.DataBaseManager;
import com.town.small.brewtopia.DataClass.ScheduledBrewSchema;

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
        sBrew.SetScheduledDates(aBrew.getPrimary(), aBrew.getSecondary(), aBrew.getBottle());
        sBrew.setColor(aBrew.getStyleSchema().getBrewStyleColor());

        if(appSettingsHelper.GetBoolAppSettingsByName(AppSettingsHelper.SCHEDULER_CALENDAR_PUSH)) {
            Date date = new Date();
            Date date1 = new Date();
            Date date2 = new Date();
            try {
                date = APPUTILS.dateFormat.parse(sBrew.getAlertSecondaryDate());
                date1 = APPUTILS.dateFormat.parse(sBrew.getAlertBottleDate());
                date2 = APPUTILS.dateFormat.parse(sBrew.getEndBrewDate());

            } catch (ParseException e) {
                e.printStackTrace();
            }

            createCalendarEvent(date, sBrew.getBrewName() + " Secondary");
            if(!(eventID==-1))
                sBrew.setAlertSecondaryCalendarId(eventID);

            createCalendarEvent(date1, sBrew.getBrewName() + " Bottling");
            if(!(eventID==-1))
                sBrew.setAlertBottleCalendarId(eventID);

            createCalendarEvent(date2, sBrew.getBrewName() + " End Brew");
            if(!(eventID==-1))
                sBrew.setEndBrewCalendarId(eventID);
        }

        DataBaseManager.getInstance(context).CreateAScheduledBrew(sBrew);
    }

    public long createCalendarEvent(Date aDate, String aTitle)
    {

        if(!appSettingsHelper.GetBoolAppSettingsByName(AppSettingsHelper.SCHEDULER_CALENDAR_PUSH))
            return -1;

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
            return -1;
        }

        return eventID;
    }


    public boolean updateCalendarEvent(Date aDate, long aEventID)
    {
        if ( (!appSettingsHelper.GetBoolAppSettingsByName(AppSettingsHelper.SCHEDULER_CALENDAR_PUSH)) &&
                eventID == -1)
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
                eventID == -1)
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
