package com.town.small.brewtopia.DataClass;

import android.util.Log;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Andrew on 3/1/2015.
 */
public class BrewSchema {

    // Log cat tag
    private static final String LOG = "BrewSchema";

    private int id;
    private String BrewName;
    private int boilTime;
    private int Primary;
    private int Secondary;
    private int Bottle;
    private String Description;
    private String CreatedOn;
    private List <BoilAdditionsSchema> boilAdditionlist;

    // constructors
    public BrewSchema() {
    }

    public BrewSchema(String aBrewName) {

        Log.e(LOG, "Creating New Brew Schema Brew Name[" + aBrewName + "]");
        this.BrewName = aBrewName;
    }

    public void setListBrewName()
    {
        for(Iterator<BoilAdditionsSchema> i = boilAdditionlist.iterator(); i.hasNext();)
        {
            BoilAdditionsSchema baSchema = i.next();
            baSchema.setBrewName(getBrewName());
        }
    }

    //getters
    public int getId() {
        return id;
    }
    public String getBrewName() {
        return BrewName;
    }
    public int getBoilTime() {
        return boilTime;
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
    public List<BoilAdditionsSchema> getBoilAdditionlist() {
        return boilAdditionlist;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setBrewName(String brewName) {
        BrewName = brewName;
    }
    public void setBoilTime(int boilTime) {
        this.boilTime = boilTime;
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
    public void setBoilAdditionlist(List<BoilAdditionsSchema> boilAdditionlist) {
        this.boilAdditionlist = boilAdditionlist;
    }

}
