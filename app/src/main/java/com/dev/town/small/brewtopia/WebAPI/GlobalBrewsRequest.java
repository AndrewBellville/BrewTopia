package com.dev.town.small.brewtopia.WebAPI;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;

import org.json.JSONArray;


/**
 * Created by Andrew on 8/27/2016.
 *
 * Class used to call for all Brews saved to global DB
 */
public class GlobalBrewsRequest extends StringRequest {

    // Log cat tag
    private static final String LOG = "GlobalBrewsRequest";
    private static String URL = APPUTILS.WEBAPIRURL+"/globalBrews";

    public GlobalBrewsRequest(Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(URL,listener,errorListener);
    }

}
