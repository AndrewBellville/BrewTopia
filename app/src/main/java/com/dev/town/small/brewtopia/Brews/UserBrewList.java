package com.dev.town.small.brewtopia.Brews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.Login;
import com.dev.town.small.brewtopia.R;

import java.util.*;

import com.dev.town.small.brewtopia.DataClass.*;



public class UserBrewList extends Fragment {

    // Log cat tag
    private static final String LOG = "UserBrews";

    private long userId;
    private ListView BrewListView;
    private DataBaseManager dbManager;
    private boolean isDelete = false;
    List<BrewSchema> brewList;
    private TextView noData;
    private ImageView addBrewImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_user_brew_list,container,false);
        Log.e(LOG, "Entering: onCreate");
        try {
            userId = CurrentUser.getInstance().getUser().getUserId();
        }
        catch (Exception e){
            // if  we fail to get user name open login activity
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }

        BrewListView = (ListView)view.findViewById(R.id.BrewList);
        BrewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                BrewSchema selectedRow = brewList.get(position);
                BrewSelect(selectedRow);
            }
        });
        dbManager = DataBaseManager.getInstance(getActivity());

        addBrewImage = (ImageView) view.findViewById(R.id.AddBrewImage);
        addBrewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getActivity(), addBrewImage);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_add_brew_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getTitle().equals("Create Brew"))
                            onCreateClick();
                        else if(item.getTitle().equals("Delete Brew"))
                            onDeleteClick();

                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        SearchView searchView = (SearchView) view.findViewById(R.id.searchView);
        //added so keyboard doesn't popup
        searchView.setFocusable(false);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                LoadSearchBrews(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                if(s.equals("")) LoadBrews();
                return false;
            }
        });

        noData = (TextView) view.findViewById(R.id.GlobalBrewsNoData);

        //If temp user hide buttons
        if(CurrentUser.getInstance().getUser().isTemp())
        {
            addBrewImage.setVisibility(View.INVISIBLE);
        }

        LoadBrews();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e(LOG, "Entering: onResume");
        LoadBrews();
    }

    private void LoadBrews() {
        Log.e(LOG, "Entering: LoadBrews");

        brewList = dbManager.getAllBrews(userId);

        if (brewList.size() > 0) {

            BrewListView.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);

            //instantiate custom adapter
            CustomBListAdapter adapter = new CustomBListAdapter(brewList, getActivity());
            adapter.setDeleteView(isDelete);
            adapter.hasColor(true);
            adapter.setEventHandler(new CustomBListAdapter.EventHandler() {
                @Override
                public void OnDeleteClickListener(BrewSchema aBrewSchema) {
                    dbManager.DeleteBrew(aBrewSchema.getBrewId(), userId);
                }
            });
            BrewListView.setAdapter(adapter);
        }
        else
        {
            BrewListView.setVisibility(View.INVISIBLE);
            noData.setVisibility(View.VISIBLE);
        }
    }

    private void LoadSearchBrews(String searchText) {
        Log.e(LOG, "Entering: LoadSearchBrews "+ searchText);

        //search current brewlist for Brew names containing search text
        List<BrewSchema> tempBrewList = new ArrayList<BrewSchema>();
        for(BrewSchema bs : brewList)
        {
            if(bs.getBrewName().toUpperCase().contains(searchText.toUpperCase()))
                tempBrewList.add(bs);
        }

        if (tempBrewList.size() > 0) {

            BrewListView.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);

            //instantiate custom adapter
            CustomBListAdapter adapter = new CustomBListAdapter(tempBrewList, getActivity());
            adapter.setDeleteView(isDelete);
            adapter.hasColor(true);
            adapter.setEventHandler(new CustomBListAdapter.EventHandler() {
                @Override
                public void OnDeleteClickListener(BrewSchema aBrewSchema) {
                    dbManager.DeleteBrew(aBrewSchema.getBrewId(), userId);
                }
            });
            BrewListView.setAdapter(adapter);
        }
        else
        {
            BrewListView.setVisibility(View.INVISIBLE);
            noData.setVisibility(View.VISIBLE);
        }
    }

    private void BrewSelect(BrewSchema aBrew) {
        Log.e(LOG, "Entering: BrewSelect" + aBrew.getBrewName());

        Intent intent = new Intent(getActivity(), UserBrew.class);

        // Set the state of display if View brew cannot be null
        BrewActivityData.getInstance().setViewStateAndBrew(BrewActivityData.DisplayMode.VIEW, aBrew);

        //start next activity
        startActivity(intent);
    }

    public void onCreateClick()
    {
        Log.e(LOG, "Entering: onCreateClick");

        Intent intent = new Intent(getActivity(), UserBrew.class);

        // Set the state of display if Add brew can be null
        BrewActivityData.getInstance().setViewStateAndBrew(BrewActivityData.DisplayMode.ADD,new BrewSchema());

        //start next activity
        startActivity(intent);
    }

    public void onDeleteClick() {
        isDelete = !isDelete;

        LoadBrews();
    }
}
