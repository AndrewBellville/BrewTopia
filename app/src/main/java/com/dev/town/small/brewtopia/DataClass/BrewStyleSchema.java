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
    private String type;
    private double minOG=0.0;
    private double maxOG=0.0;
    private double minFG=0.0;
    private double maxFG=0.0;
    private double minIBU=0.0;
    private double maxIBU=0.0;
    private double minSRM=0.0;
    private double maxSRM=0.0;
    private double minABV=0.0;
    private double maxABV=0.0;
    private int isNew = 1;//1=new 0=not new

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
    public double getMinFG() {
        return minFG;
    }
    public double getMinIBU() {
        return minIBU;
    }
    public double getMaxFG() {
        return maxFG;
    }
    public double getMaxIBU() {
        return maxIBU;
    }
    public double getMinSRM() {
        return minSRM;
    }
    public double getMaxSRM() {
        return maxSRM;
    }
    public double getMinABV() {
        return minABV;
    }
    public double getMaxABV() {
        return maxABV;
    }
    public double getMaxOG() {
        return maxOG;
    }
    public double getMinOG() {
        return minOG;
    }
    public String getType() {
        return type;
    }
    public int getIsNew() {
        return isNew;
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
    public void setMinFG(double minFG) {
        this.minFG = minFG;
    }
    public void setMinIBU(double minIBU) {
        this.minIBU = minIBU;
    }
    public void setMaxFG(double maxFG) {
        this.maxFG = maxFG;
    }
    public void setMaxIBU(double maxIBU) {
        this.maxIBU = maxIBU;
    }
    public void setMinSRM(double minSRM) {
        this.minSRM = minSRM;
    }
    public void setMaxSRM(double maxSRM) {
        this.maxSRM = maxSRM;
    }
    public void setMinABV(double minABV) {
        this.minABV = minABV;
    }
    public void setMaxABV(double maxABV) {
        this.maxABV = maxABV;
    }
    public void setMaxOG(double maxOG) {
        this.maxOG = maxOG;
    }
    public void setMinOG(double minOG) {
        this.minOG = minOG;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }
}
