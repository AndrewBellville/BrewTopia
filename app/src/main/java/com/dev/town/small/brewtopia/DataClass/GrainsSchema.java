package com.dev.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 4/22/2016.
 */
public class GrainsSchema extends InventorySchema {

    private double poundPerGallon=0.0;
    private double Lovibond=0.0;
    private double bill=0.0;

    //Getters
    public double getPoundPerGallon() {
        return poundPerGallon;
    }
    public double getLovibond() {
        return Lovibond;
    }
    public double getBill() {
        return bill;
    }


    //Setters
    public void setPoundPerGallon(double poundPerGallon) {
        this.poundPerGallon = poundPerGallon;
    }
    public void setLovibond(double lovibond) {
        Lovibond = lovibond;
    }
    public void setBill(double bill) {
        this.bill = bill;
    }
}
