package com.town.small.brewtopia.Inventory;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.town.small.brewtopia.DataClass.DataBaseManager;
import com.town.small.brewtopia.DataClass.HopsSchema;
import com.town.small.brewtopia.R;

public class AddEditViewHops extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "AddEditViewHops";

    private InventoryActivityData.DisplayMode AddEditViewState;

    private HopsSchema hopsSchema;
    private DataBaseManager dbManager;
    private Toolbar toolbar;

    private ScrollView ScrollView;
    private EditText hopsName;
    private EditText amount;
    private EditText type;
    private EditText AA;
    private EditText use;
    private EditText time;
    private EditText IBU;

    private KeyListener hopsNameListener;
    private KeyListener amountListener;
    private KeyListener typeListener;
    private KeyListener AAListener;
    private KeyListener useListener;
    private KeyListener timeListener;
    private KeyListener IBUListener;

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

        hopsNameListener = hopsName.getKeyListener();
        amountListener = amount.getKeyListener();
        typeListener = type.getKeyListener();
        AAListener = AA.getKeyListener();
        useListener = use.getKeyListener();
        timeListener = time.getKeyListener();
        IBUListener = IBU.getKeyListener();

        editInventoryButton = (Button)findViewById(R.id.inventoryEditButton);
        deleteInventoryButton = (Button)findViewById(R.id.inventoryDeleteButton);

        AddEditViewState = InventoryActivityData.getInstance().getAddEditViewState();
        dbManager = DataBaseManager.getInstance(getApplicationContext());

        ToggleFieldEditable(false);

        if(AddEditViewState == InventoryActivityData.DisplayMode.ADD)
        {
            ifAdd();
        }
        else
        {
            hopsSchema = InventoryActivityData.getInstance().getHopsSchema();
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

    public void ifEdit()
    {
        deleteInventoryButton.setVisibility(View.VISIBLE);

        if(AddEditViewState != InventoryActivityData.DisplayMode.EDIT )
        {
            editInventoryButton.setText("Submit");
            AddEditViewState = InventoryActivityData.DisplayMode.EDIT;
            ToggleFieldEditable(true);
        }
        else{
            validateSubmit();
        }
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
        amount.setText(Double.toString(hopsSchema.getAmount()));
        type.setText(hopsSchema.getType());
        AA.setText(hopsSchema.getAA());
        use.setText(hopsSchema.getUse());
        time.setText(Integer.toString(hopsSchema.getTime()));
        IBU.setText(Double.toString(hopsSchema.getIBU()));
    }

    private void validateSubmit() {
        Log.e(LOG, "Entering: validateSubmit");


    }

    public void onEditClick(View aView){ifEdit();}

    public void onDeleteClick(View aView)
    {
        // delete Inventory
        //dbManager.deleteBrewScheduledById(aScheduleSchema.getScheduleId());
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
            //OriginalGravity.setFocusable(false);

            IBU.setKeyListener(null);
            IBU.setClickable(false);
            IBU.setEnabled(false);
            //FinalGravity.setFocusable(false);
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
            //OriginalGravity.setFocusable(true);

            IBU.setKeyListener(IBUListener);
            IBU.setClickable(true);
            IBU.setEnabled(true);
            //FinalGravity.setFocusable(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
