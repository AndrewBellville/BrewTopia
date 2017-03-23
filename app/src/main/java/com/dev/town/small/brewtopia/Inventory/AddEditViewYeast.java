package com.dev.town.small.brewtopia.Inventory;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dev.town.small.brewtopia.Brews.BrewActivityData;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.CurrentUser;
import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.YeastSchema;
import com.dev.town.small.brewtopia.R;

import java.util.List;

public class AddEditViewYeast extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "AddEditViewYeast";

    private InventoryActivityData.DisplayMode AddEditViewState;

    private YeastSchema yeastSchema;
    private DataBaseManager dbManager;
    private Toolbar toolbar;

    private ScrollView ScrollView;
    private EditText Name;
    private EditText Qty;
    private EditText amount;

    private EditText laboratory;
    private EditText type;
    private EditText form;
    private EditText attenuation;
    private EditText flocculation;
    private EditText optimumTempLow;
    private EditText optimumTempHigh;
    private CheckBox starter;
    private Spinner editUOfMSpinner;
    private ArrayAdapter<String> UofMAdapter;


    private KeyListener LaboratoryListener;
    private KeyListener TypeListener;
    private KeyListener FormListener;
    private KeyListener NameListener;
    private KeyListener amountListener;
    private KeyListener QtyListener;

    private KeyListener attenuationListener;
    private KeyListener flocculationListener;
    private KeyListener optimumTempLowListener;
    private KeyListener optimumTempHighListener;


    private Button editInventoryButton;
    private Button deleteInventoryButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_view);

        //Add add edit layout default
        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_add_edit_view_yeast, null);

        ScrollView = (ScrollView)findViewById(R.id.inventoryScrollView);
        ScrollView.addView(view);

        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Yeast");

        Name = (EditText)findViewById(R.id.NameEditText);
        amount = (EditText)findViewById(R.id.amountEditText);
        Qty = (EditText)findViewById(R.id.QtyEditText);

        laboratory = (EditText)findViewById(R.id.LaboratoryEditText);
        type = (EditText)findViewById(R.id.TypeEditText);
        form = (EditText)findViewById(R.id.FormEditText);
        attenuation = (EditText)findViewById(R.id.attenuationEditText);
        flocculation = (EditText)findViewById(R.id.flocculationEditText);
        optimumTempLow = (EditText)findViewById(R.id.optimumTempLowEditText);
        optimumTempHigh = (EditText)findViewById(R.id.optimumTempHighEditText);
        starter = (CheckBox)findViewById(R.id.starterCheckBox);

        List<String> UOfMs = APPUTILS.UofM;
        UofMAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, UOfMs);
        // Specify the layout to use when the list of choices appears
        UofMAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editUOfMSpinner = (Spinner) findViewById(R.id.UofMSpinner);
        editUOfMSpinner.setAdapter(UofMAdapter);

        NameListener = Name.getKeyListener();
        amountListener = amount.getKeyListener();
        QtyListener = Qty.getKeyListener();
        LaboratoryListener = laboratory.getKeyListener();
        TypeListener = type.getKeyListener();
        FormListener = form.getKeyListener();
        attenuationListener = attenuation.getKeyListener();
        flocculationListener = flocculation.getKeyListener();
        optimumTempLowListener = optimumTempLow.getKeyListener();
        optimumTempHighListener = optimumTempHigh.getKeyListener();

        editInventoryButton = (Button)findViewById(R.id.inventoryEditButton);
        deleteInventoryButton = (Button)findViewById(R.id.inventoryDeleteButton);

        AddEditViewState = InventoryActivityData.getInstance().getAddEditViewState();
        dbManager = DataBaseManager.getInstance(getApplicationContext());

        ToggleFieldEditable(false);

        if(AddEditViewState == InventoryActivityData.DisplayMode.ADD )
            ifAdd();
        else if (AddEditViewState == InventoryActivityData.DisplayMode.BREW_ADD )
            ifAddBrew();
        else
        {
            yeastSchema = (YeastSchema) InventoryActivityData.getInstance().getYeastSchema();
            ifView();
        }
    }

    public void ifAdd()
    {
        ClearFields();
        deleteInventoryButton.setVisibility(View.INVISIBLE);
        editInventoryButton.setText("Submit");
        AddEditViewState = InventoryActivityData.DisplayMode.ADD;
        ToggleFieldEditable(true);
    }

    public void ifAddBrew()
    {
        ClearFields();
        deleteInventoryButton.setVisibility(View.INVISIBLE);
        editInventoryButton.setText("Submit");
        AddEditViewState = InventoryActivityData.DisplayMode.BREW_ADD;
        ToggleFieldEditable(true);
    }

    public void ifEdit()
    {
        deleteInventoryButton.setVisibility(View.INVISIBLE);

        //get brew and display
        DisplayYeast();
        editInventoryButton.setText("Submit");
        AddEditViewState = InventoryActivityData.DisplayMode.EDIT;

        ToggleFieldEditable(false);
        ToggleFieldEditable(true);
    }

    public void ifView()
    {
        DisplayYeast();
        editInventoryButton.setText("Edit");
        //Set EditText to not editable and hide button
        AddEditViewState = InventoryActivityData.DisplayMode.VIEW;
        deleteInventoryButton.setVisibility(View.VISIBLE);
        ToggleFieldEditable(false);
    }

    private void ClearFields()
    {
        Name.setText("");
        Qty.setText("");
        amount.setText("");
        attenuation.setText("");
        flocculation.setText("");
        optimumTempLow.setText("");
        optimumTempHigh.setText("");
        laboratory.setText("");
        type.setText("");
        form.setText("");
        starter.setChecked(false);;
    }

    private void DisplayYeast()
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: DisplayYeast");

        //Reset all fields
        Name.setText(yeastSchema.getInventoryName());
        Qty.setText(Integer.toString(yeastSchema.getInvetoryQty()));
        amount.setText(Double.toString(yeastSchema.getAmount()));
        attenuation.setText(Double.toString(yeastSchema.getAttenuation()));
        flocculation.setText(yeastSchema.getFlocculation());
        optimumTempLow.setText(Double.toString(yeastSchema.getOptimumTempLow()));
        optimumTempHigh.setText(Double.toString(yeastSchema.getOptimumTempHigh()));
        starter.setChecked(yeastSchema.getBooleanStarter());

        laboratory.setText(yeastSchema.getLaboratory());
        type.setText(yeastSchema.getType());
        form.setText(yeastSchema.getForm());

        try
        {
            editUOfMSpinner.setSelection(UofMAdapter.getPosition(yeastSchema.getInventoryUOfM()));
        }
        catch (Exception e)
        {
            editUOfMSpinner.setSelection(0);
        }

    }

    private void validateSubmit() {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: validateSubmit");

        if(Name.getText().toString().equals(""))
        {
            Toast.makeText(this, "Blank Data Field", Toast.LENGTH_LONG).show();
            return;
        }

        //Create Hops
        YeastSchema aYeastSchema;
        if(yeastSchema == null)
            aYeastSchema = new YeastSchema();
        else
            aYeastSchema = yeastSchema;

        aYeastSchema.setInventoryId(aYeastSchema.getInventoryId());
        aYeastSchema.setInventoryName(Name.getText().toString());
        aYeastSchema.setUserId(CurrentUser.getInstance().getUser().getUserId());

        double am=0.0;
        int qt=0;

        double a=0.0;
        double otl=0.0;
        double oth=0.0;


        try
        {
            am = Double.parseDouble(amount.getText().toString());
        }
        catch (Exception e){}
        try
        {
            qt = Integer.parseInt(Qty.getText().toString());
        }
        catch (Exception e){}
        try
        {
            a = Double.parseDouble(attenuation.getText().toString());
        }
        catch (Exception e){}
        try
        {
            otl = Double.parseDouble(optimumTempLow.getText().toString());
        }
        catch (Exception e){}
        try
        {
            oth = Double.parseDouble(optimumTempHigh.getText().toString());
        }
        catch (Exception e){}



        aYeastSchema.setAmount(am);
        aYeastSchema.setInvetoryQty(qt);
        aYeastSchema.setAttenuation(a);
        aYeastSchema.setOptimumTempLow(otl);
        aYeastSchema.setOptimumTempHigh(oth);
        aYeastSchema.setFlocculation(flocculation.getText().toString());
        aYeastSchema.setLaboratory(laboratory.getText().toString());
        aYeastSchema.setType(type.getText().toString());
        aYeastSchema.setForm(form.getText().toString());
        aYeastSchema.setBooleanStarter(starter.isChecked());
        aYeastSchema.setInventoryUOfM(editUOfMSpinner.getSelectedItem().toString());


        // If we are doing any adding we want to always create the base Inventory record
        if(AddEditViewState == InventoryActivityData.DisplayMode.ADD || AddEditViewState == InventoryActivityData.DisplayMode.BREW_ADD)
        {

            long inventoryId = dbManager.CreateYeast(aYeastSchema);
            if( inventoryId == 0)// 0 brews failed to create
            {
                Toast.makeText(this, "Create  Failed", Toast.LENGTH_LONG).show();
                return;
            }
            aYeastSchema.setInventoryId(inventoryId);
        }
        else if(AddEditViewState == InventoryActivityData.DisplayMode.EDIT)
        {
            dbManager.updateYeast(aYeastSchema);
        }

        // If this was on brew activity add we also want to add this to the brew and the brew already exists
        if(AddEditViewState == InventoryActivityData.DisplayMode.BREW_ADD && BrewActivityData.getInstance().getAddEditViewBrew().getBrewId() >=0)
        {

            aYeastSchema.setBrewId(BrewActivityData.getInstance().getAddEditViewBrew().getBrewId());
            long inventoryId = dbManager.CreateYeast(aYeastSchema);
            if( inventoryId == 0)// 0 brews failed to create
            {
                Toast.makeText(this, "Create  Failed", Toast.LENGTH_LONG).show();
                return;
            }
            aYeastSchema.setInventoryId(inventoryId);
        }
        else if(AddEditViewState == InventoryActivityData.DisplayMode.BREW_ADD && !(BrewActivityData.getInstance().getAddEditViewBrew().getBrewId() >=0))
        {
            // add to Brew activity data to be added when brew is created
            BrewActivityData.getInstance().getBrewInventorySchemaList().add(aYeastSchema);
        }

        yeastSchema = aYeastSchema;

        ifView();

    }

    public void onEditClick(View aView){

        if(AddEditViewState == InventoryActivityData.DisplayMode.VIEW )
            ifEdit();
        else
            validateSubmit();
    }

    public void onDeleteClick(View aView)
    {
        // delete Inventory
        dbManager.deleteYeastById(yeastSchema.getInventoryId());
        this.finish();
    }

    private void ToggleFieldEditable(boolean aEditable)
    {
        //Reset all fields
        if(!aEditable) {
            //addEditButton.setVisibility(View.INVISIBLE);
            Name.setKeyListener(null);
            Name.setClickable(false);
            Name.setEnabled(false);
            //Name.setFocusable(false);

            amount.setKeyListener(null);
            amount.setClickable(false);
            amount.setEnabled(false);
            //amount.setFocusable(false);

            Qty.setKeyListener(null);
            Qty.setClickable(false);
            Qty.setEnabled(false);
            //Qty.setFocusable(false);

            attenuation.setKeyListener(null);
            attenuation.setClickable(false);
            attenuation.setEnabled(false);
            //attenuation.setFocusable(false);

            flocculation.setKeyListener(null);
            flocculation.setClickable(false);
            flocculation.setEnabled(false);
            //flocculation.setFocusable(false);

            optimumTempLow.setKeyListener(null);
            optimumTempLow.setClickable(false);
            optimumTempLow.setEnabled(false);
            //optimumTempLow.setFocusable(false);

            optimumTempHigh.setKeyListener(null);
            optimumTempHigh.setClickable(false);
            optimumTempHigh.setEnabled(false);
            //optimumTempHigh.setFocusable(false);

            laboratory.setKeyListener(null);
            laboratory.setClickable(false);
            laboratory.setEnabled(false);
            //optimumTempLow.setFocusable(false);

            type.setKeyListener(null);
            type.setClickable(false);
            type.setEnabled(false);
            //optimumTempLow.setFocusable(false);

            form.setKeyListener(null);
            form.setClickable(false);
            form.setEnabled(false);
            //optimumTempLow.setFocusable(false);

            starter.setClickable(false);

            editUOfMSpinner.setClickable(false);
            editUOfMSpinner.setEnabled(false);
        }
        else
        {
            //addEditButton.setVisibility(View.INVISIBLE);
            Name.setKeyListener(NameListener);
            Name.setClickable(true);
            Name.setEnabled(true);
            //Name.setFocusable(true);

            amount.setKeyListener(amountListener);
            amount.setClickable(true);
            amount.setEnabled(true);
            //amount.setFocusable(true);

            Qty.setKeyListener(QtyListener);
            Qty.setClickable(true);
            Qty.setEnabled(true);
            //Qty.setFocusable(true);

            attenuation.setKeyListener(attenuationListener);
            attenuation.setClickable(true);
            attenuation.setEnabled(true);
            //attenuation.setFocusable(true);

            flocculation.setKeyListener(flocculationListener);
            flocculation.setClickable(true);
            flocculation.setEnabled(true);
            //flocculation.setFocusable(true);

            optimumTempLow.setKeyListener(optimumTempLowListener);
            optimumTempLow.setClickable(true);
            optimumTempLow.setEnabled(true);
            //optimumTempLow.setFocusable(true);

            optimumTempHigh.setKeyListener(optimumTempHighListener);
            optimumTempHigh.setClickable(true);
            optimumTempHigh.setEnabled(true);
            //optimumTempHigh.setFocusable(true);

            laboratory.setKeyListener(LaboratoryListener);
            laboratory.setClickable(true);
            laboratory.setEnabled(true);
            //optimumTempLow.setFocusable(false);

            type.setKeyListener(TypeListener);
            type.setClickable(true);
            type.setEnabled(true);
            //optimumTempLow.setFocusable(false);

            form.setKeyListener(FormListener);
            form.setClickable(true);
            form.setEnabled(true);
            //optimumTempLow.setFocusable(false);

            starter.setClickable(true);

            editUOfMSpinner.setClickable(true);
            editUOfMSpinner.setEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
