package com.dev.town.small.brewtopia.Inventory;

import com.dev.town.small.brewtopia.Brews.BrewActivityData;
import com.dev.town.small.brewtopia.DataClass.InventorySchema;

/**
 * Created by Andrew on 4/18/2016.
 */
public class InventoryActivityData {

    //State for AddEditView Brew and Brew Name if Edit/View
    public enum DisplayMode {
        ADD, BREW_ADD, EDIT, VIEW, VIEW_ONLY
    };

    private DisplayMode AddEditViewState = DisplayMode.VIEW; // STATES: Add, Edit, View

    private InventorySchema hopsSchema;
    private InventorySchema fermentablesSchema;
    private InventorySchema grainsSchema;
    private InventorySchema yeastSchema;
    private InventorySchema equipmentSchema;
    private InventorySchema otherSchema;

    private boolean isBrew = false;
    private boolean isGlobal = false;
    private boolean isSchedule = false;
    private boolean isUser = false;

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


    public void SetDisplay(String aDisplayClass)
    {
        isBrew =  aDisplayClass.contains("UserBrew");

        if(isBrew)
            isGlobal = (BrewActivityData.getInstance().getAddEditViewState() == BrewActivityData.DisplayMode.GLOBAL);


        isSchedule = aDisplayClass.contains("UserSchedule");

        isUser = aDisplayClass.contains("UserProfile");
    }

    //getters
    public InventorySchema getHopsSchema() {
        return hopsSchema;
    }
    public DisplayMode getAddEditViewState() {
        return AddEditViewState;
    }
    public InventorySchema getFermentablesSchema() {
        return fermentablesSchema;
    }
    public InventorySchema getGrainsSchema() {
        return grainsSchema;
    }
    public InventorySchema getYeastSchema() {
        return yeastSchema;
    }
    public InventorySchema getEquipmentSchema() {
        return equipmentSchema;
    }
    public InventorySchema getOtherSchema() {
        return otherSchema;
    }
    public boolean isBrew() {
        return isBrew;
    }
    public boolean isGlobal() {
        return isGlobal;
    }
    public boolean isSchedule() {
        return isSchedule;
    }
    public boolean isUser() {
        return isUser;
    }

    //Setters
    public void setHopsSchema(InventorySchema hopsSchema) {
        this.hopsSchema = hopsSchema;
    }
    public void setAddEditViewState(DisplayMode addEditViewState) {
        AddEditViewState = addEditViewState;
    }
    public void setFermentablesSchema(InventorySchema fermentablesSchema) {
        this.fermentablesSchema = fermentablesSchema;
    }
    public void setGrainsSchema(InventorySchema grainsSchema) {
        this.grainsSchema = grainsSchema;
    }
    public void setYeastSchema(InventorySchema yeastSchema) {
        this.yeastSchema = yeastSchema;
    }
    public void setEquipmentSchema(InventorySchema equipmentSchema) {
        this.equipmentSchema = equipmentSchema;
    }
    public void setOtherSchema(InventorySchema otherSchema) {
        this.otherSchema = otherSchema;
    }

}
