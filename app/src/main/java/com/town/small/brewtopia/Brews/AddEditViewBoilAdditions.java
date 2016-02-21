package com.town.small.brewtopia.Brews;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

public class AddEditViewBoilAdditions extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "AddEditBoilAdditions";

    private BrewActivityData brewData;

    private Button addButton;
    private Button confirmButton;

    private BrewActivityData.DisplayMode state;
    private BrewSchema brew;

    private ArrayList<BoilAddition> baArray = new ArrayList<BoilAddition>();

    private DataBaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_view_boil_additions);
        Log.e(LOG, "Entering: onCreate");

        getActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Boil Additions");

        dbManager = DataBaseManager.getInstance(getApplicationContext());

        addButton = (Button)findViewById(R.id.AddNewButton);
        confirmButton = (Button)findViewById(R.id.ConfirmButton);

        brewData = BrewActivityData.getInstance();

        loadAll();
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
        {
            addButton.setVisibility(View.INVISIBLE);
            confirmButton.setVisibility(View.INVISIBLE);
        }
    }

    private void BuildBoilAdditionArray() {
        Log.e(LOG, "Entering: BuildBoilAdditionArray");

        if(baArray.size()> 0)
            baArray.clear();

        if(!(brew == null))
        {

            for(BoilAdditionsSchema boilAdditionsSchema : brew.getBoilAdditionlist())
            {
                BoilAddition ba = new BoilAddition(this);
                ba.setBaSchema(boilAdditionsSchema);
                ba.setDisplay();
                baArray.add(ba);
            }
        }
        for(BoilAdditionsSchema boilAdditionsSchema : brewData.getBaArray())
        {
            BoilAddition ba = new BoilAddition(this);
            ba.setBaSchema(boilAdditionsSchema);
            ba.setDisplay();
            baArray.add(ba);
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
        LinearLayout layout = (LinearLayout)findViewById(R.id.AddList);
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
                    //baArray.clear();
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

    public void onConfirmClick(View aView) {
        if(!areAllFilled())
        {
            Toast.makeText(getApplicationContext(),"All Must be Filled in",Toast.LENGTH_LONG).show();
            return;
        }

        brewData.setBaArray(ConvertToSchemaArray());

        this.finish();
    }

    private boolean areAllFilled()
    {
        for(BoilAddition boilAddition : baArray)
        {
            if(!boilAddition.isPopulated())
                return false;
        }
        return true;
    }

    private void createBoilAddition()
    {
        BoilAddition ba = new BoilAddition(this);
        baArray.add(ba);
        DisplayBoilAddition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
