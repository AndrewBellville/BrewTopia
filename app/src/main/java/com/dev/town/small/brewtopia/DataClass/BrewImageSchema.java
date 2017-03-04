package com.dev.town.small.brewtopia.DataClass;

import android.graphics.Bitmap;

/**
 * Created by Andrew on 10/17/2016.
 */
public class BrewImageSchema {

    private long imageId=-1;
    private long brewId=-1;
    private Bitmap image;
    private String CreatedOn;

    // constructors
    public BrewImageSchema() {
    }

    //Getters
    public long getImageId() {
        return imageId;
    }
    public long getBrewId() {
        return brewId;
    }
    public Bitmap getImage() {
        return image;
    }
    public String getCreatedOn() {
        return CreatedOn;
    }

    //Setters
    public void setImageId(long imageId) {
        this.imageId = imageId;
    }
    public void setBrewId(long brewId) {
        this.brewId = brewId;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }
}
