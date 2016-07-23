package com.town.small.brewtopia.Brews;

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

import com.town.small.brewtopia.DataBase.DataBaseManager;
import com.town.small.brewtopia.Login;
import com.town.small.brewtopia.R;

import java.util.*;

import com.town.small.brewtopia.DataClass.*;



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
        Log.e(LOG, "Entering: onCreate");
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
                BrewSelect(selectedRow.getScheduleId());
            }
        });
        dbManager = DataBaseManager.getInstance(getActivity());

        LoadCompletedBrews();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e(LOG, "Entering: onResume");
        LoadCompletedBrews();
    }

    private void LoadCompletedBrews() {
        Log.e(LOG, "Entering: LoadCompletedBrews");

        completedbrewList = dbManager.getAllNonActiveScheduledBrews(userId,BrewActivityData.getInstance().getAddEditViewBrew().getBrewId());

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

    private void BrewSelect(long aScheduleId)
    {
        Intent intent = new Intent(getActivity(), CompletedBrew.class);

        // Set the state of display if View brew cannot be null
        BrewActivityData.getInstance().setScheduleId(aScheduleId);

        //start next activity
        startActivity(intent);
    }
}
