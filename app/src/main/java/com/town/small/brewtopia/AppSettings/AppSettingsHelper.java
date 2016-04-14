package com.town.small.brewtopia.AppSettings;

import android.content.Context;
import android.widget.TableRow;

import com.town.small.brewtopia.DataClass.AppSettingsSchema;
import com.town.small.brewtopia.DataClass.CurrentUser;
import com.town.small.brewtopia.DataClass.DataBaseManager;
import com.town.small.brewtopia.DataClass.UserSchema;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 4/14/2016.
 */
public class AppSettingsHelper {

    Context context;
    DataBaseManager dbManager;
    UserSchema currentUser;

    //What Screen they are for
    public static final String SCHEDULER = "Scheduler";

    //Setting Names
    public static final String SCHEDULER_CALENDAR_PUSH = "Push Calendar Scheduler";

    //Setting Values
    public static final String OFF = "0";
    public static final String ON = "1";

    public AppSettingsHelper(Context context)
    {
        dbManager = DataBaseManager.getInstance(context);
    }

    public void CreateAppSettings(int aUserId)
    {
        List<AppSettingsSchema> settingsList = new ArrayList<AppSettingsSchema>();
        settingsList.add(buildSettingsSchema(aUserId,SCHEDULER_CALENDAR_PUSH,SCHEDULER, OFF));

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
        currentUser = CurrentUser.getInstance().getUser();
        AppSettingsSchema appSettingsSchema = new AppSettingsSchema();
        appSettingsSchema.setUserId(currentUser.getUserId());
        appSettingsSchema.setSettingName(aSettingName);

        appSettingsSchema = dbManager.getAppSettingsBySettingName(appSettingsSchema);

        try {
            if(appSettingsSchema.getSettingValue().equals(OFF))
                return false;
            else if(appSettingsSchema.getSettingValue().equals(ON))
                return true;
        }
        catch (Exception e){}

        return false;
    }

    private AppSettingsSchema buildSettingsSchema(int aUserId, String aName, String aScreen,  String aValue)
    {
        AppSettingsSchema appSettingsSchema = new AppSettingsSchema();
        appSettingsSchema.setUserId(aUserId);
        appSettingsSchema.setSettingName(aName);
        appSettingsSchema.setSettingScreen(aScreen);
        appSettingsSchema.setSettingValue(aValue);// 0 = off 1 = on
        return appSettingsSchema;
    }
}
