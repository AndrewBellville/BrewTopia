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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dev.town.small.brewtopia.Brews.BrewActivityData;
import com.dev.town.small.brewtopia.Brews.CustomBListAdapter;
import com.dev.town.small.brewtopia.Brews.UserBrew;
import com.dev.town.small.brewtopia.DataClass.BrewSchema;
import com.dev.town.small.brewtopia.R;
import com.dev.town.small.brewtopia.WebAPI.GlobalBrewsRequest;
import com.dev.town.small.brewtopia.WebAPI.JSONBrewParser;
import com.dev.town.small.brewtopia.WebAPI.WebController;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class UserGlobal extends Fragment {

    // Log cat tag
    private static final String LOG = "UserGlobal";
    private ListView GlobalbrewListView;
    private TextView noData;
    List<BrewSchema> GlobalBrewList;
    private SwipeRefreshLayout swipeContainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_user_global,container,false);

        Log.e(LOG, "Entering: onCreate");

        GlobalbrewListView = (ListView)view.findViewById(R.id.GlobalBrewList);
        GlobalbrewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                BrewSchema selectedRow = GlobalBrewList.get(position);
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
                LoadSerachedGlobalBrews(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                if(s.equals("")) GetGlobalBrews();
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
        Log.e(LOG, "Entering: GetGlobalBrews");

        Response.Listener<JSONArray> ResponseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(LOG, response.toString());
                //Get respose and  parse JSON into BrewSchema
                LoadGlobalBrews(new JSONBrewParser(getActivity(), JSONBrewParser.ParseType.PUSH).ParseGlobalBrews(response));
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }
        };

        //If error
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG, error.toString());
            }
        };

        GlobalBrewsRequest globalBrewsRequest = new GlobalBrewsRequest(ResponseListener,errorListener);
        WebController.getInstance().addToRequestQueue(globalBrewsRequest);
    }

    private void LoadGlobalBrews(List<BrewSchema> brewSchemaList) {
        Log.e(LOG, "Entering: LoadGlobalBrews");

        GlobalBrewList = brewSchemaList;

        if (GlobalBrewList.size() > 0) {
            noData.setVisibility(View.GONE);

            //instantiate custom adapter
            CustomBListAdapter adapter = new CustomBListAdapter(GlobalBrewList, getActivity());
            adapter.setDeleteView(false);
            adapter.hasColor(true);

            GlobalbrewListView.setAdapter(adapter);
        }
        else
        {
            noData.setVisibility(View.VISIBLE);
        }
    }

    private void LoadSerachedGlobalBrews(String searchText) {
        Log.e(LOG, "Entering: LoadSerachedGlobalBrews");

        //search current brewlist for Brew names containing search text
        List<BrewSchema> tempBrewList = new ArrayList<BrewSchema>();
        for(BrewSchema bs : GlobalBrewList)
        {
            if(bs.getBrewName().toUpperCase().contains(searchText.toUpperCase()))
                tempBrewList.add(bs);
        }

        if (tempBrewList.size() > 0) {
            noData.setVisibility(View.GONE);

            //instantiate custom adapter
            CustomBListAdapter adapter = new CustomBListAdapter(tempBrewList, getActivity());
            adapter.setDeleteView(false);
            adapter.hasColor(true);

            GlobalbrewListView.setAdapter(adapter);
        }
        else
        {
            noData.setVisibility(View.VISIBLE);
        }
    }

    private void BrewSelect(BrewSchema aBrew)
    {
        Intent intent = new Intent(getActivity(), UserBrew.class);

        // Set the state of display if View brew cannot be null
        BrewActivityData.getInstance().setViewStateAndBrew(BrewActivityData.DisplayMode.GLOBAL,aBrew);

        //start next activity
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e(LOG, "Entering: onResume");
    }
}
