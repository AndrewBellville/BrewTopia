package com.town.small.brewtopia.Brews;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.town.small.brewtopia.R;
import com.town.small.brewtopia.DataClass.*;

public class CreateBrew extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "CreateBrew";

    private TextView brewName;
    private TextView primary;
    private TextView secondary;
    private TextView bottle;
    private TextView description;
    private TextView errorText;
    private String userName;
    private DataBaseManager dbManager;
    private CurrentUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_brew);
        Log.e(LOG, "Entering: onCreate");
        //getActionBar().hide();
        currentUser = CurrentUser.getInstance();
        userName = currentUser.getUser().getUserName();
        dbManager = DataBaseManager.getInstance(getApplicationContext());

        brewName = (TextView)findViewById(R.id.editTextBrewName);
        primary = (TextView)findViewById(R.id.editTextPrimary);
        secondary = (TextView)findViewById(R.id.editTextSecondary);
        bottle = (TextView)findViewById(R.id.editTextBottle);
        description = (TextView)findViewById(R.id.editTextDescription);
        errorText = (TextView)findViewById(R.id.ErrorTextView);
        ResetFields();
    }


    public void onSubmitClick(View aView)
    {
        Log.e(LOG, "Entering: onSubmitClick");



        if(brewName.getText().toString().equals(""))
        {
            errorText.setText("Blank Data Field");
            return;
        }

        int pf = Integer.parseInt(primary.getText().toString());
        int sf = Integer.parseInt(secondary.getText().toString());
        int bc = Integer.parseInt(bottle.getText().toString());

        //Create Brew
        BrewSchema brew = new BrewSchema();
        brew.setBrewName(brewName.getText().toString());
        brew.setPrimary(pf);
        brew.setSecondary(sf);
        brew.setBottle(bc);
        brew.setDescription(description.getText().toString());

        if(!dbManager.CreateABrew(brew))
        {
            errorText.setText("Duplicate Object Name");
            return;
        }

        this.finish();
    }

    private void ResetFields()
    {
        Log.e(LOG, "Entering: ResetFields");
        //Reset all fields
        brewName.setText("");
        primary.setText("");
        secondary.setText("");
        bottle.setText("");
        description.setText("");
    }
}
