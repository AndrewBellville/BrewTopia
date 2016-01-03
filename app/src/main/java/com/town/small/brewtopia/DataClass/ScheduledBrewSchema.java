package com.town.small.brewtopia.DataClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Andrew on 1/2/2016.
 */
public class ScheduledBrewSchema {

    private String BrewName;
    private String UserName;
    private String StartDate;
    private String AlertSecondaryDate;
    private String AlertBottleDate;
    private String EndBrewDate;
    private int Active;

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
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

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
}
