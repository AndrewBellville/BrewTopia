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

import com.android.volley.Response;
import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.Login;
import com.dev.town.small.brewtopia.R;

import java.util.*;

import com.dev.town.small.brewtopia.DataClass.*;
import com.dev.town.small.brewtopia.WebAPI.DeleteBrewRequest;
import com.dev.town.small.brewtopia.WebAPI.WebController;


public class UserBrewList extends Fragment {

    // Log cat tag
    private static final String LOG = "UserBrews";

    private long userId;
    private ListView BrewListView;
    private DataBaseManager dbManager;
    private boolean isDelete = false;
    private List<BrewSchema> brewList;
    private List<BrewSchema> searchBrewList;
    private TextView noData;
    private ImageView addBrewImage;
    private CustomBListAdapter adapter;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_user_brew_list,container,false);
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onCreate");
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
                BrewSchema selectedRow = searchBrewList.get(position);
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

        searchView = (SearchView) view.findViewById(R.id.searchView);
        //added so keyboard doesn't popup
        searchView.setFocusable(false);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                LoadSearchBrews(s);
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
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onResume");
        LoadBrews();
        LoadSearchBrews(searchView.getQuery().toString());
    }

    private void LoadBrews() {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: LoadBrews");

        brewList = dbManager.getAllBrews(userId);

        searchBrewList = new ArrayList<>();
        searchBrewList.addAll(brewList);

        if (brewList.size() > 0) {

            BrewListView.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);

            //instantiate custom adapter
            adapter = new CustomBListAdapter(searchBrewList, getActivity());
            adapter.setDeleteView(isDelete);
            adapter.hasColor(true);
            adapter.setEventHandler(new CustomBListAdapter.EventHandler() {
                @Override
                public void OnDeleteClickListener(BrewSchema aBrewSchema) {
                    DeleteBrew(aBrewSchema.getBrewId(),aBrewSchema.getIsNew());
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
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: LoadSearchBrews "+ searchText);

        //search current brewlist for Brew names containing search text
        searchBrewList.removeAll(searchBrewList);
        searchBrewList.addAll(brewList);
        for(BrewSchema bs : brewList)
        {
            if(!bs.getBrewName().toUpperCase().contains(searchText.toUpperCase()))
                searchBrewList.remove(bs);
        }

        if (searchBrewList.size() > 0)
            noData.setVisibility(View.GONE);
        else
            noData.setVisibility(View.VISIBLE);

        //update adapter
        adapter.notifyDataSetChanged();
    }

    private void BrewSelect(BrewSchema aBrew) {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: BrewSelect" + aBrew.getBrewName());

        Intent intent = new Intent(getActivity(), UserBrew.class);

        // Set the state of display if View brew cannot be null
        BrewActivityData.getInstance().setViewStateAndBrew(BrewActivityData.DisplayMode.VIEW, aBrew);

        //start next activity
        startActivity(intent);
    }

    public void onCreateClick()
    {
        if(APPUTILS.isLogging) Log.e(LOG, "Entering: onCreateClick");

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

    private void DeleteBrew(Long aBrewId, int IsNew)
    {
        if(IsNew == 1)
        {
            dbManager.DeleteBrew(aBrewId,CurrentUser.getInstance().getUser().getUserId());
            LoadBrews();
            return;
        }

        if(!APPUTILS.HasInternet(getActivity())) {
            Toast.makeText(getActivity(), "Need Internet To Perform", Toast.LENGTH_SHORT).show();
            return;
        }

        Response.Listener<String> ResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(APPUTILS.isLogging)Log.e(LOG, response);
                if (!response.equals("Error"))// no match for user exists
                {
                    dbManager.DeleteBrew(Long.parseLong(response.trim()),CurrentUser.getInstance().getUser().getUserId());
                    Toast.makeText(getActivity(),"Delete Successful", Toast.LENGTH_SHORT).show();
                    LoadBrews();
                }
                else
                {
                    Toast.makeText(getActivity(),"Delete Failed", Toast.LENGTH_SHORT).show();
                }
            }
        };


        DeleteBrewRequest deleteBrewRequest = new DeleteBrewRequest(Long.toString(aBrewId), ResponseListener,null);
        WebController.getInstance().addToRequestQueue(deleteBrewRequest);

    }
}
