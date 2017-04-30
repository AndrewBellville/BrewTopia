package com.dev.town.small.brewtopia.Global;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev.town.small.brewtopia.Brews.BrewActivityData;
import com.dev.town.small.brewtopia.Brews.CustomBListAdapter;
import com.dev.town.small.brewtopia.Brews.UserBrew;
import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.DataClass.BrewSchema;
import com.dev.town.small.brewtopia.R;
import com.dev.town.small.brewtopia.WebAPI.GlobalBrewsRequest;
import com.dev.town.small.brewtopia.WebAPI.JSONBrewParser;
import com.dev.town.small.brewtopia.WebAPI.WebController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserGlobal extends Fragment {

    // Log cat tag
    private static final String LOG = "UserGlobal";
    private ListView GlobalbrewListView;
    private TextView noData;
    private List<BrewSchema> GlobalBrewList;
    private List<BrewSchema> searchGlobalBrewList;
    private SwipeRefreshLayout swipeContainer;
    CustomBListAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_user_global,container,false);

        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onCreate");

        GlobalbrewListView = (ListView)view.findViewById(R.id.GlobalBrewList);
        GlobalbrewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                BrewSchema selectedRow = searchGlobalBrewList.get(position);
                BrewSelect(selectedRow);
            }
        });

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshBrews();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        SearchView searchView = (SearchView) view.findViewById(R.id.searchView);
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
                LoadSearchedGlobalBrews(s);
                return false;
            }
        });

        noData = (TextView) view.findViewById(R.id.GlobalBrewsNoData);

        GetGlobalBrews();

        return view;
    }

    private void RefreshBrews(){
        GetGlobalBrews();
    }

    private void GetGlobalBrews()
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: GetGlobalBrews");

        Response.Listener<String> ResponseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(APPUTILS.isLogging)Log.d(LOG, response.toString());
                try {
                    JSONArray jsonArray = new JSONArray("["+response+"]");
                    JSONObject JSONResponse = (JSONObject) jsonArray.get(0);
                    if (JSONResponse.getString("status").equals("Error"))// no match for user exists
                    {
                        DisplayError(JSONResponse.getString("message"));
                    } else {

                        //Get respose and  parse JSON into BrewSchema
                        LoadGlobalBrews(new JSONBrewParser(getActivity()).ParseGlobalBrews(new JSONArray(JSONResponse.getString("message"))));
                        // Now we call setRefreshing(false) to signal refresh has finished
                        swipeContainer.setRefreshing(false);
                    }
                }
                catch (JSONException e) {
                    DisplayError("Brew Data Load Failed");
                }
            }
        };

        //If error
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(APPUTILS.isLogging) Log.e(LOG, error.toString());
            }
        };

        GlobalBrewsRequest globalBrewsRequest = new GlobalBrewsRequest(ResponseListener,errorListener);
        WebController.getInstance().addToRequestQueue(globalBrewsRequest);
    }

    private void LoadGlobalBrews(List<BrewSchema> brewSchemaList) {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: LoadGlobalBrews");

        GlobalBrewList = brewSchemaList;
        searchGlobalBrewList= new ArrayList<>();
        searchGlobalBrewList.addAll(brewSchemaList);

        if (GlobalBrewList.size() > 0)
            noData.setVisibility(View.GONE);
        else
            noData.setVisibility(View.VISIBLE);

        //instantiate custom adapter
        adapter = new CustomBListAdapter(searchGlobalBrewList, getActivity());
        adapter.setDeleteView(false);
        adapter.hasColor(true);

        GlobalbrewListView.setAdapter(adapter);

    }

    private void LoadSearchedGlobalBrews(String searchText) {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: LoadSearchedGlobalBrews");

        //search current brewlist for Brew names containing search text
        searchGlobalBrewList.removeAll(searchGlobalBrewList);
        searchGlobalBrewList.addAll(GlobalBrewList);
        for(BrewSchema bs : GlobalBrewList)
        {
            if(!bs.getBrewName().toUpperCase().contains(searchText.toUpperCase()))
                searchGlobalBrewList.remove(bs);
        }

        if (searchGlobalBrewList.size() > 0)
            noData.setVisibility(View.GONE);
        else
            noData.setVisibility(View.VISIBLE);

        //update adapter
        adapter.notifyDataSetChanged();

    }

    private void BrewSelect(BrewSchema aBrew)
    {
        Intent intent = new Intent(getActivity(), UserBrew.class);

        // Set the state of display if View brew cannot be null
        BrewActivityData.getInstance().setViewStateAndBrew(BrewActivityData.DisplayMode.GLOBAL,aBrew);

        //start next activity
        startActivity(intent);
    }

    private void DisplayError(String errorText)
    {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: DisplayError");

        swipeContainer.setRefreshing(false);

        Toast.makeText(getActivity(), errorText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onResume");
    }
}
