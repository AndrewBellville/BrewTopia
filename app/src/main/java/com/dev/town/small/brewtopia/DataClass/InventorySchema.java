package com.dev.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 4/17/2016.
 */
public class InventorySchema {

    private long InventoryId=0;
    private long BrewId=0;
    private long UserId=-1;
    private long ScheduleId=0;
    private String InventoryName="";
    private int InvetoryQty=0;
    private double amount=0.0;
    private String InventoryUOfM="";
    private int isNew = 1;//1=new 0=not new


    //Getters
    public long getInventoryId() {
        return InventoryId;
    }
    public long getBrewId() {
        return BrewId;
    }
    public long getUserId() {
        return UserId;
    }
    public long getScheduleId() {
        return ScheduleId;
    }
    public String getInventoryName() {
        return InventoryName;
    }
    public int getInvetoryQty() {
        return InvetoryQty;
    }
    public double getAmount() {
        return amount;
    }
    public String getInventoryUOfM() {
        return InventoryUOfM;
    }
    public int getIsNew() {
        return isNew;
    }

    //Setters
    public void setInventoryId(long inventoryId) {
        InventoryId = inventoryId;
    }
    public void setBrewId(long brewId) {
        BrewId = brewId;
    }
    public void setUserId(long userId) {
        UserId = userId;
    }
    public void setScheduleId(long scheduleId) {
        ScheduleId = scheduleId;
    }
    public void setInventoryName(String inventoryName) {
        InventoryName = inventoryName;
    }
    public void setInvetoryQty(int invetoryQty) {
        InvetoryQty = invetoryQty;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setInventoryUOfM(String inventoryUOfM) {
        InventoryUOfM = inventoryUOfM;
    }
    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

}
