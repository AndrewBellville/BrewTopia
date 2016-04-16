package com.town.small.brewtopia.DataClass;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Andrew on 3/1/2015.
 */
public class BrewSchema {

    // Log cat tag
    private static final String LOG = "BrewSchema";

    private int brewId;
    private String BrewName;
    private String UserName;
    private int boilTime;
    private int Primary;
    private int Secondary;
    private int Bottle;
    private Double targetOG;
    private Double targetFG;
    private Double targetABV;
    private String Description;
    private String CreatedOn;
    private int favorite=0; //0 = no 1 = yes
    private int scheduled=0; //0 = no 1 = yes
    private int onTap=0; //0 = no 1 = yes
    private double IBU=0.0;
    private String method;
    private List <BoilAdditionsSchema> boilAdditionlist = new ArrayList<BoilAdditionsSchema>();
    private List <BrewNoteSchema> brewNoteSchemaList = new ArrayList<BrewNoteSchema>();;
    private String Style;
    private BrewStyleSchema StyleSchema;

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
            baSchema.setUserName(getUserName());
        }

        for(Iterator<BrewNoteSchema> i = brewNoteSchemaList.iterator(); i.hasNext();)
        {
            BrewNoteSchema nSchema = i.next();
            nSchema.setBrewName(getBrewName());
            nSchema.setUserName(getUserName());
        }
    }

    //getters
    public int getBrewId() {
        return brewId;
    }
    public String getBrewName() {
        return BrewName;
    }
    public String getUserName() {
        if(UserName.equals(""))
        {
           return CurrentUser.getInstance().getUser().UserName;
        }
        return UserName;
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
    public String getStyle() {
        return Style;
    }
    public BrewStyleSchema getStyleSchema() {
        return StyleSchema;
    }
    public Double getTargetOG() {
        return targetOG;
    }
    public Double getTargetFG() {
        return targetFG;
    }
    public Double getTargetABV() {
        return targetABV;
    }
    public List<BrewNoteSchema> getBrewNoteSchemaList() {
        return brewNoteSchemaList;
    }
    public int getFavorite() {
        return favorite;
    }
    public int getScheduled() {
        return scheduled;
    }
    public int getOnTap() {
        return onTap;
    }
    public boolean getBooleanFavorite() {
        if(this.favorite ==  1)
            return true;
        else
            return false;
    }
    public boolean getBooleanScheduled() {
        if(this.scheduled ==  1)
            return true;
        else
            return false;
    }
    public boolean getBooleanOnTap() {
        if(this.onTap ==  1)
            return true;
        else
            return false;
    }
    public double getIBU() {
        return IBU;
    }
    public String getMethod() {
        return method;
    }


    //setters
    public void setBrewId(int id) {
        this.brewId = id;
    }
    public void setBrewName(String brewName) {
        BrewName = brewName;
    }
    public void setUserName(String userName) {
        UserName = userName;
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
    public void setStyle(String style) {
        Style = style;
    }
    public void setStyleSchema(BrewStyleSchema styleSchema) {
        StyleSchema = styleSchema;
    }
    public void setTargetOG(Double targetOG) {
        this.targetOG = targetOG;
    }
    public void setTargetFG(Double targetFG) {
        this.targetFG = targetFG;
    }
    public void setTargetABV(Double targetABV) {
        this.targetABV = targetABV;
    }
    public void setBrewNoteSchemaList(List<BrewNoteSchema> brewNoteSchemaList) {
        this.brewNoteSchemaList = brewNoteSchemaList;
    }
    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
    public void setScheduled(int scheduled) {
        this.scheduled = scheduled;
    }
    public void setOnTap(int onTap) {
        this.onTap = onTap;
    }
    public void setBooleanFavorite(boolean favorite) {
        if(favorite)
            this.favorite = 1;
        else
            this.favorite = 0;
    }
    public void setBooleanScheduled(boolean scheduled) {
        if(scheduled)
            this.scheduled = 1;
        else
            this.scheduled = 0;
    }
    public void setBooleanOnTap(boolean onTap) {
        if(onTap)
            this.onTap = 1;
        else
            this.onTap = 0;
    }
    public void setIBU(double IBU) {
        this.IBU = IBU;
    }
    public void setMethod(String method) {
        this.method = method;
    }

}
