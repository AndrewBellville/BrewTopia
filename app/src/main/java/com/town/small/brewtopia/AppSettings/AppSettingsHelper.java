package com.town.small.brewtopia.AppSettings;

import android.content.Context;
import android.widget.TableRow;

import com.town.small.brewtopia.DataClass.AppSettingsSchema;
import com.town.small.brewtopia.DataClass.CurrentUser;
import com.town.small.brewtopia.DataClass.DataBaseManager;
import com.town.small.brewtopia.DataClass.UserSchema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrew on 4/14/2016.
 */
public class AppSettingsHelper {

    DataBaseManager dbManager;
    UserSchema currentUser;

    private HashMap<String,String> AppSettingsMap;

    //What Screen they are for
    public static final String SCHEDULER = "Scheduler";

    //Setting Names
    public static final String SCHEDULER_CALENDAR_PUSH = "Push Calendar Scheduler";
    public static final String SCHEDULER_AUTO_CREATE = "Auto Create Schedule";

    //Setting Values
    public static final String OFF = "0";
    public static final String ON = "1";

    //Singleton
    private static AppSettingsHelper mInstance = null;

    public static AppSettingsHelper getInstance(Context aContext) {
        if (mInstance == null) {
            mInstance = new AppSettingsHelper(aContext.getApplicationContext());
        }
        return mInstance;
    }
    // constructor
    private AppSettingsHelper(Context aContext) {
        dbManager = DataBaseManager.getInstance(aContext);
    }

    public void LoadMap()
    {
        currentUser = CurrentUser.getInstance().getUser();
        List<AppSettingsSchema> settingsList = dbManager.getAllAppSettingsByUserId(currentUser.getUserId());
        AppSettingsMap = new HashMap<>();
        for (AppSettingsSchema appSettingsSchema : settingsList) {
            AppSettingsMap.put(appSettingsSchema.getSettingName(),appSettingsSchema.getSettingValue());
        }
    }

    //Create All app settings and load to DB should be called on user create only
    public void CreateAppSettings(long aUserId)
    {
        List<AppSettingsSchema> settingsList = new ArrayList<AppSettingsSchema>();
        settingsList.add(buildSettingsSchema(aUserId,SCHEDULER_CALENDAR_PUSH,SCHEDULER, OFF));
        settingsList.add(buildSettingsSchema(aUserId,SCHEDULER_AUTO_CREATE,SCHEDULER, ON));

        //Add all setting to DB
        dbManager.addAllAppSettings(settingsList);
    }

    public void UpdateAppSettings(AppSettingsSchema aAppSettingsSchema, boolean isSet)
    {
        if(isSet)
            aAppSettingsSchema.setSettingValue(ON);
        else
            aAppSettingsSchema.setSettingValue(OFF);

        dbManager.updateAppSetting(aAppSettingsSchema);
        LoadMap();
    }

    public AppSettingsSchema GetAppSettingsByName(String aSettingName)
    {
        currentUser = CurrentUser.getInstance().getUser();
        AppSettingsSchema appSettingsSchema = new AppSettingsSchema();
        appSettingsSchema.setUserId(currentUser.getUserId());
        appSettingsSchema.setSettingName(aSettingName);

        return dbManager.getAppSettingsBySettingName(appSettingsSchema);
    }

    public boolean GetBoolAppSettingsByName(String aSettingName)
    {
        try {
            if(AppSettingsMap.get(aSettingName).equals(OFF))
                return false;
            else if(AppSettingsMap.get(aSettingName).equals(ON))
                return true;
        }
        catch (Exception e){}

        return false;
    }

    private AppSettingsSchema buildSettingsSchema(long aUserId, String aName, String aScreen,  String aValue)
    {
        AppSettingsSchema appSettingsSchema = new AppSettingsSchema();
        appSettingsSchema.setUserId(aUserId);
        appSettingsSchema.setSettingName(aName);
        appSettingsSchema.setSettingScreen(aScreen);
        appSettingsSchema.setSettingValue(aValue);// 0 = off 1 = on
        return appSettingsSchema;
    }
}
