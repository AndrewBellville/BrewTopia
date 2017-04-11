package com.dev.town.small.brewtopia.WebAPI;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.AppSettingsSchema;
import com.dev.town.small.brewtopia.DataClass.BoilAdditionsSchema;
import com.dev.town.small.brewtopia.DataClass.BrewImageSchema;
import com.dev.town.small.brewtopia.DataClass.BrewNoteSchema;
import com.dev.town.small.brewtopia.DataClass.BrewSchema;
import com.dev.town.small.brewtopia.DataClass.UserSchema;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 8/27/2016.
 */
public class JSONUserParser {

    private static final String LOG = "JSONParser";

    public JSONUserParser() {}

    /**
     * Method to make json array request where response starts with [
     * */
    public UserSchema ParseUser(JSONArray jsonArray) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseUser");

        List<UserSchema> userSchemas = new ArrayList<>();

        try {
            // Parsing json array response
            // loop through each json object
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject user = (JSONObject) jsonArray.get(i);
                userSchemas.add(ParseUser(user));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        if(userSchemas.size() > 1 || userSchemas.size() == 0)
            return new UserSchema();

        //retrun first user in list
        return userSchemas.get(0);
    }

    /**
     * Method to Parse one user
     * */
    private UserSchema ParseUser (JSONObject aUser) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseUser");
        UserSchema userSchema = new UserSchema();

        try {
            userSchema.setUserId(Long.parseLong(aUser.getString("UserId")));
            userSchema.setUserName(aUser.getString("UserName"));
            userSchema.setPassword(aUser.getString("Password"));
            userSchema.setRole(Integer.parseInt(aUser.getString("Role")));
            userSchema.setCreatedOn(aUser.getString("CreatedOn"));

            JSONArray appSettings = aUser.getJSONArray("AppSettings");
            userSchema.setAppSettingsSchemas(ParseAppSettings(appSettings));

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return userSchema;
    }

    /**
     * Method to Parse All App Settings
     * */
    private List<AppSettingsSchema> ParseAppSettings (JSONArray aAppSetting) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseAppSettings");
        List<AppSettingsSchema> appSettingsSchemas = new ArrayList<>();

        try {
            for (int i = 0; i < aAppSetting.length(); i++) {

                JSONObject addition = (JSONObject) aAppSetting.get(i);
                appSettingsSchemas.add(ParseAppSetting(addition));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return appSettingsSchemas;
    }

    /**
     * Method to Parse one App Setting
     * */
    private AppSettingsSchema ParseAppSetting (JSONObject aAppSetting) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseAppSetting");
        AppSettingsSchema appSettingsSchemas = new AppSettingsSchema();

        try {

            appSettingsSchemas.setAppSetttingId(Long.parseLong(aAppSetting.getString("SettingsId")));
            appSettingsSchemas.setUserId(Long.parseLong(aAppSetting.getString("UserId")));
            appSettingsSchemas.setSettingName(aAppSetting.getString("SettingsName"));
            appSettingsSchemas.setSettingValue(aAppSetting.getString("SettingsValue"));
            appSettingsSchemas.setSettingScreen(aAppSetting.getString("SettingsScreen"));

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return appSettingsSchemas;
    }
}
