package com.dev.town.small.brewtopia.Schedule;

import com.dev.town.small.brewtopia.DataClass.ScheduledBrewSchema;

/**
 * Created by Andrew on 3/26/2016.
 */
public class ScheduleActivityData {

    //State for AddEditView Brew and Brew Name if Edit/View
    public enum DisplayMode {
        ADD, EDIT, VIEW, COMPLETE
    };

    private DisplayMode AddEditViewState = DisplayMode.VIEW; // STATES: Add, Edit, View

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
    public DisplayMode getAddEditViewState() {
        return AddEditViewState;
    }

    //setters
    public void setScheduledBrewSchema(ScheduledBrewSchema scheduledBrewSchema) {
        this.scheduledBrewSchema = scheduledBrewSchema;
    }
    public void setAddEditViewState(DisplayMode addEditViewState) {
        AddEditViewState = addEditViewState;
    }
}
