package com.dev.town.small.brewtopia.WebAPI;

import android.util.Base64;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.BoilAdditionsSchema;
import com.dev.town.small.brewtopia.DataClass.BrewImageSchema;
import com.dev.town.small.brewtopia.DataClass.BrewNoteSchema;
import com.dev.town.small.brewtopia.DataClass.BrewSchema;

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
        params.put("CreatedOn", APPUTILS.dateFormat.format(new Date()));

        ArrayList<String> additions = new ArrayList<>();
        for (BoilAdditionsSchema ba: aBrewSchema.getBoilAdditionlist()) {
            HashMap<String,String> tempMap  = new HashMap<>();
            tempMap.put("AdditionId",Long.toString(ba.getAdditionId()));
            tempMap.put("BrewId",Long.toString(ba.getBrewId()));
            tempMap.put("AdditionName",ba.getAdditionName());
            tempMap.put("AdditionTime",Integer.toString(ba.getAdditionTime()));
            tempMap.put("AdditionQty",Double.toString(ba.getAdditionQty()));
            tempMap.put("AdditionUofM",ba.getUOfM());
            JSONObject jsonObject = new JSONObject(tempMap);
            additions.add( jsonObject.toString());

        }
        params.put("BoilAdditions", additions.toString());

        ArrayList<String> notes = new ArrayList<>();
        for (BrewNoteSchema bn: aBrewSchema.getBrewNoteSchemaList()) {
            HashMap<String,String> tempMap  = new HashMap<>();
            tempMap.put("BrewNoteId",Long.toString(bn.getNoteId()));
            tempMap.put("BrewId",Long.toString(bn.getBrewId()));
            tempMap.put("Note",bn.getBrewNote());
            tempMap.put("CreatedOn",bn.getCreatedOn());
            JSONObject jsonObject = new JSONObject(tempMap);
            notes.add( jsonObject.toString());

        }
        params.put("BrewNotes", notes.toString());

        ArrayList<String> images = new ArrayList<>();
        for (BrewImageSchema bi: aBrewSchema.getBrewImageSchemaList()) {
            HashMap<String,String> tempMap  = new HashMap<>();
            tempMap.put("ImageId",Long.toString(bi.getImageId()));
            tempMap.put("BrewId",Long.toString(bi.getBrewId()));
            tempMap.put("Image", Base64.encodeToString(APPUTILS.GetBitmapByteArray(bi.getImage()), Base64.DEFAULT));
            tempMap.put("CreatedOn",bi.getCreatedOn());
            JSONObject jsonObject = new JSONObject(tempMap);
            images.add( jsonObject.toString());

        }
        params.put("BrewImages", images.toString());

        System.out.println(params.toString());
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}