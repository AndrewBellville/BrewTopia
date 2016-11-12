package com.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 10/20/2016.
 */
public class ScheduledEventSchema {

    private long scheduledEventId=-1;
    private long scheduleId=-1;
    private long brewId=-1;
    private String eventDate;
    private long eventCalendarId=-1;
    private String eventText;

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
}
