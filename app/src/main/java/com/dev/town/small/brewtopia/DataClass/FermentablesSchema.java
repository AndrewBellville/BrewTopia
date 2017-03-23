package com.dev.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 4/22/2016.
 */
public class FermentablesSchema extends InventorySchema {

    private double poundPerGallon=0.0;
    private double Lovibond=0.0;
    private String type="";
    private double yield=0.0;
    private double potential=0.0;

    //Getters
    public double getPoundPerGallon() {
        return poundPerGallon;
    }
    public double getLovibond() {
        return Lovibond;
    }
    public double getPotential() {
        return potential;
    }
    public double getYield() {
        return yield;
    }
    public String getType() {
        return type;
    }

    //Setters
    public void setPoundPerGallon(double poundPerGallon) {
        this.poundPerGallon = poundPerGallon;
    }
    public void setLovibond(double lovibond) {
        Lovibond = lovibond;
    }
    public void setPotential(double potential) {
        this.potential = potential;
    }
    public void setYield(double yield) {
        this.yield = yield;
    }
    public void setType(String type) {
        this.type = type;
    }
}
