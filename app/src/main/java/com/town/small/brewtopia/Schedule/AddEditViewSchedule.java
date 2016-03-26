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
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.town.small.brewtopia.Brews.BrewActivityData;
import com.town.small.brewtopia.DataClass.CurrentUser;
import com.town.small.brewtopia.DataClass.DataBaseManager;
import com.town.small.brewtopia.DataClass.ScheduledBrewSchema;
import com.town.small.brewtopia.R;

public class AddEditViewSchedule extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "AddEditViewSchedule";

    private String  UserName;
    private ScrollView ScrollView;

    private TextView brewName;
    private TextView StartDate;
    private TextView SecondaryAlert;
    private TextView BottleAlert;
    private TextView EndDate;
    private TextView Notes;


    private KeyListener brewNameListener;
    private KeyListener StartDateListener;
    private KeyListener SecondaryAlertListener;
    private KeyListener BottleAlertListener;
    private KeyListener EndDateListener;
    private KeyListener NotesListener;

    private DataBaseManager dbManager;
    private ScheduleActivityData scheduleActivityData;


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


        //getActionBar().hide();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        dbManager = DataBaseManager.getInstance(getApplicationContext());

        UserName = CurrentUser.getInstance().getUser().getUserName();

        scheduleActivityData = ScheduleActivityData.getInstance();

        ToggleFieldEditable(false);
        DisplaySchedule(scheduleActivityData.getScheduledBrewSchema());

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

    public void onEditClick(View aView)
    {

    }
    public void onDeleteClick(View aView)
    {

    }

    private void ClearFields()
    {
        Log.e(LOG, "Entering: ClearFields");
        //Reset all fields
        brewName.setText("");
        StartDate.setText("");
        SecondaryAlert.setText("");
        BottleAlert.setText("");
        EndDate.setText("");
        Notes.setText("");
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
        }
        else
        {
            //addEditButton.setVisibility(View.VISIBLE);
            //if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.ADD)
               brewName.setKeyListener(brewNameListener);

            StartDate.setKeyListener(StartDateListener);
            SecondaryAlert.setKeyListener(SecondaryAlertListener);
            BottleAlert.setKeyListener(BottleAlertListener);
            EndDate.setKeyListener(EndDateListener);
            Notes.setKeyListener(NotesListener);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
