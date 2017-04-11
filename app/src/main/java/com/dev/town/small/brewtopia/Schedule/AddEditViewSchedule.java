package com.dev.town.small.brewtopia.Schedule;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.CurrentUser;
import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.ScheduledBrewSchema;
import com.dev.town.small.brewtopia.DataClass.ScheduledEventSchema;
import com.dev.town.small.brewtopia.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEditViewSchedule extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "AddEditViewSchedule";

    private long  userId;
    private ScrollView ScrollView;

    //State for AddEditView Brew and Brew Name if Edit/View
    public enum DisplayMode {
        ADD, EDIT, VIEW
    };
    private DisplayMode AddEditViewState = DisplayMode.VIEW; // STATES: Add, Edit, View

    private int dialogDay;
    private int dialogMonth;
    private int dialogYear;
    private EditText DateToEdit;
    private EditText[] datesList;
    static final int DIALOG_ID = 0;


    private EditText brewName;
    private EditText StartDate;
    private EditText Notes;
    private EditText OriginalGravity;
    private EditText FinalGravity;
    private EditText ABV;

    //schedule events
    private ScheduledEventSchema editScheduledEventSchema;
    private EditText eventText;
    private EditText eventDate;

    private Button addScheduleEventButton;
    private Button editScheduleButton;
    private Spinner colorSpinner;
    private CheckBox dateRollUpCheckBox;
    private CheckBox usingStater;
    private ListView ScheduleEventsListView;

    private KeyListener brewNameListener;
    private KeyListener NotesListener;
    private KeyListener OriginalGravityListener;
    private KeyListener FinalGravityListener;

    private DataBaseManager dbManager;
    private ScheduleActivityData scheduleActivityData;
    ScheduledBrewSchema gScheduleSchema;
    ArrayAdapter<String> colorAdapter;
    private Toolbar toolbar;
    private SchedulerHelper schedulerHelper;


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

        datesList = new EditText[4];
        brewName = (EditText)findViewById(R.id.ScheduleBNameeditText);
        StartDate = (EditText)findViewById(R.id.ScheduleStarteditText);
        datesList[0] = StartDate;
        Notes = (EditText)findViewById(R.id.ScheduleNoteseditText);
        OriginalGravity = (EditText)findViewById(R.id.ScheduleOGeditText);
        FinalGravity = (EditText)findViewById(R.id.ScheduleFGeditText);
        ABV = (EditText)findViewById(R.id.ScheduleABVeditText);

        colorSpinner = (Spinner) findViewById(R.id.Colorspinner);
        dateRollUpCheckBox = (CheckBox)findViewById(R.id.DateRollUpCheckBox);
        usingStater = (CheckBox)findViewById(R.id.HasStaterCheckBox);

        editScheduleButton = (Button)findViewById(R.id.EditScheduleButton);
        addScheduleEventButton = (Button)findViewById(R.id.AddEventButton);
        addScheduleEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddScheduleEvent();
            }
        });
        ScheduleEventsListView = (ListView)findViewById(R.id.ScheduleEvents);
        ScheduleEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ScheduleEventDialog(gScheduleSchema.getScheduledEventSchemaList().get(position));
            }
        });

        brewNameListener = brewName.getKeyListener();
        NotesListener = Notes.getKeyListener();
        OriginalGravityListener = OriginalGravity.getKeyListener();
        FinalGravityListener = FinalGravity.getKeyListener();


        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Schedule");
        dbManager = DataBaseManager.getInstance(getApplicationContext());

        userId = CurrentUser.getInstance().getUser().getUserId();

        scheduleActivityData = ScheduleActivityData.getInstance();
        gScheduleSchema = scheduleActivityData.getScheduledBrewSchema();
        schedulerHelper = new SchedulerHelper(this);

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
        DisplaySchedule(gScheduleSchema);
        editScheduleButton.setText("Edit Schedule");
        //Set EditText to not editable and hide button
        AddEditViewState = DisplayMode.VIEW;
        ToggleFieldEditable(false);

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

    public void showDatePickerDialog(View view)
    {
        //Set the date picker to show current day
        Calendar cal = Calendar.getInstance();
        dialogYear = cal.get(Calendar.YEAR);
        dialogMonth = cal.get(Calendar.MONTH);
        dialogDay = cal.get(Calendar.DAY_OF_MONTH);

        //set what view we want to update after we show datePicker
        DateToEdit = (EditText)view;

        // show the date picker
        showDialog(DIALOG_ID);

    }

    private void updateEditTextDate()
    {
        String month = String.format("%02d", dialogMonth);
        String day = String.format("%02d", dialogDay);

        String originalTime = DateToEdit.getText().toString();
        DateToEdit.setText( Integer.toString(dialogYear)+"-"+month+"-"+day+" 12:00:00" );

        if(dateRollUpCheckBox.isChecked() && DateToEdit != eventDate)
        {
            long daysInBetween=0;
            try
            {
                Date d1 = APPUTILS.dateFormatCompare.parse(originalTime);
                Date d2 = APPUTILS.dateFormatCompare.parse(DateToEdit.getText().toString());

                long timeDifference = d2.getTime() - d1.getTime();
                daysInBetween = timeDifference / (24*60*60*1000);

            }
            catch (Exception e){}

            rollDateUpdateForward(Arrays.asList(datesList).indexOf(DateToEdit)+1, (int)daysInBetween);
        }

    }

    private void rollDateUpdateForward(int pos, int daysToAdd)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: rollDateUpdateForward Pos["+pos+"] days["+daysToAdd+"] Total["+datesList.length+"]");

        if(pos ==  datesList.length)
            return;

        // for each  date after the one edited we want to update by days add / deleted
        for(int i = pos; i < datesList.length; i++)
        {
            try
            {
                Date parsedDate = APPUTILS.dateFormat.parse(datesList[i].getText().toString());
                datesList[i].setText(gScheduleSchema.addDateTime(daysToAdd,parsedDate));
            }
            catch (Exception e){}
        }
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if(id == DIALOG_ID)
            return new DatePickerDialog(this,dPickerListener, dialogYear,dialogMonth,dialogDay);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dPickerListener =
            new DatePickerDialog.OnDateSetListener(){
        @Override
        public  void onDateSet(DatePicker view,int year,int month, int day){
            dialogYear = year;
            dialogMonth = month + 1;
            dialogDay = day;
            updateEditTextDate();
        }
    };

    private void validateSubmit()
    {
        if(APPUTILS.isLogging) Log.e(LOG, "Entering: validateSubmit");

        //Create Brew schedule
        ScheduledBrewSchema sbrew = gScheduleSchema;
        sbrew.setScheduleId(gScheduleSchema.getScheduleId());
        sbrew.setBrewName(brewName.getText().toString());
        sbrew.setUserId(userId);
        sbrew.setBooleanHasStarter(usingStater.isChecked());

        double og=0.0;
        double fg=0.0;

        try
        {
            og = Double.parseDouble(OriginalGravity.getText().toString());
        }
        catch (Exception e){}
        try
        {
            fg = Double.parseDouble(FinalGravity.getText().toString());
        }
        catch (Exception e){}

        sbrew.setOG(og);
        sbrew.setFG(fg);


        sbrew.setStartDate(StartDate.getText().toString());

        List<ScheduledEventSchema> templist = new ArrayList<>();
        for(ScheduledEventSchema scheduledEventSchema : gScheduleSchema.getScheduledEventSchemaList())
        {
            if(sbrew.getStartDate().compareTo(scheduledEventSchema.getEventDate()) <= 0)
                templist.add(scheduledEventSchema);
        }
        gScheduleSchema.setScheduledEventSchemaList(templist);

        //update user calendar
        updateUserCalendar();

        if(colorSpinner.getSelectedItem().toString() == "Blue")
            sbrew.setStyleType("#0000FF");


        sbrew.setNotes(Notes.getText().toString());
        sbrew.setActive(1);


        gScheduleSchema = sbrew;

        dbManager.updateScheduledBrew(gScheduleSchema);

        ifView();
    }

    public void onEditClick(View aView){ifEdit();}

    public void onDeleteClick(View aView)
    {
        for(ScheduledEventSchema  scheduledEventSchema: gScheduleSchema.getScheduledEventSchemaList())
        {
            if(!(scheduledEventSchema.getEventCalendarId()==0))
                schedulerHelper.deleteCalendarEvent(scheduledEventSchema.getScheduledEventId());
        }

        // then delete brew
        dbManager.deleteBrewScheduledById(gScheduleSchema.getScheduleId());
        this.finish();
    }

    private void updateUserCalendar()
    {
        //loop over all schedule events and add / update
        for(ScheduledEventSchema scheduledEventSchema : gScheduleSchema.getScheduledEventSchemaList())
        {
            Date date3 = new Date();
            try {
                date3 = APPUTILS.dateFormat.parse(scheduledEventSchema.getEventDate());

                if(!(scheduledEventSchema.getEventCalendarId() == -1))
                    schedulerHelper.updateCalendarEvent(date3,scheduledEventSchema.getEventCalendarId());
                else
                {
                    long eventID = schedulerHelper.createCalendarEvent(date3, scheduledEventSchema.getEventText());
                    scheduledEventSchema.setEventCalendarId(eventID);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void AddScheduleEvent()
    {
        ScheduledEventSchema se = new ScheduledEventSchema();
        se.setEventText("");
        se.setBrewId(gScheduleSchema.getBrewId());
        se.setScheduleId(gScheduleSchema.getScheduleId());
        se.setEventDate(APPUTILS.dateFormat.format(new Date()));
        ScheduleEventDialog(se);

    }

    private void ScheduleEventDialog(ScheduledEventSchema aScheduledEventSchema)
    {
        editScheduledEventSchema = aScheduledEventSchema;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Schedule Event");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_schedule_event_dialog, null);
        alertDialogBuilder.setView(dialogView);


        eventText = (EditText) dialogView.findViewById(R.id.eventTextView);
        eventText.setText(aScheduledEventSchema.getEventText());

        eventDate = (EditText) dialogView.findViewById(R.id.eventDate);
        eventDate.setText(aScheduledEventSchema.getEventDate());
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();

        Button deleteButton = (Button) dialogView.findViewById(R.id.deleteButton);
        if(aScheduledEventSchema.getScheduledEventId() == 0) deleteButton.setVisibility(View.INVISIBLE);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateDelete(editScheduledEventSchema);
                alertDialog.cancel();
            }
        });
        Button SaveButton = (Button) dialogView.findViewById(R.id.saveButton);
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateSave(editScheduledEventSchema);
                alertDialog.cancel();
            }
        });
        Button cancelButon = (Button) dialogView.findViewById(R.id.cancelButton);
        cancelButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });


        alertDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();

    }

    private void validateSave(ScheduledEventSchema aScheduledEventSchema)
    {
        aScheduledEventSchema.setEventText(eventText.getText().toString());
        aScheduledEventSchema.setEventDate(eventDate.getText().toString());
        //if getScheduledEventId == 0 then its and update and we dont want to add it to the list
        if(validateScheduleDate(aScheduledEventSchema.getEventDate()) && aScheduledEventSchema.getScheduledEventId() == 0)
            gScheduleSchema.getScheduledEventSchemaList().add(aScheduledEventSchema);

        DisplaySchedule(gScheduleSchema);
    }

    private void validateDelete(ScheduledEventSchema aScheduledEventSchema)
    {
        dbManager.deleteScheduleEventById(aScheduledEventSchema.getScheduledEventId());
        gScheduleSchema = dbManager.getScheduledScheduleId(gScheduleSchema.getScheduleId());

        //delete from calendar if exists
        if(!(aScheduledEventSchema.getEventCalendarId()==0))
            schedulerHelper.deleteCalendarEvent(aScheduledEventSchema.getEventCalendarId());

        DisplaySchedule(gScheduleSchema);
    }

    private boolean validateScheduleDate(String aEventDate)
    {
        if(StartDate.getText().toString().compareTo(aEventDate) <= 0 &&
                gScheduleSchema.getEndBrewDate().compareTo(aEventDate) >= 0)
            return true;
        else
            Toast.makeText(getApplicationContext(), "Invalid Date", Toast.LENGTH_LONG).show();

        return false;
    }

    private void ToggleFieldEditable(boolean aEditable)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: ToggleFieldEditable");
        //Reset all fields
        if(!aEditable) {
            //addEditButton.setVisibility(View.INVISIBLE);
            brewName.setKeyListener(null);
            brewName.setClickable(false);
            brewName.setEnabled(false);
            brewName.setFocusable(false);

            StartDate.setKeyListener(null);
            StartDate.setClickable(false);
            StartDate.setEnabled(false);
            StartDate.setFocusable(false);

            OriginalGravity.setKeyListener(null);
            OriginalGravity.setClickable(false);
            OriginalGravity.setEnabled(false);
            //OriginalGravity.setFocusable(false);

            FinalGravity.setKeyListener(null);
            FinalGravity.setClickable(false);
            FinalGravity.setEnabled(false);
            //FinalGravity.setFocusable(false);

            ABV.setKeyListener(null);
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
            usingStater.setClickable(false);

            addScheduleEventButton.setClickable(false);
            addScheduleEventButton.setEnabled(false);

            ScheduleEventsListView.setClickable(false);
            ScheduleEventsListView.setEnabled(false);
        }
        else
        {
            //addEditButton.setVisibility(View.VISIBLE);
            //if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.ADD)
            //   brewName.setKeyListener(brewNameListener);

            //StartDate.setKeyListener(StartDateListener);
            StartDate.setClickable(true);
            StartDate.setEnabled(true);

            OriginalGravity.setKeyListener(OriginalGravityListener);
            OriginalGravity.setClickable(true);
            OriginalGravity.setEnabled(true);
            OriginalGravity.setFocusable(true);

            FinalGravity.setKeyListener(FinalGravityListener);
            FinalGravity.setClickable(true);
            FinalGravity.setEnabled(true);
            FinalGravity.setFocusable(true);

            ABV.setKeyListener(null);
            ABV.setClickable(false);
            ABV.setEnabled(false);
            ABV.setFocusable(false);

            Notes.setKeyListener(NotesListener);
            Notes.setClickable(true);
            Notes.setEnabled(true);
            Notes.setFocusable(true);

            colorSpinner.setClickable(true);
            colorSpinner.setEnabled(true);

            dateRollUpCheckBox.setClickable(true);
            usingStater.setClickable(true);

            addScheduleEventButton.setClickable(true);
            addScheduleEventButton.setEnabled(true);

            ScheduleEventsListView.setEnabled(true);
            ScheduleEventsListView.setClickable(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
