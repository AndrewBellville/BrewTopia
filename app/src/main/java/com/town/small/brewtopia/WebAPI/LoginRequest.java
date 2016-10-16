package com.town.small.brewtopia.WebAPI;

/**
 * Created by Andrew on 7/22/2016.
 */

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static String URL = "http://smalltowndev.com/index.php/mobileAPI/login";
    private Map<String, String>params;

    public LoginRequest(String aUserName, String aPassword, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("UserName",aUserName);
        params.put("Password",aPassword);
    }

    @Override
    public Map<String,String> getParams(){
        return params;
    }
}
