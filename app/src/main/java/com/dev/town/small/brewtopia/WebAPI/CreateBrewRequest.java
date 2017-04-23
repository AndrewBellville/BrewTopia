package com.dev.town.small.brewtopia.WebAPI;

import android.util.Base64;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.BoilAdditionsSchema;
import com.dev.town.small.brewtopia.DataClass.BrewImageSchema;
import com.dev.town.small.brewtopia.DataClass.BrewNoteSchema;
import com.dev.town.small.brewtopia.DataClass.BrewSchema;
import com.dev.town.small.brewtopia.DataClass.EquipmentSchema;
import com.dev.town.small.brewtopia.DataClass.FermentablesSchema;
import com.dev.town.small.brewtopia.DataClass.HopsSchema;
import com.dev.town.small.brewtopia.DataClass.InventorySchema;
import com.dev.town.small.brewtopia.DataClass.OtherSchema;
import com.dev.town.small.brewtopia.DataClass.ScheduledBrewSchema;
import com.dev.town.small.brewtopia.DataClass.ScheduledEventSchema;
import com.dev.town.small.brewtopia.DataClass.YeastSchema;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 7/23/2016.
 */
public class CreateBrewRequest extends StringRequest {
    private static String URL = APPUTILS.WEBAPIRURL+"/createBrew";
    private Map<String, String> params;

    public CreateBrewRequest(BrewSchema aBrewSchema, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, URL, listener, errorListener);

        JSONBrewBuilder JSONBrewBuilder = new JSONBrewBuilder();
        params = JSONBrewBuilder.BuildBrew(aBrewSchema);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

