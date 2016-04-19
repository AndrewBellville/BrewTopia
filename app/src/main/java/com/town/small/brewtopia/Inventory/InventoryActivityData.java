package com.town.small.brewtopia.Inventory;

import com.town.small.brewtopia.DataClass.HopsSchema;

/**
 * Created by Andrew on 4/18/2016.
 */
public class InventoryActivityData {

    //State for AddEditView Brew and Brew Name if Edit/View
    public enum DisplayMode {
        ADD, EDIT, VIEW
    };

    private DisplayMode AddEditViewState = DisplayMode.VIEW; // STATES: Add, Edit, View

    private HopsSchema hopsSchema;

    //Singleton
    private static InventoryActivityData mInstance = null;

    public static InventoryActivityData getInstance() {
        if (mInstance == null) {
            mInstance = new InventoryActivityData();
        }
        return mInstance;
    }

    // constructor
    private InventoryActivityData() {
    }

    //getters
    public HopsSchema getHopsSchema() {
        return hopsSchema;
    }
    public DisplayMode getAddEditViewState() {
        return AddEditViewState;
    }


    //Setters
    public void setHopsSchema(HopsSchema hopsSchema) {
        this.hopsSchema = hopsSchema;
    }
    public void setAddEditViewState(DisplayMode addEditViewState) {
        AddEditViewState = addEditViewState;
    }

}
