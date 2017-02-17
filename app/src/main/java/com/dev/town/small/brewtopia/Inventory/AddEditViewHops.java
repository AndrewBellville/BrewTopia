package com.dev.town.small.brewtopia.Inventory;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dev.town.small.brewtopia.Brews.BrewActivityData;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.CurrentUser;
import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.HopsSchema;
import com.dev.town.small.brewtopia.R;

import java.util.List;

public class AddEditViewHops extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "AddEditViewHops";

    private InventoryActivityData.DisplayMode AddEditViewState;

    private HopsSchema hopsSchema;
    private DataBaseManager dbManager;
    private Toolbar toolbar;

    private ScrollView ScrollView;
    private EditText hopsName;
    private EditText hopsQty;
    private EditText amount;
    private EditText type;
    private EditText AA;
    private EditText use;
    private EditText time;
    private EditText IBU;
    private Spinner editUOfMSpinner;
    private ArrayAdapter<String> UofMAdapter;

    private KeyListener hopsNameListener;
    private KeyListener amountListener;
    private KeyListener typeListener;
    private KeyListener AAListener;
    private KeyListener useListener;
    private KeyListener timeListener;
    private KeyListener IBUListener;
    private KeyListener hopsQtyListener;

    private Button editInventoryButton;
    private Button deleteInventoryButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_view);

        //Add add edit layout default
        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_add_edit_view_hops, null);

        ScrollView = (ScrollView)findViewById(R.id.inventoryScrollView);
        ScrollView.addView(view);

        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Hops");

        hopsName = (EditText)findViewById(R.id.hopsNameEditText);
        amount = (EditText)findViewById(R.id.amountEditText);
        type = (EditText)findViewById(R.id.typeEditText);
        AA = (EditText)findViewById(R.id.AAEditText);
        use = (EditText)findViewById(R.id.useEditText);
        time = (EditText)findViewById(R.id.timeEditText);
        IBU = (EditText)findViewById(R.id.IBUEditText);
        hopsQty = (EditText)findViewById(R.id.hopsQtyEditText);

        List<String> UOfMs = APPUTILS.UofM;
        UofMAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, UOfMs);
        // Specify the layout to use when the list of choices appears
        UofMAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editUOfMSpinner = (Spinner) findViewById(R.id.UofMSpinner);
        editUOfMSpinner.setAdapter(UofMAdapter);

        hopsNameListener = hopsName.getKeyListener();
        amountListener = amount.getKeyListener();
        typeListener = type.getKeyListener();
        AAListener = AA.getKeyListener();
        useListener = use.getKeyListener();
        timeListener = time.getKeyListener();
        IBUListener = IBU.getKeyListener();
        hopsQtyListener = hopsQty.getKeyListener();

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
            hopsSchema = (HopsSchema) InventoryActivityData.getInstance().getHopsSchema();
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
        DisplayHops();
        editInventoryButton.setText("Submit");
        AddEditViewState = InventoryActivityData.DisplayMode.EDIT;

        ToggleFieldEditable(false);
        ToggleFieldEditable(true);
    }

    public void ifView()
    {
        DisplayHops();
        editInventoryButton.setText("Edit");
        //Set EditText to not editable and hide button
        AddEditViewState = InventoryActivityData.DisplayMode.VIEW;
        deleteInventoryButton.setVisibility(View.VISIBLE);
        ToggleFieldEditable(false);
    }

    private void ClearFields()
    {
        hopsName.setText("");
        hopsQty.setText("");
        amount.setText("");
        type.setText("");
        AA.setText("");
        use.setText("");
        time.setText("");
        IBU.setText("");
    }

    private void DisplayHops()
    {
        Log.e(LOG, "Entering: DisplayHops");

        //Reset all fields
        hopsName.setText(hopsSchema.getInventoryName());
        hopsQty.setText(Integer.toString(hopsSchema.getInvetoryQty()));
        amount.setText(Double.toString(hopsSchema.getAmount()));
        type.setText(hopsSchema.getType());
        AA.setText(Double.toString(hopsSchema.getAA()));
        use.setText(hopsSchema.getUse());
        time.setText(Integer.toString(hopsSchema.getTime()));
        IBU.setText(Double.toString(hopsSchema.getIBU()));

        try
        {
            editUOfMSpinner.setSelection(UofMAdapter.getPosition(hopsSchema.getInventoryUOfM()));
        }
        catch (Exception e)
        {
            editUOfMSpinner.setSelection(0);
        }
    }

    private void validateSubmit() {
        Log.e(LOG, "Entering: validateSubmit");

        if(hopsName.getText().toString().equals(""))
        {
            Toast.makeText(this, "Blank Data Field", Toast.LENGTH_LONG).show();
            return;
        }

        //Create Hops
        HopsSchema aHops;
        if(hopsSchema == null)
            aHops = new HopsSchema();
        else
            aHops = hopsSchema;

        aHops.setInventoryId(aHops.getInventoryId());
        aHops.setInventoryName(hopsName.getText().toString());
        aHops.setUserId(CurrentUser.getInstance().getUser().getUserId());

        double am=0.0;
        double aa=0.0;
        double ibu=0.0;
        int t=0;
        int qt=0;

        try
        {
            am = Double.parseDouble(amount.getText().toString());
        }
        catch (Exception e){}
        try
        {
            aa = Double.parseDouble(AA.getText().toString());
        }
        catch (Exception e){}
        try
        {
            ibu = Double.parseDouble(IBU.getText().toString());
        }
        catch (Exception e){}
        try
        {
            t = Integer.parseInt(time.getText().toString());
        }
        catch (Exception e){}
        try
        {
            qt = Integer.parseInt(hopsQty.getText().toString());
        }
        catch (Exception e){}


        aHops.setAmount(am);
        aHops.setAA(aa);
        aHops.setIBU(ibu);
        aHops.setTime(t);
        aHops.setInvetoryQty(qt);
        aHops.setInventoryUOfM(editUOfMSpinner.getSelectedItem().toString());

        aHops.setUse(use.getText().toString());
        aHops.setType(type.getText().toString());

        // If we are doing any adding we want to always create the base Inventory record
        if(AddEditViewState == InventoryActivityData.DisplayMode.ADD || AddEditViewState == InventoryActivityData.DisplayMode.BREW_ADD)
        {

            long inventoryId = dbManager.CreateHops(aHops);
            if( inventoryId == -1)// -1 brews failed to create
            {
                Toast.makeText(this, "Create  Failed", Toast.LENGTH_LONG).show();
                return;
            }
            aHops.setInventoryId(inventoryId);
        }
        else if(AddEditViewState == InventoryActivityData.DisplayMode.EDIT)
        {
            dbManager.updateHops(aHops);
        }

        // If this was on brew activity add we also want to add this to the brew and the brew already exists
        if(AddEditViewState == InventoryActivityData.DisplayMode.BREW_ADD && BrewActivityData.getInstance().getAddEditViewBrew().getBrewId() >=0)
        {

            aHops.setBrewId(BrewActivityData.getInstance().getAddEditViewBrew().getBrewId());
            long inventoryId = dbManager.CreateHops(aHops);
            if( inventoryId == -1)// -1 brews failed to create
            {
                Toast.makeText(this, "Create  Failed", Toast.LENGTH_LONG).show();
                return;
            }
            aHops.setInventoryId(inventoryId);
        }
        else if(AddEditViewState == InventoryActivityData.DisplayMode.BREW_ADD && !(BrewActivityData.getInstance().getAddEditViewBrew().getBrewId() >=0))
        {
            // add to Brew activity data to be added when brew is created
            BrewActivityData.getInstance().getBrewInventorySchemaList().add(aHops);
        }

        hopsSchema = aHops;

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
        dbManager.deleteHopsById(hopsSchema.getInventoryId());
        this.finish();
    }

    private void ToggleFieldEditable(boolean aEditable)
    {
        //Reset all fields
        if(!aEditable) {
            //addEditButton.setVisibility(View.INVISIBLE);
            hopsName.setKeyListener(null);
            hopsName.setClickable(false);
            hopsName.setEnabled(false);
            //hopsName.setFocusable(false);

            amount.setKeyListener(null);
            amount.setClickable(false);
            amount.setEnabled(false);
            //amount.setFocusable(false);

            type.setKeyListener(null);
            type.setClickable(false);
            type.setEnabled(false);
            //type.setFocusable(false);

            AA.setKeyListener(null);
            AA.setClickable(false);
            AA.setEnabled(false);
            //AA.setFocusable(false);

            use.setKeyListener(null);
            use.setClickable(false);
            use.setEnabled(false);
            //use.setFocusable(false);

            time.setKeyListener(null);
            time.setClickable(false);
            time.setEnabled(false);
            //time.setFocusable(false);

            IBU.setKeyListener(null);
            IBU.setClickable(false);
            IBU.setEnabled(false);
            //IBU.setFocusable(false);

            hopsQty.setKeyListener(null);
            hopsQty.setClickable(false);
            hopsQty.setEnabled(false);
            //hopsQty.setFocusable(false);

            editUOfMSpinner.setClickable(false);
            editUOfMSpinner.setEnabled(false);
        }
        else
        {
            //addEditButton.setVisibility(View.VISIBLE);
            hopsName.setKeyListener(hopsNameListener);
            hopsName.setClickable(true);
            hopsName.setEnabled(true);
            //hopsName.setFocusable(true);

            amount.setKeyListener(amountListener);
            amount.setClickable(true);
            amount.setEnabled(true);
            //amount.setFocusable(true);

            type.setKeyListener(typeListener);
            type.setClickable(true);
            type.setEnabled(true);
            //type.setFocusable(true);

            AA.setKeyListener(AAListener);
            AA.setClickable(true);
            AA.setEnabled(true);
            //AA.setFocusable(true);

            use.setKeyListener(useListener);
            use.setClickable(true);
            use.setEnabled(true);
            //use.setFocusable(true);

            time.setKeyListener(timeListener);
            time.setClickable(true);
            time.setEnabled(true);
            //time.setFocusable(true);

            IBU.setKeyListener(IBUListener);
            IBU.setClickable(true);
            IBU.setEnabled(true);
            //IBU.setFocusable(true);

            hopsQty.setKeyListener(hopsQtyListener);
            hopsQty.setClickable(true);
            hopsQty.setEnabled(true);
            //hopsQty.setFocusable(true);

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
