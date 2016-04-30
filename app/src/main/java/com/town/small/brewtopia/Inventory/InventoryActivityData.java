package com.town.small.brewtopia.Inventory;

import com.town.small.brewtopia.DataClass.EquipmentSchema;
import com.town.small.brewtopia.DataClass.FermentablesSchema;
import com.town.small.brewtopia.DataClass.GrainsSchema;
import com.town.small.brewtopia.DataClass.HopsSchema;
import com.town.small.brewtopia.DataClass.InventorySchema;
import com.town.small.brewtopia.DataClass.YeastSchema;

/**
 * Created by Andrew on 4/18/2016.
 */
public class InventoryActivityData {

    //State for AddEditView Brew and Brew Name if Edit/View
    public enum DisplayMode {
        ADD, BREW_ADD, EDIT, VIEW
    };

    private DisplayMode AddEditViewState = DisplayMode.VIEW; // STATES: Add, Edit, View

    private InventorySchema hopsSchema;
    private InventorySchema fermentablesSchema;
    private InventorySchema grainsSchema;
    private InventorySchema yeastSchema;
    private InventorySchema equipmentSchema;

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

}
