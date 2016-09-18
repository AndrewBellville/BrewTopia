package com.town.small.brewtopia.WebAPI;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.town.small.brewtopia.DataBase.DataBaseManager;
import com.town.small.brewtopia.DataClass.BoilAdditionsSchema;
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
                brewSchemaList.add(ParseGlobalBrew(brew));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return brewSchemaList;
    }

    /**
     * Method to Parse one brew
     * */
    private BrewSchema ParseGlobalBrew (JSONObject aBrew) {

        Log.d(LOG, "Entering: ParseGlobalBrew");
        BrewSchema brewSchema = new BrewSchema();

        try {
            brewSchema.setGlobalBrewId(Long.parseLong(aBrew.getString("GlobalBrewId")));
            brewSchema.setBrewName(aBrew.getString("BrewName"));
            brewSchema.setUserId(Long.parseLong(aBrew.getString("UserId")));
            brewSchema.setBoilTime(Integer.parseInt(aBrew.getString("BoilTime")));
            brewSchema.setPrimary(Integer.parseInt(aBrew.getString("Primary")));
            brewSchema.setSecondary(Integer.parseInt(aBrew.getString("Secondary")));
            brewSchema.setBottle(Integer.parseInt(aBrew.getString("Bottling")));
            brewSchema.setDescription(aBrew.getString("Description"));
            brewSchema.setStyle(aBrew.getString("Style"));
            brewSchema.setTargetOG(Double.parseDouble(aBrew.getString("OriginalGravity")));
            brewSchema.setTargetFG(Double.parseDouble(aBrew.getString("FinalGravity")));
            brewSchema.setTargetABV(Double.parseDouble(aBrew.getString("ABV")));
            brewSchema.setIBU(Double.parseDouble(aBrew.getString("IBU")));
            brewSchema.setMethod(aBrew.getString("Method"));
            brewSchema.setBatchSize(Double.parseDouble(aBrew.getString("BatchSize")));
            brewSchema.setEfficiency(Double.parseDouble(aBrew.getString("Efficiency")));
            brewSchema.setCreatedOn(aBrew.getString("CreatedOn"));

            //Build Boil Additions
            JSONArray boilAdditions = aBrew.getJSONArray("BoilAdditions");
            brewSchema.setBoilAdditionlist(ParseGlobalBrewAdditions(boilAdditions));


            brewSchema.setStyleSchema(dataBaseManager.getBrewsStylesByName(brewSchema.getStyle()));

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return brewSchema;
    }

    /**
     * Method to Parse All Additions
     * */
    private List<BoilAdditionsSchema> ParseGlobalBrewAdditions (JSONArray aAdditions) {

        Log.d(LOG, "Entering: ParseGlobalBrewAdditions");
        List<BoilAdditionsSchema> boilAdditionsSchemas = new ArrayList<>();

        try {
            for (int i = 0; i < aAdditions.length(); i++) {

                JSONObject addition = (JSONObject) aAdditions.get(i);
                boilAdditionsSchemas.add(ParseGlobalBrewAddition(addition));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return boilAdditionsSchemas;
    }

    /**
     * Method to Parse one Addition
     * */
    private BoilAdditionsSchema ParseGlobalBrewAddition (JSONObject aBrew) {

        Log.d(LOG, "Entering: ParseGlobalBrew");
        BoilAdditionsSchema boilAdditionsSchema = new BoilAdditionsSchema();

        try {
            boilAdditionsSchema.setGlobalAdditionId(Long.parseLong(aBrew.getString("GlobalAdditionId")));
            boilAdditionsSchema.setAdditionId(Long.parseLong(aBrew.getString("AdditionId")));
            boilAdditionsSchema.setUserId(Long.parseLong(aBrew.getString("UserId")));
            boilAdditionsSchema.setBrewId(Long.parseLong(aBrew.getString("BrewId")));
            boilAdditionsSchema.setAdditionName(aBrew.getString("AdditionName"));
            boilAdditionsSchema.setAdditionTime(Integer.parseInt(aBrew.getString("AdditionTime")));
            boilAdditionsSchema.setAdditionQty(Double.parseDouble(aBrew.getString("AdditionQty")));
            boilAdditionsSchema.setUOfM(aBrew.getString("AdditionUofM"));

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return boilAdditionsSchema;
    }
}
