package com.dev.town.small.brewtopia.DataClass;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 3/1/2015.
 */
public class UserSchema {

    // Log cat tag
    private static final String LOG = "UserSchema";

    //User Roles
    public enum UserRoles {
        Admin(0), User(1), Unknown(2);

        private final int value;
        private UserRoles(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    long userId=-1;
    String UserName;
    private String Password;
    int Role = 1; //Role 0=Admin,1=user,2=unknown
    private String CreatedOn;
    private boolean isTemp = false;
    private List<AppSettingsSchema> appSettingsSchemas = new ArrayList<>();

    // constructors
    public UserSchema() {
    }

    public UserSchema(String aUserName, String aPassword) {

        if(APPUTILS.isLogging)Log.e(LOG, "Creating New User Schema UserName[" + aUserName + "], Password[" + aPassword + "]");
        this.UserName = aUserName;
        this.Password = aPassword;
    }

    public void writeString()
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Id[" + getUserId() + "] UserName[" + getUserName() + "]  Role[" + getRole() + "]  Password[" + getPassword() + "]");
    }

    // setters
    public void setUserName(String aUserName) {
        this.UserName = aUserName;
    }
    public void setPassword(String aPassword) {
        this.Password = aPassword;
    }
    public int getRole() {
        return Role;
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
    public List<AppSettingsSchema> getAppSettingsSchemas() {
        return appSettingsSchemas;
    }

    // getters
    public String getUserName() {
        return this.UserName;
    }
    public String getPassword() {
        return this.Password;
    }
    public void setRole(int role) {
        Role = role;
    }
    public String getCreatedOn() { return this.CreatedOn; }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public void setTemp(boolean temp) {
        isTemp = temp;
    }
    public void setAppSettingsSchemas(List<AppSettingsSchema> appSettingsSchemas) {
        this.appSettingsSchemas = appSettingsSchemas;
    }
}
