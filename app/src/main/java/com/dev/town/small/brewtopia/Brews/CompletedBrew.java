package com.dev.town.small.brewtopia.Brews;

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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.CurrentUser;
import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.ScheduledBrewSchema;
import com.dev.town.small.brewtopia.R;
import com.dev.town.small.brewtopia.Schedule.CustomSEListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CompletedBrew extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "CompletedBrew";

    //State for AddEditView Brew and Brew Name if Edit/View
    public enum DisplayMode {
        ADD, EDIT, VIEW
    };
    private DisplayMode AddEditViewState = DisplayMode.VIEW; // STATES: Add, Edit, View

    private long  userId;
    private ScrollView ScrollView;

    private Button editScheduleButton;
    private Button deleteScheduleButton;

    private EditText brewName;
    private EditText StartDate;
    private EditText Notes;
    private EditText OriginalGravity;
    private EditText FinalGravity;
    private EditText ABV;
    private ListView ScheduleEventsListView;
    private KeyListener NotesListener;

    private Spinner colorSpinner;
    private CheckBox dateRollUpCheckBox;
    private CheckBox usingStater;

    private DataBaseManager dbManager;
    ScheduledBrewSchema aScheduleSchema;
    ArrayAdapter<String> colorAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onCreate");
        setContentView(R.layout.schedule_view);

        //Add add edit layout default
        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_add_edit_view_schedule, null);

        ScrollView = (ScrollView)findViewById(R.id.ScheduleScrollView);
        ScrollView.addView(view);

        brewName = (EditText)findViewById(R.id.ScheduleBNameEditText);
        StartDate = (EditText)findViewById(R.id.ScheduleStarteditText);
        Notes = (EditText)findViewById(R.id.ScheduleNoteseditText);
        OriginalGravity = (EditText)findViewById(R.id.ScheduleOGeditText);
        FinalGravity = (EditText)findViewById(R.id.ScheduleFGeditText);
        ABV = (EditText)findViewById(R.id.ScheduleABVeditText);

        ScheduleEventsListView = (ListView)findViewById(R.id.ScheduleEvents);

        colorSpinner = (Spinner) findViewById(R.id.Colorspinner);
        dateRollUpCheckBox = (CheckBox)findViewById(R.id.DateRollUpCheckBox);
        usingStater = (CheckBox)findViewById(R.id.HasStaterCheckBox);

        NotesListener = Notes.getKeyListener();

        dbManager = DataBaseManager.getInstance(getApplicationContext());

        userId = CurrentUser.getInstance().getUser().getUserId();


        editScheduleButton = (Button)findViewById(R.id.EditScheduleButton);
        editScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifEdit();
            }
        });
        deleteScheduleButton = (Button)findViewById(R.id.DeleteButton);
        deleteScheduleButton.setVisibility(View.GONE);

        Button addScheduleEventButton = (Button) view.findViewById(R.id.AddEventButton);
        addScheduleEventButton.setVisibility(View.GONE);

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

    private void DisplaySchedule(ScheduledBrewSchema aScheduleSchema)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: DisplaySchedule");
        //Reset all fields
        brewName.setText(aScheduleSchema.getBrewName());
        StartDate.setText(aScheduleSchema.getStartDate());
        Notes.setText(aScheduleSchema.getNotes());
        OriginalGravity.setText(Double.toString(aScheduleSchema.getOG()));
        FinalGravity.setText(Double.toString(aScheduleSchema.getFG()));
        ABV.setText(Double.toString(APPUTILS.GetTruncatedABVPercent(aScheduleSchema.getABV())) +"%" );
        usingStater.setChecked(aScheduleSchema.getBooleanHasStarter());

        CustomSEListAdapter adapter = new CustomSEListAdapter(aScheduleSchema.getScheduledEventSchemaList(), getApplicationContext());
        ScheduleEventsListView.setAdapter(adapter);
        APPUTILS.setListViewHeightBasedOnChildren(ScheduleEventsListView);
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

    private void validateSubmit()
    {
        //update only notes
        aScheduleSchema.setNotes(Notes.getText().toString());

        dbManager.updateScheduledBrew(aScheduleSchema);

        ifView();
    }

    public void onEditClick(View aView){ifEdit();}

    private void ToggleFieldEditable(boolean aEditable)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: ToggleFieldEditable");

        //addEditButton.setVisibility(View.INVISIBLE);
        //brewName.setKeyListener(null);
        brewName.setClickable(false);
        brewName.setEnabled(false);
        brewName.setFocusable(false);

        //StartDate.setKeyListener(null);
        StartDate.setClickable(false);
        StartDate.setEnabled(false);
        StartDate.setFocusable(false);

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

        if(aEditable)
        {
            Notes.setKeyListener(NotesListener);
            Notes.setClickable(true);
            Notes.setEnabled(true);
            //Notes.setFocusable(false);
        }
        else
        {
            Notes.setKeyListener(null);
            Notes.setClickable(false);
            Notes.setEnabled(false);
            //Notes.setFocusable(false);
        }

        colorSpinner.setClickable(false);
        colorSpinner.setEnabled(false);

        dateRollUpCheckBox.setClickable(false);
        usingStater.setClickable(false);

        ScheduleEventsListView.setClickable(false);
        ScheduleEventsListView.setEnabled(false);
        ScheduleEventsListView.setFocusable(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
