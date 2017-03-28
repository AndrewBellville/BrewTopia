package com.dev.town.small.brewtopia.DataClass;

import android.graphics.Bitmap;

/**
 * Created by Andrew on 10/17/2016.
 */
public class BrewImageSchema {

    private long imageId=0;
    private long brewId=0;
    private Bitmap image;
    private String CreatedOn;
    private int isNew = 1;//1=new 0=not new

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
    public int getIsNew() {
        return isNew;
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
    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }
}
