package com.dev.town.small.brewtopia.WebAPI;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 4/29/2017.
 */
public class GetInventoryRequest extends StringRequest {
    private static String URL = APPUTILS.WEBAPIRURL+"/getInventory";
    private Map<String, String> params;

    public GetInventoryRequest(Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        //params.put("BrewId", aBrewId);
    }

    @Override
    public Map<String,String> getParams(){
        return params;
    }
}
