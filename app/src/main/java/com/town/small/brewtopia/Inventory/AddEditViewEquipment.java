package com.town.small.brewtopia.Inventory;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.town.small.brewtopia.Brews.BrewActivityData;
import com.town.small.brewtopia.DataClass.CurrentUser;
import com.town.small.brewtopia.DataClass.DataBaseManager;
import com.town.small.brewtopia.DataClass.EquipmentSchema;
import com.town.small.brewtopia.DataClass.FermentablesSchema;
import com.town.small.brewtopia.R;

public class AddEditViewEquipment extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "AddEditViewEquipment";

    private InventoryActivityData.DisplayMode AddEditViewState;

    private EquipmentSchema equipmentSchema;
    private DataBaseManager dbManager;
    private Toolbar toolbar;

    private ScrollView ScrollView;
    private EditText Name;
    private EditText Qty;
    private EditText amount;

    private KeyListener NameListener;
    private KeyListener amountListener;
    private KeyListener QtyListener;


    private Button editInventoryButton;
    private Button deleteInventoryButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_view);

        //Add add edit layout default
        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_add_edit_view_equipment, null);

        ScrollView = (ScrollView)findViewById(R.id.inventoryScrollView);
        ScrollView.addView(view);

        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Equipment");

        Name = (EditText)findViewById(R.id.NameEditText);
        amount = (EditText)findViewById(R.id.amountEditText);
        Qty = (EditText)findViewById(R.id.QtyEditText);

        NameListener = Name.getKeyListener();
        amountListener = amount.getKeyListener();
        QtyListener = Qty.getKeyListener();

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
            equipmentSchema = (EquipmentSchema) InventoryActivityData.getInstance().getEquipmentSchema();
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
        DisplayEquipment();
        editInventoryButton.setText("Submit");
        AddEditViewState = InventoryActivityData.DisplayMode.EDIT;

        ToggleFieldEditable(false);
        ToggleFieldEditable(true);
    }

    public void ifView()
    {
        DisplayEquipment();
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
    }

    private void DisplayEquipment()
    {
        Log.e(LOG, "Entering: DisplayEquipment");

        //Reset all fields
        Name.setText(equipmentSchema.getInventoryName());
        Qty.setText(Integer.toString(equipmentSchema.getInvetoryQty()));
        amount.setText(Double.toString(equipmentSchema.getAmount()));
    }

    private void validateSubmit() {
        Log.e(LOG, "Entering: validateSubmit");

        if(Name.getText().toString().equals(""))
        {
            Toast.makeText(this, "Blank Data Field", Toast.LENGTH_LONG).show();
            return;
        }

        //Create Hops
        EquipmentSchema aEquipmentSchema;
        if(equipmentSchema == null)
            aEquipmentSchema = new EquipmentSchema();
        else
            aEquipmentSchema = equipmentSchema;

        aEquipmentSchema.setInventoryId(aEquipmentSchema.getInventoryId());
        aEquipmentSchema.setInventoryName(Name.getText().toString());
        aEquipmentSchema.setUserId(CurrentUser.getInstance().getUser().getUserId());

        double am=0.0;
        int qt=0;

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


        aEquipmentSchema.setAmount(am);
        aEquipmentSchema.setInvetoryQty(qt);

        // If we are doing any adding we want to always create the base Inventory record
        if(AddEditViewState == InventoryActivityData.DisplayMode.ADD || AddEditViewState == InventoryActivityData.DisplayMode.BREW_ADD)
        {

            long inventoryId = dbManager.CreateEquipment(aEquipmentSchema);
            if( inventoryId == 0)// 0 brews failed to create
            {
                Toast.makeText(this, "Create  Failed", Toast.LENGTH_LONG).show();
                return;
            }
            aEquipmentSchema.setInventoryId(inventoryId);
        }
        else if(AddEditViewState == InventoryActivityData.DisplayMode.EDIT)
        {
            dbManager.updateEquipment(aEquipmentSchema);
        }

        // If this was a brew add we also want to add this to the brew
        if(AddEditViewState == InventoryActivityData.DisplayMode.BREW_ADD)
        {

            aEquipmentSchema.setBrewId(BrewActivityData.getInstance().getAddEditViewBrew().getBrewId());
            long inventoryId = dbManager.CreateEquipment(aEquipmentSchema);
            if( inventoryId == 0)// 0 brews failed to create
            {
                Toast.makeText(this, "Create  Failed", Toast.LENGTH_LONG).show();
                return;
            }
            aEquipmentSchema.setInventoryId(inventoryId);
        }

        equipmentSchema = aEquipmentSchema;

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
        dbManager.deleteEquipmentById(equipmentSchema.getInventoryId());
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

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
