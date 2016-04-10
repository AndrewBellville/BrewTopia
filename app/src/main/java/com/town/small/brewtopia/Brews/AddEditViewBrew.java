package com.town.small.brewtopia.Brews;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.town.small.brewtopia.R;
import com.town.small.brewtopia.DataClass.*;
import com.town.small.brewtopia.Timer.BrewTimer;
import com.town.small.brewtopia.Timer.TimerData;
import com.town.small.brewtopia.Timer.TimerPager;

import java.util.ArrayList;
import java.util.List;

public class AddEditViewBrew extends Fragment {

    // Log cat tag
    private static final String LOG = "AddEditViewBrew";

    private Button startButton;
    private Button editBrewButton;
    private EditText brewName;
    private EditText primary;
    private EditText secondary;
    private EditText bottle;
    private EditText description;
    private EditText boilTime;
    private EditText targetOG;
    private EditText targetFG;
    private EditText targetABV;

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
    private KeyListener targetOGListener;
    private KeyListener targetFGListener;
    private KeyListener targetABVListener;

    private DataBaseManager dbManager;
    private BrewActivityData brewActivityDataData;
    private BrewSchema brewSchema;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG, "Entering: onCreate");
        View returnView = inflater.inflate(R.layout.brew_view,container,false);

        //Add add edit layout default
        View mainView = inflater.inflate(R.layout.activity_add_edit_view_brew, null);

        ScrollView = (ScrollView)returnView.findViewById(R.id.BrewScrollView);
        ScrollView.addView(mainView);

        dbManager = DataBaseManager.getInstance(getActivity());

        brewName = (EditText)mainView.findViewById(R.id.editTextBrewName);
        primary = (EditText)mainView.findViewById(R.id.editTextPrimary);
        secondary = (EditText)mainView.findViewById(R.id.editTextSecondary);
        bottle = (EditText)mainView.findViewById(R.id.editTextBottle);
        description = (EditText)mainView.findViewById(R.id.editTextDescription);
        boilTime = (EditText)mainView.findViewById(R.id.editTextBoilTime);
        targetOG = (EditText)mainView.findViewById(R.id.editTextTargetOG);
        targetFG = (EditText)mainView.findViewById(R.id.editTextTargetFG);
        targetABV = (EditText)mainView.findViewById(R.id.editTextTargetABV);


        startButton = (Button)returnView.findViewById(R.id.AddStartBrewButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartButtonClick(view);
            }
        });
        editBrewButton = (Button)returnView.findViewById(R.id.EditBrewButton);
        editBrewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditClick(view);
            }
        });

        styleSpinner = (Spinner) returnView.findViewById(R.id.beerStylespinner);

        brewNameListener = brewName.getKeyListener();
        primaryListener = primary.getKeyListener();
        secondaryListener = secondary.getKeyListener();
        bottleListener = bottle.getKeyListener();
        descriptionListener = description.getKeyListener();
        boilTimeListener = boilTime.getKeyListener();
        targetOGListener = targetOG.getKeyListener();
        targetFGListener = targetFG.getKeyListener();
        targetABVListener = targetABV.getKeyListener();

        UserName = CurrentUser.getInstance().getUser().getUserName();

        brewActivityDataData = BrewActivityData.getInstance();

        setBrewStyleSpinner();

        if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.ADD) {
            ifAdd();
        }
         else if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.EDIT) {
            brewSchema = brewActivityDataData.getAddEditViewBrew();
            ifEdit();
        }
        else if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.VIEW) {
            brewSchema = brewActivityDataData.getAddEditViewBrew();
            ifView();
        }
        return returnView;
    }

    public void ifAdd()
    {
        ClearFields();
        startButton.setVisibility(View.INVISIBLE);
        editBrewButton.setText("Submit");
        brewActivityDataData.setAddEditViewState(BrewActivityData.DisplayMode.ADD);
        ToggleFieldEditable(true);
    }

    public void ifEdit()
    {
        startButton.setVisibility(View.INVISIBLE);

        //get brew and display
        DisplayBrew(brewSchema);
        editBrewButton.setText("Submit");
        brewActivityDataData.setAddEditViewState(BrewActivityData.DisplayMode.EDIT);

        ToggleFieldEditable(false);
        ToggleFieldEditable(true);
    }

    public void ifView()
    {
        //get brew and display
        DisplayBrew(brewSchema);
        editBrewButton.setText("Edit Brew");
        startButton.setText("Start Brew");
        startButton.setVisibility(View.VISIBLE);
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
        styleAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, brewStyles); //selected item will look like a spinner set from XML
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
        targetOG.setText(Double.toString(aBrewSchema.getTargetOG()));
        targetFG.setText(Double.toString(aBrewSchema.getTargetFG()));
        targetABV.setText(Double.toString(aBrewSchema.getTargetABV()));

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


    public void onStartButtonClick(View aView)
    {
        if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.VIEW)
        {
            //If we have an active timer we dont want to create a new one
            if(!(TimerData.getInstance().isTimerActive()))
            {
                //Set Brew into TimerData
                TimerData.getInstance().setTimerData(dbManager.getBrew(brewName.getText().toString(), UserName));
            }

            //Create and intent which will open Timer Activity
            Intent intent = new Intent(getActivity(), TimerPager.class);

            //start next activity
            startActivity(intent);
        }
    }

    public void onEditClick(View aView)
    {
        if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.VIEW )
            ifEdit();
        else
            validateSubmit();
    }

    private void validateSubmit()
    {
        Log.e(LOG, "Entering: validateSubmit");

        if(brewName.getText().toString().equals(""))
        {
            Toast.makeText(getActivity(), "Blank Data Field", Toast.LENGTH_LONG).show();
            return;
        }

        int pf=0;
        int sf=0;
        int bc=0;
        int bt=0;

        double og=0.0;
        double fg=0.0;
        double abv=0.0;

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

        try
        {
            og = Double.parseDouble(targetOG.getText().toString());
        }
        catch (Exception e){}
        try
        {
            fg = Double.parseDouble(targetFG.getText().toString());
        }
        catch (Exception e){}
        try
        {
            abv = Double.parseDouble(targetABV.getText().toString());
        }
        catch (Exception e){}


        //Create Brew
        BrewSchema brew = new BrewSchema();
        brew.setBrewName(brewName.getText().toString());
        brew.setUserName(UserName);
        brew.setPrimary(pf);
        brew.setSecondary(sf);
        brew.setBottle(bc);
        brew.setTargetOG(og);
        brew.setTargetFG(fg);
        brew.setTargetABV(abv);
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
                Toast.makeText(getActivity(), "Duplicate Brew Name", Toast.LENGTH_LONG).show();
                return;
            }
        }
        else if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.EDIT)
        {
            dbManager.updateABrew(brew);
        }

        //remove all additions after submission
        BrewActivityData.getInstance().getBaArray().clear();

        brewSchema = brew;
        brewActivityDataData.setAddEditViewBrew(brewSchema);

        ifView();
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
        Intent intent = new Intent(getActivity(), AddEditViewBoilAdditions.class);
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
        targetOG.setText("");
        targetFG.setText("");
        targetABV.setText("");
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
            brewName.setClickable(false);
            brewName.setEnabled(false);
            //brewName.setFocusable(false);

            primary.setKeyListener(null);
            primary.setClickable(false);
            primary.setEnabled(false);
           //primary.setFocusable(false);

            secondary.setKeyListener(null);
            secondary.setClickable(false);
            secondary.setEnabled(false);
            //secondary.setFocusable(false);

            bottle.setKeyListener(null);
            bottle.setClickable(false);
            bottle.setEnabled(false);
            //bottle.setFocusable(false);

            description.setKeyListener(null);
            description.setClickable(false);
            description.setEnabled(false);
            //description.setFocusable(false);

            boilTime.setKeyListener(null);
            boilTime.setClickable(false);
            boilTime.setEnabled(false);
            //boilTime.setFocusable(false);

            targetOG.setKeyListener(null);
            targetOG.setClickable(false);
            targetOG.setEnabled(false);
            //targetOG.setFocusable(false);

            targetFG.setKeyListener(null);
            targetFG.setClickable(false);
            targetFG.setEnabled(false);
            //targetFG.setFocusable(false);

            targetABV.setKeyListener(null);
            targetABV.setClickable(false);
            targetABV.setEnabled(false);
            //targetABV.setFocusable(false);

            styleSpinner.setClickable(false);
        }
        else
        {
            //addEditButton.setVisibility(View.VISIBLE);
            if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.ADD)
            {
                brewName.setKeyListener(brewNameListener);
                brewName.setClickable(true);
                brewName.setEnabled(true);
                brewName.setFocusable(true);
            }

            primary.setKeyListener(primaryListener);
            primary.setClickable(true);
            primary.setEnabled(true);
            primary.setFocusable(true);

            secondary.setKeyListener(secondaryListener);
            secondary.setClickable(true);
            secondary.setEnabled(true);
            secondary.setFocusable(true);

            bottle.setKeyListener(bottleListener);
            bottle.setClickable(true);
            bottle.setEnabled(true);
            bottle.setFocusable(true);

            description.setKeyListener(descriptionListener);
            description.setClickable(true);
            description.setEnabled(true);
            description.setFocusable(true);

            boilTime.setKeyListener(boilTimeListener);
            boilTime.setClickable(true);
            boilTime.setEnabled(true);
            boilTime.setFocusable(true);

            targetOG.setKeyListener(targetOGListener);
            targetOG.setClickable(true);
            targetOG.setEnabled(true);
            //targetOG.setFocusable(true);

            targetFG.setKeyListener(targetFGListener);
            targetFG.setClickable(true);
            targetFG.setEnabled(true);
            //targetFG.setFocusable(true);

            targetABV.setKeyListener(targetABVListener);
            targetABV.setClickable(true);
            targetABV.setEnabled(true);
            //targetABV.setFocusable(true);

            styleSpinner.setClickable(true);
        }
    }
}
