package com.dev.town.small.brewtopia.Brews;

import android.graphics.Bitmap;
import android.util.Log;

import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.BoilAdditionsSchema;
import com.dev.town.small.brewtopia.DataClass.BrewImageSchema;
import com.dev.town.small.brewtopia.DataClass.BrewNoteSchema;
import com.dev.town.small.brewtopia.DataClass.BrewSchema;
import com.dev.town.small.brewtopia.DataClass.CurrentUser;
import com.dev.town.small.brewtopia.DataClass.InventorySchema;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 3/8/2015.
 */
public class BrewActivityData {

    // Log cat tag
    private static final String LOG = "ActivityData";

    private List<BoilAdditionsSchema> baArray = new ArrayList<BoilAdditionsSchema>();
    private List<BrewNoteSchema> brewNoteSchemaList = new ArrayList<BrewNoteSchema>();
    private List<InventorySchema> brewInventorySchemaList = new ArrayList<InventorySchema>();
    private List<BrewImageSchema> brewImageSchemaList = new ArrayList<BrewImageSchema>();

    //State for AddEditView Brew and Brew Name if Edit/View
    public enum DisplayMode {
        ADD, EDIT, VIEW, GLOBAL
    };
    private DisplayMode AddEditViewState = DisplayMode.ADD; // STATES: Add, Edit, View
    private BrewSchema AddEditViewBrew;
    private long ScheduleId; //Used for Displaying Completed brews
    private Bitmap ImageDisplyBitmap;//Used to dispaly full screen brew image

    //Singleton
    private static BrewActivityData mInstance = null;

    public static BrewActivityData getInstance() {
        if (mInstance == null) {
            mInstance = new BrewActivityData();
        }
        return mInstance;
    }
    // constructor
    private BrewActivityData() {
    }

    public void setViewStateAndBrew(DisplayMode aState, BrewSchema aBrewSchema)
    {
        setAddEditViewState(aState);
        if (aBrewSchema != null)
            setAddEditViewBrew(aBrewSchema);
    }

    public static List<BoilAdditionsSchema> cloneAdditionList(List<BoilAdditionsSchema> list) {
        List<BoilAdditionsSchema> clone = new ArrayList<BoilAdditionsSchema>(list.size());
        for(BoilAdditionsSchema item: list) clone.add(item);
        return clone;
    }
    public static List<BrewNoteSchema> cloneNoteList(List<BrewNoteSchema> list) {
        List<BrewNoteSchema> clone = new ArrayList<BrewNoteSchema>(list.size());
        for(BrewNoteSchema item: list) clone.add(item);
        return clone;
    }
    public static List<InventorySchema> cloneInventoryList(List<InventorySchema> list) {
        List<InventorySchema> clone = new ArrayList<InventorySchema>(list.size());
        for(InventorySchema item: list) clone.add(item);
        return clone;
    }

    //If the Current User owns the current brew allow edit
    public boolean CanEdit()
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: CanEdit " + AddEditViewBrew.getUserId() + " " +CurrentUser.getInstance().getUser().getUserId());
        if(AddEditViewBrew.getUserId() == CurrentUser.getInstance().getUser().getUserId() && getAddEditViewState() != DisplayMode.GLOBAL)
            return true;

        return false;
    }

//getters
    public List<BoilAdditionsSchema> getBaArray() {
        return baArray;
    }
    public BrewSchema getAddEditViewBrew() {
        return AddEditViewBrew;
    }
    public DisplayMode getAddEditViewState() {
        return AddEditViewState;
    }
    public List<BrewNoteSchema> getBrewNoteSchemaList() {
        return brewNoteSchemaList;
    }
    public List<InventorySchema> getBrewInventorySchemaList() {
        return brewInventorySchemaList;
    }
    public long getScheduleId() {
        return ScheduleId;
    }
    public Bitmap getImageDisplyBitmap() {
        return ImageDisplyBitmap;
    }
    public List<BrewImageSchema> getBrewImageSchemaList() {
        return brewImageSchemaList;
    }

    //setters
    public void setBaArray(List<BoilAdditionsSchema> baArray) {
        this.baArray = baArray;
    }
    public void setAddEditViewBrew(BrewSchema addEditViewBrew) {
        AddEditViewBrew = addEditViewBrew;
        setBaArray(cloneAdditionList(addEditViewBrew.getBoilAdditionlist()));
        setBrewNoteSchemaList(cloneNoteList(addEditViewBrew.getBrewNoteSchemaList()));
        setBrewInventorySchemaList(cloneInventoryList(addEditViewBrew.getBrewInventorySchemaList()));
    }
    public void setAddEditViewState(DisplayMode addEditViewState) {
        AddEditViewState = addEditViewState;
    }
    public void setBrewNoteSchemaList(List<BrewNoteSchema> brewNoteSchemaList) {
        this.brewNoteSchemaList = brewNoteSchemaList;
    }
    public void setBrewInventorySchemaList(List<InventorySchema> brewInventorySchemaList) {
        this.brewInventorySchemaList = brewInventorySchemaList;
    }
    public void setScheduleId(long scheduleId) {
        ScheduleId = scheduleId;
    }
    public void setImageDisplyBitmap(Bitmap imageDisplyBitmap) {
        ImageDisplyBitmap = imageDisplyBitmap;
    }
    public void setBrewImageSchemaList(List<BrewImageSchema> brewImageSchemaList) {
        this.brewImageSchemaList = brewImageSchemaList;
    }
}
