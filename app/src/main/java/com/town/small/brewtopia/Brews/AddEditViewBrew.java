package com.town.small.brewtopia.Brews;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
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

        brewNameListener = brewName.getKeyListener();
        primaryListener = primary.getKeyListener();
        secondaryListener = secondary.getKeyListener();
        bottleListener = bottle.getKeyListener();
        descriptionListener = description.getKeyListener();
        boilTimeListener = boilTime.getKeyListener();

        UserName = CurrentUser.getInstance().getUser().getUserName();

        brewActivityDataData = BrewActivityData.getInstance();

        if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.ADD) {
            ifAdd();
            setTitle("Create");
        }
         else//if not Add display view
        {
            ifView();
            setTitle(BrewActivityData.getInstance().getAddEditViewBrew().getBrewName());
        }

        //TODO: Add this to db / brewSchema
        Spinner spinner = (Spinner) findViewById(R.id.beerStylespinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.BeerStyles, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

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


    public void onAddStartClick(View aView)
    {
        if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.VIEW)
        {
            //Create Schedule for Brew
            ScheduledBrewSchema sBrew = new ScheduledBrewSchema(brewName.getText().toString(), UserName);
            sBrew.SetScheduledDates(Integer.parseInt(primary.getText().toString()), Integer.parseInt(secondary.getText().toString()), Integer.parseInt(bottle.getText().toString()));
            dbManager.CreateAScheduledBrew(sBrew);

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

        int pf = Integer.parseInt(primary.getText().toString());
        int sf = Integer.parseInt(secondary.getText().toString());
        int bc = Integer.parseInt(bottle.getText().toString());
        int bt = Integer.parseInt(boilTime.getText().toString());

        //Create Brew
        BrewSchema brew = new BrewSchema();
        brew.setBrewName(brewName.getText().toString());
        brew.setUserName(UserName);
        brew.setPrimary(pf);
        brew.setSecondary(sf);
        brew.setBottle(bc);
        brew.setDescription(description.getText().toString());
        brew.setBoilTime(bt);

        //Add Boil additions
        brew.setBoilAdditionlist(cloneList(BrewActivityData.getInstance().getBaArray()));
        //add brew name to boil list
        brew.setListBrewName();

        //remove all additions after submission
        BrewActivityData.getInstance().getBaArray().clear();

        if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.ADD)
        {
            if(!dbManager.CreateABrew(brew))
            {
                Toast.makeText(getApplicationContext(), "Duplicate Brew Name", Toast.LENGTH_LONG).show();
                return;
            }
        }
        else if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.EDIT)
        {
            dbManager.updateABrew(brew);
        }


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
            if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.ADD)
                brewName.setKeyListener(brewNameListener);
            primary.setKeyListener(primaryListener);
            secondary.setKeyListener(secondaryListener);
            bottle.setKeyListener(bottleListener);
            description.setKeyListener(descriptionListener);
            boilTime.setKeyListener(boilTimeListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
