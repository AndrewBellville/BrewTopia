package com.dev.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 3/1/2015.
 */
public class BrewStyleSchema {

    // Log cat tag
    private static final String LOG = "BrewStyleSchema";

    private long styleId =-1;
    private long userId =-1;
    private String BrewStyleName;
    private String BrewStyleColor;

    // constructors
    public BrewStyleSchema() {
    }

    // getters
    public long getStyleId() {
        return styleId;
    }
    public long getUserId() {
        return this.userId;
    }
    public String getBrewStyleName() {
        return BrewStyleName;
    }
    public String getBrewStyleColor() {
        return BrewStyleColor;
    }
    // setters
    public void setStyleId(long styleId) {
        this.styleId = styleId;
    }
    public void setUserId(long aUserId) {
        this.userId = aUserId;
    }
    public void setBrewStyleName(String brewStyleName) {
        BrewStyleName = brewStyleName;
    }
    public void setBrewStyleColor(String brewStyleColor) {
        BrewStyleColor = brewStyleColor;
    }
}
