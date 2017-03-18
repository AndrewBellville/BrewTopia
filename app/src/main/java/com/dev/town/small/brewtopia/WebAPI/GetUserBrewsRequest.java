package com.dev.town.small.brewtopia.WebAPI;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 3/18/2017.
 */
public class GetUserBrewsRequest extends StringRequest {
    private static String URL = APPUTILS.WEBAPIRURL+"/getUsersBrew";
    private Map<String, String> params;

    public GetUserBrewsRequest(String aUserId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("UserId", aUserId);
    }

    @Override
    public Map<String,String> getParams(){
        return params;
    }
}