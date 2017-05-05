package com.dev.town.small.brewtopia.Inventory;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dev.town.small.brewtopia.Brews.BrewActivityData;
import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.DataClass.CurrentUser;
import com.dev.town.small.brewtopia.DataClass.EquipmentSchema;
import com.dev.town.small.brewtopia.DataClass.FermentablesSchema;
import com.dev.town.small.brewtopia.DataClass.HopsSchema;
import com.dev.town.small.brewtopia.DataClass.InventorySchema;
import com.dev.town.small.brewtopia.DataClass.OtherSchema;
import com.dev.town.small.brewtopia.DataClass.YeastSchema;
import com.dev.town.small.brewtopia.R;
import com.dev.town.small.brewtopia.SharedMemory.InventoryMemory;

import java.util.ArrayList;
import java.util.List;

public class InventorySearch extends ActionBarActivity {

    private SearchView categorySearchView;
    private ListView searchListView;
    private Spinner categorySpinner;
    private Button addButton;
    private ArrayAdapter<String> categoryAdapter;
    private List<InventorySchema> searchList;
    private List<InventorySchema> mainSearchList;
    private CustomInventoryListAdapter adapter;
    private List<Integer> selectedList;
    private DataBaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_search);

        Toolbar toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Search Inventory");

        dbManager = DataBaseManager.getInstance(this);
        selectedList = new ArrayList<>();

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFromList();
            }
        });

        categorySearchView = (SearchView) findViewById(R.id.categorySearchView);
        categorySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                LoadSearch(s);
                return false;
            }
        });
        searchListView = (ListView) findViewById(R.id.searchListView);


        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InventorySchema selectedRow = searchList.get(i);
                onInventoryClick(selectedRow);
            }
        });
        searchListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddSelected(i);
                return true;
            }
        });

        categorySpinner = (Spinner)  findViewById(R.id.categorySpinner);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toggleSearch(categorySpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setCategorySpinner();
        toggleSearch(categorySpinner.getSelectedItem().toString());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        toggleSearch(categorySpinner.getSelectedItem().toString());
        LoadSearch(categorySearchView.getQuery().toString());
    }



    private void setCategorySpinner()
    {
        List<String> categories = new ArrayList<>();
        categories.add("None");
        for(UserInventory.InventoryCategories inventoryCategories :  UserInventory.InventoryCategories.values()) {
            categories.add(inventoryCategories.toString());
        }

        categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories); //selected item will look like a spinner set from XML
        // Specify the layout to use when the list of choices appears
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(categoryAdapter);
    }

    private void LoadInventoryList(List<InventorySchema> aInventorySchemaList) {

        searchList= new ArrayList<>();
        searchList.addAll(aInventorySchemaList);

        //instantiate custom adapter
        adapter = new CustomInventoryListAdapter(searchList, this);
        adapter.setDeleteView(false);
        adapter.hasColor(true);

        searchListView.setAdapter(adapter);
    }

    private void toggleSearch(String aCategorySelected)
    {

        //clear selected
        ClearSelectedList();

        if(aCategorySelected.equals("None")) {
            categorySearchView.setVisibility(View.GONE);
            LoadInventoryList(new ArrayList<InventorySchema>());
            return;
        }

        categorySearchView.setVisibility(View.VISIBLE);

        mainSearchList = new ArrayList<>();

        if(aCategorySelected.equals(UserInventory.InventoryCategories.Hops.toString())) {
            mainSearchList.addAll(InventoryMemory.getInstance().getHopsSchemas());
            mainSearchList.addAll(dbManager.getAllHopsByUserId(CurrentUser.getInstance().getUser().getUserId()));
            LoadInventoryList(mainSearchList);
        }
        else if(aCategorySelected.equals(UserInventory.InventoryCategories.Fermentables.toString())) {
            mainSearchList.addAll(InventoryMemory.getInstance().getFermentablesSchemas());
            mainSearchList.addAll(dbManager.getAllFermentablesByUserId(CurrentUser.getInstance().getUser().getUserId()));
            LoadInventoryList(mainSearchList);
        }
        else if(aCategorySelected.equals(UserInventory.InventoryCategories.Yeast.toString())) {
            mainSearchList.addAll(InventoryMemory.getInstance().getYeastSchemas());
            mainSearchList.addAll(dbManager.getAllYeastByUserId(CurrentUser.getInstance().getUser().getUserId()));
            LoadInventoryList(mainSearchList);
        }
        else if(aCategorySelected.equals(UserInventory.InventoryCategories.Equipment.toString())) {
            mainSearchList.addAll(InventoryMemory.getInstance().getEquipmentSchemas());
            mainSearchList.addAll(dbManager.getAllEquipmentByUserId(CurrentUser.getInstance().getUser().getUserId()));
            LoadInventoryList(mainSearchList);
        }
        else if(aCategorySelected.equals(UserInventory.InventoryCategories.Other.toString())) {
            mainSearchList.addAll(InventoryMemory.getInstance().getOtherSchemas());
            mainSearchList.addAll(dbManager.getAllOtherByUserId(CurrentUser.getInstance().getUser().getUserId()));
            LoadInventoryList(mainSearchList);
        }
    }

    private void LoadSearch(String searchText) {

        //clear selected
        ClearSelectedList();

        if (searchList == null || mainSearchList == null)
            return;

        searchList.removeAll(searchList);
        searchList.addAll(mainSearchList);
        for(InventorySchema is : mainSearchList)
        {
            if(!is.getInventoryName().toUpperCase().contains(searchText.toUpperCase()))
                searchList.remove(is);
        }

        //update adapter
        adapter.notifyDataSetChanged();
    }

    private void onInventoryClick(InventorySchema inventorySchema)
    {
        if (inventorySchema instanceof HopsSchema) {
            InventoryActivityData.getInstance().setHopsSchema(inventorySchema);
            InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.VIEW_ONLY);
            //Start Activity
            Intent intent = new Intent(this, AddEditViewHops.class);
            startActivity(intent);
        }
        else if (inventorySchema instanceof FermentablesSchema) {
            InventoryActivityData.getInstance().setFermentablesSchema(inventorySchema);
            InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.VIEW_ONLY);
            //Start Activity
            Intent intent = new Intent(this, AddEditViewFermentables.class);
            startActivity(intent);
        }
        else if (inventorySchema instanceof YeastSchema) {
            InventoryActivityData.getInstance().setYeastSchema(inventorySchema);
            InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.VIEW_ONLY);
            //Start Activity
            Intent intent = new Intent(this, AddEditViewYeast.class);
            startActivity(intent);
        }
        else if (inventorySchema instanceof EquipmentSchema) {
            InventoryActivityData.getInstance().setEquipmentSchema(inventorySchema);
            InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.VIEW_ONLY);
            //Start Activity
            Intent intent = new Intent(this, AddEditViewEquipment.class);
            startActivity(intent);
        }
        else if (inventorySchema instanceof OtherSchema) {
            InventoryActivityData.getInstance().setOtherSchema(inventorySchema);
            InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.VIEW_ONLY);
            //Start Activity
            Intent intent = new Intent(this, AddEditViewOther.class);
            startActivity(intent);
        }
    }

    private void ClearSelectedList()
    {
        if(adapter == null)
            return;

        selectedList = new ArrayList<>();

        addButton.setVisibility(View.GONE);

        adapter.setSelected(selectedList);
        adapter.notifyDataSetChanged();
    }

    private void AddSelected(int index)
    {
        if(adapter == null)
            return;

        //dont add dups
        boolean dupFound  = false;
        for(Integer i : selectedList) {
            if (i == index) {
                selectedList.remove(i);
                dupFound = true;
                break;
            }
        }

        if(!dupFound)
            selectedList.add(index);

        if(selectedList.size() > 0) {
            addButton.setVisibility(View.VISIBLE);
            addButton.setText("Add (" + selectedList.size() + ")");
        }
        else
            addButton.setVisibility(View.GONE);

        adapter.setSelected(selectedList);
        adapter.notifyDataSetChanged();
    }

    private void AddFromList()
    {

        List<InventorySchema> tempList  = new ArrayList<>();

        for (Integer i  : selectedList) {
            tempList.add(searchList.get(i));
        }

        for (InventorySchema inventorySchema : tempList) {

            inventorySchema.setInventoryId(0);
            if(InventoryActivityData.getInstance().isUser())  inventorySchema.setBrewId(0);
            else inventorySchema.setBrewId(BrewActivityData.getInstance().getAddEditViewBrew().getBrewId());
            inventorySchema.setUserId(CurrentUser.getInstance().getUser().getUserId());

            if (categorySpinner.getSelectedItem().toString().equals(UserInventory.InventoryCategories.Hops.toString()))
                dbManager.CreateHops((HopsSchema) inventorySchema);
            else if (categorySpinner.getSelectedItem().toString().equals(UserInventory.InventoryCategories.Fermentables.toString()))
                dbManager.CreateFermentable((FermentablesSchema) inventorySchema);
            else if (categorySpinner.getSelectedItem().toString().equals(UserInventory.InventoryCategories.Yeast.toString()))
                dbManager.CreateYeast((YeastSchema) inventorySchema);
            else if (categorySpinner.getSelectedItem().toString().equals(UserInventory.InventoryCategories.Equipment.toString()))
                dbManager.CreateEquipment((EquipmentSchema) inventorySchema);
            else if (categorySpinner.getSelectedItem().toString().equals(UserInventory.InventoryCategories.Other.toString()))
                dbManager.CreateOther((OtherSchema) inventorySchema);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
