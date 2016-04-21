package com.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 4/17/2016.
 */
public class InventorySchema {

    private long InventoryId=-1;
    private long BrewId=-1;
    private long UserId=-1;
    private String InventoryName="";
    private double Amount=0.0;

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
    public double getAmount() {
        return Amount;
    }
    public String getInventoryName() {
        return InventoryName;
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
    public void setAmount(double amount) {
        Amount = amount;
    }
    public void setInventoryName(String inventoryName) {
        InventoryName = inventoryName;
    }
}
