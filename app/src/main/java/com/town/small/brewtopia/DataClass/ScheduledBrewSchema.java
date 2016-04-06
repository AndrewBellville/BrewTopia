package com.town.small.brewtopia.DataClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Andrew on 1/2/2016.
 */
public class ScheduledBrewSchema {

    private int scheduleId;
    private String BrewName;
    private String UserName;
    private String StartDate;
    private String AlertSecondaryDate;
    private String AlertBottleDate;
    private String EndBrewDate;
    private String Notes;
    private int Active;
    private int displayLevel = 0;
    private String color;

    // constructors
    public ScheduledBrewSchema() {
    }

    public ScheduledBrewSchema(String aBrewName, String aUserName) {
        setBrewName(aBrewName);
        setUserName(aUserName);
    }

    public void SetScheduledDates(int aPrimaryDate,int aSecondaryDate, int aBottleDate)
    {
        setAlertSecondaryDate(getDateTime(aPrimaryDate));
        setAlertBottleDate(getDateTime(aPrimaryDate+aSecondaryDate));
        setEndBrewDate(getDateTime(aPrimaryDate+aSecondaryDate+aBottleDate));

        if(getEndBrewDate().compareTo(getDateTime(0)) >= 0)
            setActive(1);
        else
            setActive(0);
    }

    /**
     * get datetime
     * */
    private String getDateTime(int addDays) {
        SimpleDateFormat dateFormat = APPUTILS.dateFormat;

        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, addDays); // Adding days

        return dateFormat.format(c.getTime());
    }

    //Getters
    public String getUserName() {
        return UserName;
    }
    public String getBrewName() {
        return BrewName;
    }
    public String getStartDate() {
        return StartDate;
    }
    public String getAlertSecondaryDate() {
        return AlertSecondaryDate;
    }
    public String getAlertBottleDate() {
        return AlertBottleDate;
    }
    public String getEndBrewDate() {
        return EndBrewDate;
    }
    public int getActive() {
        return Active;
    }
    public int getDisplayLevel() {
        return displayLevel;
    }
    public String getNotes() {
        return Notes;
    }
    public String getColor() {
        return color;
    }
    public int getScheduleId() {
        return scheduleId;
    }

    //Setters
    public void setUserName(String userName) {
        UserName = userName;
    }
    public void setBrewName(String brewName) {
        BrewName = brewName;
    }
    public void setStartDate(String startDate) {
        StartDate = startDate;
    }
    public void setAlertSecondaryDate(String alertSecondaryDate) {
        AlertSecondaryDate = alertSecondaryDate;
    }
    public void setAlertBottleDate(String alertBottleDate) {
        AlertBottleDate = alertBottleDate;
    }
    public void setEndBrewDate(String endBrewDate) {
        EndBrewDate = endBrewDate;
    }
    public void setActive(int active) {
        Active = active;
    }
    public void setDisplayLevel(int displayLevel) {
        this.displayLevel = displayLevel;
    }
    public void setNotes(String notes) {
        Notes = notes;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }
}
