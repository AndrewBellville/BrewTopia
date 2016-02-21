package com.town.small.brewtopia.Brews;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.town.small.brewtopia.DataClass.BoilAdditionsSchema;
import com.town.small.brewtopia.DataClass.BrewSchema;
import com.town.small.brewtopia.DataClass.DataBaseManager;
import com.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddEditViewBoilAdditions extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "AddEditBoilAdditions";

    private LinearLayout layout;
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
        layout = (LinearLayout)findViewById(R.id.boilAdditionsLayout);

        loadAll();
    }

    private void loadAll()
    {
        state = BrewActivityData.getInstance().getAddEditViewState();
        brew = BrewActivityData.getInstance().getAddEditViewBrew();
        layout.removeAllViews();
        layout.invalidate();

        if(state == BrewActivityData.DisplayMode.ADD || state == BrewActivityData.DisplayMode.EDIT)
        {
            BuildBoilAdditionArray();

            if(baArray.size() > 0)
            {
                DisplayBoilAddition();
            }
            else {
                createBoilAddition();
            }
        }
        else // display View
        {
            DisplayBrewAdditions();

            //hide buttons
            addButton.setVisibility(View.INVISIBLE);
            confirmButton.setVisibility(View.INVISIBLE);
        }
    }

    private void DisplayBrewAdditions()
    {
        BuildBoilAdditionArray();

        if(baArray.size() > 0)
        {
            DisplayBoilAddition();
        }
        else {
            if(!(BrewActivityData.getInstance().getAddEditViewState() == BrewActivityData.DisplayMode.VIEW))
                createBoilAddition();
        }
    }

    private void BuildBoilAdditionArray() {
        Log.e(LOG, "Entering: BuildBoilAdditionArray");

        if(baArray.size()> 0)
            baArray.clear();

        for(Iterator<BoilAdditionsSchema> i = brew.getBoilAdditionlist().iterator(); i.hasNext();)
        {
            BoilAdditionsSchema baSchema = i.next();
            BoilAddition ba = new BoilAddition(this);
            ba.setBaSchema(baSchema);
            ba.setDisplay();
            baArray.add(ba);
        }

        for(Iterator<BoilAdditionsSchema> i = brewData.getBaArray().iterator(); i.hasNext();)
        {
            BoilAdditionsSchema baSchema = i.next();
            BoilAddition ba = new BoilAddition(this);
            ba.setBaSchema(baSchema);
            ba.setDisplay();
            baArray.add(ba);
        }
    }

    private ArrayList<BoilAdditionsSchema> ConvertToSchemaArray() {
        Log.e(LOG, "Entering: ConvertToSchemaArray");
        ArrayList<BoilAdditionsSchema> tempArray = new ArrayList<BoilAdditionsSchema>();

        for(Iterator<BoilAddition> i = baArray.iterator(); i.hasNext();)
        {
            BoilAddition ba = i.next();
            ba.setSelf();
            tempArray.add(ba.getBaSchema());
        }
        return tempArray;
    }

    private void DisplayBoilAddition()
    {
        for(Iterator<BoilAddition> i = baArray.iterator(); i.hasNext();) {
            BoilAddition ba = i.next();

            // assign event handler
            ba.setEventHandler(new BoilAddition.EventHandler() {
                @Override
                public void OnDeleteClickListener(String aAdditionName) {
                    //delete boil addition
                    dbManager.delete_boil_additions_by_brew_name_addition_name(brew.getBrewName(),aAdditionName,brew.getUserName());
                    //update brewActivity brew
                    BrewActivityData.getInstance().setAddEditViewBrew(dbManager.getBrew(brew.getBrewName(),brew.getUserName()));
                    //re-load all
                    loadAll();
                }
            });

            if(state == BrewActivityData.DisplayMode.VIEW)
                ba.setEditable(false);//Set non editable

            layout.addView(ba.getLayout());
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

        for(Iterator<BoilAddition> i = baArray.iterator(); i.hasNext();)
        {
            BoilAddition ba = i.next();

            if(!ba.isPopulated())
                return false;
        }

        return true;
    }

    private void createBoilAddition()
    {
        BoilAddition ba = new BoilAddition(this);

        // assign event handler
        ba.setEventHandler(new BoilAddition.EventHandler() {
            @Override
            public void OnDeleteClickListener(String aAdditionName) {
                //delete boil addition
                dbManager.delete_boil_additions_by_brew_name_addition_name(brew.getBrewName(),aAdditionName,brew.getUserName());
                //update brewActivity brew
                BrewActivityData.getInstance().setAddEditViewBrew(dbManager.getBrew(brew.getBrewName(),brew.getUserName()));
                //re-load all
                loadAll();
            }
        });

        layout.addView(ba.getLayout());
        baArray.add(ba);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
