package com.town.small.brewtopia.DataClass;

import android.util.Log;

/**
 * Created by Andrew on 3/1/2015.
 */
public class UserSchema {

    // Log cat tag
    private static final String LOG = "UserSchema";

    long userId;
    String UserName;
    private String Password;
    private String CreatedOn;
    private boolean isTemp = false;

    // constructors
    public UserSchema() {
    }

    public UserSchema(String aUserName, String aPassword) {

        Log.e(LOG, "Creating New User Schema UserName[" + aUserName + "], Password[" + aPassword + "]");
        this.UserName = aUserName;
        this.Password = aPassword;
    }

    // setters
    public void setUserName(String aUserName) {
        this.UserName = aUserName;
    }
    public void setPassword(String aPassword) {
        this.Password = aPassword;
    }
    public void setCreatedOn(String aCreatedOn){
        this.CreatedOn = aCreatedOn;
    }
    public long getUserId() {
        return userId;
    }
    public boolean isTemp() {
        return isTemp;
    }

    // getters
    public String getUserName() {
        return this.UserName;
    }
    public String getPassword() {
        return this.Password;
    }
    public String getCreatedOn() { return this.CreatedOn; }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public void setTemp(boolean temp) {
        isTemp = temp;
    }
}
