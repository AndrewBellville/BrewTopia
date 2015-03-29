package com.town.small.brewtopia.Brews;

import com.town.small.brewtopia.DataClass.BoilAdditionsSchema;
import com.town.small.brewtopia.DataClass.BrewSchema;
import com.town.small.brewtopia.DataClass.DataBaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 3/8/2015.
 */
public class BrewActivityData {

    // Log cat tag
    private static final String LOG = "ActivityData";

    private List<BoilAdditionsSchema> baArray = new ArrayList<BoilAdditionsSchema>();

    //State for AddEditView Brew and Brew Name if Edit/View
    private String AddEditViewState = "Add"; // STATES: Add, Edit, View
    private BrewSchema AddEditViewBrew;

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

    public void setViewStateAndBrew(String aState, BrewSchema aBrewSchema)
    {
        AddEditViewState = aState;
        AddEditViewBrew = aBrewSchema;
    }

//getters
    public List<BoilAdditionsSchema> getBaArray() {
        return baArray;
    }
    public BrewSchema getAddEditViewBrew() {
        return AddEditViewBrew;
    }
    public String getAddEditViewState() {
        return AddEditViewState;
    }

    //setters
    public void setBaArray(List<BoilAdditionsSchema> baArray) {
        this.baArray = baArray;
    }
    public void setAddEditViewBrew(BrewSchema addEditViewBrew) {
        AddEditViewBrew = addEditViewBrew;
    }
    public void setAddEditViewState(String addEditViewState) {
        AddEditViewState = addEditViewState;
    }
}
