package com.dev.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 4/17/2016.
 */
public class HopsSchema extends InventorySchema {

    private String Type="";
    private double AA=0.0;
    private String Use="";
    private int time=0;
    private double Amount=0.0;
    private String[] types = new String[]{"Pellet", "Leaf"};
    private String[] uses = new String[]{"Boil", "Dry Hop", "Whirlpool"};


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
    public double getAmount() {
        return Amount;
    }
    public String[] getTypes() {return types;}
    public String[] getUses() {return uses;}

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
    public void setAmount(double amount) {
        Amount = amount;
    }

}
