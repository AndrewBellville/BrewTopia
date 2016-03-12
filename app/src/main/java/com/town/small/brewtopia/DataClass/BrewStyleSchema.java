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

    // getters
    public String getUserName() {
        return this.UserName;
    }
    public void setBrewStyleName(String brewStyleName) {
        BrewStyleName = brewStyleName;
    }
}
