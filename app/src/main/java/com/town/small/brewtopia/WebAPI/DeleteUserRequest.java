package com.town.small.brewtopia.WebAPI;

/**
 * Created by Andrew on 7/22/2016.
 */

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.town.small.brewtopia.DataClass.APPUTILS;

import java.util.HashMap;
import java.util.Map;

public class DeleteUserRequest extends StringRequest {
    private static String URL = APPUTILS.WEBAPIRURL+"/deleteUser";
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
