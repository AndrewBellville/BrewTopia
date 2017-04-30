package com.dev.town.small.brewtopia.SharedMemory;

import android.content.Context;

import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.EquipmentSchema;
import com.dev.town.small.brewtopia.DataClass.FermentablesSchema;
import com.dev.town.small.brewtopia.DataClass.HopsSchema;
import com.dev.town.small.brewtopia.DataClass.InventorySchema;
import com.dev.town.small.brewtopia.DataClass.OtherSchema;
import com.dev.town.small.brewtopia.DataClass.YeastSchema;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 4/29/2017.
 */
public class InventoryMemory {

    //Singleton
    private static InventoryMemory mInstance = null;

    public static InventoryMemory getInstance() {
        if (mInstance == null) {
            mInstance = new InventoryMemory();
        }
        return mInstance;
    }
    // constructor
    private InventoryMemory() {
    }

    private List<InventorySchema> hopsSchemas = new ArrayList<>();
    private List<InventorySchema> fermentablesSchemas = new ArrayList<>();
    private List<InventorySchema> yeastSchemas = new ArrayList<>();
    private List<InventorySchema> equipmentSchemas = new ArrayList<>();
    private List<InventorySchema> otherSchemas = new ArrayList<>();
    private DataBaseManager dbManager ;

    public void load(Context context)
    {
        dbManager = DataBaseManager.getInstance(context);

        hopsSchemas = dbManager.getAllHopsByUserId(1);
        fermentablesSchemas = dbManager.getAllFermentablesByUserId(1);
        yeastSchemas = dbManager.getAllYeastByUserId(1);
        equipmentSchemas = dbManager.getAllEquipmentByUserId(1);
        otherSchemas = dbManager.getAllOtherByUserId(1);
    }

    //getters
    public List<InventorySchema> getHopsSchemas() {
        return hopsSchemas;
    }
    public List<InventorySchema> getYeastSchemas() {
        return yeastSchemas;
    }
    public List<InventorySchema> getFermentablesSchemas() {
        return fermentablesSchemas;
    }
    public List<InventorySchema> getEquipmentSchemas() {
        return equipmentSchemas;
    }
    public List<InventorySchema> getOtherSchemas() {
        return otherSchemas;
    }

    //setters
    public void setHopsSchemas(List<InventorySchema> hopsSchemas) {
        this.hopsSchemas = hopsSchemas;
    }
    public void setYeastSchemas(List<InventorySchema> yeastSchemas) {
        this.yeastSchemas = yeastSchemas;
    }
    public void setFermentablesSchemas(List<InventorySchema> fermentablesSchemas) {
        this.fermentablesSchemas = fermentablesSchemas;
    }
    public void setEquipmentSchemas(List<InventorySchema> equipmentSchemas) {
        this.equipmentSchemas = equipmentSchemas;
    }
    public void setOtherSchemas(List<InventorySchema> otherSchemas) {
        this.otherSchemas = otherSchemas;
    }
}
