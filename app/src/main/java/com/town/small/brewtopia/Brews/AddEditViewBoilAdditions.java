package com.town.small.brewtopia.Brews;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.town.small.brewtopia.DataClass.BoilAdditionsSchema;
import com.town.small.brewtopia.DataClass.BrewNoteSchema;
import com.town.small.brewtopia.DataClass.BrewSchema;
import com.town.small.brewtopia.DataClass.DataBaseManager;
import com.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AddEditViewBoilAdditions extends Fragment {

    // Log cat tag
    private static final String LOG = "AddEditBoilAdditions";

    private BrewActivityData brewData;
    private ListView BrewAdditionsListView;
    private boolean isInit = false;

    private DataBaseManager dbManager;

    //edit dialog
    private  BoilAdditionsSchema editBoilAdditionsSchema;
    private ArrayAdapter<String> UofMAdapter;
    private EditText editAdditionName;
    private EditText editAdditionTime;
    private EditText editAdditionQty;
    private Spinner editUOfMSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_add_edit_view_boil_additions,container,false);
        Log.e(LOG, "Entering: onCreate");

        dbManager = DataBaseManager.getInstance(getActivity());

        BrewAdditionsListView = (ListView)view.findViewById(R.id.brewAdditonsListView);

        Button addButton = (Button)view.findViewById(R.id.AddNewButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddClick(view);
            }
        });

        brewData = BrewActivityData.getInstance();

        addButton.setVisibility(View.VISIBLE);

        setUofMAdapter();
        loadAll();

        return view;
    }

    @Override
    public void setMenuVisibility(boolean isShown) {
        if(isShown && !isInit)
        {
            Log.e(LOG, "Entering: Init Show");
            loadAll();
            isInit = true;
        }
    }

    private void loadAll()
    {
        Log.e(LOG, "Entering: loadAll");

        List<BoilAdditionsSchema> boilAdditionsSchemaList = new ArrayList<BoilAdditionsSchema>();

        boilAdditionsSchemaList.addAll(brewData.getInstance().getBaArray());

        //instantiate custom adapter
        CustomBoilAdditionsAdapter adapter = new CustomBoilAdditionsAdapter(boilAdditionsSchemaList, getActivity());
        adapter.setEventHandler(new CustomBoilAdditionsAdapter.EventHandler() {
            @Override
            public void OnEditClick(BoilAdditionsSchema aBoilAdditionsSchema) {
                AdditionSelected(aBoilAdditionsSchema);
            }
        });

        BrewAdditionsListView.setAdapter(adapter);
    }


    private void AdditionSelected(BoilAdditionsSchema aBoilAdditionsSchema) {

        editBoilAdditionsSchema = aBoilAdditionsSchema;

        final Dialog dialog = new Dialog(getActivity(),R.style.DialogStyle);
        dialog.setTitle("Boil Addition");
        dialog.setContentView(R.layout.custom_boil_addition_dialog);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        editAdditionName = (EditText) dialog.findViewById(R.id.additionNameEditText);
        editAdditionName.setText(editBoilAdditionsSchema.getAdditionName());

        editAdditionTime = (EditText) dialog.findViewById(R.id.additionTimeEditText);
        editAdditionTime.setText(Integer.toString(editBoilAdditionsSchema.getAdditionTime()));

        editAdditionQty = (EditText) dialog.findViewById(R.id.additionQtyEditText);
        editAdditionQty.setText(Integer.toString(editBoilAdditionsSchema.getAdditionTime()));

        editUOfMSpinner = (Spinner) dialog.findViewById(R.id.UofMSpinner);
        editUOfMSpinner.setAdapter(UofMAdapter);


        Button deleteButton = (Button) dialog.findViewById(R.id.deleteButton);
        if(editBoilAdditionsSchema.getAdditionId() == -1) deleteButton.setVisibility(View.INVISIBLE);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateDelete(editBoilAdditionsSchema);
                dialog.cancel();
            }
        });
        Button SaveButton = (Button) dialog.findViewById(R.id.saveButton);
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateSave(editBoilAdditionsSchema);
                dialog.cancel();
            }
        });
        Button cancelButon = (Button) dialog.findViewById(R.id.cancelButton);
        cancelButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void validateSave(BoilAdditionsSchema aBoilAdditionsSchema)
    {
        int t=0;
        int q=0;

        try
        {
            t  = Integer.parseInt(editAdditionTime.getText().toString());
        }
        catch (Exception e){}
        try
        {
            q = Integer.parseInt(editAdditionQty.getText().toString());
        }
        catch (Exception e){}

        aBoilAdditionsSchema.setAdditionName(editAdditionName.getText().toString());
        aBoilAdditionsSchema.setAdditionTime(t);
        aBoilAdditionsSchema.setAdditionQty(q);
        aBoilAdditionsSchema.setUOfM(editUOfMSpinner.getSelectedItem().toString());

        if(brewData.getInstance().getAddEditViewState() != BrewActivityData.DisplayMode.ADD)
        {
            if(aBoilAdditionsSchema.getAdditionId() == -1)
            {
                BrewSchema brewSchema  = brewData.getInstance().getAddEditViewBrew();
                aBoilAdditionsSchema.setUserName(brewSchema.getUserName());
                aBoilAdditionsSchema.setBrewName(brewSchema.getBrewName());
                dbManager.add_boil_additions(aBoilAdditionsSchema);
            }
            else
                dbManager.update_boil_addition(aBoilAdditionsSchema);

            resetBrewData(aBoilAdditionsSchema.getBrewName(),aBoilAdditionsSchema.getUserName());
        }
        else
        {
            //this should  happen when adding new brew
            loadAll();
        }
    }
    private void validateDelete(BoilAdditionsSchema aBoilAdditionsSchema)
    {
        //we should not be able to delete anything that doesnt exist so just delete
        dbManager.delete_all_boil_additions_by_id(aBoilAdditionsSchema.getAdditionId());
        resetBrewData(aBoilAdditionsSchema.getBrewName(),aBoilAdditionsSchema.getUserName());
    }

    private void resetBrewData(String aBrewName, String aUserName)
    {
        brewData.getInstance().setAddEditViewBrew(dbManager.getBrew(aBrewName,aUserName));
        loadAll();
    }


    private void setUofMAdapter()
    {
        List<String> UOfMs = new ArrayList<String>();
        UOfMs.add("Cups");
        UOfMs.add("oz");

        UofMAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, UOfMs);
        // Specify the layout to use when the list of choices appears
        UofMAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void onAddClick(View aView) {
        Log.e(LOG, "Entering: onAddClick");
        createBoilAddition();
    }

    private void createBoilAddition()
    {
        BoilAdditionsSchema ba = new BoilAdditionsSchema();
        brewData.getInstance().getBaArray().add(ba);
        loadAll();
    }

}
