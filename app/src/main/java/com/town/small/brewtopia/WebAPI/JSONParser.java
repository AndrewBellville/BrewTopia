package com.town.small.brewtopia.WebAPI;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.town.small.brewtopia.DataBase.DataBaseManager;
import com.town.small.brewtopia.DataClass.BrewSchema;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 8/27/2016.
 */
public class JSONParser {

    private static final String LOG = "JSONParser";
    DataBaseManager dataBaseManager;

    public JSONParser(Context aContext)
    {
        dataBaseManager = DataBaseManager.getInstance(aContext);
    }

    /**
     * Method to make json array request where response starts with [
     * */
    public List<BrewSchema> ParseGlobalBrews(JSONArray jsonArray) {

        Log.d(LOG, "Entering: ParseGlobalBrews");

        List<BrewSchema> brewSchemaList = new ArrayList<>();

        try {
            // Parsing json array response
            // loop through each json object
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject brew = (JSONObject) jsonArray.get(i);
                BrewSchema brewSchema = new BrewSchema();

                brewSchema.setBrewName(brew.getString("BrewName"));
                brewSchema.setBoilTime(Integer.parseInt(brew.getString("BoilTime")));
                brewSchema.setPrimary(Integer.parseInt(brew.getString("Primary")));
                brewSchema.setSecondary(Integer.parseInt(brew.getString("Secondary")));
                brewSchema.setBottle(Integer.parseInt(brew.getString("Bottling")));
                brewSchema.setDescription(brew.getString("Description"));
                brewSchema.setStyle(brew.getString("Style"));
                brewSchema.setTargetOG(Double.parseDouble(brew.getString("OriginalGravity")));
                brewSchema.setTargetFG(Double.parseDouble(brew.getString("FinalGravity")));
                brewSchema.setTargetABV(Double.parseDouble(brew.getString("ABV")));
                brewSchema.setIBU(Double.parseDouble(brew.getString("IBU")));
                brewSchema.setMethod(brew.getString("Method"));
                brewSchema.setBatchSize(Double.parseDouble(brew.getString("BatchSize")));
                brewSchema.setEfficiency(Double.parseDouble(brew.getString("Efficiency")));
                brewSchema.setCreatedOn(brew.getString("CreatedOn"));

                brewSchema.setStyleSchema(dataBaseManager.getBrewsStylesByName(brewSchema.getStyle()));

                brewSchemaList.add(brewSchema);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return brewSchemaList;
    }
}
