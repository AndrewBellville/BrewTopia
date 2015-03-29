package com.town.small.brewtopia.Brews;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.town.small.brewtopia.DataClass.BoilAdditionsSchema;
import com.town.small.brewtopia.DataClass.BrewSchema;
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

    private String state;
    private BrewSchema brew;

    private ArrayList<BoilAddition> baArray = new ArrayList<BoilAddition>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_view_boil_additions);
        Log.e(LOG, "Entering: onCreate");


        addButton = (Button)findViewById(R.id.AddNewButton);
        confirmButton = (Button)findViewById(R.id.ConfirmButton);

        brewData = BrewActivityData.getInstance();
        layout = (LinearLayout)findViewById(R.id.boilAdditionsLayout);

        state = BrewActivityData.getInstance().getAddEditViewState();
        brew = BrewActivityData.getInstance().getAddEditViewBrew();

        loadAll();
    }

    private void loadAll()
    {

        if(state.equals("Add"))
        {
            BuildBoilAdditionArray(brewData.getBaArray());

            if(baArray.size() > 0)
            {
                DisplayBoilAddition();
            }
            else {
                BoilAddition ba = new BoilAddition(this);
                layout.addView(ba.getLayout());
                baArray.add(ba);
            }
        }
        else if(state.equals("Edit")){
            DisplayBrewAdditions();
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
        BuildBoilAdditionArray(brew.getBoilAdditionlist());

        if(baArray.size() > 0)
        {
            DisplayBoilAddition();
        }
        else {
            BoilAddition ba = new BoilAddition(this);
            layout.addView(ba.getLayout());
            baArray.add(ba);
        }
    }

    private void BuildBoilAdditionArray(List<BoilAdditionsSchema> baSchemaArray) {
        Log.e(LOG, "Entering: BuildBoilAdditionArray");
        for(Iterator<BoilAdditionsSchema> i = baSchemaArray.iterator(); i.hasNext();)
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

            if(state.equals("View"))
                ba.setEditable(false);//Set non editable

            layout.addView(ba.getLayout());
        }
    }

    public void onAddClick(View aView) {
        Log.e(LOG, "Entering: onAddClick");
        BoilAddition ba = new BoilAddition(this);
        layout.addView(ba.getLayout());
        baArray.add(ba);
    }

    public void onConfirmClick(View aView) {
        if(!areAllFilled())
        {
            Toast.makeText(getApplicationContext(),"All Must be Filled in",Toast.LENGTH_LONG).show();
            return;
        }

        if(state.equals("Add")) {
            brewData.setBaArray(ConvertToSchemaArray());
        }
        else //edit
        {

        }
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
}
