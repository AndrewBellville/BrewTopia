package com.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 11/8/2015.
 */
public class CalculationsSchema {

    private String CalculationAbv;
    private String CalculationName;

    // constructors
    public CalculationsSchema() {
    }


    //Getters
    public String getCalculationName() {
        return CalculationName;
    }
    public String getCalculationAbv() {
        return CalculationAbv;
    }

    //Setters
    public void setCalculationAbv(String calculationAbv) {
        CalculationAbv = calculationAbv;
    }
    public void setCalculationName(String calculationName) {
        CalculationName = calculationName;
    }
}
