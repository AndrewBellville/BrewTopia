package com.town.small.brewtopia.Brews;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.town.small.brewtopia.R;
import com.town.small.brewtopia.DataClass.*;
import com.town.small.brewtopia.Timer.BrewTimer;
import com.town.small.brewtopia.Timer.TimerData;

import java.util.ArrayList;
import java.util.List;

public class AddEditViewBrew extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "CreateBrew";

    private Button addStartButton;
    private TextView brewName;
    private TextView primary;
    private TextView secondary;
    private TextView bottle;
    private TextView description;
    private TextView boilTime;
    private String  UserName;
    private ScrollView ScrollView;
    private Spinner styleSpinner;
    ArrayAdapter<String> styleAdapter;

    private KeyListener brewNameListener;
    private KeyListener primaryListener;
    private KeyListener secondaryListener;
    private KeyListener bottleListener;
    private KeyListener descriptionListener;
    private KeyListener boilTimeListener;

    private DataBaseManager dbManager;
    private BrewActivityData brewActivityDataData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG, "Entering: onCreate");
        setContentView(R.layout.brew_view);

        //Add add edit layout default
        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_add_edit_view_brew, null);

        ScrollView = (ScrollView)findViewById(R.id.BrewScrollView);
        ScrollView.addView(view);

        //getActionBar().hide();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        dbManager = DataBaseManager.getInstance(getApplicationContext());

        brewName = (TextView)findViewById(R.id.editTextBrewName);
        primary = (TextView)findViewById(R.id.editTextPrimary);
        secondary = (TextView)findViewById(R.id.editTextSecondary);
        bottle = (TextView)findViewById(R.id.editTextBottle);
        description = (TextView)findViewById(R.id.editTextDescription);
        boilTime = (TextView)findViewById(R.id.editTextBoilTime);
        addStartButton = (Button)findViewById(R.id.AddStartBrewButton);
        styleSpinner = (Spinner) findViewById(R.id.beerStylespinner);

        brewNameListener = brewName.getKeyListener();
        primaryListener = primary.getKeyListener();
        secondaryListener = secondary.getKeyListener();
        bottleListener = bottle.getKeyListener();
        descriptionListener = description.getKeyListener();
        boilTimeListener = boilTime.getKeyListener();

        UserName = CurrentUser.getInstance().getUser().getUserName();

        brewActivityDataData = BrewActivityData.getInstance();

        setBrewStyleSpinner();

        if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.ADD) {
            ifAdd();
            setTitle("Create");
        }
         else//if not Add display view
        {
            ifView();
            setTitle(BrewActivityData.getInstance().getAddEditViewBrew().getBrewName());
        }
    }

    public void ifAdd()
    {
        ClearFields();
        addStartButton.setText("Submit");
        brewActivityDataData.setAddEditViewState(BrewActivityData.DisplayMode.ADD);
        ToggleFieldEditable(true);
    }

    public void ifEdit()
    {
        //get brew and display
        DisplayBrew(BrewActivityData.getInstance().getAddEditViewBrew());
        addStartButton.setText("Submit");
        brewActivityDataData.setAddEditViewState(BrewActivityData.DisplayMode.EDIT);
        ToggleFieldEditable(true);
    }

    public void ifView()
    {
        //get brew and display
        DisplayBrew(BrewActivityData.getInstance().getAddEditViewBrew());
        addStartButton.setText("Start Brew");
        //Set EditText to not editable and hide button
        brewActivityDataData.setAddEditViewState(BrewActivityData.DisplayMode.VIEW);
        ToggleFieldEditable(false);

    }

    private void setBrewStyleSpinner()
    {
        int MAX_STYLES = dbManager.getBrewStyleCount();
        String[] brewStyles = new String[MAX_STYLES];
        List<BrewStyleSchema> bs = dbManager.getAllBrewsStyles();

        for(int i=0;i < bs.size();i++)
        {
            brewStyles[i]= bs.get(i).getBrewStyleName();
        }
        styleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, brewStyles); //selected item will look like a spinner set from XML
        // Specify the layout to use when the list of choices appears
        styleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        styleSpinner.setAdapter(styleAdapter);
    }

    private void DisplayBrew(BrewSchema aBrewSchema)
    {
        Log.e(LOG, "Entering: DisplayBrew");
        //Reset all fields
        brewName.setText(aBrewSchema.getBrewName());
        primary.setText(Integer.toString(aBrewSchema.getPrimary()));
        secondary.setText(Integer.toString(aBrewSchema.getSecondary()));
        bottle.setText(Integer.toString(aBrewSchema.getBottle()));
        description.setText(aBrewSchema.getDescription());
        boilTime.setText(Integer.toString(aBrewSchema.getBoilTime()));
        // set to brew Style this might be deleted if its user created
        try
        {
            styleSpinner.setSelection(styleAdapter.getPosition(aBrewSchema.getStyle()));
        }
        catch (Exception e)
        {
            //if we are here user must have deleted brew style try to set to None
            try
            {
                styleSpinner.setSelection(styleAdapter.getPosition("None"));
            }
            catch (Exception ex)
            {
                // if all else fails set to index 0
                styleSpinner.setSelection(0);
            }
        }
    }


    public void onAddStartClick(View aView)
    {
        if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.VIEW)
        {
            //If we have an active timer we dont want to create a new one
            if(!(TimerData.getInstance().isTimerActive()))
            {
                //Create Schedule for Brew
                ScheduledBrewSchema sBrew = new ScheduledBrewSchema(brewName.getText().toString(), UserName);
                sBrew.SetScheduledDates(Integer.parseInt(primary.getText().toString()), Integer.parseInt(secondary.getText().toString()), Integer.parseInt(bottle.getText().toString()));
                dbManager.CreateAScheduledBrew(sBrew);

                //Set Brew into TimerData
                TimerData.getInstance().setTimerData(dbManager.getBrew(brewName.getText().toString(), UserName));
            }

            //Create and intent which will open Timer Activity
            Intent intent = new Intent(this, BrewTimer.class);

            //start next activity
            startActivity(intent);
        }
        else
        {
            validateSubmit();
        }
    }

    public void onEditClick(View aView)
    {
        ifEdit();
    }

    private void validateSubmit()
    {
        Log.e(LOG, "Entering: validateSubmit");

        if(brewName.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Blank Data Field", Toast.LENGTH_LONG).show();
            return;
        }

        int pf=0;
        int sf=0;
        int bc=0;
        int bt=0;
        try
        {
            pf  = Integer.parseInt(primary.getText().toString());
        }
        catch (Exception e){}
        try
        {
            sf = Integer.parseInt(secondary.getText().toString());
        }
        catch (Exception e){}
        try
        {
            bc = Integer.parseInt(bottle.getText().toString());
        }
        catch (Exception e){}
        try
        {
            bt = Integer.parseInt(boilTime.getText().toString());
        }
        catch (Exception e){}


        //Create Brew
        BrewSchema brew = new BrewSchema();
        brew.setBrewName(brewName.getText().toString());
        brew.setUserName(UserName);
        brew.setPrimary(pf);
        brew.setSecondary(sf);
        brew.setBottle(bc);
        brew.setDescription(description.getText().toString());
        brew.setBoilTime(bt);
        brew.setStyle(styleSpinner.getSelectedItem().toString());

        //Add Boil additions
        brew.setBoilAdditionlist(cloneList(BrewActivityData.getInstance().getBaArray()));
        //add brew name to boil list
        brew.setListBrewName();

        if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.ADD)
        {
            if(dbManager.CreateABrew(brew) == 0)// 0 brews failed to create
            {
                Toast.makeText(getApplicationContext(), "Duplicate Brew Name", Toast.LENGTH_LONG).show();
                return;
            }
        }
        else if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.EDIT)
        {
            dbManager.updateABrew(brew);
        }

        //remove all additions after submission
        BrewActivityData.getInstance().getBaArray().clear();

        this.finish();
    }

    public static List<BoilAdditionsSchema> cloneList(List<BoilAdditionsSchema> list) {
        List<BoilAdditionsSchema> clone = new ArrayList<BoilAdditionsSchema>(list.size());
        for(BoilAdditionsSchema item: list) clone.add(item);
        return clone;
    }

    //TODO: MAKE TAB VIEW FOR THIS
    public void onAddBoilAdditions(View aView)
    {
        //Create and intent which will open activity AddEditViewBoilAdditions
        Intent intent = new Intent(this, AddEditViewBoilAdditions.class);
        //start next activity
        startActivity(intent);
    }

    private void ClearFields()
    {
        Log.e(LOG, "Entering: ClearFields");
        //Reset all fields
        brewName.setText("");
        primary.setText("");
        secondary.setText("");
        bottle.setText("");
        description.setText("");
        boilTime.setText("");
        // set to None there should always be a None
        try
        {
            styleSpinner.setSelection(styleAdapter.getPosition("None"));
        }
        catch (Exception e)
        {
            // if for some reason None doesn't exist set to index 0
            styleSpinner.setSelection(0);
        }

    }

    private void ToggleFieldEditable(boolean aEditable)
    {
        Log.e(LOG, "Entering: ToggleFieldEditable");
        //Reset all fields
        if(!aEditable) {
            //addEditButton.setVisibility(View.INVISIBLE);
            brewName.setKeyListener(null);
            primary.setKeyListener(null);
            secondary.setKeyListener(null);
            bottle.setKeyListener(null);
            description.setKeyListener(null);
            boilTime.setKeyListener(null);
            styleSpinner.setClickable(false);
        }
        else
        {
            //addEditButton.setVisibility(View.VISIBLE);
            if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.ADD)
                brewName.setKeyListener(brewNameListener);
            primary.setKeyListener(primaryListener);
            secondary.setKeyListener(secondaryListener);
            bottle.setKeyListener(bottleListener);
            description.setKeyListener(descriptionListener);
            boilTime.setKeyListener(boilTimeListener);
            styleSpinner.setClickable(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
