package com.town.small.brewtopia.DataClass;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Andrew on 1/2/2016.
 */
public class ScheduledBrewSchema {

    private long scheduleId=-1;
    private long BrewId=-1;
    private long UserId=-1;
    private String brewName = "";
    private String StartDate;
    private String AlertSecondaryDate;
    private long AlertSecondaryCalendarId=-1;
    private String AlertBottleDate;
    private long AlertBottleCalendarId=-1;
    private String EndBrewDate;
    private long EndBrewCalendarId=-1;
    private String Notes;
    private Double OG=0.0;
    private Double FG=0.0;
    private Double ABV=0.0;
    private int Active;
    private int displayLevel = 0;
    private String color;
    private int hasStarter=0; //0 = no 1 = yes
    private List<ScheduledEventSchema> scheduledEventSchemaList = new ArrayList<>();

    //visual
    private boolean showAsAlert = false;

    // constructors
    public ScheduledBrewSchema() {
    }

    public ScheduledBrewSchema(long aBrewId, long aUserId) {
        setBrewId(aBrewId);
        setUserId(aUserId);
    }

    public void SetScheduledDates(int aPrimaryDate,int aSecondaryDate, int aBottleDate)
    {
        setAlertSecondaryDate(addDateTime(aPrimaryDate, null));
        setAlertBottleDate(addDateTime(aPrimaryDate + aSecondaryDate, null));
        setEndBrewDate(addDateTime(aPrimaryDate + aSecondaryDate + aBottleDate, null));

        if(getEndBrewDate().compareTo(addDateTime(0, null)) >= 0)
            setActive(1);
        else
            setActive(0);
    }

    public String getCurrentState()
    {
        Date date = new Date();
        String d = APPUTILS.dateFormatCompare.format(date);

        if(d.compareTo(APPUTILS.StringDateCompare(getStartDate())) >= 0 &&
                d.compareTo(APPUTILS.StringDateCompare(getAlertSecondaryDate())) < 0) {
            Date parsedDate = new Date();
            try {
                parsedDate = APPUTILS.dateFormatCompare.parse(getAlertSecondaryDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long diff = date.getTime() - parsedDate.getTime();
            return "Primary " + TimeUnit.MILLISECONDS.toDays(diff) + " Days";
        }
        else if(d.compareTo(APPUTILS.StringDateCompare(getAlertSecondaryDate())) >= 0 &&
                d.compareTo(APPUTILS.StringDateCompare(getAlertBottleDate())) < 0) {
            Date parsedDate = new Date();
            try {
                parsedDate = APPUTILS.dateFormatCompare.parse(getAlertSecondaryDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long diff = date.getTime() - parsedDate.getTime();
            return "Secondary " + TimeUnit.MILLISECONDS.toDays(diff) + " Days";
        }
        else if(d.compareTo(APPUTILS.StringDateCompare(getAlertBottleDate())) >= 0 &&
                d.compareTo(APPUTILS.StringDateCompare(getEndBrewDate())) <= 0) {
            Date parsedDate = new Date();
            try {
                parsedDate = APPUTILS.dateFormatCompare.parse(getAlertSecondaryDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long diff = date.getTime() - parsedDate.getTime();
            return "Bottles " + TimeUnit.MILLISECONDS.toDays(diff) + " Days";
        }
        else
            return "Out Of Range";
    }

    public String addDateTime(int addDays, Date date) {
        SimpleDateFormat dateFormat = APPUTILS.dateFormat;

        Calendar c = Calendar.getInstance();
        if(date ==  null)
            c.setTime(new Date()); // Now use today date.
        else
        c.setTime(date);

        c.add(Calendar.DATE, addDays); // Adding days

        return dateFormat.format(c.getTime());
    }


    public boolean DateHasEvent(Date d)
    {
        SimpleDateFormat formatter = APPUTILS.dateFormatCompare;

        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = formatter.parse(getStartDate());
            endDate = formatter.parse(getEndBrewDate());
        }
        catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        String date = formatter.format(d);
        String sDate = formatter.format(startDate);
        String eDate = formatter.format(endDate);

        int startDateCompare = date.compareTo(sDate);
        int EndDateCompare =  date.compareTo(eDate);

        if(( startDateCompare >= 0 ) && ( EndDateCompare <=  0 ) )
        {
            return true;
        }
        return false;
    }

    public boolean DateHasAction(Date d)
    {
        SimpleDateFormat formatter = APPUTILS.dateFormatCompare;

        Date SecondaryDate = new Date();
        Date BottleDate = new Date();
        Date endDate = new Date();
        try {
            SecondaryDate = formatter.parse(getAlertSecondaryDate());
            BottleDate = formatter.parse(getAlertBottleDate());
            endDate = formatter.parse(getEndBrewDate());
        }
        catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        String date = formatter.format(d);
        String sDate = formatter.format(SecondaryDate);
        String bDate = formatter.format(BottleDate);
        String eDate = formatter.format(endDate);

        int SecondaryDateCompare = date.compareTo(sDate);
        int bottleDateCompare = date.compareTo(bDate);
        int EndDateCompare =  date.compareTo(eDate);

        if(( SecondaryDateCompare == 0 ) || ( bottleDateCompare ==  0 ) || ( EndDateCompare ==  0 ) )
        {
            return true;
        }

        for(ScheduledEventSchema scheduledEventSchema : getScheduledEventSchemaList())
        {
            Date date1 = new Date();

            try {
                date1 = formatter.parse(scheduledEventSchema.getEventDate());
                String dateString = formatter.format(date1);
                int DateCompare = date.compareTo(dateString);
                if(DateCompare == 0)
                    return true;

            }
            catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }


        return false;
    }

    private void CalculateABV()
    {
        if(FG != 0)
            setABV( APPUTILS.GetTruncatedABV(APPUTILS.GetABV(OG,FG)) );
        else
            setABV(0.0);
    }

    //Getters
    public long getUserId() {
        return UserId;
    }
    public long getBrewId() {
        return BrewId;
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
    public long getScheduleId() {
        return scheduleId;
    }
    public boolean isShowAsAlert() {
        return showAsAlert;
    }
    public Double getOG() {
        return OG;
    }
    public Double getFG() {
        return FG;
    }
    public Double getABV() {
        return ABV;
    }
    public long getAlertSecondaryCalendarId() {
        return AlertSecondaryCalendarId;
    }
    public long getAlertBottleCalendarId() {
        return AlertBottleCalendarId;
    }
    public long getEndBrewCalendarId() {
        return EndBrewCalendarId;
    }
    public String getBrewName() {
        return brewName;
    }
    public int getHasStarter() {
        return hasStarter;
    }
    public boolean getBooleanHasStarter() {
        if(this.hasStarter ==  1)
            return true;
        else
            return false;
    }
    public List<ScheduledEventSchema> getScheduledEventSchemaList() {
        return scheduledEventSchemaList;
    }

    //Setters
    public void setUserId(long userId) {
        UserId = userId;
    }
    public void setBrewId(long BrewId) {
        this.BrewId = BrewId;
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
    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }
    public void setShowAsAlert(boolean showAsAlert) {
        this.showAsAlert = showAsAlert;
    }
    public void setOG(Double OG) {
        this.OG = OG;
    }
    public void setFG(Double FG) {
        this.FG = FG;
        CalculateABV();
    }
    public void setABV(Double ABV) {
        this.ABV = ABV;
    }
    public void setAlertSecondaryCalendarId(long alertSecondaryCalendarId) {
        AlertSecondaryCalendarId = alertSecondaryCalendarId;
    }
    public void setAlertBottleCalendarId(long alertBottleCalendarId) {
        AlertBottleCalendarId = alertBottleCalendarId;
    }
    public void setEndBrewCalendarId(long endBrewCalendarId) {
        EndBrewCalendarId = endBrewCalendarId;
    }
    public void setBrewName(String brewName) {
        this.brewName = brewName;
    }
    public void setHasStarter(int hasStarter) {
        this.hasStarter = hasStarter;
    }
    public void setBooleanHasStarter(boolean hasStarter) {
        if(hasStarter)
            this.hasStarter = 1;
        else
            this.hasStarter = 0;
    }
    public void setScheduledEventSchemaList(List<ScheduledEventSchema> scheduledEventSchemaList) {
        this.scheduledEventSchemaList = scheduledEventSchemaList;
    }
}
