package com.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 4/11/2016.
 */
public class AppSettingsSchema {

    // Log cat tag
    private static final String LOG = "AppSettingsSchema";

    private int appSetttingId;
    private int userId;
    private String settingName;
    private String settingScreen;
    private String settingValue;

    public AppSettingsSchema() {
    }


    //Getters
    public String getSettingName() {
        return settingName;
    }
    public String getSettingValue() {
        return settingValue;
    }
    public int getUserId() {
        return userId;
    }
    public int getAppSetttingId() {
        return appSetttingId;
    }
    public String getSettingScreen() {
        return settingScreen;
    }

    //Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }
    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }
    public void setAppSetttingId(int appSetttingId) {
        this.appSetttingId = appSetttingId;
    }
    public void setSettingScreen(String settingScreen) {
        this.settingScreen = settingScreen;
    }

}
