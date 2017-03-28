package com.dev.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 3/7/2015.
 */
public class BoilAdditionsSchema {

    private long additionId=0;
    private long brewId=0;
    private String additionName = "";
    private int additionTime = 0;
    private double additionQty = 0.0;
    private String uOfM;
    private int isNew = 1;//1=new 0=not new

    public BoilAdditionsSchema() {
    }

    //getters
    public String getAdditionName() {
        return additionName;
    }
    public int getAdditionTime() {
        return additionTime;
    }
    public long getBrewId() {
        return brewId;
    }
    public double getAdditionQty() {
        return additionQty;
    }
    public String getUOfM() {
        return uOfM;
    }
    public long getAdditionId() {
        return additionId;
    }
    public int getIsNew() {
        return isNew;
    }


    //setters
    public void setAdditionName(String additionName) {
        this.additionName = additionName;
    }
    public void setAdditionTime(int additionTime) {
        this.additionTime = additionTime;
    }
    public void setBrewId(long BrewId) {
        this.brewId = BrewId;
    }
    public void setAdditionQty(double additionQty) {
        this.additionQty = additionQty;
    }
    public void setUOfM(String uOfM) {
        this.uOfM = uOfM;
    }
    public void setAdditionId(long additionId) {
        this.additionId = additionId;
    }
    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }


}
