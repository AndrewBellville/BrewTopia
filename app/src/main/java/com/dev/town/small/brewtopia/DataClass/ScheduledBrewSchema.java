package com.dev.town.small.brewtopia.DataClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Andrew on 1/2/2016.
 */
public class ScheduledBrewSchema {

    private long scheduleId=0;
    private long BrewId=0;
    private long UserId=-1;
    private String brewName = "";
    private String StartDate;
    private String Notes;
    private Double OG=0.0;
    private Double FG=0.0;
    private Double ABV=0.0;
    private int Active=1;
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

    public String getCurrentState()
    {
        //get the current date as a string
        Date date = new Date();
        String d = APPUTILS.dateFormatCompare.format(date);

        //sort the list of events in order of event date smallest to largest
        Collections.sort(scheduledEventSchemaList, new Comparator<ScheduledEventSchema>(){
            public int compare(ScheduledEventSchema se1, ScheduledEventSchema se2){
                return se1.getEventDate().compareTo(se2.getEventDate());
            }
        });

        // loop over each event and find the one  we are currently in
        for (int i = 0; i < scheduledEventSchemaList.size(); i++) {
            //find the first date we are less then
            if(d.compareTo(APPUTILS.StringDateCompare(scheduledEventSchemaList.get(i).getEventDate())) < 0)
            {
                //if we are less then first event we should be in primary else we are at event list -1
                if(i==0)
                {
                    Date parsedDate = new Date();
                    try {
                        parsedDate = APPUTILS.dateFormatCompare.parse(getStartDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long diff = date.getTime() - parsedDate.getTime();
                    return "Primary " + TimeUnit.MILLISECONDS.toDays(diff) + " Days";
                }
                else
                {
                    Date parsedDate = new Date();
                    try {
                        parsedDate = APPUTILS.dateFormatCompare.parse(scheduledEventSchemaList.get(i-1).getEventDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long diff = date.getTime() - parsedDate.getTime();
                    return scheduledEventSchemaList.get(i-1).getEventText()+ " " + TimeUnit.MILLISECONDS.toDays(diff) + " Days";
                }
            }
        }

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
        String date = formatter.format(d);

        for(ScheduledEventSchema scheduledEventSchema : getScheduledEventSchemaList())
        {
            Date date1;

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
    public String getEndBrewDate()
    {
        //sort the list of events in order of event date smallest to largest
        Collections.sort(scheduledEventSchemaList, new Comparator<ScheduledEventSchema>(){
            public int compare(ScheduledEventSchema se1, ScheduledEventSchema se2){
                return se1.getEventDate().compareTo(se2.getEventDate());
            }
        });
        //return last event date in sorted list
        if(scheduledEventSchemaList.size() > 0)
            return scheduledEventSchemaList.get(scheduledEventSchemaList.size()-1).getEventDate();
        else
            return APPUTILS.dateFormatCompare.format(new Date());
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
