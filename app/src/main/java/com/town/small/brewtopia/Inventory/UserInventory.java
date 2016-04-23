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

import com.town.small.brewtopia.DataClass.CurrentUser;
import com.town.small.brewtopia.DataClass.DataBaseManager;
import com.town.small.brewtopia.DataClass.EquipmentSchema;
import com.town.small.brewtopia.DataClass.FermentablesSchema;
import com.town.small.brewtopia.DataClass.GrainsSchema;
import com.town.small.brewtopia.DataClass.HopsSchema;
import com.town.small.brewtopia.DataClass.YeastSchema;
import com.town.small.brewtopia.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UserInventory extends Fragment {

    //Hops List
    ExpandableListView expHopsListView;
    CustomHopsExpandableListAdapter HopsListAdapter;
    List<String> HopsListDataHeader;
    HashMap<String, List<HopsSchema>> HopsListDataChild;

    //Fermentables List
    ExpandableListView expFermentablesListView;
    CustomFermentablesExpandableListAdapter FermentablesListAdapter;
    List<String> FermentablesListDataHeader;
    HashMap<String, List<FermentablesSchema>> FermentablesListDataChild;

    //Grains List
    ExpandableListView expGrainsListView;
    CustomGrainsExpandableListAdapter GrainsListAdapter;
    List<String> GrainsListDataHeader;
    HashMap<String, List<GrainsSchema>> GrainsListDataChild;

    //Yeast List
    ExpandableListView expYeastListView;
    CustomYeastExpandableListAdapter YeastListAdapter;
    List<String> YeastListDataHeader;
    HashMap<String, List<YeastSchema>> YeastListDataChild;

    //Equipment List
    ExpandableListView expEquipmentListView;
    CustomEquipmentExpandableListAdapter EquipmentListAdapter;
    List<String> EquipmentListDataHeader;
    HashMap<String, List<EquipmentSchema>> EquipmentListDataChild;

    DataBaseManager dbManager;
    long userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_user_inventory,container,false);

        // get the listview
        expHopsListView = (ExpandableListView) view.findViewById(R.id.inventoryExpandableHopsList);
        expFermentablesListView = (ExpandableListView) view.findViewById(R.id.inventoryExpandableFermentablesList);
        expGrainsListView = (ExpandableListView) view.findViewById(R.id.inventoryExpandableGrainsList);
        expYeastListView = (ExpandableListView) view.findViewById(R.id.inventoryExpandableYeastList);
        expEquipmentListView = (ExpandableListView) view.findViewById(R.id.inventoryExpandableEquipmentList);

        dbManager = DataBaseManager.getInstance(getActivity());
        userId = CurrentUser.getInstance().getUser().getUserId();

        SetUpHopsList();
        SetUpFermentablesList();
        SetUpGrainsList();
        SetUpYeastList();
        SetUpEquipmentList();

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        SetUpHopsList();
        SetUpFermentablesList();
        SetUpGrainsList();
        SetUpYeastList();
        SetUpEquipmentList();
    }

    //***************************Hops*****************************************
    private void SetUpHopsList()
    {
        // preparing list data
        prepareHopsListData();

        HopsListAdapter = new CustomHopsExpandableListAdapter(getActivity(), HopsListDataHeader, HopsListDataChild);
        HopsListAdapter.setEventHandler(new CustomHopsExpandableListAdapter.EventHandler() {
            @Override
            public void OnEditClick() {
                //Set Selected hops in Activity Data
                InventoryActivityData.getInstance().setHopsSchema(null);
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
        List<HopsSchema> hopsList = dbManager.getAllHopsByUserId(userId);

        // Adding child data
        HopsListDataHeader.add("Hops ("+hopsList.size()+")");

        HopsListDataChild.put(HopsListDataHeader.get(0), hopsList); // Header, Child data
    }

    //***************************Fermentables*****************************************
    private void SetUpFermentablesList()
    {
        // preparing list data
        prepareFermentablesListData();

        FermentablesListAdapter = new CustomFermentablesExpandableListAdapter(getActivity(), FermentablesListDataHeader, FermentablesListDataChild);
        FermentablesListAdapter.setEventHandler(new CustomFermentablesExpandableListAdapter.EventHandler() {
            @Override
            public void OnEditClick() {
                //Set Selected hops in Activity Data
                InventoryActivityData.getInstance().setFermentablesSchema(null);
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.ADD);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewFermentables.class);
                startActivity(intent);
            }
        });

        // setting list adapter
        expFermentablesListView.setAdapter(FermentablesListAdapter);
        expFermentablesListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

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
                InventoryActivityData.getInstance().setFermentablesSchema(FermentablesListDataChild.get(FermentablesListDataHeader.get(groupPosition)).get(childPosition));
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.VIEW);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewFermentables.class);
                startActivity(intent);

                return false;
            }
        });
        expFermentablesListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*
                Toast.makeText(getActivity(),
                        HopsListDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
                        */
            }
        });
        expFermentablesListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

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

    private void prepareFermentablesListData() {
        FermentablesListDataHeader = new ArrayList<String>();
        FermentablesListDataChild = new HashMap<String, List<FermentablesSchema>>();

        // Adding child data
        List<FermentablesSchema> FermentablesList = dbManager.getAllFermentablesByUserId(userId);

        // Adding child data
        FermentablesListDataHeader.add("Fermentables ("+FermentablesList.size()+")");

        FermentablesListDataChild.put(FermentablesListDataHeader.get(0), FermentablesList); // Header, Child data
    }
    //***************************Grains*****************************************
    private void SetUpGrainsList()
    {
        // preparing list data
        prepareGrainsListData();

        GrainsListAdapter = new CustomGrainsExpandableListAdapter(getActivity(), GrainsListDataHeader, GrainsListDataChild);
        GrainsListAdapter.setEventHandler(new CustomGrainsExpandableListAdapter.EventHandler() {
            @Override
            public void OnEditClick() {
                //Set Selected hops in Activity Data
                InventoryActivityData.getInstance().setGrainsSchema(null);
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.ADD);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewGrains.class);
                startActivity(intent);
            }
        });

        // setting list adapter
        expGrainsListView.setAdapter(GrainsListAdapter);
        expGrainsListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

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
                InventoryActivityData.getInstance().setGrainsSchema(GrainsListDataChild.get(GrainsListDataHeader.get(groupPosition)).get(childPosition));
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.VIEW);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewGrains.class);
                startActivity(intent);

                return false;
            }
        });
        expGrainsListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*
                Toast.makeText(getActivity(),
                        HopsListDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
                        */
            }
        });
        expGrainsListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

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

    private void prepareGrainsListData() {
        GrainsListDataHeader = new ArrayList<String>();
        GrainsListDataChild = new HashMap<String, List<GrainsSchema>>();

        // Adding child data
        List<GrainsSchema> grainsList = dbManager.getAllGrainsByUserId(userId);

        // Adding child data
        GrainsListDataHeader.add("Grains ("+grainsList.size()+")");

        GrainsListDataChild.put(GrainsListDataHeader.get(0), grainsList); // Header, Child data
    }
    //***************************Yeast*****************************************
    private void SetUpYeastList()
    {
        // preparing list data
        prepareYeastListData();

        YeastListAdapter = new CustomYeastExpandableListAdapter(getActivity(), YeastListDataHeader, YeastListDataChild);
        YeastListAdapter.setEventHandler(new CustomYeastExpandableListAdapter.EventHandler() {
            @Override
            public void OnEditClick() {
                //Set Selected hops in Activity Data
                InventoryActivityData.getInstance().setYeastSchema(null);
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.ADD);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewYeast.class);
                startActivity(intent);
            }
        });

        // setting list adapter
        expYeastListView.setAdapter(YeastListAdapter);
        expYeastListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

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
                InventoryActivityData.getInstance().setYeastSchema(YeastListDataChild.get(YeastListDataHeader.get(groupPosition)).get(childPosition));
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.VIEW);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewYeast.class);
                startActivity(intent);

                return false;
            }
        });
        expYeastListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*
                Toast.makeText(getActivity(),
                        HopsListDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
                        */
            }
        });
        expYeastListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

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

    private void prepareYeastListData() {
        YeastListDataHeader = new ArrayList<String>();
        YeastListDataChild = new HashMap<String, List<YeastSchema>>();

        // Adding child data
        List<YeastSchema> yeastList = dbManager.getAllYeastByUserId(userId);

        // Adding child data
        YeastListDataHeader.add("Yeast ("+yeastList.size()+")");

        YeastListDataChild.put(YeastListDataHeader.get(0), yeastList); // Header, Child data
    }
    //***************************Equipment*****************************************
    private void SetUpEquipmentList()
    {
        // preparing list data
        prepareEquipmentListData();

        EquipmentListAdapter = new CustomEquipmentExpandableListAdapter(getActivity(), EquipmentListDataHeader, EquipmentListDataChild);
        EquipmentListAdapter.setEventHandler(new CustomEquipmentExpandableListAdapter.EventHandler() {
            @Override
            public void OnEditClick() {
                //Set Selected hops in Activity Data
                InventoryActivityData.getInstance().setEquipmentSchema(null);
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.ADD);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewEquipment.class);
                startActivity(intent);
            }
        });

        // setting list adapter
        expEquipmentListView.setAdapter(EquipmentListAdapter);
        expEquipmentListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

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
                InventoryActivityData.getInstance().setEquipmentSchema(EquipmentListDataChild.get(EquipmentListDataHeader.get(groupPosition)).get(childPosition));
                InventoryActivityData.getInstance().setAddEditViewState(InventoryActivityData.DisplayMode.VIEW);
                //Start Activity
                Intent intent = new Intent(getActivity(), AddEditViewEquipment.class);
                startActivity(intent);

                return false;
            }
        });
        expEquipmentListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*
                Toast.makeText(getActivity(),
                        HopsListDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
                        */
            }
        });
        expEquipmentListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

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

    private void prepareEquipmentListData() {
        EquipmentListDataHeader = new ArrayList<String>();
        EquipmentListDataChild = new HashMap<String, List<EquipmentSchema>>();

        // Adding child data
        List<EquipmentSchema> equipmentList = dbManager.getAllEquipmentByUserId(userId);

        // Adding child data
        EquipmentListDataHeader.add("Equipment ("+equipmentList.size()+")");

        EquipmentListDataChild.put(EquipmentListDataHeader.get(0), equipmentList); // Header, Child data
    }

}
