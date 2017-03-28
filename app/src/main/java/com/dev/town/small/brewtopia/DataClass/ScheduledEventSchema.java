package com.dev.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 10/20/2016.
 */
public class ScheduledEventSchema {

    private long scheduledEventId=0;
    private long scheduleId=0;
    private long brewId=0;
    private String eventDate;
    private long eventCalendarId=-1;
    private String eventText;
    private int isNew = 1;//1=new 0=not new

    //Getters
    public long getScheduledEventId() {
        return scheduledEventId;
    }
    public long getScheduleId() {
        return scheduleId;
    }
    public long getBrewId() {
        return brewId;
    }
    public String getEventDate() {
        return eventDate;
    }
    public long getEventCalendarId() {
        return eventCalendarId;
    }
    public String getEventText() {
        return eventText;
    }
    public int getIsNew() {
        return isNew;
    }

    //Setters
    public void setScheduledEventId(long scheduledEventId) {
        this.scheduledEventId = scheduledEventId;
    }
    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }
    public void setBrewId(long brewId) {
        this.brewId = brewId;
    }
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
    public void setEventCalendarId(long eventCalendarId) {
        this.eventCalendarId = eventCalendarId;
    }
    public void setEventText(String eventText) {
        this.eventText = eventText;
    }
    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }
}
