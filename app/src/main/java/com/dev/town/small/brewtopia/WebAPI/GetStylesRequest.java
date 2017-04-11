package com.dev.town.small.brewtopia.WebAPI;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 7/22/2016.
 */
public class GetStylesRequest extends StringRequest {
    private static String URL = APPUTILS.WEBAPIRURL+"/getStyles";
    private Map<String, String>params;

    public GetStylesRequest(Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        //params.put("BrewId", aBrewId);
    }

    @Override
    public Map<String,String> getParams(){
        return params;
    }
}
