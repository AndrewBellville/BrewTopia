package com.dev.town.small.brewtopia.Brews;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dev.town.small.brewtopia.DataBase.DataBaseManager;
import com.dev.town.small.brewtopia.Login;
import com.dev.town.small.brewtopia.R;

import java.util.*;

import com.dev.town.small.brewtopia.DataClass.*;
import com.dev.town.small.brewtopia.Schedule.ScheduleActivityData;
import com.dev.town.small.brewtopia.Schedule.UserSchedule;


public class UserCompletedBrewList extends Fragment {

    // Log cat tag
    private static final String LOG = "UserCompletedBrewList";

    private long userId;
    private ListView CompletedBrewListView;
    private TextView NoData;
    private DataBaseManager dbManager;
    List<ScheduledBrewSchema> completedbrewList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_user_comp_brew_list,container,false);
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onCreate");
        try {
            userId = CurrentUser.getInstance().getUser().getUserId();
        }
        catch (Exception e){
            // if  we fail to get user name open login activity
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }

        NoData = (TextView)view.findViewById(R.id.NoData);
        CompletedBrewListView = (ListView)view.findViewById(R.id.CompletedBrewList);
        CompletedBrewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ScheduledBrewSchema selectedRow = completedbrewList.get(position);
                BrewSelect(selectedRow);
            }
        });
        dbManager = DataBaseManager.getInstance(getActivity());

        LoadCompletedBrews();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onResume");
        LoadCompletedBrews();
    }

    private void LoadCompletedBrews() {
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: LoadCompletedBrews");

        if(BrewActivityData.getInstance().getAddEditViewState() != BrewActivityData.DisplayMode.GLOBAL)
            completedbrewList = dbManager.getAllNonActiveScheduledBrews(BrewActivityData.getInstance().getAddEditViewBrew().getBrewId());
        else
            completedbrewList = new ArrayList<ScheduledBrewSchema>();

        if (completedbrewList.size() > 0) {
            //instantiate custom adapter
            CustomCompListAdapter adapter = new CustomCompListAdapter(completedbrewList, getActivity());
            adapter.setDeleteView(false);
            adapter.hasColor(false);
            CompletedBrewListView.setAdapter(adapter);
            NoData.setVisibility(View.GONE);
        }
        else
        {
            NoData.setVisibility(View.VISIBLE);
        }
    }

    private void BrewSelect(ScheduledBrewSchema aSchedule)
    {
        Intent intent = new Intent(getActivity(), UserSchedule.class);

        // Set the state of display if View brew cannot be null
        ScheduleActivityData.getInstance().setAddEditViewState(ScheduleActivityData.DisplayMode.COMPLETE);
        ScheduleActivityData.getInstance().setScheduledBrewSchema(aSchedule);

        //start next activity
        startActivity(intent);
    }
}
