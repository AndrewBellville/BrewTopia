package com.dev.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 4/17/2016.
 */
public class HopsSchema extends InventorySchema {

    private String Type="";
    private double AA=0.0;
    private String Use="";
    private int time=0;
    private double IBU=0.0;
    private double Amount=0.0;


    //Getters
    public String getType() {
        return Type;
    }
    public double getAA() {
        return AA;
    }
    public String getUse() {
        return Use;
    }
    public int getTime() {
        return time;
    }
    public double getIBU() {
        return IBU;
    }
    public double getAmount() {
        return Amount;
    }

    //Setters
    public void setType(String type) {
        Type = type;
    }
    public void setAA(double AA) {
        this.AA = AA;
    }
    public void setUse(String use) {
        Use = use;
    }
    public void setTime(int time) {
        this.time = time;
    }
    public void setIBU(double IBU) {
        this.IBU = IBU;
    }
    public void setAmount(double amount) {
        Amount = amount;
    }

}
