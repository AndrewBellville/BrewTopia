package com.dev.town.small.brewtopia.WebAPI;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 8/27/2016.
 */
public class JSONBrewParser {

    private static final String LOG = "JSONParser";
    DataBaseManager dataBaseManager;

    public JSONBrewParser(Context aContext)
    {
        dataBaseManager = DataBaseManager.getInstance(aContext);
    }

    /**
     * Method to make json array request where response starts with [
     * */
    public List<BrewSchema> ParseGlobalBrews(JSONArray jsonArray) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrews");

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

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrew");
        BrewSchema brewSchema = new BrewSchema();

        try {

            brewSchema.setBrewId(Long.parseLong(aBrew.getString("BrewId")));
            brewSchema.setUserId(Long.parseLong(aBrew.getString("UserId")));

            brewSchema.setBrewName(aBrew.getString("BrewName"));
            brewSchema.setBoilTime(Integer.parseInt(aBrew.getString("BoilTime")));
            brewSchema.setPrimary(Integer.parseInt(aBrew.getString("Primary")));
            brewSchema.setSecondary(Integer.parseInt(aBrew.getString("Secondary")));
            brewSchema.setBottle(Integer.parseInt(aBrew.getString("Bottling")));
            brewSchema.setTargetOG(Double.parseDouble(aBrew.getString("OriginalGravity")));
            brewSchema.setTargetFG(Double.parseDouble(aBrew.getString("FinalGravity")));
            brewSchema.setTargetABV(Double.parseDouble(aBrew.getString("ABV")));
            brewSchema.setDescription(aBrew.getString("Description"));
            brewSchema.setIBU(Double.parseDouble(aBrew.getString("IBU")));
            brewSchema.setBatchSize(Double.parseDouble(aBrew.getString("BatchSize")));
            brewSchema.setEfficiency(Double.parseDouble(aBrew.getString("Efficiency")));
            brewSchema.setMethod(aBrew.getString("Method"));
            brewSchema.setStyleType(aBrew.getString("Style"));
            brewSchema.setStyleId(Long.parseLong(aBrew.getString("StyleId")));
            brewSchema.setSRM(Integer.parseInt(aBrew.getString("SRM")));

            // if global display we want to field to be blak so ignore if missing
            try {

                brewSchema.setFavorite(Integer.parseInt(aBrew.getString("Favorite")));
                brewSchema.setScheduled(Integer.parseInt(aBrew.getString("Scheduled")));
                brewSchema.setOnTap(Integer.parseInt(aBrew.getString("OnTap")));
                brewSchema.setIsGlobal(Integer.parseInt(aBrew.getString("IsGlobal")));
                brewSchema.setCreatedOn(aBrew.getString("CreatedOn"));
            }catch (JSONException ex){}


            //Build Boil Additions
            JSONArray boilAdditions = aBrew.getJSONArray("BoilAdditions");
            brewSchema.setBoilAdditionlist(ParseGlobalBrewAdditions(boilAdditions));

            //Build Brew Notes
            JSONArray brewNotes = aBrew.getJSONArray("BrewNotes");
            brewSchema.setBrewNoteSchemaList(ParseGlobalBrewNotes(brewNotes));

            //Build Brew Notes
            JSONArray brewImages = aBrew.getJSONArray("BrewImages");
            brewSchema.setBrewImageSchemaList(ParseGlobalBrewImages(brewImages));

            //Build Brews Scheduled
            JSONArray brewsScheduled = aBrew.getJSONArray("BrewsScheduled");
            brewSchema.setScheduledBrewSchemas(ParseGlobalBrewsScheduled(brewsScheduled));

            //Build Brew events
            JSONArray brewEvents = aBrew.getJSONArray("ScheduleEvents");
            brewSchema.setScheduledEventSchemas(ParseGlobalBrewEvents(brewEvents));

            //Build Brew InventoryHops
            JSONArray InventoryHops = aBrew.getJSONArray("InventoryHops");
            brewSchema.setBrewInventorySchemaList(ParseGlobalBrewHops(InventoryHops));

            //Build Brew InventoryFermentables
            JSONArray InventoryFermentables = aBrew.getJSONArray("InventoryFermentables");
            brewSchema.getBrewInventorySchemaList().addAll(ParseGlobalBrewFermentables(InventoryFermentables));

            //Build Brew InventoryYeast
            JSONArray InventoryYeast = aBrew.getJSONArray("InventoryYeast");
            brewSchema.getBrewInventorySchemaList().addAll(ParseGlobalBrewYeasts(InventoryYeast));

            //Build Brew InventoryEquipment
            JSONArray InventoryEquipment = aBrew.getJSONArray("InventoryEquipment");
            brewSchema.getBrewInventorySchemaList().addAll(ParseGlobalBrewEquipments(InventoryEquipment));

            //Build Brew InventoryOther
            JSONArray InventoryOther = aBrew.getJSONArray("InventoryOther");
            brewSchema.getBrewInventorySchemaList().addAll(ParseGlobalBrewOthers(InventoryOther));

            brewSchema.setStyleSchema(dataBaseManager.getBrewsStylesById(brewSchema.getStyleId()));

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

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrewAdditions");
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

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrew");
        BoilAdditionsSchema boilAdditionsSchema = new BoilAdditionsSchema();

        try {

            boilAdditionsSchema.setAdditionId(Long.parseLong(aBrew.getString("AdditionId")));
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


    /**
     * Method to Parse All BrewNotes
     * */
    private List<BrewNoteSchema> ParseGlobalBrewNotes (JSONArray aNotes) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrewNotes");
        List<BrewNoteSchema> brewNoteSchemaList = new ArrayList<>();

        try {
            for (int i = 0; i < aNotes.length(); i++) {

                JSONObject note = (JSONObject) aNotes.get(i);
                brewNoteSchemaList.add(ParseGlobalBrewNote(note));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return brewNoteSchemaList;
    }

    /**
     * Method to Parse one Brew Note
     * */
    private BrewNoteSchema ParseGlobalBrewNote (JSONObject aBrewNote) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrewNote");
        BrewNoteSchema brewNoteSchema = new BrewNoteSchema();

        try {

            brewNoteSchema.setNoteId(Long.parseLong(aBrewNote.getString("BrewNoteId")));
            brewNoteSchema.setBrewId(Long.parseLong(aBrewNote.getString("BrewId")));


            brewNoteSchema.setBrewNote(aBrewNote.getString("Note"));
            brewNoteSchema.setCreatedOn(aBrewNote.getString("CreatedOn"));

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return brewNoteSchema;
    }

    /**
     * Method to Parse All Brew Images
     * */
    private List<BrewImageSchema> ParseGlobalBrewImages (JSONArray aImage) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrewImages");
        List<BrewImageSchema> brewImageSchemaList = new ArrayList<>();

        try {
            for (int i = 0; i < aImage.length(); i++) {

                JSONObject image = (JSONObject) aImage.get(i);
                brewImageSchemaList.add(ParseGlobalBrewImage(image));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return brewImageSchemaList;
    }

    /**
     * Method to Parse one Brew Image
     * */
    private BrewImageSchema ParseGlobalBrewImage (JSONObject aBrewImage) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrewImage");
        BrewImageSchema brewImageSchema = new BrewImageSchema();

        try {
            brewImageSchema.setImageId(Long.parseLong(aBrewImage.getString("ImageId")));
            brewImageSchema.setBrewId(Long.parseLong(aBrewImage.getString("BrewId")));


            brewImageSchema.setImage(APPUTILS.GetBitmapFromByteArr(Base64.decode(aBrewImage.getString("Image"), Base64.DEFAULT)));
            brewImageSchema.setCreatedOn(aBrewImage.getString("CreatedOn"));

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return brewImageSchema;
    }

    /**
     * Method to Parse All Brews Scheduled
     * */
    private List<ScheduledBrewSchema> ParseGlobalBrewsScheduled (JSONArray aSchedule) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrewsScheduled");
        List<ScheduledBrewSchema> scheduledBrewSchemas = new ArrayList<>();

        try {
            for (int i = 0; i < aSchedule.length(); i++) {

                JSONObject scheudle = (JSONObject) aSchedule.get(i);
                scheduledBrewSchemas.add(ParseGlobalBrewsSchedule(scheudle));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return scheduledBrewSchemas;
    }

    /**
     * Method to Parse one Brew schedule
     * */
    private ScheduledBrewSchema ParseGlobalBrewsSchedule (JSONObject aSchedule) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrewsSchedule");
        ScheduledBrewSchema scheduledBrewSchema = new ScheduledBrewSchema();

        try {

            scheduledBrewSchema.setScheduleId(Long.parseLong(aSchedule.getString("ScheduleId")));
            scheduledBrewSchema.setBrewId(Long.parseLong(aSchedule.getString("BrewId")));
            scheduledBrewSchema.setUserId(Long.parseLong(aSchedule.getString("UserId")));

            scheduledBrewSchema.setBrewName(aSchedule.getString("BrewName"));
            scheduledBrewSchema.setNotes(aSchedule.getString("Note"));
            scheduledBrewSchema.setStyleType(aSchedule.getString("StyleType"));
            scheduledBrewSchema.setOG(Double.parseDouble(aSchedule.getString("OriginalGravity")));
            scheduledBrewSchema.setFG(Double.parseDouble(aSchedule.getString("FinalGravity")));
            scheduledBrewSchema.setABV(Double.parseDouble(aSchedule.getString("ABV")));
            scheduledBrewSchema.setActive(Integer.parseInt(aSchedule.getString("Active")));
            scheduledBrewSchema.setStartDate(aSchedule.getString("CreatedOn"));
            scheduledBrewSchema.setHasStarter(Integer.parseInt(aSchedule.getString("UseStarter")));

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return scheduledBrewSchema;
    }

    /**
     * Method to Parse All Brews events
     * */
    private List<ScheduledEventSchema> ParseGlobalBrewEvents (JSONArray aEvent) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrewEvents");
        List<ScheduledEventSchema> scheduledEventSchemas = new ArrayList<>();

        try {
            for (int i = 0; i < aEvent.length(); i++) {

                JSONObject event = (JSONObject) aEvent.get(i);
                scheduledEventSchemas.add(ParseGlobalBrewEvent(event));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return scheduledEventSchemas;
    }

    /**
     * Method to Parse one Brew event
     * */
    private ScheduledEventSchema ParseGlobalBrewEvent (JSONObject aEvent) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrewEvent");
        ScheduledEventSchema scheduledEventSchema = new ScheduledEventSchema();

        try {
            scheduledEventSchema.setScheduledEventId(Long.parseLong(aEvent.getString("EventId")));
            scheduledEventSchema.setScheduleId(Long.parseLong(aEvent.getString("ScheduleId")));
            scheduledEventSchema.setBrewId(Long.parseLong(aEvent.getString("BrewId")));


            scheduledEventSchema.setEventText(aEvent.getString("EventText"));
            scheduledEventSchema.setEventDate(aEvent.getString("EventDate"));
            scheduledEventSchema.setEventCalendarId(Long.parseLong(aEvent.getString("EventCalendarId")));


        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return scheduledEventSchema;
    }

    /**
     * Method to Parse All Brew Hops
     * */
    private List<InventorySchema> ParseGlobalBrewHops (JSONArray aInventory) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrewHops");
        List<InventorySchema> inventorySchemas = new ArrayList<>();

        try {
            for (int i = 0; i < aInventory.length(); i++) {

                JSONObject inventory = (JSONObject) aInventory.get(i);
                inventorySchemas.add(ParseGlobalBrewHop(inventory));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return inventorySchemas;
    }

    /**
     * Method to Parse one Brew Hop
     * */
    private InventorySchema ParseGlobalBrewHop (JSONObject aInventory) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrewHop");
        HopsSchema inventorySchema = new HopsSchema();

        try {

            inventorySchema.setInventoryId(Long.parseLong(aInventory.getString("HopsId")));
            inventorySchema.setBrewId(Long.parseLong(aInventory.getString("BrewId")));
            inventorySchema.setUserId(Long.parseLong(aInventory.getString("UserId")));

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
     * Method to Parse All Brew Fermentables
     * */
    private List<InventorySchema> ParseGlobalBrewFermentables (JSONArray aInventory) {

        if(APPUTILS.isLogging) Log.d(LOG, "Entering: ParseGlobalBrewFermentables");
        List<InventorySchema> inventorySchemas = new ArrayList<>();

        try {
            for (int i = 0; i < aInventory.length(); i++) {

                JSONObject inventory = (JSONObject) aInventory.get(i);
                inventorySchemas.add(ParseGlobalBrewFermentable(inventory));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return inventorySchemas;
    }

    /**
     * Method to Parse one Brew Fermentable
     * */
    private InventorySchema ParseGlobalBrewFermentable (JSONObject aInventory) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrewFermentable");
        FermentablesSchema inventorySchema = new FermentablesSchema();

        try {

            inventorySchema.setInventoryId(Long.parseLong(aInventory.getString("FermentablesId")));
            inventorySchema.setBrewId(Long.parseLong(aInventory.getString("BrewId")));
            inventorySchema.setUserId(Long.parseLong(aInventory.getString("UserId")));

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
     * Method to Parse All Brew Yeasts
     * */
    private List<InventorySchema> ParseGlobalBrewYeasts (JSONArray aInventory) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrewYeasts");
        List<InventorySchema> inventorySchemas = new ArrayList<>();

        try {
            for (int i = 0; i < aInventory.length(); i++) {

                JSONObject inventory = (JSONObject) aInventory.get(i);
                inventorySchemas.add(ParseGlobalBrewYeast(inventory));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return inventorySchemas;
    }

    /**
     * Method to Parse one Brew Yeast
     * */
    private InventorySchema ParseGlobalBrewYeast (JSONObject aInventory) {

        if(APPUTILS.isLogging) Log.d(LOG, "Entering: ParseGlobalBrewYeast");
        YeastSchema inventorySchema = new YeastSchema();

        try {
            inventorySchema.setInventoryId(Long.parseLong(aInventory.getString("YeastId")));
            inventorySchema.setBrewId(Long.parseLong(aInventory.getString("BrewId")));
            inventorySchema.setUserId(Long.parseLong(aInventory.getString("UserId")));

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
     * Method to Parse All Brew Equipments
     * */
    private List<InventorySchema> ParseGlobalBrewEquipments (JSONArray aInventory) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrewEquipments");
        List<InventorySchema> inventorySchemas = new ArrayList<>();

        try {
            for (int i = 0; i < aInventory.length(); i++) {

                JSONObject inventory = (JSONObject) aInventory.get(i);
                inventorySchemas.add(ParseGlobalBrewEquipment(inventory));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return inventorySchemas;
    }

    /**
     * Method to Parse one Brew Equipment
     * */
    private InventorySchema ParseGlobalBrewEquipment (JSONObject aInventory) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrewEquipment");
        EquipmentSchema inventorySchema = new EquipmentSchema();

        try {

            inventorySchema.setInventoryId(Long.parseLong(aInventory.getString("EquipmentId")));
            inventorySchema.setBrewId(Long.parseLong(aInventory.getString("BrewId")));
            inventorySchema.setUserId(Long.parseLong(aInventory.getString("UserId")));

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
     * Method to Parse All Brew Others
     * */
    private List<InventorySchema> ParseGlobalBrewOthers (JSONArray aInventory) {

        if(APPUTILS.isLogging)Log.d(LOG, "Entering: ParseGlobalBrewOthers");
        List<InventorySchema> inventorySchemas = new ArrayList<>();

        try {
            for (int i = 0; i < aInventory.length(); i++) {

                JSONObject inventory = (JSONObject) aInventory.get(i);
                inventorySchemas.add(ParseGlobalBrewOther(inventory));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return inventorySchemas;
    }

    /**
     * Method to Parse one Brew Other
     * */
    private InventorySchema ParseGlobalBrewOther (JSONObject aInventory) {

        if(APPUTILS.isLogging) Log.d(LOG, "Entering: ParseGlobalBrewOther");
        OtherSchema inventorySchema = new OtherSchema();

        try {

            inventorySchema.setInventoryId(Long.parseLong(aInventory.getString("OtherInventoryId")));
            inventorySchema.setBrewId(Long.parseLong(aInventory.getString("BrewId")));
            inventorySchema.setUserId(Long.parseLong(aInventory.getString("UserId")));

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
