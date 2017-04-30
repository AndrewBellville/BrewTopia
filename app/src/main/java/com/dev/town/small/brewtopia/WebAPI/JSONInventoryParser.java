package com.dev.town.small.brewtopia.WebAPI;

import android.util.Log;

import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.BrewStyleSchema;
import com.dev.town.small.brewtopia.DataClass.EquipmentSchema;
import com.dev.town.small.brewtopia.DataClass.FermentablesSchema;
import com.dev.town.small.brewtopia.DataClass.HopsSchema;
import com.dev.town.small.brewtopia.DataClass.InventorySchema;
import com.dev.town.small.brewtopia.DataClass.OtherSchema;
import com.dev.town.small.brewtopia.DataClass.YeastSchema;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 8/27/2016.
 */
public class JSONInventoryParser {

    private static final String LOG = "JSONParser";

    /**
     * Method to make json array request where response starts with [
     * */
    public List<InventorySchema> ParseInventory(JSONArray jsonArray) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseInventory");

        List<InventorySchema> inventorySchemas = new ArrayList<>();

        try {
            // Parsing json array response
            // loop through each json object
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject inventory = (JSONObject) jsonArray.get(i);
                inventorySchemas.addAll(ParseAllInventory(inventory));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        //return inventory list
        return inventorySchemas;
    }

    /**
     * Method to Parse All Inventory
     * */
    public List<InventorySchema> ParseAllInventory (JSONObject aInventoryGroup) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseAllInventory");
        List<InventorySchema> inventorySchemas = new ArrayList<>();

        try {
            //Build Hops
            JSONArray InventoryHops = aInventoryGroup.getJSONArray("InventoryHops");
            inventorySchemas.addAll(ParseHops(InventoryHops));

            //Build Fermentables
            JSONArray InventoryFermentables = aInventoryGroup.getJSONArray("InventoryFermentables");
            inventorySchemas.addAll(ParseFermentables(InventoryFermentables));

            //Build Yeast
            JSONArray InventoryYeast = aInventoryGroup.getJSONArray("InventoryYeast");
            inventorySchemas.addAll(ParseYeasts(InventoryYeast));

            //Build Equipment
            JSONArray InventoryEquipment = aInventoryGroup.getJSONArray("InventoryEquipment");
            inventorySchemas.addAll(ParseEquipments(InventoryEquipment));

            //Build Other
            JSONArray InventoryOther = aInventoryGroup.getJSONArray("InventoryOther");
            inventorySchemas.addAll(ParseOthers(InventoryOther));

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return inventorySchemas;
    }

    /**
     * Method to Parse All Hops
     * */
    private List<InventorySchema> ParseHops (JSONArray aInventory) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrewHops");
        List<InventorySchema> inventorySchemas = new ArrayList<>();

        try {
            for (int i = 0; i < aInventory.length(); i++) {

                JSONObject inventory = (JSONObject) aInventory.get(i);
                inventorySchemas.add(ParseHop(inventory));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return inventorySchemas;
    }

    /**
     * Method to Parse one Hop
     * */
    private InventorySchema ParseHop (JSONObject aInventory) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseHop");
        HopsSchema inventorySchema = new HopsSchema();

        try {

            inventorySchema.setInventoryId(Long.parseLong(aInventory.getString("HopsId")));
            inventorySchema.setBrewId(Long.parseLong(aInventory.getString("BrewId")));
            inventorySchema.setUserId(Long.parseLong(aInventory.getString("UserId")));
            inventorySchema.setUserId(Long.parseLong(aInventory.getString("ScheduleId")));

            inventorySchema.setInvetoryQty(Integer.parseInt(aInventory.getString("InventoryQty")));
            inventorySchema.setInventoryName(aInventory.getString("InventoryName"));
            inventorySchema.setAmount(Double.parseDouble(aInventory.getString("InventoryAmount")));
            inventorySchema.setType(aInventory.getString("InventoryType"));
            inventorySchema.setAA(Double.parseDouble(aInventory.getString("AA")));
            inventorySchema.setUse(aInventory.getString("InventoryUse"));
            inventorySchema.setTime(Integer.parseInt(aInventory.getString("InventoryTime")));
            inventorySchema.setInventoryUOfM(aInventory.getString("InventoryUofM"));

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return inventorySchema;
    }

    /**
     * Method to Parse All Fermentables
     * */
    private List<InventorySchema> ParseFermentables (JSONArray aInventory) {

        if(APPUTILS.isLogging) Log.d(LOG, "Entering: ParseFermentables");
        List<InventorySchema> inventorySchemas = new ArrayList<>();

        try {
            for (int i = 0; i < aInventory.length(); i++) {

                JSONObject inventory = (JSONObject) aInventory.get(i);
                inventorySchemas.add(ParseFermentable(inventory));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return inventorySchemas;
    }

    /**
     * Method to Parse one Fermentable
     * */
    private InventorySchema ParseFermentable (JSONObject aInventory) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseFermentable");
        FermentablesSchema inventorySchema = new FermentablesSchema();

        try {

            inventorySchema.setInventoryId(Long.parseLong(aInventory.getString("FermentablesId")));
            inventorySchema.setBrewId(Long.parseLong(aInventory.getString("BrewId")));
            inventorySchema.setUserId(Long.parseLong(aInventory.getString("UserId")));
            inventorySchema.setUserId(Long.parseLong(aInventory.getString("ScheduleId")));

            inventorySchema.setInvetoryQty(Integer.parseInt(aInventory.getString("InventoryQty")));
            inventorySchema.setInventoryName(aInventory.getString("InventoryName"));
            inventorySchema.setAmount(Double.parseDouble(aInventory.getString("InventoryAmount")));
            inventorySchema.setPoundPerGallon(Double.parseDouble(aInventory.getString("PPG")));
            inventorySchema.setLovibond(Double.parseDouble(aInventory.getString("Lovibond")));
            inventorySchema.setInventoryUOfM(aInventory.getString("InventoryUofM"));
            inventorySchema.setType(aInventory.getString("InventoryType"));
            inventorySchema.setYield(Double.parseDouble(aInventory.getString("Yield")));
            inventorySchema.setPotential(Double.parseDouble(aInventory.getString("Potential")));

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return inventorySchema;
    }

    /**
     * Method to Parse All Yeasts
     * */
    private List<InventorySchema> ParseYeasts (JSONArray aInventory) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseYeasts");
        List<InventorySchema> inventorySchemas = new ArrayList<>();

        try {
            for (int i = 0; i < aInventory.length(); i++) {

                JSONObject inventory = (JSONObject) aInventory.get(i);
                inventorySchemas.add(ParseYeast(inventory));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return inventorySchemas;
    }

    /**
     * Method to Parse one Yeast
     * */
    private InventorySchema ParseYeast (JSONObject aInventory) {

        if(APPUTILS.isLogging) Log.d(LOG, "Entering: ParseYeast");
        YeastSchema inventorySchema = new YeastSchema();

        try {
            inventorySchema.setInventoryId(Long.parseLong(aInventory.getString("YeastId")));
            inventorySchema.setBrewId(Long.parseLong(aInventory.getString("BrewId")));
            inventorySchema.setUserId(Long.parseLong(aInventory.getString("UserId")));
            inventorySchema.setUserId(Long.parseLong(aInventory.getString("ScheduleId")));

            inventorySchema.setInvetoryQty(Integer.parseInt(aInventory.getString("InventoryQty")));
            inventorySchema.setInventoryName(aInventory.getString("InventoryName"));
            inventorySchema.setAmount(Double.parseDouble(aInventory.getString("InventoryAmount")));
            inventorySchema.setFlocculation(aInventory.getString("Flocculation"));
            inventorySchema.setStarter(Integer.parseInt(aInventory.getString("Starter")));
            inventorySchema.setAttenuation(Double.parseDouble(aInventory.getString("Attenuation")));
            inventorySchema.setOptimumTempLow(Double.parseDouble(aInventory.getString("OTL")));
            inventorySchema.setOptimumTempHigh(Double.parseDouble(aInventory.getString("OTH")));
            inventorySchema.setInventoryUOfM(aInventory.getString("InventoryUofM"));
            inventorySchema.setLaboratory(aInventory.getString("Laboratory"));
            inventorySchema.setType(aInventory.getString("InventoryType"));
            inventorySchema.setForm(aInventory.getString("Form"));

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return inventorySchema;
    }

    /**
     * Method to Parse All Equipments
     * */
    private List<InventorySchema> ParseEquipments (JSONArray aInventory) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseEquipments");
        List<InventorySchema> inventorySchemas = new ArrayList<>();

        try {
            for (int i = 0; i < aInventory.length(); i++) {

                JSONObject inventory = (JSONObject) aInventory.get(i);
                inventorySchemas.add(ParseEquipment(inventory));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return inventorySchemas;
    }

    /**
     * Method to Parse one Equipment
     * */
    private InventorySchema ParseEquipment (JSONObject aInventory) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseEquipment");
        EquipmentSchema inventorySchema = new EquipmentSchema();

        try {

            inventorySchema.setInventoryId(Long.parseLong(aInventory.getString("EquipmentId")));
            inventorySchema.setBrewId(Long.parseLong(aInventory.getString("BrewId")));
            inventorySchema.setUserId(Long.parseLong(aInventory.getString("UserId")));
            inventorySchema.setUserId(Long.parseLong(aInventory.getString("ScheduleId")));

            inventorySchema.setInvetoryQty(Integer.parseInt(aInventory.getString("InventoryQty")));
            inventorySchema.setInventoryName(aInventory.getString("InventoryName"));
            inventorySchema.setAmount(Double.parseDouble(aInventory.getString("InventoryAmount")));
            inventorySchema.setInventoryUOfM(aInventory.getString("InventoryUofM"));

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return inventorySchema;
    }

    /**
     * Method to Parse All Others
     * */
    private List<InventorySchema> ParseOthers (JSONArray aInventory) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseOthers");
        List<InventorySchema> inventorySchemas = new ArrayList<>();

        try {
            for (int i = 0; i < aInventory.length(); i++) {

                JSONObject inventory = (JSONObject) aInventory.get(i);
                inventorySchemas.add(ParseOther(inventory));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return inventorySchemas;
    }

    /**
     * Method to Parse Other
     * */
    private InventorySchema ParseOther (JSONObject aInventory) {

        if(APPUTILS.isLogging) Log.d(LOG, "Entering: ParseOther");
        OtherSchema inventorySchema = new OtherSchema();

        try {

            inventorySchema.setInventoryId(Long.parseLong(aInventory.getString("OtherInventoryId")));
            inventorySchema.setBrewId(Long.parseLong(aInventory.getString("BrewId")));
            inventorySchema.setUserId(Long.parseLong(aInventory.getString("UserId")));
            inventorySchema.setUserId(Long.parseLong(aInventory.getString("ScheduleId")));

            inventorySchema.setInvetoryQty(Integer.parseInt(aInventory.getString("InventoryQty")));
            inventorySchema.setInventoryName(aInventory.getString("InventoryName"));
            inventorySchema.setAmount(Double.parseDouble(aInventory.getString("InventoryAmount")));
            inventorySchema.setInventoryUOfM(aInventory.getString("InventoryUofM"));
            inventorySchema.setUse(aInventory.getString("InventoryUse"));

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return inventorySchema;
    }
}
