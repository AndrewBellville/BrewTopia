package com.town.small.brewtopia.Brews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.town.small.brewtopia.DataClass.APPUTILS;
import com.town.small.brewtopia.DataClass.BrewNoteSchema;
import com.town.small.brewtopia.DataClass.BrewSchema;
import com.town.small.brewtopia.DataClass.CurrentUser;
import com.town.small.brewtopia.DataClass.DataBaseManager;
import com.town.small.brewtopia.DataClass.ScheduledBrewSchema;
import com.town.small.brewtopia.R;
import com.town.small.brewtopia.Schedule.AddEditViewSchedule;
import com.town.small.brewtopia.Schedule.CustomSListAdapter;
import com.town.small.brewtopia.Schedule.ScheduleActivityData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Andrew on 4/9/2016.
 */
public class AddEditViewBrewNotes extends Fragment {

    // Log cat tag
    private static final String LOG = "AddEditViewBrewNotes";

    private ListView BrewNotesListView;
    private TextView NoNotes;
    private DataBaseManager dbManager;

    //edit dialog
    private BrewNoteSchema editBrewNoteSchema;
    private EditText noteText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_add_edit_view_brew_notes,container,false);
        Log.e(LOG, "Entering: onCreate");

        BrewNotesListView = (ListView)view.findViewById(R.id.brewNotesLstView);
        NoNotes = (TextView)view.findViewById(R.id.noNotesTextView);
        dbManager = DataBaseManager.getInstance(getActivity());

        Button addButton = (Button)view.findViewById(R.id.AddNewButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddClick(view);
            }
        });

        LoadBrewNoteView();

        return view;
    }

    @Override
    public void setMenuVisibility(boolean isShown) {
        if(isShown)
            LoadBrewNoteView();
    }

    private void LoadBrewNoteView() {
        Log.e(LOG, "Entering: LoadBrewNoteView");

        List<BrewNoteSchema> brewNoteSchemaList = new ArrayList<BrewNoteSchema>();

        brewNoteSchemaList.addAll(BrewActivityData.getInstance().getBrewNoteSchemaList());

        if(brewNoteSchemaList.size() == 0)
            NoNotes.setVisibility(View.VISIBLE);
        else
            NoNotes.setVisibility(View.INVISIBLE);

        //instantiate custom adapter
        CustomNListAdapter adapter = new CustomNListAdapter(brewNoteSchemaList, getActivity());
        adapter.setEventHandler(new CustomNListAdapter.EventHandler() {
            @Override
            public void OnEditClick(BrewNoteSchema aBrewNoteSchema) {
                NoteSelected(aBrewNoteSchema);
            }
        });

        BrewNotesListView.setAdapter(adapter);
    }

    private void NoteSelected(BrewNoteSchema aBrewNoteSchema)
    {
        editBrewNoteSchema = aBrewNoteSchema;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Brew Note");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_note_dialog, null);
        alertDialogBuilder.setView(dialogView);

        TextView dateTime = (TextView) dialogView.findViewById(R.id.DateTextView);
        dateTime.setText(editBrewNoteSchema.getCreatedOn());

        noteText = (EditText) dialogView.findViewById(R.id.NoteEditText);
        noteText.setText(aBrewNoteSchema.getBrewNote());


        final AlertDialog alertDialog = alertDialogBuilder.create();

        Button deleteButton = (Button) dialogView.findViewById(R.id.deleteButton);
        if(aBrewNoteSchema.getNoteId() == -1) deleteButton.setVisibility(View.INVISIBLE);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateDelete(editBrewNoteSchema);
                alertDialog.cancel();
            }
        });
        Button SaveButton = (Button) dialogView.findViewById(R.id.saveButton);
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateSave(editBrewNoteSchema);
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

    private void validateSave(BrewNoteSchema aBrewNoteSchema)
    {
        BrewSchema brewSchema  = BrewActivityData.getInstance().getAddEditViewBrew();
        aBrewNoteSchema.setBrewNote(noteText.getText().toString());
        if(BrewActivityData.getInstance().getAddEditViewState() != BrewActivityData.DisplayMode.ADD)
        {
            if(aBrewNoteSchema.getNoteId() == -1)
            {
                aBrewNoteSchema.setUserId(brewSchema.getUserId());
                aBrewNoteSchema.setBrewId(brewSchema.getBrewId());
                dbManager.addBrewNote(aBrewNoteSchema);
            }
            else
                dbManager.updateBrewNotes(aBrewNoteSchema);

            resetBrewData(aBrewNoteSchema.getBrewId(),aBrewNoteSchema.getUserId());
        }
        else
        {
            //this should  happen when adding new brew
            LoadBrewNoteView();
        }
    }
    private void validateDelete(BrewNoteSchema aBrewNoteSchema)
    {
        //we should not be able to delete anything that doesnt exist so just delete
        dbManager.deleteBrewNoteById(aBrewNoteSchema.getNoteId());
        resetBrewData(aBrewNoteSchema.getBrewId(),aBrewNoteSchema.getUserId());
    }

    private void resetBrewData(long aBrewId, long aUserId)
    {
        BrewActivityData.getInstance().setAddEditViewBrew(dbManager.getBrew(aBrewId,aUserId));
        LoadBrewNoteView();
    }

    public void onAddClick(View aView) {
        Log.e(LOG, "Entering: onAddClick");
        createBoilAddition();
    }

    private void createBoilAddition()
    {
        BrewNoteSchema bn = new BrewNoteSchema();
        BrewActivityData.getInstance().getBrewNoteSchemaList().add(bn);
        LoadBrewNoteView();
    }
}
