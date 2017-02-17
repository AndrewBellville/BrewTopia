package com.dev.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 4/11/2016.
 */
public class AppSettingsSchema {

    // Log cat tag
    private static final String LOG = "AppSettingsSchema";

    private long appSetttingId=-1;
    private long userId=-1;
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
    public long getUserId() {
        return userId;
    }
    public long getAppSetttingId() {
        return appSetttingId;
    }
    public String getSettingScreen() {
        return settingScreen;
    }

    //Setters
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }
    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }
    public void setAppSetttingId(long appSetttingId) {
        this.appSetttingId = appSetttingId;
    }
    public void setSettingScreen(String settingScreen) {
        this.settingScreen = settingScreen;
    }

}
