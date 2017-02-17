package com.dev.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 3/1/2015.
 */
public class BrewStyleSchema {

    // Log cat tag
    private static final String LOG = "BrewStyleSchema";

    private long userId =-1;
    private String BrewStyleName;
    private String BrewStyleColor;

    // constructors
    public BrewStyleSchema() {
    }

    // setters
    public void setUserId(long aUserId) {
        this.userId = aUserId;
    }
    public String getBrewStyleName() {
        return BrewStyleName;
    }
    public String getBrewStyleColor() {
        return BrewStyleColor;
    }
    // getters
    public long getUserId() {
        return this.userId;
    }
    public void setBrewStyleName(String brewStyleName) {
        BrewStyleName = brewStyleName;
    }
    public void setBrewStyleColor(String brewStyleColor) {
        BrewStyleColor = brewStyleColor;
    }
}
