package com.town.small.brewtopia.DataClass;

import android.graphics.Bitmap;

/**
 * Created by Andrew on 10/17/2016.
 */
public class BrewImageSchema {

    private long globalImageId=-1;
    private long imageId=-1;
    private long brewId=-1;
    private long userId=-1;
    private Bitmap image;
    private String CreatedOn;

    // constructors
    public BrewImageSchema() {
    }

    //Getters
    public long getGlobalImageId() {
        return globalImageId;
    }
    public long getImageId() {
        return imageId;
    }
    public long getBrewId() {
        return brewId;
    }
    public long getUserId() {
        return userId;
    }
    public Bitmap getImage() {
        return image;
    }
    public String getCreatedOn() {
        return CreatedOn;
    }

    //Setters
    public void setGlobalImageId(long globalImageId) {
        this.globalImageId = globalImageId;
    }
    public void setImageId(long imageId) {
        this.imageId = imageId;
    }
    public void setBrewId(long brewId) {
        this.brewId = brewId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }
}
