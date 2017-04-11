package com.dev.town.small.brewtopia.WebAPI;

import android.util.Log;

import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.AppSettingsSchema;
import com.dev.town.small.brewtopia.DataClass.BrewStyleSchema;
import com.dev.town.small.brewtopia.DataClass.UserSchema;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 8/27/2016.
 */
public class JSONStyleParser {

    private static final String LOG = "JSONParser";

    public JSONStyleParser() {}

    /**
     * Method to make json array request where response starts with [
     * */
    public List<BrewStyleSchema> ParseStyles(JSONArray jsonArray) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseStyle");

        List<BrewStyleSchema> styleSchemas = new ArrayList<>();

        try {
            // Parsing json array response
            // loop through each json object
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject style = (JSONObject) jsonArray.get(i);
                styleSchemas.add(ParseStyle(style));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        //return style list
        return styleSchemas;
    }

    /**
     * Method to Parse one style
     * */
    private BrewStyleSchema ParseStyle (JSONObject aStyle) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseStyle");
        BrewStyleSchema styleSchema = new BrewStyleSchema();

        try {
            styleSchema.setStyleId(Long.parseLong(aStyle.getString("StyleId")));
            styleSchema.setUserId(Long.parseLong(aStyle.getString("UserId")));
            styleSchema.setBrewStyleName(aStyle.getString("Name"));
            styleSchema.setBrewStyleColor(aStyle.getString("DisplayColor"));
            styleSchema.setType(aStyle.getString("Type"));

            styleSchema.setMinOG(Double.parseDouble(aStyle.getString("MinOG")));
            styleSchema.setMaxOG(Double.parseDouble(aStyle.getString("MaxOG")));

            styleSchema.setMinFG(Double.parseDouble(aStyle.getString("MinFG")));
            styleSchema.setMaxFG(Double.parseDouble(aStyle.getString("MaxFG")));

            styleSchema.setMinIBU(Double.parseDouble(aStyle.getString("MinIBU")));
            styleSchema.setMaxIBU(Double.parseDouble(aStyle.getString("MaxIBU")));

            styleSchema.setMinSRM(Double.parseDouble(aStyle.getString("MinColor")));
            styleSchema.setMaxSRM(Double.parseDouble(aStyle.getString("MaxColor")));

            styleSchema.setMinABV(Double.parseDouble(aStyle.getString("MinABV")));
            styleSchema.setMaxABV(Double.parseDouble(aStyle.getString("MaxABV")));

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return styleSchema;
    }
}
