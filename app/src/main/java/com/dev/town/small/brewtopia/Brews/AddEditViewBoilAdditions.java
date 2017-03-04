package com.dev.town.small.brewtopia.Brews;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.BoilAdditionsSchema;
import com.dev.town.small.brewtopia.DataClass.BrewSchema;
import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.CurrentUser;
import com.dev.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.List;

public class AddEditViewBoilAdditions extends Fragment {

    // Log cat tag
    private static final String LOG = "AddEditBoilAdditions";

    private BrewActivityData brewData;
    private boolean CanEdit = false;
    private ListView BrewAdditionsListView;
    private TextView NoAdditions;

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
        NoAdditions = (TextView)view.findViewById(R.id.noAdditionsTextView);

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

        setUofMAdapter();
        loadAll();

        return view;
    }

    @Override
    public void setMenuVisibility(boolean isShown) {
        if(isShown)
            loadAll();
    }

    private void loadAll()
    {
        Log.e(LOG, "Entering: loadAll");

        List<BoilAdditionsSchema> boilAdditionsSchemaList = new ArrayList<BoilAdditionsSchema>();

        boilAdditionsSchemaList.addAll(brewData.getInstance().getBaArray());

        if(boilAdditionsSchemaList.size() == 0)
            NoAdditions.setVisibility(View.VISIBLE);
        else
            NoAdditions.setVisibility(View.INVISIBLE);

        //instantiate custom adapter
        CustomBoilAdditionsAdapter adapter = new CustomBoilAdditionsAdapter(boilAdditionsSchemaList, getActivity());
        adapter.setEventHandler(new CustomBoilAdditionsAdapter.EventHandler() {
            @Override
            public void OnEditClick(BoilAdditionsSchema aBoilAdditionsSchema) {
                AdditionSelected(aBoilAdditionsSchema);
            }
        });

        if(!CanEdit || CurrentUser.getInstance().getUser().isTemp())
            adapter.setEditable(false);

        BrewAdditionsListView.setAdapter(adapter);
    }


    private void AdditionSelected(BoilAdditionsSchema aBoilAdditionsSchema) {

        editBoilAdditionsSchema = aBoilAdditionsSchema;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Boil Addition");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_boil_addition_dialog, null);
        alertDialogBuilder.setView(dialogView);


        editAdditionName = (EditText) dialogView.findViewById(R.id.additionNameEditText);
        editAdditionName.setText(editBoilAdditionsSchema.getAdditionName());

        editAdditionTime = (EditText) dialogView.findViewById(R.id.additionTimeEditText);
        editAdditionTime.setText(Integer.toString(editBoilAdditionsSchema.getAdditionTime()));

        editAdditionQty = (EditText) dialogView.findViewById(R.id.additionQtyEditText);
        editAdditionQty.setText(Double.toString(editBoilAdditionsSchema.getAdditionQty()));

        editUOfMSpinner = (Spinner) dialogView.findViewById(R.id.UofMSpinner);
        editUOfMSpinner.setAdapter(UofMAdapter);

        try
        {
            editUOfMSpinner.setSelection(UofMAdapter.getPosition(editBoilAdditionsSchema.getUOfM()));
        }
        catch (Exception e)
        {
            editUOfMSpinner.setSelection(0);
        }

        final AlertDialog alertDialog = alertDialogBuilder.create();

        Button deleteButton = (Button) dialogView.findViewById(R.id.deleteButton);
        if(editBoilAdditionsSchema.getAdditionId() == -1) deleteButton.setVisibility(View.INVISIBLE);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateDelete(editBoilAdditionsSchema);
                alertDialog.cancel();
            }
        });
        Button SaveButton = (Button) dialogView.findViewById(R.id.saveButton);
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateSave(editBoilAdditionsSchema);
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

    private void validateSave(BoilAdditionsSchema aBoilAdditionsSchema)
    {
        int t=0;
        double q=0.0;

        try
        {
            t  = Integer.parseInt(editAdditionTime.getText().toString());
        }
        catch (Exception e){}
        try
        {
            q = Double.parseDouble(editAdditionQty.getText().toString());
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
                aBoilAdditionsSchema.setBrewId(brewSchema.getBrewId());
                dbManager.add_boil_additions(aBoilAdditionsSchema);
            }
            else
                dbManager.update_boil_addition(aBoilAdditionsSchema);

            resetBrewData(aBoilAdditionsSchema.getBrewId());
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
        resetBrewData(aBoilAdditionsSchema.getBrewId());
    }

    private void resetBrewData(long aBrewId)
    {
        brewData.getInstance().setAddEditViewBrew(dbManager.getBrew(aBrewId));
        loadAll();
    }


    private void setUofMAdapter()
    {
        List<String> UOfMs = APPUTILS.UofM;

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
