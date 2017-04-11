package com.dev.town.small.brewtopia.WebAPI;

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
public class UpdateUserRequest extends StringRequest {
    private static String URL = APPUTILS.WEBAPIRURL+"/updateUser";
    private Map<String, String> params;

    public UpdateUserRequest(UserSchema aUserSchema, String aCheckPassword,String aNewUserName,String aNewPassword, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("UserId",Long.toString(aUserSchema.getUserId()));
        params.put("UserName",aUserSchema.getUserName());
        params.put("CheckPassword", aCheckPassword);
        params.put("NewUserName", aNewUserName);
        params.put("NewUserPassword", aNewPassword);

        if(APPUTILS.isLogging) System.out.println(params.toString());
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}