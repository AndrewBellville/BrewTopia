package com.town.small.brewtopia.Inventory;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.town.small.brewtopia.DataClass.HopsSchema;
import com.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UserInventory extends Fragment {


    //Temp List
    ExpandableListView expListView;
    CustomExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    //Hops List
    ExpandableListView expHopsListView;
    CustomHopsExpandableListAdapter HopsListAdapter;
    List<String> HopsListDataHeader;
    HashMap<String, List<HopsSchema>> HopsListDataChild;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_user_inventory,container,false);

        // get the listview
        expHopsListView = (ExpandableListView) view.findViewById(R.id.inventoryExpandableHopsList);
        expListView = (ExpandableListView) view.findViewById(R.id.inventoryExpandableList);

        SetUpHopsList();
        SetUpList();

        return view;
    }

    private void SetUpList()
    {
        // preparing list data
        prepareListData();

        listAdapter = new CustomExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                /*
                Toast.makeText(
                        getActivity(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                        */
                return false;
            }
        });
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*
                Toast.makeText(getActivity(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
                        */
            }
        });
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                /*
                Toast.makeText(getActivity(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();
                        */

            }
        });
    }

    private void SetUpHopsList()
    {
        // preparing list data
        prepareHopsListData();

        HopsListAdapter = new CustomHopsExpandableListAdapter(getActivity(), HopsListDataHeader, HopsListDataChild);
        HopsListAdapter.setEventHandler(new CustomHopsExpandableListAdapter.EventHandler() {
            @Override
            public void OnEditClick() {
                //Set Selected hops in Activity Data
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.ADD);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewHops.class);
                startActivity(intent);
            }
        });

        // setting list adapter
        expHopsListView.setAdapter(HopsListAdapter);
        expHopsListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

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
                //Set Selected hops in Activity Data
                InventoryActivityData.getInstance().setHopsSchema(HopsListDataChild.get(HopsListDataHeader.get(groupPosition)).get(childPosition));
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.VIEW);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewHops.class);
                startActivity(intent);

                return false;
            }
        });
        expHopsListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*
                Toast.makeText(getActivity(),
                        HopsListDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
                        */
            }
        });
        expHopsListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

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

    private void prepareHopsListData() {
        HopsListDataHeader = new ArrayList<String>();
        HopsListDataChild = new HashMap<String, List<HopsSchema>>();

        // Adding child data
        List<HopsSchema> hopsList = new ArrayList<HopsSchema>();
        HopsSchema hopsSchema = new HopsSchema();
        hopsSchema.setInventoryName("Test Hops Name");
        hopsSchema.setAmount(0.25);
        hopsSchema.setType("Pellet");
        hopsSchema.setUse("Boil");
        hopsSchema.setTime(45);
        hopsSchema.setIBU(15.16);
        hopsSchema.setAA("AA");
        hopsList.add(hopsSchema);

        // Adding child data
        HopsListDataHeader.add("Hops ("+hopsList.size()+")");

        HopsListDataChild.put(HopsListDataHeader.get(0), hopsList); // Header, Child data
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        List<String> fermentables = new ArrayList<String>();
        fermentables.add("Test fermentable 1");
        fermentables.add("Test fermentable 2");
        fermentables.add("Test fermentable 3");
        fermentables.add("Test fermentable 4");
        fermentables.add("Test fermentable 5");
        fermentables.add("Test fermentable 6");

        List<String> steepingGrains = new ArrayList<String>();
        steepingGrains.add("Test Steeping Grains 1");
        steepingGrains.add("Test Steeping Grains 2");
        steepingGrains.add("Test Steeping Grains 3");
        steepingGrains.add("Test Steeping Grains 4");
        steepingGrains.add("Test Steeping Grains 5");

        List<String> yeast = new ArrayList<String>();
        yeast.add("Test Yeast 1");
        yeast.add("Test Yeast 2");
        yeast.add("Test Yeast 3");

        List<String> equipment  = new ArrayList<String>();
        equipment.add("Test Equipment 1");
        equipment.add("Test Equipment 2");
        equipment.add("Test Equipment 3");
        equipment.add("Test Equipment 4");


        // Adding child data
        listDataHeader.add("Fermentables ("+fermentables.size()+")");
        listDataHeader.add("Steeping Grains ("+steepingGrains.size()+")");
        listDataHeader.add("Yeast ("+yeast.size()+")");
        listDataHeader.add("Equipment ("+equipment.size()+")");

        listDataChild.put(listDataHeader.get(0), fermentables); // Header, Child data
        listDataChild.put(listDataHeader.get(1), steepingGrains);
        listDataChild.put(listDataHeader.get(2), yeast);
        listDataChild.put(listDataHeader.get(3), equipment);
    }

}
