package com.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 4/22/2016.
 */
public class YeastSchema extends InventorySchema {

    private double attenuation=0.0;
    private String flocculation="";
    private double optimumTempLow=0.0;
    private double optimumTempHigh=0.0;
    private int starter=0;


    //Getters
    public double getAttenuation() {
        return attenuation;
    }
    public String getFlocculation() {
        return flocculation;
    }
    public double getOptimumTempLow() {
        return optimumTempLow;
    }
    public double getOptimumTempHigh() {
        return optimumTempHigh;
    }
    public int getStarter() {
        return starter;
    }
    public Boolean getBooleanStarter() {
        if(this.starter == 1)
            return true;
        else
        return false;
    }


    //Setters
    public void setAttenuation(double attenuation) {
        this.attenuation = attenuation;
    }
    public void setFlocculation(String flocculation) {
        this.flocculation = flocculation;
    }
    public void setOptimumTempLow(double optimumTempLow) {
        this.optimumTempLow = optimumTempLow;
    }
    public void setOptimumTempHigh(double optimumTempHigh) {
        this.optimumTempHigh = optimumTempHigh;
    }
    public void setStarter(int starter) {
        this.starter = starter;
    }
    public void setBooleanStarter(Boolean starter) {
        if(starter)
            this.starter = 1;
        else
            this.starter = 0;
    }

}
