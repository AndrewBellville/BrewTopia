package com.dev.town.small.brewtopia.DataClass;

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

    private long brewId =0;
    private String BrewName;
    private long UserId = -1;
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
    private double BatchSize=0.0;
    private double Efficiency=0.0;
    private String method;
    private List <BoilAdditionsSchema> boilAdditionlist = new ArrayList<BoilAdditionsSchema>();
    private List <BrewNoteSchema> brewNoteSchemaList = new ArrayList<BrewNoteSchema>();
    private List <InventorySchema> brewInventorySchemaList = new ArrayList<InventorySchema>();
    private List<BrewImageSchema> brewImageSchemaList = new ArrayList<BrewImageSchema>();
    private List<ScheduledBrewSchema> scheduledBrewSchemas = new ArrayList<ScheduledBrewSchema>();
    private List<ScheduledEventSchema> scheduledEventSchemas = new ArrayList<ScheduledEventSchema>();
    private String Style;
    private BrewStyleSchema StyleSchema;
    private int totalBrewed=0;
    private int SRM=0;
    private int isDirty=0; //0 = no 1 = yes
    private int isGlobal=0; //0 = no 1 = yes

    // constructors
    public BrewSchema() {
    }

    public BrewSchema(String aBrewName) {

        if(APPUTILS.isLogging)Log.e(LOG, "Creating New Brew Schema Brew Name[" + aBrewName + "]");
        this.BrewName = aBrewName;
    }

    public void setListUserId()
    {
        for(Iterator<InventorySchema> i = brewInventorySchemaList.iterator(); i.hasNext();)
        {
            InventorySchema iSchema = i.next();
            iSchema.setUserId(getUserId());
        }
    }

    private void CalculateABV()
    {
        if(targetFG != 0)
            setTargetABV( APPUTILS.GetTruncatedABV(APPUTILS.GetABV(targetOG,targetFG)) );
        else
            setTargetABV(0.0);
    }

    //getters
    public long getBrewId() {
        return brewId;
    }
    public String getBrewName() {
        return BrewName;
    }
    public Long getUserId() {
        if(UserId == -1)
        {
           return CurrentUser.getInstance().getUser().getUserId();
        }
        return UserId;
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
    public double getBatchSize() {
        return BatchSize;
    }
    public double getEfficiency() {
        return Efficiency;
    }
    public List<InventorySchema> getBrewInventorySchemaList() {
        return brewInventorySchemaList;
    }
    public int getTotalBrewed() {
        return totalBrewed;
    }
    public List<BrewImageSchema> getBrewImageSchemaList() {
        return brewImageSchemaList;
    }
    public List<ScheduledBrewSchema> getScheduledBrewSchemas() {
        return scheduledBrewSchemas;
    }
    public List<ScheduledEventSchema> getScheduledEventSchemas() {
        return scheduledEventSchemas;
    }
    public int getSRM() {
        return SRM;
    }
    public int getIsDirty() {
        return isDirty;
    }
    public boolean getBooleanIsDirty() {
        if(this.isDirty ==  1)
            return true;
        else
            return false;
    }
    public int getIsGlobal() {
        return isGlobal;
    }
    public boolean getBooleanIsGlobal() {
        if(this.isGlobal ==  1)
            return true;
        else
            return false;
    }


    //setters
    public void setBrewId(long id) {
        this.brewId = id;
    }
    public void setBrewName(String brewName) {
        BrewName = brewName;
    }
    public void setUserId(long userId) {
        this.UserId = userId;
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
        CalculateABV();
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
    public void setBatchSize(double batchSize) {
        BatchSize = batchSize;
    }
    public void setEfficiency(double efficiency) {
        Efficiency = efficiency;
    }
    public void setBrewInventorySchemaList(List<InventorySchema> brewInventorySchemaList) {
        this.brewInventorySchemaList = brewInventorySchemaList;
    }
    public void setTotalBrewed(int totalBrewed) {
        this.totalBrewed = totalBrewed;
    }
    public void setBrewImageSchemaList(List<BrewImageSchema> brewImageSchemaList) {
        this.brewImageSchemaList = brewImageSchemaList;
    }
    public void setScheduledBrewSchemas(List<ScheduledBrewSchema> scheduledBrewSchemas) {
        this.scheduledBrewSchemas = scheduledBrewSchemas;
    }
    public void setScheduledEventSchemas(List<ScheduledEventSchema> scheduledEventSchemas) {
        this.scheduledEventSchemas = scheduledEventSchemas;
    }
    public void setSRM(int SRM) {
        this.SRM = SRM;
    }
    public void setIsDirty(int isDirty) {
        this.isDirty = isDirty;
    }
    public void setBooleanIsDirty(boolean isDirty) {
        if(isDirty)
            this.isDirty = 1;
        else
            this.isDirty = 0;
    }
    public void setIsGlobal(int isGlobal) {
        this.isGlobal = isGlobal;
    }
    public void setBooleanIsGlobal(boolean isGlobal) {
        if(isGlobal)
            this.isGlobal = 1;
        else
            this.isGlobal = 0;
    }
}
