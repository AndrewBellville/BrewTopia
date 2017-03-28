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
        params = new HashMap<>();
        params.put("BrewId", Long.toString(aBrewSchema.getBrewId()));
        params.put("BrewName", aBrewSchema.getBrewName());
        params.put("UserId", Long.toString(aBrewSchema.getUserId()));
        params.put("BoilTime", Integer.toString(aBrewSchema.getBoilTime()));
        params.put("Primary", Integer.toString(aBrewSchema.getPrimary()));
        params.put("Secondary", Integer.toString(aBrewSchema.getSecondary()));
        params.put("Bottling", Integer.toString(aBrewSchema.getBottle()));
        params.put("OriginalGravity", Double.toString(aBrewSchema.getTargetOG()));
        params.put("FinalGravity", Double.toString(aBrewSchema.getTargetFG()));
        params.put("ABV", Double.toString(aBrewSchema.getTargetABV()));
        params.put("Description", aBrewSchema.getDescription());
        params.put("Favorite", Integer.toString(aBrewSchema.getFavorite()));
        params.put("Scheduled", Integer.toString(aBrewSchema.getScheduled()));
        params.put("OnTap", Integer.toString(aBrewSchema.getOnTap()));
        params.put("IBU", Double.toString(aBrewSchema.getIBU()));
        params.put("BatchSize", Double.toString(aBrewSchema.getBatchSize()));
        params.put("Efficiency", Double.toString(aBrewSchema.getEfficiency()));
        params.put("Method", aBrewSchema.getMethod());
        params.put("Style", aBrewSchema.getStyle());
        params.put("TotalBrewed", Integer.toString(aBrewSchema.getTotalBrewed()));
        params.put("SRM", Integer.toString(aBrewSchema.getSRM()));
        params.put("IsGlobal", Integer.toString(aBrewSchema.getIsGlobal()));
        params.put("IsNew", Integer.toString(aBrewSchema.getIsNew()));
        params.put("CreatedOn", APPUTILS.dateFormat.format(new Date()));

        //*******************Boil Additions***********************************
        ArrayList<String> additions = new ArrayList<>();
        for (BoilAdditionsSchema ba: aBrewSchema.getBoilAdditionlist()) {
            HashMap<String,String> tempMap  = new HashMap<>();
            tempMap.put("AdditionId",Long.toString(ba.getAdditionId()));
            tempMap.put("BrewId",Long.toString(ba.getBrewId()));
            tempMap.put("AdditionName",ba.getAdditionName());
            tempMap.put("AdditionTime",Integer.toString(ba.getAdditionTime()));
            tempMap.put("AdditionQty",Double.toString(ba.getAdditionQty()));
            tempMap.put("AdditionUofM",ba.getUOfM());
            tempMap.put("IsNew", Integer.toString(ba.getIsNew()));
            JSONObject jsonObject = new JSONObject(tempMap);
            additions.add( jsonObject.toString());

        }
        params.put("BoilAdditions", additions.toString());

        //*******************Brew Notes***********************************
        ArrayList<String> notes = new ArrayList<>();
        for (BrewNoteSchema bn: aBrewSchema.getBrewNoteSchemaList()) {
            HashMap<String,String> tempMap  = new HashMap<>();
            tempMap.put("BrewNoteId",Long.toString(bn.getNoteId()));
            tempMap.put("BrewId",Long.toString(bn.getBrewId()));
            tempMap.put("Note",bn.getBrewNote());
            tempMap.put("CreatedOn",bn.getCreatedOn());
            tempMap.put("IsNew", Integer.toString(bn.getIsNew()));
            JSONObject jsonObject = new JSONObject(tempMap);
            notes.add( jsonObject.toString());

        }
        params.put("BrewNotes", notes.toString());

        //*******************Brew Images***********************************
        ArrayList<String> images = new ArrayList<>();
        for (BrewImageSchema bi: aBrewSchema.getBrewImageSchemaList()) {
            HashMap<String,String> tempMap  = new HashMap<>();
            tempMap.put("ImageId",Long.toString(bi.getImageId()));
            tempMap.put("BrewId",Long.toString(bi.getBrewId()));
            tempMap.put("Image", Base64.encodeToString(APPUTILS.GetBitmapByteArray(bi.getImage()), Base64.DEFAULT));
            tempMap.put("CreatedOn",bi.getCreatedOn());
            tempMap.put("IsNew", Integer.toString(bi.getIsNew()));
            JSONObject jsonObject = new JSONObject(tempMap);
            images.add( jsonObject.toString());

        }
        params.put("BrewImages", images.toString());

        //*******************Brew Schedule***********************************
        ArrayList<String> schedule = new ArrayList<>();
        for (ScheduledBrewSchema bs: aBrewSchema.getScheduledBrewSchemas()) {
            HashMap<String,String> tempMap  = new HashMap<>();
            tempMap.put("ScheduleId",Long.toString(bs.getScheduleId()));
            tempMap.put("BrewId",Long.toString(bs.getBrewId()));
            tempMap.put("UserId",Long.toString(bs.getUserId()));
            tempMap.put("BrewName",bs.getBrewName());
            tempMap.put("EndBrewDate",bs.getEndBrewDate());
            tempMap.put("Note",bs.getNotes());
            tempMap.put("StyleColor",bs.getColor());
            tempMap.put("OriginalGravity",Double.toString(bs.getOG()));
            tempMap.put("FinalGravity",Double.toString(bs.getFG()));
            tempMap.put("ABV",Double.toString(bs.getABV()));
            tempMap.put("Active",Integer.toString(bs.getActive()));
            tempMap.put("CreatedOn",bs.getStartDate());
            tempMap.put("UseStarter",Integer.toString(bs.getHasStarter()));
            tempMap.put("IsNew", Integer.toString(bs.getIsNew()));
            JSONObject jsonObject = new JSONObject(tempMap);
            schedule.add( jsonObject.toString());
        }
        params.put("BrewSchedule", schedule.toString());

        //*******************Brew Schedule Events***********************************
        ArrayList<String> scheduleEvents = new ArrayList<>();
        for (ScheduledEventSchema se: aBrewSchema.getScheduledEventSchemas()) {
            HashMap<String,String> tempMap  = new HashMap<>();
            tempMap.put("EventId",Long.toString(se.getScheduledEventId()));
            tempMap.put("BrewId",Long.toString(se.getBrewId()));
            tempMap.put("ScheduleId",Long.toString(se.getScheduleId()));
            tempMap.put("EventText",se.getEventText());
            tempMap.put("EventDate",se.getEventDate());
            tempMap.put("EventCalendarId",Long.toString(se.getEventCalendarId()));
            tempMap.put("IsNew", Integer.toString(se.getIsNew()));
            JSONObject jsonObject = new JSONObject(tempMap);
            scheduleEvents.add( jsonObject.toString());
        }
        params.put("BrewScheduleEvents", scheduleEvents.toString());

        //*******************Brew Inventory***********************************
        ArrayList<String> inventoryHops = new ArrayList<>();
        ArrayList<String> inventoryFermentables = new ArrayList<>();
        ArrayList<String> inventoryYeast = new ArrayList<>();
        ArrayList<String> inventoryEquipment = new ArrayList<>();
        ArrayList<String> inventoryOther = new ArrayList<>();
        for (InventorySchema is: aBrewSchema.getBrewInventorySchemaList()) {
            if(is instanceof HopsSchema)
            {
                JSONObject jsonObject = new JSONObject(InventoryHopsHelper((HopsSchema)is));
                inventoryHops.add( jsonObject.toString());
            }
            if(is instanceof FermentablesSchema)
            {
                JSONObject jsonObject = new JSONObject(InventoryFermentablesHelper((FermentablesSchema)is));
                inventoryFermentables.add( jsonObject.toString());
            }
            if(is instanceof YeastSchema)
            {
                JSONObject jsonObject = new JSONObject(InventoryYeastHelper((YeastSchema)is));
                inventoryYeast.add( jsonObject.toString());
            }
            if(is instanceof EquipmentSchema)
            {
                JSONObject jsonObject = new JSONObject(InventoryEquipmentHelper((EquipmentSchema)is));
                inventoryEquipment.add( jsonObject.toString());
            }
            if(is instanceof OtherSchema)
            {
                JSONObject jsonObject = new JSONObject(InventoryOtherHelper((OtherSchema)is));
                inventoryOther.add( jsonObject.toString());
            }

        }
        params.put("InventoryHops", inventoryHops.toString());
        params.put("InventoryFermentables", inventoryFermentables.toString());
        params.put("InventoryYeast", inventoryYeast.toString());
        params.put("InventoryEquipment", inventoryEquipment.toString());
        params.put("InventoryOther", inventoryOther.toString());

        if(APPUTILS.isLogging) System.out.println(params.toString());
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    private HashMap<String,String> InventoryHopsHelper(HopsSchema aInventorySchema)
    {
        HashMap<String,String> tempMap  = new HashMap<>();
        tempMap.put("HopsId",Long.toString(aInventorySchema.getInventoryId()));
        tempMap.put("BrewId",Long.toString(aInventorySchema.getBrewId()));
        tempMap.put("UserId",Long.toString(aInventorySchema.getUserId()));
        tempMap.put("InventoryQty",Integer.toString(aInventorySchema.getInvetoryQty()));
        tempMap.put("InventoryName",aInventorySchema.getInventoryName());
        tempMap.put("InventoryAmount",Double.toString(aInventorySchema.getAmount()));
        tempMap.put("InventoryType",aInventorySchema.getType());
        tempMap.put("AA",Double.toString(aInventorySchema.getAA()));
        tempMap.put("InventoryUse",aInventorySchema.getUse());
        tempMap.put("InventoryTime",Integer.toString(aInventorySchema.getTime()));
        tempMap.put("InventoryUofM",aInventorySchema.getInventoryUOfM());
        tempMap.put("IsNew", Integer.toString(aInventorySchema.getIsNew()));

        return tempMap;
    }

    private HashMap<String,String> InventoryFermentablesHelper(FermentablesSchema aInventorySchema)
    {
        HashMap<String,String> tempMap  = new HashMap<>();
        tempMap.put("FermentablesId",Long.toString(aInventorySchema.getInventoryId()));
        tempMap.put("BrewId",Long.toString(aInventorySchema.getBrewId()));
        tempMap.put("UserId",Long.toString(aInventorySchema.getUserId()));
        tempMap.put("InventoryQty",Integer.toString(aInventorySchema.getInvetoryQty()));
        tempMap.put("InventoryName",aInventorySchema.getInventoryName());
        tempMap.put("InventoryAmount",Double.toString(aInventorySchema.getAmount()));
        tempMap.put("PPG",Double.toString(aInventorySchema.getPoundPerGallon()));
        tempMap.put("Lovibond",Double.toString(aInventorySchema.getLovibond()));
        tempMap.put("InventoryUofM",aInventorySchema.getInventoryUOfM());
        tempMap.put("InventoryType",aInventorySchema.getType());
        tempMap.put("Yield",Double.toString(aInventorySchema.getYield()));
        tempMap.put("Potential",Double.toString(aInventorySchema.getPotential()));
        tempMap.put("IsNew", Integer.toString(aInventorySchema.getIsNew()));

        return tempMap;
    }

    private HashMap<String,String> InventoryYeastHelper(YeastSchema aInventorySchema)
    {
        HashMap<String,String> tempMap  = new HashMap<>();
        tempMap.put("YeastId",Long.toString(aInventorySchema.getInventoryId()));
        tempMap.put("BrewId",Long.toString(aInventorySchema.getBrewId()));
        tempMap.put("UserId",Long.toString(aInventorySchema.getUserId()));
        tempMap.put("InventoryQty",Integer.toString(aInventorySchema.getInvetoryQty()));
        tempMap.put("InventoryName",aInventorySchema.getInventoryName());
        tempMap.put("InventoryAmount",Double.toString(aInventorySchema.getAmount()));
        tempMap.put("Flocculation",aInventorySchema.getFlocculation());
        tempMap.put("Starter",Integer.toString(aInventorySchema.getStarter()));
        tempMap.put("Attenuation",Double.toString(aInventorySchema.getAttenuation()));
        tempMap.put("OTL",Double.toString(aInventorySchema.getOptimumTempLow()));
        tempMap.put("OTH",Double.toString(aInventorySchema.getOptimumTempHigh()));
        tempMap.put("InventoryUofM",aInventorySchema.getInventoryUOfM());
        tempMap.put("Laboratory",aInventorySchema.getLaboratory());
        tempMap.put("InventoryType",aInventorySchema.getType());
        tempMap.put("Form",aInventorySchema.getForm());
        tempMap.put("IsNew", Integer.toString(aInventorySchema.getIsNew()));

        return tempMap;
    }

    private HashMap<String,String> InventoryEquipmentHelper(EquipmentSchema aInventorySchema)
    {
        HashMap<String,String> tempMap  = new HashMap<>();
        tempMap.put("EquipmentId",Long.toString(aInventorySchema.getInventoryId()));
        tempMap.put("BrewId",Long.toString(aInventorySchema.getBrewId()));
        tempMap.put("UserId",Long.toString(aInventorySchema.getUserId()));
        tempMap.put("InventoryQty",Integer.toString(aInventorySchema.getInvetoryQty()));
        tempMap.put("InventoryName",aInventorySchema.getInventoryName());
        tempMap.put("InventoryAmount",Double.toString(aInventorySchema.getAmount()));
        tempMap.put("InventoryUofM",aInventorySchema.getInventoryUOfM());
        tempMap.put("IsNew", Integer.toString(aInventorySchema.getIsNew()));

        return tempMap;
    }

    private HashMap<String,String> InventoryOtherHelper(OtherSchema aInventorySchema)
    {
        HashMap<String,String> tempMap  = new HashMap<>();
        tempMap.put("OtherInventoryId",Long.toString(aInventorySchema.getInventoryId()));
        tempMap.put("BrewId",Long.toString(aInventorySchema.getBrewId()));
        tempMap.put("UserId",Long.toString(aInventorySchema.getUserId()));
        tempMap.put("InventoryQty",Integer.toString(aInventorySchema.getInvetoryQty()));
        tempMap.put("InventoryName",aInventorySchema.getInventoryName());
        tempMap.put("InventoryAmount",Double.toString(aInventorySchema.getAmount()));
        tempMap.put("InventoryUofM",aInventorySchema.getInventoryUOfM());
        tempMap.put("InventoryUse",aInventorySchema.getUse());
        tempMap.put("IsNew", Integer.toString(aInventorySchema.getIsNew()));

        return tempMap;
    }
}

