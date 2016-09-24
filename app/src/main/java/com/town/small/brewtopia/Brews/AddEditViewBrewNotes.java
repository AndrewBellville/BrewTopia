package com.town.small.brewtopia.Brews;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.town.small.brewtopia.DataClass.BrewNoteSchema;
import com.town.small.brewtopia.DataClass.BrewSchema;
import com.town.small.brewtopia.DataBase.DataBaseManager;
import com.town.small.brewtopia.R;

import java.util.ArrayList;
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

    private BrewActivityData brewData;
    private boolean CanEdit = false;

    //edit dialog
    private BrewNoteSchema editBrewNoteSchema;
    private EditText noteText;

    private boolean init = false;

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

        brewData = BrewActivityData.getInstance();
        CanEdit = brewData.CanEdit();

        //Hide Button if We cant edit
        if(!CanEdit)
            addButton.setVisibility(View.INVISIBLE);

        LoadBrewNoteView();

        init  = true;

        return view;
    }

    @Override
    public void setMenuVisibility(boolean isShown) {
        if(isShown && init)
            LoadBrewNoteView();
    }

    private void LoadBrewNoteView() {
        Log.e(LOG, "Entering: LoadBrewNoteView");

        List<BrewNoteSchema> brewNoteSchemaList = new ArrayList<BrewNoteSchema>();

        brewNoteSchemaList.addAll(brewData.getBrewNoteSchemaList());

        if(brewNoteSchemaList.size() == 0)
            NoNotes.setVisibility(View.VISIBLE);
        else
            NoNotes.setVisibility(View.INVISIBLE);

        //instantiate custom adapter
        CustomNListAdapter adapter = new CustomNListAdapter(brewNoteSchemaList, getActivity());
        adapter.setEditable(CanEdit);
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
        BrewSchema brewSchema  = brewData.getAddEditViewBrew();
        aBrewNoteSchema.setBrewNote(noteText.getText().toString());
        if(brewData.getAddEditViewState() != BrewActivityData.DisplayMode.ADD)
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
        brewData.setAddEditViewBrew(dbManager.getBrew(aBrewId,aUserId));
        LoadBrewNoteView();
    }

    public void onAddClick(View aView) {
        Log.e(LOG, "Entering: onAddClick");
        createBrewNote();
    }

    private void createBrewNote()
    {
        BrewNoteSchema bn = new BrewNoteSchema();
        brewData.getBrewNoteSchemaList().add(bn);
        LoadBrewNoteView();
    }
}
