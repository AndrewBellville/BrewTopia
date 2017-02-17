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
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dev.town.small.brewtopia.Brews.BrewActivityData;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.CurrentUser;
import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.GrainsSchema;
import com.dev.town.small.brewtopia.R;

import java.util.List;

public class AddEditViewGrains extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "AddEditViewGrains";

    private InventoryActivityData.DisplayMode AddEditViewState;

    private GrainsSchema grainsSchema;
    private DataBaseManager dbManager;
    private Toolbar toolbar;

    private ScrollView ScrollView;
    private EditText Name;
    private EditText Qty;
    private EditText amount;
    private Spinner editUOfMSpinner;
    private ArrayAdapter<String> UofMAdapter;

    private EditText poundPerGallon;
    private EditText Lovibond;
    private EditText bill;

    private KeyListener NameListener;
    private KeyListener amountListener;
    private KeyListener QtyListener;

    private KeyListener poundPerGallonListener;
    private KeyListener LovibondListener;
    private KeyListener billListener;

    private Button editInventoryButton;
    private Button deleteInventoryButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_view);

        //Add add edit layout default
        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_add_edit_view_grains, null);

        ScrollView = (ScrollView)findViewById(R.id.inventoryScrollView);
        ScrollView.addView(view);

        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Grains");

        Name = (EditText)findViewById(R.id.NameEditText);
        amount = (EditText)findViewById(R.id.amountEditText);
        Qty = (EditText)findViewById(R.id.QtyEditText);
        poundPerGallon = (EditText)findViewById(R.id.poundPerGallonEditText);
        Lovibond = (EditText)findViewById(R.id.lovibondEditText);
        bill = (EditText)findViewById(R.id.billEditText);

        List<String> UOfMs = APPUTILS.UofM;
        UofMAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, UOfMs);
        // Specify the layout to use when the list of choices appears
        UofMAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editUOfMSpinner = (Spinner) findViewById(R.id.UofMSpinner);
        editUOfMSpinner.setAdapter(UofMAdapter);

        NameListener = Name.getKeyListener();
        amountListener = amount.getKeyListener();
        QtyListener = Qty.getKeyListener();
        poundPerGallonListener = poundPerGallon.getKeyListener();
        LovibondListener = Lovibond.getKeyListener();
        billListener = bill.getKeyListener();


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
            grainsSchema = (GrainsSchema) InventoryActivityData.getInstance().getGrainsSchema();
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
        DisplayGrains();
        editInventoryButton.setText("Submit");
        AddEditViewState = InventoryActivityData.DisplayMode.EDIT;

        ToggleFieldEditable(false);
        ToggleFieldEditable(true);
    }

    public void ifView()
    {
        DisplayGrains();
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
        poundPerGallon.setText("");
        Lovibond.setText("");
        bill.setText("");
    }

    private void DisplayGrains()
    {
        Log.e(LOG, "Entering: DisplayGrains");

        //Reset all fields
        Name.setText(grainsSchema.getInventoryName());
        Qty.setText(Integer.toString(grainsSchema.getInvetoryQty()));
        amount.setText(Double.toString(grainsSchema.getAmount()));
        poundPerGallon.setText(Double.toString(grainsSchema.getPoundPerGallon()));
        Lovibond.setText(Double.toString(grainsSchema.getLovibond()));
        bill.setText(Double.toString(grainsSchema.getBill()));

        try
        {
            editUOfMSpinner.setSelection(UofMAdapter.getPosition(grainsSchema.getInventoryUOfM()));
        }
        catch (Exception e)
        {
            editUOfMSpinner.setSelection(0);
        }

    }

    private void validateSubmit() {
        Log.e(LOG, "Entering: validateSubmit");

        if(Name.getText().toString().equals(""))
        {
            Toast.makeText(this, "Blank Data Field", Toast.LENGTH_LONG).show();
            return;
        }

        //Create Hops
        GrainsSchema aGrainsSchema;
        if(grainsSchema == null)
            aGrainsSchema = new GrainsSchema();
        else
            aGrainsSchema = grainsSchema;

        aGrainsSchema.setInventoryId(aGrainsSchema.getInventoryId());
        aGrainsSchema.setInventoryName(Name.getText().toString());
        aGrainsSchema.setUserId(CurrentUser.getInstance().getUser().getUserId());

        double am=0.0;
        int qt=0;

        double ppg=0.0;
        double l=0.0;
        double b=0.0;


        try
        {
            am = Double.parseDouble(amount.getText().toString());
        }
        catch (Exception e){}
        try
        {
            ppg = Double.parseDouble(poundPerGallon.getText().toString());
        }
        catch (Exception e){}
        try
        {
            l = Double.parseDouble(Lovibond.getText().toString());
        }
        catch (Exception e){}
        try
        {
            b = Double.parseDouble(bill.getText().toString());
        }
        catch (Exception e){}
        try
        {
            qt = Integer.parseInt(Qty.getText().toString());
        }
        catch (Exception e){}


        aGrainsSchema.setAmount(am);
        aGrainsSchema.setInvetoryQty(qt);
        aGrainsSchema.setPoundPerGallon(ppg);
        aGrainsSchema.setLovibond(l);
        aGrainsSchema.setBill(b);
        aGrainsSchema.setInventoryUOfM(editUOfMSpinner.getSelectedItem().toString());

        // If we are doing any adding we want to always create the base Inventory record
        if(AddEditViewState == InventoryActivityData.DisplayMode.ADD || AddEditViewState == InventoryActivityData.DisplayMode.BREW_ADD)
        {

            long inventoryId = dbManager.CreateGrain(aGrainsSchema);
            if( inventoryId == -1)// -1 brews failed to create
            {
                Toast.makeText(this, "Create  Failed", Toast.LENGTH_LONG).show();
                return;
            }
            aGrainsSchema.setInventoryId(inventoryId);
        }
        else if(AddEditViewState == InventoryActivityData.DisplayMode.EDIT)
        {
            dbManager.updateGrain(aGrainsSchema);
        }

        // If this was on brew activity add we also want to add this to the brew and the brew already exists
        if(AddEditViewState == InventoryActivityData.DisplayMode.BREW_ADD && BrewActivityData.getInstance().getAddEditViewBrew().getBrewId() >=0)
        {

            aGrainsSchema.setBrewId(BrewActivityData.getInstance().getAddEditViewBrew().getBrewId());
            long inventoryId = dbManager.CreateGrain(aGrainsSchema);
            if( inventoryId == -1)// -1 brews failed to create
            {
                Toast.makeText(this, "Create  Failed", Toast.LENGTH_LONG).show();
                return;
            }
            aGrainsSchema.setInventoryId(inventoryId);
        }
        else if(AddEditViewState == InventoryActivityData.DisplayMode.BREW_ADD && !(BrewActivityData.getInstance().getAddEditViewBrew().getBrewId() >=0))
        {
            // add to Brew activity data to be added when brew is created
            BrewActivityData.getInstance().getBrewInventorySchemaList().add(aGrainsSchema);
        }

        grainsSchema = aGrainsSchema;

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
        dbManager.deleteGrainById(grainsSchema.getInventoryId());
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

            Lovibond.setKeyListener(null);
            Lovibond.setClickable(false);
            Lovibond.setEnabled(false);
            //Lovibond.setFocusable(false);

            poundPerGallon.setKeyListener(null);
            poundPerGallon.setClickable(false);
            poundPerGallon.setEnabled(false);
            //poundPerGallon.setFocusable(false);

            bill.setKeyListener(null);
            bill.setClickable(false);
            bill.setEnabled(false);
            //bill.setFocusable(false);

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

            Lovibond.setKeyListener(LovibondListener);
            Lovibond.setClickable(true);
            Lovibond.setEnabled(true);
            //Lovibond.setFocusable(true);

            poundPerGallon.setKeyListener(poundPerGallonListener);
            poundPerGallon.setClickable(true);
            poundPerGallon.setEnabled(true);
            //poundPerGallon.setFocusable(true);

            bill.setKeyListener(billListener);
            bill.setClickable(true);
            bill.setEnabled(true);
            //bill.setFocusable(true);

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
