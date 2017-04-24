package com.dev.town.small.brewtopia.Schedule;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.dev.town.small.brewtopia.Utilites.DatePickerFrag;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddEditViewSchedule extends Fragment {

    // Log cat tag
    private static final String LOG = "AddEditViewSchedule";

    private long  userId;
    private ScrollView ScrollView;

    //State for AddEditView Brew and Brew Name if Edit/View
    public enum DisplayMode {
        ADD, EDIT, VIEW
    };
    private DisplayMode AddEditViewState = DisplayMode.VIEW; // STATES: Add, Edit, View


    private EditText DateToEdit;

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
    private Button deleteScheduleButton;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View returnView = inflater.inflate(R.layout.schedule_view,container,false);
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onCreate");

        //Add add edit layout default
        View mainView = inflater.inflate(R.layout.activity_add_edit_view_schedule, null);

        ScrollView = (ScrollView)returnView.findViewById(R.id.ScheduleScrollView);
        ScrollView.addView(mainView);

        brewName = (EditText)mainView.findViewById(R.id.ScheduleBNameEditText);
        StartDate = (EditText)mainView.findViewById(R.id.ScheduleStarteditText);

        Notes = (EditText)mainView.findViewById(R.id.ScheduleNoteseditText);
        OriginalGravity = (EditText)mainView.findViewById(R.id.ScheduleOGeditText);
        FinalGravity = (EditText)mainView.findViewById(R.id.ScheduleFGeditText);
        ABV = (EditText)mainView.findViewById(R.id.ScheduleABVeditText);

        colorSpinner = (Spinner) mainView.findViewById(R.id.Colorspinner);
        dateRollUpCheckBox = (CheckBox)mainView.findViewById(R.id.DateRollUpCheckBox);
        usingStater = (CheckBox)mainView.findViewById(R.id.HasStaterCheckBox);

        editScheduleButton = (Button)returnView.findViewById(R.id.EditScheduleButton);
        editScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifEdit();
            }
        });
        deleteScheduleButton = (Button)returnView.findViewById(R.id.DeleteButton);
        deleteScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClick();
            }
        });
        addScheduleEventButton = (Button)mainView.findViewById(R.id.AddEventButton);
        addScheduleEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddScheduleEvent();
            }
        });
        ScheduleEventsListView = (ListView)mainView.findViewById(R.id.ScheduleEvents);
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

        dbManager = DataBaseManager.getInstance(getActivity());

        userId = CurrentUser.getInstance().getUser().getUserId();

        scheduleActivityData = ScheduleActivityData.getInstance();
        gScheduleSchema = scheduleActivityData.getScheduledBrewSchema();
        schedulerHelper = new SchedulerHelper(getActivity());

        ToggleFieldEditable(false);
        setColorSpinner();

        if(scheduleActivityData.getAddEditViewState() == ScheduleActivityData.DisplayMode.COMPLETE) {
            deleteScheduleButton.setVisibility(View.GONE);
        }

        ifView();


        return returnView;
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

        // set to brew Style this might be deleted if its user created
        try
        {
            colorSpinner.setSelection(colorAdapter.getPosition(aScheduleSchema.getStyleType()));
        }
        catch (Exception e)
        {
            //if we are here user must have deleted brew style try to set to None
            try
            {
                colorSpinner.setSelection(colorAdapter.getPosition("None"));
            }
            catch (Exception ex)
            {
                // if all else fails set to index 0
                colorSpinner.setSelection(0);
            }
        }

        CustomSEListAdapter adapter = new CustomSEListAdapter(aScheduleSchema.getScheduledEventSchemaList(), getActivity());
        ScheduleEventsListView.setAdapter(adapter);
        APPUTILS.setListViewHeightBasedOnChildren(ScheduleEventsListView);
    }

    private void setColorSpinner()
    {
        String[] brewStyles = new String[ APPUTILS.StyleMap.size()];

        Iterator it = APPUTILS.StyleMap.entrySet().iterator();
        int i=0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            brewStyles[i++]= pair.getKey().toString();
        }

        colorAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, brewStyles); //selected item will look like a spinner set from XML
        // Specify the layout to use when the list of choices appears
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        colorSpinner.setAdapter(colorAdapter);
    }

    private void validateSubmit()
    {
        if(APPUTILS.isLogging) Log.e(LOG, "Entering: validateSubmit");

        //Create Brew schedule
        ScheduledBrewSchema sbrew = gScheduleSchema;
        sbrew.setScheduleId(gScheduleSchema.getScheduleId());
        sbrew.setBrewId(gScheduleSchema.getBrewId());
        sbrew.setBrewName(brewName.getText().toString());
        sbrew.setUserId(userId);
        sbrew.setBooleanHasStarter(usingStater.isChecked());
        sbrew.setStyleType(colorSpinner.getSelectedItem().toString());

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


        sbrew.setNotes(Notes.getText().toString());
        sbrew.setActive(gScheduleSchema.getActive());

        gScheduleSchema = sbrew;

        dbManager.updateScheduledBrew(gScheduleSchema);

        ifView();
    }

    public void onDeleteClick()
    {
        for(ScheduledEventSchema  scheduledEventSchema: gScheduleSchema.getScheduledEventSchemaList())
        {
            if(!(scheduledEventSchema.getEventCalendarId()==0))
                schedulerHelper.deleteCalendarEvent(scheduledEventSchema.getScheduledEventId());
        }

        // then delete brew
        dbManager.deleteBrewScheduledById(gScheduleSchema.getScheduleId());
        getActivity().finish();
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Schedule Event");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_schedule_event_dialog, null);
        alertDialogBuilder.setView(dialogView);


        eventText = (EditText) dialogView.findViewById(R.id.eventTextView);
        eventText.setText(aScheduledEventSchema.getEventText());

        eventDate = (EditText) dialogView.findViewById(R.id.eventDate);
        eventDate.setText(aScheduledEventSchema.getEventDate());
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateToEdit = (EditText) view;
                DatePickerFrag newFragment = new DatePickerFrag();
                newFragment.setEditText(DateToEdit);
                newFragment.show(getFragmentManager(), "DatePicker");
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
            Toast.makeText(getActivity(), "Invalid Date", Toast.LENGTH_LONG).show();

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
            //brewName.setFocusable(false);

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
            if(scheduleActivityData.getAddEditViewState() != ScheduleActivityData.DisplayMode.COMPLETE) {

                brewName.setKeyListener(brewNameListener);
                brewName.setClickable(true);
                brewName.setEnabled(true);
                brewName.setFocusable(true);

                //StartDate.setKeyListener(StartDateListener);
                //StartDate.setClickable(true);
                //StartDate.setEnabled(true);

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

                colorSpinner.setClickable(true);
                colorSpinner.setEnabled(true);

                dateRollUpCheckBox.setClickable(true);
                usingStater.setClickable(true);

                addScheduleEventButton.setClickable(true);
                addScheduleEventButton.setEnabled(true);

                ScheduleEventsListView.setEnabled(true);
                ScheduleEventsListView.setClickable(true);
            }

            Notes.setKeyListener(NotesListener);
            Notes.setClickable(true);
            Notes.setEnabled(true);
            Notes.setFocusable(true);
        }
    }
}

