package com.town.small.brewtopia.Brews;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.town.small.brewtopia.DataClass.APPUTILS;
import com.town.small.brewtopia.DataClass.CurrentUser;
import com.town.small.brewtopia.DataBase.DataBaseManager;
import com.town.small.brewtopia.DataClass.ScheduledBrewSchema;
import com.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.List;

public class CompletedBrew extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "CompletedBrew";

    private long  userId;
    private ScrollView ScrollView;

    private EditText brewName;
    private EditText StartDate;
    private EditText SecondaryAlert;
    private EditText BottleAlert;
    private EditText EndDate;
    private EditText Notes;
    private EditText OriginalGravity;
    private EditText FinalGravity;
    private EditText ABV;

    private Spinner colorSpinner;
    private CheckBox dateRollUpCheckBox;

    private DataBaseManager dbManager;
    ScheduledBrewSchema aScheduleSchema;
    ArrayAdapter<String> colorAdapter;
    private Toolbar toolbar;

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

        brewName = (EditText)findViewById(R.id.ScheduleBNameeditText);
        StartDate = (EditText)findViewById(R.id.ScheduleStarteditText);
        SecondaryAlert = (EditText)findViewById(R.id.Schedule2AlerteditText);
        BottleAlert = (EditText)findViewById(R.id.SchduleBottleAlerteditText);
        EndDate = (EditText)findViewById(R.id.ScheduleEndDateeditText);
        Notes = (EditText)findViewById(R.id.ScheduleNoteseditText);
        OriginalGravity = (EditText)findViewById(R.id.ScheduleOGeditText);
        FinalGravity = (EditText)findViewById(R.id.ScheduleFGeditText);
        ABV = (EditText)findViewById(R.id.ScheduleABVeditText);

        colorSpinner = (Spinner) findViewById(R.id.Colorspinner);
        dateRollUpCheckBox = (CheckBox)findViewById(R.id.DateRollUpCheckBox);

        RelativeLayout buttonlayout = (RelativeLayout)findViewById(R.id.ButtonRow);
        buttonlayout.setVisibility(View.GONE);

        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Completed Brew");
        dbManager = DataBaseManager.getInstance(getApplicationContext());

        userId = CurrentUser.getInstance().getUser().getUserId();

        ToggleFieldEditable(false);
        setColorSpinner();

        ifView();

    }

    public void ifView()
    {
        //get brew and display
        aScheduleSchema = dbManager.getNonActiveScheduledScheduleId(BrewActivityData.getInstance().getScheduleId());
        DisplaySchedule(aScheduleSchema);
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
        OriginalGravity.setText(Double.toString(aScheduleSchema.getOG()));
        FinalGravity.setText(Double.toString(aScheduleSchema.getFG()));
        ABV.setText(Double.toString(APPUTILS.GetTruncatedABVPercent(aScheduleSchema.getABV())) +"%" );
    }

    private void setColorSpinner()
    {
        List<String> colors = new ArrayList<String>();
        colors.add("Default");
        colors.add("Blue");

        colorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors); //selected item will look like a spinner set from XML
        // Specify the layout to use when the list of choices appears
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        colorSpinner.setAdapter(colorAdapter);
    }

    private void ToggleFieldEditable(boolean aEditable)
    {
        Log.e(LOG, "Entering: ToggleFieldEditable");

        //addEditButton.setVisibility(View.INVISIBLE);
        //brewName.setKeyListener(null);
        brewName.setClickable(false);
        brewName.setEnabled(false);
        brewName.setFocusable(false);

        //StartDate.setKeyListener(null);
        StartDate.setClickable(false);
        StartDate.setEnabled(false);
        StartDate.setFocusable(false);

        //SecondaryAlert.setKeyListener(null);
        SecondaryAlert.setClickable(false);
        SecondaryAlert.setEnabled(false);
        SecondaryAlert.setFocusable(false);

        //BottleAlert.setKeyListener(null);
        BottleAlert.setClickable(false);
        BottleAlert.setEnabled(false);
        BottleAlert.setFocusable(false);

        //EndDate.setKeyListener(null);
        EndDate.setClickable(false);
        EndDate.setEnabled(false);
        EndDate.setFocusable(false);

       // OriginalGravity.setKeyListener(null);
        OriginalGravity.setClickable(false);
        OriginalGravity.setEnabled(false);
        //OriginalGravity.setFocusable(false);

        //FinalGravity.setKeyListener(null);
        FinalGravity.setClickable(false);
        FinalGravity.setEnabled(false);
        //FinalGravity.setFocusable(false);

        //ABV.setKeyListener(null);
        ABV.setClickable(false);
        ABV.setEnabled(false);
        //ABV.setFocusable(false);

        Notes.setKeyListener(null);
        Notes.setClickable(false);
        Notes.setEnabled(false);
        //Notes.setFocusable(false);

        colorSpinner.setClickable(false);
        colorSpinner.setEnabled(false);

        dateRollUpCheckBox.setClickable(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
