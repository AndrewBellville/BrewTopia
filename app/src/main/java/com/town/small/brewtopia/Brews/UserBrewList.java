package com.town.small.brewtopia.Brews;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;

import com.town.small.brewtopia.CustomListAdapter;
import com.town.small.brewtopia.Login;
import com.town.small.brewtopia.R;

import java.util.*;

import com.town.small.brewtopia.DataClass.*;



public class UserBrewList extends Fragment {

    // Log cat tag
    private static final String LOG = "UserBrews";

    //intent message
    //public final static String EXTRA_MESSAGE = "com.town.small.brewtopia.Brews";

    private long userId;
    private ListView BrewListView;
    private DataBaseManager dbManager;
    private boolean isDelete = false;
    List<BrewSchema> brewList;

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
                BrewSelect(selectedRow.getBrewId());
            }
        });
        dbManager = DataBaseManager.getInstance(getActivity());

        Button createButton = (Button) view.findViewById(R.id.AddBrewButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateClick(view);
            }
        });

        RadioButton deleteButon = (RadioButton) view.findViewById(R.id.DeleteBrewRadioButton);
        deleteButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRadioButtonClicked(view);
            }
        });

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
    }

    private void BrewSelect(long aBrewId)
    {
            Intent intent = new Intent(getActivity(), UserBrew.class);

            // Set the state of display if View brew cannot be null
            BrewActivityData.getInstance().setViewStateAndBrew(BrewActivityData.DisplayMode.VIEW,dbManager.getBrew(aBrewId,userId));

            //start next activity
            startActivity(intent);
    }

    public void onCreateClick(View aView)
    {
        Log.e(LOG, "Entering: onCreateClick");

        Intent intent = new Intent(getActivity(), UserBrew.class);

        // Set the state of display if Add brew can be null
        BrewActivityData.getInstance().setViewStateAndBrew(BrewActivityData.DisplayMode.ADD,new BrewSchema());

        //start next activity
        startActivity(intent);
    }

    private String GetStyleColor(String styleName)
    {
        BrewStyleSchema brewStyleSchema = dbManager.getBrewsStylesByName(styleName);
        return brewStyleSchema.getBrewStyleColor();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        if(isDelete) ((RadioButton) view).setChecked(false);
        isDelete = ((RadioButton) view).isChecked();

        LoadBrews();
    }
}
