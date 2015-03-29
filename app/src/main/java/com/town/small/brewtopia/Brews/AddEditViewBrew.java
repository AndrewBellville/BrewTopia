package com.town.small.brewtopia.Brews;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.town.small.brewtopia.R;
import com.town.small.brewtopia.DataClass.*;
import com.town.small.brewtopia.Timer.BrewTimer;
import com.town.small.brewtopia.Timer.TimerData;

public class AddEditViewBrew extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "CreateBrew";

    private Button addEditButton;
    private TextView brewName;
    private TextView primary;
    private TextView secondary;
    private TextView bottle;
    private TextView description;
    private TextView boilTime;
    private TextView errorText;

    private KeyListener brewNameListener;
    private KeyListener primaryListener;
    private KeyListener secondaryListener;
    private KeyListener bottleListener;
    private KeyListener descriptionListener;
    private KeyListener boilTimeListener;

    private DataBaseManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_view_brew);
        Log.e(LOG, "Entering: onCreate");
        //getActionBar().hide();
        dbManager = DataBaseManager.getInstance(getApplicationContext());

        brewName = (TextView)findViewById(R.id.editTextBrewName);
        primary = (TextView)findViewById(R.id.editTextPrimary);
        secondary = (TextView)findViewById(R.id.editTextSecondary);
        bottle = (TextView)findViewById(R.id.editTextBottle);
        description = (TextView)findViewById(R.id.editTextDescription);
        boilTime = (TextView)findViewById(R.id.editTextBoilTime);
        errorText = (TextView)findViewById(R.id.ErrorTextView);
        addEditButton = (Button)findViewById(R.id.AddBrewButton);

        brewNameListener = brewName.getKeyListener();
        primaryListener = primary.getKeyListener();
        secondaryListener = secondary.getKeyListener();
        bottleListener = bottle.getKeyListener();
        descriptionListener = description.getKeyListener();
        boilTimeListener = boilTime.getKeyListener();

        String tempState = BrewActivityData.getInstance().getAddEditViewState();
        if(tempState.equals("Add"))
            ifAdd();
         else if(tempState.equals("Edit"))
            ifEdit();
         else//if not Add/Edit display view
            ifView();


    }

    public void ifAdd()
    {
        ClearFields();
        addEditButton.setText("Submit");
        ToggleFieldEditable(true);
    }

    public void ifEdit()
    {
        //get brew and display
        DisplayBrew(BrewActivityData.getInstance().getAddEditViewBrew());
        addEditButton.setText("Submit");
        ToggleFieldEditable(true);
    }

    public void ifView()
    {
        //get brew and display
        DisplayBrew(BrewActivityData.getInstance().getAddEditViewBrew());
        addEditButton.setText("Start Brew");
        //Set EditText to not editable and hide button
        ToggleFieldEditable(false);

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
    }


    public void onButtonClick(View aView)
    {
        if(addEditButton.getText().equals("Start Brew"))
        {
            //Create and intent which will open Timer Activity
            Intent intent = new Intent(this, BrewTimer.class);

            //Set Brew into TimerData
            TimerData.getInstance().setTimeData(BrewActivityData.getInstance().getAddEditViewBrew());

            //start next activity
            startActivity(intent);
        }
        else
        {
            validateSubmit();
        }
    }

    private void validateSubmit()
    {
        Log.e(LOG, "Entering: validateSubmit");

        if(brewName.getText().toString().equals(""))
        {
            errorText.setText("Blank Data Field");
            return;
        }

        int pf = Integer.parseInt(primary.getText().toString());
        int sf = Integer.parseInt(secondary.getText().toString());
        int bc = Integer.parseInt(bottle.getText().toString());
        int bt = Integer.parseInt(boilTime.getText().toString());

        //Create Brew
        BrewSchema brew = new BrewSchema();
        brew.setBrewName(brewName.getText().toString());
        brew.setPrimary(pf);
        brew.setSecondary(sf);
        brew.setBottle(bc);
        brew.setDescription(description.getText().toString());
        brew.setBoilTime(bt);

        //Add Boil additions
        brew.setBoilAdditionlist(BrewActivityData.getInstance().getBaArray());
        //add brew name to boil list
        brew.setListBrewName();

        if(!dbManager.CreateABrew(brew))
        {
            errorText.setText("Duplicate Brew Name");
            return;
        }

        this.finish();
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
        }
        else
        {
            //addEditButton.setVisibility(View.VISIBLE);
            brewName.setKeyListener(brewNameListener);
            primary.setKeyListener(primaryListener);
            secondary.setKeyListener(secondaryListener);
            bottle.setKeyListener(bottleListener);
            description.setKeyListener(descriptionListener);
            boilTime.setKeyListener(boilTimeListener);
        }


    }
}
