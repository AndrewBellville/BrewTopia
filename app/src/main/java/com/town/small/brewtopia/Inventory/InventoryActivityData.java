package com.town.small.brewtopia.Inventory;

import com.town.small.brewtopia.DataClass.EquipmentSchema;
import com.town.small.brewtopia.DataClass.FermentablesSchema;
import com.town.small.brewtopia.DataClass.GrainsSchema;
import com.town.small.brewtopia.DataClass.HopsSchema;
import com.town.small.brewtopia.DataClass.YeastSchema;

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
    private FermentablesSchema fermentablesSchema;
    private GrainsSchema grainsSchema;
    private YeastSchema yeastSchema;
    private EquipmentSchema equipmentSchema;

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
    public FermentablesSchema getFermentablesSchema() {
        return fermentablesSchema;
    }
    public GrainsSchema getGrainsSchema() {
        return grainsSchema;
    }
    public YeastSchema getYeastSchema() {
        return yeastSchema;
    }
    public EquipmentSchema getEquipmentSchema() {
        return equipmentSchema;
    }


    //Setters
    public void setHopsSchema(HopsSchema hopsSchema) {
        this.hopsSchema = hopsSchema;
    }
    public void setAddEditViewState(DisplayMode addEditViewState) {
        AddEditViewState = addEditViewState;
    }
    public void setFermentablesSchema(FermentablesSchema fermentablesSchema) {
        this.fermentablesSchema = fermentablesSchema;
    }
    public void setGrainsSchema(GrainsSchema grainsSchema) {
        this.grainsSchema = grainsSchema;
    }
    public void setYeastSchema(YeastSchema yeastSchema) {
        this.yeastSchema = yeastSchema;
    }
    public void setEquipmentSchema(EquipmentSchema equipmentSchema) {
        this.equipmentSchema = equipmentSchema;
    }

}
