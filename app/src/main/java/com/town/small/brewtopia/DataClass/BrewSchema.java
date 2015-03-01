package com.town.small.brewtopia.DataClass;

import android.util.Log;

/**
 * Created by Andrew on 3/1/2015.
 */
public class BrewSchema {

    // Log cat tag
    private static final String LOG = "BrewSchema";

    private int id;
    private String BrewName;
    private int Primary;
    private int Secondary;
    private int Bottle;
    private String Description;
    private String CreatedOn;

    // constructors
    public BrewSchema() {
    }

    public BrewSchema(String aBrewName) {

        Log.e(LOG, "Creating New Brew Schema Brew Name[" + aBrewName + "]");
        this.BrewName = aBrewName;
    }

    //getters
    public int getId() {
        return id;
    }
    public String getBrewName() {
        return BrewName;
    }
    public int getPrimary() {
        return Primary;
    }
    public String getCreatedOn() {
        return CreatedOn;
    }
    public String getDescription() {
        return Description;
    }
    public int getBottle() {
        return Bottle;
    }
    public int getSecondary() {
        return Secondary;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setBrewName(String brewName) {
        BrewName = brewName;
    }
    public void setPrimary(int primary) {
        Primary = primary;
    }
    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }
    public void setDescription(String description) {
        Description = description;
    }
    public void setBottle(int bottle) {
        Bottle = bottle;
    }
    public void setSecondary(int secondary) {
        Secondary = secondary;
    }

}
