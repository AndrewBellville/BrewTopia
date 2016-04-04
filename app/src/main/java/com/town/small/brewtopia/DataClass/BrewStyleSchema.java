package com.town.small.brewtopia.DataClass;

import android.util.Log;

/**
 * Created by Andrew on 3/1/2015.
 */
public class BrewStyleSchema {

    // Log cat tag
    private static final String LOG = "BrewStyleSchema";

    String UserName;
    String BrewStyleName;
    String BrewStyleColor;

    // constructors
    public BrewStyleSchema() {
    }

    // setters
    public void setUserName(String aUserName) {
        this.UserName = aUserName;
    }
    public String getBrewStyleName() {
        return BrewStyleName;
    }
    public String getBrewStyleColor() {
        return BrewStyleColor;
    }
    // getters
    public String getUserName() {
        return this.UserName;
    }
    public void setBrewStyleName(String brewStyleName) {
        BrewStyleName = brewStyleName;
    }
    public void setBrewStyleColor(String brewStyleColor) {
        BrewStyleColor = brewStyleColor;
    }
}
