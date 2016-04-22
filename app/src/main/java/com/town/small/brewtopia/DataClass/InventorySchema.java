package com.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 4/17/2016.
 */
public class InventorySchema {

    private long InventoryId=-1;
    private long BrewId=-1;
    private long UserId=-1;
    private String InventoryName="";
    private int InvetoryQty=0;


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
    public String getInventoryName() {
        return InventoryName;
    }
    public int getInvetoryQty() {
        return InvetoryQty;
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
    public void setInventoryName(String inventoryName) {
        InventoryName = inventoryName;
    }
    public void setInvetoryQty(int invetoryQty) {
        InvetoryQty = invetoryQty;
    }
}
