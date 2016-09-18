package com.town.small.brewtopia.Global;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.town.small.brewtopia.Brews.BrewActivityData;
import com.town.small.brewtopia.Brews.CustomBListAdapter;
import com.town.small.brewtopia.Brews.UserBrew;
import com.town.small.brewtopia.DataClass.BrewSchema;
import com.town.small.brewtopia.R;
import com.town.small.brewtopia.WebAPI.GlobalBrewsRequest;
import com.town.small.brewtopia.WebAPI.JSONParser;
import com.town.small.brewtopia.WebAPI.WebController;

import org.json.JSONArray;

import java.util.List;

public class UserGlobal extends Fragment {

    // Log cat tag
    private static final String LOG = "UserGlobal";
    private ListView GlobalbrewListView;
    private TextView noData;
    List<BrewSchema> GlobalBrewList;


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

        noData = (TextView) view.findViewById(R.id.GlobalBrewsNoData);

        GetGlobalBrews();

        return view;
    }

    private void GetGlobalBrews()
    {
        Log.e(LOG, "Entering: GetGlobalBrews");

        Response.Listener<JSONArray> ResponseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(LOG, response.toString());
                //Get respose and  parse JSON into BrewSchema
                LoadGlobalBrews(new JSONParser(getActivity()).ParseGlobalBrews(response));
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

    private void BrewSelect(BrewSchema aBrew)
    {
        Intent intent = new Intent(getActivity(), UserBrew.class);

        // Set the state of display if View brew cannot be null
        BrewActivityData.getInstance().setViewStateAndBrew(BrewActivityData.DisplayMode.VIEW,aBrew);

        //start next activity
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e(LOG, "Entering: onResume");
    }
}
