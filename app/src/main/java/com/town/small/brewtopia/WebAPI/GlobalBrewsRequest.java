package com.town.small.brewtopia.WebAPI;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;


/**
 * Created by Andrew on 8/27/2016.
 *
 * Class used to call for all Brews saved to global DB
 */
public class GlobalBrewsRequest extends JsonArrayRequest {

    // Log cat tag
    private static final String LOG = "GlobalBrewsRequest";
    private static String URL = "http://smalltowndev.com/index.php/mobileAPI/globalBrews";

    public GlobalBrewsRequest(Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(URL,listener,errorListener);
    }

}
