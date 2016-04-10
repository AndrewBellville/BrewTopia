package com.town.small.brewtopia.Brews;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.town.small.brewtopia.DataClass.BoilAdditionsSchema;
import com.town.small.brewtopia.DataClass.BrewSchema;
import com.town.small.brewtopia.DataClass.DataBaseManager;
import com.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AddEditViewBoilAdditions extends Fragment {

    // Log cat tag
    private static final String LOG = "AddEditBoilAdditions";

    private BrewActivityData brewData;

    private Button addButton;
    LinearLayout layout;

    private BrewActivityData.DisplayMode state;
    private BrewSchema brew;

    private ArrayList<BoilAddition> baArray = new ArrayList<BoilAddition>();

    private DataBaseManager dbManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_add_edit_view_boil_additions,container,false);
        Log.e(LOG, "Entering: onCreate");

        dbManager = DataBaseManager.getInstance(getActivity());

        addButton = (Button)view.findViewById(R.id.AddNewButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddClick(view);
            }
        });

        layout = (LinearLayout)view.findViewById(R.id.AddList);

        brewData = BrewActivityData.getInstance();
        if(brewData.getBaArray().size()  > 0)
            brewData.getBaArray().clear();

        loadAll();

        return view;
    }

    @Override
    public void setMenuVisibility(boolean isShown) {
        if(isShown)
            loadAll();
        else
            onHidden();
    }

    private void loadAll()
    {
        state = BrewActivityData.getInstance().getAddEditViewState();
        brew = BrewActivityData.getInstance().getAddEditViewBrew();

        BuildBoilAdditionArray();

        if(baArray.size() > 0)
            DisplayBoilAddition();
        else
            createBoilAddition();

         //hide buttons
        if(state == BrewActivityData.DisplayMode.VIEW)
            addButton.setVisibility(View.INVISIBLE);
        else
            addButton.setVisibility(View.VISIBLE);
    }

    private void BuildBoilAdditionArray() {
        Log.e(LOG, "Entering: BuildBoilAdditionArray");

        if(baArray.size()> 0)
            baArray.clear();

        //If we have saved data we want to load  that and not from DB
        if(brewData.getBaArray().size() == 0)
        {

            for(BoilAdditionsSchema boilAdditionsSchema : brew.getBoilAdditionlist())
            {
                BoilAddition ba = new BoilAddition(getActivity());
                ba.setBaSchema(boilAdditionsSchema);
                ba.setDisplay();
                baArray.add(ba);
            }
        }
        else
        {
            for(BoilAdditionsSchema boilAdditionsSchema : brewData.getBaArray())
            {
                BoilAddition ba = new BoilAddition(getActivity());
                ba.setBaSchema(boilAdditionsSchema);
                ba.setDisplay();
                baArray.add(ba);
            }
        }

    }

    private ArrayList<BoilAdditionsSchema> ConvertToSchemaArray() {
        Log.e(LOG, "Entering: ConvertToSchemaArray");
        ArrayList<BoilAdditionsSchema> tempArray = new ArrayList<BoilAdditionsSchema>();

        for(BoilAddition boilAddition : baArray)
        {
            boilAddition.setSelf();
            tempArray.add(boilAddition.getBaSchema());
        }
        return tempArray;
    }

    private void DisplayBoilAddition()
    {

        layout.removeAllViews();
        layout.invalidate();

        for(BoilAddition boilAddition : baArray)
        {
            if(state == BrewActivityData.DisplayMode.VIEW) boilAddition.setEditable(false);

            // assign event handler
            boilAddition.setEventHandler(new BoilAddition.EventHandler() {
                @Override
                public void OnDeleteClickListener(String aAdditionName) {
                    //delete boil addition
                    dbManager.delete_boil_additions_by_brew_name_addition_name(brew.getBrewName(),aAdditionName,brew.getUserName());
                    //update brewActivity brew
                    BrewActivityData.getInstance().setAddEditViewBrew(dbManager.getBrew(brew.getBrewName(),brew.getUserName()));
                    BrewActivityData.getInstance().getBaArray().clear();
                    //re-load all
                    loadAll();
                }
            });

            layout.addView(boilAddition.getView());
        }
    }

    public void onAddClick(View aView) {
        Log.e(LOG, "Entering: onAddClick");
        createBoilAddition();
    }

    public void onHidden() {
        if(!(brewData == null) )
        {
            brewData.setBaArray(ConvertToSchemaArray());
        }
    }

    private void createBoilAddition()
    {
        BoilAddition ba = new BoilAddition(getActivity());
        baArray.add(ba);
        DisplayBoilAddition();
    }

}
