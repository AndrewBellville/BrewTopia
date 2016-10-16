package com.town.small.brewtopia.WebAPI;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.town.small.brewtopia.DataClass.APPUTILS;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 7/23/2016.
 */
public class CreateUserRequest extends StringRequest {
    private static String URL = "http://smalltowndev.com/index.php/mobileAPI/createUser";
    private Map<String, String> params;

    public CreateUserRequest(String aUserName, String aPassword, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("UserName", aUserName);
        params.put("Password", aPassword);
        params.put("CreatedOn", APPUTILS.dateFormat.format(new Date()));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}