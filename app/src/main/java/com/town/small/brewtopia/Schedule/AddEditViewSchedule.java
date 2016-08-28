package com.town.small.brewtopia.Schedule;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.town.small.brewtopia.DataClass.APPUTILS;
import com.town.small.brewtopia.DataClass.CurrentUser;
import com.town.small.brewtopia.DataBase.DataBaseManager;
import com.town.small.brewtopia.DataClass.ScheduledBrewSchema;
import com.town.small.brewtopia.R;

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
    private EditText SecondaryAlert;
    private EditText BottleAlert;
    private EditText EndDate;
    private EditText Notes;
    private EditText OriginalGravity;
    private EditText FinalGravity;
    private EditText ABV;

    private Button editScheduleButton;
    private Spinner colorSpinner;
    private CheckBox dateRollUpCheckBox;
    private CheckBox usingStater;

    private KeyListener brewNameListener;
    private KeyListener NotesListener;
    private KeyListener OriginalGravityListener;
    private KeyListener FinalGravityListener;

    private DataBaseManager dbManager;
    private ScheduleActivityData scheduleActivityData;
    ScheduledBrewSchema aScheduleSchema;
    ArrayAdapter<String> colorAdapter;
    private Toolbar toolbar;
    private SchedulerHelper schedulerHelper;


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

        datesList = new EditText[4];
        brewName = (EditText)findViewById(R.id.ScheduleBNameeditText);
        StartDate = (EditText)findViewById(R.id.ScheduleStarteditText);
        datesList[0] = StartDate;
        SecondaryAlert = (EditText)findViewById(R.id.Schedule2AlerteditText);
        datesList[1] = SecondaryAlert;
        BottleAlert = (EditText)findViewById(R.id.SchduleBottleAlerteditText);
        datesList[2] = BottleAlert;
        EndDate = (EditText)findViewById(R.id.ScheduleEndDateeditText);
        datesList[3] = EndDate;
        Notes = (EditText)findViewById(R.id.ScheduleNoteseditText);
        OriginalGravity = (EditText)findViewById(R.id.ScheduleOGeditText);
        FinalGravity = (EditText)findViewById(R.id.ScheduleFGeditText);
        ABV = (EditText)findViewById(R.id.ScheduleABVeditText);

        colorSpinner = (Spinner) findViewById(R.id.Colorspinner);
        dateRollUpCheckBox = (CheckBox)findViewById(R.id.DateRollUpCheckBox);
        usingStater = (CheckBox)findViewById(R.id.HasStaterCheckBox);

        editScheduleButton = (Button)findViewById(R.id.EditScheduleButton);


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
        aScheduleSchema = scheduleActivityData.getScheduledBrewSchema();
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
        OriginalGravity.setText(Double.toString(aScheduleSchema.getOG()));
        FinalGravity.setText(Double.toString(aScheduleSchema.getFG()));
        ABV.setText(Double.toString(APPUTILS.GetTruncatedABVPercent(aScheduleSchema.getABV())) +"%" );
        usingStater.setChecked(aScheduleSchema.getBooleanHasStarter());
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

        if(dateRollUpCheckBox.isChecked())
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
        Log.e(LOG, "Entering: rollDateUpdateForward Pos["+pos+"] days["+daysToAdd+"] Total["+datesList.length+"]");

        if(pos ==  datesList.length)
            return;

        // for each  date after the one edited we want to update by days add / deleted
        for(int i = pos; i < datesList.length; i++)
        {
            try
            {
                Date parsedDate = APPUTILS.dateFormat.parse(datesList[i].getText().toString());
                datesList[i].setText(aScheduleSchema.addDateTime(daysToAdd,parsedDate));
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
        Log.e(LOG, "Entering: validateSubmit");

        //Create Brew schedule
        ScheduledBrewSchema sbrew = aScheduleSchema;
        sbrew.setScheduleId(aScheduleSchema.getScheduleId());
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

        //SecondaryAlert date need to be greater then all other dates before it
        if( (StartDate.getText().toString().compareTo(SecondaryAlert.getText().toString()) <= 0) &&
                (SecondaryAlert.getText().toString().compareTo(StartDate.getText().toString()) >= 0) &&
                (BottleAlert.getText().toString().compareTo(StartDate.getText().toString()) >= 0) &&
                (BottleAlert.getText().toString().compareTo(SecondaryAlert.getText().toString()) >= 0) &&
                (EndDate.getText().toString().compareTo(StartDate.getText().toString()) >= 0) &&
                (EndDate.getText().toString().compareTo(SecondaryAlert.getText().toString()) >= 0) &&
                (EndDate.getText().toString().compareTo(BottleAlert.getText().toString()) >= 0))
        {
            sbrew.setStartDate(StartDate.getText().toString());
            sbrew.setAlertSecondaryDate(SecondaryAlert.getText().toString());
            sbrew.setAlertBottleDate(BottleAlert.getText().toString());
            sbrew.setEndBrewDate(EndDate.getText().toString());

            updateUserCalendar(sbrew);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Invalid Date", Toast.LENGTH_LONG).show();
        }

        if(colorSpinner.getSelectedItem().toString() == "Blue")
            sbrew.setColor("#0000FF");


        sbrew.setNotes(Notes.getText().toString());
        sbrew.setActive(1);


        aScheduleSchema = sbrew;

        dbManager.updateScheduledBrew(aScheduleSchema);

        ifView();
    }

    public void onEditClick(View aView){ifEdit();}

    public void onDeleteClick(View aView)
    {
        //delete each event from user Calendar
        schedulerHelper.deleteCalendarEvent(aScheduleSchema.getAlertSecondaryCalendarId());
        schedulerHelper.deleteCalendarEvent(aScheduleSchema.getAlertBottleCalendarId());
        schedulerHelper.deleteCalendarEvent(aScheduleSchema.getEndBrewCalendarId());

        // then delete brew
        dbManager.deleteBrewScheduledById(aScheduleSchema.getScheduleId());
        this.finish();
    }

    private void updateUserCalendar(ScheduledBrewSchema sbrew)
    {
        Date date = new Date();
        Date date1 = new Date();
        Date date2 = new Date();
        try {
            date = APPUTILS.dateFormat.parse(sbrew.getAlertSecondaryDate());
            date1 = APPUTILS.dateFormat.parse(sbrew.getAlertBottleDate());
            date2 = APPUTILS.dateFormat.parse(sbrew.getEndBrewDate());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //delete each event from user Calendar
        schedulerHelper.updateCalendarEvent(date,sbrew.getAlertSecondaryCalendarId());
        schedulerHelper.updateCalendarEvent(date1,sbrew.getAlertBottleCalendarId());
        schedulerHelper.updateCalendarEvent(date2,sbrew.getEndBrewCalendarId());
    }

    private void ToggleFieldEditable(boolean aEditable)
    {
        Log.e(LOG, "Entering: ToggleFieldEditable");
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

            SecondaryAlert.setKeyListener(null);
            SecondaryAlert.setClickable(false);
            SecondaryAlert.setEnabled(false);
            SecondaryAlert.setFocusable(false);

            BottleAlert.setKeyListener(null);
            BottleAlert.setClickable(false);
            BottleAlert.setEnabled(false);
            BottleAlert.setFocusable(false);

            EndDate.setKeyListener(null);
            EndDate.setClickable(false);
            EndDate.setEnabled(false);
            EndDate.setFocusable(false);

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
        }
        else
        {
            //addEditButton.setVisibility(View.VISIBLE);
            //if(brewActivityDataData.getAddEditViewState() == BrewActivityData.DisplayMode.ADD)
            //   brewName.setKeyListener(brewNameListener);

            //StartDate.setKeyListener(StartDateListener);
            StartDate.setClickable(true);
            StartDate.setEnabled(true);

            //SecondaryAlert.setKeyListener(SecondaryAlertListener);
            SecondaryAlert.setClickable(true);
            SecondaryAlert.setEnabled(true);

            //BottleAlert.setKeyListener(BottleAlertListener);
            BottleAlert.setClickable(true);
            BottleAlert.setEnabled(true);

            EndDate.setClickable(true);
            EndDate.setEnabled(true);
            //EndDate.setFocusable(true);


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
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
