package com.dev.town.small.brewtopia.Schedule;

import com.dev.town.small.brewtopia.DataClass.ScheduledBrewSchema;

/**
 * Created by Andrew on 3/26/2016.
 */
public class ScheduleActivityData {

    private ScheduledBrewSchema scheduledBrewSchema;

    //Singleton
    private static ScheduleActivityData mInstance = null;

    public static ScheduleActivityData getInstance() {
        if (mInstance == null) {
            mInstance = new ScheduleActivityData();
        }
        return mInstance;
    }
    // constructor
    private ScheduleActivityData() {
    }

    //getters
    public ScheduledBrewSchema getScheduledBrewSchema() {
        return scheduledBrewSchema;
    }
    
    //setters
    public void setScheduledBrewSchema(ScheduledBrewSchema scheduledBrewSchema) {
        this.scheduledBrewSchema = scheduledBrewSchema;
    }
}
