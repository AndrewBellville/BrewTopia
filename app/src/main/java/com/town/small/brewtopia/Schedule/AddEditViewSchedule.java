package com.town.small.brewtopia.Schedule;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.town.small.brewtopia.Brews.BrewActivityData;
import com.town.small.brewtopia.DataClass.BrewStyleSchema;
import com.town.small.brewtopia.DataClass.CurrentUser;
import com.town.small.brewtopia.DataClass.DataBaseManager;
import com.town.small.brewtopia.DataClass.ScheduledBrewSchema;
import com.town.small.brewtopia.R;

import java.util.List;

public class AddEditViewSchedule extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "AddEditViewSchedule";

    private String  UserName;
    private ScrollView ScrollView;

    //State for AddEditView Brew and Brew Name if Edit/View
    public enum DisplayMode {
        ADD, EDIT, VIEW
    };
    private DisplayMode AddEditViewState = DisplayMode.VIEW; // STATES: Add, Edit, View

    private TextView brewName;
    private TextView StartDate;
    private TextView SecondaryAlert;
    private TextView BottleAlert;
    private TextView EndDate;
    private TextView Notes;
    private Button editScheduleButton;
    private Spinner colorSpinner;


    private KeyListener brewNameListener;
    private KeyListener StartDateListener;
    private KeyListener SecondaryAlertListener;
    private KeyListener BottleAlertListener;
    private KeyListener EndDateListener;
    private KeyListener NotesListener;

    private DataBaseManager dbManager;
    private ScheduleActivityData scheduleActivityData;
    ScheduledBrewSchema aScheduleSchema;
    ArrayAdapter<String> colorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG, "Entering: onCreate");
        setContentView(R.layout.schedule_view);

        //Add add edit layout default
        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_add_edit_view_schedule, null);

        ScrollView = (ScrollView)findViewById(R.id.ScheduleScrollView);
        ScrollView.addView(view);


        brewName = (TextView)findViewById(R.id.ScheduleBNameeditText);
        StartDate = (TextView)findViewById(R.id.ScheduleStarteditText);
        SecondaryAlert = (TextView)findViewById(R.id.Schedule2AlerteditText);
        BottleAlert = (TextView)findViewById(R.id.SchduleBottleAlerteditText);
        EndDate = (TextView)findViewById(R.id.ScheduleEndDateeditText);
        Notes = (TextView)findViewById(R.id.ScheduleNoteseditText);
        colorSpinner = (Spinner) findViewById(R.id.Colorspinner);

        editScheduleButton = (Button)findViewById(R.id.EditScheduleButton);

        brewNameListener = brewName.getKeyListener();
        StartDateListener = StartDate.getKeyListener();
        SecondaryAlertListener = SecondaryAlert.getKeyListener();
        BottleAlertListener = BottleAlert.getKeyListener();
        EndDateListener = EndDate.getKeyListener();
        NotesListener = Notes.getKeyListener();


        //getActionBar().hide();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Schedule");
        dbManager = DataBaseManager.getInstance(getApplicationContext());

        UserName = CurrentUser.getInstance().getUser().getUserName();

        scheduleActivityData = ScheduleActivityData.getInstance();
        aScheduleSchema = scheduleActivityData.getScheduledBrewSchema();

        ToggleFieldEditable(false);
        setColorSpinner();

        ifView();

    }

    public void ifAdd()
    {

    }

    public void ifEdit()
    {
        if(AddEditViewState != DisplayMode.EDIT )
        {
            editScheduleButton.setText("Submit");
            AddEditViewState = DisplayMode.EDIT;
            ToggleFieldEditable(true);
        }
        else{
            validateSubmit();
        }
    }

    public void ifView()
    {
        //get brew and display
        DisplaySchedule(aScheduleSchema);
        editScheduleButton.setText("Edit Schedule");
        //Set EditText to not editable and hide button
        AddEditViewState = DisplayMode.VIEW;
        ToggleFieldEditable(false);

    }

    private void DisplaySchedule(ScheduledBrewSchema aScheduleSchema)
    {
        Log.e(LOG, "Entering: DisplaySchedule");
        //Reset all fields
        brewName.setText(aScheduleSchema.getBrewName());
        StartDate.setText(aScheduleSchema.getStartDate());
        SecondaryAlert.setText(aScheduleSchema.getAlertSecondaryDate());
        BottleAlert.setText(aScheduleSchema.getAlertBottleDate());
        EndDate.setText(aScheduleSchema.getEndBrewDate());
        Notes.setText(aScheduleSchema.getNotes());
    }

    private void setColorSpinner()
    {
        int MAX_COLORS = 2;
        String[] colors = new String[MAX_COLORS];

        colors[0]= "Default";
        colors[1]= "Blue";

        colorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors); //selected item will look like a spinner set from XML
        // Specify the layout to use when the list of choices appears
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        colorSpinner.setAdapter(colorAdapter);
    }

    private void validateSubmit()
    {
        Log.e(LOG, "Entering: validateSubmit");

        //Create Brew schedule
        ScheduledBrewSchema sbrew = new ScheduledBrewSchema();
        sbrew.setBrewName(brewName.getText().toString());
        sbrew.setUserName(UserName);
        sbrew.setStartDate(StartDate.getText().toString());
        sbrew.setAlertSecondaryDate(SecondaryAlert.getText().toString());
        sbrew.setAlertBottleDate(BottleAlert.getText().toString());
        sbrew.setEndBrewDate(EndDate.getText().toString());

        if(colorSpinner.getSelectedItem().toString() == "Blue")
            sbrew.setColor("#0000FF");
        else
            sbrew.setColor(aScheduleSchema.getColor());


        sbrew.setNotes(Notes.getText().toString());
        sbrew.setActive(1);


        aScheduleSchema = sbrew;

        dbManager.updateScheduledBrew(aScheduleSchema);

        ifView();
    }

    public void onEditClick(View aView){ifEdit();}

    public void onDeleteClick(View aView)
    {
        dbManager.deleteBrewScheduled(aScheduleSchema.getBrewName(),UserName,aScheduleSchema.getStartDate());
        this.finish();
    }

    private void ToggleFieldEditable(boolean aEditable)
    {
        Log.e(LOG, "Entering: ToggleFieldEditable");
        //Reset all fields
        if(!aEditable) {
            //addEditButton.setVisibility(View.INVISIBLE);
            brewName.setKeyListener(null);
            StartDate.setKeyListener(null);
            SecondaryAlert.setKeyListener(null);
            BottleAlert.setKeyListener(null);
            EndDate.setKeyListener(null);
            Notes.setKeyListener(null);
            colorSpinner.setClickable(false);
        }
        else
        {
            //addEditButton.setVisibility(View.VISIBLE);
            //if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.ADD)
            //   brewName.setKeyListener(brewNameListener);

            //StartDate.setKeyListener(StartDateListener);
            //SecondaryAlert.setKeyListener(SecondaryAlertListener);
            //BottleAlert.setKeyListener(BottleAlertListener);
            //EndDate.setKeyListener(EndDateListener);
            Notes.setKeyListener(NotesListener);
            colorSpinner.setClickable(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
