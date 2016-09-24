package com.town.small.brewtopia.WebAPI;

/**
 * Created by Andrew on 7/22/2016.
 */

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteUserRequest extends StringRequest {
    private static String URL = "http://blackatombrewery.com/index.php/mobileAPI/deleteUser";
    private Map<String, String>params;

    public DeleteUserRequest(String aGlobalUserId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("UserId",aGlobalUserId);
    }

    @Override
    public Map<String,String> getParams(){
        return params;
    }
}
