package com.dev.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 4/22/2016.
 */
public class YeastSchema extends InventorySchema {

    private double attenuation=0.0;
    private String flocculation="";
    private double optimumTempLow=0.0;
    private double optimumTempHigh=0.0;
    private int starter=0;
    private String Laboratory="";
    private String Type="";
    private String Form="";


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
    public String getLaboratory() {
        return Laboratory;
    }
    public String getType() {
        return Type;
    }
    public String getForm() {
        return Form;
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
    public void setLaboratory(String laboratory) {
        Laboratory = laboratory;
    }
    public void setType(String type) {
        Type = type;
    }
    public void setForm(String form) {
        Form = form;
    }

}
