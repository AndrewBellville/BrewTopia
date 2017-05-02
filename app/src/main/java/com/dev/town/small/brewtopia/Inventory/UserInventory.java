package com.dev.town.small.brewtopia.Inventory;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dev.town.small.brewtopia.Brews.BrewActivityData;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.CurrentUser;
import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.EquipmentSchema;
import com.dev.town.small.brewtopia.DataClass.FermentablesSchema;
import com.dev.town.small.brewtopia.DataClass.HopsSchema;
import com.dev.town.small.brewtopia.DataClass.InventorySchema;
import com.dev.town.small.brewtopia.DataClass.OtherSchema;
import com.dev.town.small.brewtopia.DataClass.YeastSchema;
import com.dev.town.small.brewtopia.R;
import com.dev.town.small.brewtopia.SharedMemory.InventoryMemory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class UserInventory extends Fragment {

    // Log cat tag
    private static final String LOG = "UserInventory";

    //Inventory Categories
    public enum InventoryCategories {
        Hops,
        Fermentables,
        Yeast,
        Equipment,
        Other
    };

    //Inventory List
    private ExpandableListView expInventoryListView;
    private CustomInventoryExpandableListAdapter InventoryListAdapter;
    private List<String> InventoryListDataHeader;
    private HashMap<String, List<InventorySchema>> InventoryListDataChild;


    private DataBaseManager dbManager;
    private long userId;
    private boolean isBrewDisplay = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_user_inventory,container,false);
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onCreateView");

        // get the listview
        expInventoryListView = (ExpandableListView) view.findViewById(R.id.inventoryExpandableInventoryList);
        final Button searchButton = (Button) view.findViewById(R.id.addButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchButtonClick();
            }
        });

        dbManager = DataBaseManager.getInstance(getActivity());
        userId = CurrentUser.getInstance().getUser().getUserId();

        CheckForBrewDisplay();

        SetUpInventoryList();

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        CheckForBrewDisplay();
        SetUpInventoryList();
    }

    private void CheckForBrewDisplay()
    {
        // If this is being displayed by the brew class we want to perform brew specific functionality
        if( getActivity().getLocalClassName().contains("Brews.UserBrew") || getActivity().getLocalClassName().contains("Global.UserGlobal")) isBrewDisplay = true;
    }

    private void SetUpInventoryList()
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: SetUpInventoryList");
        // preparing list data
        prepareInventoryListData();

        InventoryListAdapter = new CustomInventoryExpandableListAdapter(getActivity(), InventoryListDataHeader, InventoryListDataChild);
        InventoryListAdapter.setEventHandler(new CustomInventoryExpandableListAdapter.EventHandler() {
            @Override
            public void OnAddClick(String aHeaderTitle) {
                addClickInventory(aHeaderTitle);
            }
        });

        //If this is a brew type display and we cant edit then we want to set editable false
        if((isBrewDisplay && !BrewActivityData.getInstance().CanEdit()) || CurrentUser.getInstance().getUser().isTemp())
            InventoryListAdapter.setEditable(false);

        // setting list adapter
        expInventoryListView.setAdapter(InventoryListAdapter);
        expInventoryListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                /*
                Toast.makeText(
                        getActivity(),
                        HopsListDataHeader.get(groupPosition)
                                + " : "
                                + HopsListDataChild.get(
                                HopsListDataHeader.get(groupPosition)).get(
                                childPosition).getInventoryName(), Toast.LENGTH_SHORT)
                        .show();
                        */
                onInventoryClick(groupPosition, childPosition);
                return false;
            }
        });
        expInventoryListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*
                Toast.makeText(getActivity(),
                        HopsListDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
                        */
            }
        });
        expInventoryListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                /*
                Toast.makeText(getActivity(),
                        HopsListDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();
                        */

            }
        });
    }

    private void prepareInventoryListData() {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: prepareInventoryListData");
        InventoryListDataHeader = new ArrayList<String>();
        InventoryListDataChild = new HashMap<String, List<InventorySchema>>();


        for(InventoryCategories inventoryCategories :  InventoryCategories.values())
        {
            List<InventorySchema> inventorySchemaList;


            if(inventoryCategories == InventoryCategories.Hops)
            {
                if(isBrewDisplay && !(BrewActivityData.getInstance().getAddEditViewBrew().getBrewId() >= 0))
                    inventorySchemaList = prepareInventoryListDataHelper(inventoryCategories);
                else if(isBrewDisplay)
                    inventorySchemaList = dbManager.getAllHopsByUserIdandBrewId(userId, BrewActivityData.getInstance().getAddEditViewBrew().getBrewId());
                else
                    inventorySchemaList = InventoryMemory.getInstance().getHopsSchemas();
            }
            else if(inventoryCategories == InventoryCategories.Fermentables)
            {
                if(isBrewDisplay && !(BrewActivityData.getInstance().getAddEditViewBrew().getBrewId() >= 0))
                    inventorySchemaList = prepareInventoryListDataHelper(inventoryCategories);
                else if(isBrewDisplay)
                    inventorySchemaList = dbManager.getAllFermentablesByUserIdandBrewId(userId, BrewActivityData.getInstance().getAddEditViewBrew().getBrewId());
                else
                    inventorySchemaList = InventoryMemory.getInstance().getFermentablesSchemas();
            }
            else if(inventoryCategories == InventoryCategories.Yeast)
            {
                if(isBrewDisplay && !(BrewActivityData.getInstance().getAddEditViewBrew().getBrewId() >= 0))
                    inventorySchemaList = prepareInventoryListDataHelper(inventoryCategories);
                else if(isBrewDisplay)
                    inventorySchemaList = dbManager.getAllYeastByUserIdandBrewId(userId, BrewActivityData.getInstance().getAddEditViewBrew().getBrewId());
                else
                    inventorySchemaList = InventoryMemory.getInstance().getYeastSchemas();
            }
            else if(inventoryCategories == InventoryCategories.Equipment)
            {
                if(isBrewDisplay && !(BrewActivityData.getInstance().getAddEditViewBrew().getBrewId() >= 0))
                    inventorySchemaList = prepareInventoryListDataHelper(inventoryCategories);
                else if(isBrewDisplay)
                    inventorySchemaList = dbManager.getAllEquipmentByUserIdandBrewId(userId, BrewActivityData.getInstance().getAddEditViewBrew().getBrewId());
                else
                    inventorySchemaList = InventoryMemory.getInstance().getEquipmentSchemas();
            }
            else if(inventoryCategories == InventoryCategories.Other)
            {
                if(isBrewDisplay && !(BrewActivityData.getInstance().getAddEditViewBrew().getBrewId() >= 0))
                    inventorySchemaList = prepareInventoryListDataHelper(inventoryCategories);
                else if(isBrewDisplay)
                    inventorySchemaList = dbManager.getAllOtherByUserIdandBrewId(userId, BrewActivityData.getInstance().getAddEditViewBrew().getBrewId());
                else
                    inventorySchemaList = InventoryMemory.getInstance().getOtherSchemas();
            }
            else
                inventorySchemaList = new ArrayList<InventorySchema>();

            String headerText = inventoryCategories.toString()+" ("+inventorySchemaList.size()+")";
            InventoryListDataHeader.add(headerText);
            InventoryListDataChild.put(headerText, inventorySchemaList); // Header, Child data
        }
    }

    private ArrayList<InventorySchema> prepareInventoryListDataHelper(InventoryCategories aInventoryCategories)
    {
        ArrayList<InventorySchema> inventorySchemas = new ArrayList<InventorySchema>();

        for(InventorySchema inventorySchema : BrewActivityData.getInstance().getAddEditViewBrew().getBrewInventorySchemaList())
        {
            if(inventorySchema instanceof HopsSchema && aInventoryCategories == InventoryCategories.Hops)
                inventorySchemas.add(inventorySchema);
            if(inventorySchema instanceof FermentablesSchema && aInventoryCategories == InventoryCategories.Fermentables)
                inventorySchemas.add(inventorySchema);
            if(inventorySchema instanceof YeastSchema && aInventoryCategories == InventoryCategories.Yeast)
                inventorySchemas.add(inventorySchema);
            if(inventorySchema instanceof EquipmentSchema && aInventoryCategories == InventoryCategories.Equipment)
                inventorySchemas.add(inventorySchema);
            if(inventorySchema instanceof OtherSchema && aInventoryCategories == InventoryCategories.Other)
                inventorySchemas.add(inventorySchema);
        }

        return inventorySchemas;
    }

    private void addClickInventory(String aHeaderTitle) {

        if(aHeaderTitle.contains(InventoryCategories.Hops.toString()) )
        {
            if(isBrewDisplay)
            {
                addToBrewDialog(InventoryCategories.Hops);
            }
            else
            {
                //Set Selected Inventory in Activity Data
                InventoryActivityData.getInstance().setHopsSchema(null);
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.ADD);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewHops.class);
                startActivity(intent);
            }
        }
        else if(aHeaderTitle.contains(InventoryCategories.Fermentables.toString()) )
        {
            if(isBrewDisplay)
            {
                addToBrewDialog(InventoryCategories.Fermentables);
            }
            else
            {
                //Set Selected Inventory in Activity Data
                InventoryActivityData.getInstance().setFermentablesSchema(null);
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.ADD);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewFermentables.class);
                startActivity(intent);
            }
        }
        else if(aHeaderTitle.contains(InventoryCategories.Yeast.toString()) )
        {
            if(isBrewDisplay)
            {
                addToBrewDialog(InventoryCategories.Yeast);
            }
            else
            {
                //Set Selected Inventory in Activity Data
                InventoryActivityData.getInstance().setYeastSchema(null);
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.ADD);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewYeast.class);
                startActivity(intent);
            }
        }
        else if(aHeaderTitle.contains(InventoryCategories.Equipment.toString()) )
        {
            if(isBrewDisplay)
            {
                addToBrewDialog(InventoryCategories.Equipment);
            }
            else
            {
                //Set Selected Inventory in Activity Data
                InventoryActivityData.getInstance().setEquipmentSchema(null);
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.ADD);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewEquipment.class);
                startActivity(intent);
            }
        }
        else if(aHeaderTitle.contains(InventoryCategories.Other.toString()) )
        {
            if(isBrewDisplay)
            {
                addToBrewDialog(InventoryCategories.Other);
            }
            else
            {
                //Set Selected Inventory in Activity Data
                InventoryActivityData.getInstance().setOtherSchema(null);
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.ADD);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewOther.class);
                startActivity(intent);
            }
        }

    }

    private void onInventoryClick(int groupPosition, int childPosition)
    {
        //Set Selected hops in Activity Data
        InventorySchema inventorySchema = InventoryListDataChild.get(InventoryListDataHeader.get(groupPosition)).get(childPosition);
        if (inventorySchema instanceof HopsSchema) {
            InventoryActivityData.getInstance().setHopsSchema(inventorySchema);
            InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.VIEW);
            //Start Activity
            Intent intent = new Intent(getActivity(), AddEditViewHops.class);
            startActivity(intent);
        }
        else if (inventorySchema instanceof FermentablesSchema) {
            InventoryActivityData.getInstance().setFermentablesSchema(inventorySchema);
            InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.VIEW);
            //Start Activity
            Intent intent = new Intent(getActivity(), AddEditViewFermentables.class);
            startActivity(intent);
        }
        else if (inventorySchema instanceof YeastSchema) {
            InventoryActivityData.getInstance().setYeastSchema(inventorySchema);
            InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.VIEW);
            //Start Activity
            Intent intent = new Intent(getActivity(), AddEditViewYeast.class);
            startActivity(intent);
        }
        else if (inventorySchema instanceof EquipmentSchema) {
            InventoryActivityData.getInstance().setEquipmentSchema(inventorySchema);
            InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.VIEW);
            //Start Activity
            Intent intent = new Intent(getActivity(), AddEditViewEquipment.class);
            startActivity(intent);
        }
        else if (inventorySchema instanceof OtherSchema) {
            InventoryActivityData.getInstance().setOtherSchema(inventorySchema);
            InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.VIEW);
            //Start Activity
            Intent intent = new Intent(getActivity(), AddEditViewOther.class);
            startActivity(intent);
        }
    }

    private void addNewBrew(InventoryCategories aInventoryCategories) {

        if(aInventoryCategories ==InventoryCategories.Hops )
        {
            if(isBrewDisplay) {
                //Set Selected Inventory in Activity Data
                InventoryActivityData.getInstance().setHopsSchema(null);
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.BREW_ADD);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewHops.class);
                startActivity(intent);
            }
        }
        else if(aInventoryCategories ==InventoryCategories.Fermentables )
        {
            if(isBrewDisplay) {
                //Set Selected Inventory in Activity Data
                InventoryActivityData.getInstance().setFermentablesSchema(null);
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.BREW_ADD);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewFermentables.class);
                startActivity(intent);
            }
        }
        else if(aInventoryCategories ==InventoryCategories.Yeast )
        {
            if(isBrewDisplay) {

                //Set Selected Inventory in Activity Data
                InventoryActivityData.getInstance().setYeastSchema(null);
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.BREW_ADD);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewYeast.class);
                startActivity(intent);
            }
        }
        else if(aInventoryCategories ==InventoryCategories.Equipment )
        {
            if(isBrewDisplay){
                //Set Selected Inventory in Activity Data
                InventoryActivityData.getInstance().setEquipmentSchema(null);
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.BREW_ADD);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewEquipment.class);
                startActivity(intent);
            }
        }
        else if(aInventoryCategories ==InventoryCategories.Other )
        {
            if(isBrewDisplay){
                //Set Selected Inventory in Activity Data
                InventoryActivityData.getInstance().setOtherSchema(null);
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.BREW_ADD);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewOther.class);
                startActivity(intent);
            }
        }

    }

    private void addToBrewDialog(InventoryCategories aInventoryCategories)
    {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(aInventoryCategories.toString());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_inventory_add_dialog, null);
        alertDialogBuilder.setView(dialogView);

        final ListView inventoryAddListView = (ListView) dialogView.findViewById(R.id.inventoryAddListView);
        final List<InventorySchema> inventorySchemas;

        if(aInventoryCategories == InventoryCategories.Hops)
            inventorySchemas = InventoryMemory.getInstance().getHopsSchemas();
        else if(aInventoryCategories == InventoryCategories.Fermentables)
            inventorySchemas = InventoryMemory.getInstance().getFermentablesSchemas();
        else if(aInventoryCategories == InventoryCategories.Yeast)
            inventorySchemas = InventoryMemory.getInstance().getYeastSchemas();
        else if(aInventoryCategories == InventoryCategories.Equipment)
            inventorySchemas = InventoryMemory.getInstance().getEquipmentSchemas();
        else if(aInventoryCategories == InventoryCategories.Other)
            inventorySchemas = InventoryMemory.getInstance().getOtherSchemas();
        else
            inventorySchemas= new ArrayList<InventorySchema>();

        //instantiate custom adapter
        final CustomInventoryListAdapter adapter = new CustomInventoryListAdapter(inventorySchemas, getActivity());

        inventoryAddListView.setAdapter(adapter);

        final List<InventorySchema> selectedInventorySchemas = new ArrayList<InventorySchema>();
        inventoryAddListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(selectedInventorySchemas.contains(inventorySchemas.get(i)))
                {
                    view.setBackgroundColor(Color.TRANSPARENT);
                    selectedInventorySchemas.remove(inventorySchemas.get(i));
                }
                else
                {
                    view.setBackgroundColor(getResources().getColor(R.color.AccentColor));
                    selectedInventorySchemas.add(inventorySchemas.get(i));
                }
            }
        });

        if(inventorySchemas.size() <= 0)
        {
            TextView noInventoryTextView = (TextView) dialogView.findViewById(R.id.noInventoryTextView);
            noInventoryTextView.setText("No "+ aInventoryCategories.toString() +" Created");
            noInventoryTextView.setVisibility(View.VISIBLE);
        }

        final AlertDialog alertDialog = alertDialogBuilder.create();
        final InventoryCategories inventoryCategoriesAdd = aInventoryCategories;


        Button ConfirmButton = (Button) dialogView.findViewById(R.id.confirmButton);
        ConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFromList(inventoryCategoriesAdd, selectedInventorySchemas);
                alertDialog.cancel();
            }
        });
        Button addNew = (Button) dialogView.findViewById(R.id.addNewButton);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewBrew(inventoryCategoriesAdd);
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

    private void AddFromList(InventoryCategories aInventoryCategories, List<InventorySchema> selectedInventorySchemas)
    {
        if(isBrewDisplay && !(BrewActivityData.getInstance().getAddEditViewBrew().getBrewId() >= 0))
        {
            for (InventorySchema inventorySchema : selectedInventorySchemas) {
                BrewActivityData.getInstance().getBrewInventorySchemaList().add(inventorySchema);
            }
        }
        else
        {
            for (InventorySchema inventorySchema : selectedInventorySchemas) {
                inventorySchema.setBrewId(BrewActivityData.getInstance().getAddEditViewBrew().getBrewId());

                if (aInventoryCategories == InventoryCategories.Hops)
                    dbManager.CreateHops((HopsSchema) inventorySchema);
                else if (aInventoryCategories == InventoryCategories.Fermentables)
                    dbManager.CreateFermentable((FermentablesSchema) inventorySchema);
                else if (aInventoryCategories == InventoryCategories.Yeast)
                    dbManager.CreateYeast((YeastSchema) inventorySchema);
                else if (aInventoryCategories == InventoryCategories.Equipment)
                    dbManager.CreateEquipment((EquipmentSchema) inventorySchema);
                else if (aInventoryCategories == InventoryCategories.Other)
                    dbManager.CreateOther((OtherSchema) inventorySchema);
            }
        }

        SetUpInventoryList();
    }

    private void searchButtonClick()
    {
        //Create and intent which will open next activity UserProfile
        Intent intent = new Intent(getActivity(), InventorySearch.class);

        //start next activity
        startActivity(intent);
    }

}
