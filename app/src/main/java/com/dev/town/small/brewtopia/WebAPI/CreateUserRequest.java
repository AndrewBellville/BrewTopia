package com.dev.town.small.brewtopia.WebAPI;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.AppSettingsSchema;
import com.dev.town.small.brewtopia.DataClass.UserSchema;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 7/23/2016.
 */
public class CreateUserRequest extends StringRequest {
    private static String URL = APPUTILS.WEBAPIRURL+"/createUser";
    private Map<String, String> params;

    public CreateUserRequest(UserSchema aUserSchema, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("UserName", aUserSchema.getUserName());
        params.put("Password", aUserSchema.getPassword());
        params.put("CreatedOn", APPUTILS.dateFormat.format(new Date()));

        //*******************App Settings***********************************
        ArrayList<String> appSettings = new ArrayList<>();
        for (AppSettingsSchema as: aUserSchema.getAppSettingsSchemas()) {
            HashMap<String,String> tempMap  = new HashMap<>();
            tempMap.put("SettingsId",Long.toString(as.getAppSetttingId()));
            tempMap.put("SettingsName",as.getSettingName());
            tempMap.put("SettingsValue",as.getSettingValue());
            tempMap.put("SettingsScreen",as.getSettingScreen());
            JSONObject jsonObject = new JSONObject(tempMap);
            appSettings.add( jsonObject.toString());

        }
        params.put("AppSettings", appSettings.toString());
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}